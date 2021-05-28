package com.hackathon.speediestapi.controller;

import com.hackathon.speediestapi.domain.ConnectionEntity;
import com.hackathon.speediestapi.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService service;

    @PostMapping
    @Transactional
    public ResponseEntity<ConnectionEntity> createPlayer(@Valid @RequestBody ConnectionEntity connection, UriComponentsBuilder uriBuilder) {
        service.createConnection(connection);
        URI uri = uriBuilder.path("/connections").buildAndExpand(connection.getId()).toUri();

        return ResponseEntity.created(uri).body(connection);
    }

    @GetMapping
    @Cacheable(value = "allConnections")
    public ResponseEntity<List<ConnectionEntity>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConnectionEntity> findById(@PathVariable UUID id) {
        Optional<ConnectionEntity> connection = service.findById(id);
        if(connection.isPresent()) {
            return ResponseEntity.ok().body(connection.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}