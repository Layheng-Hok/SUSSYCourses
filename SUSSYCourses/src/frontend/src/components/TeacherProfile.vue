<template>
  <div class="profile-container">
    <div class="profile-summary">
      <!-- Profile Picture -->
      <div class="profile-picture">
        <el-avatar :size="70" shape="circle">
          <template v-if="user.profilePic">
            <img :src="user.profilePic" alt="Profile Picture"/>
          </template>
          <template v-else>
            {{ user.initials }}
          </template>
        </el-avatar>
        <div class="overlay" @click="isModalVisible = true">
          <el-icon>
            <picture-filled/>
          </el-icon>
        </div>
      </div>
      <!-- Modal for Image Upload -->
      <el-dialog v-model="isModalVisible" title="Upload Profile Picture">
        <!-- File Input -->
        <input type="file" @change="previewImage" accept="image/*"/>
        <div v-if="newProfilePic" class="preview">
          <img :src="newProfilePic" alt="New Profile Preview"/>
        </div>

        <!-- Modal Footer -->
        <template #footer>
          <el-button @click="cancelUpload">Cancel</el-button>
          <el-button type="primary" @click="saveProfilePicture">Save</el-button>
        </template>
      </el-dialog>

      <!-- Basic Info -->
      <div class="basic-info">
        <h2>{{ user.name }}</h2>
        <p><strong>Email:</strong> {{ user.email }}</p>
        <p><strong>Role:</strong> {{ user.role }}</p>
        <p><strong>Gender:</strong> {{ user.gender }}</p>
        <p><strong>Joined:</strong> {{ user.created }}</p>
      </div>
    </div>

    <div class="bio-section">
      <p><strong>Bio</strong></p>
      <el-input v-model="user.bio" type="textarea" placeholder="Write something about yourself..."/>
      <el-form-item label="Gender" class="gender-form-item">
        <el-radio-group v-model="user.gender">
          <el-radio label="Male">Male</el-radio>
          <el-radio label="Female">Female</el-radio>
          <el-radio label="Others">Others</el-radio>
        </el-radio-group>
      </el-form-item>
    </div>
  </div>
  <el-button @click="cancelChanges" class="cancel-button">Cancel</el-button>
  <el-button type="primary" @click="saveChanges" class="save-button">Save</el-button>
</template>

<script setup>
import {computed, ref} from 'vue';
import {PictureFilled} from "@element-plus/icons-vue";
import {ElMessage} from 'element-plus';

const user = ref({
  name: 'John Doe',
  email: 'JohnDoe@mgmail.com',
  gender: 'Male',
  role: 'Teacher',
  created: '15 Nov 2024',
  profilePic: null,
  bio: '',
  initials: computed(() => user.value.name.charAt(0).toUpperCase()),
});

// edit profile logic
const backupUser = ref({...user.value});
const cancelChanges = () => {
  user.value = {...backupUser.value};
  newProfilePic.value = null;
  isModalVisible.value = false;
  ElMessage.info('Changes canceled');
};

const saveChanges = () => {
  backupUser.value = {...user.value};
  isModalVisible.value = false;
  ElMessage.success('Changes saved!');
};

const isModalVisible = ref(false);
const newProfilePic = ref(null);

const previewImage = (event) => {
  const file = event.target.files[0];
  if (file) {
    newProfilePic.value = URL.createObjectURL(file);
  }
};

const cancelUpload = () => {
  newProfilePic.value = null;
  isModalVisible.value = false;
};

const saveProfilePicture = () => {
  if (newProfilePic.value) {
    user.value.profilePic = newProfilePic.value;
    isModalVisible.value = false;
    ElMessage.success('Profile picture updated!');
  }
};
</script>

<style scoped>
.profile-summary {
  width: 40%;
  text-align: left;
  margin-top: 120px;
  margin-left: 170px;
}

.basic-info h2 {
  font-size: 1.5rem;
  margin-top: 10px;
  color: black;
}

.basic-info p {
  font-size: 1rem;
  color: black;
}

.el-avatar {
  background-color: #333 !important;
  color: white;
  font-size: 25px;
  font-weight: bold;
}

.el-avatar:hover {
  background-color: #444 !important;
}

.profile-picture {
  position: relative;
  display: inline-block;
}

.overlay {
  position: absolute;
  background-color: rgba(0, 0, 0, 0.7);
  bottom: 0;
  right: 0;
  color: white;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
}

.preview img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-top: 10px;
}

.profile-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 20px;
}

.bio-section {
  margin-top: 170px;
  margin-right: 250px;
  width: 60%;
}

.bio-section .el-input {
  width: 100%;
}

.bio-section p {
  color: black;
  font-size: 18px;
  text-align: left;
  margin-bottom: 10px;
}

.gender-form-item >>> .el-form-item__label {
  font-weight: bold;
  color: black;
  font-size: 18px;
}

.gender-form-item {
  margin-top: 20px;
  margin-bottom: 20px;
}


.save-button {
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
  margin-top: -20px !important;
}

.save-button:hover {
  background-color: #9DCAEB;
  color: white;
  border: 1px solid #9DCAEB;
}

.cancel-button {
  background-color: white;
  color: #74B3E3;
  border: 1px solid #74B3E3;
  margin-top: -20px !important;
}

.cancel-button:hover {
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
}
</style>
