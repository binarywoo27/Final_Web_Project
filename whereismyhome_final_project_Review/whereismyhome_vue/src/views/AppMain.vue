<template>
  <b-container class="bv-example-row mt-3 text-center">
    <h3 class=""><b-icon icon="house"></b-icon> Where Is My Home?</h3>
    <div>
      <b-carousel
        id="carousel-1"
        v-model="slide"
        :interval="4000"
        controls
        indicators
        background="#ababab"
        img-width="1024"
        img-height="480"
        style="text-shadow: 1px 1px 2px #333"
        @sliding-start="onSlideStart"
        @sliding-end="onSlideEnd"
      >
        <!-- Text slides with image -->
        <b-carousel-slide :img-src="require(`@/assets/building/building2.jpg`)" @click="moveMain"></b-carousel-slide>
        <b-carousel-slide :img-src="require(`@/assets/building/building3.jpg`)" @click="moveMain"></b-carousel-slide>
        <b-carousel-slide :img-src="require(`@/assets/building/building1.jpg`)" @click="moveMain"></b-carousel-slide>
        <b-carousel-slide :img-src="require(`@/assets/building/building4.jpg`)" @click="moveMain"></b-carousel-slide>
      </b-carousel>
    </div>
    <main-news-list></main-news-list>
    <main-house-rank />
  </b-container>
</template>

<script>
import MainNewsList from "@/components/main/MainNewsList.vue";
import MainHouseRank from "@/components/main/MainHouseRank.vue";

export default {
  name: "AppMain",
  components: {
    MainNewsList,
    MainHouseRank,
  },
  props: {
    msg: String,
  },
  data() {
    return {
      slide: 0,
      sliding: null,
    };
  },
  methods: {
    onSlideStart(slide) {
      slide, (this.sliding = true);
    },
    onSlideEnd(slide) {
      slide, (this.sliding = false);
    },
    moveMain() {
      this.$router.push({ name: "main" });
    },
  },
};
</script>

<style scoped>
.underline-steelblue {
  display: inline-block;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(72, 190, 233, 0.3) 30%);
}
</style>
