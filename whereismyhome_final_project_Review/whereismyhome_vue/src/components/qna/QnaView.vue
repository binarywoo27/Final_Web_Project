<template>
  <b-container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <b-alert show><h3>QnA 글보기</h3></b-alert>
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col class="text-left">
        <b-button variant="outline-primary" @click="moveList">목록</b-button>
      </b-col>
      <b-col
        class="text-right"
        v-if="userInfo != null && userInfo.id === article.userid"
      >
        <b-button
          variant="outline-info"
          size="sm"
          @click="moveModifyArticle"
          class="mr-2"
          >글수정</b-button
        >
        <b-button variant="outline-danger" size="sm" @click="deleteArticle"
          >글삭제</b-button
        >
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col>
        <b-card
          :header-html="`<h3>${article.articleno}.
          ${article.subject} </h3>
          <div >조회수: ${article.hit}</div>
          <div><h6>${article.userid}</div><div>${article.regtime}</h6></div>`"
          class="mb-2"
          border-variant="dark"
          no-body
        >
          <b-card-body class="text-left">
            <div v-html="message"></div>
            <div v-if="files.length > 0">
              파일명: {{ article.filename }}
              <div v-for="fileName in files" :key="fileName">
                <div v-if="fileName == article.dbfilename">
                  <img :src="`${backendUrl}/image/${fileName}`" alt="이미지" width="80%" text-align:center/>
                </div>
              </div>
            </div>
          </b-card-body>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
// import moment from "moment";
import { getArticle } from "@/api/qna";
import { mapState } from "vuex";
import axios from "axios";

const memberStore = "memberStore";

export default {
  name: "QnaDetail",
  data() {
    return {
      article: {},
      files: [],
    };
  },
  computed: {
    ...mapState(memberStore, ["userInfo"]),
    message() {
      if (this.article.content)
        return this.article.content.split("\n").join("<br>");
      return "";
    },
    backendUrl() {
      return process.env.VUE_APP_API_BASE_URL;
    },
  },
  mounted() {
    this.fetchFiles();
  },
  created() {
    let param = this.$route.params.articleno;
    getArticle(
      param,
      ({ data }) => {
        this.article = data;
      },
      (error) => {
        console.log(error);
      }
    );
  },
  methods: {
    async fetchFiles() {
      // console.log("url ==> " + this.backendUrl);
      const response = await axios.get(`${this.backendUrl}/files`);
      this.files = response.data;
      for (var i = 0; i < this.files.length; i++) {
        console.log(this.files[i]);
      }
    },
    moveModifyArticle() {
      this.$router.replace({
        name: "qnamodify",
        params: { articleno: this.article.articleno },
      });
      //   this.$router.push({ path: `/board/modify/${this.article.articleno}` });
    },
    deleteArticle() {
      if (confirm("게시글을 정말로 삭제하시겠습니까?")) {
        this.$router.replace({
          name: "qnadelete",
          params: { articleno: this.article.articleno },
        });
      }
    },
    moveList() {
      this.$router.push({ name: "qnalist" });
    },
  },
  // filters: {
  //   dateFormat(regtime) {
  //     return moment(new Date(regtime)).format("YY.MM.DD hh:mm:ss");
  //   },
  // },
};
</script>

<style></style>
