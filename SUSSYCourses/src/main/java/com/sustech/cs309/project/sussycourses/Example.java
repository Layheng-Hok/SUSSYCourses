package com.sustech.cs309.project.sussycourses;



import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;
import video.api.client.api.clients.LiveStreamsApi;
import java.util.*;

public class Example {
    public static void main(String[] args) {
        ApiVideoClient client = new ApiVideoClient("Yhf2OhyHRdvH9MZm8oQdU1FKT98uppMG7ENahjlxnJ3");

        LiveStreamCreationPayload liveStreamCreationPayload = new LiveStreamCreationPayload();
        liveStreamCreationPayload.setName("My Live Stream Video"); // Add a name for your live stream here.
        liveStreamCreationPayload.setPublic(false); // Whether your video can be viewed by everyone, or requires authentication to see it.
//        liveStreamCreationPayload.setPlayerId("li13iCMAvpx9AWd9aekz3fng"); // The unique identifier for the player.
//        liveStreamCreationPayload.setRestreams(Collections.singletonList(new RestreamsRequestObject() // Use this parameter to add, edit, or remove RTMP services where you want to restream a live stream. The list can only contain up to 5 destinations.
//                .name("My RTMP server")
//                .serverUrl("rtmp://my.broadcast.example.com/app")
//                .streamKey("dw-dew8-q6w9-k67w-1ws8")));


        try {
            LiveStream liveStream = client.liveStreams().create(liveStreamCreationPayload);
            System.out.println(liveStream);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}


