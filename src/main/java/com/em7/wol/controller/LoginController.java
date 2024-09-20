package com.em7.wol.controller;

import com.em7.wol.dto.in.EntradaLoginDTO;
import com.em7.wol.dto.out.SalidaLoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Controller
public class LoginController {

    @Value("${wol.user}")
    String user;

    @Value("${wol.password}")
    String password;
	
	@Value("${version}")
    private String version;

    @RequestMapping(value = "/dologin", produces = "application/json"
            , consumes = "application/json")
    @ResponseBody
    public SalidaLoginDTO login(@RequestBody EntradaLoginDTO loginDTO, HttpServletRequest request) {
        try {
            SalidaLoginDTO respuestaDTO = new SalidaLoginDTO();
            respuestaDTO.setResult("OK");
            // Username is case insensitive because Android by default makes text uppercase on first letter and I am lazy to type twice each time
            if(loginDTO.getUsername().toLowerCase().equals(user.toLowerCase()) && loginDTO.getPassword().equals(password))
            {
                request.getSession().setAttribute("username", loginDTO.getUsername());
                log.info("Successful login for " + loginDTO.getUsername() + " from " + request.getRemoteAddr());
            }else{
                request.getSession().removeAttribute("username");
                log.error("Authentication failed for " + loginDTO.getUsername() + " from " + request.getRemoteAddr());
            }
            return respuestaDTO;
        } catch (Exception e) {
            String nombreMetodo = new Object() {}.getClass().getEnclosingMethod().getName();
            String mensajeError = "Error en " + nombreMetodo + " - " + e.toString();
            log.error(mensajeError);
            SalidaLoginDTO respuestaDTO = new SalidaLoginDTO();
            respuestaDTO.setResult("KO");
            respuestaDTO.setMessage(mensajeError);
            return respuestaDTO;
        }
    }
    @GetMapping("/")
    public String showLogin(Model model, HttpServletRequest request) {
        request.getSession().removeAttribute("username");
		model.addAttribute("WoLVersion", version);
        return "login";
    }
}
