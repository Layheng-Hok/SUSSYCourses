<template>
  <!-- Top Navigation Menu -->
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
    <el-menu-item index="0">
      <router-link to="/">
        <img src="@/assets/logo.png" alt="Element logo" />
      </router-link>
    </el-menu-item>
    <el-menu-item index="1">
      <router-link to="/teacherpage">Teach on SUSSY</router-link>
    </el-menu-item>
    <el-menu-item index="2">
      <router-link to="/login">Log in</router-link>
    </el-menu-item>
    <el-menu-item index="3">
      <router-link to="/signup-student">Sign up</router-link>
    </el-menu-item>
    <el-menu-item index="4" @click="toggleSidebar" class="sidebar-toggle">
      <img class="profile-pic-small" :src="user.profilePic" alt="Profile Picture" />
    </el-menu-item>
  </el-menu>

  <div class="page-container">
    <!-- Overlay for Sidebar Toggle -->
    <div v-if="isSidebarVisible" class="overlay" @click="toggleSidebar"></div>
  
    <!-- Course Boxes Section -->
    <div :class="['course-boxes', { 'content-shifted': isSidebarVisible }]">
      <div
      v-for="(course, index) in courses"
      :key="index"
      class="course-box"
      @click="goToCourse(course.id)"
      >
      <h3>{{ course.title }}</h3>
    </div>
    </div>
    
    <!-- Sidebar Component -->
    <ProfileSidebar
    :user="user"
    :activeIndex="activeIndex"
    :isVisible="isSidebarVisible"
    @menuSelect="handleMenuSelect"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router'; 
import ProfileSidebar from '@/components/ProfileSidebar.vue';

const user = ref({
  name: 'John Doe',
  email: 'john.doe@example.com',
  profilePic: "/assets/Avatars/student.jpg",
});

const activeIndex = ref('1');
const isSidebarVisible = ref(false);
const router = useRouter();

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};

// Updated course list with unique course IDs
const courses = ref([
  { id: 1, title: 'React Course' },
  { id: 2, title: 'Vue Course' },
]);

// Route to corresponding course page based on courseId
const goToCourse = (courseId) => {
  router.push({ name: 'CoursePage', params: { courseId } });
};

const handleMenuSelect = (index) => {
  activeIndex.value = index;
};
</script>

<style scoped>
.page-container {
  display: flex;
  justify-content: space-between;
  position: relative;
}

.course-boxes {
  display: flex;
  justify-content: left;
  margin-top: 50px;
  padding: 10px;
  gap: 10px;
  width: 50%;
  transition: transform 0.3s ease;
}

.course-boxes.content-shifted {
  transform: translateX(-250px);
}

.course-box {
  width: 200px;
  height: 100px;
  background-color: #e0e0e0;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.course-box:hover {
  background-color: #c0c0c0;
}

.el-menu-demo {
  background-color: white;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  height: 75px;
  padding: 0 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.el-menu-demo img {
  width: 60px;
  height: auto;
  object-fit: contain;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

.el-menu-demo .el-menu-item {
  font-size: 18px;
  color: black !important;
  background-color: transparent !important;
  transition: color 0.3s;
  font-family: 'Aptos Narrow', sans-serif;
}

.el-menu-demo .el-menu-item:not(:first-child):hover {
  color: #74B3E3 !important;
}

.el-menu-item.is-active {
  background-color: transparent !important;
  border-bottom: none !important;
}

.el-menu-demo .el-menu-item a {
  text-decoration: none !important;
}

.el-menu-item:nth-child(3) a {
  border: 1px solid black;
  padding: 5px 16px;
  color: black;
  height: 30px;
  line-height: 30px;
  font-weight: 550;
  background-color: white;
  transition: background-color 0.3s, color 0.3s;
  font-size: 16px;
  font-family: 'Aptos Narrow', sans-serif;
}

.el-menu-item:nth-child(3) a:hover {
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
}

.el-menu-item:nth-child(4) a {
  border: 1px solid #74B3E3;
  background-color: #74B3E3;
  color: white;
  padding: 5px 10px;
  height: 30px;
  line-height: 30px;
  font-weight: bold;
  transition: background-color 0.3s, color 0.3s;
  font-size: 16px;
  font-family: 'Aptos Narrow', sans-serif;
}

.el-menu-item:nth-child(4) a:hover {
  background-color: #9DCAEB;
  border: 1px solid #9DCAEB;
  color: white;
}

.el-menu-item:nth-child(3) {
  margin-right: -25px; 
}

.el-menu-item:nth-child(4) {
  margin-left: 0; 
}

.sidebar-toggle {
  margin-left: auto;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.profile-pic-small {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  overflow: hidden;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
}
</style>
