package br.com.gabriel.todolist.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class AddTaskController {
    
    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello World!";
    }
    
}
