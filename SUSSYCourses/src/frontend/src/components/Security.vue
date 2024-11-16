<template>
  <div class="header">
    <h1>Account Security</h1>
  </div>
  <div class="content-container">

    <div class="content">
      <!-- Email Section -->
      <div class="input-container" @click="showEmailMessage">
        <p>Email:</p>
        <div class="input-box">
          <input type="text" :value="user.email" class="disabled-input" disabled/>
        </div>
      </div>

      <!-- Password Section -->
      <div class="input-container">
        <p>Password:</p>
        <div class="input-box">
          <input type="password" value="********" disabled/>
          <el-icon class="edit-icon" @click="openPasswordModal">
            <edit/>
          </el-icon>
        </div>
      </div>
    </div>

    <img src="@/assets/password5.gif" alt="Password Gif" class="gif"/>

    <div class="modal">
      <!-- Modal for Changing Password -->
      <el-dialog v-model="isPasswordModalVisible" title="Change Password" class="modal-title">
        <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-position="top">
          <el-form-item label="Current Password" prop="currentPassword">
            <el-input placeholder="Enter current password" v-model="passwordForm.currentPassword" type="password"/>
          </el-form-item>
          <el-form-item label="New Password" prop="newPassword">
            <el-input placeholder="Enter new password" v-model="passwordForm.newPassword" type="password"/>
          </el-form-item>
          <el-form-item label="Confirm New Password" prop="confirmPassword">
            <el-input placeholder="Re-type new password" v-model="passwordForm.confirmPassword" type="password"/>
          </el-form-item>
        </el-form>

        <!-- Modal Footer -->
        <template #footer>
          <el-button @click="isPasswordModalVisible = false" class="cancel-button">Cancel</el-button>
          <el-button type="primary" @click="changePassword" class="submit-button">Change Password</el-button>
        </template>
      </el-dialog>

    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {ElMessage} from "element-plus";
import "element-plus/theme-chalk/el-message.css";
import {Edit} from "@element-plus/icons-vue";

const user = ref({
  email: "johnDoe@gmail.com",
});

const isPasswordModalVisible = ref(false);

const passwordForm = ref({
  currentPassword: "",
  newPassword: "",
  confirmPassword: "",
});

const passwordRules = {
  currentPassword: [{required: true, message: "Please enter your current password", trigger: "blur"}],
  newPassword: [{required: true, message: "Please enter a new password", trigger: "blur"}],
  confirmPassword: [
    {required: true, message: "Please confirm your new password", trigger: "blur"},
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error("Passwords do not match"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
};

const showEmailMessage = () => {
  ElMessage.info("Cannot modify email.");
};

const openPasswordModal = () => {
  isPasswordModalVisible.value = true;
};

const changePassword = () => {
  const formRef = passwordForm.value;
  if (!formRef.currentPassword || !formRef.newPassword || !formRef.confirmPassword) {
    ElMessage.error("Please complete all fields!");
    return;
  }
  if (formRef.newPassword !== formRef.confirmPassword) {
    ElMessage.error("Passwords do not match!");
    return;
  }

  ElMessage.success("Password changed successfully!");
  isPasswordModalVisible.value = false;
  passwordForm.value = {currentPassword: "", newPassword: "", confirmPassword: ""};
};
</script>

<style scoped>
.header h1 {
  color: black;
  margin-top: 90px;
  text-align: left;
  margin-left: 80px;
  font-family: Garamond;
}

.content {
  margin-left: 180px;
  margin-top: 50px;
  text-align: left;
}

.input-container {
  margin-bottom: 20px;
}

p {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 0;
  color: black;
}

.input-box {
  background-color: transparent;
  max-width: 400px;
  position: relative;
}

input {
  width: 100%;
  padding: 20px;
  margin-top: 5px;
  margin-bottom: 5px;
  border: 1px solid #333;
  font-size: 16px;
  color: #333 !important;
  box-sizing: border-box;
  position: relative;
}

.edit-icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  color: #333;
}

.edit-icon:hover {
  color: #74B3E3;
}

.submit-button {
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
  margin-top: 0 !important;
}

.submit-button:hover {
  background-color: #9DCAEB;
  color: white;
  border: 1px solid #9DCAEB;
}

.cancel-button {
  background-color: white;
  color: #74B3E3;
  border: 1px solid #74B3E3;
  margin-top: 0 !important;
}

.cancel-button:hover {
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
}

.content-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.content {
  flex: 1;
}

.gif {
  margin-top: 20px;
  max-width: 410px;
  height: auto;
  margin-right: 180px;
  align-self: flex-start;
}

.modal {
  text-align: left;
}

.disabled-input {
  pointer-events: none;
}
</style>

<style>
.el-dialog__title {
  color: black !important;
  font-family: Garamond !important;
  font-size: 18px !important;
  font-weight: bold !important;
}
</style>

