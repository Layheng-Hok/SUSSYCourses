<template>
  <div class="container">
    <!-- Top Menu with Profile Avatar -->
    <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
      <el-menu
          class="left-sidebar"
          mode="vertical"
          :class="{ expanded: isSidebarExpanded }"
          @mouseover="toggleSidebarExpansion(true)"
          @mouseleave="toggleSidebarExpansion(false)"
          @click="toggleSidebarExpansion(false)"
      >

        <!-- Logo -->
        <el-menu-item index="0">
          <router-link to="/">
            <img :src="isSidebarExpanded ? require('@/assets/logo4.png') : require('@/assets/logo2.png')"
                 alt="Element logo"
                 :class="{ expanded: isSidebarExpanded, collapsed: !isSidebarExpanded }"
            />
          </router-link>
        </el-menu-item>

        <!-- Courses -->
        <el-menu-item index="1"
                      @click="setActiveTab('courses')"
                      :class="{ 'is-active': activeTab === 'courses' }"
        >
          <el-icon>
            <Notebook/>
          </el-icon>
          <span v-if="isSidebarExpanded">Courses</span>
        </el-menu-item>

        <!-- Notifications -->
        <el-menu-item index="2" @click="setActiveTab('notifications')">
          <el-icon>
            <Notification/>
          </el-icon>
          <span v-if="isSidebarExpanded">Notifications</span>
        </el-menu-item>

        <!-- Help and Support -->
        <el-menu-item index="3" @click="setActiveTab('help')">
          <el-icon>
            <Help/>
          </el-icon>
          <span v-if="isSidebarExpanded">Help and Support</span>
        </el-menu-item>
      </el-menu>

      <!-- Profile Avatar -->
      <el-menu-item index="0" @click="toggleSidebar">
        <el-avatar :size="50" shape="circle">
          <template v-if="user.profilePic">
            <img :src="user.profilePic" alt="Profile Picture"/>
          </template>
          <template v-else>
            {{ user.initials }}
          </template>
        </el-avatar>
      </el-menu-item>
    </el-menu>

    <!-- Overlay for Shadow Effect -->
    <div v-if="showSidebar" class="overlay" @click="hideSidebar"></div>

    <!-- Sidebar (slide-in effect) -->
    <el-menu :class="['sidebar', { 'sidebar-open': showSidebar }]" mode="vertical" :default-active="activeIndex"
             @select="handleSelect">
      <!-- Profile Header Section -->
      <div class="profile-header">
        <el-avatar :size="70" shape="circle">
          <template v-if="user.profilePic">
            <img :src="user.profilePic" alt="Profile Picture"/>
          </template>
          <template v-else>
            {{ user.initials }}
          </template>
        </el-avatar>
        <h3>{{ user.name }}</h3>
        <p>{{ user.email }}</p>
      </div>

      <!-- Sidebar Menu Items -->
      <el-menu-item index="1" @click="setActiveTab('profile')">
        <i class="el-icon-user"></i>
        <span>Profile</span>
      </el-menu-item>

      <el-menu-item index="2">
        <i class="el-icon-folder"></i>
        <span>My Courses</span>
      </el-menu-item>

      <el-menu-item index="3" @click="setActiveTab('security')">
        <i class="el-icon-setting"></i>
        <span>Account Security</span>
      </el-menu-item>
      <el-menu-item index="4">
        <i class="el-icon-document"></i>
        <router-link to="/teacherpage">
          <span>Main Teacher Page</span>
        </router-link>
      </el-menu-item>
      <el-menu-item index="5">
        <i class="el-icon-close"></i>
        <span>Log Out</span>
      </el-menu-item>
    </el-menu>

    <!-- Main Content Area -->
    <div class="main-content">
      <TeacherCourses v-if="activeTab === 'courses' && !showCourseDetails"
                      @course-submitted="navigateToCourseDetails"/>
      <TeacherCoursesDetails v-if="activeTab === 'courses' && showCourseDetails"
      />
      <HelpSupport v-if="activeTab === 'help'"/>
      <TeacherProfile v-if="activeTab === 'profile'"/>
      <Security v-if="activeTab === 'security'"/>
      <TeacherNotifications v-if="activeTab === 'notifications' "/>
    </div>
  </div>
</template>

<script setup>
import {ref, computed, watch, onMounted} from 'vue';
import {defineProps} from 'vue';
import {Help, Notebook, Notification} from "@element-plus/icons-vue";
import TeacherCourses from "@/components/TeacherCourses.vue";
import HelpSupport from "@/components/HelpSupport.vue";
import TeacherCoursesDetails from "@/components/TeacherCoursesDetails.vue";
import TeacherProfile from "@/components/TeacherProfile.vue";
import Security from "@/components/Security.vue";
import TeacherNotifications from "@/components/TeacherNotifications.vue";
import axiosInstances from "@/services/axiosInstance";

const props = defineProps({
  initialTab: String,
});

const activeTab = ref('courses'); // Default to 'courses'
onMounted(() => {
  if (props.initialTab) {
    activeTab.value = props.initialTab;
  }
});

const showCourseDetails = ref(false); // Controls whether to show TeacherCourses or CourseDetails
watch(
    () => props.initialTab,
    (newTab) => {
      if (newTab) {
        activeTab.value = newTab;
      }
    }
);
const setActiveTab = (tab) => {
  activeTab.value = tab;
  console.log("Active tab:", activeTab.value);
};

const user = ref({
  name: 'Loading...',
  email: 'Fetching...',
  profilePic: null,
  initials: computed(() => user.value.name.charAt(0).toUpperCase()),
});

const activeIndex = ref('1');
const showSidebar = ref(false);

const toggleSidebar = () => {
  showSidebar.value = !showSidebar.value;
};

const hideSidebar = () => {
  showSidebar.value = false;
};

const handleSelect = (index) => {
  console.log("Selected menu item:", index);
  showSidebar.value = false;
};

const isSidebarExpanded = ref(false);
const toggleSidebarExpansion = (expand) => {
  isSidebarExpanded.value = expand;
};

// Callback to switch to CourseDetails after form submission
const navigateToCourseDetails = () => {
  showCourseDetails.value = true;
};

onMounted(async () => {
  try {
    const userId = localStorage.getItem("userId");
    if (!userId) {
      console.error("No userId found in localStorage!");
      return;
    }

    const response = await axiosInstances.axiosInstance.get(`/instructor/profile/${userId}`);
    const userData = response.data;

    user.value.name = userData.fullName || "No Name";
    user.value.email = userData.email || "No Email";
    user.value.profilePic = userData.profileImageUrl || null;
  } catch (error) {
    console.error("Error fetching user data:", error);
  }
});
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
  width: 43px;
  height: auto;
  margin-left: -10px;
  object-fit: contain;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

.el-menu-demo .el-menu-item:last-child {
  margin-left: auto;
}

.el-menu-item.is-active {
  background-color: transparent !important;
  border-bottom: none !important;
}

.el-menu-demo .el-menu-item a {
  text-decoration: none !important;
}

.el-menu-demo .el-menu-item {
  font-size: 18px;
  color: black !important;
  background-color: transparent !important;
  transition: color 0.3s;
  font-family: 'Aptos Narrow', sans-serif;
}

.el-avatar {
  background-color: #333 !important;
  color: white;
  font-size: 20px;
}

.el-avatar:hover {
  background-color: #444 !important;
}

.container {
  display: flex;
}

.sidebar {
  position: fixed;
  top: 0;
  right: 0;
  width: 250px;
  height: 100vh;
  background-color: white;
  padding-top: 80px;
  z-index: 1100;
  transform: translateX(100%);
  transition: transform 0.3s ease;
}

.sidebar-open {
  transform: translateX(0);
}

.profile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
  text-align: center;
}

.profile-header h3 {
  margin: 10px 0 5px 0;
  font-size: 18px;
  color: black;
}

.profile-header p {
  color: black;
  font-size: 14px;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 1000;
}

.main-content {
  flex: 1;
  padding: 20px;
}

.sidebar .el-menu-item {
  font-size: 16px;
  display: flex;
  align-items: center;
  padding-left: 20px;
}

.sidebar .el-menu-item i {
  margin-right: 10px;
  font-size: 20px;
}

.left-sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 65px;
  height: 100vh;
  background-color: #222;
  transition: width 0.3s ease;
  overflow: hidden;
  z-index: 1100;
  padding-top: 20px;
}

.left-sidebar.expanded {
  width: 230px;
}

.left-sidebar img {
  transition: width 0.3s ease, height 0.3s ease;
}

.left-sidebar img.expanded {
  width: 190px;
  height: auto;
}

.left-sidebar img.collapsed {
  width: 43px;
  height: auto;
}

.left-sidebar .el-menu-item {
  display: flex;
  align-items: center;
  padding: 10px;
  color: white;
}

.left-sidebar .el-menu-item i {
  font-size: 24px;
  margin-right: 10px;
}

.left-sidebar .el-menu-item span {
  display: none;
  white-space: nowrap;
}

.left-sidebar:hover .el-menu-item span {
  display: inline;
}

.left-sidebar .el-menu-item {
  color: white !important;
  margin-bottom: 20px;
}

.left-sidebar .el-menu-item:hover {
  color: #74B3E3 !important;
}

.el-menu-item a {
  color: black;
  text-decoration: none;
}

.el-menu-item {
  color: black;
}

.el-menu-item a:hover {
  color: #74B3E3;
}

.el-menu-item:hover {
  color: #74B3E3;
  background-color: transparent;
}

.sidebar .el-menu-item.is-active {
  color: black !important;
}

.el-menu-item.is-active:hover {
  color: #74B3E3 !important;
}

.left-sidebar .el-menu-item.is-active {
  position: relative;
}

.left-sidebar .el-menu-item.is-active::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  height: 125%;
  width: 4px;
  background-color: #74B3E3;
}
</style>
