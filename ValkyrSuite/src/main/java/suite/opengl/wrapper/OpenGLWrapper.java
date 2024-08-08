package suite.opengl.wrapper;

/**
 * @author ReverendDread on 12/9/2019
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */

import static org.lwjgl.opengl.AMDDebugOutput.glDebugMessageCallbackAMD;
import static org.lwjgl.opengl.GL11.GL_DONT_CARE;
import static org.lwjgl.opengl.KHRDebug.GL_DEBUG_SEVERITY_MEDIUM;
import static org.lwjgl.opengl.KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION;
import static org.lwjgl.opengl.KHRDebug.GL_DEBUG_SOURCE_API;
import static org.lwjgl.opengl.KHRDebug.GL_DEBUG_TYPE_PERFORMANCE;
import static org.lwjgl.opengl.KHRDebug.glDebugMessageCallback;
import static org.lwjgl.opengl.KHRDebug.glDebugMessageControl;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import javafx.scene.control.Tab;
import lombok.extern.slf4j.Slf4j;
import misc.CustomTab;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.stream.RenderStream;
import org.lwjgl.util.stream.StreamHandler;
import org.lwjgl.util.stream.StreamUtil;
import org.lwjgl.util.stream.StreamUtil.RenderStreamFactory;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import lombok.Getter;
import lombok.Setter;
import suite.controller.Selection;
import suite.opengl.RendererRepository;
import suite.opengl.camera.Camera;

/**
 * A class serving as a wrapper to render OpenGL in JavaFX.
 * @param <T> The render context (i.e what we're rendering).
 * @author Displee
 */
@Slf4j
public abstract class OpenGLWrapper<T> {

    /**
     * A queue of runnables which will be executed on the OpenGL thread.
     */
    private static final Queue<Runnable> QUEUE = new LinkedList<>();

    /**
     * The zoom factor.
     */
    private static final float ZOOM_FACTOR = 0.2F;

    /**
     * The interval used to calculate the FPS.
     */
    private static final long FPS_UPD_INTERVAL = (1000L * 1000L * 1000L);

    /**
     * If swing rendering should be used.
     */
    public static final boolean USE_SWING_RENDERING = false;

    /**
     * If swing rendering should be used.
     */
    public static final boolean RENDER_TEXTURES = true;

    /**
     * The render context.
     */
    @Getter
    @Setter
    protected T context;

    /**
     * The buffer.
     */
    private final Pbuffer pbuffer;

    /**
     * The render factory.
     */
    private StreamUtil.RenderStreamFactory renderStreamFactory;

    /**
     * The actual render buffer.
     */
    private RenderStream renderStream;

    /**
     * The property holding the current FPS.
     */
    protected final ReadOnlyIntegerWrapper fps;

    /**
     * The image view.
     */
    protected final ImageView view;

    /**
     * The image we are rendering.
     */
    protected WritableImage renderImage;

    /**
     * The FPS limit;
     */
    @Setter
    private int fpsLimit = 120;

    /**
     * If we are running.
     */
    private boolean running = true;

    /**
     * Camera used for the scene.
     */
    protected Camera camera;

    /**
     * The scale.
     */
    protected float scale = 1.0f;

    /**
     * The last known width of the {@code view}.
     */
    private double lastWidth;

    /**
     * The last known height of the {@code view}.
     */
    private double lastHeight;

    /**
     * The parent component of this wrapper.
     */
    @Getter private final CustomTab parent;

    /**
     *
     */
    @Getter private UUID uuid;

    /**
     * Create a new {@code GLWrapper} {@code Object}.
     * @param view The image view.
     */
    public OpenGLWrapper(final ImageView view, final CustomTab parent) {
        this.view = view;
        this.parent = parent;
        if ((Pbuffer.getCapabilities() & Pbuffer.PBUFFER_SUPPORTED) == 0) {
            throw new UnsupportedOperationException("Support for pbuffers is required.");
        }
        this.fps = new ReadOnlyIntegerWrapper(this, "fps", 0);
        try {
            //not sure if this pixel format is 100%
            pbuffer = new Pbuffer(1, 1, new PixelFormat(), null, null, new ContextAttribs().withDebug(true));
            pbuffer.makeCurrent();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
        final ContextCapabilities caps = GLContext.getCapabilities();
        if (caps.GL_KHR_debug) {
            glDebugMessageCallback(new KHRDebugCallback());
            glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DEBUG_SEVERITY_NOTIFICATION, null, false);//Buffer object mallocs
            glDebugMessageControl(GL_DEBUG_SOURCE_API, GL_DEBUG_TYPE_PERFORMANCE, GL_DEBUG_SEVERITY_MEDIUM, null, false);//Pixel-transfer synchronized with rendering
        } else if (caps.GL_AMD_debug_output) {
            glDebugMessageCallbackAMD(new AMDDebugOutputCallback());
        }
        this.renderStreamFactory = StreamUtil.getRenderStreamImplementation();
        this.renderStream = renderStreamFactory.create(getReadHandler(), 8, 2);
        this.uuid = parent.getUuid();
        RendererRepository.get().addWrapper(this);
        this.camera = new Camera();
        view.setOnMousePressed(event -> camera.position((float) event.getSceneX(), (float) event.getSceneY(), 0f));
        view.setOnMouseDragged(event -> camera.position((float) event.getSceneX(), (float) event.getSceneY(), 0f));
        view.setOnScroll(e -> {
            final double delta = e.getDeltaY();
            if (delta > 1.0F) {
                scale += ZOOM_FACTOR;
            } else if (delta < -1.0F && scale - ZOOM_FACTOR >= ZOOM_FACTOR) {
                scale -= ZOOM_FACTOR;
            }
            e.consume();
        });
    }

    /**
     * Initialize OpenGL.
     */
    public abstract void init();

    /**
     * Event called when the {@code view} is being resized.
     */
    protected void onResize() {

    }

    /**
     * The render loop.
     */
    protected abstract void render();

    /**
     * Use the argued nodes to notify updates.
     * @param nodes The nodes.
     */
    protected abstract void update(Node... nodes);

    /**
     * An event fired when this wrapper gets terminated.
     */
    protected abstract void onTerminate();

    /**
     * Run this wrapper.
     * @param nodes The update nodes.
     */
    public void run(Node... nodes) {

        if (USE_SWING_RENDERING) {
            try {
                Display.setDisplayMode(new DisplayMode(800, 600));
                Display.create();
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }

        init();

        long nextFPSUpdateTime = System.nanoTime() + FPS_UPD_INTERVAL;

        int frames = 0;

        while (running && !Selection.terminate) {

            Runnable runnable;
            while((runnable = QUEUE.poll()) != null) {
                runnable.run();
            }

            if (lastWidth != view.getFitWidth() || lastHeight != view.getFitHeight()) {
                onResize();
                lastWidth = view.getFitWidth();
                lastHeight = view.getFitHeight();
            }

            if (context != null) {
                renderStream.bind();
                render();
                renderStream.swapBuffers();
            }

            //capped to 100 FPS
            if (USE_SWING_RENDERING) {
                Display.update();
            }

            Display.sync(fpsLimit);

            final long currentTime = System.nanoTime();
            frames++;
            if (nextFPSUpdateTime <= currentTime ) {
                long timeUsed = FPS_UPD_INTERVAL + (currentTime - nextFPSUpdateTime);
                nextFPSUpdateTime = currentTime + FPS_UPD_INTERVAL;
                final int fpsAverage = (int)(frames * FPS_UPD_INTERVAL / (timeUsed));
                fps.set(fpsAverage);
                frames = 0;
            }
            Platform.runLater(() -> update(nodes));
        }

        onTerminate();
        renderStream.destroy();
        pbuffer.destroy();
        Display.destroy();
        context = null;
    }

    /**
     * Terminate this wrapper.
     */
    public void terminate() {
        running = false;
    }

    /**
     * Set the scale size.
     * @param scale The scale size to set.
     */
    public void scale(float scale) {
        this.scale = scale;
    }

    public static void runLater(Runnable runnable) {
        QUEUE.add(runnable);
    }

    /**
     * Get the read handler which uploads a snapshot to JavaFX.
     * @return A new {@code StreamHandler} instance.
     */
    private StreamHandler getReadHandler() {
        return new StreamHandler() {

            private long frame;
            private long lastUpload;

            {
                new AnimationTimer() {
                    @Override
                    public void handle(final long now) {
                        frame++;
                    }
                }.start();
            }

            @Override
            public int getWidth() {
                return (int) view.getFitWidth();
            }

            @Override
            public int getHeight() {
                return (int) view.getFitHeight();
            }

            @Override
            public void process(final int width, final int height, final ByteBuffer data, final int stride, final Semaphore signal) {
                // This method runs in the background rendering thread
                // TODO: Run setPixels on the PlatformImage in this thread, run pixelsDirty on JFX application thread with runLater.
                Platform.runLater(() -> {
                    try {
                        if (!view.isVisible()) {
                            return;
                        }
                        if (renderImage == null || (int) renderImage.getWidth() != width || (int) renderImage.getHeight() != height) {
                            renderImage = new WritableImage(width, height);
                            view.setImage(renderImage);
                        }
                        if (frame <= lastUpload + 1) {
                            return;
                        }
                        lastUpload = frame;
                        PixelWriter pw = renderImage.getPixelWriter();
                        pw.setPixels(0, 0, width, height, pw.getPixelFormat(), data, stride);
                    } finally {
                        signal.release();
                    }
                });
            }
        };
    }

}
