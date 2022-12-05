import axios from "axios";
import { favoritesList, houseList } from "@/api/house.js";

const houseStore = {
  namespaced: true,
  state: {
    sidos: [{ value: null, text: '시 / 도 선택' }],
    guguns: [{ value: null, text: '구 / 군 선택' }],
    dongs: [{ value: null, text: '동 선택' }],
    houses: [],
    addresses: [{ address: null, name: null}],
    selectGeo: { range: null, sidoName: null, gugunName: null, geo: null },
    favorites: [],
    favoSidoCode: null,
    favoGugunCode: null,
    searchButton: null,
  },
  mutations: {
    // 주소 시도 목록을 저장합니다
    SET_SIDO_LIST(state, sidos) {
      sidos.forEach((sido) => {
        state.sidos.push({ value: sido.code, text: sido.name })
      })
    },
    // 주소 구군 목록을 저장합니다
    SET_GUGUN_LIST(state, guguns) {
      guguns.forEach((gugun) => {
        state.guguns.push({
          value: gugun.code,
          text: gugun.name.split(' ')[1],
        })
      })
    },
    // 주소 동 목록을 저장합니다
    SET_DONG_LIST(state, dongs) {
      dongs.forEach((dong) => {
        state.dongs.push({
          value: dong.code,
          text: dong.name.split(' ')[2],
        })
      })
    },
    // 주택 목록을 저장합니다
    SET_HOUSE_LIST(state, houses) {
      state.houses = houses;
    },
    // 마커를 생성하기위한 좌표값을 저장합니다
    SET_ADDRESSES_LIST(state, datas) {
      datas.forEach((data) => {
        state.addresses.push({
          address: data.도로명 + " " + data.도로명건물본번호코드 + " " + data.도로명건물부번호코드,
          name : data.아파트
        })
      })
    },
    // 관심지역 목록을 저장합니다
    SET_FAVORITES_LIST(state, favorites) {
      state.favorites = favorites;
    },
    // 매물 목록에서 선택한 지역값을 반영합니다
    SET_SELECT_GEO(state, data) {
      state.selectGeo = { range: data.range, sidoName: data.sidoName, gugunName: data.gugunName, geo: data.geo };
    },
    // 관심지역창에서 선택한 값을 반영합니다
    SET_FAVO_SIDO_GUGUN(state, data) {
      state.favoSidoCode = data.sidoCode;
      state.favoGugunCode = data.gugunCode;
    },
    // 주변 정보 검색을 위한 버튼값을 저장합니다
    SET_SEARCH_BUTTON(state, data) {
      state.searchButton = data;
    },

    // sidos의 값을 비웁니다
    CLEAR_SIDO_LIST(state) {
      state.sidos = [{ value: null, text: '시 / 도 선택' }]
    },
    // guguns의 값을 비웁니다
    CLEAR_GUGUN_LIST(state) {
      state.guguns = [{ value: null, text: '구 / 군 선택' }]
    },
    // dongs의 값을 비웁니다
    CLEAR_DONG_LIST(state) {
      state.dongs = [{ value: null, text: '동 선택' }]
    },
    // houses와 house의 값을 비웁니다
    CLEAR_HOUSE_LIST(state) {
      state.houses = []
    },
    // markers의 값을 비웁니다
    CLEAR_ADDRESSES_LIST(state) {
      state.addresses = [{ address: null, name: null}]
    },
    CLEAR_FAVORITES_LIST(state) {
      state.favorites = []
    },
    // 주변 정보 검색을 위한 버튼값을 비웁니다
    CLEAR_SEARCH_BUTTON(state) {
      state.searchButton = null;
    },
  },
  actions: {
    // 컴포넌트 실행 시 시/도 목록을 가져오는 함수입니다.
    getSido({ commit }) {
      const url =
        'https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?'
      let params = 'regcode_pattern=*00000000&is_ignore_zero=true'
      axios
        .get(url + params)
        .then(({ data }) => {
          commit('SET_SIDO_LIST', data.regcodes)
        })
        .catch((error) => {
          console.log(error)
        })
    },

    // 선택한 시도가 변하면 구군 목록을 가져오는 함수입니다
    getGugun({ commit }, sidoCode) {
      const url =
        'https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?'
      let regcode = sidoCode
      regcode = regcode.substr(0, 2) + '*00000'
      let params = 'regcode_pattern=' + regcode + '&is_ignore_zero=true'
      axios
        .get(url + params)
        .then(({ data }) => {
          commit('SET_GUGUN_LIST', data.regcodes)
        })
        .catch((error) => {
          console.log(error)
        })
    },

    // 선택한 구/군이 변하면 동 목록을 가져오는 함수입니다
    getDong({ commit }, gugunCode) {
      const url =
        'https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?'
      let regcode = gugunCode
      regcode = regcode.substr(0, 5) + '*'
      let params = 'regcode_pattern=' + regcode + '&is_ignore_zero=true'
      axios
        .get(url + params)
        .then(({ data }) => {
          commit('SET_DONG_LIST', data.regcodes)
        })
        .catch((error) => {
          console.log(error)
        })
    },

    getHouseList({ commit }, info) {
      const SERVICE_KEY = process.env.VUE_APP_APT_DEAL_API_KEY;
      const params = {
        LAWD_CD: info.lawd_cd,
        DEAL_YMD: info.deal_ymd,
        serviceKey: decodeURIComponent(SERVICE_KEY),
      };
      houseList(
        params,
        ({ data }) => {
          commit("SET_HOUSE_LIST", data.response.body.items.item);
          commit("SET_ADDRESSES_LIST", data.response.body.items.item);
        },
        (error) => {
          console.log(error);
        }
      );
      commit("SET_SELECT_GEO", info.selectGeo);
    },

    setSelectGeo({ commit }, geo) {
      commit("SET_SELECT_GEO", geo);
    },

    // 관심지역 목록을 가져오는 함수입니다
    getFavorites({ commit }, id) {
      commit("CLEAR_FAVORITES_LIST");
      const params = {
        id: id
      }
      favoritesList(
        params,
        ({ data }) => {
          commit('SET_FAVORITES_LIST', data)
        },
        (error) => {
          console.log(error);
        }
      )
    },

    // 관심지역 리스트를 클릭하면 그 내용을 저장합니다 
    setFavoSidoGugun({ commit }, data) {
      commit('SET_FAVO_SIDO_GUGUN', data)
    },

    // 클릭한 주변 상권 값을 저장합니다
    setSearchButton({ commit }, data) {
      commit('SET_SEARCH_BUTTON', data)
    }

  },
  modules: {},
};

export default houseStore;
