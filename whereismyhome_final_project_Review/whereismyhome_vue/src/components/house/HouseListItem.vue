<template>
  <b-row
    class="m-2"
    @click="selectHouse"
    @mouseover="colorChange(true)"
    @mouseout="colorChange(false)"
    :class="{ 'mouse-over-bgcolor': isColor }"
  >
    <b-col cols="2" class="text-center align-self-center">
      <!-- <b-img thumbnail src="https://picsum.photos/250/250/?image=58" alt="Image 1"></b-img> -->
      <p class="h1 mb-2"><b-icon icon="building"></b-icon></p>
    </b-col>
    <b-col cols="10" class="align-self-center"> {{ house.아파트 }} | {{ house.거래금액 }}만원 </b-col>
  </b-row>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { houseHit } from "@/api/house.js";

const houseStore = "houseStore";

export default {
  name: "HouseListItem",
  data() {
    return {
      isColor: false,
      sidoGugun: null,
    };
  },
  computed: {
    ...mapState(houseStore, ["addresses", "selectGeo"]),
  },
  props: {
    house: Object,
  },
  created() {
    this.sidoGugun = this.selectGeo.geo;
  },
  methods: {
    ...mapActions(houseStore, ["setSelectGeo"]),
    selectHouse() {
      const params = {
        roadCode: this.house.도로명코드,
        sidoName: this.selectGeo.sidoName,
        gugunName: this.selectGeo.gugunName,
        houseName: this.house.아파트,
      };
      this.setSelectGeo({
        range: "close",
        sidoName: this.selectGeo.sidoName,
        gugunName: this.selectGeo.gugunName,
        geo:
          this.sidoGugun +
          " " +
          this.house.도로명 +
          " " +
          this.house.도로명건물본번호코드 +
          " " +
          this.house.도로명건물부번호코드,
      });

      houseHit(params);
      (error) => {
        console.log(error);
      };
    },
    colorChange(flag) {
      this.isColor = flag;
    },
  },
};
</script>

<style scoped>
.apt {
  width: 50px;
}
.mouse-over-bgcolor {
  background-color: lightblue;
}
</style>
