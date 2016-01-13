package android.fireworks.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Json {

	public static Gson mapper = new Gson();
	public static Gson prettyMapper = new GsonBuilder().setPrettyPrinting().create();

	public static String encode(Object obj) throws EncodeException {
		try {
			return mapper.toJson(obj);
		} catch (Exception e) {
			throw new EncodeException("Failed to encode as JSON: " + e.getMessage());
		}
	}

	public static String encodePrettily(Object obj) throws EncodeException {
		try {
			return prettyMapper.toJson(obj);
		} catch (Exception e) {
			throw new EncodeException("Failed to encode as JSON: " + e.getMessage());
		}
	}

	public static <T> T decodeValue(String str, Class<T> clazz) throws DecodeException {
		try {
			return mapper.fromJson(str, clazz);
		} catch (Exception e) {
			throw new DecodeException("Failed to decode:" + e.getMessage());
		}
	}

	public static JsonObject toJsonObject(String str) {
		return decodeValue(str, JsonObject.class);
	}

}