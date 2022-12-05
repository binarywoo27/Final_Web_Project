<template>
  <b-row class="mb-1">
    <b-col style="text-align: left">
      <b-form @submit="onSubmit" @reset="onReset">
        <b-form-group id="userid-group" label="작성자:" label-for="userid">
          <b-form-input
            id="userid"
            :disabled="isUserid"
            v-model="article.userid"
            type="text"
            required
            placeholder="작성자 입력..."
            >${article.userid}</b-form-input
          >
        </b-form-group>

        <b-form-group
          id="subject-group"
          label="제목:"
          label-for="subject"
          description="제목을 입력하세요."
        >
          <b-form-input
            id="subject"
            v-model="article.subject"
            type="text"
            required
            placeholder="제목 입력..."
          ></b-form-input>
        </b-form-group>

        <b-form-group id="content-group" label="내용:" label-for="content">
          <b-form-textarea
            id="content"
            v-model="article.content"
            placeholder="내용 입력..."
            rows="10"
            max-rows="15"
          ></b-form-textarea>
        </b-form-group>

        <b-form-group id="content-group" label="이미지:" label-for="content">
          <!-- <input
            type="file"
            id="content"
            accept="image/*"
            @change="fileChange"
          />

          <p>
            upload 이미지 :{{ file.name }} ({{ file.size }}) / {{ file.type }}
          </p> -->
          <!-- <label for="file">일반 사진 등록</label><br /> -->
          <input
            type="file"
            @change="selectFile"
            id="file"
            accept="image/*"
            ref="fileRef"
          />
          <!-- <div v-if="files.length > 0">
            <div v-for="fileName in files" :key="fileName">
              <img :src="`${backendUrl}/image/${fileName}`" alt="이미지" />
            </div>
          </div> -->
        </b-form-group>

        <b-button
          type="submit"
          variant="dark"
          class="m-1 btn-button"
          v-if="this.type === 'register'"
          >글작성</b-button
        >
        <b-button type="submit" variant="primary" class="m-1" v-else
          >글수정</b-button
        >
        <b-button type="reset" variant="secondary" class="m-1">초기화</b-button>
      </b-form>
    </b-col>
  </b-row>
</template>

<script>
import { writeArticle, modifyArticle, getArticle } from "@/api/qna";
import { mapState } from "vuex";
import axios from "axios";

const memberStore = "memberStore";
const formData = new FormData();

export default {
  name: "QnaInputItem",
  data() {
    return {
      article: {
        articleno: 0,
        userid: "",
        subject: "",
        content: "",
        filename: "",
      },
      isUserid: false,
      files: [], //업로드용 파일
    };
  },
  computed: {
    ...mapState(memberStore, ["userInfo"]),
    backendUrl() {
      return process.env.VUE_APP_API_BASE_URL;
    },
  },
  // mounted() {
  //   this.fetchFiles();
  // },
  props: {
    type: { type: String },
  },
  created() {
    if (this.type === "modify") {
      let param = this.$route.params.articleno;
      getArticle(
        param,
        ({ data }) => {
          // this.article.articleno = data.article.articleno;
          // this.article.userid = data.article.userid;
          // this.article.subject = data.article.subject;
          // this.article.content = data.article.content;
          this.article = data;
        },
        (error) => {
          console.log(error);
        }
      );
    }
    this.isUserid = true;
    this.article.userid = this.userInfo.id;
  },
  methods: {
    async fetchFiles() {
      // console.log("url ==> " + this.backendUrl);
      const response = await axios.get(`${this.backendUrl}/files`);
      this.files = response.data;
    },
    selectFile(event) {
      for (const file of event.target.files) {
        this.article.filename = file.name;
        console.log("DTO에 저장될 파일 이름:" + this.article.filename);
        console.log("file 이름:" + file.name);
        formData.append("files", file);
      }
    },
    onSubmit(event) {
      event.preventDefault();

      let err = true;
      let msg = "";
      !this.article.userid &&
        ((msg = "작성자 입력해주세요"),
        (err = false),
        this.$refs.userid.focus());
      err &&
        !this.article.subject &&
        ((msg = "제목 입력해주세요"),
        (err = false),
        this.$refs.subject.focus());
      err &&
        !this.article.content &&
        ((msg = "내용 입력해주세요"),
        (err = false),
        this.$refs.content.focus());

      if (!err) alert(msg);
      else
        this.type === "register" ? this.registArticle() : this.modifyArticle();
    },
    onReset(event) {
      event.preventDefault();
      this.article.articleno = 0;
      this.article.subject = "";
      this.article.content = "";
      this.article.filename = "";
      this.moveList();
    },
    registArticle() {
      let param = {
        userid: this.article.userid,
        subject: this.article.subject,
        content: this.article.content,
        filename: this.article.filename,
      };
      formData.append("userid", this.article.userid);
      formData.append("subject", this.article.subject);
      formData.append("content", this.article.content);
      formData.append("filename", this.article.filename);
      // formData.append("files", file);    이건 위에서 처리된.

      writeArticle(
        param,
        ({ data }) => {
          let msg = "등록 처리시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "등록이 완료되었습니다.";

            axios
              .post(`${this.backendUrl}/files`, formData, {
                headers: { "Content-Type": "multipart/form-data" },
              })
              .then(() => {
                this.fetchFiles();
              })
              .catch(() => {
                alert("파일 업로드를 취소하였습니다.");
              });
          }
          alert(msg);
          this.moveList();
        },
        (error) => {
          console.log(error);
        }
      );
    },
    modifyArticle() {
      let param = {
        articleno: this.article.articleno,
        userid: this.article.userid,
        subject: this.article.subject,
        content: this.article.content,
        filename: this.article.filename,
      };
      modifyArticle(
        param,
        ({ data }) => {
          let msg = "수정 처리시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "수정이 완료되었습니다.";
          }
          alert(msg);
          // 현재 route를 /list로 변경.
          this.moveList();
        },
        (error) => {
          console.log(error);
        }
      );
    },
    moveList() {
      this.$router.push({ name: "qnalist" });
    },
  },
};
</script>

<style>
.btn-custom {
  background-color: #353866;
}
</style>
