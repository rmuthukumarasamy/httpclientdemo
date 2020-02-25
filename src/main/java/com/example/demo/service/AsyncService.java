package com.example.demo.service;

import com.example.demo.properties.ExternalCallProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
@Slf4j
public class AsyncService {

    private ExternalCallProperties externalCallProperties;

    public String sampleRequest() throws InterruptedException, ExecutionException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(externalCallProperties.getBaseURL() + "/EUR"))
                .build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        CompletableFuture<HttpResponse<String>> httpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        printHeaders(httpResponse.thenApply(HttpResponse::headers).get());
        String responseBody = httpResponse.thenApply(HttpResponse::body).get();
        printBody(responseBody);
        return responseBody;
    }

    private void printHeaders(HttpHeaders httpHeaders) {
        httpHeaders.map().forEach((key, value) -> log.info("Header key: {}, value: {}", key, value));
    }

    private void printBody(String responseBody) {
        log.info("Content: {}", responseBody);
    }

}
