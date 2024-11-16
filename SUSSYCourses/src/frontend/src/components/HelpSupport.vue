<template>
    <!-- Back Button -->
    <div class="back-button" @click="goBack">
      <ArrowLeft class="back-icon" /> Back
    </div>
  
    <!-- Intro Section -->
    <div class="intro-section">
      <h1>Help and Support</h1>
      <p>If you need assistance, please check the FAQs below or contact our support team.</p>
    </div>
  
    <!-- FAQ Section -->
    <div class="faq-section">
      <h2>Frequently Asked Questions</h2>
      <el-collapse v-model="activeFaqs">
        <el-collapse-item v-for="(faq, index) in faqs" :key="index" :title="faq.question">
          <p>{{ faq.answer }}</p>
        </el-collapse-item>
      </el-collapse>
    </div>
  
    <!-- Contact Information Section -->
    <div class="contact-section">
      <h2>Contact Support</h2>
      <p>If you need further help, feel free to reach out to us:</p>
      <ul>
        <li><strong>Phone:</strong> +86 1511 123456</li>
        <li><strong>Email:</strong> sussycourses@gmail.com</li>
      </ul>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { ArrowLeft } from '@element-plus/icons-vue';
  
  const router = useRouter();
  const goBack = () => {
    router.back();
  };
  
  // FAQs
  const activeFaqs = ref([]);
  const faqs = ref([
    { question: "How do I reset my password?", answer: "Go to the Account Security page and follow the instructions to reset your password." },
    { question: "How can I contact support?", answer: "You can reach support via email at sussycourses@gmail.com or by phone at +86 1511 123456." },
    { question: "Can I change my profile picture?", answer: "Yes, you can change your profile picture by editing your profile." },
    { question: "Can I change my email address?", answer: "No, you cannot change your email address." },
  ]);
  </script>
  
  <style scoped>
  .back-button {
    display: flex;
    align-items: center;
    cursor: pointer;
    color: #007bff;
    font-weight: bold;
    margin: 1% 0 0 1%;
    font-family: 'Aptos Narrow', sans-serif;
  }
  
  .back-icon {
    width: 20px;
    height: 20px;
    margin-right: 5px;
  }
  
  .back-button:hover {
    color: #0056b3;
  }
  
  .intro-section {
    text-align: center;
    margin: 40px auto;
    max-width: 800px;
    color: black;
  }
  
  .intro-section h1 {
    font-size: 36px;
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .intro-section p {
    font-size: 18px;
    color: #666;
  }
  
  .faq-section {
    max-width: 800px;
    margin: 20px auto;
    color: black;

  }
  
  .faq-section h2 {
    font-size: 24px;
    margin-bottom: 10px;
  }
  
  .contact-section {
    max-width: 800px;
    margin: 20px auto;
    color: black;

  }
  
  .contact-section h2 {
    font-size: 24px;
    margin-bottom: 10px;
  }
  
  .contact-section ul {
    list-style-type: none;
    padding: 0;
  }
  
  .contact-section li {
    font-size: 18px;
    margin: 5px 0;
  }
  
  .contact-section li strong {
    font-size: 16px;
    color: #333;
  }
  </style>
  