package com.scientificworkserver.controller;

import com.scientificworkserver.model.User;
import com.scientificworkserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(
            value = "/korisnik/hello",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Zdravo", HttpStatus.OK);
    }


    @RequestMapping(
            value = "/korisnik/login/{username}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> login(@PathVariable String username) {

        User k = userService.findByUsername(username);
        User user = userService.save(k);
        UserService.aktivanUser = user;
        System.out.println("usla sam u kontroler");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/getActiveUser",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> getActiveUser() {

        return new ResponseEntity<User>(userService.aktivanUser, HttpStatus.OK);
    }




}
