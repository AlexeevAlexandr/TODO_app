package com.example.todo.controller;

import com.example.todo.entity.TODOEntity;
import com.example.todo.service.TODOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/todo")
public class TODOController {

    private final TODOService todoService;

    @Autowired
    public TODOController(TODOService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<TODOEntity> getAll(){
        return todoService.getAll();
    }

    @PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public TODOEntity create(@RequestBody TODOEntity todoEntity){
        return todoService.create(todoEntity);
    }

    @PutMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public TODOEntity update(@RequestBody TODOEntity todoEntity){
        return todoService.update(todoEntity);
    }

    @DeleteMapping(value = "{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public void delete(@PathVariable long id){
        todoService.delete(id);
    }
}
