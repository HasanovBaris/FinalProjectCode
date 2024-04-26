package com.naa.trainingcenter.controller;

import com.naa.trainingcenter.domain.entities.User;
import com.naa.trainingcenter.dto.UserDto;
import com.naa.trainingcenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/role/{role}")
    List<UserDto> getUsersByRole(@PathVariable String role){
        return userService.getUsersByRole(role);
    }

    @GetMapping("/save/{username}")
     void saveUser(@PathVariable String username){
        userService.addUser(username);
    }


    @PutMapping("/{username}/{activation}")
    void enableOrDisable(@PathVariable("username") String username, @PathVariable("activation") boolean activation){
        userService.enableOrDisable(username, activation);
    }

    @PutMapping("/roles/add/{username}")
    User addRoleToUser(@PathVariable("username") String username, @RequestParam("roleId") Long roleId){
        return userService.addRoleToUser(username, roleId);
    }

    @DeleteMapping("/roles/remove/{username}")
    User removeRoleFromUser(@PathVariable("username") String username, @RequestParam("roleId") Long roleId){
        return userService.removeRoleFromUser(username, roleId);
    }

    @GetMapping("/all")
    List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/all/active")
    List<UserDto> getActiveUsers(){
        return userService.getActiveUsers();
    }

    @GetMapping("/all/passive")
    List<UserDto> getPassiveUsers(){
        return userService.getPassiveUsers();
    }
}
