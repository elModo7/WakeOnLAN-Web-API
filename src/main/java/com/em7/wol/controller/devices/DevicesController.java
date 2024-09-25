package com.em7.wol.controller.devices;


import com.em7.wol.dto.out.OutDeviceDTO;
import com.em7.wol.service.PingService;
import com.em7.wol.service.WakeService;
import com.em7.wol.util.RestUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


@Slf4j
@Controller
public class DevicesController {

    @Value("classpath:devices.json")
    private Resource deviceListJSON;

	@Value("${version}")
    private String version;

    @Value("${shutdown.port}")
    private Integer shutdownPort;

    @Autowired
    private PingService pingService;

    @Autowired
    private WakeService wakeService;

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

            // Method 1: Using wakeonlan via package installed client
            Process p;
            try {
                p = Runtime.getRuntime().exec("wakeonlan " + mac);
                p.waitFor();
                p.destroy();
            } catch (Exception e) {
                log.error("There was an error turning on device with ip: " + ip + " mac: " + mac);
                log.error("Error: " + e);
            }

            // # Method 2: Using plain Java socket (I use both ports 7 and 9 for compatibility)
            wakeService.sendMagicPacket(ip, mac);

            return "devices/list";
        }else{
            return "redirect:/";
        }
    }

    @RequestMapping(path = "/devices/shutdown/{ip}", method = RequestMethod.GET)
    public String sendShutdown(Model model, HttpServletRequest request, @PathVariable String ip) {
        String username = (String) request.getSession().getAttribute("username");
        if(username != null && !username.isEmpty()) {
            model.addAttribute("username", username);
            model.addAttribute("devices", getDevices(request));
            log.info("Turning off device with ip: " + ip + " on shutdownPort: " + shutdownPort);
            try {
                URL url = new URL("http://" + ip + ":" + shutdownPort + "/shutdown");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                // Optional: Set timeouts for connection and read
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                // Check the response code
                int responseCode = con.getResponseCode();
                con.disconnect();
            } catch (Exception e) {
                log.error("There was an error turning off device with ip: " + ip + " on shutdownPort: " + shutdownPort);
                log.error("Error: " + e);
            }
            return "devices/list";
        }else{
            return "redirect:/";
        }
    }
}