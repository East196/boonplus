package com.github.east196.fireworks.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.Date;

public class Json {

    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Date.class, new JavaDateGsonAdapter())
            .create();
    public static final Gson PRETTY_GSON = new GsonBuilder().registerTypeAdapter(Date.class, new JavaDateGsonAdapter())
            .setPrettyPrinting().create();
    public static final Gson PRETTY_GSON_WITH_NULL = new GsonBuilder().registerTypeAdapter(Date.class, new JavaDateGsonAdapter())
            .setPrettyPrinting().serializeNulls().create();

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static String toPrettyJson(Object src) {
        return PRETTY_GSON.toJson(src);
    }
    
    public static String toDebugJson(Object src) {
        return PRETTY_GSON_WITH_NULL.toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    public static JsonElement getPath(JsonObject jsonObject, String... path) {
        JsonElement retval = null;
        if (jsonObject != null) {
            JsonElement obj = jsonObject;
            for (String component : path) {
                if (obj == null) break;
                obj = ((JsonObject) obj).get(component);
            }
            retval = obj;
        }
        return retval;
    }
}
