<template>
  <el-card class="course-content" shadow="hover">
    <h2>Course Content</h2>
    <el-collapse>
      <!-- Teaching Chapters -->
      <el-collapse-item title="Teaching Chapters" name="1">
        <div v-for="(chapter, index) in uniqueChapters" :key="index" class="material">
      <h3>Chapter {{ chapter }}</h3>
      <el-list>
        <el-list-item v-for="material in filteredMaterials('lecture').filter(m => m.chapter === chapter)" :key="material.coursewareId">
          <FilePreview :material="material" />
        </el-list-item>
      </el-list>
    </div>
      </el-collapse-item>

      <!-- Homework Chapters -->
      <el-collapse-item title="Homework Chapters" name="2">
        <div v-for="(material, index) in filteredMaterials('homework')" :key="index" class="material">
          <h3>Chapter {{ material.chapter }}</h3>
          <el-list>
            <el-list-item :key="material.coursewareId">
              <FilePreview :material="material" />
            </el-list-item>
          </el-list>
        </div>
      </el-collapse-item>

      <!-- Project Chapters -->
      <el-collapse-item title="Project Chapters" name="3">
        <div v-for="(material, index) in filteredMaterials('project')" :key="index" class="material">
          <h3>Chapter {{ material.chapter }}</h3>
          <el-list>
            <el-list-item :key="material.coursewareId">
              <FilePreview :material="material" />
            </el-list-item>
          </el-list>
        </div>
      </el-collapse-item>

        <!-- Project Chapters -->
        <el-collapse-item title="Attachment" name="4">
        <div v-for="(material, index) in filteredMaterials('attachment')" :key="index" class="material">
          <h3>Chapter {{ material.chapter }}</h3>
          <el-list>
            <el-list-item :key="material.coursewareId">
              <FilePreview :material="material" />
            </el-list-item>
          </el-list>
        </div>
      </el-collapse-item>
    </el-collapse>
  </el-card>
</template>

  
  <script setup>
  import { ref, onMounted, computed} from 'vue';
  import { useRoute } from 'vue-router';
  import axiosInstances from "@/services/axiosInstance";
  import FilePreview from './FilePreview.vue';
  
  const route = useRoute();
  const userId = localStorage.getItem('userId');
  const courseId = route.params.courseId;
  
  const courseMaterials = ref([]);

  const getCoursewares = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`students/${userId}/courses/${courseId}/coursewares`);
    
    if (response.data && Array.isArray(response.data)) {
      courseMaterials.value = response.data;
    } else {
      console.error('Invalid data format');
    }
  } catch (error) {
    console.error('Error fetching courseware materials:', error);
  }
};

const filteredMaterials = (category) => {
  return courseMaterials.value.filter(material => material.category === category);
};

const uniqueChapters = computed(() => {
  return [...new Set(filteredMaterials('lecture').map(m => m.chapter))];
});
  
  onMounted(async () => {
    await getCoursewares();
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

::v-deep .el-collapse-item__header {
color: black;
font-size: 16px;
font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif
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
  </style>
  