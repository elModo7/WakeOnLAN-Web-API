package com.em7.wol.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Controller
public class HomeController {
	
	@Value("${version}")
    private String version;

    @GetMapping("/home")
    public String showHome(Model model, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if(username != null && !username.isEmpty()) {
            model.addAttribute("username", username);
			model.addAttribute("WoLVersion", version);
            return "redirect:/devices/list";
        }else{
            return "redirect:/"; // Para que deje la URL bien
        }
    }
}
