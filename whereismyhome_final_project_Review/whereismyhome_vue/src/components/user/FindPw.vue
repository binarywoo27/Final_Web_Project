<template>
  <b-container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <b-alert variant="secondary" show><h3>계정 확인 페이지 입니다.</h3></b-alert>
      </b-col>
    </b-row>
    <b-row>
      <b-col></b-col>
      <b-col cols="8">
        <b-card class="text-center mt-3" style="max-width: 40rem" align="left">
          <b-form class="text-left">
            <b-form-group label="아이디:" label-for="userid">
              <b-form-input
                id="userid"
                v-model="user.id"
                required
                placeholder="아이디 입력...."
                @keyup.enter="findpw"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="전화번호:" label-for="phone">
              <b-form-input
                id="phone"
                v-model="user.phone"
                required
                placeholder="전화번호 입력...."
                @keyup.enter="findpw"
              ></b-form-input>
            </b-form-group>
            <b-button type="button" variant="dark" class="m-1 btn-custom" @click="checkAcc">계정 확인하기</b-button>
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
  name: "FindPw",
  data() {
    return {
      user: {
        id: null,
        phone: null,
      },
    };
  },
  computed: {
    ...mapState(memberStore, ["resetId"]),
  },
  methods: {
    ...mapActions(memberStore, ["checkAccount", "clearResetId"]),
    async checkAcc() {
        var id = this.user.id;
        var phone = this.user.phone;
        await this.checkAccount({id, phone});
        if (this.resetId == this.user.id) {
            this.$router.push({ name: "resetpw" });
        } else {
            this.$router.push({ name: "main" });
        }
    },
    async movePage() {
      await this.clearResetId();
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
