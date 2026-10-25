package com.example.backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;

@Service
public class DatabaseService {

    private String dbPath;

    @PostConstruct
    public void init() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream("spfl.sqlite");

        if (input == null) {
            throw new FileNotFoundException("spfl.sqlite not found in resources");
        }

        Path tempFile = Files.createTempFile("spfl", ".sqlite");
        tempFile.toFile().deleteOnExit();

        try (input) {
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        dbPath = tempFile.toAbsolutePath().toString();
        System.out.println("Database extracted to: " + dbPath);
    }

    public String getConnectionUrl() {
        return "jdbc:sqlite:" + dbPath;
    }
}
