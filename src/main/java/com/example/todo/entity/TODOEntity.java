package com.example.todo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TODOEntity {
    String description;
    String owner;
    boolean completed;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate deadlineDate;

    @Override
    public String toString(){
        return "{" +
                "\"description\" : \"" + description + "\", " +
                "\"owner\" : \"" + owner + "\"," +
                "\"completed\" : \"" + completed + "\"," +
                "\"creationDate\" : \"" + creationDate + "\", " +
                "\"deadlineDate\" : \"" + deadlineDate + "\"" +
                "}";
    }
}
