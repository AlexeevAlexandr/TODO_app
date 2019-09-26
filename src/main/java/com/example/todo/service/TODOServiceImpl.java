package com.example.todo.service;

import com.example.todo.entity.TODOEntity;
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
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TODOServiceImpl implements TODOService{

    @Override
    public TODOEntity create(TODOEntity todoEntity) {
        todoEntity.setCreationDate(LocalDate.now());

        String path = "https://api.backendless.com/878883AB-5152-C87B-FF2C-11DD779C3900/" +
                "9FF8F9DF-9570-641F-FF78-DFFBC3FFA100/data/todo";

        URL url = getUrl(path);

        HttpURLConnection con = getConnection(url);

        Objects.requireNonNull(con).setDoOutput(true);

        try {
            log.info("Set the method for the URL request");
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            log.error("Error in the underlying protocol");
            e.printStackTrace();
        }

        con.setRequestProperty("Content-Type", "application/json");

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
    public List<TODOEntity> getAll() {
        return null;
    }

    @Override
    public TODOEntity update(TODOEntity todoEntity) {
        return null;
    }

    // marks as completed
    @Override
    public void delete(long id) {

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