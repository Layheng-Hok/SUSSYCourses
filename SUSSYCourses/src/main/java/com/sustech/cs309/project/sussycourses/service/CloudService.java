package com.sustech.cs309.project.sussycourses.service;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.google.cloud.storage.HttpMethod;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class CloudService {
    public static String generateV4GetObjectSignedUrl(
            String projectId, String bucketName, String objectName, String serviceAccountKeyPath) throws StorageException, IOException {
        ServiceAccountCredentials credentials = ServiceAccountCredentials
                .fromStream(new FileInputStream(serviceAccountKeyPath));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();

        // Define resource
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();

        URL url = storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());

        return url.toString();
    }


    public static String readStorageKey(String filePath) {
        try {
            // Read the JSON file content into a String
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the JSON content
            JSONObject jsonObject = new JSONObject(content);

            // Return project_id

            return jsonObject.getString("project_id");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String uploadObject(
            String projectId, String bucketName, String objectName, MultipartFile file, String fileType) throws IOException {

        ServiceAccountCredentials credentials = ServiceAccountCredentials
                .fromStream(new FileInputStream("SUSSYCourses/src/main/config/storage_key.json"));

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(fileType)  // Set the content type to image/png
                .build();

        // Optional: set a generation-match precondition to avoid potential race
        // conditions and data corruptions. The request returns a 412 error if the
        // preconditions are not met.
        Storage.BlobWriteOption precondition;
        if (storage.get(bucketName, objectName) == null) {
            // For a target object that does not yet exist, set the DoesNotExist precondition.
            // This will cause the request to fail if the object is created before the request runs.
            precondition = Storage.BlobWriteOption.doesNotExist();
        } else {
            // If the destination already exists in your bucket, instead set a generation-match
            // precondition. This will cause the request to fail if the existing object's generation
            // changes before the request runs.
            precondition =
                    Storage.BlobWriteOption.generationMatch(
                            storage.get(bucketName, objectName).getGeneration());
        }
//        storage.createFrom(blobInfo, Paths.get(filePath), precondition);
        try (InputStream inputStream = file.getInputStream()) {
            storage.createFrom(blobInfo, inputStream);
        }


        return "Successful upload";
    }

    public static String fixName(String fileType, String fileName){
        if(Objects.equals(fileType, "video/mp4")){
            return "Courseware/videos/" + fileName;
        }
        else if(Objects.equals(fileType, "application/pdf")){
            return "Courseware/pdf/" + fileName;
        }
        else if(Objects.equals(fileType, "image/jpeg") || Objects.equals(fileType, "image/png")){
            return "Courseware/images/" + fileName;
        }
        else if(Objects.equals(fileType, "text/markdown")){
            return "Courseware/md/" + fileName;
        }
        return "File Type Not Supported";
    }
}

