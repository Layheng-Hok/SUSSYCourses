// VideoController.java
package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.StreamResponse;
import com.sustech.cs309.project.sussycourses.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StreamController {
    private final StreamService streamService;
    @PostMapping("stream/getStreamInfo/{teacherId}")
    public StreamResponse getStreamInfo(@RequestBody String name, @RequestBody String description, @PathVariable Long teacherId) throws IOException {
        return streamService.generateStreamKey(name, teacherId, description);
    }

    @GetMapping("stream/{courseId}")
    public ArrayList<String> getStreamUrl(@PathVariable Long courseId) throws IOException {
        return streamService.getStreamInformation(courseId);
    }
}
