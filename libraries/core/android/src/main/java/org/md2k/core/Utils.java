package org.md2k.core;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    /**
     * Reads a json file and returns the data.
     *
     * @param filePath  Path to the json file.
     * @param classType Class that defines the object or data contained in the json file.
     * @param <T>       Formal generic.
     * @return The data or object from the json file.
     * @throws FileNotFoundException
     */
    public static <T> T readJson(String filePath, Class<T> classType) throws FileNotFoundException {
        T data = null;
        BufferedReader reader = null;
        try {
            InputStream in = new FileInputStream(filePath);
            reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            data = gson.fromJson(reader, classType);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return data;
    }

    public static <T> boolean writeJson(String filePath, T data) {
        boolean result = true;
        FileWriter writer = null;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(data);
            writer = new FileWriter(filePath);
            writer.write(json);
        } catch (Exception e) {
            result = false;
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                result = false;
            }
        }
        return result;
    }
}
