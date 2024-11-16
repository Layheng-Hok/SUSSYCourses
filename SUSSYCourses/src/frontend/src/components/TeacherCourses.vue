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
            <img :src="form.courseImage[0]" class="el-icon-plus"
                 style="width: 150px; height: 150px; object-fit: cover; border-radius: 6px"/>
          </template>
        </el-form-item>

      </el-form>
      <template #footer>
        <el-button @click="isModalVisible = false" class="cancel-button">Cancel</el-button>
        <el-button type="primary" @click="submitCourse" class="submit-button">Submit</el-button>
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

  <router-link to="/teacher-courses-details">
    check check
  </router-link>
</template>

<script>
import {ref} from 'vue';

export default {
  emits: ['courseSubmitted'],
  setup(props, {emit}) {
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

    // Form and rules for validation
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

    const submitCourse = () => {
      formRef.value.validate((valid) => {
        if (valid) {
          // Get the current date and time
          const submissionDate = new Date().toLocaleString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: 'numeric',
            minute: 'numeric',
            second: 'numeric',
            hour12: true
          });

          const newCourse = {
            ...form.value,
            submissionDate
          };

          let courses = JSON.parse(localStorage.getItem('courses')) || [];
          courses.push(newCourse);
          localStorage.setItem('courses', JSON.stringify(courses));

          // Reset form fields after submission
          form.value = {
            courseName: '',
            courseField: '',
            courseDescription: '',
            courseImage: [],
          };

          isModalVisible.value = false;
          emit('courseSubmitted');
        } else {
          console.log('Error: Please fill out the required fields');
          return false;
        }
      });
    };

    const handleImageChange = (file) => {
      if (file.raw) {
        form.value.courseImage = [URL.createObjectURL(file.raw)];
      }
    };

    return {
      activeFaqs,
      faqs,
      isModalVisible,
      formRef,
      form,
      rules,
      submitCourse,
      handleImageChange
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
