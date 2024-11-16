package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.service.CloudService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/cloud")
public class CloudController {


    //Pulls a file from GCP
    @PostMapping("/get")
    public String getStorageKey(String fileName) throws IOException {
        String projectId = CloudService.readStorageKey("/Users/layhenghok/Desktop/SUSTech/Year3Semester1/CS309-Object-oriented-Analysis-and-Design/Project/SUSSYCourses/SUSSYCourses/src/main/config/storage_key.json");
        return CloudService.generateV4GetObjectSignedUrl(projectId, "sussycourses", fileName, "/Users/layhenghok/Desktop/SUSTech/Year3Semester1/CS309-Object-oriented-Analysis-and-Design/Project/SUSSYCourses/SUSSYCourses/src/main/config/storage_key.json");
    }


    //Upload a file to GCP
    @PostMapping("/put")
    public String putStorageKey(@RequestParam("file") MultipartFile file, @RequestParam("fileType") String fileType, @RequestParam("fileName") String fileName) throws Exception {
        String fixed_name = CloudService.fixName(fileType, fileName);
        if (fixed_name.equals("File Type Not Supported")) {
            return "File Type Not Supported";
        }
        String projectId = CloudService.readStorageKey("/Users/layhenghok/Desktop/SUSTech/Year3Semester1/CS309-Object-oriented-Analysis-and-Design/Project/SUSSYCourses/SUSSYCourses/src/main/config/storage_key.json");
        return CloudService.uploadObject(projectId, "sussycourses", fixed_name, file, fileType);
    }
}
