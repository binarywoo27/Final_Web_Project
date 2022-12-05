<template>
  <b-container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <b-alert variant="info" show><h3>로그인</h3></b-alert>
      </b-col>
    </b-row>
    <b-row>
      <b-col></b-col>
      <b-col cols="8">
        <b-card class="text-center mt-3" style="max-width: 40rem" align="left">
          <b-form class="text-left">
            <b-alert show variant="danger" v-if="isLoginError"
              >아이디 또는 비밀번호를 확인하세요.</b-alert
            >
            <b-form-group label="아이디:" label-for="userid">
              <b-form-input
                id="userid"
                v-model="user.id"
                required
                placeholder="아이디 입력...."
                @keyup.enter="confirm"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="비밀번호:" label-for="userpwd">
              <b-form-input
                type="password"
                id="userpwd"
                v-model="user.password"
                required
                placeholder="비밀번호 입력...."
                @keyup.enter="confirm"
              ></b-form-input>
            </b-form-group>
            <b-button type="button" variant="dark" class="m-1 btn-custom" @click="confirm">로그인</b-button>
            <b-button type="button" variant="dark" class="m-1 btn-custom" @click="movePage">회원가입</b-button>
            <b-button type="button" variant="secondary" class="m-1" @click="movePage2">비밀번호를 까먹었나요?</b-button>
          </b-form>
        </b-card>
      </b-col>
      <b-col></b-col>
    </b-row>
  </b-container>
</template>

<script>
import { mapState, mapActions } from "vuex";

const memberStore = "memberStore";

export default {
  name: "UserLogin",
  data() {
    return {
      // isLoginError: false,
      user: {
        id: null,
        password: null,
      },
    };
  },
  computed: {
    ...mapState(memberStore, [
      "isLogin",
      "isLoginError",
      "userInfo",
      "routeTo",
    ]),
  },
  methods: {
    ...mapActions(memberStore, ["userConfirm", "getUserInfo"]),
    async confirm() {
      await this.userConfirm(this.user);
      let token = sessionStorage.getItem("access-token");
      // console.log("1. confirm() token >> " + token);
      if (this.isLogin) {
        await this.getUserInfo(token);
        console.log("4. confirm() userInfo :: ", this.userInfo);

        // 검증 필요 (jw)
        if (this.routeTo != null) {
          this.$router.push({ name: this.routeTo });
        } else {
          this.$router.push({ name: "main" });
        }
      }
    },
    movePage() {
      this.$router.push({ name: "join" });
    },
    movePage2() {
      this.$router.push({ name: "findpw" });
    }
  },
};
</script>

<style>
.btn-custom {
  background-color: #353866;
}
</style>
