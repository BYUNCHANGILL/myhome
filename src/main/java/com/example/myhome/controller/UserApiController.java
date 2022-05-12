package com.example.myhome.controller;

import com.example.myhome.model.Board;
import com.example.myhome.model.User;
import com.example.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
class UserApiController {
    @Autowired
    private UserRepository repository;

    @GetMapping("/user")
    List<User> all() {
        return repository.findAll();
    }

    @PostMapping("/user")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    @GetMapping("/user/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id).orElseThrow(null);
    }

    @PutMapping("/user/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
//                    user.setTitle(newUser.getTitle());
//                    user.setContent(newUser.getContent());
//                    user.setBoards(newUser.getBoards());
                    user.getBoards().clear();
                    user.getBoards().addAll(newUser.getBoards());
                    for(Board board : user.getBoards()) {
                        board.setUser(user);
                    }
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/user/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
