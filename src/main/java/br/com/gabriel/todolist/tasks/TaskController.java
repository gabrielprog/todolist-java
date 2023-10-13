package br.com.gabriel.todolist.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.todolist.users.UserRespository;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRespository userRespository;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody TaskModel taskModel) {

        taskModel.setUserId(userRespository.findByUsername("exemplo01"));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(taskModel));
    }
}
