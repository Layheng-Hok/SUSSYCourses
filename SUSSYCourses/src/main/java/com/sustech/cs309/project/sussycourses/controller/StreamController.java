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
    public StreamResponse getStreamInfo(@RequestBody String name, @PathVariable Long teacherId) throws IOException {
        return streamService.generateStreamKey(name, teacherId);
//        return new StreamResponse("name", "60acefa0-ab2d-460f-8d58-52624728e52c", "https://embed.api.video/live/li6MeFh3UB2qfir2298ibaHi");
    }

    @GetMapping("stream/url")
    public ArrayList<String> getStreamUrl() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        list.add("https://embed.api.video/live/li6MeFh3UB2qfir2298ibaHi");
        list.add("Name");
        list.add("Desc");
        //return streamUtils.generateStreamKey(name);
        return list;
    }
}
