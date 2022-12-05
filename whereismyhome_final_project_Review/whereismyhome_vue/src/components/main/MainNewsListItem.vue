<template>
    <b-row
        class="m-2"
        @click="moveLink"
        @mouseover="colorChange(true)"
        @mouseout="colorChange(false)"
        :class="{ 'mouse-over-bgcolor': isColor }"
    >
        <b-col cols="2" class="text-center align-self-center">
        <p class="h1 mb-2"><b-icon icon="newspaper"></b-icon></p>
        </b-col>
        <b-col cols="10" class="align-self-center"> <span class="title">{{ value.title | charReplace | charReplace }}</span> <br> <span class="pubDate">{{ value.pubDate | dateChange }}</span> </b-col>
    </b-row>
</template>

<script>
export default {
    name: "MainNewsListItem",
    data() {
        return {
            isColor: false,
        }
    },
    props: {
    value: Object,
  },
    filters: {
    /* eslint-disable */
    charReplace: function (str) {
        str = str.replace("&lt;", "\<");
        str = str.replace("&gt;", "\>");
        str = str.replace("<b>", "");
        str = str.replace("</b>", "");
        str = str.replace("&quot;", "\"");
        str = str.replace("&quot;", "\"");
        str = str.replace("&apos;", "\'");
        str = str.replace("&apos;", "\'");
        return str;
    },
    dateChange: function (time) {
        // ['Tue,', '22', 'Nov', '2022', '15:07:00', '+0900']
        let timeArr = time.split(" ");
        // console.log(timeArr);
        if (timeArr[2] == 'Nov') {
            timeArr[2] = 11;
        } else if (timeArr[2] == 'Jan') {
            timeArr[2] = 1;
        } else if (timeArr[2] == 'Feb') {
            timeArr[2] = 2;
        } else if (timeArr[2] == 'Mar') {
            timeArr[2] = 3;
        } else if (timeArr[2] == 'Apr') {
            timeArr[2] = 4;
        } else if (timeArr[2] == 'May') {
            timeArr[2] = 5;
        } else if (timeArr[2] == 'Jun') {
            timeArr[2] = 6;
        } else if (timeArr[2] == 'Jul') {
            timeArr[2] = 7;
        } else if (timeArr[2] == 'Aug') {
            timeArr[2] = 8;
        } else if (timeArr[2] == 'Sep') {
            timeArr[2] = 9;
        } else if (timeArr[2] == 'Oct') {
            timeArr[2] = 10;
        } else if (timeArr[2] == 'Dec') {
            timeArr[2] = 12;
        }

        if (timeArr[0] == 'Tue,') {
            timeArr[0] = "화요일";
        } else if (timeArr[0] == 'Mon,') {
            timeArr[0] = "월요일";
        } else if (timeArr[0] == 'Wed,') {
            timeArr[0] = "수요일";
        } else if (timeArr[0] == 'Thu,') {
            timeArr[0] = "목요일";
        } else if (timeArr[0] == 'Fri,') {
            timeArr[0] = "금요일";
        } else if (timeArr[0] == 'Sat,') {
            timeArr[0] = "토요일";
        } else if (timeArr[0] == 'Sun,') {
            timeArr[0] = "일요일";
        }
        let res = timeArr[3] + "년 " + timeArr[2] + "월 " +timeArr[1] + "일 " +timeArr[0] + " " + timeArr[4];
        return res;
    },
    },
    methods: {
        moveLink() {
            window.open(this.value.link);
        },
        colorChange(flag) {
        this.isColor = flag;
        },
    }
}
</script>

<style scoped>
.mouse-over-bgcolor {
  background-color: lightblue;
}
.title {
    font-weight: bold;
}
.pubDate {
    font-size: 14px;
}
</style>
