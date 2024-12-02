<template>
  <h2>Start your Stream</h2>

  <!-- Stream Information -->
  <div class="stream-info">
    <!-- Stream Name Display -->
    <div class="info-item">
      <label for="streamName">Stream Name:</label>
      <input
          type="text"
          id="streamName"
          v-model="streamInfo.streamName"
          :class="{'input-error': streamNameError}"
      />
      <span v-if="streamNameError" class="error-message">{{ streamNameError }}</span>
    </div>

    <!-- Button to regenerate Stream Key -->
    <div class="info-button">
      <button @click="generateStreamKey" class="submit-button">Get Stream Key</button>
    </div>
  </div>

  <!-- Stream Key Display -->
  <div class="info-item">
    <label for="chapter">Stream Key:</label>
    <input
        type="text"
        id="streamKey"
        v-model="streamInfo.streamKey"
        readonly
        :class="{'input-error': streamKeyError}"
    />
    <span v-if="streamKeyError" class="error-message">{{ streamKeyError }}</span>
  </div>

  <div class="info-item">
    <label for="streamLink">URL:</label>
    <input
        type="text"
        id="streamLink"
        v-model="streamInfo.streamLink"
        readonly
        :class="{'input-error': streamKeyError}"
    />
    <span v-if="streamKeyError" class="error-message">{{ streamKeyError }}</span>
  </div>

  <el-alert
      v-if="notificationStatus"
      :title="notificationStatus"
      type="success"
      :closable="false"
  />

  <el-alert
      v-if="badNotificationStatus"
      :title="badNotificationStatus"
      type="error"
      :closable="false"
  />

</template>

<script setup>
import { ref } from 'vue';
import axiosInstances from '@/services/axiosInstance';

const userId = localStorage.getItem('userId');

const streamNameError = ref('');
const streamKeyError = ref('');
const streamInfo = ref({
  streamName: '',
  streamKey: '',
  streamLink: ''
});
const notificationStatus = ref('');
const badNotificationStatus = ref('');

// Function to regenerate the stream key
const generateStreamKey = async () => {
  try {
    if(streamInfo.value.streamName === ''){
      badNotificationStatus.value = 'Fill in a stream name';
      return;
    }
    const response = await axiosInstances.axiosInstance.post(`/stream/getStreamInfo/${userId}`, streamInfo.value.streamName, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    if (response.status === 200) {
      streamInfo.value.streamKey = response.data.streamKey;
      streamInfo.value.streamName = response.data.streamName.replace(/^"|"$/g, '');
      streamInfo.value.streamLink = response.data.streamLink;
      notificationStatus.value = "Your stream key has been successfully generated! Copy the key and paste it into your streaming service to begin broadcasting. Share the stream link with your students, so they can join in and watch your session live!"
    } else {
      notificationStatus.value = 'Failed to regenerate stream key.';
    }
  } catch (error) {
    notificationStatus.value = 'Error regenerating stream key.';
    console.error(error);
  }
};

</script>

<style scoped>
.stream-info {
  display: flex;
  flex-direction: column;
  max-width: 600px;
  margin: 0 auto;
}

.info-item {
  margin: 10px 0;
  text-align: left; /* Ensure all text inside is aligned to the left */
}

.info-item label {
  display: block; /* Make the label a block element so it takes the full width */
  margin-bottom: 5px; /* Add some space between the label and the input */
  font-weight: bold; /* Optional: Make the label stand out more */
}

.info-item input {
  width: 100%; /* Ensure input takes up the full width of the container */
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
}


input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin: 5px 0;
}

.input-error {
  border-color: red;
}

.error-message {
  color: red;
  font-size: 12px;
}

.submit-button {
  padding: 10px 20px;
  background-color: #9d00ff;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
}

.submit-button:hover {
  background-color: #9d00ff;
}

</style>
