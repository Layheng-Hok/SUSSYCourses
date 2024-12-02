<template>
  <div class="video-container">
    <iframe
        :src="videoUrl"
        width="100%"
        height="100%"
        frameborder="0"
        scrolling="no"
        allowfullscreen="true"
    ></iframe>
  </div>
  <div class="video-info" v-if="videoTitle || videoDescription">
    <h2 class="video-title">{{ videoTitle || "Default Stream Title" }}</h2>
    <p class="video-description">{{ videoDescription || "No description available." }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axiosInstances from "@/services/axiosInstance";

// Reactive reference to hold the video URL
const videoUrl = ref(''); // Initially empty
const videoTitle = ref('Hello');
const videoDescription = ref('Hello');

// Function to fetch or generate the stream URL
const getStreamUrl = async () => {
  try {
    // Simulating an API call or logic to get the stream URL
    const response = await axiosInstances.axiosInstance.get('stream/url');
    videoUrl.value = response.data[0];
    videoTitle.value = response.data[1];
    videoDescription.value = response.data[2];
  } catch (error) {
    console.error('Error fetching stream URL:', error);
    videoUrl.value = ''; // Fallback or error message URL
  }
};

onMounted(() => {
  getStreamUrl();
});
</script>

<style scoped>
.video-container {
  position: relative;
  width: 80%;
  padding-top: 42.25%; /* 16:9 aspect ratio */
  height: 0;
  overflow: hidden;
  max-width: 100%;
  margin: 20px auto;
  background-color: #000; /* Optional: For better contrast with the video */
}

.video-container iframe {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 8px; /* Optional: Rounded corners for iframe */
}

.video-info {
  padding: 0 calc(0.1 * 100%); /* Adjust padding on the left and right */
  text-align: left; /* Align content to the left */
  background-color: #f9f9f9; /* Light background for description */
  border-radius: 8px;
  margin-top: 10px;
  width: 80%; /* Same width as video container */
  margin-left: 0; /* Remove any default margins */
  margin-right: 0; /* Ensure it doesn't have extra right margin */
}

.video-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  border-bottom: 2px solid #ddd; /* Add bottom border below the title */
  padding-bottom: 10px; /* Add padding for some space between title and border */
}

.video-description {
  font-size: 16px;
  color: #666;
  line-height: 1.6;
  margin: 0;
  width: 100%;
}


</style>
