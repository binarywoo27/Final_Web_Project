<template>
  <div id="houseRank">
    <b-row class="mb-4">
      <b-col class="text-center">
        <h3><b-icon icon="bar-chart"></b-icon> 아파트 매매 정보 조회수 랭킹</h3></b-col
      >
    </b-row>
    <Bar
      :chart-options="chartOptions"
      :chart-data="chartData"
      :chart-id="chartId"
      :dataset-id-key="datasetIdKey"
      :plugins="plugins"
      :css-classes="cssClasses"
      :styles="styles"
      :width="width"
      :height="height"
    />
  </div>
</template>

<script>
import { Bar } from "vue-chartjs/legacy";
import { getHouseRank } from "@/api/house.js";

import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from "chart.js";

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);

export default {
  name: "MainHouseRank",
  components: {
    Bar,
  },
  props: {
    chartId: {
      type: String,
      default: "bar-chart",
    },
    datasetIdKey: {
      type: String,
      default: "label",
    },
    width: {
      type: Number,
      default: 400,
    },
    height: {
      type: Number,
      default: 400,
    },
    cssClasses: {
      default: "",
      type: String,
    },
    styles: {
      type: Object,
      default: () => {},
    },
    plugins: {
      type: Array,
      default: () => [],
    },
  },

  data() {
    return {
      chartData: {
        labels: [],
        datasets: [
          {
            label: "조회수",
            backgroundColor: "#86cecb",
            data: [],
          },
        ],
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
      },
    };
  },

  created() {
    getHouseRank(
      ({ data }) => {
        for (let i = 0; i < data.length; i++) {
          this.chartData.labels.push(data[i].houseName);
          this.chartData.datasets[0].data.push(data[i].hit);
        }
      },
      (error) => {
        console.log(error);
      }
    );
  },
};
</script>

<style>
#houseRank {
  margin-top: 40px;
}
</style>
