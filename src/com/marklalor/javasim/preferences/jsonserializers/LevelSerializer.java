package com.marklalor.javasim.preferences.jsonserializers;

import java.lang.reflect.Type;

import org.apache.log4j.Level;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LevelSerializer implements JsonSerializer<Level>, JsonDeserializer<Level>
{
    @Override
    public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        return Level.toLevel(json.getAsString());
    }
    
    @Override
    public JsonElement serialize(Level src, Type typeOfSrc, JsonSerializationContext context)
    {
        return context.serialize(src.toString());
    }
}
