<template>
  <!-- Top Navigation Menu -->
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
    <el-menu-item index="0">
      <router-link to="/">
        <img src="@/assets/logo.png" alt="Element logo"/>
      </router-link>
    </el-menu-item>
    <el-menu-item index="1" @click="toggleSidebar" class="sidebar-toggle">
      <img class="profile-pic-small" :src="user.profilePic" alt="Profile Picture"/>
    </el-menu-item>
  </el-menu>

  <div class="page-container">
    <div v-if="isSidebarVisible" class="overlay" @click="toggleSidebar"></div>

    <!-- Main Content Section -->
    <div :class="['main-content', { 'content-shifted': isSidebarVisible }]">
      <!-- Search and Filter Section -->
      <div class="search-filter-section">
        <el-input
            placeholder="Search for courses..."
            v-model="searchQuery"
            @keyup.enter="filterCourses"
            clearable
            prefix-icon="el-icon-search"
        ></el-input>
        <el-select
            v-model="selectedCategory"
            placeholder="Select Category"
            @change="filterCourses"
            clearable
        >
          <el-option
              v-for="category in categories"
              :key="category"
              :label="category"
              :value="category"
          />
        </el-select>
      </div>

      <!-- Course Boxes Section -->
      <h2 class="section-heading">Available Courses</h2>
      <div class="course-boxes">
        <div
            v-for="(course, index) in filteredCourses"
            :key="index"
            class="course-box"
            @click="goToCourse(course.id)"
        >
          <img :src="course.image" alt="Course Image" class="course-image"/>
          <h3>{{ course.title }}</h3>
          <p class="course-rating">⭐ {{ course.rating }} / 5</p>
        </div>
      </div>

      <!-- Student Testimonials Section -->
      <div class="testimonials-section">
        <h2 class="section-heading">What Our Students Say</h2>
        <div class="testimonials">
          <div v-for="(testimonial, index) in testimonials" :key="index" class="testimonial">
            <p class="testimonial-text">"{{ testimonial.text }}"</p>
            <p class="testimonial-author">- {{ testimonial.author }}</p>
          </div>
        </div>
      </div>

      <!-- Newsletter Subscription Section -->
      <div class="newsletter-section">
        <h2>Stay Updated with Our Newsletter</h2>
        <p>Subscribe to our newsletter for the latest updates on new courses and promotions.</p>
        <div class="subscription-form">
          <el-input
              placeholder="Enter your email"
              v-model="subscriberEmail"
              prefix-icon="el-icon-mail"
          ></el-input>
          <el-button type="primary" @click="subscribeNewsletter">Subscribe</el-button>
        </div>
      </div>
    </div>

    <!-- Call to Action Section -->
    <div class="call-to-action">
      <h1>Become a student today</h1>
      <p>Join one of the world's largest online learning marketplaces.</p>
      <router-link to="/signup-student">
        <button class="cta-button">Get started</button>
      </router-link>
    </div>

    <!-- Sidebar Component -->
    <ProfileSidebar
        :user="user"
        :activeIndex="activeIndex"
        :isVisible="isSidebarVisible"
        @menuSelect="handleMenuSelect"
    />
  </div>
</template>

<script setup>
import {computed, ref} from 'vue';
import {useRouter} from 'vue-router';
import ProfileSidebar from '@/components/ProfileSidebar.vue';

const user = ref({
  name: 'John Doe',
  email: 'john.doe@example.com',
  profilePic: "/assets/Avatars/student.jpg",
});

const activeIndex = ref('1');
const isSidebarVisible = ref(false);
const router = useRouter();

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};


const courses = ref([
  {id: 1, title: 'React Course', image: "/assets/Courses/course.jpg", rating: 4.5},
  {id: 2, title: 'Vue Course', image: '/assets/Courses/course2.jpg', rating: 4.7},
  {id: 3, title: 'Python Basics', image: '/assets/courses/course3.png', rating: 4.3},
  {id: 4, title: 'Graphic Design', image: '/assets/courses/course4.png', rating: 4.6},
]);

const categories = ref(['All', 'Web Development', 'Programming', 'Design']);
const searchQuery = ref('');
const selectedCategory = ref('All');

const testimonials = ref([
  {text: "This platform transformed my learning experience!", author: "Alice Brown"},
  {text: "The courses are well-structured and very informative.", author: "David Chen"},
  {text: "I've gained so much knowledge in such a short time.", author: "Lily Evans"}
]);

const subscriberEmail = ref('');

const subscribeNewsletter = () => {
  if (subscriberEmail.value) {
    alert(`Thank you for subscribing, ${subscriberEmail.value}!`);
    subscriberEmail.value = '';
  } else {
    alert("Please enter a valid email address.");
  }
};

const filteredCourses = computed(() => {
  return courses.value.filter(course => {
    const matchesSearch = course.title.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesCategory = selectedCategory.value === 'All' || course.category === selectedCategory.value;
    return matchesSearch && matchesCategory;
  });
});

const filterCourses = () => {
  // The computed property 'filteredCourses' will automatically update
};

const goToCourse = (courseId) => {
  router.push({name: 'CoursePage', params: {courseId}});
};

const handleMenuSelect = (index) => {
  activeIndex.value = index;
};
</script>

<style scoped>
.main-content {
  flex: 1;
  margin-left: 20px;
  transition: transform 0.3s ease;
}

.main-content.content-shifted {
  transform: translateX(-250px);
}

.search-filter-section {
  display: flex;
  gap: 20px;
  margin-top: 50px;
  padding: 20px;
  margin-left: 200px;
  width: 30%;
}

.section-heading {
  font-size: 36px;
  font-weight: bold;
  color: #333;
  margin: 30px 0;
  text-align: center;

  font-family: 'Aptos Narrow', sans-serif;
}

.course-boxes {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  gap: 30px;
  margin: 0 200px;
}

.course-box {
  width: 250px;
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 12px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  text-align: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.course-box:hover {
  background-color: #e0e0e0;
}

.course-box h3 {
  font-size: 20px;
  margin: 15px 0 10px 0;
}

.course-box p.course-rating {
  font-size: 16px;
  color: #555;
}

.course-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.testimonials-section {
  margin: 60px 200px;
  padding: 30px;
  background-color: #f0f8ff;
  border-radius: 12px;
  text-align: center;
}

.testimonial {
  width: 220px;
  background-color: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  margin: 20px auto;
  text-align: center;
}

.testimonial-text {
  font-size: 18px;
  color: #333;
  margin-bottom: 8px;
  text-align: center;
}

.testimonial-author {
  font-size: 16px;
  color: #555;
  font-weight: bold;
}

.newsletter-section {
  margin: 60px 200px;
  padding: 30px;
  background-color: #f0f8ff;
  border-radius: 12px;
  text-align: center;
}

.newsletter-section h2 {
  font-size: 32px;
  font-weight: bold;
  color: #333;
}

.newsletter-section p {
  font-size: 18px;
  color: #666;
  margin: 15px 0;
}

.subscription-form {
  display: flex;
  gap: 10px;
  margin-top: 20px;
  width: 60%;
  margin: 0 auto;
  justify-content: center;
}

.call-to-action {
  background-color: #f8f9fa;
  text-align: center;
  padding: 50px;
  margin-top: 50px;
  border-radius: 12px;
}

.call-to-action h1 {
  font-size: 36px;
  color: #333;
}

.call-to-action p {
  font-size: 18px;
  color: #666;
}

.cta-button {
  background-color: #333;
  color: white;
  padding: 15px 50px;
  border: none;
  font-size: 18px;
  font-weight: bold;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.cta-button:hover {
  background-color: #444;
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
  margin-right: -25px;
}

.el-menu-item:nth-child(4) {
  margin-left: 0;
}

.sidebar-toggle {
  margin-left: auto;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.profile-pic-small {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  overflow: hidden;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
}
</style>
