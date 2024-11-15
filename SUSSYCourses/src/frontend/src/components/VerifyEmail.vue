<template>
  <div class="verification-card">
    <h2>Verify Email</h2>
    <div v-if="message" :class="messageClass">{{ message }}</div>
    <div v-if="message" class="login-link">
      <a href="#" @click="goToLogin">Log In</a>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import axios from 'axios';
import {useRoute, useRouter} from 'vue-router';

const message = ref('');
const messageClass = ref('');
const route = useRoute();
const router = useRouter();

const goToLogin = () => {
  router.push('/login');
};

onMounted(async () => {
  const token = route.query.verificationToken;
  try {
    const response = await axios.get(`http://localhost:8081/verify-email?verificationToken=${token}`);
    message.value = response.data.message;
    messageClass.value = 'alert alert-success';
  } catch (error) {
    message.value = error.response.data.message || 'Verification failed.';
    messageClass.value = 'alert alert-danger';
  }
});
</script>

<style scoped>
.verification-card {
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: white;
  text-align: center;
  margin-top: 20vh;
}

.login-link {
  margin-top: 1rem;
}

.login-link a {
  color: #74B3E3;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
