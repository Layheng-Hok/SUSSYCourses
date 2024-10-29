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
      <!-- <el-menu-item index="1"><router-link to="teacherpage">Teach on SUSSY</router-link></el-menu-item> -->
      <!-- <el-menu-item index="3"><router-link to="/signupteacher">Sign up</router-link></el-menu-item> -->
    </el-menu>
  
    <div class="intro-section">
      <h1>notifications:</h1>
    </div>
  
  
  </template>
  
  <script setup>

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
  
  .intro-section {
    text-align: center;
    margin: 100px auto 50px auto; /* Adjust spacing top right bottom left*/
    max-width: 800px; /* Optional: limit the text width */
  }
  
  .intro-section h1 {
    font-size: 36px;
    font-weight: bold;
    margin-bottom: 5px !important;
  }
  
  
  </style>/
  