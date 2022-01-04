package uz.mvp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Doston Bokhodirov, Sun 9:06 PM. 12/19/2021
 */
public class Util {
    public static String asString(Object obj) {
        return gson().toJson(obj);
    }


    public static Gson gsonWithNulls() {
        return new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    }


    public static Gson gson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
