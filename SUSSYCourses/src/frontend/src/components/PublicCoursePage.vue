<template>
    <div class="back-button" @click="goBack">
      <ArrowLeft class="back-icon" /> Back
    </div>
  
    <div class="page-container">
      <div 
        v-if="isSidebarVisible" 
        class="overlay"
        @click="toggleSidebar">
      </div>
      <div v-if="course" class="content-container">
        <!-- Left Side: Courseware, Enroll Button-->
        <div class="left-section">
          <!-- Courseware Section -->
          <div class="courseware-section">
            <h2>Courseware</h2>
            <PublicCourseware :course-id="courseId" />
          </div>
          <div class="enroll-section">
            <p>If you're interested in this course, click below to enroll and start learning!</p>
            <button @click.stop="enrollInCourse(course.courseId)" class="enroll-button">Enroll Now</button>
        </div>

        </div>
  
        <!-- Right Side: Course Details and Instructor Information -->
        <div class="right-section">
          <!-- Course Details -->
          <el-card class="course-details" shadow="hover">
            <h2>Course Details</h2>
            <img :src="course?.coverImageUrl || defaultCoverPic" alt="Course Image" class="course-image" />
            <h3><strong>{{ course.courseName }}</strong></h3>
            <p class="description">{{ course?.description || "No description for this course yet."}}</p>
            <p><strong>Category:</strong> {{ course.topic }}</p>
            <p><strong>Rating:</strong> ⭐ {{ course?.averageRating || 0}}</p>
            <p><strong>Likes Count:</strong> ❤️ {{ course?.likesCount || 0 }}</p>
          </el-card>
          
          <!-- Instructor Information -->
          <el-card class="instructor-info" shadow="hover">
            <h2>Instructor Information</h2>
            <div>
        <!-- Teacher image with tilt effect -->
        <img
          ref="teacherImage"
          :src="course?.teacherProfilePictureUrl || defaultTeacherProfilePic"
          alt="Instructor Image"
          class="instructor-image"
        />
      </div>
          <div>
             <p><strong>{{ course.teacherName }}</strong></p>
              <p class="bio">{{ course?.teacherBio || "The teacher doesn't want to introduce him/herself" }}</p>
            </div>
          </el-card>
        </div>
      </div>
  
      <div v-else>
        <p>Loading...</p>
      </div>
      
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, onUnmounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { ArrowLeft } from "@element-plus/icons-vue";
  import axiosInstances from '@/services/axiosInstance';
  import VanillaTilt from "vanilla-tilt";
    import PublicCourseware from './PublicCourseware.vue';
  
  const teacherImage = ref(null);
  const route = useRoute();
  const router = useRouter();
  const course = ref(null);
  const courseId = route.params.courseId;
const userId = localStorage.getItem('userId');  
  
  const isSidebarVisible = ref(false);
  const defaultTeacherProfilePic = "/assets/Avatars/instructor.jpg";
  const defaultCoverPic = "/assets/Courses/whale.png";

    const enrollInCourse = async (courseId) => {
  try {
    if (!userId || userId === 1) {
    alert('Please log in before you enroll in a course.');
    router.push({ name: 'LogIn' });
    return; 
  }
    const response = await axiosInstances.axiosInstance.post(`/students/${userId}/courses/${courseId}/enroll`);
    console.log('Enrollment successful:', response.data);
    alert('You have been successfully enrolled in the course!');
  } catch (error) {
    console.error('Error enrolling in course:', error);
    alert('There was an error enrolling in the course. Please try again.');
  }
};

  const fetchCourseDetails = async () => {
    try {
      const response = await axiosInstances.axiosInstance2.get(`courses/${courseId}`);
      course.value = response.data;
    } catch (error) {
  
      console.log("Error Details:", error);
      if (error.response && error.response.status === 403) {
        router.push({ name: 'ForbiddenPage' });
      } else {
        console.error("Unexpected error occurred:", error);
      }    
    }
  };
  
  onMounted(async () => {
    await fetchCourseDetails();

    if (teacherImage.value) {
      VanillaTilt.init(teacherImage.value, {
        max: 25, 
        speed: 400, 
        scale: 1.1, 
        glare: true, 
        "max-glare": 0.5, 
      });
    }
  });
  onUnmounted(() => {
    if (teacherImage.value && teacherImage.value.vanillaTilt) {
      teacherImage.value.vanillaTilt.destroy();
    }
  });
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

.enroll-section {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #ddd;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.enroll-section p {
  font-size: 16px;
  color: #333;
  margin-bottom: 20px;
}

.enroll-button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  width: 130px;
  margin: 0 auto;
}

.enroll-button:hover {
  background-color: #45a049;
}

  .page-container {
    display: flex;
    flex-direction: column;
    padding: 20px;
    margin: 20px;
  }
  
  .content-container {
    display: flex;
    gap: 30px;
    justify-content: space-between;
  }
  
  .left-section {
    flex: 2;
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .right-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 20px;
    margin-left: 20px;
  }
  
  .courseware-section {
    font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  }
  
  .course-details,
  .instructor-info {
    font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
    background-color: #ffffff;
    border-radius: 8px;
    padding: 5px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  }
  
  .course-image{
    width: 300px;
    height: 160px;
    border-radius: 8px;
  }
  
  .course-image:hover {
    transform: scale(1.05) translateY(-5px);
    transition: transform 0.3s ease-out;
  }
  
  .instructor-image {
    width: 150ox;
    height: 150px;
    border-radius: 8px;
  }
  
  .courseware-section h2,
  .course-details h2,
  .instructor-info h2{
    margin-bottom: 10px;
    font-size: 24px;
    font-weight: bold;
    color: black !important;
  }
  
  .description, .bio {
    margin: 10px 10px;
    text-align: justify;
    font-size: 16px;
    line-height: 1.6;
  }
  
  </style>