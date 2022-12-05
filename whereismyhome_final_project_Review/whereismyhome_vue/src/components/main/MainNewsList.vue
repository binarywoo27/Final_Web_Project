<template>
  <b-container v-if="news && news.length != 0" class="bv-example-row mt-3">
    <br>
    <h3>실시간 부동산 뉴스 정보</h3>
    <br>
    <b-row>
      <b-col>
        <main-news-list-item v-for="index in news.length / 2" :key="index" :value="news[index - 1]" />
      </b-col>
      <b-col>
        <main-news-list-item v-for="index in news.length / 2" :key="index" :value="news[index + news.length / 2 - 1]" />
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import axios from "axios";
import MainNewsListItem from "@/components/main/MainNewsListItem.vue";
export default {
  name: "MainNewsList",
  components: {
    MainNewsListItem,
  },
  data() {
    return {
      news: null,
      img: [
        "news_img1.jpg",
        "news_img2.jpg",
        "news_img3.jpg",
        "news_img4.jpg",
        "news_img5.jpg",
        "news_img6.jpg",
        "news_img7.jpg",
        "news_img8.jpg",
        "news_img9.jpg",
        "news_img10.jpg",
      ],
    };
  },
  created() {
    return (
      axios
        .get(`http://localhost:8000/news/`)
        // .then(response => this.news = response)
        .then((res) => res)
        .then((data) => (this.news = data.data.items))
        .catch((e) => console.log(e))
    );
  },
};
</script>
