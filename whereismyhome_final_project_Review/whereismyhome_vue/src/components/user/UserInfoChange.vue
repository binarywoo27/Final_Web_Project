<template>
  <b-container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <b-alert variant="info" show><h3>회원정보 수정</h3></b-alert>
      </b-col>
    </b-row>
    <b-row>
      <b-col></b-col>
      <b-col cols="8">
        <b-card class="text-center mt-3" style="max-width: 40rem" align="left">
          <b-form class="text-left">
            <b-form-group label="변경할 비밀번호:" label-for="userpwd">
              <b-form-input
                type="password"
                id="userpwd"
                v-model="user.password"
                required
                placeholder="변경할 비밀번호 입력..."
                @keyup.enter="update"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="변경할 주소:" label-for="address">
              <b-form-input
                id="address"
                v-model="user.address"
                required
                :value = "user.address"
                @keyup.enter="update"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="변경할 전화번호:" label-for="phone">
              <b-form-input
                id="phone"
                v-model="user.phone"
                required
                :value = "user.phone"
                @keyup.enter="update"
              ></b-form-input>
            </b-form-group>
            <b-button type="button" variant="primary" class="m-1 btn-custom" @click="update"
              >업데이트</b-button
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
  name: "UserInfoChange",
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
    ...mapState(memberStore, ["userInfo", "updateCheck"]),
  },
  created() {
        this.user.id = this.userInfo.id;
        this.user.password = this.userInfo.password;
        this.user.name = this.userInfo.name;
        this.user.address = this.userInfo.address;
        this.user.phone = this.userInfo.phone;
  },
  methods: {
    ...mapActions(memberStore, ["doUpdate"]),
    async update() {
      var id = this.userInfo.id;
      var password = this.user.password;
      var name = this.userInfo.name;
      var address = this.user.address;
      var phone = this.user.phone;
      // console.log("변경정보 :: " + id + " " + password + " " + name + " " + address + " " + phone);
      await this.doUpdate({ id, password, name, address, phone });
      // console.log(this.updateCheck);
      if (this.updateCheck) {
        this.$router.push({ name: "main" });
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
