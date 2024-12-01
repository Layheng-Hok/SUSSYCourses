<template>
  <!-- Top Navigation Menu -->
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
    <el-menu-item index="0">
      <router-link to="/">
        <img class="nav-logo" src="@/assets/Banner/banner2.png" alt="Element logo"/>
      </router-link>
    </el-menu-item>
    <el-menu-item index="1" @click="toggleSidebar" class="sidebar-toggle">
    <div class="user-info">
      <div class="user-details">
        <span class="user-points">Points: {{ user?.points ||0}}</span>
        <span class="user-level">Level: {{ user?.level || 0 }}</span>
      </div>
      <img class="profile-pic-small" :src="user?.profileImageUrl || defaultProfilePic" alt="Profile Picture"/>
    </div>
  </el-menu-item>
  </el-menu>

  <!-- Hero Banner Section -->
  <div class="hero-banner">
    <HeroBanner :userFullName="user?.fullName"/>
  </div>

  <div class="page-container">
    <div v-if="isSidebarVisible" class="overlay" @click="toggleSidebar"></div>

    <!-- Main Content Section -->
    <div class="main-content">
      <!-- Search and Filter Section -->
      <div class="search-filter-section">
      <el-input
        placeholder="Search for courses..."
        v-model="searchQuery"
        @keyup.enter="filterCourses"
        clearable
        prefix-icon="el-icon-search"
      ></el-input>

      <el-select
        v-model="selectedCategory"
        placeholder="Select Category"
        @change="filterCourses"
        clearable
      >
        <el-option
          v-for="topic in categories"
          :key="topic"
          :label="topic"
          :value="topic"
        />
      </el-select>

      <!-- Sorting Dropdown -->
      <el-select
        v-model="sortBy"
        placeholder="Sort By"
        @change="sortCourses"
        clearable
      >
        <el-option label="Alphabetical (A to Z)" value="alphabetical-1" />
        <el-option label="Alphabetical (Z to A)" value="alphabetical-2" />

        <el-option label="By Progress (from Low to High)" value="progress-1" />
        <el-option label="By Progress (from High to Low)" value="progress-2" />

      </el-select>

        <!-- Enrollment Status Filter -->
        <el-select
        v-model="selectedEnrollmentStatus"
        placeholder="Filter by Enrollment Status"
        @change="filterCourses"
        clearable
      >
        <el-option label="All Statuses" value="all" />
        <el-option label="Enrolled" value="enrolled" />
        <el-option label="Pending" value="pending" />
        <el-option label="Rejected" value="rejected" />
      </el-select>
    </div>

      <!-- Course Boxes Section -->
      <h2 class="section-heading">
      {{ selectedEnrollmentStatus === 'all' ? 'All Courses: ' : 'Courses that are: '+ selectedEnrollmentStatus }}
    </h2>
          <div class="course-boxes">
        <div
            v-for="(course, index) in filteredCourses"
            :key="index"
            class="course-box"
            @click="goToCourse(course.courseId)"
        >
          <img :src="course?.coverImageUrl || defaultCoverPic" alt="Course Image" class="course-image"/>
          <h3>{{ course.courseName }}</h3>
          <p class="course-instructor"> Intrucstor: {{ course.teacherName }} </p>
          <p class="course-topic"> Category: {{ course.topic }}</p>
          
          <p class="course-type"> Type: {{ course.type }}</p>
          <p class="course-status"> Status: <b> {{ course.enrollmentStatus }}</b></p>
          <p class="course-progress"> Learning progress: NA %</p>
        </div>
      </div>
    </div>
        <!-- Course Breakdown Section -->
        <div class="course-breakdown-container">
      <h2>Course Enrollment Breakdown</h2>
      <p>Visualize your enrolled courses by topic:</p>
      <CourseBreakdown />
    </div>

    <!-- Call to Action Section -->
    <div class="call-to-action">
      <h1>Explore for more courses</h1>
      <p>We have courses over the categories of web development, programming and etc.</p>
      <router-link to="/courselist">
        <button class="cta-button">Go explore</button>
      </router-link>
    </div>

    <!-- Sidebar Component -->
    <ProfileSidebar
        :user="user"
        :activeIndex="activeIndex"
        :isVisible="isSidebarVisible"
        @menuSelect="handleMenuSelect"
    />

    <Chat/>
  </div>
</template>

<script setup>
import {computed, ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import Chat from './Chat.vue';
import HeroBanner from './HeroBanner.vue';
import ProfileSidebar from '@/components/ProfileSidebar.vue';
import CourseBreakdown from './CourseBreakdown.vue';
import axiosInstances from '@/services/axiosInstance';

const router = useRouter();
const user = ref(null); 
const courses = ref([]);
const userId = localStorage.getItem('userId');

const defaultProfilePic = "/assets/Avatars/student.jpg";
const defaultCoverPic = "/assets/Courses/whale.png";
const activeIndex = ref('1');
const isSidebarVisible = ref(false);

const categories = ref(['All', 'Web Development','Marketing', 'Programming','Finance','Leadership','Data Science', 'Design','Hardware','Economics']);
const searchQuery = ref('');
const selectedCategory = ref('All');
const sortBy = ref("");
const selectedEnrollmentStatus = ref('all'); 

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
    }    
  }
};

const fetchCourseData = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`students/${userId}/courses`);
    courses.value = response.data.courses;
    // console.log("c:"+ courses.value.coverImageUrl)
  } catch (error) {

    console.log("Error Details:", error);
    if (error.response && error.response.status === 403) {
      router.push({ name: 'ForbiddenPage' });
    } else {
      console.error("Unexpected error occurred:", error);
    }    
  }
};

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};

const filteredCourses = computed(() => {
  let filtered = courses.value.filter((course) => {
    const matchesSearch = course.courseName.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesCategory = selectedCategory.value === 'All' || course.topic === selectedCategory.value;
    const matchesStatus = selectedEnrollmentStatus.value === 'all' || course.enrollmentStatus === selectedEnrollmentStatus.value;

    return matchesSearch && matchesCategory && matchesStatus;
  });
  
  if (sortBy.value === 'alphabetical-1') {
    filtered.sort((a, b) => a.courseName.localeCompare(b.courseName)); // A to Z
  } else if (sortBy.value === 'alphabetical-2') {
    filtered.sort((a, b) => b.courseName.localeCompare(a.courseName)); // Z to A
  } else if (sortBy.value === 'progress-1') {
    filtered.sort((a, b) => a.progress - b.progress); // Low to High
  } else if (sortBy.value === 'progress-2') {
    filtered.sort((a, b) => b.progress - a.progress); // High to Low
  }
  
  return filtered;
});

const sortCourses = () => {
  // Computed property will automatically re-run
};

const filterCourses = () => {
  // console.log("Filtering courses...");
};

const goToCourse = (courseId) => {
  router.push({name: 'CoursePage', params: {courseId}});
};

const handleMenuSelect = (index) => {
  activeIndex.value = index;
};

onMounted(async () => {
  await fetchUserData();
  await fetchCourseData();
});
</script>

<style scoped>

html {
  scroll-behavior: smooth;
}

.el-menu-demo {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  background-color: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); 
}

.nav-logo {
  width: 240px;
  height: 90px;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

.el-menu-demo .el-menu-item:hover {
  box-shadow: none !important; 
  background-color: transparent !important;
}

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

.user-points,
.user-level {
  display: block;
}

.sidebar-toggle {
  margin-left: auto;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.profile-pic-small {
  width: 55px;
  height: 55px;
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
  cursor: pointer;
}

.hero-banner {
  position: relative;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  height: 400px;
  background: linear-gradient(to bottom right, #f3e5f5, #e1f5fe); 
  color: #003366; 
  text-align: left;
  border-radius: 10px; 
  box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1); 
  margin: 10px auto;
  margin-bottom: 40px;
  padding: 20px;
  gap: -100px !important;
  opacity: 0;
  transform: translateY(-50px);
  animation: slide-in 1s ease forwards;
}

@keyframes slide-in {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.main-content {
  flex: 1;
  margin-left: 20px;
  transition: transform 0.3s ease;
}

.search-filter-section {
  display: flex;
  align-items: center;
  justify-content: left;

  gap: 20px;
  margin: 20px 0;
  margin-left: 130px;
  font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
}

.el-input,
.el-select {
  width: 250px;
}

.section-heading {
  font-size: 36px;
  font-weight: bold;
  color: #333;
  margin: 40px 0 10px 0;
  text-align: center;
  font-family:'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
}

.course-boxes {
  display: flex;
  flex-wrap: wrap; 
  justify-content: center;
  gap: 20px; 
  padding: 20px; 
  margin: 0px auto; 
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
}

.course-box {
  flex: 1 1 calc(25% - 40px); 
  max-width: 260px; 
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 12px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  text-align: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  opacity: 0;
  transform: translateY(20px);
  animation: fadeIn 0.5s ease forwards;
}

@keyframes fadeIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.course-box:hover {
  transform: scale(1.05);
  background-color: #f0f0f0;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2); 
  transition: transform 0.3s ease, background-color 0.3s ease, box-shadow 0.3s ease;
}

.course-box h3 {
  font-size: 20px;
  margin: 15px 0 10px 0;
}

.course-box p.course-instructor {
  font-size: 16px;
  color: #555;
}
.course-box p.course-type {
  font-size: 16px;
  color: #555;
}

.course-box p.course-progress {
  font-size: 13.5px;
  color: #555;
  opacity: 0.8;
}

.course-image {
  width: 100%;
  height: 135px;
  border-radius: 8px;
  transition: transform 0.3s ease;
}

.course-image:hover {
  transform: scale(1.1); 
}

.call-to-action {
  background-color: #f8f9fa;
  text-align: center;
  padding: 30px;
  margin-top: 50px;
  border-radius: 12px;
  font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif
}

.call-to-action h1 {
  font-size: 36px;
  color: #333;
}

.call-to-action p {
  font-size: 18px;
  color: #666;
}

.cta-button {
  color: white;
  padding: 15px 50px;
  border: none;
  font-size: 18px;
  font-weight: bold;
  border-radius: 8px;
  cursor: pointer;
  background-color: #555;
  transform: scale(1.1); 
  transition: transform 0.3s ease, background-color 0.3s ease;
}

.cta-button:hover {
  background-color: #444;
}

.course-breakdown-container {
  margin: 40px auto;
  padding: 20px;
  background: linear-gradient(to bottom right, #f3e5f5, #e1f5fe); 
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  max-width: 1000px;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
}

.course-breakdown-container h2 {
  font-size: 32px;
  font-weight: bold;
  color: #4a148c; 
  margin-bottom: 10px;
}

.course-breakdown-container p {
  font-size: 20px;
  color: #666;
  margin-bottom: 20px;
}
</style>