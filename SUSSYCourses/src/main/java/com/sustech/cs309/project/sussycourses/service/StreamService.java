package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.Stream;
import com.sustech.cs309.project.sussycourses.dto.StreamResponse;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.StreamRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.LiveStream;
import video.api.client.api.models.LiveStreamCreationPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreamService {
    private final StreamRepository streamRepository;
    private final CourseRepository courseRepository;

    public StreamResponse generateStreamKey(String name, Long teacherId, String description, Long courseId){
        Stream stream = streamRepository.findByTeacherId(teacherId);
        if(stream.getStreamKey() != null){
            return new StreamResponse(name, stream.getStreamKey(), "localhost:8080/sussy/stream/" + courseId);
        }

        ApiVideoClient client = new ApiVideoClient("Yhf2OhyHRdvH9MZm8oQdU1FKT98uppMG7ENahjlxnJ3");

        LiveStreamCreationPayload liveStreamCreationPayload = new LiveStreamCreationPayload();
        liveStreamCreationPayload.setName(name); // Add a name for your live stream here.
        liveStreamCreationPayload.setPublic(true); // Whether your video can be viewed by everyone, or requires authentication to see it.
//        liveStreamCreationPayload.setPlayerId("li13iCMAvpx9AWd9aekz3fng"); // The unique identifier for the player.
//        liveStreamCreationPayload.setRestreams(Collections.singletonList(new RestreamsRequestObject() // Use this parameter to add, edit, or remove RTMP services where you want to restream a live stream. The list can only contain up to 5 destinations.
//                .name("My RTMP server")
//                .serverUrl("rtmp://my.broadcast.example.com/app")
//                .streamKey("dw-dew8-q6w9-k67w-1ws8")));
        try {
            LiveStream liveStream = client.liveStreams().create(liveStreamCreationPayload);
            stream.setStreamKey(liveStream.getStreamKey());
            stream.setUrl(String.valueOf(liveStream.getAssets().getPlayer()));
            stream.setTitle(name);
            stream.setDescription(description);
            streamRepository.save(stream);
//            return new StreamResponse(name, liveStream.getStreamKey(), String.valueOf(liveStream.getAssets().getPlayer()));
            return new StreamResponse(name, liveStream.getStreamKey(), "localhost:8080/sussy/stream/" + stream.getTeacher().getUserId());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getStreamInformation(Long courseId){
        ArrayList<String> list = new ArrayList<>();
        Course course = courseRepository.findById(courseId).get();

        Stream stream = streamRepository.findByTeacherId(course.getTeacher().getUserId());
        System.out.println(stream.getUrl());
        System.out.println(stream.getTeacher().getUserId());
        list.add(stream.getUrl());
        list.add(stream.getTitle());
        list.add(stream.getDescription());
        return list;
    }
}
