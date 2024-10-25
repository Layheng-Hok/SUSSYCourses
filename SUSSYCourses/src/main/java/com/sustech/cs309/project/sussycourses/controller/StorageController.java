package com.sustech.cs309.project.sussycourses.controller;
import com.sustech.cs309.project.sussycourses.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/storageKey")
    public String getStorageKey() throws IOException {
        String projectId = storageService.readStorageKey("SUSSYCourses/src/main/config/storage_key.json");
        return storageService.generateV4GetObjectSignedUrl(projectId, "sussycourses", "videos/video1.mp4","SUSSYCourses/src/main/config/storage_key.json");
    }
}
