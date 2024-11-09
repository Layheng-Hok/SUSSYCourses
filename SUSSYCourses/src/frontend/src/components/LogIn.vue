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
  </el-menu>

  <div class="main-container">
    <div class="content-wrapper">
      <img src="@/assets/signup.gif" alt="signup Image" class="sign-up"/>
      <div class="login-content">
        <h1>Log in to continue your learning journey</h1>
        <div class="login-form">
          <input v-model="email" type="email" placeholder="Email"/>
          <input v-model="password" type="password" placeholder="Password"/>
        </div>
        <button @click="handleLogin">Log in</button>
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

const email = ref('');
const password = ref('');
const errorMessage = ref('');

const handleLogin = async () => {
  errorMessage.value = '';

  const payload = {
    email: email.value,
    password: password.value,
  };

  try {
    const response = await axios.post('http://localhost:8081/login', payload);
    alert(response.data);
  } catch (error) {
    if (error.response) {
      errorMessage.value = error.response.data || 'Login failed';
    } else {
      console.log("Something went wrong");
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
  margin-bottom: 10px; /* Space below each input */
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
</style>/

