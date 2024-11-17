<template>
  <!-- Top Navigation Menu -->
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
    <el-menu-item index="0">
      <router-link to="/">
        <img src="@/assets/logo2.png" alt="Element logo"/>
      </router-link>
    </el-menu-item>
    <el-menu-item index="1" @click="toggleSidebar" class="sidebar-toggle">
      <img class="profile-pic-small" :src=" user?.profilePic || defaultProfilePic" alt="Profile Picture"/>
    </el-menu-item>
  </el-menu>

  <div class="page-container">
    <div v-if="isSidebarVisible" class="overlay" @click="toggleSidebar"></div>

    <!-- Main Content Section -->
    <div :class="['main-content', { 'content-shifted': isSidebarVisible }]">
      <!-- Search and Filter Section -->
      <h3>{{ user?.fullName || 'Loading...' }}</h3>

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
          v-for="category in categories"
          :key="category"
          :label="category"
          :value="category"
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

        <el-option label="By Ratings (from Low to High)" value="rating-1" />
        <el-option label="By Ratings (from High to Low)" value="rating-2" />

        <el-option label="By Progress (from Low to High)" value="progress-1" />
        <el-option label="By Progress (from High to Low)" value="progress-2" />

      </el-select>
    </div>

      <!-- Course Boxes Section -->
      <h2 class="section-heading">Courses that you are enrolled in:</h2>
      <div class="course-boxes">
        <div
            v-for="(course, index) in filteredCourses"
            :key="index"
            class="course-box"
            @click="goToCourse(course.id)"
        >
          <img :src="course.image" alt="Course Image" class="course-image"/>
          <h3>{{ course.title }}</h3>
          <p class="course-instructor"> Intrusctor: </p>
          <p class="course-category"> Category: {{ course.category }}</p>
          
          <p class="course-rating">⭐ {{ course.rating }} / 5</p>
          <p class="course-progress"> Learning progress: {{ course.progress }}%</p>
        </div>
      </div>
    </div>
        <!-- Course Breakdown Section -->
        <div class="course-breakdown-container">
      <h2>Course Enrollment Breakdown</h2>
      <p>Visualize your enrolled courses by category:</p>
      <CourseBreakdown />
    </div>

    <!-- Call to Action Section -->
    <div class="call-to-action">
      <h1>Explore for more courses</h1>
      <p>We have courses over the categories of web development, programming and etc.</p>
      <router-link to="/">
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
  </div>
</template>

<script setup>
import {computed, ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import ProfileSidebar from '@/components/ProfileSidebar.vue';
import CourseBreakdown from './CourseBreakdown.vue';
import axiosInstances from '@/services/axiosInstance';

const user = ref(null); 
const userId = localStorage.getItem('userId'); 
const defaultProfilePic = "/assets/Avatars/student.jpg";

const fetchUserData = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`student/profile/${userId}`);
    user.value = response.data;
  } catch (error) {
    console.log("Error Details:", error);
  }
};

onMounted(fetchUserData);


const activeIndex = ref('1');
const isSidebarVisible = ref(false);
const router = useRouter();

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};

const courses = ref([
  {id: 1, title: 'React Course', image: "/assets/Courses/course.jpg", rating: 4.5, category: "Programming", progress :30},
  {id: 2, title: 'Vue Course', image: '/assets/Courses/course2.jpg', rating: 4.7, category: "Programming", progress :99.9},
  {id: 3, title: 'Python Basics', image: '/assets/courses/course3.png', rating: 4.3, category: "Programming", progress :0},
  {id: 4, title: 'Graphic Design', image: '/assets/courses/course3.png', rating: 4.6, category: "Design", progress :6},
]);

const categories = ref(['All', 'Web Development','Marketing', 'Programming','Finance','Leadership','Data Science', 'Design','Hardware','Economics']);
const searchQuery = ref('');
const selectedCategory = ref('All');
const sortBy = ref('Default');

const filteredCourses = computed(() => {
  let filtered = courses.value.filter(course => {
    const matchesSearch = course.title.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesCategory = selectedCategory.value === 'All' || course.category === selectedCategory.value;
    return matchesSearch && matchesCategory;
  });

  if (sortBy.value === 'alphabetical-1') {
    filtered.sort((a, b) => a.title.localeCompare(b.title)); // A to Z
  } else if (sortBy.value === 'alphabetical-2') {
    filtered.sort((a, b) => b.title.localeCompare(a.title)); // Z to A
  } else if (sortBy.value === 'rating-1') {
    filtered.sort((a, b) => a.rating - b.rating); // Low to High
  } else if (sortBy.value === 'rating-2') {
    filtered.sort((a, b) => b.rating - a.rating); // High to Low
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
</script>

<style scoped>
.main-content {
  flex: 1;
  margin-left: 20px;
  transition: transform 0.3s ease;
}

.main-content.content-shifted {
  transform: translateX(-250px);
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
}

.course-box:hover {
  background-color: #e0e0e0;
}

.course-box h3 {
  font-size: 20px;
  margin: 15px 0 10px 0;
}

.course-box p.course-instructor {
  font-size: 16px;
  color: #555;
}
.course-box p.course-rating {
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
  background-color: #333;
  color: white;
  padding: 15px 50px;
  border: none;
  font-size: 18px;
  font-weight: bold;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.cta-button:hover {
  background-color: #444;
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

.course-breakdown-container {
  margin: 40px auto;
  padding: 20px;
  background-color: #f0f8ff;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  max-width: 1000px; /* Optional: Adjust the width as needed */
}

.course-breakdown-container h2 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.course-breakdown-container p {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}
</style>
