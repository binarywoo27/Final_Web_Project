<template>
  <b-container class="mt-4" v-if="userInfo">
    <b-row>
      <b-col>
        <b-alert variant="info" show><h3>내정보</h3></b-alert>
      </b-col>
    </b-row>
    <b-row>
      <b-col></b-col>
      <b-col cols="8">
        <b-jumbotron>
          <template #header>My Page</template>

          <template #lead> 내 정보 확인페이지입니다. </template>

          <hr class="my-4" />

          <b-container class="mt-4">
            <b-row>
              <b-col cols="2"></b-col>
              <b-col cols="3" align-self="end">아이디</b-col
              ><b-col cols="4" align-self="start">{{ userInfo.id }}</b-col>
              <b-col cols="2"></b-col>
            </b-row>
            <b-row>
              <b-col cols="2"></b-col>
              <b-col cols="3" align-self="end">이름</b-col
              ><b-col cols="4" align-self="start">{{ userInfo.name }}</b-col>
              <b-col cols="2"></b-col>
            </b-row>
            <b-row>
              <b-col cols="2"></b-col>
              <b-col cols="3" align-self="end">주소</b-col
              ><b-col cols="4" align-self="start">{{ userInfo.address }}</b-col>
              <b-col cols="2"></b-col>
            </b-row>
            <b-row>
              <b-col cols="2"></b-col>
              <b-col cols="3" align-self="end">전화번호</b-col
              ><b-col cols="4" align-self="start">{{ userInfo.phone }}</b-col>
              <b-col cols="2"></b-col>
            </b-row>
          </b-container>
          <hr class="my-4" />

          <b-button variant="dark" @click="moveUpdate" class="mr-1 btn-custom">정보수정</b-button>
          <b-button variant="danger" @click="deleteUser" >회원탈퇴</b-button>
        </b-jumbotron>
      </b-col>
      <b-col></b-col>
    </b-row>
  </b-container>
</template>

<script>
import { mapActions, mapState } from "vuex";

const memberStore = "memberStore";

export default {
  name: "UserMyPage",
  components: {},
  computed: {
    ...mapState(memberStore, ["userInfo", "deleteCheck"]),
  },
  methods: {
    ...mapActions(memberStore, ["doDelete", "userLogout"]),
    async deleteUser() {
      const delCheck = confirm("정말 회원탈퇴를 하시겠습니까?");
      if (delCheck) {
        await this.doDelete(this.userInfo.id);
        await this.userLogout(this.userInfo.id);
      }
      if (this.deleteCheck) {
        this.$router.push({name: "main"});
      }
    },
    moveUpdate() {
      this.$router.push({name: "update"});
    },
  },
};
</script>

<style></style>
