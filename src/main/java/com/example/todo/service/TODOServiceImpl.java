package com.example.todo.service;

import com.example.todo.entity.TODOEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TODOServiceImpl implements TODOService{

    private String path = "https://api.backendless.com/878883AB-5152-C87B-FF2C-11DD779C3900/" +
            "9FF8F9DF-9570-641F-FF78-DFFBC3FFA100/data/todo";

    @Override
    public List<TODOEntity> getAll() {
        HttpURLConnection con = getConnection(getUrl(path));
        Objects.requireNonNull(con).setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json");

        try {
            log.info("Set the method for the URL request");
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            log.error("Error in the underlying protocol");
            e.printStackTrace();
        }

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
            log.error("Can not read data");
        }

        List<TODOEntity> content = new ArrayList<>(Arrays.asList(todoEntity));
        con.disconnect();
        return content;
    }

    @Override
    public TODOEntity getById(String id) {
        HttpURLConnection con = getConnection(getUrl(path + "/" + id));
        Objects.requireNonNull(con).setDoInput(true);

        try {
            log.info("Set the method for the URL request");
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            log.error("Error in the underlying protocol");
            e.printStackTrace();
        }

        con.setRequestProperty("Content-Type", "application/json");

        TODOEntity todoEntity = new TODOEntity();
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            todoEntity = mapper.readValue(in.readLine(), TODOEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Can not read data");
        }

        con.disconnect();
        return todoEntity;
    }

    @Override
    public TODOEntity create(TODOEntity todoEntity) {
        todoEntity.setCreationDate(LocalDate.now());
        HttpURLConnection con = getConnection(getUrl(path));
        Objects.requireNonNull(con).setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        try {
            log.info("Set the method for the URL request");
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            log.error("Error in the underlying protocol");
            e.printStackTrace();
        }

        byte[] postData = todoEntity.toString().getBytes(StandardCharsets.UTF_8);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())){
            log.info("Write data");
            wr.write(postData);
        } catch (IOException e) {
            log.error("Can not write data");
            e.printStackTrace();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = in.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
            log.info("Data has been saved: " + content.toString());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Can not read data");
        }

        con.disconnect();
        return todoEntity;
    }

    @Override
    public TODOEntity update(TODOEntity todoEntity) {
        HttpURLConnection con = getConnection(getUrl(path + "/" + todoEntity.getObjectId()));
        Objects.requireNonNull(con).setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        try {
            log.info("Set the method for the URL request");
            con.setRequestMethod("PUT");
        } catch (ProtocolException e) {
            log.error("Error in the underlying protocol");
            e.printStackTrace();
        }

        byte[] postData = todoEntity.toString().getBytes(StandardCharsets.UTF_8);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())){
            log.info("Update data");
            wr.write(postData);
        } catch (IOException e) {
            log.error("Can not update data");
            e.printStackTrace();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = in.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
            log.info("Data has been updated: " + content.toString());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Can not read data");
        }

        con.disconnect();
        return todoEntity;
    }

    // marks as completed
    @Override
    public void delete(String id) {

    }

    private URL getUrl(String path){
        try {
            log.info("Creates a URL object from " + path);
            return new URL(path);
        } catch (MalformedURLException e) {
            log.error("Invalid URL: " + path);
            e.printStackTrace();
            return null;
        }
    }

    private HttpURLConnection getConnection(URL url){
        try {
            log.info("Connection to the remote object");
            return (HttpURLConnection) Objects.requireNonNull(url).openConnection();
        } catch (IOException e) {
            log.error("Can not open connection to url: " + url);
            e.printStackTrace();
            return null;
        }
    }
}
