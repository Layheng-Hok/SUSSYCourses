<template>
  <!-- Back Button -->
  <div class="back-button" @click="goBack">
    <ArrowLeft class="back-icon" /> Back
  </div>

  <!-- Intro Section -->
  <div class="intro-section">
    <h1>Notifications ({{ totalNotifications }})</h1>
  </div>

  <!-- Toggle Between Received and Sent Messages -->
  <div class="toggle-section">
    <el-button
      :type="activeTab === 'received' ? 'primary' : 'default'"
      @click="switchTab('received')"
    >
      Received Messages
    </el-button>
    <el-button
      :type="activeTab === 'sent' ? 'primary' : 'default'"
      @click="switchTab('sent')"
    >
      Sent Messages
    </el-button>
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
        <!-- Subject -->
        <h2 class="notification-title">{{ notification.subject }}</h2>

        <!-- Email Information -->
        <p v-if="activeTab === 'received'" class="notification-sender">
          <strong>From:</strong> {{ notification.senderFullName }} ({{ notification.senderEmail }})
        </p>
        <p v-else class="notification-sender">
          <strong>To:</strong> {{ notification.receiverFullName }} ({{ notification.receiverEmail }})
        </p>

        <!-- Message -->
        <p class="notification-message">{{ notification.text }}</p>

        <!-- Timestamp -->
        <div class="notification-footer">
          <span class="notification-time">Sent: {{ formatDate(notification.createdAt) }}</span>
        </div>
      </div>
    </el-card>
  </div>

  <!-- Pagination Section -->
  <div class="pagination-section">
    <el-pagination
      background
      layout="prev, pager, next"
      :page-size="pageSize"
      :total="totalNotifications"
      @current-change="fetchNotifications"
      :current-page="currentPage"
    ></el-pagination>
    <!-- Notifications Per Page Dropdown -->
    <el-select v-model="pageSize" class="page-size-dropdown" @change="fetchNotifications(currentPage)" placeholder="Notifications Per Page">
      <el-option label="20" :value="20" />
      <el-option label="50" :value="50" />
      <el-option label="100" :value="100" />
    </el-select>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft } from '@element-plus/icons-vue';
import axiosInstances from '@/services/axiosInstance';

const router = useRouter();
const goBack = () => {
  router.back();
};

// Pagination variables
const currentPage = ref(1);
const pageSize = ref(20); 
const totalNotifications = ref(0);
const notifications = ref([]);
const activeTab = ref('received'); // Default to 'received' messages

const fetchNotifications = async (page = 1) => {
  try {
    const userId = localStorage.getItem('userId'); 
    const endpoint =
      activeTab.value === 'received'
        ? `users/${userId}/mailbox`
        : `users/${userId}/sent`;

    const response = await axiosInstances.axiosInstance.get(endpoint, {
      params: {
        page: page - 1, 
        size: pageSize.value,
      },
    });

    notifications.value = response.data.notifications;
    totalNotifications.value = response.data.totalNotifications;
    currentPage.value = page; 
  } catch (error) {
    console.error('Error fetching notifications:', error);
  }
};

const switchTab = (tab) => {
  activeTab.value = tab; 
  fetchNotifications(1); 
};

const formatDate = (isoDate) => {
  const date = new Date(isoDate);
  return date.toLocaleString();
};

onMounted(() => {
  fetchNotifications();
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
  
  .toggle-section {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin: 0 auto;
  margin-bottom: 30px;
}
  .notifications-list {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px; 
  margin: 0 120px;
}

.notification-card {
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #e4e4e4;
  transition: box-shadow 0.3s;
}

.notification-card:hover {
  box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1);
}

.notification-content {
  display: flex;
  flex-direction: column;
  gap: 10px; 
}

.notification-title {
  font-size: 24px;
  font-weight: bold;
  color: #000;
  margin-bottom: 10px;
}

.notification-sender {
  font-size: 16px;
  color: #555;
}

.notification-message {
  font-size: 16px;
  color: #333;
  line-height: 1.5;
}

.notification-footer {
  margin-top: 15px;
  font-size: 12px;
  color: #888;
  text-align: right; 
}

.pagination-section {
  margin: 40px 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-size-dropdown {
  margin-left: 20px;
  max-width: 100px;
}
  </style>
  