import axios from "axios";

// local vue api axios instance
function apiInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// reg code get api axios instance
function regInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_REGCODE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// house deal API axios instance
function houseInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_HOUSE_DEAL_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

export { apiInstance, regInstance, houseInstance };
