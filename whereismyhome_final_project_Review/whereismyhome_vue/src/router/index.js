import Vue from "vue";
import VueRouter from "vue-router";
import AppMain from "@/views/AppMain";
import AppHouse from "@/views/AppHouse";

import store from "@/store";

Vue.use(VueRouter);

// https://v3.router.vuejs.org/kr/guide/advanced/navigation-guards.html
// const checkUserInfo=null;
const onlyAuthUser = async (to, from, next) => {
  const checkUserInfo = store.getters["memberStore/checkUserInfo"];
  const checkToken = store.getters["memberStore/checkToken"];
  let token = sessionStorage.getItem("access-token");
  // console.log("로그인 처리 전", checkUserInfo, token);

  if (checkUserInfo != null && token) {
    // console.log("토큰 유효성 체크하러 가자!!!!");
    await store.dispatch("memberStore/getUserInfo", token);
  }
  if (!checkToken || checkUserInfo === null) {
    alert("로그인이 필요한 페이지입니다..");
    // next({ name: "login" });
    router.push({ name: "login" });
  } else {
    // console.log("로그인 했다!!!!!!!!!!!!!.");
    next();
  }
};

// const onlyAdmin = function() {
//   if(store)
// };


const routes = [
  {
    path: "/",
    name: "main",
    component: AppMain,
  },
  {
    path: "/house",
    name: "house",
    component: AppHouse,
  },
  {
    path: "/user",
    name: "user",
    component: () => import("@/views/AppUser"),
    children: [
      {
        path: "join",
        name: "join",
        component: () => import("@/components/user/UserRegister"),
      },
      {
        path: "login",
        name: "login",
        component: () => import("@/components/user/UserLogin"),
      },
      {
        path: "mypage",
        name: "mypage",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/user/UserMyPage"),
      },
      {
        path: "update",
        name: "update",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/user/UserInfoChange"),
      },
      {
        path: "findpw",
        name: "findpw",
        component: () => import("@/components/user/FindPw"),
      },
      {
        path: "resetpw",
        name: "resetpw",
        component: () => import("@/components/user/ResetPw"),
      },
    ],
  },
  {
    path: "/board",
    name: "board",
    component: () => import("@/views/AppBoard"),
    redirect: "/board/list",
    children: [
      {
        path: "list",
        name: "boardlist",
        component: () => import("@/components/board/BoardList"),
      },
      {
        path: "write",
        name: "boardwrite",
        beforeEnter: onlyAuthUser,
        // meta: {
        //   roles : ["admin"]
        // },
        component: () => import("@/components/board/BoardWrite"),
      },
      {
        path: "view/:articleno",
        name: "boardview",
        // beforeEnter: onlyAuthUser,
        component: () => import("@/components/board/BoardView"),
      },
      {
        path: "modify",
        name: "boardmodify",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/board/BoardModify"),
      },
      {
        path: "delete/:articleno",
        name: "boarddelete",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/board/BoardDelete"),
      },
    ],
  },
  {
    path: "/qna",
    name: "qna",
    component: () => import("@/views/AppBoard"),
    redirect: "/qna/list",
    children: [
      {
        path: "list",
        name: "qnalist",
        component: () => import("@/components/qna/QnaList"),
      },
      {
        path: "write",
        name: "qnawrite",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/qna/QnaWrite"),
      },
      {
        path: "view/:articleno",
        name: "qnaview",
        // beforeEnter: onlyAuthUser,
        component: () => import("@/components/qna/QnaView"),
      },
      {
        path: "modify",
        name: "qnamodify",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/qna/QnaModify"),
      },
      {
        path: "delete/:articleno",
        name: "qnadelete",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/qna/QnaDelete"),
      },
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
