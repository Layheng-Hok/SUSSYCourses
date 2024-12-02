<template>
  <div class="doughnut-container">
    <Doughnut :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
import { computed, defineProps } from "vue";
import { Doughnut } from "vue-chartjs";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";

ChartJS.register(ArcElement, Tooltip, Legend);

const props = defineProps({
  data: Array,   
  colors: Array,
});

const chartData = computed(() => ({
  labels: ['Completed', 'Remaining'],
  datasets: [
    {
      data: props.data,
      backgroundColor: props.colors,
    },
  ],
}));

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

</script>


  <style scoped>
.doughnut-container {
  width: 200px;
  height: 200px; 
  margin: 25px auto; 
}
</style>
