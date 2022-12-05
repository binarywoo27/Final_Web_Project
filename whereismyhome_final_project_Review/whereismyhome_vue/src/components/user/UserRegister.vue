<template>
  <b-container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <b-alert variant="info" show><h3>회원가입</h3></b-alert>
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
                @keyup.enter="regist"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="비밀번호:" label-for="userpwd">
              <b-form-input
                type="password"
                id="userpwd"
                v-model="user.password"
                required
                placeholder="비밀번호 입력...."
                @keyup.enter="regist"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="이름:" label-for="name">
              <b-form-input
                id="name"
                v-model="user.name"
                required
                placeholder="이름 입력...."
                @keyup.enter="regist"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="주소:" label-for="address">
              <b-form-input
                id="address"
                v-model="user.address"
                required
                placeholder="주소 입력...."
                @keyup.enter="regist"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="전화번호:" label-for="phone">
              <b-form-input
                id="phone"
                v-model="user.phone"
                required
                placeholder="전화번호 입력...."
                @keyup.enter="regist"
              ></b-form-input>
            </b-form-group>
            <b-button type="button" variant="dark" class="m-1 btn-custom" @click="regist"
              >회원가입</b-button
            >
            <b-button type="button" variant="secondary" class="m-1" @click="movePage"
              >처음 화면으로</b-button
            >
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
  name: "UserRegister",
  data() {
    return {
      user: {
        id: null,
        password: null,
        name: null,
        address: null,
        phone: null,
      },
    };
  },
  computed: {
    ...mapState(memberStore, ["registCheck"]),
  },
  methods: {
    ...mapActions(memberStore, ["doRegist"]),
    async regist() {
      var id = this.user.id;
      var password = this.user.password;
      var name = this.user.name;
      var address = this.user.address;
      var phone = this.user.phone;
      // console.log(id + " " + password + " " + name + " " + address + " " + phone);
      await this.doRegist({ id, password, name, address, phone });
      // console.log(this.registCheck);
      if (this.registCheck) {
        this.$router.push({ name: "login" });
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
