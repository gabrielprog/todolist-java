package br.com.gabriel.todolist.tasks;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.gabriel.todolist.users.UserModel;
import jakarta.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskModel {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Getter
    @Setter
    private String title;
    
    @Getter
    @Setter
    private String description;
    
    @Getter
    @Setter
    private UUID userId;
    
    @Getter
    @Setter
    private boolean completed;

    @Getter
    @Setter
    private int priority;

    @Getter
    @Setter
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Getter
    @Setter
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
