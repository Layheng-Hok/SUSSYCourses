<template>
  <div class="header">
    <h1>Notifications</h1>
    <hr class="divider"/>
    <!-- Check if there are notifications -->
    <div v-if="notifications.length > 0">
      <ul class="notification-list">
        <li v-for="notification in notifications" :key="notification.id" class="notification-item">
          <p>{{ notification.message }}</p>
          <span class="timestamp">{{ notification.timestamp }}</span>
        </li>
      </ul>
    </div>
    <p v-else>No notifications.</p>
  </div>
</template>

<script setup>
import {ref} from "vue";

// Mock notifications
const notifications = ref([
  {id: 1, message: "Student John Doe enrolled in your course Mathematics 101", timestamp: "2024-11-16 12:45"},
  {id: 2, message: "Student Jane Smith enrolled in your course Physics 202", timestamp: "2024-11-15 09:30"},
]);
</script>

<style scoped>
.header h1 {
  color: black;
  margin-top: 90px;
  text-align: left;
  margin-left: 80px;
  font-family: Garamond;
}

.divider {
  border: none;
  border-top: 1px solid #ccc;
  margin: 10px 80px;
}

.header p {
  color: #777;
  text-align: left;
  margin-left: 330px;
  font-size: 19px;
}

.notification-list {
  list-style-type: none;
  padding: 0;
  margin-left: 80px;
  margin-right: 80px;
}

.notification-item {
  padding: 15px;
  margin-bottom: 10px;
  background-color: white;
  display: flex;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #ddd;
  justify-content: space-between;
}

.notification-item p {
  margin: 0;
  font-size: 16px;
  color: black;
}

.timestamp {
  font-size: 12px;
  color: #777;
  align-self: flex-end;
}
</style>
