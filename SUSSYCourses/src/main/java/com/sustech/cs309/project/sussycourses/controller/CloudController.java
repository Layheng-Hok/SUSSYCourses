package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.service.CloudService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/cloud")
public class CloudController {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./.env")
            .filename(".env")
            .load();

    //Pulls a file from GCP
    @PostMapping("/get")
    public String getStorageKey(String fileName) throws IOException {
        String projectId = CloudService.readStorageKey(dotenv.get("STORAGE_KEY"));
        return CloudService.generateV4GetObjectSignedUrl(projectId, "sussycourses", fileName, dotenv.get("STORAGE_KEY"));
    }


    //Upload a file to GCP
    @PostMapping("/put")
    public String putStorageKey(@RequestParam("file") MultipartFile file, @RequestParam("fileType") String fileType, @RequestParam("fileLocation") String fileLocation) throws Exception {
        if (fileLocation.equals("File Type Not Supported")) {
            return "File Type Not Supported";
        }
        String projectId = CloudService.readStorageKey(dotenv.get("STORAGE_KEY"));
        return CloudService.uploadObject(projectId, "sussycourses", fileLocation, file, fileType);
    }

    @DeleteMapping("/delete")
    public String deleteBlob( @RequestParam("fileLocation") String fileLocation) throws Exception {
        String projectId = CloudService.readStorageKey(dotenv.get("STORAGE_KEY"));
        return CloudService.deleteObject(projectId, "sussycourses", fileLocation);
    }
}
