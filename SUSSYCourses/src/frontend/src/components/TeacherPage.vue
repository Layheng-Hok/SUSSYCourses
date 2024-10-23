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

    <el-menu-item index="1"><router-link to="/studentpage">Study on SUSSY</router-link></el-menu-item>
    <el-menu-item index="2"><router-link to="/login">Log in</router-link></el-menu-item>
    <el-menu-item index="3"><router-link to="/signupteacher">Sign up</router-link></el-menu-item>
  </el-menu>

  <div class="main-picture">
  <img src="@/assets/img_3.png" alt="teacher-picture" />
    <h1>Come teach with us</h1>
    <p>Become an instructor and change lives — including your own</p>

  </div>

  <div class="intro-section">
    <h1>So many reasons to start</h1>
  </div>
  <div class="reasons-container">
    <div class="reason">
      <img src="@/assets/tm1.png" alt="Teach your way icon" />
      <h3>Teach your way</h3>
      <p>
        Publish the course you want, in the way you want, and always have control of your own content.
      </p>
    </div>
    <div class="reason">
      <img src="@/assets/tm2.png" alt="Inspire learners icon" />
      <h3>Inspire learners</h3>
      <p>
        Teach what you know and help learners explore their interests, gain new skills, and advance their careers.
      </p>
    </div>
    <div class="reason">
      <img src="@/assets/tm3.png" alt="Get rewarded icon" />
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
    <el-tab-pane label="Create a Course" name="create-course">
      <h3>Create Your Course</h3>
      <p>Plan engaging lessons, create assignments, and organize content into chapters. Decide how much of your course will be open to students.</p>
      <h4>What You Can Do:</h4>
      <ul>
        <li>Organize course content into chapters for teaching, homework, and projects.</li>
        <li>Add multimedia resources, including PDFs, videos, and slides.</li>
        <li>Select whether your course will be open, semi-open, or closed.</li>
      </ul>
      <img src="@/assets/tm1.png" alt="Create Course" />
    </el-tab-pane>

    <el-tab-pane label="Engage with Students" name="engage-students" class="engage">
      <h3>Engage with Your Students</h3>
      <p>Build a connection with your students by interacting through notifications, Q&A sessions, and comments.</p>
      <h4>What You Can Do:</h4>
      <ul>
        <li>Send announcements and reminders to enrolled students.</li>
        <li>Interact with students through comments and discussion forums.</li>
        <li>Allow students to give feedback on lessons and assignments.</li>
      </ul>
      <img src="@/assets/tm2.png" alt="Engage with Students" />
    </el-tab-pane>

    <el-tab-pane label="Track Progress" name="track-progress" class="progress">
      <h3>Track Student Progress</h3>
      <p>Use analytics tools to monitor student engagement and course completion rates.</p>
      <h4>What You Can Do:</h4>
      <ul>
        <li>View progress reports for each student and chapter.</li>
        <li>Monitor homework submissions and participation.</li>
        <li>Access feedback and reviews to improve your course.</li>
      </ul>
      <img src="@/assets/tm3.png" alt="Track Progress" />
    </el-tab-pane>

    <el-tab-pane label="Host Live Sessions" name="live-sessions" class="sessions">
      <h3>Host Live Sessions</h3>
      <p>Engage with students in real-time through live streaming and Q&A sessions.</p>
      <h4>What You Can Do:</h4>
      <ul>
        <li>Set up live broadcasts for interactive lessons.</li>
        <li>Host Q&A sessions and group discussions.</li>
        <li>Record live sessions for students to review later.</li>
      </ul>
      <img src="@/assets/tm1.png" alt="Live Sessions" />
    </el-tab-pane>
  </el-tabs>


</template>

<script setup>
import { ref } from 'vue'
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

.main-picture img{

}
.main-picture h1 {
  color: black;
  font-family: 'Aptos Narrow', sans-serif;
  font-weight: bold;
  margin-left: 0;
  margin-right: auto;
}

.main-picture p {
  color: black;
  max-width: 280px;
  font-family: 'Aptos Narrow', sans-serif;
  margin-left: 50px;
  text-align: left;
}

.intro-section {
  text-align: center;
  margin: 50px auto 50px auto; /* Adjust spacing top right bottom left*/
  max-width: 800px; /* Optional: limit the text width */
}

.intro-section h1, .start h1 {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 5px !important;
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
  display: block;
  max-width: 400px;
  margin: 20px auto;
}

.el-tab-pane {
  padding: 20px;
  font-size: 16px;
}

h3 {
  font-weight: bold;
  margin-bottom: 10px;
}

h4 {
  margin-top: 20px;
  margin-bottom: 10px;
  font-weight: bold;
}



</style>/
