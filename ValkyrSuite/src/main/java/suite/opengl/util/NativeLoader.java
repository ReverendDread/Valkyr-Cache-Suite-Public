/**
 * 
 */
package suite.opengl.util;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ReverendDread
 * Aug 19, 2019
 */
@Slf4j
public class NativeLoader {

    public static void extractNatives() throws Exception {
    	log.info(System.getProperty("java.library.path"));
    	File folder = new File(System.getProperty("user.home") + File.separator + ".valkyr" + File.separator + "native");
		folder.mkdirs();
		URI uri = NativeLoader.class.getClassLoader().getResource("native/windows").toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath("/native/windows");
        } else {
            myPath = Paths.get(uri);
        }
        Stream<Path> walk = Files.walk(myPath, 1);
        for (Iterator<Path> it = walk.iterator(); it.hasNext();){
           Path next = it.next();
           try {
        	   Files.copy(next, new File(folder, next.getFileName().toString()).toPath());
           } catch(Exception ex) {
        	   
           }
        }
        walk.close();
        System.setProperty("java.library.path", folder.getAbsolutePath() + ";" + System.getProperty("java.library.path"));
        System.setProperty("org.lwjgl.librarypath", folder.getAbsolutePath());
    	log.info(System.getProperty("java.library.path"));
    }

}
