// VideoController.java
package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.utils.CloudUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class VideoController {
    @GetMapping("/video")
    public String displayVideo(Model model) throws IOException {
        String fileName = "Courses/Course1/courseware/videos/chapter1"; //Just an example
        String videoUrl = CloudUtils.getStorageKey(fileName);
        System.out.println(videoUrl);
        model.addAttribute("videoUrl", videoUrl);
        return "video"; // This will resolve to video.html
    }

}
