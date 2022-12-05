<template>
  <b-container class="bv-example-row mt-3">
    <h3 class="">
      <b-icon icon="question-circle"></b-icon> QnA Service
    </h3>
    <b-row>
      <b-col>
        <b-alert show variant="info"
          ><h3>QnA 목록</h3>
          <i
            >이용하시면서 겪으신 불편사항, 건전한 제언, 칭송 및 문의 사항을
            남겨주세요.</i
          >
        </b-alert>
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col class="text-right">
        <b-button variant="outline-dark" @click="moveWrite()"
          >글쓰기</b-button
        >
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-table
          striped
          hover
          :items="articles"
          :fields="fields"
          @row-clicked="viewArticle"
        >
          <template #cell(subject)="data">
            <router-link
              :to="{
                name: 'qnaview',
                params: { articleno: data.item.articleno },
              }"
            >
              {{ data.item.subject }}
            </router-link>
          </template>
        </b-table>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import { listArticle } from "@/api/qna";
import { mapMutations, mapState } from "vuex";

const memberStore = "memberStore";

export default {
  name: "QnaList",
  data() {
    return {
      articles: [],
      fields: [
        { key: "articleno", label: "글번호", tdClass: "tdClass" },
        { key: "subject", label: "제목", tdClass: "tdSubject" },
        { key: "userid", label: "작성자", tdClass: "tdClass" },
        { key: "regtime", label: "작성일", tdClass: "tdClass" },
        { key: "hit", label: "조회수", tdClass: "tdClass" },
      ],
    };
  },
  created() {
    let param = {
      pg: 1,
      spp: 20,
      key: null,
      word: null,
    };
    listArticle(
      param,
      ({ data }) => {
        this.articles = data;
      },
      (error) => {
        console.log(error);
      }
    );
  },
  computed: {
    ...mapState(memberStore, ["routeTo"]),
  },
  methods: {
    ...mapMutations(memberStore, ["SET_ROUTE_TO"]),
    moveWrite() {
      this.SET_ROUTE_TO("qnawrite");

      this.$router.push({ name: "qnawrite" });
    },
    viewArticle(article) {
      this.$router.push({
        name: "qnaview",
        params: { articleno: article.articleno },
      });
    },
  },
};
</script>
<style scope>
.tdClass {
  width: 50px;
  text-align: center;
}
.tdSubject {
  width: 300px;
  text-align: center;
}
.underline-purple {
  display: inline-block;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0) 70%,
    rgba(228, 29, 191, 0.3) 30%
  );
}
a {
  color: #1e2ace;
}
</style>
