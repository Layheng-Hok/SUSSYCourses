<template>
    <div class="profile-container">
      <!-- Avatar Selection -->
      <div class="profile-picture-section">
        <img :src="user.profilePic" alt="Profile Picture" class="profile-pic" />
        <div class="avatar-selection">
          <img
            v-for="(avatar, index) in avatars"
            :key="index"
            :src="avatar"
            class="avatar-option"
            @click="selectAvatar(avatar)"
          />
        </div>
      </div>
  
      <!-- User Information Form -->
      <el-form :model="user" label-width="120px">
        <el-form-item label="Username">
          <el-input v-model="user.name" placeholder="Enter your name"></el-input>
        </el-form-item>
  
        <el-form-item label="Gender">
          <el-radio-group v-model="user.gender">
            <el-radio label="Male">Male</el-radio>
            <el-radio label="Female">Female</el-radio>
            <el-radio label="Other">Other</el-radio>
          </el-radio-group>
        </el-form-item>
  
        <el-form-item>
          <el-button type="primary" @click="submitForm">Save Changes</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const user = ref({
    name: 'John Doe',
    profilePic: "/assets/Avatars/student.jpg",
    gender: 'Male',
  });
  
  const avatars = [
    "/assets/Avatars/student.jpg",
    "/assets/Avatars/student2.webp",
    "/assets/Avatars/student3.jpg",
  ];
  
  const selectAvatar = (avatar) => {
    user.value.profilePic = avatar;
  };
  
  const submitForm = () => {
    console.log('Profile updated:', user.value);
    // Save changes logic goes here
  };
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
  
  .avatar-selection {
    display: flex;
    gap: 10px;
    margin-top: 10px;
  }
  
  .avatar-option {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    cursor: pointer;
    object-fit: cover;
  }
  
  .avatar-option:hover {
    border: 2px solid #007bff;
  }
  </style>
  