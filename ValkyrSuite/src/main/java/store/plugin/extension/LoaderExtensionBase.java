/**
 * 
 */
package store.plugin.extension;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import store.io.impl.InputStream;

/**
 * @author ReverendDread
 * Oct 6, 2019
 */
@Slf4j
public abstract class LoaderExtensionBase {

	@Getter
	protected Map<Integer, ConfigExtensionBase> definitions = Maps.newHashMap();
	
	public LoaderExtensionBase() {
		load();
	}
	
	public abstract boolean load();
	
	public abstract int getFile();
	
	public abstract int getArchive();
	
	public abstract int getIndex();
	
	public void reload() {
		definitions.clear();
		load();
	}
	
	protected void readConfig(InputStream buffer, ConfigExtensionBase definition) {
		for (;;) {
			int opcode = buffer.readUnsignedByte();
			if (opcode == 0)
				break;
			definition.decode(opcode, buffer);
		}
	}
	
}
