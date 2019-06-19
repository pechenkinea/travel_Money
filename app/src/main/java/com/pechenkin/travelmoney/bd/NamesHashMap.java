package com.pechenkin.travelmoney.bd;

import java.util.HashMap;

/**
 * Created by pechenkin on 31.05.2018.
 * Обертка HashMap для того, что бы хранить все ключи в нижнем регистре и заменять "ё" на "е"
 */

public class NamesHashMap<V> extends HashMap<String, V> {


    public V put(String key, V value) {
        return super.put(keyValidate(key), value);
    }

    public V get(String key) {
        return super.get(keyValidate(key));
    }

    public static String keyValidate(String key)
    {
        return  key.toLowerCase().replaceAll("ё","е");
    }
}
