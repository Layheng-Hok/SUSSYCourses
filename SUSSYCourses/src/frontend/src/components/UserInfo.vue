<template>
    <div class="user-info">
      <div class="user-details">
        <!-- <span class="user-points">Points: {{ points }}</span> -->
        <span class="user-level">Level: {{ level }}</span>
      </div>
      <div class="progress-bar-container">
        <el-progress
          :percentage="progressPercentage"
          :text-inside="true"
          :stroke-width="26"
          :show-text="true"
          :status="progressStatus"
          :format="progressFormat"
                  ></el-progress>
      </div>
      <img class="profile-pic-small" :src="profileImage" alt="Profile Picture"/>
    </div>
  </template>
  
<script setup>
import { computed, defineProps } from 'vue';

const props = defineProps({
user: {
    type: Object,
    required: true,
}
});

const defaultProfilePic = "/assets/Avatars/student.jpg";    
const points = computed(() => props.user?.points || 0);
const level = computed(() => Math.floor(props.user?.points / 100));
const progressPercentage = computed(() => points.value % 100);
const progressStatus = computed(() => (progressPercentage.value === 100 ? 'success' : 'active'));
const profileImage = computed(() => props.user?.profileImageUrl || defaultProfilePic);

const progressFormat = (percentage) => {
  return `${percentage} Points`;
};

</script>
  
  <style scoped>
  .user-info {
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.user-details {
  margin-right: 20px;
  font-size: 16px;
  color: #333;
  line-height: 1.2; 
}

.progress-bar-container {
width: 150px;
margin-right: 16px;
}

::v-deep .el-progress-bar__innerText {
color: black;
font-size: 13px;
margin-left: 9px;
}
  .profile-pic-small {
  width: 55px;
  height: 55px;
  border-radius: 50%;
  object-fit: cover;
  overflow: hidden;
}
  </style>
  