<template>
<div class="back-button" @click="goBack">
      <ArrowLeft class="back-icon" /> Back
    </div>
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
      <p><strong>Your Role:</strong> {{ user.role }}</p>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft } from '@element-plus/icons-vue';


const router = useRouter(); 

const goBack = () => {
  router.back();
};
const user = ref({
  name: 'John Doe',
  email: 'john.doe@example.com',
  profilePic: "/assets/Avatars/student.jpg",
  gender: 'Male',
  role: 'Student',

});
</script>

<style scoped>
.back-button {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #007bff;
  font-weight: bold;
  margin: 1% 0 0 1%;
  font-family: 'Aptos Narrow', sans-serif;
}

.back-icon {
  width: 20px;
  height: 20px;
  font-size: 5px;
  margin-right: 5px;
}

.back-button:hover {
  color: #0056b3;
}
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
