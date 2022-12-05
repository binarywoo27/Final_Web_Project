<template>
  <b-modal id="modal-1" title="당신의 관심지역" ok-only centered>
    <b-container v-if="favorites && favorites.length != 0" class="bv-example-row mt-3">
      <div id="favoriteListItem">
        <house-favorite-item v-for="(favorite, index) in itemsForList" :key="index" :favorite="favorite" />
      </div>
      <b-pagination
        v-model="currentPage"
        :total-rows="rows"
        :per-page="perPage"
        aria-controls="favoriteListItem"
        align="center"
      ></b-pagination>
    </b-container>
    <b-container v-else class="bv-example-row mt-3">
      <b-row>
        <b-col><b-alert show>관심 지역이 없습니다.</b-alert></b-col>
      </b-row>
    </b-container>
  </b-modal>
</template>

<script>
import HouseFavoriteItem from "@/components/house/HouseFavoriteItem";
import { mapState } from "vuex";

const houseStore = "houseStore";

export default {
  name: "HouseFavorite",
  components: {
    HouseFavoriteItem,
  },
  data() {
    return {
      perPage: 7,
      currentPage: 1,
    };
  },
  computed: {
    ...mapState(houseStore, ["favorites"]),
    rows() {
      return this.favorites.length;
    },
    itemsForList() {
      return this.favorites.slice((this.currentPage - 1) * this.perPage, this.currentPage * this.perPage);
    },
  },
};
</script>
