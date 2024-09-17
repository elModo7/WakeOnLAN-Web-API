package com.em7.wol.controller.devices;


import com.em7.wol.service.PingService;
import com.em7.wol.util.RestUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.em7.wol.dto.out.OutDeviceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
public class DevicesController {

    @Value("classpath:devices.json")
    private Resource deviceListJSON;

	@Value("${version}")
    private String version;

    @RequestMapping(value = "/getDevices", method = RequestMethod.GET)
    @ResponseBody
    public List<OutDeviceDTO> getDevices(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if(username != null && !username.isEmpty()) {
            try {
                return getAllDevices();
            } catch (Exception e) {
                String methodName = new Object() {
                }.getClass().getEnclosingMethod().getName();
                String error = "Error en " + methodName + " - " + e.toString();
                log.error(error);
                return null;
            }
        }else{
            return null;
        }
    }

    @GetMapping("/devices/list")
    public String showDevicesList(Model model, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if(username != null && !username.isEmpty()) {
            model.addAttribute("username", username);
            model.addAttribute("devices", getDevices(request));
			model.addAttribute("WoLVersion", version);
            return "devices/list";
        }else{
            return "redirect:/";
        }
    }

    public List<OutDeviceDTO> getAllDevices() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Type listType = new TypeToken<List<OutDeviceDTO>>() {}.getType();
        List<OutDeviceDTO> outDeviceDTOs = gson.fromJson(RestUtils.asString(deviceListJSON), listType);

        try {
            PingService pingService = new PingService();
            pingService.pingDevicesConcurrently(outDeviceDTOs);
        } catch (Exception e) {
            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            String error = "Error en " + methodName + " - " + e.toString();
            log.error(error);
            return null;
        }
        return outDeviceDTOs;
    }

    @RequestMapping(path = "/devices/wol/{mac}&{ip}", method = RequestMethod.GET)
    public String sendWoL(Model model, HttpServletRequest request, @PathVariable String mac, @PathVariable String ip) {
        String username = (String) request.getSession().getAttribute("username");
        if(username != null && !username.isEmpty()) {
            model.addAttribute("username", username);
            model.addAttribute("devices", getDevices(request));
            log.info("Turning on device with ip: " + ip + " mac: " + mac);
            Process p;
            try {
                p = Runtime.getRuntime().exec("wakeonlan " + mac);
                p.waitFor();
                p.destroy();
            } catch (Exception e) {
                log.error("There was an error turning on device with ip: " + ip + " mac: " + mac);
                log.error("Error: " + e);
            }
            return "devices/list";
        }else{
            return "redirect:/";
        }
    }
}