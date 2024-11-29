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
        <div class="overlay" @click="triggerFileInput">
          <el-icon>
            <picture-filled/>
          </el-icon>
        </div>
        <input ref="fileInput" type="file" accept="image/*" @change="handleImageChange" hidden/>
      </div>

      <!-- Basic Info -->
      <div class="basic-info">
        <h2>
    <span v-if="!isEditingName" @click="enableNameEdit">
      {{ user.name }}
      <el-icon class="edit-icon"><edit/></el-icon>
    </span>
          <el-input
              v-else
              v-model="user.name"
              placeholder="Enter your name"
              @blur="disableNameEdit"
          />
        </h2>
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
  <el-button type="primary" :loading="isSaving" @click="checkChanges" class="save-button">Save</el-button>
</template>

<script setup>
import {computed, ref, onMounted} from 'vue';
import {PictureFilled, Edit} from "@element-plus/icons-vue";
import {ElMessage} from 'element-plus';
import axiosInstances from "@/services/axiosInstance";
import {useRouter} from 'vue-router';

const router = useRouter();

const isEditingName = ref(false);
const enableNameEdit = () => {
  isEditingName.value = true;
};

const disableNameEdit = () => {
  isEditingName.value = false;
};

const user = ref({
  name: 'Loading...',
  email: 'Fetching...',
  gender: 'Loading...',
  role: 'Loading...',
  created: 'Loading...',
  profilePic: null,
  bio: '',
  initials: computed(() => user.value.name.charAt(0).toUpperCase()),
});

const backupUser = ref({...user.value});
const hasChanges = computed(() => {
  return (
      user.value.name !== backupUser.value.name ||
      user.value.bio !== backupUser.value.bio ||
      user.value.gender !== backupUser.value.gender ||
      user.value.profilePic !== backupUser.value.profilePic // add more checks for other fields as needed
  );
});

const checkChanges = () => {
  if (hasChanges.value) {
    saveChanges();
  } else {
    ElMessage.info('No changes made');
  }
};

const cancelChanges = () => {
  user.value = JSON.parse(JSON.stringify(backupUser.value));
  newProfilePic.value = null;
  isModalVisible.value = false;
  ElMessage.info('Changes canceled');

  const userId = localStorage.getItem("userId");
  console.log('Redirecting to TeacherDashboard with userId:', userId);
  router.push({name: 'TeacherDashboard', params: {userId: userId}, query: {tab: 'courses', _r: Date.now()}});

};

const userId = localStorage.getItem('userId');
const isSaving = ref(false);
const profileImage = ref(null);
const fileNameWithoutExtension = ref('');
const fileType = ref('');

const fileInput = ref(null);
const triggerFileInput = () => {
  fileInput.value.click();
};

const handleImageChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    profileImage.value = file;
    user.value.profilePic = URL.createObjectURL(file);

    const fullFileName = file.name;
    fileNameWithoutExtension.value = fullFileName.substring(0, fullFileName.lastIndexOf('.'));
    fileType.value = fullFileName.substring(fullFileName.lastIndexOf('.') + 1);
  }
};

const saveChanges = async () => {
      try {
        isSaving.value = true;
        if (!user.value.name || !user.value.gender || !user.value.bio) {
          ElMessage.error('Please fill in all the required fields.')
          return;
        }

        const formData = new FormData();

        formData.append('fullName', user.value.name);
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

          backupUser.value = JSON.parse(JSON.stringify(user.value));
          newProfilePic.value = null;
          ElMessage.success("Profile updated successfully!");

          router.push({name: 'TeacherDashboard', params: {userId: userId}, query: {tab: 'courses', _r: Date.now()}});
        } else {
          throw new Error(response.data.message || 'Failed to update profile.');
        }
      } catch
          (error) {
        ElMessage.error("Failed to save changes. Please try again.");
      } finally {
        isSaving.value = false;
      }
    }
;

const isModalVisible = ref(false);
const newProfilePic = ref(null);

onMounted(async () => {
  try {
    const userId = localStorage.getItem("userId");
    if (!userId) {
      console.error("No userId found in localStorage!");
      return;
    }

    const response = await axiosInstances.axiosInstance.get(`/instructors/${userId}`);
    const userData = response.data;

    user.value.name = userData.fullName || "No Name";
    user.value.email = userData.email || "No Email";
    user.value.gender = userData.gender || "Not specified";
    user.value.role = userData.roleName || "Not specified";
    user.value.created = userData.createdAt || "Not available";
    user.value.profilePic = userData.profileImageUrl || null;
    user.value.bio = userData.bio || null;

    backupUser.value = JSON.parse(JSON.stringify(user.value));

  } catch (error) {
    console.error("Error fetching user data:", error);
  }
});
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

.basic-info h2 .edit-icon {
  font-size: 21px;
  margin-left: 2px;
  color: #333;
  cursor: pointer;
  transition: color 0.3s ease;
}

.basic-info h2 .edit-icon:hover {
  color: #74b3e3;
}

.basic-info h2 .el-input {
  font-size: 1.2rem;
  color: #333;
  width: 300px;
}
</style>
