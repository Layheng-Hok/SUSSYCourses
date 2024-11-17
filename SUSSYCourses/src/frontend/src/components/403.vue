<template>
    <div class="error-container">
      <h1>403 - Forbidden</h1>
      <p>You don't have permission to access this page.</p>
      <router-link to="/" class="back-link">Return to Homepage</router-link>
    </div>
  </template>
  
  <script setup>
  </script>
  
  <style scoped>
  .error-container {
    text-align: center;
    margin-top: 100px;
    font-family: 'Arial', sans-serif;
  }
  
  h1 {
    font-size: 48px;
    color: #ff6b6b;
  }
  
  p {
    font-size: 20px;
    color: #555;
  }
  
  .back-link {
    display: inline-block;
    margin-top: 20px;
    font-size: 18px;
    color: #007bff;
    text-decoration: none;
  }
  
  .back-link:hover {
    text-decoration: underline;
  }
  </style>
  