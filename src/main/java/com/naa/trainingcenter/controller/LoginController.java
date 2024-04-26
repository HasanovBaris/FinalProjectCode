package com.naa.trainingcenter.controller;

import com.naa.trainingcenter.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {

    @PostMapping("/sign-in")
    public String signIn(@RequestBody LoginRequestDto request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("username", request.getUsername());
        redirectAttributes.addAttribute("password", request.getPassword());
        return "redirect:/login";
    }
}
