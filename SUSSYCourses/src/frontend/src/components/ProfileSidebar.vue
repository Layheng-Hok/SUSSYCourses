<template>
    <el-aside class="profile-sidebar" :class="{ visible: isVisible }">
      <!-- Profile Section -->
      <div class="profile-section">
        <img class="profile-pic-large" :src="user.profilePic || defaultProfilePic" alt="Profile Picture" />
        <h3>{{ user.name }}</h3>
        <p>{{ user.email }}</p>
      </div>
  
      <!-- Sidebar Links -->
      <el-menu class="sidebar-menu" :default-active="activeIndex" @select="handleMenuSelect">
        <el-menu-item index="1">
          <router-link to="/profilepage">Edit Profile</router-link>
        </el-menu-item>
        <el-menu-item index="2">
          <router-link to="/notifications">Notifications</router-link>
        </el-menu-item>
        <el-menu-item index="3">
          <router-link to="/teacherpage">Teach on SUSSY</router-link>
        </el-menu-item>
        <el-menu-item index="4">
          <router-link to="/help">Help and Support</router-link>
        </el-menu-item>
        <el-menu-item index="5">
          <router-link to="/">Log Out</router-link>
        </el-menu-item>
      </el-menu>
  
      <!-- Version Display -->
      <div class="version">v1.0.0</div>
    </el-aside>
  </template>
  
  <script setup>
  import { defineProps, defineEmits } from 'vue';
  
  defineProps({
    user: Object,
    activeIndex: String,
    isVisible: Boolean,
  });
  
  // Default profile picture as fallback
  const defaultProfilePic = "/assets/Avatars/student.jpg"; // Adjust the path as needed
  
  // Emit event for menu selection handling
  const emit = defineEmits(['menuSelect']);
  const handleMenuSelect = (index) => {
    emit('menuSelect', index);
  };
  </script>
  
  <style scoped>
  .profile-sidebar {
    width: 250px;
    background-color: #f9f9f9;
    padding: 20px;
    border-left: 1px solid #ddd;
    position: fixed;
    top: 0;
    right: 0;
    height: 100vh;
    transform: translateX(100%);
    transition: transform 0.3s ease;
    z-index: 1000;
  }
  
  .profile-sidebar.visible {
    transform: translateX(0);
  }
  
  .profile-section {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .profile-pic-large {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    object-fit: cover;
    overflow: hidden;
  }
  
  .sidebar-menu .el-menu-item {
    padding: 10px 20px;
    cursor: pointer; 
  }
  
  .version {
    position: absolute;
    bottom: 20px;
    left: 20px;
    font-size: 12px;
    color: #999;
    text-align: center;
  }
  </style>
  