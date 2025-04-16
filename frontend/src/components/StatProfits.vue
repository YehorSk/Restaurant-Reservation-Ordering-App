<script>
import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default {
  name: "StatProfits" ,
  components: {Bar},
  props:['data', 'label'],
  data(){
    return {
      chartOptions: {
        responsive: true
      }
    }
  },
  computed: {
    chartData() {
      const months = this.data.months;
      const dataStats = this.data.data_stats;

      const translatedMonths = months.map((m, i) => this.$t(`Home.${m}`));

      return {
        labels: translatedMonths,
        datasets: [{
          label: this.label,
          data: dataStats,
          borderColor: 'rgba(75, 192, 192, 1)',
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderWidth: 1
        }]
      };
    }
  }
}
</script>

<template>
  <Bar
      id="my_chart"
      :options="chartOptions"
      :data="chartData"/>
</template>

<style scoped>

</style>