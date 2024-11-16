<template>
    <div class="course-breakdown">
      <Doughnut :data="chartData" :options="chartOptions" />
    </div>
  </template>
  
  
  <script setup>
import { ref, computed } from "vue";
import { Doughnut } from "vue-chartjs";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
} from "chart.js";

ChartJS.register(Title, Tooltip, Legend, ArcElement);

const categories = ref([
  "Web Development",
  "Marketing",
  "Programming",
  "Finance",
  "Leadership",
  "Data Science",
  "Design",
  "Hardware",
  "Economics",
]);

// User's enrolled courses (replace with actual data from the backend)
const enrolledCourses = ref([
  { id: 1, title: "Intro to Web Dev", category: "Web Development" },
  { id: 2, title: "JavaScript Basics", category: "Programming" },
  { id: 3, title: "Advanced Python", category: "Programming" },
  { id: 4, title: "Data Analysis", category: "Data Science" },
]);

// Calculate percentages for each category
const categoryPercentages = computed(() => {
  const totalCourses = enrolledCourses.value.length;
  const counts = {};

  categories.value.forEach((category) => (counts[category] = 0));
  enrolledCourses.value.forEach((course) => {
    if (counts[course.category] !== undefined) {
      counts[course.category]++;
    }
  });

  return categories.value.map((category) => ({
    category,
    percentage: ((counts[category] / totalCourses) * 100).toFixed(2),
  }));
});

const chartData = computed(() => ({
  labels: categoryPercentages.value
    .filter((item) => item.percentage > 0) // Only show non-zero categories
    .map((item) => item.category),
  datasets: [
    {
      label: "Enrollment Percentage",
      data: categoryPercentages.value
        .filter((item) => item.percentage > 0)
        .map((item) => item.percentage),
      backgroundColor: [
        "#4caf50",
        "#03a9f4",
        "#ff9800",
        "#e91e63",
        "#9c27b0",
        "#3f51b5",
        "#009688",
        "#8bc34a",
        "#ff5722",
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
</script>

<style scoped>
.course-breakdown {
  text-align: center;
  width: 550px;
  height: 550px; 
  margin: 0 auto;
}

</style>
