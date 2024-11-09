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
      <div class="topic-box">Web Development</div>
      <div class="topic-box">Marketing</div>
      <div class="topic-box">Programming</div>
      <div class="topic-box">Finance</div>
      <div class="topic-box">Leadership</div>
      <div class="topic-box">Data Science</div>
      <div class="topic-box">Design</div>
      <div class="topic-box">Hardware</div>
      <div class="topic-box">Economics</div>
      <div class="topic-box">Entrepreneurship</div>
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
      <swiper-slide>
        <div class="course-card">
          <div class="course-image-wrapper">
            <img src="@/assets/tm1.png" alt="Course 1" class="course-image"/>
          </div>
          <div class="course-content">
            <h3>The Complete Web Development Bootcamp</h3>
            <p>Dr. Angela Yu, Developer and Lead Instructor</p>
            <p class="rating">4.7 ⭐ (408,046)</p>
            <p class="price">$16.99 <span class="old-price">$89.99</span></p>
            <button class="enroll-button">
              <router-link to="/signup-student">Enroll Now</router-link>
            </button>
          </div>
        </div>
      </swiper-slide>

      <swiper-slide>
        <div class="course-card">
          <div class="course-image-wrapper">
            <img src="@/assets/tm2.png" alt="Course 2" class="course-image"/>
          </div>
          <div class="course-content">
            <h3>The Web Developer Bootcamp 2024</h3>
            <p>Colt Steele</p>
            <p class="rating">4.7 ⭐ (276,491)</p>
            <p class="price">$27.99 <span class="old-price">$149.99</span></p>
            <button class="enroll-button">
              <router-link to="/signup-student">Enroll Now</router-link>
            </button>
          </div>
        </div>
      </swiper-slide>

      <swiper-slide>
        <div class="course-card">
          <div class="course-image-wrapper">
            <img src="@/assets/tm1.png" alt="Course 3" class="course-image"/>
          </div>
          <div class="course-content">
            <h3>Web Development Masterclass - Online Certification</h3>
            <p>YouAccel Training</p>
            <p class="rating">4.3 ⭐ (9,981)</p>
            <p class="price">$13.99 <span class="old-price">$69.99</span></p>
            <button class="enroll-button">
              <router-link to="/signup-student">Enroll Now</router-link>
            </button>
          </div>
        </div>
      </swiper-slide>

      <swiper-slide>
        <div class="course-card">
          <div class="course-image-wrapper">
            <img src="@/assets/tm3.png" alt="Course 4" class="course-image"/>
          </div>
          <div class="course-content">
            <h3>Practical Web Development: 22 Courses in 1</h3>
            <p>Creative Online School</p>
            <p class="rating">4.2 ⭐ (4,352)</p>
            <p class="price">$13.99 <span class="old-price">$54.99</span></p>
            <button class="enroll-button">
              <router-link to="/signup-student">Enroll Now</router-link>
            </button>
          </div>
        </div>
      </swiper-slide>
      <swiper-slide>
        <div class="course-card">
          <div class="course-image-wrapper">
            <img src="@/assets/tm1.png" alt="Course 3" class="course-image"/>
          </div>
          <div class="course-content">
            <h3>Web Development Masterclass - Online Certification</h3>
            <p>YouAccel Training</p>
            <p class="rating">4.3 ⭐ (9,981)</p>
            <p class="price">$13.99 <span class="old-price">$69.99</span></p>
            <button class="enroll-button">
              <router-link to="/signup-student">Enroll Now</router-link>
            </button>
          </div>
        </div>
      </swiper-slide>
      <swiper-slide>
        <div class="course-card">
          <div class="course-image-wrapper">
            <img src="@/assets/tm2.png" alt="Course 2" class="course-image"/>
          </div>
          <div class="course-content">
            <h3>The Web Developer Bootcamp 2024</h3>
            <p>Colt Steele</p>
            <p class="rating">4.7 ⭐ (276,491)</p>
            <p class="price">$27.99 <span class="old-price">$149.99</span></p>
            <button class="enroll-button">
              <router-link to="/signup-student">Enroll Now</router-link>
            </button>
          </div>
        </div>
      </swiper-slide>

      <swiper-slide>
        <div class="course-card">
          <div class="course-image-wrapper">
            <img src="@/assets/tm1.png" alt="Course 3" class="course-image"/>
          </div>
          <div class="course-content">
            <h3>Web Development Masterclass - Online Certification</h3>
            <p>YouAccel Training</p>
            <p class="rating">4.3 ⭐ (9,981)</p>
            <p class="price">$13.99 <span class="old-price">$69.99</span></p>
            <button class="enroll-button">
              <router-link to="/signup-student">Enroll Now</router-link>
            </button>
          </div>
        </div>
      </swiper-slide>

      <swiper-slide>
        <div class="course-card">
          <div class="course-image-wrapper">
            <img src="@/assets/tm3.png" alt="Course 4" class="course-image"/>
          </div>
          <div class="course-content">
            <h3>Practical Web Development: 22 Courses in 1</h3>
            <p>Creative Online School</p>
            <p class="rating">4.2 ⭐ (4,352)</p>
            <p class="price">$13.99 <span class="old-price">$54.99</span></p>
            <button class="enroll-button">
              <router-link to="/signup-student">Enroll Now</router-link>
            </button>
          </div>
        </div>
      </swiper-slide>
      <div v-if="!atStart" class="swiper-button-prev" @click="goToPrev"></div>
      <div v-if="!atEnd" class="swiper-button-next" @click="goToNext"></div>
    </swiper>
  </div>

  <div class="popular-instructors">
    <h1>Popular Instructors</h1>
    <p>These real-world experts are highly rated by learners like you.</p>
    <div class="instructor-list">
      <div class="instructor-card" v-for="teacher in mockTeachers" :key="teacher.name">
        <div class="profile-picture">
          <img src="@/assets/img_10.png" alt="Instructor Picture">
        </div>
        <div class="instructor-info">
          <h2>{{ teacher.name }}</h2>
          <p>{{ teacher.description }}</p>
          <p class="rating">Rating: {{ teacher.rating }} <img src="@/assets/img_12.png" alt="Star" class="star-icon"></p>
          <p class="student-count">{{ teacher.students }} students</p>
          <p class="course-count">{{ teacher.courses }} courses</p>
        </div>
      </div>
    </div>
  </div>

  <div class="trending-courses">
    <div class="left-part">
      <h1>Trending Now</h1>
      <h2>ChatGPT is a top skill</h2>
      <router-link to="" class="course-link">See ChatGPT courses &gt;</router-link>
      <p class="learner-count">3,705,695 learners</p>
    </div>

    <div class="vertical-divider"></div>

    <div class="category-grid">
      <div class="category" v-for="(category, index) in categories" :key="index">
        <h3>{{ category.name }}</h3>
        <ul>
          <li v-for="course in category.courses" :key="course.name">
            <router-link to="course.link" class="course-name">{{ course.name }} &gt;</router-link>
            <p class="learner-count2">{{ course.learners }} learners</p>
          </li>
        </ul>
      </div>
    </div>

  </div>
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
  {src: require('@/assets/main_1.png'), alt: 'Image 2'},
  {src: require('@/assets/img_4.png'), alt: 'Image 1'},
];

const categories = ref([
  {
    name: "Development",
    courses: [
      {name: "Python", learners: "46,203,544", link: "#"},
      {name: "Web Development", learners: "13,697,450", link: "#"},
      {name: "Data Science", learners: "7,489,675", link: "#"},
    ],
  },
  {
    name: "Design",
    courses: [
      {name: "Blender", learners: "2,778,547", link: "#"},
      {name: "Graphic Design", learners: "4,375,054", link: "#"},
      {name: "User Experience (UX) Design", learners: "2,009,988", link: "#"},
    ],
  },
  {
    name: "Business",
    courses: [
      {name: "PMI Project Management Professional (PMP)", learners: "2,412,480", link: "#"},
      {name: "Microsoft Power BI", learners: "4,340,192", link: "#"},
      {name: "Project Management", learners: "3,752,340", link: "#"},
    ],
  },
]);

const mockTeachers = [
  {
    name: "TIA Education, Andrew Ramdayal",
    rating: 4.7,
    students: "503,883",
    courses: 26,
    description: "PMI Project Management Professional (PMP), PMI…"
  },
  {
    name: "Maven Analytics",
    rating: 4.6,
    students: "1,381,684",
    courses: 43,
    description: "Business Intelligence (BI), Microsoft Power BI"
  },
  {
    name: "TIA Education, Andrew Ramdayal",
    rating: 4.7,
    students: "503,883",
    courses: 26,
    description: "PMI Project Management Professional (PMP), PMI…"
  },
  {
    name: "Maven Analytics",
    rating: 4.6,
    students: "1,381,684",
    courses: 43,
    description: "Business Intelligence (BI), Microsoft Power BI"
  },
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
  width: 300px; /* Adjust width as needed */
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

.price {
  font-size: 18px;
  font-weight: bold;
  color: black;
}

.old-price {
  font-size: 14px;
  color: #888;
  text-decoration: line-through;
  margin-left: 8px;
}

.enroll-button {
  background-color: #74B3E3;
  color: white;
  font-size: 16px;
  font-family: 'Aptos Narrow', sans-serif;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  margin-top: 16px;
  width: 100%;
  transition: background-color 0.3s;
}

.enroll-button:hover {
  background-color: #9DCAEB;
}

.enroll-button a {
  color: inherit;
  text-decoration: none;
  display: inline-block;
  width: 100%;
  height: 100%;
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
  margin-left: 100px;
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
</style>