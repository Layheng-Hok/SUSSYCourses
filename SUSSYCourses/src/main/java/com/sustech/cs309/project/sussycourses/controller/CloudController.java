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
            .directory("SUSSYCourses/src/main/resources/.env")
            .filename(".env")
            .load();

    //Pulls a file from GCP
    @PostMapping("/get")
    public String getStorageKey(String fileName) throws IOException {
        String projectId = CloudService.readStorageKey(dotenv.get("STORAGE_KEY"));
        return CloudService.generateV4GetObjectSignedUrl(projectId, "sussycourses", fileName,dotenv.get("STORAGE_KEY"));
    }


    //Upload a file to GCP
    @PostMapping("/put")
    public String putStorageKey(@RequestParam("file") MultipartFile file, @RequestParam("fileType") String fileType, @RequestParam("fileName") String fileName) throws Exception {
        String fixed_name = CloudService.fixName(fileType, fileName);
        if(fixed_name.equals("File Type Not Supported")){
            return "File Type Not Supported";
        }
        String projectId = CloudService.readStorageKey(dotenv.get("STORAGE_KEY"));
        return CloudService.uploadObject(projectId, "sussycourses", fixed_name, file, fileType);
    }
}
