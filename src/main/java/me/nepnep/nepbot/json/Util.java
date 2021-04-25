package me.nepnep.nepbot.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class Util {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> List<T> readListFromJsonArray(String path) {
        ObjectReader objectReader = OBJECT_MAPPER.reader().forType(new TypeReference<List<T>>() {});
        List<T> list;

        try {
            list = objectReader.readValue(new FileReader(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return list;
    }

    public static <T> void writeListToJsonArray(List<T> list, String path) {

        try (FileWriter writer = new FileWriter(path)) {
            String json = OBJECT_MAPPER
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(list);
            writer.write(json);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
