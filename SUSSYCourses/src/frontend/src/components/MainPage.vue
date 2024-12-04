<template>
  <el-menu
      class="el-menu-demo"
      mode="horizontal"
      :ellipsis="false"
      @select="handleSelect"
  >
    <el-menu-item index="0">
      <router-link to="/">
        <img
            src="@/assets/logo2.png"
            alt="Element logo"
        /></router-link>
    </el-menu-item>
    <el-menu-item index="1">
      <router-link to="/teacherpage">Teach on SUSSY</router-link>
    </el-menu-item>

    <el-menu-item index="2">
      <router-link to="/login">Log in</router-link>
    </el-menu-item>
    <el-menu-item index="3">
      <router-link to="/signup-student">Sign up</router-link>
    </el-menu-item>

  </el-menu>

  <div class="block text-center carousel-container">
    <el-carousel
        motion-blur
        :interval="5000"
        arrow="never"
        class="custom-carousel">
      <el-carousel-item v-for="(img, index) in images" :key="index">
        <img :src="img.src" :alt="img.alt" class="carousel-image"/>
        <div v-if="index === 0" class="carousel-box">
          <h2>Empower Your Future</h2>
          <p>Gain the skills to achieve your dreams. Start learning today and unlock endless possibilities.</p>
          <router-link to="/signup-student" class="get-started-button">Get Started</router-link>
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>

  <div class="popular-topics">
    <h1>Popular topics</h1>
    <div class="topics-grid">
      <div
          class="topic-box"
          v-for="topic in popularTopics"
          :key="topic"
          @click="goToCourseList(topic)"
      >
        {{ topic }}
      </div>
    </div>
  </div>

  <div class="top-courses">
    <h1>Top-Rated Courses</h1>
  </div>
  <div class="swiper-wrapper">
    <swiper
        :modules="[Navigation]"
        class="mySwiper"
        @swiper="handleSwiper"
        :slides-per-view="4"
        :slides-per-group="4"
    >
      <swiper-slide v-for="course in courses" :key="course.courseId">
        <div class="course-card">
          <div class="course-image-wrapper">
            <img :src="course.coverImageUrl" :alt="course.courseName" class="course-image"/>
          </div>
          <div class="course-content">
            <h3>{{ course.courseName }}</h3>
            <p>{{ course.teacherName }}</p>
            <p class="rating"><strong>Topic: </strong>{{ course.topic }}</p>
            <p class="rating"><strong>Rating:</strong> {{ course.averageRating }} <img src="@/assets/img_12.png"
                                                                                       alt="Star" class="star-icon">({{
                course.numEvaluations
              }})</p>
            <p class="rating"><strong>Type: </strong>{{ course.type }}</p>
          </div>
        </div>
      </swiper-slide>

      <div v-if="!atStart" class="swiper-button-prev" @click="goToPrev"></div>
      <div v-if="!atEnd" class="swiper-button-next" @click="goToNext"></div>
    </swiper>
  </div>
  <div class="enroll">
    <router-link to="/course-list">
      <button>
        Enroll Now
      </button>
    </router-link>
  </div>
  <div class="popular-instructors">
    <h1>Popular Instructors</h1>
    <p>These real-world experts are highly rated by learners like you.</p>
    <div class="instructor-list">
      <div class="instructor-card" v-for="teacher in teachers" :key="teacher.userId">
        <div class="profile-picture">
          <img :src="teacher.profileImageUrl" alt="Instructor Picture">
        </div>
        <div class="instructor-info">
          <h2>{{ teacher.fullName }}</h2>
          <p>{{ getUniqueTopics(teacher.courses) }}</p>
          <p class="rating"><strong>Rating:</strong> {{ teacher.averageRating }} <img src="@/assets/img_12.png"
                                                                                      alt="Star" class="star-icon">
          </p>
          <p class="student-count"><strong>{{ teacher.totalStudents }}</strong> students</p>
          <p class="course-count"><strong>{{ teacher.numCourses }}</strong> courses</p>
        </div>
      </div>
    </div>
  </div>

  <div class="trending-courses">
    <div class="left-part">
      <h1>Trending Now</h1>
      <h2>Chinese is a top skill</h2>
      <router-link :to="{ path: '/course-list', query: { searchQuery: 'Chinese' } }" class="course-link">See Chinese
        courses &gt;
      </router-link>
      <p class="learner-count"> 30 learners</p>
    </div>
    <div class="vertical-divider"></div>
    <div class="category-grid">
      <div class="category" v-for="(category, index) in categories" :key="index">
        <h3>{{ category.name }}</h3>
        <ul>
          <li v-for="course in category.courses" :key="course.id">
            <router-link :to="course.link" class="course-name">{{ course.name }} &gt;</router-link>
            <p class="learner-count2">{{ course.learners }} learners</p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {Swiper, SwiperSlide} from 'swiper/vue';
import axiosInstances from "@/services/axiosInstance";
import {Navigation} from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';
import {useRouter} from 'vue-router';

const router = useRouter();

const popularTopics = [
  "Programming",
  "Hardware",
  "Math",
  "Science",
  "Languages",
];

const goToCourseList = (topic) => {
  router.push({name: 'CourseList', query: {searchQuery: topic}});
};

const atStart = ref(true);
const atEnd = ref(false);
const swiperInstance = ref(null);

const handleSwiper = (swiper) => {
  swiperInstance.value = swiper;
  swiper.on('slideChange', () => {
    atStart.value = swiper.isBeginning;
    atEnd.value = swiper.isEnd;
  });
};

const goToPrev = () => {
  swiperInstance.value?.slidePrev();
};

const goToNext = () => {
  swiperInstance.value?.slideNext();
};

const handleSelect = (index) => {
  console.log(`Selected menu item index: ${index}`);
};

const teachers = ref([]);

onMounted(() => {
  fetchInstructors();
});

const fetchInstructors = async () => {
  try {
    const response = await axiosInstances.axiosInstance2.get('/courses/instructors/top-rated');
    teachers.value = response.data;
  } catch (error) {
    console.error('Error fetching instructors:', error);
  }
};

const getUniqueTopics = (courses) => {
  const topics = courses.map((course) => course.topic);
  const uniqueTopics = [...new Set(topics)];
  return uniqueTopics.join(', ');
};

const courses = ref([]);

onMounted(async () => {
  try {
    const response = await axiosInstances.axiosInstance2.get('/courses/top-rated');
    courses.value = response.data;
  } catch (error) {
    console.error('Error fetching courses:', error);
  }
});

const images = [
  {src: require('@/assets/main_1.png'), alt: 'Image 2'},
  {src: require('@/assets/img_4.png'), alt: 'Image 1'},
];

const categories = ref([
  {
    name: "Programming",
    courses: [
      {id: 1, name: "C/C++ System Design", learners: "20", link: "/public-course/1"},
      {id: 5, name: "Data Structure and Algorithm Analysis", learners: "5", link: "/public-course/5"},
      {id: 7, name: "Introduction to Java Programming", learners: "10", link: "/public-course/7"},
    ],
  },
  {
    name: "Science",
    courses: [
      {id: 13, name: "Principles of Physics", learners: "10", link: "/public-course/13"},
      {id: 14, name: "Introduction to Chemistry", learners: "5", link: "/public-course/14"},
      {id: 15, name: "Basic Life Science", learners: "7", link: "/public-course/15"},
    ],
  },
  {
    name: "Hardware",
    courses: [
      {id: 9, name: "Digital Logic", learners: "12", link: "/public-course/9"},
      {id: 10, name: "Computer Organization", learners: "4", link: "/public-course/10"},
    ],
  },
]);
</script>

<style scoped>
.el-menu-demo {
  background-color: white;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  height: 75px;
  padding: 0 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.el-menu-demo img {
  width: 60px;
  height: auto;
  object-fit: contain;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

.el-menu-demo .el-menu-item {
  font-size: 18px;
  color: black !important;
  background-color: transparent !important;
  transition: color 0.3s;
  font-family: 'Aptos Narrow', sans-serif;
}

.el-menu-demo .el-menu-item:not(:first-child):hover {
  color: #74B3E3 !important;
}

.el-menu-item.is-active {
  background-color: transparent !important;
  border-bottom: none !important;
}

.el-menu-demo .el-menu-item a {
  text-decoration: none !important;
}

.carousel-container {
  margin-top: 75px;
  width: 100%; /* Make the carousel span full width */
  display: flex;
  justify-content: center;
}

.custom-carousel {
  width: 90%;
  overflow: hidden;
  height: 500px;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.el-carousel__item {
  height: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent;
}

.el-menu-item:nth-child(3) a {
  border: 1px solid black;
  padding: 5px 16px;
  color: black;
  height: 30px;
  line-height: 30px;
  font-weight: 550;
  background-color: white;
  transition: background-color 0.3s, color 0.3s;
  font-size: 16px;
  font-family: 'Aptos Narrow', sans-serif;
}

.el-menu-item:nth-child(3) a:hover {
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
}

.el-menu-item:nth-child(4) a {
  border: 1px solid #74B3E3;
  background-color: #74B3E3;
  color: white;
  padding: 5px 10px;
  height: 30px;
  line-height: 30px;
  font-weight: bold;
  transition: background-color 0.3s, color 0.3s;
  font-size: 16px;
  font-family: 'Aptos Narrow', sans-serif;
}

.el-menu-item:nth-child(4) a:hover {
  background-color: #9DCAEB;
  border: 1px solid #9DCAEB;
  color: white;
}

.el-menu-item:nth-child(3) {
  margin-right: -25px; /* Adjust the space between 'Log in' and 'Sign up' */
}

.el-menu-item:nth-child(4) {
  margin-left: 0; /* Adjust the space between 'Log in' and 'Sign up' */
}

.swiper-button-next,
.swiper-button-prev {
  color: white;
  font-weight: bold;
  background-color: #333;
  border-radius: 50%; /* Make it circular */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 45px;
  height: 45px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.swiper-button-next:hover,
.swiper-button-prev:hover {
  background-color: #444;
}

.swiper-button-next {
  right: 10px;
}

.swiper-button-prev {
  left: 10px;
}

.swiper-button-next,
.swiper-button-prev {
  z-index: 10;
}

/* Change arrow icon size */
.swiper-button-next::after,
.swiper-button-prev::after {
  font-size: 20px;
}

.top-courses h1 {
  font-weight: bold;
  font-family: 'Aptos Narrow', sans-serif;
  color: black;
  margin-top: 80px;
  text-align: left;
  margin-left: 80px;
}

.course-card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 300px;
  min-height: 350px;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 0;
  cursor: pointer;
  text-align: left;
  transition: transform 0.2s ease;
  position: relative;
}

.course-content {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  justify-content: space-between;
  padding: 12px;
}

.course-card:hover {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

.course-card h3 {
  font-size: 18px;
  margin: 0 0 6px;
  font-weight: bold;
  color: black;
}

.course-card p {
  color: black;
  font-size: 14px;
  margin: 4px 0;
}

.course-image-wrapper {
  position: relative;
  width: 100%;
  height: 170px;
  overflow: hidden;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

.course-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.2s ease;
}

.course-image-wrapper::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3); /* Black transparent overlay */
  opacity: 0;
  transition: opacity 0.2s ease;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

.course-card:hover .course-image-wrapper::before {
  opacity: 1;
}

.swiper-slide {
  display: flex;
  overflow: hidden;
}

.mySwiper {
  width: 100%;
  max-width: 1400px;
  overflow: hidden;
}

.trending-courses {
  padding: 20px;
  display: flex;
  margin-top: 100px;
  background-color: #f8f9fa;
  text-align: left;
  margin-left: -9px;
  margin-right: -9px;
}

.left-part h1 {
  font-size: 45px;
  color: black;
  margin: 40px -50px 66px 50px; /*top right bottom left*/
  /*font-family: 'Aptos Narrow', sans-serif;*/
  font-family: Garamond;
}

.left-part h2 {
  margin-left: 40px;
  font-size: 33px;
  font-weight: bold;
  width: 350px;
  color: black;
  margin-bottom: 10px;
  font-family: 'Aptos Narrow', sans-serif;
}

.vertical-divider {
  width: 2px;
  background-color: #ddd;
  height: auto;
  margin-left: 70px;
}

.course-link {
  font-size: 16px;
  color: #006DB5;
  text-decoration: none;
  margin-left: 40px;
  display: inline-block;
}

.learner-count {
  margin-top: 10px;
  font-size: 14px;
  margin-left: 40px;
  color: black;
}

.learner-count2 {
  font-size: 14px;
  color: black;
}

.category-grid {
  display: flex;
  gap: 80px;
  margin-left: 120px;
  margin-top: 40px;
}

.category {
  flex: 1;
}

.category h3 {
  font-size: 20px;
  font-weight: bold;
  color: black;
  margin-bottom: 10px;
  font-family: 'Aptos Narrow', sans-serif;
}

.category ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category li {
  margin-bottom: 15px;
}

.course-name {
  font-size: 16px;
  color: #006DB5;
  text-decoration: none;
}

.carousel-box {
  position: absolute;
  top: 23%;
  left: 10%;
  max-width: 320px;
  padding: 25px;
  background-color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.carousel-box h2 {
  text-align: left;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
  margin-top: 6px;
  font-family: 'Aptos Narrow', sans-serif;
  color: black;
}

.carousel-box p {
  text-align: left;
  font-size: 16px;
  color: black;
  margin-bottom: 35px !important;
}

.get-started-button {
  text-align: center;
  background-color: #333;
  color: white;
  padding: 15px 85px;
  border: none;
  font-size: 16px;
  font-weight: bold;
  font-family: 'Aptos Narrow', sans-serif;
  cursor: pointer;
  transition: background-color 0.3s ease;
  text-decoration: none;
}

.get-started-button:hover {
  background-color: #444;
}

.popular-topics {
  text-align: left;
  margin-left: 80px;
  margin-right: 80px;
}

.popular-topics h1 {
  font-weight: bold;
  font-family: 'Aptos Narrow', sans-serif;
  color: black;
  margin-top: 80px;
}

.topics-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr); /* Set to 5 columns */
  gap: 10px 20px; /* 30px vertical gap, 20px horizontal gap */
}

.topic-box {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px;
  border: 1px solid #ddd;
  font-size: 16px;
  color: black;
  font-weight: bold;
  background-color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.topic-box:hover {
  background-color: #f8f9fa;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

.popular-instructors {
  text-align: left;
  margin: 60px 80px;
}

.popular-instructors h1 {
  font-weight: bold;
  font-family: 'Aptos Narrow', sans-serif;
  color: black;
  margin-bottom: 10px;
}

.popular-instructors p {
  font-size: 18px;
  color: black;
  margin-bottom: 40px;
}

.instructor-list {
  display: flex;
  gap: 20px;
  flex-wrap: nowrap; /* Ensures the cards stay in one line without wrapping */
  justify-content: space-between; /* Distributes space evenly between cards */
  overflow-x: auto; /* Allows horizontal scroll on smaller screens */
}

.instructor-card {
  display: flex;
  align-items: flex-start;
  background: white;
  border: 1px solid #ddd;
  padding: 20px;
  width: 320px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
}

.instructor-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
  background-color: #f8f9fa;
}

.profile-picture {
  flex: 0 0 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
}

.profile-picture img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.instructor-info {
  display: flex;
  flex-direction: column;
}

.instructor-info h2 {
  font-size: 18px;
  font-weight: bold;
  color: black;
  margin: 0;
}

.instructor-info p {
  font-size: 14px;
  color: black;
  margin: 4px 0;
}

.instructor-info .rating {
  font-size: 14px;
  color: black;
}

.instructor-info .student-count,
.instructor-info .course-count {
  font-size: 13px;
  color: black;
}

.star-icon {
  width: 23px;
  height: auto;
  vertical-align: -2px;
  margin-left: -2px;
}

.enroll {
  text-align: left;
  margin-left: 90px;
}

.enroll button {
  padding: 15px 10px;
  font-size: 16px;
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
  font-weight: bold;
  cursor: pointer;
  margin-top: 25px;
}

.enroll button:hover {
  background-color: #9DCAEB;
  border: 1px solid #9DCAEB;
}

</style>