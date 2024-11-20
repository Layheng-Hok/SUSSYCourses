<template>
<div class="back-button" @click="goBack">
      <ArrowLeft class="back-icon" /> Back
    </div>
  <div class="profile-container">
    <!-- Profile Picture -->
    <div class="profile-picture-section">
      <img :src="user?.profilePic || defaultProfilePic" alt="Profile Picture" class="profile-pic" />
    </div>

    <!-- User Information -->
    <div class="profile-info">
      <p><strong>Username:</strong> {{ user?.fullName }}</p>
    <p><strong>Email:</strong> {{ user?.email }}</p>
        <p><strong>Gender:</strong> {{ user?.gender }}</p>
      <p><strong>Your Role:</strong> {{ user?.roleName }}</p>
      <p><strong>Points:</strong> {{ user?.points }}</p>
      <p><strong>Level:</strong> {{ user?.points/100 }}</p>
      <p><strong>Bio:</strong> 
  {{ user?.bio || "No bio description yet. Add a description of yourself by going to 'Edit Profile'." }}</p>     
    <!-- <p><strong>Courses Enrolled:</strong> {{ user?.numCoursesEnrolled }}</p> -->
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft } from '@element-plus/icons-vue';
import axiosInstances from '@/services/axiosInstance';

const router = useRouter();
const user = ref(null); 
const userId = localStorage.getItem('userId'); 
const defaultProfilePic = "/assets/Avatars/student.jpg";

const fetchUserData = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`students/${userId}`);
    user.value = response.data;
  } catch (error) {
    console.log("Error Details:", error);
    if (error.response && error.response.status === 403) {
      router.push({ name: 'ForbiddenPage' });
    } else {
      console.error("Unexpected error occurred:", error);
    }  }
};

onMounted(fetchUserData);

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
.profile-container {
  font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  max-width: 600px;
  margin: auto auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-top: 5%;
}

.profile-picture-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.profile-pic {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

.profile-info {
  text-align: left;
  margin-left: 10px;
}

.profile-info p {
  margin-bottom: 8px;
}

</style>
