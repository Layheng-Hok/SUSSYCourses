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
      :slides-per-view="4"
      :space-between="20"
      navigation
      pagination
      class="course-swiper"
  >
    <swiper-slide v-for="(course, index) in courses" :key="index">
      <div class="course-card">
        <img :src="course.src" :alt="course.alt" class="course-image"/>
        <h3>{{ course.title }}</h3>
        <p>{{ course.instructor }}</p>
        <div class="course-rating">
          <span>{{ course.rating }} ★ ({{ course.reviews }})</span>
        </div>
        <div class="course-price">{{ course.price }}</div>
      </div>
    </swiper-slide>
  </swiper>
</template>
<script>
import {Swiper, SwiperSlide} from 'swiper/vue';
import 'swiper/swiper-bundle.css';

export default {
  components: {
    Swiper,
    SwiperSlide,
  },
  data() {
    return {
      courses: [
        {
          src: require('@/assets/tm1.png'),
          title: 'Course 1',
          instructor: 'Instructor A',
          rating: 4.5,
          reviews: 150,
          price: '$99.99'
        },
        {
          src: require('@/assets/tm2.png'),
          title: 'Course 2',
          instructor: 'Instructor B',
          rating: 4.7,
          reviews: 200,
          price: '$89.99'
        },
        {
          src: require('@/assets/tm3.png'),
          title: 'Course 3',
          instructor: 'Instructor C',
          rating: 4.3,
          reviews: 120,
          price: '$79.99'
        },
        // Add more courses here...
        {
          src: require('@/assets/tm1.png'),
          title: 'Course 1',
          instructor: 'Instructor A',
          rating: 4.5,
          reviews: 150,
          price: '$99.99'
        },
        {
          src: require('@/assets/tm2.png'),
          title: 'Course 2',
          instructor: 'Instructor B',
          rating: 4.7,
          reviews: 200,
          price: '$89.99'
        },
        {
          src: require('@/assets/tm3.png'),
          title: 'Course 3',
          instructor: 'Instructor C',
          rating: 4.3,
          reviews: 120,
          price: '$79.99'
        },
        // Add more courses here...
        {
          src: require('@/assets/tm1.png'),
          title: 'Course 1',
          instructor: 'Instructor A',
          rating: 4.5,
          reviews: 150,
          price: '$99.99'
        },
        {
          src: require('@/assets/tm2.png'),
          title: 'Course 2',
          instructor: 'Instructor B',
          rating: 4.7,
          reviews: 200,
          price: '$89.99'
        },
        {
          src: require('@/assets/tm3.png'),
          title: 'Course 3',
          instructor: 'Instructor C',
          rating: 4.3,
          reviews: 120,
          price: '$79.99'
        },
        // Add more courses here...
      ],
    };
  },
};
</script>

<script setup>
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

.course-swiper {
  width: 80%;
  margin: 0 auto;
  padding: 20px 0;
}

.course-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  transition: box-shadow 0.3s;
}

.course-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.course-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 10px;
}


/* Menu styling */
.el-menu-demo {
  background-color: white; /* Transparent to match the topbar */
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  height: 75px; /* Adjust height to make it bigger */
  padding: 0 20px; /* Add horizontal padding */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.el-menu-demo img {
  width: 60px; /* Force the new size */
  height: auto; /* Maintain aspect ratio */
  object-fit: contain;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

/* Remove the active color effect */
.el-menu-demo .el-menu-item {
  font-size: 18px; /* Adjust font size */
  color: black !important; /* Default text color */
  background-color: transparent !important; /* Prevent background highlighting */
  transition: color 0.3s; /* Smooth transition */

}

/* Add hover effect */
.el-menu-demo .el-menu-item:not(:first-child):hover {
  color: purple !important; /* Change text color on hover */
}

/* Prevent the background effect on selection */
.el-menu-item.is-active {
  background-color: transparent !important;
  border-bottom: none !important;
}

/* Remove underline from all router links inside the menu */
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
  width: 90%; /* Adjust horizontal width */
  border-radius: 8px; /* Slight rounding for modern feel */
  overflow: hidden;
}


.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover; /* Ensures the image fits the container without distortion */
  border-radius: 8px;
}

.el-carousel__item {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent; /* Optional: removes the background color */
}

.intro-section {
  text-align: left;
  margin: 50px auto 50px 100px; /* Adjust spacing top right bottom left*/
  max-width: 800px; /* Optional: limit the text width */
}

.intro-section h1 {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 5px !important;
}

.intro-section p {
  font-size: 18px;
  color: #666; /* Subtle color for the description */
  line-height: 1.6;
  margin-top: 0 !important;
}


</style>
