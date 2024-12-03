<template>
  <div class="student-list">
    <h2>Enrolled students</h2>
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

  <div class="student-list">
    <h2>Pending students</h2>
    <!-- List of students -->
    <el-list :data="pendingStudents" v-if="pendingStudents.length > 0">
      <el-list-item v-for="student in pendingStudents" :key="student.userId">
        <div class="pending-student-card">
          <!-- Student's Profile Picture -->
          <div class="pending-student-info">
            <div class="profile-image">
              <img :src="student.profileImageUrl" alt="Student Profile" />
            </div>
            <!-- Student Info -->
            <div class="student-info">
              <h3>{{ student.fullName }}</h3>
              <p>{{ student.email }}</p>
            </div>
          </div>
          <!-- Accept/Reject Buttons -->
          <div class="student-actions">
            <button @click="acceptStudent(student)" class="accept-btn" success>
              <i class="el-icon-check"></i> Accept
            </button>
            <button @click="rejectStudent(student)" class="reject-btn">
              <i class="el-icon-close"></i> Reject
            </button>
          </div>
        </div>
      </el-list-item>
    </el-list>
    <p v-else>No Pending Students</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axiosInstances from '@/services/axiosInstance';
import { ElNotification } from 'element-plus';

const route = useRoute();
const students = ref([]);
const pendingStudents = ref([]);
const courseId = route.params.courseId;
const userId = localStorage.getItem("userId");

onMounted(async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`/courses/${courseId}/students/enrolled`);
    const response2 = await axiosInstances.axiosInstance.get(`/courses/${courseId}/students/pending`);
    pendingStudents.value = response2.data.students;
    students.value = response.data.students;
  } catch (error) {
    console.error('Error fetching students data:', error);
  }
});

const acceptStudent = async (student) => {
  try {
    await axiosInstances.axiosInstance.put(`/instructors/${userId}/courses/${courseId}/accept/${student.userId}`);
    console.log(`Student ${student.fullName} accepted.`);
    // Trigger success notification
    ElNotification({
      title: 'Success',
      message: `Student ${student.fullName} accepted successfully.`,
      type: 'success',
    });
    // Remove from pending list
    pendingStudents.value = pendingStudents.value.filter(s => s.userId !== student.userId);
    students.value.push(student);  // Optionally add to enrolled list
  } catch (error) {
    console.error(`Error accepting student ${student.fullName}:`, error);
    ElNotification({
      title: 'Error',
      message: `Failed to accept student ${student.fullName}.`,
      type: 'error',
    });
  }
};

const rejectStudent = async (student) => {
  try {
    await axiosInstances.axiosInstance.put(`/instructors/${userId}/courses/${courseId}/reject/${student.userId}`);
    console.log(`Student ${student.fullName} rejected.`);
    // Trigger error notification
    ElNotification({
      title: 'Rejected',
      message: `Student ${student.fullName} was rejected.`,
      type: 'error',
    });
    // Remove from pending list
    pendingStudents.value = pendingStudents.value.filter(s => s.userId !== student.userId);
  } catch (error) {
    console.error(`Error rejecting student ${student.fullName}:`, error);
    ElNotification({
      title: 'Error',
      message: `Failed to reject student ${student.fullName}.`,
      type: 'error',
    });
  }
};
</script>

<style scoped>
.student-list {
  margin-top: 20px;
}

.student-card{
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.pending-student-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.pending-student-info{
  display: flex;
  flex-direction: row;;
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

.student-actions {
  display: flex;
  gap: 10px;
}

.accept-btn, .reject-btn {
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
}

.accept-btn {
  background-color: #4CAF50;
  color: white;
}

.reject-btn {
  background-color: #F44336;
  color: white;
}

.accept-btn:hover, .reject-btn:hover {
  opacity: 0.8;
}

.el-icon-check, .el-icon-close {
  margin-right: 5px;
}
</style>
