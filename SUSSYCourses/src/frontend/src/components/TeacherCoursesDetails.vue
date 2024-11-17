<template>
  <div class="header">
    <h1>
      Courses
    </h1>
  </div>
  <div class="search-bar-container">

    <div class="search-bar">
      <el-input
          v-model="searchQuery"
          placeholder="Search your courses"
          class="search-input"
          @keyup.enter="handleSearch"
          clearable
      />
      <el-button class="search-button" @click="handleSearch">
        <el-icon>
          <Search/>
        </el-icon>
      </el-button>
    </div>
    <div class="new-course">
      <button @click="isModalVisible = true">
        New Course
      </button>
    </div>
  </div>

  <div class="modal">
    <el-dialog v-model="isModalVisible" title="Create Your Course" width="50%" center>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" @submit.prevent="submitCourse">
        <el-form-item label="Course Name" prop="courseName">
          <el-input placeholder="Course Name" v-model="form.courseName" class="modal-input"/>
        </el-form-item>

        <el-form-item label="Select Field" prop="courseField">
          <el-select v-model="form.courseField" placeholder="Select Field" class="modal-input">
            <el-option label="Web Development" value="Web Development"/>
            <el-option label="Data Science" value="Data Science"/>
            <el-option label="Marketing" value="Marketing"/>
            <el-option label="Design" value="Design"/>
            <el-option label="Programming" value="Programming"/>
            <el-option label="Hardware" value="Hardware"/>
            <el-option label="Finance" value="Finance"/>
            <el-option label="Economics" value="Economics"/>
            <el-option label="Leadership" value="Leadership"/>
            <el-option label="Entrepreneurship" value="Entrepreneurship"/>
          </el-select>
        </el-form-item>

        <el-form-item label="Description" prop="courseDescription">
          <el-input type="textarea" placeholder="Description" v-model="form.courseDescription" class="modal-input"/>
        </el-form-item>

        <el-form-item label="Course Image" prop="courseImage">
          <template v-if="form.courseImage.length === 0">
            <el-upload
                class="upload-demo"
                action=""
                list-type="picture-card"
                :auto-upload="false"
                @change="handleImageChange"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </template>
          <template v-else>
            <img
                :src="form.courseImage[0]"
                style="width: 150px; height: 150px; object-fit: cover; border-radius: 6px; cursor: pointer;"
                @click="triggerImageUpload"
            />
            <input
                type="file"
                ref="fileInput"
                style="display: none;"
                @change="handleFileChange"
            />
          </template>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="isModalVisible = false" class="cancel-button">Cancel</el-button>
        <el-button type="primary" @click="submitCourse" class="submit-button">Submit</el-button>
      </template>
    </el-dialog>
  </div>

    <div v-for="(course, index) in approvedCourses" :key="course.courseId || index" class="course-block">
      <div class="pending-courses">
        <img :src="course.coverImageUrl" class="course-image" v-if="course.coverImageUrl"/>
        <div class="course-info">
          <p><strong>Course Name:</strong> {{ course.courseName }}</p>
          <p><strong>Topic:</strong> {{ course.topic }}</p>
          <p><strong>Description:</strong> {{ course.description }}</p>
          <p><strong>Type:</strong> {{ course.type }}</p>
          <p><strong>Status:</strong> {{ course.approvalStatus }}</p>
          <button class="edit-button" @click="editCourse(index)">Edit Submission</button>
        </div>
        <p class="submission-date">{{ course.createdAt }}</p>
      </div>
    </div>

  <div class="pending">
    <h2>Pending Courses</h2>
    <p>Waiting to be approved by administrator...</p>
  </div>

  <div v-for="(course, index) in pendingCourses" :key="course.courseId || index" class="course-block">
    <div class="pending-courses">
      <img :src="course.coverImageUrl" class="course-image" v-if="course.coverImageUrl"/>
      <div class="course-info">
        <p><strong>Course Name:</strong> {{ course.courseName }}</p>
        <p><strong>Topic:</strong> {{ course.topic }}</p>
        <p><strong>Description:</strong> {{ course.description }}</p>
        <p><strong>Type:</strong> {{ course.type }}</p>
        <p><strong>Status:</strong> {{ course.approvalStatus }}</p>
        <button class="edit-button" @click="editCourse(index)">Edit Submission</button>
      </div>
      <p class="submission-date">{{ course.createdAt }}</p>
    </div>
  </div>

  <div class="pending">
    <h2>Rejected Courses</h2>
    <p>The courses below have been rejected...</p>
  </div>

  <div v-for="(course, index) in rejectedCourses" :key="course.courseId || index" class="course-block">
    <div class="pending-courses">
      <img :src="course.coverImageUrl" class="course-image" v-if="course.coverImageUrl"/>
      <div class="course-info">
        <p><strong>Course Name:</strong> {{ course.courseName }}</p>
        <p><strong>Topic:</strong> {{ course.topic }}</p>
        <p><strong>Description:</strong> {{ course.description }}</p>
        <p><strong>Type:</strong> {{ course.type }}</p>
        <p><strong>Status:</strong> {{ course.approvalStatus }}</p>
        <button class="edit-button" @click="editCourse(index)">Edit Submission</button>
      </div>
      <p class="submission-date">{{ course.createdAt }}</p>
    </div>
  </div>

  <div class="second">
    <p>Based on your experience, we think these resources will be helpful.
    </p>
  </div>

  <div class="guideline">
    <img src="@/assets/img_13.png">
    <div class="guideline-text">
      <h2>Create an Engaging Course</h2>
      <p>Whether you've been teaching for years or are teaching for the first time, you can make an engaging course.
        We've compiled resources and best practices to help you get to the next level, no matter where you're
        starting.</p>
      <router-link to="/guideline" target="_blank">Guideline</router-link>
    </div>
  </div>

  <div class="faq-section">
    <h2>Frequently Asked Questions</h2>
    <el-collapse v-model="activeFaqs">
      <el-collapse-item v-for="(faq, index) in faqs" :key="index" :title="faq.question">
        <p>{{ faq.answer }}</p>
      </el-collapse-item>
    </el-collapse>
  </div>

  <div class="final-text">
    <p>
      Add More courses
    </p>
    <button @click="isModalVisible = true">
      New Course
    </button>
  </div>
</template>

<script>
import {ref, onMounted} from 'vue';
import {Search} from '@element-plus/icons-vue';
import axiosInstances from "@/services/axiosInstance";

export default {
  components: {
    Search
  },
  emits: ['courseSubmitted'],
  setup() {
    const activeFaqs = ref([]);
    const faqs = ref([
      {
        question: "How do I add assignments?",
        answer: "After creating your course and receiving approval from the administrator, you can add assignments to engage your students and assess their understanding."
      },
      {
        question: "How can I upload files or videos?",
        answer: "Once your course is approved, you’ll have access to the upload feature, allowing you to enhance your course with files, videos, and other multimedia resources."
      },
      {
        question: "Can I track student progress?",
        answer: "Yes! After your course is live, you can track student progress, view engagement metrics, and gather feedback to improve the learning experience."
      },
    ]);

    // Search functionality
    const searchQuery = ref('');

    const handleSearch = () => {
      console.log('Searching for:', searchQuery.value);
      // logic
    };

    // Modal visibility
    const isModalVisible = ref(false);
    const pendingCourses = ref([]);
    const approvedCourses = ref([]);
    const rejectedCourses = ref([]);

    const formRef = ref(null);
    const courses = ref([]);
    const courseIndex = ref(null);
    const fileInput = ref(null);

    onMounted(async () => {
      try {
        const userId = localStorage.getItem("userId");
        if (!userId) {
          console.error("No userId found in localStorage!");
          return;
        }

        const response = await axiosInstances.axiosInstance.get(`/instructor/profile/${userId}`);

        if (response.data && response.data.courses) {
          pendingCourses.value = response.data.courses.filter(course => course.approvalStatus === "pending");
          approvedCourses.value = response.data.courses.filter(course => course.approvalStatus === "approved");
          rejectedCourses.value = response.data.courses.filter(course => course.approvalStatus === "rejected");
        } else  {
          console.warn("No courses found in the response.");
          pendingCourses.value = [];
          approvedCourses.value = [];
          rejectedCourses.value = [];
        }
      } catch (error) {
        console.error("Error fetching courses:", error);

      }
    });

    // Form and validation rules
    const form = ref({
      courseName: '',
      courseField: '',
      courseDescription: '',
      courseImage: [],
    });

    const rules = {
      courseName: [
        {required: true, message: 'Please enter the course name', trigger: 'blur'}
      ],
      courseDescription: [
        {required: true, message: 'Please enter a course description', trigger: 'blur'}
      ],
      courseField: [
        {required: true, message: 'Please select a course field', trigger: ['blur', 'change']}
      ],
      courseImage: [
        {required: true, message: 'Please select an image for your course', trigger: 'blue'}
      ],
    };

    const editCourse = (index) => {
      courseIndex.value = index; // Set the index of the course being edited
      const course = courses.value[index];
      form.value = {...course};
      isModalVisible.value = true;
    };

    const submitCourse = () => {
      formRef.value.validate((valid) => {
        if (valid) {
          const updatedCourse = {
            ...form.value,
            submissionDate: new Date().toLocaleString('en-US', {
              year: 'numeric',
              month: 'long',
              day: 'numeric',
              hour: 'numeric',
              minute: 'numeric',
              second: 'numeric',
              hour12: true,
            })
          };

          if (courseIndex.value === null) {
            courses.value.push(updatedCourse);
          } else {
            courses.value.splice(courseIndex.value, 1, updatedCourse);
          }

          localStorage.setItem('courses', JSON.stringify(courses.value));
          resetForm();
        }
      });
    };
    const handleImageChange = (file) => {
      if (file.raw) {
        form.value.courseImage = [URL.createObjectURL(file.raw)];
      }
    };

    const triggerImageUpload = () => {
      fileInput.value.click();
    };

    const handleFileChange = (event) => {
      const file = event.target.files[0];
      if (file) {
        form.value.courseImage = [URL.createObjectURL(file)];
      }
    };

    const resetForm = () => {
      form.value = {
        courseName: '',
        courseField: '',
        courseDescription: '',
        courseImage: [],
      };
      courseIndex.value = null;
      isModalVisible.value = false;
    };

    return {
      activeFaqs,
      faqs,
      searchQuery,
      handleSearch,
      isModalVisible,
      formRef,
      form,
      rules,
      submitCourse,
      handleImageChange,
      courses,
      editCourse,
      resetForm,
      handleFileChange,
      fileInput,
      triggerImageUpload,
      pendingCourses,
      approvedCourses,
      rejectedCourses
    };
  }
};
</script>

<style scoped>
.header h1 {
  color: black;
  margin-top: 90px;
  text-align: left;
  margin-left: 80px;
  font-family: Garamond;
}

.search-bar {
  display: flex;
  border: 1px solid #333;
  overflow: hidden;
  max-width: 230px;
  height: 50px;
  margin-left: 80px;
}

.search-button {
  background-color: #333;
  color: white;
  font-weight: bolder;
  font-size: 20px;
  border: none;
  border-radius: 0;
  display: flex;
  align-items: center;
  height: 50px;
}

.search-button:hover {
  background-color: #444;
  color: white;
}

.search-input ::v-deep .el-input__inner::placeholder {
  color: inherit !important;
  font-size: 17px;
}

.new-course button {
  padding: 15px 10px;
  font-size: 16px;
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
  font-weight: bold;
  cursor: pointer;
  margin-right: 40px;
}

.new-course button:hover {
  background-color: #9DCAEB;
  border: 1px solid #9DCAEB;
}

.modal-input {
  margin-bottom: 15px;
  width: 100%;
}

.submit-button {
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
  margin-top: -20px !important;
}

.submit-button:hover {
  background-color: #9DCAEB;
  color: white;
  border: 1px solid #9DCAEB;
}

.cancel-button {
  background-color: white;
  color: #74B3E3;
  border: 1px solid #74B3E3;
  margin-top: -20px !important;
}

.cancel-button:hover {
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
}

::v-deep .el-dialog__title {
  font-size: 24px;
  color: black;
  font-family: Garamond;
}

.search-bar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1600px;
  margin-right: -25px;
}

.search-bar {
  flex: 1;
}

.pending {
  text-align: left;
  margin-left: 80px;
  margin-bottom: 25px;
}

.pending h2 {
  margin-top: 50px;
  margin-bottom: 10px;
  color: black;
  font-family: Garamond;
}

.pending p {
  color: black;
  margin-top: 0;
}

.course-block {
  margin-top: 25px;
  background-color: white;
  padding: 30px;
  width: 89%;
  margin-left: 80px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #ddd;
  position: relative;
}

.pending-courses {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.course-image {
  width: 150px;
  height: 150px;
  object-fit: cover;
  border-radius: 6px;
  margin-right: 20px;
}

.course-info {
  text-align: left;
  margin-left: 130px;
}

.course-info p {
  margin: 5px 0;
  color: black;
  font-size: 16px;
}

.submission-date {
  position: absolute;
  bottom: 10px;
  margin-left: auto;
  right: 30px;
  font-size: 12px;
  color: #7A7A7A;
}

.edit-button {
  margin-top: 10px;
  padding: 8px 12px;
  font-size: 14px;
  color: white;
  background-color: #74B3E3;
  border: none;
  cursor: pointer;
}

.edit-button:hover {
  background-color: #9DCAEB;
}

.second p {
  color: black;
  margin-top: 60px;
  margin-bottom: 60px;
}

.guideline {
  background-color: white;
  padding: 50px;
  width: 87%;
  margin-left: 75px;
  margin-right: 200px !important;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #ddd;
  display: flex;
  align-items: center;
  gap: 100px;
}

.guideline img {
  width: 230px;
  height: auto;
  margin-left: 250px;
}

.guideline-text {
  max-width: 600px;
  text-align: left;
  margin-left: auto;
  margin-right: 60px;
}

.guideline-text h2 {
  font-size: 27px;
  margin-bottom: 50px;
  margin-top: -15px;
  color: black;
  font-family: Garamond;
}

.guideline-text p {
  font-size: 16px;
  line-height: 1.5;
  color: black;
  margin-bottom: 30px;
}

.guideline-text a {
  font-size: 17px;
  color: #007bff;
  text-decoration: underline;
  line-height: 1.5;
  text-underline-offset: 3px;
}

.guideline-text a:hover {
  color: #0066cc;
}

.faq-section {
  max-width: 800px;
  margin: 60px auto 80px;
}

.faq-section h2 {
  font-size: 24px;
  margin-bottom: 30px;
  color: black;
  font-family: Garamond;
}

.final-text p {
  color: black;
  font-size: 17px;
  line-height: 1.5;
}

.final-text button {
  padding: 15px 20px;
  font-size: 16px;
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 30px !important;
}

.final-text button:hover {
  background-color: #9DCAEB;
  border: 1px solid #9DCAEB;
}

.search-bar ::v-deep(.el-input__wrapper) {
  border: none !important;
  box-shadow: none !important;
}
</style>
