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
      /> </router-link>
    </el-menu-item>
    <el-menu-item index="1"><router-link to="/teacherpage">Teach on SUSSY</router-link></el-menu-item>
    <el-menu-item index="2"><router-link to="/loginstudent">Log in</router-link></el-menu-item>
    <el-menu-item index="3"><router-link to="/signupstudent">Sign up</router-link></el-menu-item>
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
    <p>Learn new skills to elevate your career and achieve personal growth. Explore courses and find your passion today.</p>
  </div>

</template>

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


/* Menu styling */
.el-menu-demo {
  background-color: white; /* Transparent to match the topbar */
  position: fixed;
  top:0;
  left:0;
  width: 100%;
  z-index: 1000;
  height: 75px; /* Adjust height to make it bigger */
  padding: 0 20px; /* Add horizontal padding */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}
.el-menu-demo img {
  width: 60px ; /* Force the new size */
  height: auto ; /* Maintain aspect ratio */
  object-fit: contain;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

/* Remove the active color effect */
.el-menu-demo .el-menu-item {
  font-size: 18px; /* Adjust font size */
  color: black !important;/* Default text color */
  background-color: transparent !important; /* Prevent background highlighting */
  transition: color 0.3s; /* Smooth transition */

}
/* Add hover effect */
.el-menu-demo .el-menu-item:not(:first-child):hover{
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

.carousel-container{
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
  height:100%;
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
