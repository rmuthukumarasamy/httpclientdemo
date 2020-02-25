package com.example.demo.controller;

import com.example.demo.service.AsyncService;
import com.example.demo.service.ConcurrentService;
import com.example.demo.service.SyncService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
public class DemoController {

    private SyncService syncRequestService;
    private AsyncService asyncService;
    private ConcurrentService concurrentService;

    @GetMapping(value = "/sync", produces = {"application/json"})
    public ResponseEntity<String> syncService() throws IOException, InterruptedException {
        return Optional.ofNullable(syncRequestService.sampleRequest()).map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = "/async", produces = {"application/json"})
    public ResponseEntity<String> asyncService() throws InterruptedException, ExecutionException {
        return Optional.ofNullable(asyncService.sampleRequest()).map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = "/concurrent", produces = {"application/json"})
    public ResponseEntity<List<String>> concurrentService() throws URISyntaxException {
        return Optional.ofNullable(concurrentService.sampleRequest()).map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
}
