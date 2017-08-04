package com.github.east196.fireworks.gson;

import com.github.east196.fireworks.Time;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;


public class JavaDateGsonAdapter extends TypeAdapter<Date> {

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String str = in.nextString();
        try {
            Date date = new Date(Long.parseLong(str));
            return date;
        } catch (Exception e1) {
            try {
                Date date = Time.praseDate(str);
                return date;
            } catch (Exception e2) {
                throw new JsonSyntaxException(e2);
            }
        }
    }

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        out.value(value == null ? null : Time.longDate(value));
    }

}