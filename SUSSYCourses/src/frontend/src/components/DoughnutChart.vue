<template>
    <div class ="doughnut-container">
      <Doughnut :data="chartData" :options="chartOptions" />
    </div>
  </template>
  
  <script setup>
  import { ref } from "vue";
  import { Doughnut } from "vue-chartjs";
  import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
  
  ChartJS.register(ArcElement, Tooltip, Legend);
  
  const chartData = ref({
    labels: ["Completed", "Remaining"],
    datasets: [
      {
        data: [75, 25], // Example progress
        backgroundColor: ["#4caf50", "#d3d3d3"],
        hoverBackgroundColor: ["#66bb6a", "#e0e0e0"],
      },
    ],
  });
  
  const chartOptions = ref({
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: "bottom",
      },
    },
  });
  </script>


  <style scoped>
.doughnut-container {
  width: 200px;
  height: 200px; 
  margin: 25px auto; 
}
</style>
