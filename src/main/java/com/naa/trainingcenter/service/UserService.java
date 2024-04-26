package com.naa.trainingcenter.service;

import com.naa.trainingcenter.domain.entities.Role;
import com.naa.trainingcenter.domain.entities.User;
import com.naa.trainingcenter.dto.UserDto;
import com.naa.trainingcenter.exception.ResourceNotFoundException;
import com.naa.trainingcenter.repo.RoleRepo;
import com.naa.trainingcenter.repo.UserRepo;
import com.naa.trainingcenter.utils.CustomUserDetails;
import com.naa.trainingcenter.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final LogService logService;

    @Override
    @Transactional
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new CustomUserDetails(
                user.getUsername(), user.getPassword(), authorities, user.getName(), user.getSurname(), user.getPosition(), user.getId(), user.getIsEnabled());
    }

    public List<UserDto> getUsersByRole(String roleName){
        return Mapper.mapAll(userRepo.findByRoleName(roleName), UserDto.class);
    }

    public List<UserDto> getAllUsers(){
        return Mapper.mapAll(userRepo.findAll(), UserDto.class);
    }

    public void addUser(String username){
        String encodedPassword = passwordEncoder.encode(username);
        System.out.println(encodedPassword);
    }

    public User addRoleToUser(String username, Long roleId){
        User user = userRepo.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role Not Found"));
        user.getRoles().add(role);

        String usernameSecurity = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Role %s added to user %s", role.getName(), user.getUsername()), usernameSecurity);
        return userRepo.save(user);
    }

    public void enableOrDisable(String username, boolean activation){
        User user = userRepo.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        user.setIsEnabled(activation);

        String usernameSecurity = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Account activation for %s set to %b", user.getUsername(), activation), usernameSecurity);

        userRepo.save(user);
    }

    public List<UserDto> getActiveUsers(){
        return Mapper.mapAll(userRepo.getAllByIsEnabledAndUsernameIsNot(true, "admin@it.center"), UserDto.class);
    }

    public List<UserDto> getPassiveUsers(){
        return Mapper.mapAll(userRepo.getAllByIsEnabledAndUsernameIsNot(false, "admin@it.center"), UserDto.class);
    }

    public User removeRoleFromUser(String username, Long roleId){
        User user = userRepo.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role Not Found"));
        user.getRoles().remove(role);

        String usernameSecurity = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("DELETE", this.getClass().getName(), String.format("Role %s removed from user %s", role.getName(), user.getUsername()), usernameSecurity);
        return userRepo.save(user);
    }

    private void resetPassword(User user, String password){
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        String usernameSecurity = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Password was reset for user %s", user.getUsername()), usernameSecurity);
        userRepo.save(user);
    }
}
