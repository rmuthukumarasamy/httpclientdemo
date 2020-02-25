package com.example.demo.service;

import com.example.demo.properties.ExternalCallProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ConcurrentService {

    private ExternalCallProperties externalCallProperties;

    public List<String> sampleRequest() throws URISyntaxException {
        HttpClient httpClient = HttpClient.newBuilder().executor(Executors.newFixedThreadPool(5)).build();
        List<URI> targets = Arrays.asList(new URI(externalCallProperties.getBaseURL() + "/GBP"),
                new URI(externalCallProperties.getBaseURL() + "/EUR"),
                new URI(externalCallProperties.getBaseURL() + "/BSD"),
                new URI(externalCallProperties.getBaseURL() + "/USD"),
                new URI(externalCallProperties.getBaseURL() + "/CAD"));
        List<CompletableFuture<String>> responseBodyList = targets.stream().map(this::prepareRequest)
                .map(httpRequest -> httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body))
                .collect(Collectors.toList());
        return responseBodyList.stream().map(this::getResponse).collect(Collectors.toList());
    }

    private String getResponse(CompletableFuture<String> completableFuture) {
        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpRequest prepareRequest(URI uri) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
    }

}
