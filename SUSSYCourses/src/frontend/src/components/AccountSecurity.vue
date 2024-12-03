<template>
  <div class="back-button" @click="goBack">
    <ArrowLeft class="back-icon"/>
    Back
  </div>
  <div class="profile-container">
    <el-form :model="passwordForm" label-width="120px">
      <!-- Current Password -->
      <el-form-item label="Current Password">
        <el-input
            :type="showPassword ? 'text' : 'password'"
            v-model="passwordForm.currentPassword"
            placeholder="Enter your current password"
        >
          <template #suffix>
            <el-button @click="toggleShowPassword" type="text" size="small">
              <component :is="showPassword ? Hide : View" style="width: 1em; height: 1em"/>
            </el-button>
          </template>
        </el-input>
      </el-form-item>
      <!-- New Password -->
      <el-form-item label="New Password">
        <el-input
            :type="showPassword ? 'text' : 'password'"
            v-model="passwordForm.newPassword"
            placeholder="Enter your new password"
        >
          <template #suffix>
            <el-button @click="toggleShowPassword" type="text" size="small">
              <component :is="showPassword ? Hide : View" style="width: 1em; height: 1em"/>
            </el-button>
          </template>
        </el-input>
      </el-form-item>

      <!-- Confirm Password -->
      <el-form-item label="Confirm Password">
        <el-input
            :type="showPassword ? 'text' : 'password'"
            v-model="passwordForm.confirmPassword"
            placeholder="Confirm your new password"
        >
          <template #suffix>
            <el-button @click="toggleShowPassword" type="text" size="small">
              <component :is="showPassword ? Hide : View" style="width: 1em; height: 1em"/>
            </el-button>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitPasswordChange">Update Password</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import {ArrowLeft, Hide, View} from '@element-plus/icons-vue';
import axiosInstances from "@/services/axiosInstance";

const router = useRouter();

const goBack = () => {
  router.back();
};

const passwordForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: '',
});

const showPassword = ref(false);

const toggleShowPassword = () => {
  showPassword.value = !showPassword.value;
};

const submitPasswordChange = async () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    alert('Passwords do not match');
  } else {
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        alert('User ID not found');
        return;
      }

      const response = await axiosInstances.axiosInstance.put(`users/${userId}/change-password`, {
        currentPassword: passwordForm.value.currentPassword,
        newPassword: passwordForm.value.newPassword,
        confirmPassword: passwordForm.value.confirmPassword,
      });

      if (response.status === 200) {
        alert('Password updated successfully');
        router.push({name: 'LogIn'});
      }
    } catch (error) {
      const errorMessage = error.response?.data || "Failed to change password";
      alert(errorMessage);
    }
  }
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
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-top: 10%;
}
</style>
  