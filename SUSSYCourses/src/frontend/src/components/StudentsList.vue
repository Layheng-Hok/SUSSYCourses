<template>
    <div class="student-list">
      <!-- List of students -->
      <el-list :data="students" v-if="students.length > 0">
        <el-list-item v-for="student in students" :key="student.userId">
          <div class="student-card">
            <!-- Student's Profile Picture -->
            <div class="profile-image">
              <img :src="student.profileImageUrl" alt="Student Profile" />
            </div>
            <!-- Student Info -->
            <div class="student-info">
              <h3>{{ student.fullName }}</h3>
              <p>{{ student.email }}</p>
            </div>
          </div>
        </el-list-item>
      </el-list>
  
      <p v-else>No students enrolled in this course.</p>
    </div>
  </template>
  
  <script setup>
  
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axiosInstances from '@/services/axiosInstance';

const route = useRoute();
const students = ref([]);
const courseId = route.params.courseId;

onMounted(async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`/courses/${courseId}/students/enrolled`);
    students.value = response.data.students;
  } catch (error) {
    console.error('Error fetching students data:', error);
  }
});

  
  </script>
  
  <style scoped>
  .student-list {
    margin-top: 20px;
  }
  
  .student-card {
    display: flex;
    align-items: center;
    padding: 10px;
    border-bottom: 1px solid #e0e0e0;
  }
  
  .profile-image {
    width: 50px;
    height: 50px;
    margin-right: 15px;
  }
  
  .profile-image img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
  }
  
  .student-info h3 {
    margin: 0;
    font-size: 16px;
    font-weight: 600;
  }
  
  .student-info p {
    margin: 5px 0 0;
    color: #888;
  }
  
  .el-list {
    margin: 0;
  }
  
  .error-message {
    color: red;
    font-size: 12px;
  }
  </style>
  