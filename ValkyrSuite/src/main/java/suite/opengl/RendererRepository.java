package suite.opengl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import suite.opengl.wrapper.OpenGLWrapper;

import java.util.List;
import java.util.UUID;

/**
 * @author ReverendDread on 12/11/2019
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@Slf4j
public class RendererRepository {

    private static RendererRepository instance;

    private List<OpenGLWrapper> wrappers = Lists.newArrayList();

    public RendererRepository() {
        instance = this;
    }

    public void addWrapper(OpenGLWrapper wrapper) {
        wrappers.add(wrapper);
    }

    public void terminateWrapper(UUID uuid) {
        wrappers.stream().filter(wrapper -> wrapper.getUuid().toString().equals(uuid.toString())).forEach((wrapper) -> {
            log.info("Terminated wrapper {}", wrapper.getUuid().toString());
            wrapper.terminate();
        });
    }

    public static RendererRepository get() {
        if (instance == null)
            return new RendererRepository();
        return instance;
    }

}
