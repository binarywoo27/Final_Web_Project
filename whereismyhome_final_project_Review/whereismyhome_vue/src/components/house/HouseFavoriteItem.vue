<template>
  <b-row
    class="m-2"
    @mouseover="colorChange(true)"
    @mouseout="colorChange(false)"
    :class="{ 'mouse-over-bgcolor': isColor }"
  >
    <b-col cols="2" class="text-center align-self-center">
      <p class="h3 mb-2"><b-icon icon="pin-map"></b-icon></p>
    </b-col>
    <b-col cols="8" class="align-self-center" @click="setSidoGugun"> [{{ favorite.sido }}] {{ favorite.gugun }} </b-col>
    <b-col cols="2" class="align-self-center" @click="deleteFavorite"><b-icon icon="x"></b-icon></b-col>
  </b-row>
</template>

<script>
import { favoritesDel } from "@/api/house.js";
import { mapActions } from "vuex";

const houseStore = "houseStore";

export default {
  name: "HouseFavoriteItem",
  data() {
    return {
      isColor: false,
    };
  },
  props: {
    favorite: Object,
  },
  methods: {
    ...mapActions(houseStore, ["getFavorites", "setFavoSidoGugun"]),

    setSidoGugun() {
      const data = {
        sidoCode: this.favorite.sidoCode,
        gugunCode: this.favorite.gugunCode,
      };
      this.setFavoSidoGugun(data);
    },

    deleteFavorite() {
      const params = {
        gugunCode: this.favorite.gugunCode,
        id: this.favorite.id,
      };
      // console.log(params);

      favoritesDel(
        params,
        ({ data }) => {
          // console.log(data);
          data;
          this.getFavorites(this.favorite.id);
        },
        (error) => {
          console.log(error);
        }
      );
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
  background-color: #86cecb;
}
</style>
