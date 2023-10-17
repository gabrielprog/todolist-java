package br.com.gabriel.todolist.tasks;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @Autowired
    private TaskRepository taskRepository;

    public boolean isPriorityValid(TaskModel taskModel) {
        if (taskModel.getPriority() < 1 || taskModel.getPriority() > 3) 
            return false;
        
        return true;
    }
    
    @GetMapping("/read")
    public List<TaskModel> read(HttpServletRequest request) {
        return taskRepository.findByUserId((UUID) request.getAttribute("userId"));
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        taskModel.setUserId((UUID) request.getAttribute("userId"));

        if(isPriorityValid(taskModel) == false)
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("Priority must be between 1 and 3");

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(taskRepository.save(taskModel));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {
        TaskModel taskId = taskRepository.findById(id).orElse(null);

        if(taskId == null)
        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body("Task not found");
        
        taskModel.setUserId((UUID) request.getAttribute("userId"));
        Utils.copyNonNullProperties(taskModel, taskId);
        
        if(isPriorityValid(taskModel) == false)
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("Priority must be between 1 and 3");

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(taskRepository.save(taskId));
    }

}
