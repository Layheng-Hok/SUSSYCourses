package com.sustech.cs309.project.sussycourses.utils;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class CloudUtils {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./.env")
            .filename(".env")
            .load();

    public static String getStorageKey(String filePath) throws IOException {
        if (filePath == null) {
            return null;
        }
        String projectId = CloudUtils.readStorageKey(dotenv.get("STORAGE_KEY"));
        return CloudUtils.generateV4GetObjectSignedUrl(projectId, "sussycourses", filePath, dotenv.get("STORAGE_KEY"));
    }


    public static void putStorageKey(MultipartFile file, String fileType, String fileLocation) throws Exception {
        if (fileLocation.equals("File Type Not Supported")) {
            return;
        }
        String projectId = CloudUtils.readStorageKey(dotenv.get("STORAGE_KEY"));
        CloudUtils.uploadObject(projectId, "sussycourses", fileLocation, file, fileType);
    }

    public static void deleteBlob(String fileLocation) throws Exception {
        String projectId = CloudUtils.readStorageKey(dotenv.get("STORAGE_KEY"));
        CloudUtils.deleteObject(projectId, "sussycourses", fileLocation);
    }

    public static String resolveUserProfilePictureLocation(Long userId, String profilePicture) {
        if (profilePicture == null || profilePicture.trim().isEmpty()) {
            return null;
        }
        return "Users/" + userId + "/" + profilePicture;
    }

    public static String resolveCourseCoverImageLocation(Long courseId, String coverPhotoName) {
        if (coverPhotoName == null || coverPhotoName.trim().isEmpty()) {
            return null;
        }
        return "Courses/" + courseId + "/" + coverPhotoName;
    }

    public static String resolveCommentAttachmentLocation(Long commentId, String attachmentName) {
        if (attachmentName == null || attachmentName.trim().isEmpty()) {
            return null;
        }
        return "Comments/" + commentId + "/" + attachmentName;
    }

    private static String generateV4GetObjectSignedUrl(String projectId, String bucketName, String objectName, String serviceAccountKeyPath) throws StorageException, IOException {
        ServiceAccountCredentials credentials = ServiceAccountCredentials
                .fromStream(new FileInputStream(serviceAccountKeyPath));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();

        // Define resource
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();

        URL url = storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());

        return url.toString();
    }

    private static String readStorageKey(String filePath) {
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

    private static String uploadObject(
            String projectId, String bucketName, String objectName, MultipartFile file, String fileType) throws IOException {

        ServiceAccountCredentials credentials = ServiceAccountCredentials
                .fromStream(new FileInputStream(dotenv.get("STORAGE_KEY")));

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


    private static String deleteObject(String projectId, String bucketName, String objectName) throws IOException {
        ServiceAccountCredentials credentials = ServiceAccountCredentials
                .fromStream(new FileInputStream(dotenv.get("STORAGE_KEY")));

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
        Bucket bucket = storage.get(bucketName);
        Blob blob = bucket.get(objectName);
        blob.delete();
        return "Successful delete";
    }
}
