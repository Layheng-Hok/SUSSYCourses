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

        <el-form-item label="Select Topic" prop="courseField">
          <el-select v-model="form.courseField" placeholder="Select Topic" class="modal-input">
            <el-option label="Programming" value="Programming"/>
            <el-option label="Hardware" value="Hardware"/>
            <el-option label="Math" value="Math"/>
            <el-option label="Science" value="Science"/>
            <el-option label="Languages" value="Languages"/>
            <el-option label="Chinese" value="Chinese"/>
            <el-option label="Software" value="Software"/>
            <el-option label="Business" value="Business"/>
            <el-option label="Data Science" value="Science"/>
            <el-option label="Languages" value="Languages"/>
          </el-select>
        </el-form-item>

        <el-form-item label="Description" prop="courseDescription">
          <el-input type="textarea" placeholder="Description" v-model="form.courseDescription" class="modal-input"/>
        </el-form-item>

        <el-form-item label="Select Type" prop="courseType">
          <el-select v-model="form.courseType" placeholder="Select Type" class="modal-input">
            <el-option label="open" value="open"/>
            <el-option label="semi-open" value="semi-open"/>
            <el-option label="non-open" value="non-open"/>
          </el-select>
        </el-form-item>

        <el-form-item label="Course Image" prop="courseImage">
          <template v-if="!form.courseImagePreview">
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
                :src="form.courseImagePreview"
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
        <el-button @click="cancelChanges" class="cancel-button">Cancel</el-button>
        <el-button type="primary" :loading="isSaving" @click="submitCourse" class="submit-button">Submit</el-button>
      </template>
    </el-dialog>
  </div>

  <div v-if="searchQuery.trim() && noCoursesFound" class="pending">
    <h2>No courses found</h2>
    <p>We couldn't find any courses matching your search query. Please try a different keyword.</p>
  </div>

  <div v-if="approvedCourses.length > 0" class="pending">
    <h2>Your Courses</h2>
    <p>The courses below have been approved!</p>

    <div v-for="(course, index) in approvedCourses" :key="course.courseId || index" class="course-block"
         @click=goTo(course.courseId)>
      <div class="pending-courses">
        <img :src="course.coverImageUrl" class="course-image" v-if="course.coverImageUrl"/>
        <div class="course-info">
          <p><strong>Course Name:</strong> {{ course.courseName }}</p>
          <p><strong>Topic:</strong> {{ course.topic }}</p>
          <p><strong>Description:</strong> {{ course.description }}</p>
          <p><strong>Type:</strong> {{ course.type }}</p>
          <p><strong>Status:</strong> {{ course.approvalStatus }}</p>
        </div>
        <p class="submission-date">{{ course.createdAt }}</p>
      </div>
    </div>
  </div>
  <div v-else-if="approvedCourses.length =0">
    <h2>Your Courses</h2>
    <p>You don't have any approved courses yet.</p>
  </div>

  <div v-if="pendingCourses.length > 0" class="pending">
    <h2>Pending Courses</h2>
    <p>Waiting to be approved by administrator...</p>

    <div v-for="(course, index) in pendingCourses" :key="course.courseId || index" class="course-block">
      <div class="pending-courses">
        <img :src="course.coverImageUrl" class="course-image" v-if="course.coverImageUrl"/>
        <div class="course-info">
          <p><strong>Course Name:</strong> {{ course.courseName }}</p>
          <p><strong>Topic:</strong> {{ course.topic }}</p>
          <p><strong>Description:</strong> {{ course.description }}</p>
          <p><strong>Type:</strong> {{ course.type }}</p>
          <p><strong>Status:</strong> {{ course.approvalStatus }}</p>
        </div>
        <p class="submission-date">{{ course.createdAt }}</p>
      </div>
    </div>
  </div>
  <div v-else-if="pendingCourses.length =0">
    <h2>Pending Courses</h2>
    <p>You don't have any pending courses yet.</p>
  </div>

  <div v-if="rejectedCourses.length > 0" class="pending">
    <h2>Rejected Courses</h2>
    <p>The courses below have been rejected...</p>

    <div v-for="(course, index) in rejectedCourses" :key="course.courseId || index" class="course-block">
      <div class="pending-courses">
        <img :src="course.coverImageUrl" class="course-image" v-if="course.coverImageUrl"/>
        <div class="course-info">
          <p><strong>Course Name:</strong> {{ course.courseName }}</p>
          <p><strong>Topic:</strong> {{ course.topic }}</p>
          <p><strong>Description:</strong> {{ course.description }}</p>
          <p><strong>Type:</strong> {{ course.type }}</p>
          <p><strong>Status:</strong> {{ course.approvalStatus }}</p>

        </div>
        <p class="submission-date">{{ course.createdAt }}</p>
      </div>
    </div>
  </div>
  <div v-else-if="rejectedCourses.length =0">
    <h2>Rejected Courses</h2>
    <p>You don't have any rejected courses!</p>
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
import {computed, onMounted, ref} from 'vue';
import {Search} from '@element-plus/icons-vue';
import axiosInstances from "@/services/axiosInstance";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";

export default {
  components: {
    Search
  },
  emits: ['courseSubmitted'],
  setup() {
    const router = useRouter();
    const activeFaqs = ref([]);
    const faqs = ref([
      {
        question: "How do I add assignments?",
        answer: "After creating your course and receiving approval from the administrator, you can add assignments to engage your students and assess their understanding."
      },
      {
        question: "How can I upload files or videos?",
        answer: "Once your course is approved, you'll have access to the upload feature, allowing you to enhance your course with files, videos, and other multimedia resources."
      },
      {
        question: "Can I track student progress?",
        answer: "Yes! After your course is live, you can track student progress, view engagement metrics, and gather feedback to improve the learning experience."
      },
    ]);

    const searchQuery = ref('');
    const allCourses = ref([]); // all courses

    const noCoursesFound = computed(() => {
      return (
          approvedCourses.value.length === 0 &&
          pendingCourses.value.length === 0 &&
          rejectedCourses.value.length === 0
      );
    });

    const goTo = (courseId) => {
      const currentRoute = router.currentRoute.value;
      const newPath = `${currentRoute.path}/course/${courseId}`;
      router.push(newPath);
    };

    const handleSearch = () => {
      const query = searchQuery.value.toLowerCase().trim();

      if (!query) {
        approvedCourses.value = allCourses.value.filter(course => course.approvalStatus === "approved");
        pendingCourses.value = allCourses.value.filter(course => course.approvalStatus === "pending");
        rejectedCourses.value = allCourses.value.filter(course => course.approvalStatus === "rejected");
        return;
      }

      approvedCourses.value = allCourses.value.filter(
          course => course.approvalStatus === "approved" && course.courseName.toLowerCase().includes(query)
      );
      pendingCourses.value = allCourses.value.filter(
          course => course.approvalStatus === "pending" && course.courseName.toLowerCase().includes(query)
      );
      rejectedCourses.value = allCourses.value.filter(
          course => course.approvalStatus === "rejected" && course.courseName.toLowerCase().includes(query)
      );
    };

    const isModalVisible = ref(false);
    const pendingCourses = ref([]);
    const approvedCourses = ref([]);
    const rejectedCourses = ref([]);

    const formRef = ref(null);
    const isSaving = ref(false);

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

        const response = await axiosInstances.axiosInstance.get(`/instructors/${userId}`);
        if (response.data?.courses) {
          allCourses.value = response.data.courses;

        } else {
          console.warn("Courses not found in the response.");
          allCourses.value = [];
        }

        approvedCourses.value = allCourses.value.filter(course => course.approvalStatus === "approved");
        pendingCourses.value = allCourses.value.filter(course => course.approvalStatus === "pending");
        rejectedCourses.value = allCourses.value.filter(course => course.approvalStatus === "rejected");
      } catch (error) {
        console.error("Error fetching courses:", error);
      }
    });

    const form = ref({
      courseName: '',
      courseField: '',
      courseDescription: '',
      courseImage: [],
      courseType: '',
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
        {required: true, message: 'Please select an image for your course', trigger: 'blur'}
      ],
      courseType: [
        {required: true, message: 'Please select a course type', trigger: 'blur'}
      ],
    };

    const cancelChanges = () => {
      resetForm();
      isModalVisible.value = false;
      ElMessage.info('Changes canceled');
    }

    const submitCourse = async () => {
      formRef.value.validate(async (valid) => {
        if (!valid) return;
        try {
          isSaving.value = true;
          const userId = localStorage.getItem("userId");
          if (!userId) {
            ElMessage.error("User ID not found. Please log in.");
            return;
          }

          const formData = new FormData();
          formData.append("courseName", form.value.courseName);
          formData.append("description", form.value.courseDescription);
          formData.append("teacherId", userId);
          formData.append("type", form.value.courseType);
          formData.append("topic", form.value.courseField);
          const file = form.value.courseImage[0];
          if (file) {
            formData.append("coverImageName", form.value.coverImageName);
            formData.append("fileType", form.value.fileType);
            formData.append("coverImageFile", file);
          } else {
            throw new Error("No image file selected.");
          }

          const response = await axiosInstances.axiosInstance.post("/courses/create", formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          });

          if (response.status === 200) {
            ElMessage.success("Course added successfully!");
            resetForm();
          } else {
            throw new Error(response.data.message || "Failed to add course.");
          }
        } catch (error) {
          ElMessage.error("Failed to add course. Please try again.");
        } finally {
          isSaving.value = false;
        }
      });
    };

    const handleImageChange = (file) => {
      if (file.raw) {
        form.value.courseImage = [file.raw];
        form.value.courseImagePreview = URL.createObjectURL(file.raw);

        const fullFileName = file.raw.name;
        const fileNameWithoutExtension = fullFileName.substring(0, fullFileName.lastIndexOf('.'));
        const fileExtension = fullFileName.substring(fullFileName.lastIndexOf('.') + 1);

        form.value.coverImageName = fileNameWithoutExtension;
        form.value.fileType = fileExtension;
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
        courseImagePreview: null,
        courseType: '',
      };
      courseIndex.value = null;
      isModalVisible.value = false;
    };

    return {
      goTo,
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
      resetForm,
      handleFileChange,
      fileInput,
      triggerImageUpload,
      pendingCourses,
      approvedCourses,
      rejectedCourses,
      cancelChanges,
      isSaving,
      noCoursesFound,
    };
  }
}
;
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
  margin-right: 43px;
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

.course-block {
  margin-top: 25px;
  background-color: white;
  padding: 30px;
  width: 94.2%;
  margin-left: 0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #ddd;
  position: relative;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.course-block:hover {
  transform: scale(1.01);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.pending-courses {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.course-image {
  width: 369px;
  height: auto;
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
  padding: 15px 15px;
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

