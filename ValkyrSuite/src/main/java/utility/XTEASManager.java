package utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Setter;
import suite.Constants;

public class XTEASManager {

	public static final Map<Integer, int[]> maps = new HashMap<>();
	public static final int[] NULL_KEYS = new int[4];
	@Setter public XTEAParserType parserType = XTEAParserType.POLAR;
	private static XTEASManager instance;

	public XTEASManager() {
		this.instance = this;
	}

	public void init() {
		new JsonLoader() {

			@Override
			public void load(JsonObject reader, Gson builder) {
				switch (parserType) {
					case DISPLEE:
						Map<Integer, int[]> keyPairMap = builder.fromJson(reader, new TypeToken<Map<Integer, int[]>>(){}.getType());
						keyPairMap.forEach(maps::put);
						break;
					case POLAR:
						int region = reader.get("mapsquare").getAsInt();
						int[] keys = builder.fromJson(reader.getAsJsonArray("key"), int[].class);
						maps.put(region, keys);
						break;
				}
			}

			@Override
			public String filePath() {
				return Constants.settings.xteaFile;
			}

		}.load();
	}

	public static final int[] lookup(int id) {
		if (maps.isEmpty()) {
			XTEASManager.get().init();
		}
		int[] keys = maps.get(id);
		if (keys == null)
			return NULL_KEYS;
		return keys;
	}

	public static XTEASManager get() {
		if (Objects.isNull(instance)) {
			return new XTEASManager();
		}
		return instance;
	}


	public enum XTEAParserType {
		POLAR, DISPLEE
	}

}
