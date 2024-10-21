<template>
    <div class="profile-container">
      <!-- Profile Picture Section -->
      <div class="profile-picture-section">
        <img :src="user.profilePic" alt="Profile Picture" class="profile-pic" />
        <input type="file" @change="onFileChange" accept="image/*" class="file-input" />
        <button @click="resetToAvatar" class="btn">Reset to Default Avatar</button>
      </div>
  
      <!-- Profile Information Section -->
      <div class="profile-info">
        <el-form :model="user" label-width="120px">
          <!-- Username -->
          <el-form-item label="Username">
            <el-input v-model="user.name" placeholder="Enter your name"></el-input>
          </el-form-item>
  
          <!-- Email -->
          <el-form-item label="Email">
            <el-input v-model="user.email" placeholder="Enter your email" disabled></el-input>
          </el-form-item>
  
          <!-- Birthday -->
          <el-form-item label="Birthday">
            <el-date-picker v-model="user.birthday" type="date" placeholder="Select your birthday"></el-date-picker>
          </el-form-item>
  
          <!-- Gender -->
          <el-form-item label="Gender">
            <el-radio-group v-model="user.gender">
              <el-radio label="Male">Male</el-radio>
              <el-radio label="Female">Female</el-radio>
              <el-radio label="Other">Other</el-radio>
            </el-radio-group>
          </el-form-item>
  
          <!-- Password -->
          <el-form-item label="Password">
            <el-input :type="showPassword ? 'text' : 'password'" v-model="user.password" placeholder="Enter your password">
              <template #suffix>
                <el-button @click="toggleShowPassword" type="text" size="small">
                  <i :class="showPassword ? 'el-icon-view' : 'el-icon-view-off'"></i>
                </el-button>
              </template>
            </el-input>
          </el-form-item>
  
          <!-- Phone Number -->
          <el-form-item label="Phone Number">
            <el-input v-model="user.phoneNumber" placeholder="Enter your phone number"></el-input>
          </el-form-item>
  
          <!-- Address -->
          <el-form-item label="Address">
            <el-input v-model="user.address" placeholder="Enter your address"></el-input>
          </el-form-item>
  
          <!-- Submit Button -->
          <el-form-item>
            <el-button type="primary" @click="submitForm">Save Changes</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const user = ref({
    name: 'John Doe',
    email: 'john.doe@example.com',
    profilePic: require('@/assets/hutao.jpg'), // Default avatar image
    birthday: '',
    gender: 'Male',
    password: 'password123',
    phoneNumber: '123-456-7890',
    address: '123 Main St, Springfield, USA',
  });
  
  const showPassword = ref(false);
  
  const onFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        user.value.profilePic = e.target.result; // Update profile picture
      };
      reader.readAsDataURL(file);
    }
  };
  
  const resetToAvatar = () => {
    user.value.profilePic = require('@/assets/hutao.jpg'); // Reset to default avatar
  };
  
  const toggleShowPassword = () => {
    showPassword.value = !showPassword.value; // Toggle password visibility
  };
  
  const submitForm = () => {
    console.log('Form submitted:', user.value);
    // You can send the updated data to the server or handle it as needed
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
    margin-bottom: 10px;
  }
  
  .file-input {
    margin-bottom: 10px;
  }
  
  .profile-info {
    width: 100%;
  }
  
  .el-form-item {
    margin-bottom: 20px;
  }
  
  .btn {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 8px 16px;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .btn:hover {
    background-color: #0056b3;
  }
  </style>
  