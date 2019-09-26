package com.example.todo.testHelper;

import com.example.todo.entity.TODOEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public List<TODOEntity> getAll() {
        String path = "https://api.backendless.com/878883AB-5152-C87B-FF2C-11DD779C3900/" +
                "9FF8F9DF-9570-641F-FF78-DFFBC3FFA100/data/todo";

        HttpURLConnection con = getConnection(getUrl(path));

        Objects.requireNonNull(con).setDoInput(true);

        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        con.setRequestProperty("Content-Type", "application/json");

        TODOEntity[] todoEntity = new TODOEntity[0];
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            todoEntity = mapper.readValue(in.readLine(), TODOEntity[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<TODOEntity> content = new ArrayList<>(Arrays.asList(todoEntity));
        con.disconnect();
        return content;
    }

    private URL getUrl(String path){
        try {
            return new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpURLConnection getConnection(URL url){
        try {
            return (HttpURLConnection) Objects.requireNonNull(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
