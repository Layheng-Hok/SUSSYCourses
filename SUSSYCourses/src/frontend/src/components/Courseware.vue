<template>
    <el-card class="course-content" shadow="hover">
      <h2>Course Content</h2>
      <el-collapse>
        <el-collapse-item title="Teaching Chapters" name="1">
          <div v-for="chapter in course.teachingChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.url">
              <!-- Video content -->
              <div v-if="material.type === 'mp4'" class="video-container">
            <video controls :src="`${material.url}`" controlsList="nodownload" class="video-player"></video>
            </div>
              <div v-else>
                  <FilePreview :type="material.type" :title="material.title" :url="material.url" :isNonDownloadable="material.isNonDownloadable" />
              </div>
            </el-list-item>

            </el-list>
          </div>
        </el-collapse-item>

        <el-collapse-item title="Homework Chapters" name="2">
          <div v-for="chapter in course.homeworkChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.url">
                <FilePreview :type="material.type" :title="material.title" :url="material.url" />
              </el-list-item>
            </el-list>
          </div>
        </el-collapse-item>

        <el-collapse-item title="Project Chapters" name="3">
          <div v-for="chapter in course.projectChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.url">
                <FilePreview :type="material.type" :title="material.title" :url="material.url" />
              </el-list-item>
            </el-list>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRoute } from 'vue-router';
  import axiosInstances from "@/services/axiosInstance";
  import FilePreview from './FilePreview.vue';
  
  const route = useRoute();
  const courseId = route.params.courseId;
  
  const course = ref({
    teachingChapters: [],
    homeworkChapters: [],
    projectChapters: []
  });
  
  onMounted(async () => {
    try {
      const response = await axiosInstances.axiosInstance.get('http://localhost:8081/courseware/coursewarePage');
      const coursesData = response.data;
      course.value = coursesData.find((c) => c.id === courseId);
  
      if (!course.value) {
        console.error("Course not found!");
      }
    } catch (error) {
      console.error('Error fetching courses:', error);
    }
  });
  </script>

  <style scoped>
.course-content {
  margin: 20px;
  padding: 20px;
  border-radius: 8px;
}

h2 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.chapter h3 {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
  margin-top: 15px;
  margin-bottom: 10px;
}

.el-collapse-item {
  margin-bottom: 15px;
}

.el-collapse-item__header {
  font-size: 18px;
  font-weight: 500;
  color: #1a73e8;
  cursor: pointer;
}

.el-list {
  margin-top: 10px;
  padding-left: 15px;
}

.el-list-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}

.el-list-item:last-child {
  border-bottom: none;
}

.el-list-item a {
  color: #1a73e8
}

.video-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 10px 0;
  max-width: 100%;
  width: 100%;
}

.video-player {
  width: 80%;
  height: 80%;
}
  </style>
  