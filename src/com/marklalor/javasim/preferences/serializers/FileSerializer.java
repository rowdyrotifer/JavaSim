package com.marklalor.javasim.preferences.serializers;

import java.io.File;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class FileSerializer implements JsonSerializer<File>, JsonDeserializer<File>
{
    @Override
    public JsonElement serialize(File src, Type typeOfSrc, JsonSerializationContext context)
    {
        return context.serialize(src.getAbsolutePath());
    }
    
    @Override
    public File deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        return new File(json.getAsString());
    }
}