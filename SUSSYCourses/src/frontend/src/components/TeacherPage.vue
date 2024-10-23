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
      <router-link to="/signupteacher">Sign up</router-link>
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

  <div class="call-to-action">
    <h1>Become an instructor today</h1>
    <p>Join one of the world’s largest online learning marketplaces.</p>
    <router-link to="/signupteacher">
      <button class="cta-button">Get started</button>
    </router-link>
  </div>


</template>

<script setup>
import {ref} from 'vue'

const activeTab = ref('create-course');

const handleClick = (tab, event) => {
  console.log(`Tab: ${tab.label} clicked`, event);
};

</script>


<style scoped>

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
  font-family: 'Aptos Narrow', sans-serif;

}

/* Add hover effect */
.el-menu-demo .el-menu-item:not(:first-child):hover {
  color: #74B3E3 !important; /* Change text color on hover */
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

.main-picture {
  position: relative;
  width: 100%; /* Make the image take up the full width */
  max-height: 600px; /* Adjust height as needed */
  overflow: hidden;
}

.main-picture img {
  width: 100%; /* Image covers the full container width */
  height: 100%; /* Maintain aspect ratio */
  object-fit: cover; /* Ensure the image fits nicely */
}

.text-box {
  position: absolute;
  top: 33%; /* Adjust the vertical position */
  left: 10%; /* Align the box to the left */
  background-color: rgba(255, 255, 255, 0.9); /* White background with opacity */
  padding: 30px;
  max-width: 320px; /* Adjust width of the box */
  font-family: 'Aptos Narrow', sans-serif;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2); /* Optional: Subtle shadow */
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
  margin: 50px auto 50px auto; /* Adjust spacing top right bottom left*/
  max-width: 800px; /* Optional: limit the text width */
}

.intro-section h1 {
  font-size: 36px;
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
  max-width: 300px; /* Restrict the width of each section */
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
  margin-top: 5px !important; /* Adjust space under the tabs */
  color: black;
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
  font-size: 20px; /* Adjust the font size */
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
  background-color: #f8f9fa; /* Light grey background */
  text-align: center;
  padding: 60px 20px; /* Add space around the content */
  margin-top: 50px; /* Space above the section */
  display: flex;
  flex-direction: column;
  align-items: center; /* Center horizontally */
  justify-content: center; /* Center vertically */
}

.call-to-action h1 {
  font-size: 40px;
  font-weight: bold; /* Slightly bold */
  margin-bottom: 15px !important;
  color: black; /* Dark grey text */
  font-family: 'Aptos Narrow', sans-serif;
}

.call-to-action p {
  font-size: 23px;
  max-width: 500px;
  margin-bottom: 30px;
  color: black; /* Lighter grey text */
  margin-top: 0 !important;
  text-align: center; /* Ensure the text is centered */

}

.cta-button {
  background-color: #333; /* Dark button background */
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
  background-color: #444; /* Slightly lighter on hover */
}

</style>/
