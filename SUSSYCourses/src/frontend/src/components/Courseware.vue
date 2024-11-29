<template>
    <!-- Course Content Section with Interactive Media -->
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
                <video controls :src="`${material.url}`" width="50%"></video>
              </div>
  
              <!-- PDF File Preview -->
              <div v-else-if="material.type === 'pdf'" class="attachment-box">
                <div class="icon-container">
                  <img src="/assets/Icons/pdf-icon.svg" alt="PDF Icon" class="pdf-icon" />
                </div>
                <div class="file-info">
                  <p>{{ material.title || "PDF File" }}</p>
                </div>
                <div class="expand-container">
                  <button @click="openInNewTab(material.url)" class="expand-button">
                    View
                  </button>
                </div>
              </div>
              
              <!-- Other materials -->
              <div v-else>
                <a :href="`${material.url}`" target="_blank">
                  <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" />
                  {{ material.title }}
                </a>
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
                  <!-- PDF File Preview -->
              <div v-if="material.type === 'pdf'" class="attachment-box">
                <div class="icon-container">
                  <img src="/assets/Icons/pdf-icon.svg" alt="PDF Icon" class="pdf-icon" />
                </div>
                <div class="file-info">
                  <p>{{ material.title || "PDF File" }}</p>
                </div>
                <div class="expand-container">
                  <button @click="openInNewTab(material.url)" class="expand-button">
                    View
                  </button>
                </div>
              </div>
              
              <!-- Other materials -->
              <div v-else>
                <a :href="`${material.url}`" target="_blank">
                  <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" />
                  {{ material.title }}
                </a>
              </div>
              </el-list-item>
            </el-list>
          </div>
        </el-collapse-item>

        <el-collapse-item title="Project Chapters" name="3">
          <div v-for="chapter in course.projectChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.url">
               <!-- PDF File Preview -->
               <div v-if="material.type === 'pdf'" class="attachment-box">
                <div class="icon-container">
                  <img src="/assets/Icons/pdf-icon.svg" alt="PDF Icon" class="pdf-icon" />
                </div>
                <div class="file-info">
                  <p>{{ material.title || "PDF File" }}</p>
                </div>
                <div class="expand-container">
                  <button @click="openInNewTab(material.url)" class="expand-button">
                    View
                  </button>
                </div>
              </div>
              
              <!-- Other materials -->
              <div v-else>
                <a :href="`${material.url}`" target="_blank">
                  <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" />
                  {{ material.title }}
                </a>
              </div>
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
  import { Document, DataBoard, VideoCamera, Files } from '@element-plus/icons-vue';
  import axiosInstances from "@/services/axiosInstance";
  
  const route = useRoute();
  const courseId = route.params.courseId;
  
  const course = ref({
    teachingChapters: [],
    homeworkChapters: [],
    projectChapters: []
  });
  
  const openInNewTab = (url) => {
  window.open(url, "_blank");
};

  const materialIcon = (type) => {
    switch (type) {
      case "pdf":
        return Document;
      case "pptx":
        return DataBoard;
      case "mp4":
        return VideoCamera;
      default:
        return Files;
    }
  };
  
  onMounted(async () => {
    try {
      const response = await axiosInstances.axiosInstance.get('http://localhost:8081/courseware/coursewarePage');
      const coursesData = response.data;
      course.value = coursesData.find((c) => c.id === courseId);
      console.log("courseware:", course.value);
  
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
  margin: 20px 0;
}

.attachment-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(0, 0, 0, 0.05);
  border: 1px solid #ddd;
  border-radius: 12px; 
  padding: 10px 15px;
  height: 50px;
  width: 100%;
  max-width: 270px; 
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  margin: 20px auto;
}

.pdf-icon {
  width: 30px;
  height: 30px;
}

.file-info {
  flex: 1;
  padding: 0 10px;
  font-size: 16px;
  color: #333;
}

.expand-container {
  flex: 0 0 auto;
}

.expand-button {
  background: #007bff;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 5px 15px;
  font-size: 14px;
  cursor: pointer;
  font-family: 'Arial', sans-serif;
  transition: all 0.3s ease;
}

.expand-button:hover {
  background: #0056b3;
}

  </style>
  