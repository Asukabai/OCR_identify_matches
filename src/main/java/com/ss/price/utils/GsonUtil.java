package com.ss.price.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    public GsonUtil() {
    }

    public static boolean checkJsonIsGood(String json) {
        try {
            (new JsonParser()).parse(json);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static <T> T jsonToObject(String json, Class<T> classOfT) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, classOfT);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static JsonObject jsonToJsonObject(String json) {
        return (JsonObject)(new Gson()).fromJson(json, JsonObject.class);
    }

    public static JsonArray jsonToJsonArray(String json) {
        return (JsonArray)(new Gson()).fromJson(json, JsonArray.class);
    }

    public static List jsonArrayToList(JsonArray jsonArray) {
        return (List)(new Gson()).fromJson(jsonArray, ArrayList.class);
    }

    public static <T> T jsonObjectToObject(JsonObject jsonObject, Class<T> obj) {
        return (new Gson()).fromJson(jsonObject, obj);
    }

    public static <T> Map<String, T> jsonToSMap(String json, Class<T> classOfT) {
        Gson gson = new Gson();
        Map<String, T> maps = (Map)gson.fromJson(json, (new TypeToken<Map<String, T>>() {
        }).getType());
        return maps;
    }

    public static String objectToJson(Object obj) {
        if (obj instanceof String) {
            return (String)obj;
        } else {
            Gson gson = (new GsonBuilder()).serializeNulls().disableHtmlEscaping().create();
            String json = gson.toJson(obj);
            return json;
        }
    }

    public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] arr = (T[]) gson.fromJson(json, clazz);
        return Arrays.asList(arr);
    }

    public static String listToJson(List<?> list) {
        Gson gson = (new GsonBuilder()).registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                return src == (double)src.longValue() ? new JsonPrimitive(src.longValue()) : new JsonPrimitive(src);
            }
        }).create();
        String json = gson.toJson(list);
        return json;
    }

    public static JsonArray listToJsonArray(List list) {
        return (new Gson()).toJsonTree(list, (new TypeToken<List>() {
        }).getType()).getAsJsonArray();
    }

    public static String mapToJson(Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }
}
