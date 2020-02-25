package com.example.demo.service;

import com.example.demo.properties.ExternalCallProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
@AllArgsConstructor
public class SyncService {

    private ExternalCallProperties externalCallProperties;

    public String sampleRequest() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(externalCallProperties.getBaseURL() + "/USD"))
                .build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        printHeaders(httpResponse);
        printBody(httpResponse);
        return httpResponse.body();
    }

    private void printHeaders(HttpResponse<String> httpResponse) {
        log.info("Headers: {}", httpResponse.headers());
    }

    private void printBody(HttpResponse<String> httpResponse) {
        log.info("Content: {}", httpResponse.body());
    }

}
