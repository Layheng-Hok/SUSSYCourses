<template>
  <!-- Rating and Review Section -->
  <div class="ratings-section" :class="{'submitted-review': hasSubmittedReview}">
    <h2>Rate and Review</h2>
  
    <!-- Display submitted review if available -->
    <div v-if="hasSubmittedReview" class="submitted-message">
      <p><strong>Content Quality:</strong> {{ previousReview.contentQuality }} ⭐</p>
      <p><strong>Teaching Competence:</strong> {{ previousReview.teachingCompetence }} ⭐</p>
      <p><strong>Workload Balance:</strong> {{ previousReview.workBalance }} ⭐</p>
      <p><strong>Feedback:</strong> {{ previousReview.feedback }}</p>
    </div>
  
    <!-- Show Rating Form if no previous review -->
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
      <textarea
        type="textarea"
        class="review-input"
        v-model="userReview"
        placeholder="Write your review here..."
        rows="4"
      ></textarea>
  
      <!-- Submit Button -->
      <el-button
        type="primary"
        class="submit-button"
        @click="submitReview"
      >
        Submit
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axiosInstances from '@/services/axiosInstance';

const route = useRoute();
const userId = localStorage.getItem('userId');
const courseId = route.params.courseId;

const contentQualityRating = ref(0);
const teachingCompetenceRating = ref(0);
const workloadBalanceRating = ref(0);
const userReview = ref('');
const hasSubmittedReview = ref(false);
const previousReview = ref(null); 


const fetchReview = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(
      `/students/${userId}/courses/${courseId}/rating`
    );

    if (response.data) {
      previousReview.value = response.data;
      hasSubmittedReview.value = true;
    } else {
      hasSubmittedReview.value = false;
    }
  } catch (error) {
    console.error("Failed to fetch the review:", error);
  }
};

const submitReview = async () => {
  if (
    !contentQualityRating.value ||
    !teachingCompetenceRating.value ||
    !workloadBalanceRating.value ||
    !userReview.value.trim()
  ) {
    alert("Please provide ratings for all categories and write a review before submitting.");
    return;
  }

  const ratingRequest = {
    contentQuality: contentQualityRating.value,
    teachingCompetence: teachingCompetenceRating.value,
    workloadBalance: workloadBalanceRating.value,
    feedback: userReview.value.trim(),
  };

  try {
    await axiosInstances.axiosInstance.put(
      `/students/${userId}/courses/${courseId}/rate`,
      ratingRequest
    );

    alert("Thank you for your review!");
    hasSubmittedReview.value = true;

    previousReview.value = ratingRequest;
  } catch (error) {
    console.error("Failed to submit the review:", error);
    alert("An error occurred while submitting your review. Please try again.");
  }
};

onMounted(fetchReview);
</script>


<style scoped>

textarea {
  width: 90%;
  height: 80px;
  flex: 1;
  padding: 10px;
  margin-right: 20px;
  resize: none;
  font-size: 16px;
  border-radius: 5px;
  margin-bottom: 20px;
  border: 1px solid #ddd;
  margin: 0 auto;
}

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

</style>