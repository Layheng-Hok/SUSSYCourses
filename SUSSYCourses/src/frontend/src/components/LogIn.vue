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
            src="@/assets/logo2.png"
            alt="Element logo"
        /></router-link>
    </el-menu-item>
  </el-menu>

  <div class="main-container">
    <div class="content-wrapper">
      <img src="@/assets/signup.gif" alt="signup Image" class="sign-up"/>
      <div class="login-content">
        <h1>Log in to continue your learning journey</h1>
        <div class="login-form">
          <div class="email-input-wrapper">
            <input v-model="email" type="email" placeholder="Email"
                   @input="() => { clearError('emailError'); showSuggestions(); }"
                   @blur="emailSuggestions.value = []; emptyEmail()"
                   @keydown="handleKeydown"/>
            <!-- Show suggestions dropdown -->
            <ul v-if="emailSuggestions.length" class="suggestions-list">
              <li v-for="(suggestion, index) in emailSuggestions"
                  :key="suggestion"
                  @click="selectSuggestion(suggestion)"
                  :class="{ 'highlighted': index === selectedIndex }">
                {{ email.split('@')[0] + '@' + suggestion }}
              </li>
            </ul>
            <span v-if="emailError" class="field-error">{{ emailError }}</span>
          </div>

          <input v-model="password" type="password" placeholder="Password" @input="clearError('passwordError')"
                 @blur="emptyPassword()"
          />
          <span v-if="passwordError" class="field-error">{{ passwordError }}</span>
        </div>

        <button @click="handleLogin">Log in</button>
        <!-- Display error message if any -->
        <span v-if="errorMessage" class="error-message">{{ errorMessage }}</span>

        <h3>Don't have an account?
          <router-link to="/signup-student" class="signup-link">Sign up</router-link>
        </h3>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import axios from 'axios';
import axiosInstances from "@/services/axiosInstance";
import {useRouter} from "vue-router";

const router = useRouter();

const email = ref('');
const emailSuggestions = ref([]);
const commonDomains = ['gmail.com', 'yahoo.com', 'outlook.com', 'sustech.edu.cn'];
const selectedIndex = ref(-1); // Track the highlighted suggestion
const password = ref('');
const errorMessage = ref('');

const emailError = ref('');
const passwordError = ref('');
const serverErrorMessage = ref('');

const clearError = (field) => {
  if (field === 'emailError') emailError.value = '';
  if (field === 'passwordError') passwordError.value = '';
  if (field === 'serverErrorMessage') serverErrorMessage.value = '';
};

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
/*
username@.com (domain part cannot start with a period)
@domain.com (missing username part)
user@domaincom (missing dot in the domain part)
 */
const emptyEmail = () => {
  if (!email.value.trim()) {
    emailError.value = 'Please enter your email';
  } else if (!emailPattern.test(email.value)) {
    emailError.value = 'Please enter a valid email address';
  } else {
    emailError.value = ''; // Clear error if the email is valid
  }
};

const emptyPassword = () => {
  if (!password.value.trim()) {
    passwordError.value = 'Please enter a password';
    return;
  }
};

const showSuggestions = () => {
  const atIndex = email.value.indexOf('@');

  if (atIndex === -1) {
    emailSuggestions.value = [];
  } else if (atIndex === email.value.length - 1) {
    // "@" is at the end, show all domains
    emailSuggestions.value = commonDomains;
  } else {
    // Filter domains based on what is typed after "@"
    const typedDomain = email.value.slice(atIndex + 1).toLowerCase();
    emailSuggestions.value = commonDomains.filter(domain =>
        domain.startsWith(typedDomain)
    );
  }
};

const handleKeydown = (event) => {
  if (!emailSuggestions.value.length) return;
  if (event.key === 'ArrowDown') {
    selectedIndex.value = (selectedIndex.value + 1) % emailSuggestions.value.length;
    event.preventDefault();
  } else if (event.key === 'ArrowUp') {
    selectedIndex.value = (selectedIndex.value - 1 + emailSuggestions.value.length) % emailSuggestions.value.length;
    event.preventDefault();
  } else if (event.key === 'Enter' && selectedIndex.value > -1) {
    selectSuggestion(emailSuggestions.value[selectedIndex.value]);
    event.preventDefault();
  }
};

const selectSuggestion = (suggestion) => {
  const atIndex = email.value.indexOf('@');
  email.value = email.value.slice(0, atIndex + 1) + suggestion;
  emailSuggestions.value = []; // Clear suggestions after selection
  selectedIndex.value = -1; // Reset selected index
};

const handleLogin = async () => {
      errorMessage.value = '';

      const payload = {
        email: email.value,
        password: password.value,
      };

      try {
        await axios.post('http://localhost:8081/login', payload);
        localStorage.setItem('usn', email.value);
        localStorage.setItem('pwd', password.value);
        localStorage.setItem('isLoggedIn', 'true')

        const response2 = await axiosInstances.axiosInstance.get(`/users/${email.value}`);
        let userId = response2.data.userId;
        let userRole = response2.data.roleName;
        localStorage.setItem('userId', userId);
        localStorage.setItem('roleName', userRole);

        if (userRole === 'ADMIN') {
          await router.push(`admin-dashboard`);
        } else if (userRole === 'STUDENT') {
          await router.push(`student-dashboard/${userId}`);
        } else if (userRole === 'INSTRUCTOR') {
          await router.push(`instructor-dashboard/${userId}`);
        } else {
          alert('Unknown user role');
        }
      } catch (error) {
        if (error.response) {
          errorMessage.value = error.response.data.message || 'Invalid credentials';
          console.error("Error from backend: ", error.response.data.message);
        } else {
          errorMessage.value = 'Something went wrong. Please try again.';
          console.error("Error: ", error);
        }
      }
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

/* Remove the active color effect */
.el-menu-demo .el-menu-item {
  font-size: 18px;
  color: black !important;
  background-color: transparent !important;
  transition: color 0.3s;

}

/* Add hover effect */
.el-menu-demo .el-menu-item:not(:first-child):hover {
  color: #74B3E3 !important;
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

.el-menu-item:nth-child(4) a {
  border: 1px solid black;
  padding: 5px 15px;
  color: black;
  height: 30px;
  line-height: 30px;
  font-weight: bold;
  background-color: white;
  transition: background-color 0.3s, color 0.3s;
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
  padding: 5px 15px;
  height: 30px;
  line-height: 30px;
  font-weight: bold;
  transition: background-color 0.3s, color 0.3s;
}

.el-menu-item:nth-child(5) a:hover {
  background-color: #9DCAEB;
  border: 1px solid #9DCAEB;
  color: white;
}


.main-container {
  margin-top: 100px;
  display: flex;
  justify-content: center;
}

.content-wrapper {
  display: flex;
  align-items: center;
  gap: 150px; /* Space distribution */
  max-width: 1200px;
  margin: 0 auto; /* Center the content */
  padding: 90px;
}

.login-form {
  background-color: transparent;
  max-width: 400px;
}

.login-form input, .login-form select {
  width: 100%;
  padding: 20px; /* Space inside inputs */
  margin-top: 5px;
  margin-bottom: 5px; /* Space below each input */
  border: 1px solid #333;
  font-size: 16px;
  color: #333 !important;
  box-sizing: border-box; /* Include padding and border in the width calculation */
  position: relative;
}

.login-form input::placeholder {
  position: absolute;
  top: 50%;
  left: 20px;
  transform: translateY(-50%);
  transition: all 0.3s ease;
  pointer-events: none;
  color: black;
  font-weight: bold;
  font-family: 'Aptos Narrow', sans-serif;
}

.login-form input:focus {
  outline: none;
  padding-top: 25px;
  padding-bottom: 15px;
}


.login-form input:focus::placeholder,
.login-form input:not(:placeholder-shown)::placeholder {
  top: 5px;
  transform: translateY(0);
  font-size: 13px;
  color: black;
  font-weight: bold;


}

.main-container button {
  width: calc(100% - 80px); /* Adjust width to match input (border difference) */
  padding: 15px;
  background-color: #74B3E3;
  border: 1px solid #74B3E3;
  color: white;
  font-size: 18px;
  cursor: pointer; /* Pointer on hover */
  font-weight: bold;
  margin-top: 12px;
  font-family: 'Aptos Narrow', sans-serif;
}

.main-container h1 {
  margin-bottom: 10px;
}

.main-container h3 {
  margin-top: 25px;
}

.main-container p {
  font-size: 18px;
  color: black;
  line-height: 1.6;
  margin-top: 0 !important;
}

.terms-policy p {
  font-size: 12px;
  color: black;
  line-height: 1.6;
  margin-top: 12px !important;
}

.sign-up {
  width: 500px;

}

.signup-link {
  color: #74B3E3;
  text-decoration: underline;
  font-weight: bold;
  margin-left: 2px; /* Add some space between the text and the link */
  font-family: 'Aptos Narrow', sans-serif;

}

.login-content {
  margin-left: 50px;
  display: flex;
  flex-direction: column;
  align-items: center; /* Centers the content horizontally */
  width: 100%;
}

.login-content h1 {
  color: black;
  max-width: 480px;
  margin-bottom: 25px !important;
  font-weight: bold;
  line-height: 1.2;
  font-family: 'Aptos Narrow', sans-serif;
}

.login-content h3 {
  color: black;
  margin-top: 25px !important;
  line-height: 1.2;
  font-family: 'Aptos Narrow', sans-serif;
}

.field-error {
  color: #D32F2F;
  font-size: 14px;
  font-family: 'Aptos Narrow', sans-serif;
}

.field-error::before {
  content: "⚠️"; /* Adding an icon for a modern touch */
  margin-right: 5px;
  font-size: 14px;
}

.email-input-wrapper {
  position: relative;
}

.suggestions-list {
  list-style: none;
  padding: 0;
  margin: 0;
  position: absolute;
  top: 100%;
  left: 0;
  background-color: #ffffff;
  border: 1px solid #ccc;
  width: 100%;
  max-height: 180px; /* Set a max height for scrolling */
  overflow-y: auto;
  z-index: 1000;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15); /* Softer shadow for depth */
  transition: box-shadow 0.2s ease, border-color 0.2s ease; /* Smooth transitions */
}

.suggestions-list li {
  padding: 12px 16px; /* Increased padding for better spacing */
  cursor: pointer;
  font-size: 16px;
  font-family: 'Aptos Narrow', sans-serif;
  color: black;
  transition: background-color 0.2s ease, color 0.2s ease;
}

.suggestions-list li:not(:last-child) {
  border-bottom: 1px solid #eee; /* Light border between items */
}

.suggestions-list li:hover,
.suggestions-list .highlighted {
  background-color: #f7faff; /* Light blue background for hover and highlight */
  color: #0066cc; /* Change text color on hover */
  font-weight: bold;
  transition: background-color 0.2s ease, color 0.2s ease;
}

.error-message {
  color: #D32F2F;
  font-size: 14px;
  margin-top: 10px;
  font-family: 'Aptos Narrow', sans-serif;
}
</style>/
