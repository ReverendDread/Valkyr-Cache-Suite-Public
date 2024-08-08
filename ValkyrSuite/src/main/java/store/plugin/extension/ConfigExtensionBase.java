/**
 * 
 */
package store.plugin.extension;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.scene.image.Image;
import javafx.util.Pair;
import lombok.Setter;
import store.codec.model.Mesh;
import store.io.impl.InputStream;
import store.io.impl.OutputStream;
import suite.annotation.MeshIdentifier;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author ReverendDread Sep 14, 2019
 */
@Setter
public abstract class ConfigExtensionBase implements Cloneable {

	public int id;

	public int previousOpcodeIndex;
	public int[] previousOpcodes;

	public void decode(int opcode, InputStream buffer) {}

	public void decode(InputStream buffer) {}

	public abstract OutputStream encode(OutputStream buffer);

	public byte[] encode() {
		return null;
	}

	public abstract String toString();

	public abstract Map<Field, Integer> getPriority();

	public List<Integer> getMeshIds() { return null; }

	public List<Pair<Integer, Integer>> getRecolors() { return null; }

	public List<Pair<Integer, Integer>> getRetextures() { return null; }

	public List<Image> getImages() { return null; }

	public void onDecodeFinish() {}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
