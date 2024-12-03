<template>
  <div class="create-course">
    <p>Create your first course to inspire students to learn and grow.</p>
    <button @click="isModalVisible = true">
      Create Your Course
    </button>
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
      Your first course is just a few clicks away!
    </p>
    <button @click="isModalVisible = true">
      Create Your Course
    </button>
  </div>
</template>

<script>
import {ref} from 'vue';
import {ElMessage} from "element-plus";
import axiosInstances from "@/services/axiosInstance";

export default {
  emits: ['courseSubmitted'],
  setup() {
    // FAQs
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

    const isModalVisible = ref(false);
    const formRef = ref(null);
    const isSaving = ref(false);
    const courseIndex = ref(null);
    const fileInput = ref(null);

    // Form and rules for validation
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
        {required: true, message: 'Please select an image for your course', trigger: 'blue'}
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
      activeFaqs,
      faqs,
      isModalVisible,
      formRef,
      form,
      rules,
      submitCourse,
      handleImageChange,
      triggerImageUpload,
      handleFileChange,
      cancelChanges,
      isSaving,
    };
  }
};
</script>

<style scoped>
.create-course {
  margin-top: 100px;
  background-color: white;
  padding: 30px;
  width: 90%;
  margin-left: 75px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #ddd;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.create-course p {
  color: black;
  text-align: left;
  font-size: 18px;
}

.create-course button {
  padding: 15px 30px;
  font-size: 16px;
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
  font-weight: bold;
  cursor: pointer;
  margin-right: 40px;
}

.create-course button:hover {
  background-color: #9DCAEB;
  border: 1px solid #9DCAEB;
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
  padding: 15px 30px;
  font-size: 16px;
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

.final-text button:hover {
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
</style>
