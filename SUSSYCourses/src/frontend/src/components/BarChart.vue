<template>
    <div>
      <canvas ref="barChart"></canvas>
    </div>
  </template>
  
  <script setup>
  import { ref, watch, defineProps } from 'vue';
  import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js';
  
  ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);
  
  const props = defineProps({
    contentQualityRating: {
      type: Number,
      required: true,
    },
    teachingCompetenceRating: {
      type: Number,
      required: true,
    },
    workloadBalanceRating: {
      type: Number,
      required: true,
    },
  });
  
  const barChart = ref(null);
  
  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      title: {
        display: true,
      },
    },
  };
  
  const createChart = () => {
    new ChartJS(barChart.value, {
      type: 'bar',
      data: {
        labels: ['Content Quality', 'Teaching Competence', 'Workload Balance'],
        datasets: [
          {
            label: 'Ratings',
            data: [
              props.contentQualityRating,
              props.teachingCompetenceRating,
              props.workloadBalanceRating
            ],
            backgroundColor: ['#4caf50', '#ff9800', '#f44336'],
          },
        ],
      },
      options: chartOptions,
    });
  };
  
  watch(
    () => [props.contentQualityRating, props.teachingCompetenceRating, props.workloadBalanceRating],
    createChart,
    { immediate: true }
  );
  </script>
  
  <style scoped>
  canvas {
    max-width: 100%;
    height: 270px;
  }
  </style>
  