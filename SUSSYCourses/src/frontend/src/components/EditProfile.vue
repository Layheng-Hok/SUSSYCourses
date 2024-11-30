<template>
  <div class="back-button" @click="goBack">
    <ArrowLeft class="back-icon" /> Back
  </div>
  <div class="profile-container">
    <!-- Profile Picture Section -->
    <div class="profile-picture-section">
      <img :src="profileImagePreview || user?.profileImageUrl || defaultProfilePic" alt="Profile Picture" class="profile-pic" />
      <div class="upload-avatar">
        <label class="upload-label">
          Upload New Picture
          <input type="file" accept="image/*" @change="handleImageChange" hidden />
        </label>
      </div>
    </div>

    <!-- User Information Form -->
    <el-form :model="user" label-width="120px">
      <el-form-item label="Username">
        <el-input v-model="user.fullName" placeholder="Enter your name"></el-input>
      </el-form-item>

      <el-form-item label="Gender">
        <el-radio-group v-model="user.gender">
          <el-radio label="Male">Male</el-radio>
          <el-radio label="Female">Female</el-radio>
          <el-radio label="Other">Other</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="Bio">
        <el-input
          type="textarea"
          v-model="user.bio"
          placeholder="Enter your bio description"
          maxlength="300"
          show-word-limit
          :rows="4"
        ></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm">Save Changes</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
  
<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft } from '@element-plus/icons-vue';
import axiosInstances from '@/services/axiosInstance';

const router = useRouter();
const goBack = () => router.back();

const user = ref({
  profilePic: null,
  fullName: '',
  gender: '',
  bio: '',
});
const userId = localStorage.getItem('userId');
const defaultProfilePic = '/assets/Avatars/student.jpg';

const profileImage = ref(null); 
const profileImagePreview = ref(null);
const fileNameWithoutExtension = ref('');
const fileType = ref('');
const roleName = localStorage.getItem('roleName');

const fetchUserData = async () => {
  try {
    console.log("Role Name:", roleName);
    let endpoint = '';
    
    if (roleName === 'INSTRUCTOR') {
      endpoint = `instructors/${userId}`;
    } else if (roleName === 'STUDENT') {
      endpoint = `students/${userId}`;
    } else {
      console.error("Unknown role:", roleName);
      return;
    }

    const response = await axiosInstances.axiosInstance.get(endpoint);
    user.value = response.data;
  } catch (error) {
    console.log("Error Details:", error);
    if (error.response && error.response.status === 403) {
      router.push({ name: 'ForbiddenPage' });
    } else {
      console.error("Unexpected error occurred:", error);
    }  }
};

const handleImageChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    profileImage.value = file;
    profileImagePreview.value = URL.createObjectURL(file);

    const fullFileName = file.name;
    fileNameWithoutExtension.value = fullFileName.substring(0, fullFileName.lastIndexOf('.'));
    fileType.value = fullFileName.substring(fullFileName.lastIndexOf('.') + 1);
  }
};

const submitForm = async () => {
  try {
    if (!user.value.fullName || !user.value.gender || !user.value.bio) {
      alert('Please fill in all the required fields.');
      return;
    }

    const formData = new FormData();
    formData.append('fullName', user.value.fullName);
    formData.append('gender', user.value.gender);
    formData.append('bio', user.value.bio);

    if (profileImage.value) {
      formData.append('profilePictureName', fileNameWithoutExtension.value);
      formData.append('fileType', fileType.value);
      formData.append('profilePicture', profileImage.value);
    }

    const response = await axiosInstances.axiosInstance.put(`users/update/${userId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    if (response.status === 200) {
      console.log('Profile updated successfully:', response.data);
      alert('Profile updated successfully!');
    } else {
      throw new Error(response.data.message || 'Failed to update profile.');
    }
  } catch (error) {
    console.error('Error updating profile:', error);
    alert('An error occurred while updating the profile. Please try again.');
  }
};

onMounted(fetchUserData);
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
max-width: 600px;
margin: 0 auto;
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
width: 120px;
height: 120px;
border-radius: 50%;
object-fit: cover;
border: 2px solid #ddd;
}

.upload-label {
display: inline-block;
padding: 8px 15px;
background-color: #4caf50;
color: white;
border-radius: 4px;
cursor: pointer;
transition: background-color 0.3s;
margin-top: 15px;
}

.upload-label:hover {
background-color: #45a049;
}

input[type="file"] {
display: none; 
}
</style>
  