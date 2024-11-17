<template>
    <!-- Back Button -->
    <div class="back-button" @click="goBack">
      <ArrowLeft class="back-icon" /> Back
    </div>
  
    <!-- Intro Section -->
    <div class="intro-section">
      <h1>Notifications</h1>
    </div>
  
    <!-- Actions -->
    <div class="actions">
      <el-button @click="markAllAsRead" type="primary" size="small">Mark All as Read</el-button>
      <el-button @click="clearNotifications" type="danger" size="small">Clear Notifications</el-button>
    </div>
  
    <!-- Notifications List -->
    <div class="notifications-list">
      <div v-if="notifications.length === 0" class="no-notifications">
        No notifications to show.
      </div>
      <el-card
        v-for="(notification, index) in notifications"
        :key="index"
        class="notification-card"
        shadow="hover"
      >
        <div class="notification-content">
          <h3 class="notification-title">{{ notification.title }}</h3>
          <p class="notification-message">{{ notification.message }}</p>
          <span class="notification-time">{{ notification.time }}</span>
        </div>
        <el-button @click="markAsRead(index)" type="text" size="small" v-if="!notification.read">Mark as Read</el-button>
      </el-card>
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
  
  // to be replaced with data from backend
  const notifications = ref([
    { title: "Welcome!", message: "Thank you for signing up.", time: "2 hours ago", read: false },
    { title: "Assignment Due", message: "Your assignment is due tomorrow.", time: "1 day ago", read: false },
    { title: "New Message", message: "You have a new message from your instructor.", time: "3 days ago", read: true },
  ]);
  
  const markAllAsRead = () => {
    notifications.value.forEach(notification => notification.read = true);
  };
  
  const clearNotifications = () => {
    notifications.value = [];
  };
  
  const markAsRead = (index) => {
    notifications.value[index].read = true;
  };
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
    margin-right: 5px;
  }
  
  .back-button:hover {
    color: #0056b3;
  }
  
  .intro-section {
    text-align: center;
    margin: 40px auto;
    max-width: 800px;
  }
  
  .intro-section h1 {
    font-size: 36px;
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .actions {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin: 20px 0;
  }
  
  .notifications-list {
    max-width: 800px;
    margin: 0 auto;
  }
  
  .notification-card {
    margin-bottom: 15px;
  }
  
  .notification-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .notification-title {
    font-size: 18px;
    font-weight: bold;
    margin: 0;
  }
  
  .notification-message {
    font-size: 14px;
    color: #666;
    margin: 5px 0;
  }
  
  .notification-time {
    font-size: 12px;
    color: #999;
  }
  
  .no-notifications {
    text-align: center;
    color: #999;
    font-size: 16px;
    margin-top: 20px;
  }
  </style>
  