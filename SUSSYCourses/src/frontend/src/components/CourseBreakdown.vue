<template>
    <div class="course-breakdown">
      <Doughnut :data="chartData" :options="chartOptions" />
    </div>
  </template>
  
  <script setup>
import { ref, computed, onMounted} from "vue";
import { useRouter } from "vue-router";
import axiosInstances from '@/services/axiosInstance';
import { Doughnut } from "vue-chartjs";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
} from "chart.js";

ChartJS.register(Title, Tooltip, Legend, ArcElement);

const router = useRouter();
const courses = ref([]);
const userId = localStorage.getItem('userId');

const categories = [
  "Languages",
  "Chinese",
  "Programming",
  "Software",
  "Math",
  "Data Science",
  "Science",
  "Hardware",
  "Business",
  "Art"
];

const fetchCourseData = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`students/${userId}/courses`);
    courses.value = response.data.courses;
  } catch (error) {

    console.log("Error Details:", error);
    if (error.response && error.response.status === 403) {
      router.push({ name: 'ForbiddenPage' });
    } else {
      console.error("Unexpected error occurred:", error);
    }    
  }
};

// Calculate percentages for each topic
const categoryPercentages = computed(() => {
  const totalCourses = courses.value.length;
  const counts = {};

  categories.forEach((topic) => (counts[topic] = 0));

  courses.value.forEach((course) => {
    if (counts[course.topic] !== undefined) {
      counts[course.topic]++;
    }
  });

  return categories.map((topic) => ({
    topic,
    percentage: totalCourses > 0 ? ((counts[topic] / totalCourses) * 100).toFixed(2) : 0,
  }));
});

const chartData = computed(() => ({
  labels: categoryPercentages.value
    .filter((item) => item.percentage > 0) 
    .map((item) => item.topic),
  datasets: [
    {
      label: "Enrollment Percentage",
      data: categoryPercentages.value
        .filter((item) => item.percentage > 0)
        .map((item) => item.percentage),
      backgroundColor: [
        "#A8D5BA", 
        "#A2CFFE",  
        "#F4D06F",  
        "#F7A7C3",  
        "#D9B9F1",  
        "#A9B8D9",  
        "#80CBC4",  
        "#B6E5A8",  
        "#FFCCBC",  
      ],
      hoverOffset: 8,
    },
  ],
}));


const chartOptions = {
  responsive: true,
  plugins: {
    legend: {
      position: "right",
      labels: {
        boxWidth: 20,
        padding: 10,
      },
    },
    tooltip: {
      callbacks: {
        label: function (context) {
          const label = context.label || "";
          const value = context.raw || 0;
          return `${label}: ${value}%`;
        },
      },
    },
  },
};

onMounted(fetchCourseData);
</script>

<style scoped>
.course-breakdown {
  text-align: center;
  width: 550px;
  height: 550px; 
  margin: 0 auto;
}

</style>
