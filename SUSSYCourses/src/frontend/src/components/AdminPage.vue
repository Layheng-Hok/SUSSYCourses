<template>
  <div class="page-container">
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
          <el-option label="By Created Time (from Low to High)" value="createdAt-1" />
          <el-option label="By Created Time (from High to Low)" value="createdAt-2" />
        </el-select>

        <!-- Logout Button -->
        <el-button class="logout-button" type="danger" @click="logout">Log Out</el-button>
      </div>

      <!-- Course Boxes Section -->
      <h2 class="section-heading">Courses on pending:</h2>
      <div class="course-boxes">
        <div
          v-for="(course, index) in filteredCourses"
          :key="index"
          class="course-box"
        >
          <img :src="course.image" alt="Course Image" class="course-image" />
          <h3>{{ course.courseName }}</h3>
          <p class="course-instructor">Instructor: {{ course.teacherName }}</p>
          <p class="course-topic">Category: {{ course.topic }}</p>
          <p class="course-description">Description: {{ course.description }}</p>
          <p class="course-created">Created At: {{ formatDate(course.createdAt) }}</p>
          
          <!-- Approve Button -->
          <el-button
            type="success"
            size="small"
            @click="approveCourse(course.courseId)"
          >
            Approve
          </el-button>
          <el-button
          type="danger"
          size="small"
          @click="rejectCourse(course.courseId)"
        >
          Reject
        </el-button>
        </div>
      </div>
    </div>
</template>


<script setup>
import {computed, ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import axiosInstances from '@/services/axiosInstance';

const courses = ref([]);
const router = useRouter();

const categories = ref(['All', 'Web Development','Marketing', 'Programming','Finance','Leadership','Data Science', 'Design','Hardware','Economics']);
const searchQuery = ref('');
const selectedCategory = ref('All');
const sortBy = ref('Default');

const fetchUserData = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`courses/pending`);
    courses.value = response.data;
    console.log(courses.value);
  } catch (error) {
    console.log("Error Details:", error);
    if (error.response && error.response.status === 403) {
      router.push({ name: 'ForbiddenPage' });
    } else {
      console.error("Unexpected error occurred:", error);
    }  }
};

onMounted(fetchUserData);

const filteredCourses = computed(() => {
  let filtered = courses.value.filter(course => {
    console.log(course);
    const matchesSearch = course.courseName.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesCategory = selectedCategory.value === 'All' || course.topic === selectedCategory.value;
    return matchesSearch && matchesCategory;
  });

  if (sortBy.value === 'alphabetical-1') {
    filtered.sort((a, b) => a.courseName.localeCompare(b.courseName)); // A to Z
  } else if (sortBy.value === 'alphabetical-2') {
    filtered.sort((a, b) => b.courseName.localeCompare(a.courseName)); // Z to A
  } else if (sortBy.value === 'createdAt-1') {
    filtered.sort((a, b) => a.createdAt - b.createdAt); // Low to High
  } else if (sortBy.value === 'createdAt-2') {
    filtered.sort((a, b) => b.createdAt - a.createdAt); // High to Low
  } 
  return filtered;
});

const approveCourse = async (courseId) => {
try {
  await axiosInstances.axiosInstance.put(`/courses/approve/${courseId}`);
  courses.value = courses.value.filter((course) => course.courseId !== courseId);
  console.log(`Course ${courseId} approved.`);
} catch (error) {
  console.error('Error approving course:', error);
}
};

const rejectCourse = async (courseId) => {
  try {
    await axiosInstances.axiosInstance.put(`courses/reject/${courseId}`);
    courses.value = courses.value.filter((course) => course.courseId !== courseId);
    console.log(`Course ${courseId} rejected.`);
  } catch (error) {
    console.error("Error rejecting course:", error);
  }
};

const logout = () => {
  localStorage.setItem('usn',''); 
  localStorage.setItem('pwd',''); 
  localStorage.setItem('userId', ''); 
  alert("You have successfully logged out");
  router.push('/login');
};

const formatDate = (dateString) => {
const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
return new Date(dateString).toLocaleDateString(undefined, options);
};

const sortCourses = () => {
  // Computed property will automatically re-run
};

const filterCourses = () => {
  // console.log("Filtering courses...");
};

</script>

<style scoped>

.search-filter-section {
  display: flex;
  align-items: center;
  justify-content: center;

  gap: 20px;
  margin: 50px auto;
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
.course-box p.course-ratingd {
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

</style>
