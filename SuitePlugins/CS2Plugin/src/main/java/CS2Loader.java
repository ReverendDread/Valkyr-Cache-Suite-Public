import lombok.extern.slf4j.Slf4j;
import script.CS2Disassembler;
import script.CS2Script;
import store.CacheLibrary;
import store.cache.index.Index;
import store.io.impl.InputStream;
import store.plugin.extension.LoaderExtensionBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class CS2Loader extends LoaderExtensionBase {

    private static final String DUMP_DIR = System.getProperty("user.home") + "\\Desktop\\Kronos\\Scripts\\";
    private static final boolean DUMP_CS2 = false;
    private CS2Disassembler disassembler = new CS2Disassembler();

    @Override
    public boolean load() {
        try {
            Index index = CacheLibrary.get().getIndex(getIndex());
            int[] archiveIds = index.getArchiveIds();
            for (int archive : archiveIds) {
                byte[] data = index.getArchive(archive).getData();
                if (data == null) continue;
                CS2Script script = new CS2Script();
                script.id = archive;
                script.decode(new InputStream(data));
                definitions.put(archive, script);
                if (DUMP_CS2) {
                    String output = disassembler.disassemble(script);
                    File folder = new File(DUMP_DIR);
                    if (folder.exists() || folder.mkdirs()) {
                        File file = new File(DUMP_DIR, script.id + ".txt");
                        if (file.exists() || file.createNewFile()) {
                            log.info("Writing script {} content", script.id);
                            try (FileWriter fw = new FileWriter(file)) {
                                fw.write(output);
                                fw.flush();
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                }
            }
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public int getFile() {
        return 0;
    }

    @Override
    public int getArchive() {
        return -1;
    }

    @Override
    public int getIndex() {
        return 12;
    }
}
