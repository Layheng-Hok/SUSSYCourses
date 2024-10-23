<template>
  <el-menu
    class="el-menu-demo"
    mode="horizontal"
    :ellipsis="false"
    @select="handleSelect"
  >
    <el-menu-item index="0">
      <router-link to="/">
        <img
          src="@/assets/logo.png"
          alt="Element logo"
        /> 
      </router-link>
    </el-menu-item>
    <el-menu-item index="1">
      <router-link to="/teacherpage">Teach on SUSSY</router-link>
    </el-menu-item>
    <el-menu-item index="2">
      <router-link to="/login">Log in</router-link>
    </el-menu-item>
    <el-menu-item index="3">
      <router-link to="/signupstudent">Sign up</router-link>
    </el-menu-item>
    <!-- Sidebar Toggle Profile Picture -->
    <el-menu-item index="4" @click="toggleSidebar" class="sidebar-toggle">
       <img class="profile-pic-small" :src="user.profilePic" alt="Profile Picture" />
    </el-menu-item>
  </el-menu>

  <div class="page-container">
    <!-- Carousel Section -->
    <!-- <div :class="['carousel-container', {'content-shifted': isSidebarVisible}]">
      <el-carousel
        motion-blur
        :interval="5000"
        arrow="never"
        class="custom-carousel"
      >
        <el-carousel-item v-for="(img, index) in images" :key="index">
          <img :src="img.src" :alt="img.alt" class="carousel-image" />
        </el-carousel-item>
      </el-carousel>
    </div> -->

    <!-- Course Boxes Section -->
    <div :class="['course-boxes', {'content-shifted': isSidebarVisible}]">
      <div 
        v-for="(course, index) in courses" 
        :key="index" 
        class="course-box" 
        @click="goToCourse(course.link)"
      >
        <h3>{{ course.title }}</h3>
      </div>
    </div>

    <!-- Right Sidebar -->
    <el-aside class="right-sidebar" :class="{'visible': isSidebarVisible}">
      <!-- Profile Section -->
      <div class="profile-section">
        <img class="profile-pic-large" :src="user.profilePic" alt="Profile Picture" />
        <h3>{{ user.name }}</h3>
        <p>{{ user.email }}</p>
      </div>

      <!-- Sidebar Links -->
      <el-menu
        class="sidebar-menu"
        :default-active="activeIndex"
        @select="handleMenuSelect"
      >
        <el-menu-item index="1">
          <router-link to="/profilepage">User's Profile</router-link>
        </el-menu-item>
        <el-menu-item index="2">
          <router-link to="/">Log Out</router-link>
        </el-menu-item>
        <el-menu-item index="3">
          <router-link to="/policy">View Policy</router-link>
        </el-menu-item>
        <el-menu-item index="4">
          <router-link to="/help">Help Center</router-link>
        </el-menu-item>
      </el-menu>
    </el-aside>
  </div>
</template>


<script setup>
import { ref } from 'vue';

const user = ref({
  name: 'John Doe',
  email: 'john.doe@example.com',
  profilePic: require('@/assets/hutao.jpg'), // Replace with actual image path
});

const activeIndex = ref('1');

const isSidebarVisible = ref(false);

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};

const courses = ref([
  { title: 'Course 1', link: '/coursepage' },
  { title: 'Course 2', link: '/course/2' }, //
  { title: 'Course 3', link: '/course/3' }, //
  { title: 'Course 4', link: '/course/4' }, //
]);

const goToCourse = (link) => {
  window.location.href = link;
};

//const images = [
  //{ src: require('@/assets/img.png'), alt: 'Image 1' },
//];
</script>

<style scoped>
.page-container {
  display: flex;
  justify-content: space-between;
  position: relative;
}

.carousel-container {
  width: 100%;
  margin-top: 75px;
  transition: transform 0.3s ease;
}

.carousel-container.content-shifted {
  transform: translateX(-250px); /* Move left when sidebar opens */
}

.custom-carousel {
  width: 100%;
  overflow: hidden;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.el-carousel__item {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent;
}

.course-boxes {
  display: flex;
  justify-content: space-around;
  margin: 20px auto;
  padding: 20px;
  gap: 20px;
  width: 100%;
  transition: transform 0.3s ease; /* Add smooth transition */
}

.course-boxes.content-shifted {
  transform: translateX(-250px); /* Shift boxes to the left */
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

.right-sidebar {
  width: 250px;
  background-color: #f9f9f9;
  padding: 20px;
  border-left: 1px solid #ddd;
  position: fixed;
  top: 75px;
  right: -250px;
  height: calc(100% - 75px);
  transition: right 0.3s ease;
}

.right-sidebar.visible {
  right: 0;
}

.profile-section {
  text-align: center;
  margin-bottom: 20px;
}

.profile-pic-large {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 10px;
}

.profile-pic-small {
  width: 12px; 
  height: 12px;
  border-radius: 50%; 
  object-fit: cover;
}

.sidebar-menu .el-menu-item {
  padding: 10px 20px;
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
}

.el-menu-demo .el-menu-item:not(:first-child):hover {
  color: purple !important;
}

.el-menu-item.is-active {
  background-color: transparent !important;
  border-bottom: none !important;
}

.el-menu-demo .el-menu-item a {
  text-decoration: none !important;
}

/* Sidebar Toggle Icon Styling */
.sidebar-toggle {
  margin-left: auto;
  cursor: pointer;
  display: flex;
  align-items: center;
}
</style>

