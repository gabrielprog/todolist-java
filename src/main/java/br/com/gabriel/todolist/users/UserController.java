package br.com.gabriel.todolist.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRespository userRespository;

    public boolean isUserRegistered(UserModel userModel)  {
        return userRespository
        .findByUsername(userModel.getUsername()) == null ? true : false;
    }
    
    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody UserModel userModel) {
        
        userModel.setPassword(PasswordCrypter.crypt(userModel.getPassword()));

        if(isUserRegistered(userModel))
            return 
            ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userRespository.save(userModel));

        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Username already exists");
    
    }
}
