package com.example.todo.testHelper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

@Component
public class TestHelper {

    public JSONObject getJsonObjectFromFile(String filePath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(new FileReader(getFileFromResources(filePath)));
    }

    private File getFileFromResources(String path) {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(path)).getFile());
    }
}
