<template>
    <h2>Announcement</h2>
  
    <!-- Announcement Form -->
    <form @submit.prevent="submitAnnouncement" class="announcement-form">
      <!-- Subject Input -->
      <div class="form-item">
        <label for="subject">Subject:</label>
        <input 
          type="text" 
          id="subject" 
          v-model="announcementForm.subject" 
          placeholder="Enter subject" 
          :class="{'input-error': subjectError}"
        />
        <span v-if="subjectError" class="error-message">{{ subjectError }}</span>
      </div>
  
      <!-- Message Input -->
      <div class="form-item">
        <label for="message">Message:</label>
        <textarea 
          id="message" 
          v-model="announcementForm.message" 
          placeholder="Enter message" 
          rows="5" 
          :class="{'input-error': messageError}"
        ></textarea>
        <span v-if="messageError" class="error-message">{{ messageError }}</span>
      </div>
  
      <div class="form-item">
        <button type="submit" class="submit-button">Send Announcement</button>
      </div>
    </form>
  
    <el-alert 
      v-if="notificationStatus"
      :title="notificationStatus"
      type="success"
      closable
    />
  </template>
  
  <script setup>
  import { ref } from 'vue';
    import { useRoute } from 'vue-router';
  import axiosInstances from '@/services/axiosInstance';
  
    const route = useRoute();
  const userId = localStorage.getItem('userId');
  const courseId = route.params.courseId;
  const subjectError = ref('');
  const messageError = ref('');
  const announcementForm = ref({
    subject: '',
    message: ''
  });
  const notificationStatus = ref('');
  
  const submitAnnouncement = async () => {
    subjectError.value = '';
    messageError.value = '';
  
    let isValid = true;
  
    if (!announcementForm.value.subject) {
      subjectError.value = 'Please input the subject';
      isValid = false;
    }
  
    if (!announcementForm.value.message) {
      messageError.value = 'Please input the message';
      isValid = false;
    }
  
    if (!isValid) return;
  
    try {
      const { subject, message } = announcementForm.value;
      const receiverEmail = null;
  
      const response = await axiosInstances.axiosInstance.post(`/instructors/${userId}/courses/${courseId}/notify-all`, {
        receiverEmail,
        subject,
        text:message
      });
  
      if (response.status === 200) {
        notificationStatus.value = 'Announcement sent successfully!';
        announcementForm.value.subject = '';
        announcementForm.value.message = '';
      } else {
        notificationStatus.value = 'Failed to send announcement.';
      }
    } catch (error) {
      notificationStatus.value = 'Error sending announcement.';
      console.error(error);
    }
  };
  </script>
<style scoped>

.announcement-form {
  display: flex;
  flex-direction: column;
}

.form-item {
  margin-bottom: 20px;
}

input, textarea {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
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
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
}

.submit-button:hover {
  background-color: #45a049;
}
</style>