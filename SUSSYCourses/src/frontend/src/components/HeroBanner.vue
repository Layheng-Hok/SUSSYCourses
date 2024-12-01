<template>
      <div class="hero-content">
    <div class="hero-text">
      <h1 class="typing-text">Welcome back, {{ userFullName || 'Student' }}</h1>
      <p>Discover your next adventure in learning!</p>
    </div>
  </div>
  <div class="hero-image">
    <img src="@/assets/Banner/whale.png" alt="Whale Image" class="whale-image" />
  </div>
</template>

<script setup>

import { defineProps } from 'vue';
defineProps({
  userFullName: {
    type: String,
    default: 'Student'  
  }
});
</script>

<style>

.typing-text {
  display: inline-block;
  border-right: 2px solid #4caf50;
  white-space: nowrap;
  overflow: hidden;
  animation: typing 3s steps(30, end), blink 0.5s step-end infinite;
}

@keyframes typing {
  from {
    width: 0;
  }
  to {
    width: 100%;
  }
}

@keyframes blink {
  from,
  to {
    border-color: transparent;
  }
  50% {
    border-color: #4caf50;
  }
}

.hero-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start; 
  gap: 10px;
  margin-left: 200px;
  margin-top: 30px;
}
.hero-text h1 {
  font-size: 40px;
  font-family: 'Poppins', sans-serif; 
  font-weight: 600;
  margin: 0;
  color: #4a148c; 
}

.hero-text p {
  font-size: 24px;
  font-family: 'Roboto', sans-serif; 
  margin-top: 10px;
  color: #00695c; 
}

.hero-image {
  margin-top: 38px;
  flex: 1;
  display: flex;
  justify-content: center; 
  align-items: center;
  margin-left: -100px;
}

.whale-image {
  max-width: 100%;
  height: auto;
  max-height: 340px; 
}

</style>