package com.sustech.cs309.project.sussycourses.utils;

import com.sustech.cs309.project.sussycourses.dto.StreamResponse;
import lombok.extern.slf4j.Slf4j;
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.LiveStream;
import video.api.client.api.models.LiveStreamCreationPayload;

import java.util.ArrayList;

public class StreamUtils {
    public StreamResponse generateStreamKey(String name){
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
            return new StreamResponse(name, liveStream.getStreamKey(), String.valueOf(liveStream.getAssets().getPlayer()));
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
