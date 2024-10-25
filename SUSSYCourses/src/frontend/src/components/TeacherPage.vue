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
      <router-link to="/studentpage">Study on SUSSY</router-link>
    </el-menu-item>
    <el-menu-item index="2">
      <router-link to="/login">Log in</router-link>
    </el-menu-item>
    <el-menu-item index="3">
      <router-link to="/signup-teacher">Sign up</router-link>
    </el-menu-item>
  </el-menu>

  <div class="main-picture">
    <img src="@/assets/img_2.png" alt="teacher-picture"/>
    <div class="text-box">
      <h1>Come teach with us</h1>
      <p>Become an instructor and change lives — including your own</p>
    </div>
  </div>

  <div class="intro-section">
    <h1>So many reasons to start</h1>
  </div>
  <div class="reasons-container">
    <div class="reason">
      <img src="@/assets/tm1.png" alt="Teach your way icon"/>
      <h3>Teach your way</h3>
      <p>
        Publish the course you want, in the way you want, and always have control of your own content.
      </p>
    </div>
    <div class="reason">
      <img src="@/assets/tm2.png" alt="Inspire learners icon"/>
      <h3>Inspire learners</h3>
      <p>
        Teach what you know and help learners explore their interests, gain new skills, and advance their careers.
      </p>
    </div>
    <div class="reason">
      <img src="@/assets/tm3.png" alt="Get rewarded icon"/>
      <h3>Get rewarded</h3>
      <p>
        Expand your professional network, build your expertise, and earn money on each paid enrollment.
      </p>
    </div>
  </div>

  <div class="impact-bar">
    <div class="bar-content">
      <h1>10,000+</h1>
      <p>Courses Published</p>
    </div>
    <div class="bar-content">
      <h1>50,000+</h1>
      <p>Satisfied Students</p>
    </div>
    <div class="bar-content">
      <h1>1,200+</h1>
      <p>Expert Instructors</p>
    </div>
    <div class="bar-content">
      <h1>98%</h1>
      <p>Positive Feedback</p>
    </div>
  </div>

  <div class="start">
    <h1>How to begin</h1>
  </div>

  <el-tabs v-model="activeTab" class="tabs" @tab-click="handleClick">
    <el-tab-pane label="Create a Course" name="create-course" class="tab-content">
      <div>
        <h3>Create Your Course</h3>
        <p>Plan engaging lessons, create assignments, and organize content into chapters. Decide how much of your course
          will be open to students.</p>
        <h4>What You Can Do:</h4>
        <ul>
          <li>Organize course content into chapters for teaching, homework, and projects.</li>
          <li>Add multimedia resources, including PDFs, videos, and slides.</li>
          <li>Select whether your course will be open, semi-open, or closed.</li>
        </ul>
      </div>
      <img src="@/assets/img_8.png" alt="Create Course"/>
    </el-tab-pane>

    <el-tab-pane label="Engage with Students" name="engage-students" class="tab-content">
      <div>
        <h3>Engage with Your Students</h3>
        <p>Build a connection with your students by interacting through notifications, Q&A sessions, and comments.</p>
        <h4>What You Can Do:</h4>
        <ul>
          <li>Send announcements and reminders to enrolled students.</li>
          <li>Interact with students through comments and discussion forums.</li>
          <li>Allow students to give feedback on lessons and assignments.</li>
        </ul>
      </div>
      <img src="@/assets/img_3.png" alt="Engage with Students"/>
    </el-tab-pane>

    <el-tab-pane label="Track Progress" name="track-progress" class="tab-content">
      <div>
        <h3>Track Student Progress</h3>
        <p>Use analytics tools to monitor student engagement and course completion rates.</p>
        <h4>What You Can Do:</h4>
        <ul>
          <li>View progress reports for each student and chapter.</li>
          <li>Monitor homework submissions and participation.</li>
          <li>Access feedback and reviews to improve your course.</li>
        </ul>
      </div>
      <img src="@/assets/img_5.png" alt="Track Progress"/>
    </el-tab-pane>

    <el-tab-pane label="Host Live Sessions" name="host-live" class="tab-content">
      <div>
        <h3>Host Live Sessions</h3>
        <p>Engage with students in real-time through live streaming and Q&A sessions.</p>
        <h4>What You Can Do:</h4>
        <ul>
          <li>Set up live broadcasts for interactive lessons.</li>
          <li>Host Q&A sessions and group discussions.</li>
          <li>Record live sessions for students to review later.</li>
        </ul>
      </div>
      <img src="@/assets/img_7.png" alt="Live Session Image"/>
    </el-tab-pane>
  </el-tabs>

  <swiper
      :modules="[Navigation]"
      class="mySwiper"
      @swiper="handleSwiper"
  >
    <swiper-slide>
      <div class="slide-content">
        <img src="@/assets/img_10.png" alt="Instructor Image" class="slide-image"/>
        <div class="slide-text">
          <p class="quote1">"Teaching is about creating a space where curiosity can thrive and knowledge becomes a tool
            for growth. Every interaction is a chance to inspire a brighter future for both the student and the
            teacher."</p>
          <h3 class="author1">Maria Sanchez</h3>
          <p class="author-title1">Leadership and Personal Development</p>
        </div>
      </div>
    </swiper-slide>

    <swiper-slide>
      <div class="slide-content">
        <img src="@/assets/img_11.png" alt="Instructor Image" class="slide-image"/>
        <div class="slide-text">
          <p class="quote2">"True learning is more than memorizing facts; it's about inspiring curiosity and encouraging
            the drive to
            explore. My goal is to empower every student to see beyond the textbook and embrace the wonders of
            discovery.""</p>
          <h3 class="author2">Thomas Meyer</h3>
          <p class="author-title2">Medical Educator & Health Science</p>
        </div>
      </div>
    </swiper-slide>

    <swiper-slide>
      <div class="slide-content">
        <img src="@/assets/img_6.png" alt="Instructor Image" class="slide-image"/>
        <div class="slide-text">
          <p class="quote3">"The beauty of teaching lies in the ability to guide others to unlock their potential and
            achieve things
            they never thought possible. Every lesson is an opportunity to inspire growth, not just in students, but in
            ourselves as well."
          </p>
          <h3 class="author3">Sophia Bennett
          </h3>
          <p class="author-title3">Digital Marketing Consultant & Instructor
          </p>
        </div>
      </div>
    </swiper-slide>
    <div v-if="!atStart" class="swiper-button-prev" @click="goToPrev"></div>
    <div v-if="!atEnd" class="swiper-button-next" @click="goToNext"></div>
  </swiper>

  <div class="teacher-journey">
    <img src="@/assets/img_14.png" alt="Left Image" class="journey-img1"/>
    <div class="journey-text">
      <h1>You're not alone in this journey</h1>
      <p>Whether you’re creating your first course or looking to refine your teaching approach, we’re here to support
        you.
        Our resources include tutorials, guides, and community discussions, helping you every step of the way. Connect
        with fellow instructors and grow together in our supportive learning environment.</p>
    </div>
    <img src="@/assets/img_15.png" alt="Right Image" class="journey-img2"/>
  </div>

  <div class="call-to-action">
    <h1>Become an instructor today</h1>
    <p>Join one of the world’s largest online learning marketplaces.</p>
    <router-link to="/signup-teacher">
      <button class="cta-button">Get started</button>
    </router-link>
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

const activeTab = ref('create-course');
const handleClick = (tab, event) => {
  console.log(`Tab: ${tab.label} clicked`, event);
};
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

.main-picture {
  position: relative;
  max-height: 600px; /* Adjust height as needed */
  overflow: hidden;
  margin-right: -9px;
  margin-left: -9px;
}

.main-picture img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.text-box {
  position: absolute;
  top: 33%;
  left: 10%;
  background-color: rgba(255, 255, 255, 0.9);
  padding: 30px;
  max-width: 320px;
  font-family: 'Aptos Narrow', sans-serif;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.main-picture h1 {
  font-family: 'Aptos Narrow', sans-serif;
  font-weight: bold;
}

.main-picture p {
  max-width: 280px;
  font-family: 'Aptos Narrow', sans-serif;
  margin-left: 5px;
  text-align: left;
}

.intro-section {
  text-align: center;
  margin: 50px auto 50px auto;
  max-width: 800px;
}

.intro-section h1 {
  font-size: 40px;
  font-weight: bold;
  margin-bottom: 30px !important;
  margin-top: 100px !important;
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
}

.start h1 {
  font-size: 40px;
  font-weight: bold;
  margin-bottom: 25px !important;
  margin-top: 50px !important;
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
}

.reasons-container {
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin: 50px auto;
  max-width: 1400px;
  text-align: center;
}

.reason {
  flex: 1;
  margin: 0 20px 50px;
  max-width: 300px;
  text-align: center;
}

.reason img {
  width: 100px;
  height: auto;
  margin-bottom: 5px;
}

.reason h3 {
  margin-top: 0;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 5px;
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
}

.reason p {
  margin: 0 auto;
  font-size: 16px;
  line-height: 1.5;
  color: black;
}

.tabs {
  margin-top: 30px;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
  text-align: left;
}

.tab-content img {
  max-width: 40%;
  height: auto;
}

.tab-content div {
  max-width: 55%;
}

.tab-content {
  display: flex;
  align-items: center; /* Vertically align content */
  justify-content: space-between; /* Space between text and image */
  gap: 20px; /* Add space between elements */
  margin-top: 10px !important;
  color: black;
  font-size: 18px;
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

::v-deep(.el-tabs__nav) {
  display: flex;
  justify-content: center; /* Center the tabs */
}

::v-deep(.el-tabs__item) {
  font-size: 20px;
  font-weight: bold;
  padding: 27px 18.3px;
  color: #7A7A7A; /* Grey color for non-active tabs */
  position: relative; /* Ensure the underline is positioned correctly */
  transition: color 0.3s ease;
  font-family: 'Aptos Narrow', sans-serif;
}

::v-deep(.el-tabs__item::after) {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: transparent; /* Initially invisible */
  transition: background-color 0.3s ease;
}

::v-deep(.el-tabs__item.is-active) {
  color: black;
}

::v-deep(.el-tabs__active-bar) {
  background-color: black !important; /* Change the default underline to black */
}

::v-deep(.el-tabs__item:hover) {
  color: black; /* Black text on hover */
}

.call-to-action {
  background-color: #f8f9fa;
  text-align: center;
  padding: 50px 20px; /* Add space around the content */
  margin-top: 50px;
  display: flex;
  flex-direction: column;
  align-items: center; /* Center horizontally */
  justify-content: center; /* Center vertically */
  margin-left: -9px;
  margin-right: -9px;
}

.call-to-action h1 {
  font-size: 40px;
  font-weight: bold;
  margin-bottom: 15px !important;
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
}

.call-to-action p {
  font-size: 23px;
  max-width: 500px;
  margin-bottom: 30px;
  color: black;
  margin-top: 0 !important;
  text-align: center;
}

.cta-button {
  background-color: #333;
  color: white;
  padding: 15px 115px;
  border: none;
  font-size: 16px;
  font-weight: bold;
  font-family: 'Aptos Narrow', sans-serif;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.cta-button:hover {
  background-color: #444;
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

.slide-image {
  margin-right: auto;
  margin-left: 400px;
  width: 20%;
  object-fit: cover;
}

.slide-text {
  margin-left: 0 !important;
  margin-right: 370px;
  width: 25%;
  text-align: left;
  padding-left: 20px;
}

.quote1 {
  font-size: 18px;
  line-height: 1.5;
  color: black;
  margin-bottom: 0;
  font-style: italic;
  margin-top: 0 !important;
}

.author1 {
  margin-top: 80px;
  margin-bottom: 0 !important;
  font-size: 22px;
  font-weight: bold;
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
}

.author-title1 {
  margin-top: 5px !important;
  font-size: 16px;
  color: black;
  font-weight: lighter;
  margin-bottom: 50px !important;
}

.quote2 {
  font-size: 18px;
  line-height: 1.5;
  color: black;
  margin-bottom: 10px;
  font-style: italic;
  margin-top: 0 !important;
}

.author2 {
  margin-top: 80px;
  margin-bottom: 0 !important;
  font-size: 22px;
  font-weight: bold;
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
}

.author-title2 {
  margin-top: 5px !important;
  font-size: 16px;
  color: black;
  font-weight: lighter;
  margin-bottom: 30px !important;
}

.quote3 {
  font-size: 18px;
  line-height: 1.5;
  color: black;
  margin-bottom: 5px;
  font-style: italic;
  margin-top: 0 !important;
}

.author3 {
  margin-top: 50px;
  margin-bottom: 0 !important;
  font-size: 22px;
  font-weight: bold;
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
}

.author-title3 {
  margin-top: 5px !important;
  font-size: 16px;
  color: black;
  font-weight: lighter;
  margin-bottom: 15px !important;
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

.impact-bar {
  display: flex;
  align-items: center;
  justify-content: space-around;
  background-color: #74B3E3;
  margin-top: 70px;
  margin-bottom: 100px;
  padding: 60px;
  margin-left: -9px !important;
  margin-right: -9px !important;
}

.bar-content h1 {
  margin-top: 0;
  font-size: 40px;
  font-weight: bolder;
  margin-bottom: 5px;
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
}

.bar-content p {
  margin: 0 auto;
  font-size: 16px;
  font-weight: bold;
  line-height: 1.5;
  color: black;
  text-align: center;
}

.teacher-journey {
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin-top: 70px;
  padding: 0; /* Remove padding on the sides */
  margin-left: -9px !important;
  margin-right: -9px !important;
}

.journey-img1, .journey-img2 {
  width: 25%;
  object-fit: cover;
}

.journey-img1 {
  margin-left: -30px;
  margin-right: 110px;

}

.journey-img2 {
  margin-left: auto;
}

.journey-text h1 {
  margin-top: 0;
  font-size: 40px;
  font-weight: bolder;
  margin-bottom: 5px;
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
}

.journey-text p {
  font-size: 16px;
  line-height: 1.5;
  color: black;
  text-align: center;
  max-width: 600px;
  margin-top: 15px;
}
</style>
