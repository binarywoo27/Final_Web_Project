<template>
  <div id="mapButton">
    <b-row>
      <b-col></b-col>
      <b-col>
        <b-button-group size="sm">
          <b-button variant="dark" cols="auto" @click="clickButton(null)">기본</b-button>
          <b-button variant="primary" cols="auto" @click="clickButton('CS2')">편의점</b-button>
          <b-button variant="info" cols="auto" @click="clickButton('BK9')">은행</b-button>
          <b-button variant="warning" cols="auto" @click="clickButton('CE7')">카페</b-button>
          <b-button variant="danger" cols="auto" @click="clickButton('HP8')">병원</b-button>
        </b-button-group>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

const houseStore = "houseStore";

export default {
  name: "HouseMapButton",
  data() {
    return {
      searchValue: null,
    };
  },

  computed: {
    ...mapState(houseStore, ["searchButton"]),
  },

  methods: {
    ...mapActions(houseStore, ["setSearchButton"]),
    clickButton(data) {
      this.setSearchButton(data);
    },
  },
};
</script>

<style scoped>
#mapButton {
  margin: 10px 0px 10px 890px;
}
</style>
