<template>
    <div class="back-button" @click="goBack">
      <ArrowLeft class="back-icon" /> Back
    </div>
  
    <!-- Main Content Section -->
    <div class="main-content">
      <!-- Sorting Section -->
      <div class="search-filter-section">
        <!-- Sorting Dropdown -->
        <el-select v-model="sortBy" placeholder="Sort By" clearable>
          <el-option label="Alphabetical (A to Z)" value="alphabetical-1" />
          <el-option label="Alphabetical (Z to A)" value="alphabetical-2" />
          <el-option label="By Likes Count (from Low to High)" value="like-1" />
          <el-option label="By Likes Count (from High to Low)" value="like-2" />
          <el-option label="By Ratings (from Low to High)" value="rating-1" />
          <el-option label="By Ratings (from High to Low)" value="rating-2" />
        </el-select>
      </div>
  
      <!-- Course Boxes Section -->
      <h2 class="section-heading">All available courses ({{ totalCourses }}): </h2>
      <div class="course-boxes">
        <div
          v-for="(course, index) in filteredCourses"
          :key="index"
          class="course-box"
          @click="goToCourse(course.courseId)"
        >
          <img
            :src="course?.coverImageUrl || defaultCoverPic"
            alt="Course Image"
            class="course-image"
          />
          <h3>{{ course.courseName }}</h3>
          <p class="course-instructor">Instructor: {{ course.teacherName }}</p>
          <p class="course-likes">Likes Count: {{ course?.likesCount || 0 }} ❤️</p>
          <p class="course-rating">Rating: {{ course?.averageRating || 0 }} ⭐</p>
        </div>
      </div>
  
      <!-- Pagination Section -->
      <div class="pagination-section">
        <el-pagination
          background
          layout="prev, pager, next"
          :page-size="pageSize"
          :total="totalCourses"
          @current-change="handlePageChange"
          :current-page="currentPage"
        ></el-pagination>
  
        <!-- Pagination Size Dropdown -->
        <el-select v-model="pageSize" class="page-size-dropdown" placeholder="Courses Per Page">
          <el-option label="5" :value="5" />
          <el-option label="10" :value="10" />
          <el-option label="20" :value="20" />
          <el-option label="40" :value="40" />
        </el-select>
      </div>
    </div>
  </template>
  
  <script setup>
  import { computed, ref, onMounted, watch } from "vue";
  import { useRouter, useRoute } from "vue-router";
  import { ArrowLeft } from "@element-plus/icons-vue";
  import { debounce } from "lodash";
  import axiosInstances from "@/services/axiosInstance";
  
  const courses = ref([]);
  const defaultCoverPic = "/assets/Courses/whale.png";
  const router = useRouter();
  const route = useRoute();
  
  const sortBy = ref("");
  const currentPage = ref(1);
  const totalCourses = ref(0);
  const pageSize = ref(10); 
  
  const goBack = () => router.back();
  
  const fetchCourseData = debounce(async () => {
    try {
      const backendPage = currentPage.value - 1;
  
      const response = await axiosInstances.axiosInstance2.get(`courses/approved`, {
        params: {
          page: backendPage,
          size: pageSize.value, 
          userId: localStorage.getItem("userId") || 1,
        },
      });
  
      courses.value = [...response.data.courses];
      totalCourses.value = response.data.totalApprovedCourses || 0;
    } catch (error) {
      console.error("Error fetching courses:", error);
      if (error.response && error.response.status === 403) {
        router.push({ name: "ForbiddenPage" });
      }
    }
  }, 300);

  onMounted(() => {
    currentPage.value = parseInt(route.query.page || "1");
    pageSize.value = parseInt(route.query.pageSize || "10");
    sortBy.value = route.query.sortBy || "";
    fetchCourseData();
  });
  
  watch([currentPage, pageSize], () => {
    fetchCourseData();
  });
  
  const filteredCourses = computed(() => {
    let sorted = [...courses.value];
  
    if (sortBy.value === "alphabetical-1") {
      sorted.sort((a, b) => a.courseName.localeCompare(b.courseName));
    } else if (sortBy.value === "alphabetical-2") {
      sorted.sort((a, b) => b.courseName.localeCompare(a.courseName));
    } else if (sortBy.value === "like-1") {
      sorted.sort((a, b) => a.likesCount - b.likesCount);
    } else if (sortBy.value === "like-2") {
      sorted.sort((a, b) => b.likesCount - a.likesCount);
    } else if (sortBy.value === "rating-1") {
      sorted.sort((a, b) => (a.averageRating || 0) - (b.averageRating || 0));
    } else if (sortBy.value === "rating-2") {
      sorted.sort((a, b) => (b.averageRating || 0) - (a.averageRating || 0));
    }
  
    return sorted;
  });
  
  const handlePageChange = (page) => {
    currentPage.value = page;
  };

  const goToCourse = (courseId) => {
  router.push({name: 'PublicCoursePage', params: {courseId}});
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

  html {
    scroll-behavior: smooth;
  }

  .main-content {
    flex: 1;
    margin: 40px 0;
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

  .pagination-section {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  padding-bottom: 20px;
    font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
}

.page-size-dropdown {
  margin-left: 20px;
  max-width: 100px;
}

</style>