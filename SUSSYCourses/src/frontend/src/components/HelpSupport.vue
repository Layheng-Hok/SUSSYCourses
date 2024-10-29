<template>
    <div class="back-button" @click="goBack">
      <ArrowLeft class="back-icon" /> Back
    </div>
    <div class="intro-section">
      <h1>Help and Support:</h1>
    </div>
  
  
  </template>
  
  <script setup>
import { useRouter } from 'vue-router';
import { ArrowLeft } from '@element-plus/icons-vue';


const router = useRouter(); 

const goBack = () => {
  router.back();
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
  font-size: 5px;
  margin-right: 5px;
}

.back-button:hover {
  color: #0056b3;
}
 
  .intro-section {
    text-align: center;
    margin: 100px auto 50px auto; /* Adjust spacing top right bottom left*/
    max-width: 800px; /* Optional: limit the text width */
  }
  
  .intro-section h1 {
    font-size: 36px;
    font-weight: bold;
    margin-bottom: 5px !important;
  }
  
  
  </style>/
  