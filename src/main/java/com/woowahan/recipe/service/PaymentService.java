package com.woowahan.recipe.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Map;

@Service
@Slf4j
public class PaymentService {

    @Value("${IMP_KEY}")
    private String impKey;

    @Value("${IMP_SECRET}")
    private String impSecret;

    @Getter
    private class Response {
        private PaymentInfo response;
    }

    @Getter
    private class PaymentInfo {
        private int amount;
    }
    public String getToken() throws IOException {

        HttpsURLConnection conn = null;

        URL url = new URL("https://api.iamport.kr/users/getToken");

        conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        JsonObject json = new JsonObject();

        json.addProperty("imp_key", impKey);
        json.addProperty("imp_secret", impSecret);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

        bw.write(json.toString());
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

        Gson gson = new Gson();
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
        log.info("gson.response={}", response);
        String token = gson.fromJson(response, Map.class).get("access_token").toString();
        log.info("gson.token={}", token);
        br.close();
        conn.disconnect();

        return token;
    }

    public int paymentInfo(String imp_Uid, String token) throws IOException {
        HttpsURLConnection conn = null;
        log.info("imp_Uid={}", imp_Uid);

        URL url = new URL("https://api.iamport.kr/payments/" + imp_Uid);

        conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", token);
        conn.setDoOutput(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

        Gson gson = new Gson();

        Response response = gson.fromJson(br.readLine(), Response.class);

        br.close();
        conn.disconnect();

        return response.getResponse().getAmount();
    }

    public void paymentCancel(String token, String imp_Uid, int amount, String reason) throws IOException {
        log.info("paymentService 결제취소");
        log.info("paymentService token={}", token);
        log.info("imp_uid={}", imp_Uid);
        log.info("amount={}", amount);
        log.info("reason={}", reason);

        HttpsURLConnection conn = null;
        URL url = new URL("https://api.iamport.kr/payments/cancel");

        conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", token);

        conn.setDoOutput(true);

        JsonObject json = new JsonObject();

        json.addProperty("reason", reason);
        json.addProperty("imp_uid", imp_Uid);
        json.addProperty("amount", amount);
        json.addProperty("checksum", amount);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

        bw.write(json.toString());
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

        br.close();
        conn.disconnect();

    }




}
