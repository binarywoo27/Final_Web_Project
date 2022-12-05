<template>
  <div id="map"></div>
</template>

<script>
import { mapState } from "vuex";

const houseStore = "houseStore";

export default {
  name: "HouseMap",
  data() {
    return {
      map: null,
      lat: 33.450701, // 위도
      lng: 126.570667, // 경도
      msg: "", // 인포윈도우에 표시할 내용
      kakaoMarkers: [],
      ps: null,
      areaMarkers: [],
      orders: ["BK9", "MT1", "HP8", "OL7", "CE7", "CS2"],
    };
  },
  computed: {
    ...mapState(houseStore, ["addresses", "selectGeo", "searchButton"]),
  },
  created() {
    // HTML5의 geolocation으로 사용할 수 있는지 확인
    if (!("geolocation" in navigator)) {
      this.lat = 33.450701;
      this.lng = 126.570667;
      this.msg = "geolocation을 사용할수 없어요..";
    }

    // 현재 접속위치 받아오기
    navigator.geolocation.getCurrentPosition((position) => {
      this.lat = position.coords.latitude;
      this.lng = position.coords.longitude;
      this.msg = '<div style="padding:5px;">I' + "'" + "m Here</div>";
    });
  },
  mounted() {
    if (!window.kakao || !window.kakao.maps) {
      const script = document.createElement("script");
      const API_KEY = process.env.VUE_APP_KAKAO_MAP_API_KEY;
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${API_KEY}&libraries=services,clusterer,drawing`;
      /* global kakao */
      script.addEventListener("load", () => {
        kakao.maps.load(this.initMap);
      });
      document.head.appendChild(script);
    } else {
      // console.log("이미 로딩됨: ", window.kakao);
      this.initMap();
    }
  },
  methods: {
    initMap() {
      const container = document.getElementById("map");
      const options = {
        // 좌표 위치 = 카카오. geoLocation으로 값 받아오지 못하면 사용
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 4,
      };
      this.map = new kakao.maps.Map(container, options);

      // 장소 검색 객체를 생성합니다
      this.ps = new kakao.maps.services.Places(this.map);
      // 지도에 idle 이벤트를 등록합니다
      kakao.maps.event.addListener(this.map, "idle", this.searchPlaces);

      // 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
      let mapTypeControl = new kakao.maps.MapTypeControl();

      // 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
      let zoomControl = new kakao.maps.ZoomControl();
      this.map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);
      this.map.addControl(zoomControl, kakao.maps.ControlPosition.BOTTOMLEFT);

      // 지도의 중심 위치를 변경합니다.
      let myLatLng = new kakao.maps.LatLng(this.lat, this.lng);
      this.map.setCenter(myLatLng);
    },

    // 매매정보 검색 start ----------------------------------------------------------------------------------------------------

    // 새로운 마커 표시 전에 이미 존재하던 마커들을 제거하는 함수입니다
    removeMarker() {
      this.kakaoMarkers.forEach((kakaoMarker) => {
        kakaoMarker.setMap(null);
      });

      this.kakaoMarkers = [];
    },

    // 검색 후 아파트 리스트들의 위치를 마커로 표시하는 함수입니다
    addMarker() {
      let imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
      let imageSize = new kakao.maps.Size(20, 25);
      let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
      let geocoder = new kakao.maps.services.Geocoder();
      let map = this.map;
      let kakaoMarkers = this.kakaoMarkers;
      let selectedGeo = this.selectGeo.geo;

      this.addresses.forEach((address) => {
        if (address.address == null) return;

        geocoder.addressSearch(selectedGeo + address.address, function (result, status) {
          // 정상적으로 검색이 완료됐으면
          if (status === kakao.maps.services.Status.OK) {
            let coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            // 마커를 생성합니다
            let kakaoMarker = new kakao.maps.Marker({
              map: map, // 마커를 표시할 지도
              position: coords, // 마커를 표시할 위치
              title: address.name, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
              image: markerImage, // 마커 이미지
            });
            kakaoMarkers.push(kakaoMarker);
          } else {
            console.log("marker geo search error");
          }
        });
      });
    },

    // 매매정보 검색 end ----------------------------------------------------------------------------------------------------

    // map의 중심을 이동시키는 함수입니다
    moveMap() {
      let geocoder = new kakao.maps.services.Geocoder();
      let map = this.map;
      let mapLevel = this.selectGeo.range == "wide" ? 7 : 3;
      geocoder.addressSearch(this.selectGeo.geo, function (result, status) {
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
          let coords = new kakao.maps.LatLng(result[0].y, result[0].x);
          // 지도의 크기와 중심위치를 변경합니다
          map.setLevel(mapLevel);
          map.setCenter(coords);
        }
      });
    },

    // 카테고리 검색 start ----------------------------------------------------------------------------------------------------

    // 카테고리 검색을 요청하는 함수입니다
    searchPlaces() {
      // 지도에 표시되고 있는 마커를 제거합니다
      this.removeAreaMarker();

      if (!this.searchButton) {
        return;
      }

      this.ps.categorySearch(this.searchButton, this.placesSearchCB, { useMapBounds: true });
    },

    // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
    placesSearchCB(data, status, pagination) {
      pagination;
      if (status === kakao.maps.services.Status.OK) {
        // 정상적으로 검색이 완료됐으면 지도에 마커를 표출합니다
        this.displayPlaces(data);
      } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        // 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요
      } else if (status === kakao.maps.services.Status.ERROR) {
        // 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요
      }
    },

    // 지도에 마커를 표출하는 함수입니다
    displayPlaces(places) {
      // 몇번째 카테고리가 선택되어 있는지 얻어옵니다
      // 이 순서는 스프라이트 이미지에서의 위치를 계산하는데 사용됩니다
      let order = null;
      order = this.orders.indexOf(this.searchButton);

      for (let i = 0; i < places.length; i++) {
        // 마커를 생성하고 지도에 표시합니다
        this.addAreaMarker(new kakao.maps.LatLng(places[i].y, places[i].x), order, places[i]);
      }
    },

    // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
    addAreaMarker(position, order, place) {
      let map = this.map;
      let imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png", // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(27, 28), // 마커 이미지의 크기
        imgOptions = {
          spriteSize: new kakao.maps.Size(72, 208), // 스프라이트 이미지의 크기
          spriteOrigin: new kakao.maps.Point(10, order * 36), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
          offset: new kakao.maps.Point(11, 28), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
        marker = new kakao.maps.Marker({
          position: position, // 마커의 위치
          image: markerImage,
          title: place.place_name,
        });

      marker.setMap(map); // 지도 위에 마커를 표출합니다
      this.areaMarkers.push(marker); // 배열에 생성된 마커를 추가합니다

      return marker;
    },

    // 지도 위에 표시되고 있는 마커를 모두 제거합니다
    removeAreaMarker() {
      for (var i = 0; i < this.areaMarkers.length; i++) {
        this.areaMarkers[i].setMap(null);
      }
      this.areaMarkers = [];
    },

    // 카테고리 검색 end ----------------------------------------------------------------------------------------------------
  },

  watch: {
    addresses: function () {
      this.removeMarker();
      this.moveMap();

      if (this.addresses.length > 1) {
        this.addMarker();
      }
    },

    selectGeo: function () {
      this.moveMap();
    },

    searchButton: function () {
      this.searchPlaces();
    },
  },
};
</script>

<style scoped>
#map {
  width: 1400px;
  height: 650px;
}
</style>
