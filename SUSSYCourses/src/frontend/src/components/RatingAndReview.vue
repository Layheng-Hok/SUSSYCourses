<template>
<!-- Rating and Review Section -->
<div class="ratings-section" :class="{'submitted-review': hasSubmittedReview}">
    <h2>Rate and Review</h2>
  
    <!-- Check if the user has already submitted a review -->
    <div v-if="hasSubmittedReview" class="submitted-message">
      <p>You have already submitted a review before.</p>
    </div>
  
    <!-- Show Rating Section if not submitted before -->
    <div v-else>
      <!-- Content Quality -->
      <div class="rating-item">
        <p>Content Quality:</p>
        <el-rate
          v-model="contentQualityRating"
          allow-half
          :colors="['#c6c6c6', '#FFD700', '#FFB800', '#FFAA00', '#FF9000']"
        ></el-rate>
      </div>
  
      <!-- Teaching Competence -->
      <div class="rating-item">
        <p>Teaching Competence:</p>
        <el-rate
          v-model="teachingCompetenceRating"
          allow-half
          :colors="['#c6c6c6', '#FFD700', '#FFB800', '#FFAA00', '#FF9000']"
        ></el-rate>
      </div>
  
      <!-- Workload Balance -->
      <div class="rating-item">
        <p>Workload Balance:</p>
        <el-rate
          v-model="workloadBalanceRating"
          allow-half
          :colors="['#c6c6c6', '#FFD700', '#FFB800', '#FFAA00', '#FF9000']"
        ></el-rate>
      </div>
  
      <!-- Review Input -->
      <el-input
        type="textarea"
        class="review-input"
        v-model="userReview"
        placeholder="Write your review here..."
        rows="4"
      ></el-input>
  
      <!-- Submit Button -->
      <el-button
        type="primary"
        class="submit-button"
        @click="submitReview"
      >
        Submit
      </el-button>
    </div>
  
    <!-- Button to toggle review section -->
    <el-button
      class="toggle-button"
      @click="toggleReviewSection"
    >
      {{ hasSubmittedReview ? 'Edit Review' : 'Submit Review' }}
    </el-button>
  </div>
</template>
  <script setup>
import { ref } from 'vue';

const contentQualityRating = ref(0);
const teachingCompetenceRating = ref(0);
const workloadBalanceRating = ref(0);
const userReview = ref('');
const hasSubmittedReview = ref(false); // 
const userRating = ref(0);


const submitReview = () => {
  if (userRating.value && userReview.value.trim()) {
    alert(
      `Thank you for your review! Rating: ${userRating.value}, Review: "${userReview.value}"`
    );
    userRating.value = 0;
    userReview.value = "";
    hasSubmittedReview.value = true;
  } else {
    alert("Please provide a rating and review before submitting.");
  }
};

const toggleReviewSection = () => {
  hasSubmittedReview.value = !hasSubmittedReview.value;
};
</script>

<style scoped>
.ratings-section {
  margin-bottom: 20px;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
  background-color: #f9f9f9;
  border-radius: 8px;
  padding:20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.ratings-section.submitted-review {
  background-color: #f4f4f4; 
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2); 
}

.rating-item {
  margin-bottom: 15px;
  gap: 10px;
}

.rating-item p {
  margin: 0;
  font-weight: bold;
}

.review-input {
  margin-top: 15px;
  width: 90%;
}

.submit-button{
  background: #007bff;
  color: #fff;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
  font-size: 16px;
  width: 100px;
  height: auto;
  margin-top: 10px;
}

.submitted-message {
  padding: 10px;
  background-color: #e0e0e0;
  border-radius: 5px;
  color: #333;
  text-align: center;
  font-weight: bold;
}

.toggle-button {
  margin-top: 15px;
}
</style>