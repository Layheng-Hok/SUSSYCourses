<template>
    <div class="profile-container">
      <el-form :model="passwordForm" label-width="120px">
        <!-- New Password -->
        <el-form-item label="New Password">
          <el-input
            :type="showPassword ? 'text' : 'password'"
            v-model="passwordForm.newPassword"
            placeholder="Enter your new password"
          >
            <template #suffix>
              <el-button @click="toggleShowPassword" type="text" size="small">
                <i :class="showPassword ? 'el-icon-view' : 'el-icon-view-off'"></i>
              </el-button>
            </template>
          </el-input>
        </el-form-item>
  
        <!-- Confirm Password -->
        <el-form-item label="Confirm Password">
          <el-input
            :type="showPassword ? 'text' : 'password'"
            v-model="passwordForm.confirmPassword"
            placeholder="Confirm your new password"
          />
        </el-form-item>
  
        <el-form-item>
          <el-button type="primary" @click="submitPasswordChange">Update Password</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const passwordForm = ref({
    newPassword: '',
    confirmPassword: '',
  });
  
  const showPassword = ref(false);
  
  const toggleShowPassword = () => {
    showPassword.value = !showPassword.value;
  };
  
  const submitPasswordChange = () => {
    if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
      alert('Passwords do not match');
    } else {
      console.log('Password updated:', passwordForm.value.newPassword);
      // Implement password update logic
    }
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
  </style>
  