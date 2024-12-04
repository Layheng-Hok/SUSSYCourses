<template>
  <div>
    <canvas ref="barChart"></canvas>
  </div>
</template>

<script setup>
import {ref, onMounted, watch, defineProps} from 'vue';
import {Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale} from 'chart.js';

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

const barChart = ref(null); // Ref to canvas
let chartInstance = null; // Variable to hold the chart instance

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    title: {
      display: true,
    },
  },
};

// Function to create chart
const createChart = () => {
  if (chartInstance) {
    chartInstance.destroy(); // Destroy the existing chart before creating a new one
  }
  chartInstance = new ChartJS(barChart.value, {
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

onMounted(() => {
  // Create chart when component is mounted
  createChart();
});

// Watch for changes to props and re-create the chart if necessary
watch(
    () => [props.contentQualityRating, props.teachingCompetenceRating, props.workloadBalanceRating],
    createChart
);
</script>

<style scoped>
canvas {
  max-width: 100%;
  height: 270px;
}
</style>
