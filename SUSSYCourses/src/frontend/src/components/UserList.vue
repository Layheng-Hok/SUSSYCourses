<template>
  <div>
    <h2>List of Users</h2>
    <ul>
      <li v-for="user in users" :key="user.email">
        <strong>{{ user.fullName }}</strong> - {{ user.email }}
      </li>
    </ul>
  </div>
</template>

<!-- ListOfUsers.vue -->
<script setup>
import {onMounted, ref} from 'vue';
import axiosInstance from '@/services/axiosInstance';

const users = ref([]); // Reactive variable to store the list of users

// Fetch users with axios instance
const fetchUsers = async () => {
  try {
    const response = await axiosInstance.get('/admin/users');
    users.value = response.data;
  } catch (error) {
    console.error('Error fetching users:', error);
  }
};

onMounted(fetchUsers);

</script>

<style scoped>
/* Add any custom styling here */
</style>
