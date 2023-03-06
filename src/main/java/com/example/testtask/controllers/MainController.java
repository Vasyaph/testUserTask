package com.example.testtask.controllers;

import com.example.testtask.user.User;
import com.example.testtask.user.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class MainController {
    public final UserRepository userRepo;
    @Autowired
    public MainController(UserRepository userRepository){
        userRepo=userRepository;
    }

    @GetMapping
    public List<User> main(){
        return userRepo.findAll();
    }

    @GetMapping("{id}")
    public User getOne(@PathVariable("id") User user) {
        return user;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepo.save(user);
    }
    @PutMapping("{id}")
    public User update(@PathVariable("id") User userFromDB, @RequestBody User user) {
        BeanUtils.copyProperties(user,userFromDB,"id");

        return userRepo.save(userFromDB);
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") User user) {
        userRepo.delete(user);
    }
}
