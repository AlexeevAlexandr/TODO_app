package com.example.todo.service;

import com.example.todo.entity.TODOEntity;

import java.util.List;

public interface TODOService {

    TODOEntity create(TODOEntity todoEntity);

    TODOEntity getById(String id);

    List<TODOEntity> getAll();

    TODOEntity update(TODOEntity todoEntity);

    void delete(String id);
}
