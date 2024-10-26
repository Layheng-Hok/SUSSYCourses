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
            src="@/assets/logo.png"
            alt="Element logo"
        /></router-link>
    </el-menu-item>
    <el-menu-item index="1">
      <router-link to="/teacherpage">Teach on SUSSY</router-link>
    </el-menu-item>

    <el-menu-item index="2">
      <router-link to="/studentpage">Study on SUSSY</router-link>
    </el-menu-item>

    <el-menu-item index="3">
      <router-link to="/login">Log in</router-link>
    </el-menu-item>
    <el-menu-item index="4">
      <router-link to="/signup-student">Sign up</router-link>
    </el-menu-item>

  </el-menu>

  <div class="block text-center carousel-container">
    <el-carousel
        motion-blur
        :interval="5000"
        arrow="always"
        class="custom-carousel">
      <el-carousel-item v-for="(img, index) in images" :key="index">
        <img :src="img.src" :alt="img.alt" class="carousel-image"/>
      </el-carousel-item>
    </el-carousel>
  </div>

  <div class="intro-section">
    <h1>Unlock Your Future</h1>
    <p>Learn new skills to elevate your career and achieve personal growth. Explore courses and find your passion
      today.</p>
  </div>

  <swiper
      :modules="[Navigation]"
      class="mySwiper"
      @swiper="handleSwiper"
  >
    <swiper-slide>
      <div class="course-card">
        <img src="@/assets/tm1.png" alt="Course 1" class="course-image"/>
        <h3>The Complete Web Development Bootcamp</h3>
        <p>Dr. Angela Yu, Developer and Lead Instructor</p>
        <p class="rating">4.7 ⭐ (408,046)</p>
        <p class="price">$16.99 <span class="old-price">$89.99</span></p>
        <button class="enroll-button">Enroll Now</button>
      </div>
    </swiper-slide>

    <swiper-slide>
      <div class="course-card">
        <img src="@/assets/tm2.png" alt="Course 2" class="course-image"/>
        <h3>The Web Developer Bootcamp 2024</h3>
        <p>Colt Steele</p>
        <p class="rating">4.7 ⭐ (276,491)</p>
        <p class="price">$27.99 <span class="old-price">$149.99</span></p>
        <button class="enroll-button">Enroll Now</button>
      </div>
    </swiper-slide>

    <swiper-slide>
      <div class="course-card">
        <img src="@/assets/tm1.png" alt="Course 3" class="course-image"/>
        <h3>Web Development Masterclass - Online Certification</h3>
        <p>YouAccel Training</p>
        <p class="rating">4.3 ⭐ (9,981)</p>
        <p class="price">$13.99 <span class="old-price">$69.99</span></p>
        <button class="enroll-button">Enroll Now</button>
      </div>
    </swiper-slide>

    <swiper-slide>
      <div class="course-card">
        <img src="@/assets/tm3.png" alt="Course 4" class="course-image"/>
        <h3>Practical Web Development: 22 Courses in 1</h3>
        <p>Creative Online School</p>
        <p class="rating">4.2 ⭐ (4,352)</p>
        <p class="price">$13.99 <span class="old-price">$54.99</span></p>
        <button class="enroll-button">Enroll Now</button>
      </div>
    </swiper-slide>
    <div v-if="!atStart" class="swiper-button-prev" @click="goToPrev"></div>
    <div v-if="!atEnd" class="swiper-button-next" @click="goToNext"></div>
  </swiper>
</template>

<script setup>
import {ref} from 'vue';
import {Swiper, SwiperSlide} from 'swiper/vue';
import {Navigation} from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';

const atStart = ref(true);  // Tracks if at the first slide
const atEnd = ref(false);   // Tracks if at the last slide
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
  // Add routing logic or other actions here based on the selected index
};

const images = [
  {src: require('@/assets/img.png'), alt: 'Image 1'},
  {src: require('@/assets/img_1.png'), alt: 'Image 2'},
];
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
  border-radius: 8px;
  overflow: hidden;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.el-carousel__item {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent;
}

.intro-section {
  text-align: left;
  margin: 50px auto 50px 100px;
  max-width: 800px;
}

.intro-section h1 {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 10px !important;
  margin-top: 90px;
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
}

.intro-section p {
  font-size: 18px;
  color: black;
  line-height: 1.6;
  margin-top: 0 !important;
  margin-bottom: 80px;
}

.el-menu-item:nth-child(4) a {
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

.el-menu-item:nth-child(4) a:hover {
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
}

.el-menu-item:nth-child(5) a {
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

.el-menu-item:nth-child(5) a:hover {
  background-color: #9DCAEB;
  border: 1px solid #9DCAEB;
  color: white;
}

.el-menu-item:nth-child(4) {
  margin-right: -25px; /* Adjust the space between 'Log in' and 'Sign up' */
}

.el-menu-item:nth-child(5) {
  margin-left: 0; /* Adjust the space between 'Log in' and 'Sign up' */
}


.mySwiper {
  height: 370px;
  margin-top: 100px;
  margin-right: -9px;
  margin-left: -9px;
}

.slide-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0;
  background-color: #f8f9fa;
}


.swiper-button-next,
.swiper-button-prev {
  color: white;
  font-weight: bold;
  background-color: #333;
  border-radius: 50%; /* Make it circular */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 50px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.swiper-button-next:hover,
.swiper-button-prev:hover {
  background-color: #444;
  transform: scale(1.1);
}

.swiper-button-next {
  right: 280px;
}

.swiper-button-prev {
  left: 300px;
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

</style>