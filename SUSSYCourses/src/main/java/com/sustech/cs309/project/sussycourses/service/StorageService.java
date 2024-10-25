package com.sustech.cs309.project.sussycourses.service;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.google.cloud.storage.HttpMethod;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class StorageService {
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

    public static void generateV4PutObjectSignedUrl(
            String projectId, String bucketName, String objectName) throws StorageException {
        // String projectId = "my-project-id";
        // String bucketName = "my-bucket";
        // String objectName = "my-object";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        // Define Resource
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();

        // Generate Signed URL
        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", "application/octet-stream");

        URL url =
                storage.signUrl(
                        blobInfo,
                        15,
                        TimeUnit.MINUTES,
                        Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                        Storage.SignUrlOption.withExtHeaders(extensionHeaders),
                        Storage.SignUrlOption.withV4Signature());

        System.out.println("Generated PUT signed URL:");
        System.out.println(url);
        System.out.println("You can use this URL with any user agent, for example:");
        System.out.println(
                "curl -X PUT -H 'Content-Type: application/octet-stream' --upload-file my-file '"
                        + url
                        + "'");
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
}

