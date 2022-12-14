import axios from "axios";
import jwtDecode from "jwt-decode";
import router from "@/router";
import { login, findById, tokenRegeneration, logout } from "@/api/member";

const memberStore = {
  namespaced: true,
  state: {
    isLogin: false,
    isLoginError: false,
    userInfo: null,
    isValidToken: false,
    registCheck: false,
    routeTo: null,
    updateCheck: false,
    deleteCheck: false,
    resetId: null,
    isPasswordChkError: false,
  },
  getters: {
    checkUserInfo: function (state) {
      return state.userInfo;
    },
    checkToken: function (state) {
      return state.isValidToken;
    },
  },
  mutations: {
    SET_IS_LOGIN: (state, isLogin) => {
      state.isLogin = isLogin;
    },
    SET_IS_LOGIN_ERROR: (state, isLoginError) => {
      state.isLoginError = isLoginError;
    },
    SET_IS_PASSWORD_CHK_ERROR: (state, isPasswordChkError) => {
      state.isPasswordChkError = isPasswordChkError;
    },
    SET_IS_VALID_TOKEN: (state, isValidToken) => {
      state.isValidToken = isValidToken;
    },
    SET_USER_INFO: (state, userInfo) => {
      state.isLogin = true;
      state.userInfo = userInfo;
    },
    clearRegist(state) {
      state.registCheck = false;
    },
    regist(state) {
      state.registCheck = true;
    },
    SET_ROUTE_TO: (state, route) => {
      state.routeTo = route;
    },
    CLEAR_ROUTE_TO: (state) => {
      state.routeTo = null;
    },
    clearUpdate(state) {
      state.updateCheck = false;
    },
    update(state) {
      state.updateCheck = true;
    },
    clearDelete(state) {
      state.deleteCheck = false;
    },
    delete(state) {
      state.deleteCheck = true;
    },
    SET_RESET_ID: (state, id) => {
      state.resetId = id;
    },
    CLEAR_RESET_ID: (state) => {
      state.resetId = null;
    }
  },
  actions: {
    async userConfirm({ commit }, user) {
      await login(
        user,
        ({ data }) => {
          if (data.message === "success") {
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];
            // console.log("login success token created!!!! >> ", accessToken, refreshToken);
            commit("SET_IS_LOGIN", true);
            commit("SET_IS_LOGIN_ERROR", false);
            commit("SET_IS_VALID_TOKEN", true);
            sessionStorage.setItem("access-token", accessToken);
            sessionStorage.setItem("refresh-token", refreshToken);
          } else {
            commit("SET_IS_LOGIN", false);
            commit("SET_IS_LOGIN_ERROR", true);
            commit("SET_IS_VALID_TOKEN", false);
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    async getUserInfo({ commit, dispatch }, token) {
      let decodeToken = jwtDecode(token);
      // console.log("2. getUserInfo() decodeToken :: ", decodeToken);
      await findById(
        decodeToken.id,
        ({ data }) => {
          if (data.message === "success") {
            commit("SET_USER_INFO", data.userInfo);
            // console.log("3. getUserInfo data >> ", data);
          } else {
            console.log("?????? ?????? ??????");
          }
        },
        async (error) => {
          console.log("getUserInfo() error code [?????? ???????????? ?????? ?????????.] ::: ", error.response.status);
          commit("SET_IS_VALID_TOKEN", false);
          await dispatch("tokenRegeneration");
        }
      );
    },
    async tokenRegeneration({ commit, state }) {
      // console.log("?????? ????????? >> ?????? ?????? ?????? : {}", sessionStorage.getItem("access-token"));
      await tokenRegeneration(
        JSON.stringify(state.userInfo),
        ({ data }) => {
          if (data.message === "success") {
            let accessToken = data["access-token"];
            // console.log("????????? ?????? >> ????????? ?????? : {}", accessToken);
            sessionStorage.setItem("access-token", accessToken);
            commit("SET_IS_VALID_TOKEN", true);
          }
        },
        async (error) => {
          // HttpStatus.UNAUTHORIZE(401) : RefreshToken ?????? ?????? >> ?????? ?????????!!!!
          if (error.response.status === 401) {
            console.log("?????? ??????");
            // ?????? ????????? ??? DB??? ????????? RefreshToken ??????.
            await logout(
              state.userInfo.userid,
              ({ data }) => {
                if (data.message === "success") {
                  console.log("???????????? ?????? ?????? ??????");
                } else {
                  console.log("???????????? ?????? ?????? ??????");
                }
                alert("RefreshToken ?????? ??????!!! ?????? ???????????? ?????????.");
                commit("SET_IS_LOGIN", false);
                commit("SET_USER_INFO", null);
                commit("SET_IS_VALID_TOKEN", false);
                router.push({ name: "login" });
              },
              (error) => {
                console.log(error);
                commit("SET_IS_LOGIN", false);
                commit("SET_USER_INFO", null);
              }
            );
          }
        }
      );
    },
    async userLogout({ commit }, userid) {
      await logout(
        userid,
        ({ data }) => {
          if (data.message === "success") {
            commit("SET_IS_LOGIN", false);
            commit("SET_USER_INFO", null);
            commit("SET_IS_VALID_TOKEN", false);
            commit("CLEAR_ROUTE_TO");
            console.log(this.routeTo);
          } else {
            console.log("?????? ?????? ??????");
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    doRegist({ commit }, { id, password, name, address, phone }) {
      commit("clearRegist");
      const params = {
        id: id,
        password: password,
        name: name,
        address: address,
        phone: phone,
      }
      console.log("adding user with id: "+" "+id);
      return axios
        .post('http://localhost:8000/user/regist', JSON.stringify(params), {
          headers: {
            'Content-Type': 'application/json; charset=utf-8',
            'Access-Control-Allow-Origin': '*',
          },
        })
        .then((res) => res)
        .then((data) => {
          if (data.data === '') {
            alert('???????????? ??????');
          } else {
            alert('???????????? ??????');
            commit('regist')
          }
        })
        .catch((e) => {
          console.log(e);
          alert('???????????? ????????? ????????? ??????');
        })
    },
    doUpdate({ commit }, { id, password, name, address, phone }) {
      commit("clearUpdate");
      const params = {
        id: id,
        password: password,
        name: name,
        address: address,
        phone: phone,
      }
      console.log("updating id:"+" "+id);
      return axios
        .put('http://localhost:8000/user/update', JSON.stringify(params), {
          headers: {
            'Content-Type': 'application/json; charset=utf-8',
            'Access-Control-Allow-Origin': '*',
          },
        })
        .then((res) => res)
        .then((data) => {
          if (data.data === '') {
            alert('???????????? ?????? ??????');
          } else {
            alert('???????????? ?????? ??????');
            commit('update')
          }
        })
        .catch((e) => {
          console.log(e);
          alert('???????????? ????????? ????????? ??????');
        })
    },
    doDelete({ commit }, id) {
      commit("clearDelete");
      const params = {
        id: id,
      }
      console.log("deleting id:" + " " + id);
      return axios
        .delete(`http://localhost:8000/user/delete/${id}`, JSON.stringify(params), {
          headers: {
            'Content-Type': 'application/json; charset=utf-8',
            'Access-Control-Allow-Origin': '*',
          },
        })
        .then((res) => res)
        .then((data) => {
          if (data.data === '') {
            alert('???????????? ?????? ??????');
          } else {
            alert('???????????? ?????? ??????');
            commit('delete')
          }
        })
        .catch((e) => {
          console.log(e);
          alert('???????????? ????????? ????????? ??????')
        })
    },
    checkAccount({ commit }, { id, phone }) {
      const params = {
        id: id,
        phone: phone,
      }
      console.log("checking account with id:" + " " + id);
      return axios
        .post(`http://localhost:8000/user/check/${id}/${phone}`, JSON.stringify(params), {
          headers: {
            'Content-Type': 'application/json; charset=utf-8',
            'Access-Control-Allow-Origin': '*',
          },
        })
        .then((res) => res)
        .then((data) => {
          console.log(data);
          if (data.data === '') {
            alert('???????????? ?????? ????????? ????????????.');
          } else {
            alert('???????????? ????????? ???????????? ???????????????.');
            commit('SET_RESET_ID', id);
          }
        })
        .catch((e) => {
          console.log(e);
          alert('??????????????? ???????????? ????????? ??????????????? ???????????????.')
        })
    },
    doReset({ commit }, { id, password }) {
      const params = {
        id: id,
        password: password,
      }
      console.log("resetting password of account id: " + " " + id);
      return axios
        .put(`http://localhost:8000/user/resetPw/${id}/${password}`, JSON.stringify(params), {
          headers: {
            'Content-Type': 'application/json; charset=utf-8',
            'Access-Control-Allow-Origin': '*',
          },
        })
        .then((res) => res)
        .then((data) => {
          console.log(data);
          if (data.data === '') {
            alert('???????????? ????????? ??????. ????????? ????????????.');
          } else {
            alert('???????????? ????????? ??????! ????????? ???????????? ???????????????.');
            commit("SET_IS_PASSWORD_CHK_ERROR", false);
            commit('CLEAR_RESET_ID');
          }
        })
        .catch((e) => {
          console.log(e);
          alert('???????????? ???????????? ????????? ??????')
        })
    },
    setPwChkErr({ commit }) {
      commit("SET_IS_PASSWORD_CHK_ERROR", true);
    },
    clearResetId({ commit }) {
      commit("CLEAR_RESET_ID");
    }
  },
};

export default memberStore;
