<template>
  <div id="houseList">
    <b-container class="bv-example-row mt-3">
      <b-row class="m-2">
        <b-col cols="2" class="text-center align-self-center"
          ><p class="h1 mb-2"><b-icon icon="building"></b-icon></p
        ></b-col>
        <b-col cols="10" class="align-self-center"><strong>아파트 이름 | 가격</strong></b-col>
      </b-row>
    </b-container>
    <hr />
    <b-container v-if="houses && houses.length != 0" class="bv-example-row mt-3">
      <div id="houseListItem">
        <house-list-item v-for="(house, index) in itemsForList" :key="index" :house="house" />
      </div>
      <b-pagination
        v-model="currentPage"
        :total-rows="rows"
        :per-page="perPage"
        aria-controls="houseListItem"
        align="center"
      ></b-pagination>
    </b-container>
    <b-container v-else class="bv-example-row mt-3">
      <b-row>
        <b-col><b-alert show>주택 목록이 없습니다.</b-alert></b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import HouseListItem from "@/components/house/HouseListItem";
import { mapState } from "vuex";

const houseStore = "houseStore";

export default {
  name: "HouseList",

  components: {
    HouseListItem,
  },

  data() {
    return {
      tables: [{ 매매리스트: null, 아파트이름: null, 가격: null }],
      perPage: 9,
      currentPage: 1,
    };
  },

  computed: {
    ...mapState(houseStore, ["houses"]),
    rows() {
      return this.houses.length;
    },
    itemsForList() {
      return this.houses.slice((this.currentPage - 1) * this.perPage, this.currentPage * this.perPage);
    },
  },

  watch: {
    houses: function () {
      this.currentPage = 1;
    },
  },
};
</script>

<style>
#houseList {
  margin-top: 55px;
}
</style>
