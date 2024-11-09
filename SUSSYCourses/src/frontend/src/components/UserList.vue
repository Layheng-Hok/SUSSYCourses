<script setup>
import {onMounted, ref} from 'vue';
import axios from 'axios';

const users = ref([]); // Reactive variable to store the list of users

// Configure axios to include credentials in cross-origin requests
const fetchUsers = async () => {
  try {
    const response = await axios.get('http://localhost:8081/admin/users', {
      withCredentials: true,
    });
    users.value = response.data;
  } catch (error) {
    console.error('Error fetching users:', error);
  }
};

onMounted(fetchUsers);

</script>

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

<style scoped>
/* Add any custom styling here */
</style>
