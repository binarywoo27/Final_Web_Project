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
            console.log("유저 정보 없음");
          }
        },
        async (error) => {
          console.log("getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ", error.response.status);
          commit("SET_IS_VALID_TOKEN", false);
          await dispatch("tokenRegeneration");
        }
      );
    },
    async tokenRegeneration({ commit, state }) {
      // console.log("토큰 재발급 >> 기존 토큰 정보 : {}", sessionStorage.getItem("access-token"));
      await tokenRegeneration(
        JSON.stringify(state.userInfo),
        ({ data }) => {
          if (data.message === "success") {
            let accessToken = data["access-token"];
            // console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
            sessionStorage.setItem("access-token", accessToken);
            commit("SET_IS_VALID_TOKEN", true);
          }
        },
        async (error) => {
          // HttpStatus.UNAUTHORIZE(401) : RefreshToken 기간 만료 >> 다시 로그인!!!!
          if (error.response.status === 401) {
            console.log("갱신 실패");
            // 다시 로그인 전 DB에 저장된 RefreshToken 제거.
            await logout(
              state.userInfo.userid,
              ({ data }) => {
                if (data.message === "success") {
                  console.log("리프레시 토큰 제거 성공");
                } else {
                  console.log("리프레시 토큰 제거 실패");
                }
                alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
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
            console.log("유저 정보 없음");
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
            alert('회원가입 실패');
          } else {
            alert('회원가입 성공');
            commit('regist')
          }
        })
        .catch((e) => {
          console.log(e);
          alert('회원가입 과정에 문제가 발생');
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
            alert('회원정보 수정 실패');
          } else {
            alert('회원정보 수정 완료');
            commit('update')
          }
        })
        .catch((e) => {
          console.log(e);
          alert('회원정보 수정에 문제가 발생');
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
            alert('회원정보 삭제 실패');
          } else {
            alert('회원정보 삭제 완료');
            commit('delete')
          }
        })
        .catch((e) => {
          console.log(e);
          alert('회원정보 삭제에 문제가 발생')
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
            alert('해당하는 회원 정보가 없습니다.');
          } else {
            alert('비밀번호 재설정 화면으로 이동합니다.');
            commit('SET_RESET_ID', id);
          }
        })
        .catch((e) => {
          console.log(e);
          alert('회원정보가 존재하지 않거나 비정상적인 접근입니다.')
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
            alert('비밀번호 재설정 실패. 재시도 바랍니다.');
          } else {
            alert('비밀번호 재설정 성공! 로그인 화면으로 이동합니다.');
            commit("SET_IS_PASSWORD_CHK_ERROR", false);
            commit('CLEAR_RESET_ID');
          }
        })
        .catch((e) => {
          console.log(e);
          alert('비밀번호 재설정에 문제가 발생')
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
