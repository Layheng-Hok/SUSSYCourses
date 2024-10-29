<template>
  <div class="profile-container">
    <!-- Profile Picture -->
    <div class="profile-picture-section">
      <img :src="user.profilePic" alt="Profile Picture" class="profile-pic" />
    </div>

    <!-- User Information -->
    <div class="profile-info">
      <p><strong>Username:</strong> {{ user.name }}</p>
      <p><strong>Email:</strong> {{ user.email }}</p>
      <p><strong>Gender:</strong> {{ user.gender }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const user = ref({
  name: 'John Doe',
  email: 'john.doe@example.com',
  profilePic: "/assets/Avatars/student.jpg",
  gender: 'Male',
});
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-top: 10%;
}

.profile-picture-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.profile-pic {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

.profile-info p {
  margin: 10px 0;
}
</style>
