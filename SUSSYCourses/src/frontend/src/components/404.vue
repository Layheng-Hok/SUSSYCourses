<template>
    <div class="not-found">
      <h1>404 - Page Not Found</h1>
      <p>Oops! The page you're looking for doesn't exist.</p>
      <router-link to="/">Go back to Home</router-link>
    </div>
  </template>
  
  <script setup>
  </script>
  
  <style scoped>
  .not-found {
    text-align: center;
    margin-top: 50px;
  }
  
  .not-found h1 {
    font-size: 3rem;
    color: #ff5555;
  }
  
  .not-found p {
    font-size: 1.5rem;
    margin: 20px 0;
  }
  </style>
  