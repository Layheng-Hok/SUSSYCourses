package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Stream;
import com.sustech.cs309.project.sussycourses.dto.StreamResponse;
import com.sustech.cs309.project.sussycourses.repository.StreamRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.LiveStream;
import video.api.client.api.models.LiveStreamCreationPayload;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreamService {
    private final StreamRepository streamRepository;


    public StreamResponse generateStreamKey(String name, Long teacherId){
        Stream stream = streamRepository.findByTeacherId(teacherId);
        if(stream.getStreamKey() != null){
            return new StreamResponse(name, stream.getStreamKey(), "localhost:8080/sussy/stream/" + stream.getTeacher().getUserId());
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
            streamRepository.save(stream);
//            return new StreamResponse(name, liveStream.getStreamKey(), String.valueOf(liveStream.getAssets().getPlayer()));
            return new StreamResponse(name, liveStream.getStreamKey(), "localhost:8080/sussy/stream/" + stream.getTeacher().getUserId());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
