// VideoController.java
package com.sustech.cs309.project.sussycourses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class VideoController {
    @Autowired
    CloudController storageController;

    @GetMapping("/video")
    public String displayVideo(Model model) throws IOException {
        String fileName = "video6.png"; //Just an example
        String videoUrl = storageController.getStorageKey(fileName);
        System.out.println(videoUrl);
        model.addAttribute("videoUrl", videoUrl);
        return "video"; // This will resolve to video.html
    }

}
