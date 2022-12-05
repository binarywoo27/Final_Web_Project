<template>
  <b-container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <b-alert variant="secondary" show><h3>비밀번호 재설정</h3></b-alert>
      </b-col>
    </b-row>
    <b-row>
      <b-col></b-col>
      <b-col cols="8">
        <b-card class="text-center mt-3" style="max-width: 40rem" align="left">
          <b-form class="text-left">
            <b-alert show variant="danger" v-if="isPasswordChkError">비밀번호를 확인하세요.</b-alert>
            <b-form-group label="새로운 비밀번호:" label-for="userpwd">
              <b-form-input
                type="password"
                id="userpwd"
                v-model="user.password"
                required
                placeholder="비밀번호 입력...."
                @keyup.enter="resetpw"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="비밀번호 확인:" label-for="userpwdChk">
              <b-form-input
                type="password"
                id="userpwdChk"
                v-model="user.passwordChk"
                required
                placeholder="비밀번호 확인 입력...."
                @keyup.enter="resetpw"
              ></b-form-input>
            </b-form-group>
            <b-button type="button" variant="dark" class="m-1 btn-custom" @click="resetpw">비밀번호 재설정</b-button>
            <b-button type="button" variant="secondary" class="m-1" @click="movePage">처음 화면으로</b-button>
          </b-form>
        </b-card>
      </b-col>
      <b-col></b-col>
    </b-row>
  </b-container>
</template>

<script>
import { mapActions, mapState } from "vuex";
const memberStore = "memberStore";

export default {
  name: "ResetPw",
  data() {
    return {
      user: {
        id: null,
        password: null,
        passwordChk: null,
      },
    };
  },
  computed: {
    ...mapState(memberStore, ["resetId", "isPasswordChkError"]),
  },
  created() {
    this.user.id = this.resetId;
  },
  methods: {
    ...mapActions(memberStore, ["doReset", "setPwChkErr"]),
    async resetpw() {
        // console.log(this.resetId);
        if (this.user.password === this.user.passwordChk) {
            var id = this.user.id;
            var password = this.user.password;
            await this.doReset({id, password});
            this.$router.push({ name: "login" });
        } else {
            // alert("비밀번호가 일치하지 않습니다. 다시 시도해 주세요.");
            await this.setPwChkErr();
            this.$router.push({ name: "resetpw" });
        }
    },
    movePage() {
      this.$router.push({ name: "main" });
    },
  },
};
</script>

<style>
.btn-custom {
  background-color: #353866;
}
</style>
