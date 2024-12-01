<template>
  <div class="doughnut-container">
    <Doughnut :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { Doughnut } from "vue-chartjs";
import axiosInstances from "@/services/axiosInstance";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";

ChartJS.register(ArcElement, Tooltip, Legend);

const route = useRoute();

const chartData = ref({
  labels: ["Completed", "Remaining"],
  datasets: [
    {
      data: [0, 100], 
      backgroundColor: ["#FF8A80", "#BDBDBD"], 
      hoverBackgroundColor: ["#FF5252", "#E0E0E0"], 
    },
  ],
});

const chartOptions = {
  responsive: true,
  plugins: {
    legend: {
      position: "top",
    },
    tooltip: {
      callbacks: {
        label: function (context) {
          return `${context.raw}%`; 
        },
      },
    },
  },
};

const fetchProgressData = async () => {
  try {
    const userId = localStorage.getItem("userId");
    const courseId = route.params.courseId;
    
    const response = await axiosInstances.axiosInstance.get(`/students/${userId}/courses/${courseId}/progress`);
    
    const data = response.data;

    const totalCompleted = (data.completedTeachingMaterials + data.completedCourseworkMaterials) || 0;
    const totalMaterials = (data.totalTeachingMaterials + data.totalCourseworkMaterials) || 1; 

    const progressPercentage = (totalCompleted / totalMaterials) * 100;

    chartData.value = {
      labels: ["Completed", "Remaining"],
      datasets: [
        {
          data: [progressPercentage, 100 - progressPercentage], 
          backgroundColor: ["#FF8A80", "#BDBDBD"], 
          hoverBackgroundColor: ["#FF5252", "#E0E0E0"], 
        },
      ],
    };
  } catch (error) {
    console.error("Error fetching progress data:", error);
  }
};

onMounted(() => {
  fetchProgressData();
});
</script>


  <style scoped>
.doughnut-container {
  width: 200px;
  height: 200px; 
  margin: 25px auto; 
}
</style>
