package com.example.todo.service;

import com.example.todo.entity.TODOEntity;

import java.util.List;

public interface TODOService {

    TODOEntity create(TODOEntity todoEntity);

    List<TODOEntity> getAll();

    TODOEntity update(TODOEntity todoEntity);

    void delete(long id);
}
