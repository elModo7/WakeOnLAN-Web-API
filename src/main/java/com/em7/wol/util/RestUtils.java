package com.em7.wol.util;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class RestUtils {
    public static String doPost(String urlString, JsonObject params) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(methodName + " Data input: " + " - url: " + urlString + " - params: " + params.toString());
        String response = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(params.toString());
            wr.flush();
            response = getResponse(conn);
            conn.disconnect();
        } catch (Exception e) {
            log.error("", e);
        }
        log.info(methodName + " - Data output: " + response);
        return response;
    }

    public static String doGet(String urlString) {
        log.info("GET to " + urlString);
        String respuesta = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            respuesta = getResponse(conn);
            conn.disconnect();
        } catch (Exception e) {
            log.error("", e);
        }
        return respuesta;
    }

    private static String getResponse(HttpURLConnection conn) throws Exception {
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            log.error("Error connecting to the server");
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                return strCurrentLine;
            }
        }
        return "";
    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}