<template>
  <div id="house">
    <b-row>
      <b-col class="sm-3">
        <house-list />
      </b-col>
      <b-col class="sm-3">
        <house-search-bar />
        <house-map-button />
        <house-map />
      </b-col>
    </b-row>
  </div>
</template>

<script>
import HouseSearchBar from "@/components/house/HouseSearchBar.vue";
import HouseList from "@/components/house/HouseList.vue";
import HouseMapButton from "@/components/house/HouseMapButton.vue";
import HouseMap from "@/components/house/HouseMap.vue";

export default {
  name: "AppHouse",
  components: {
    HouseSearchBar,
    HouseList,
    HouseMapButton,
    HouseMap,
  },
};
</script>
<style scoped>
.underline-orange {
  display: inline-block;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(231, 149, 27, 0.3) 30%);
}

#house {
  margin: 10px 30px 10px 30px;
}
</style>
