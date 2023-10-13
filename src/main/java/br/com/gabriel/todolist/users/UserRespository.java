package br.com.gabriel.todolist.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String username);
 }
