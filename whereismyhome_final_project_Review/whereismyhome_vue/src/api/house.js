import { apiInstance, regInstance } from "./index.js";

const api = apiInstance();
const reg = regInstance();

function sidoList(params, success, fail) {
  reg.get({ params: params }).then(success).catch(fail);
}

function gugunList(params, success, fail) {
  api.get(`/map/gugun`, { params: params }).then(success).catch(fail);
}

function houseList(params, success, fail) {
  api.get(`/house`, { params: params }).then(success).catch(fail);
}

function houseHit(params, success, fail) {
  api.put(`/house`, JSON.stringify(params)).then(success).catch(fail);
}

function getHouseRank(success, fail) {
  api.get(`/house/rank`).then(success).catch(fail);
}

function favoritesList(params, success, fail) {
  api.get(`/favorite`, { params: params }).then(success).catch(fail);
}

function favoritesAdd(params, success, fail) {
  api.post(`/favorite`, JSON.stringify(params)).then(success).catch(fail);
}

function favoritesDel(params, success, fail) {
  api.delete(`/favorite`, { params: params }).then(success).catch(fail);
}

export { sidoList, gugunList, houseList, houseHit, favoritesList, favoritesAdd, favoritesDel, getHouseRank };
