package com.chanfinecloud.cflforemployee.util.http;

import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe: JSON解析
 */
public class JsonParse {
    public static BaseEntity parse(String json){
        return new Gson().fromJson(json, BaseEntity.class);
    }

    public static <T> BaseEntity<T> parse( String json, Class<T> clazz){
        Type type = new ParameterizedTypeImpl(BaseEntity.class, new Class[]{clazz});
        return new Gson().fromJson(json, type);
    }

    public static Object parseList( String json,Type type){
        try {
            JSONObject jsonObject=new JSONObject(json);
            Gson gson=new Gson();
            return gson.fromJson(jsonObject.getJSONArray("result").toString(),type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
