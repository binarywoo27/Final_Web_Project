<template>
  <b-row class="mt-4 mb-4" align-content="between">
    <b-col>
      <b-form-select v-model="sidoCode" :options="sidos" @change="gugunList"></b-form-select>
    </b-col>
    <b-col>
      <b-form-select v-model="gugunCode" :options="guguns"></b-form-select>
    </b-col>
    <!-- <b-col >
        <b-form-select v-model="dongCode" :options="dongs"></b-form-select>
      </b-col> -->

    <b-col>
      <b-form-select v-model="nowYear" :options="years"></b-form-select>
    </b-col>
    <b-col>
      <b-form-select v-model="nowMonth" :options="months"></b-form-select>
    </b-col>

    <b-col v-if="userInfo" @click="toggleFavorite">
      <b-button v-if="mapExist" variant="white">
        <b-icon icon="pin-map-fill" variant="warning" shift-h="-4" />관심지역 삭제
      </b-button>
      <b-button v-else variant="white">
        <b-icon icon="pin-map" variant="warning" shift-h="-4" />관심지역 추가
      </b-button>
    </b-col>
    <b-col v-if="userInfo">
      <b-button v-b-modal.modal-1 variant="white">
        <b-icon icon="map" variant="warning" shift-h="-2" /> 관심지역 목록
      </b-button>
      <house-favorite />
    </b-col>

      <b-col class="sm-3" align="left">
        <b-button variant="outline-dark" @click="searchApt"
          ><b-icon icon="search" shift-h="-2" /> 매매정보 찾기</b-button
        >
      </b-col>
    </b-row>
</template>

<script>
import { mapState, mapActions, mapMutations } from "vuex";
import HouseFavorite from "@/components/house/HouseFavorite.vue";
import { favoritesAdd, favoritesDel } from "@/api/house.js";

const houseStore = "houseStore";
const memberStore = "memberStore";

export default {
  name: "HouseSearchBar",
  components: {
    HouseFavorite,
  },
  data() {
    return {
      sidoCode: null,
      gugunCode: null,
      dongCode: null,
      nowYear: null,
      nowMonth: null,
      years: [{ value: null, text: `년도 선택` }],
      months: [{ value: null, text: `월 선택` }],
      showDismissibleAlert: false,
      alertText: null,
      alertColor: "danger",
      mapExist: false,
    };
  },
  computed: {
    ...mapState(houseStore, ["sidos", "guguns", "dongs", "houses", "favorites", "favoSidoCode", "favoGugunCode"]),
    ...mapState(memberStore, ["userInfo"]),
    selectedSido: function () {
      let report = this.sidos.find((option) => option.value === this.sidoCode);
      return report.text;
    },
    selectedGugun: function () {
      let report = this.guguns.find((option) => option.value === this.gugunCode);
      return report.text;
    },
  },
  created() {
    this.CLEAR_SIDO_LIST();
    this.CLEAR_HOUSE_LIST();
    this.getSido();
    this.getYear();
    this.getMonth();
    if (this.userInfo != null && this.userInfo.id != null) {
      // console.log(this.userInfo.id);
      this.getFavorites(this.userInfo.id);
    }
  },
  methods: {
    ...mapActions(houseStore, ["getSido", "getGugun", "getDong", "getHouseList", "getFavorites", "setFavorite"]),
    ...mapMutations(houseStore, [
      "CLEAR_SIDO_LIST",
      "CLEAR_GUGUN_LIST",
      "CLEAR_DONG_LIST",
      "CLEAR_HOUSE_LIST",
      "CLEAR_ADDRESSES_LIST",
    ]),

    getYear() {
      let date = new Date();
      let year = date.getFullYear();
      for (let i = year; i > year - 20; i--) {
        this.years.push({ value: i, text: `${i}년` });
      }
      // console.log(this.years)
    },

    getMonth() {
      for (let i = 1; i <= 12; i++) {
        this.months.push({ value: i, text: `${i}월` });
      }
      // console.log(this.years)
    },

    async gugunList() {
      this.CLEAR_GUGUN_LIST();
      this.CLEAR_DONG_LIST();
      this.gugunCode = null;
      this.dongCode = null;
      if (this.sidoCode) await this.getGugun(this.sidoCode);
    },

    dongList() {
      // console.log(event.target.value)
      this.CLEAR_DONG_LIST();
      this.dongCode = null;
      if (this.gugunCode) this.getDong(this.gugunCode);
    },

    searchApt() {
      this.CLEAR_HOUSE_LIST();
      this.CLEAR_ADDRESSES_LIST();
      // console.log(this.sidoCode);
      // console.log(this.gugunCode);
      // console.log(this.userInfo);
      if (this.gugunCode && this.nowYear && this.nowMonth) {
        let info = {
          lawd_cd: String(this.gugunCode).substring(0, 5),
          deal_ymd: this.nowYear + String(this.nowMonth).padStart(2, "0"),
          selectGeo: {
            range: "wide",
            sidoName: this.selectedSido,
            gugunName: this.selectedGugun,
            geo: this.selectedSido + " " + this.selectedGugun,
          },
        };
        this.getHouseList(info);
      } else {
        this.$bvToast.toast("선택지를 모두 설정해주세요", {
          title: "지역선택",
          variant: "danger",
          autoHideDelay: 1500,
          solid: true,
        });
      }
    },

    toggleFavorite() {
      // 관심지역에 있는 경우 => 삭제
      if (this.mapExist) {
        this.deleteFavorite();
      } else {
        this.addFavorite();
      }
    },

    addFavorite() {
      for (let index = 0; index < this.favorites.length; index++) {
        if (this.favorites[index].gugunCode == this.gugunCode) {
          this.$bvToast.toast("이미 관심지역에 있습니다", {
            title: "관심지역",
            variant: "warning",
            autoHideDelay: 1500,
            solid: true,
          });
        }
      }

      if (this.sidoCode == null || this.gugunCode == null) {
        this.$bvToast.toast("지역 설정을 완료해주세요", {
          title: "관심지역",
          variant: "warning",
          autoHideDelay: 1500,
          solid: true,
        });
        return;
      } else {
        const params = {
          sidoCode: this.sidoCode,
          gugunCode: this.gugunCode,
          id: this.userInfo.id,
          sido: this.selectedSido,
          gugun: this.selectedGugun,
        };

        favoritesAdd(
          params,
          ({ data }) => {
            data;
            // console.log(data);
            this.getFavorites(this.userInfo.id);
            this.mapExist = true;
            this.$bvToast.toast("관심지역이 추가됐습니다", {
              title: "관심지역",
              variant: "warning",
              autoHideDelay: 1500,
              solid: true,
            });
          },
          (error) => {
            console.log(error);
          }
        );
      }
    },

    deleteFavorite() {
      const params = {
        gugunCode: this.gugunCode,
        id: this.userInfo.id,
      };
      // console.log(params);

      favoritesDel(
        params,
        ({ data }) => {
          // console.log(data);
          data;
          this.mapExist = false;
          this.getFavorites(this.userInfo.id);
          this.$bvToast.toast("관심지역이 삭제됐습니다", {
            title: "관심지역",
            variant: "warning",
            autoHideDelay: 1500,
            solid: true,
          });
        },
        (error) => {
          console.log(error);
        }
      );
    },

    checkFavorite() {
      this.mapExist = false;
      if (this.favorites && this.favorites.length > 0) {
        this.favorites.forEach((favorite) => {
          if (favorite.gugunCode == this.gugunCode) {
            this.mapExist = true;
            return;
          }
        });
      }
    },
  },

  watch: {
    favoGugunCode: function () {
      this.sidoCode = this.favoSidoCode;
      this.gugunList();
      this.gugunCode = this.favoGugunCode;
    },

    gugunCode: function () {
      this.checkFavorite();
    },

    favorites: function () {
      this.checkFavorite();
    },
  },
};
</script>
