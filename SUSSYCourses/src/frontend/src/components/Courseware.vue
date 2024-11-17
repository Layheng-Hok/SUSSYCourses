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
                <div v-if="material.type === 'mp4'" class="video-container">
                  <video controls :src="`${material.url}`" width="50%"></video>
                </div>
                <div v-else>
                  <a :href="`${material.url}`" target="_blank">
                    <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" />
                    {{material.title}}
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
                <a :href="`/assets/Materials/${material.url}`" target="_blank">
                  <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" />
                  {{ material.title }}
                </a>
              </el-list-item>
            </el-list>
          </div>
        </el-collapse-item>

        <el-collapse-item title="Project Chapters" name="3">
          <div v-for="chapter in course.projectChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.url">
                <a :href="`/assets/Materials/${material.url}`" target="_blank">
                  <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" />
                  {{ material.title }}
                </a>
              </el-list-item>
            </el-list>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue';
  import { Document, DataBoard, VideoCamera, Files } from '@element-plus/icons-vue';
  import axiosInstances from "@/services/axiosInstance";
  export default {
    name: "CourseContent",
    props: {
      courseId: {
        type: String,
        required: true,
      },
    },
    setup(props) {
      const course = ref({
        teachingChapters: [],
        homeworkChapters: [],
        projectChapters: []
      });

      // Simulated course data
      // const coursesData = [
      //   {
      //     id: '1',
      //     name: "React Crash Course",
      //     description: "This is a comprehensive web development course.",
      //     image: "/assets/Courses/course.jpg",
      //     instructorName: "Dr. Angela Yu",
      //     instructorImage: "/assets/Avatars/instructor.jpg",
      //     instructorBio: "Developer with a love for teaching.",
      //     teachingChapters: [
      //       {
      //         name: "Chapter 1",
      //         materials: [
      //           { title: "Lecture 1.mp4", type: "mp4" },
      //           { title: "Introduction.pdf", type: "pdf" },
      //         ],
      //       },
      //     ],
      //     homeworkChapters: [
      //       { name: "Homework 1", materials: [{ title: "Assignment 1.pdf", type: "pdf" }] },
      //     ],
      //     projectChapters: [
      //       { name: "Project 1", materials: [{ title: "Project Plan.pptx", type: "pptx" }] },
      //     ],
      //   },
      //   {
      //     id: '2',
      //     name: "Vue Crash Course",
      //     description: "This is a comprehensive web development course.",
      //     image: "/assets/Courses/course2.jpg",
      //     instructorName: "Mr. Jack Bruh",
      //     instructorImage: "/assets/Avatars/student.jpg",
      //     instructorBio: "Developer with a love for teaching.",
      //     teachingChapters: [
      //       {
      //         name: "Chapter 1",
      //         materials: [{ title: "Introduction.pdf", type: "pdf" }],
      //       },
      //     ],
      //     homeworkChapters: [
      //       { name: "Homework 1", materials: [{ title: "Assignment 1.pdf", type: "pdf" }] },
      //     ],
      //     projectChapters: [
      //       { name: "Project 1", materials: [{ title: "Project Plan.pptx", type: "pptx" }] },
      //     ],
      //   },
      // ];

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

      // onMounted(() => {
      //   course.value = coursesData.find((c) => c.id === props.courseId);
      //   if (!course.value) {
      //     console.error("Course not found!");
      //   }
      // });

      onMounted(async () => {
        try {
          const response = await axiosInstances.axiosInstance.get('http://localhost:8081/courseware/coursewarePage');
          const coursesData = response.data
          console.log(coursesData)
          course.value = coursesData.find((c) => c.id === props.courseId);

          if (!course.value) {
            console.error("Course not found!");
          }
        } catch (error) {
          console.error('Error fetching courses:', error);
        }
      });

      return {
        course,
        materialIcon,
      };
    },
  };
  </script>
  
  <style scoped>
  .course-content {
    margin: 20px 0;
  }
  
  .chapter h3 {
    color: #333;
    margin: 10px 0;
  }
  
  .video-container {
    margin: 10px 0;
  }
  
  .el-list-item {
    display: flex;
    align-items: center;
    margin: 10px 0;
  }
  
  .el-list-item a {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: #409eff;
    transition: color 0.3s ease;
  }
  
  .el-list-item a:hover {
    color: #66b1ff;
  }
  </style>
  