package br.com.gabriel.todolist.tasks;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.todolist.Utils.ObjectComparator;
import br.com.gabriel.todolist.Utils.Utils;
import br.com.gabriel.todolist.users.UserModel;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/read")
    public List<TaskModel> read(HttpServletRequest request) {
        return taskRepository.findByUserId((UUID) request.getAttribute("userId"));
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        taskModel.setUserId((UUID) request.getAttribute("userId"));
        
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(taskRepository.save(taskModel));
    }

    @PutMapping("/{id}")
    public TaskModel update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {
        TaskModel taskId = taskRepository.findById(id).orElseThrow();
        
        taskModel.setUserId((UUID) request.getAttribute("userId"));
        Utils.copyNonNullProperties(taskModel, taskId);
        
        return taskRepository.save(taskModel);
    }

}
