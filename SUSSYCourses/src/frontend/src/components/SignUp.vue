<template>
  <div class="signup-form">
    <h2>Sign Up</h2>
    <input v-model="name" type="text" placeholder="Name" />
    <input v-model="email" type="email" placeholder="Email" />
    <input v-model="password" type="password" placeholder="Password" />
    <input v-model="confirmPassword" type="password" placeholder="Confirm Password" />

    <!-- Role Selection -->
    <label for="role">Select your role:</label>
    <select v-model="role">
      <option value="student">Student</option>
      <option value="teacher">Course Teacher</option>
      <option value="admin">System Administrator</option>
    </select>

    <button @click="handleSignup">Sign Up</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const name = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');
const role = ref('student'); // Default role is student

const handleSignup = () => {
  if (password.value !== confirmPassword.value) {
    console.error("Passwords don't match");
    return;
  }

  // Send signup request to backend with the role
  console.log('Sign up with:', name.value, email.value, password.value, role.value);
};
</script>


<style scoped>
.signup-form {
  background-color: #f9f9f9; /* Light background */
  padding: 20px; /* Padding inside the form */
  max-width: 400px; /* Restrict the form width */
  margin: 50px auto; /* Center the form on the page */
  border-radius: 8px; /* Rounded corners */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Light shadow for depth */
}

.signup-form h2 {
  text-align: center; /* Center the heading */
  color: #333; /* Dark text color */
  margin-bottom: 20px; /* Space below the heading */
}

.signup-form input, .signup-form select {
  width: 100%; /* Full width inputs */
  padding: 10px; /* Space inside inputs */
  margin-bottom: 15px; /* Space below each input */
  border: 1px solid #ccc; /* Light border */
  border-radius: 4px; /* Rounded inputs */
  font-size: 16px; /* Font size */
}

.signup-form button {
  width: 100%; /* Full width button */
  padding: 10px;
  background-color: #4CAF50; /* Green background */
  color: white; /* White text */
  border: none; /* Remove default border */
  border-radius: 4px; /* Rounded corners */
  font-size: 16px; /* Font size */
  cursor: pointer; /* Pointer on hover */
}

.signup-form button:hover {
  background-color: #45a049; /* Darker green on hover */
}

.signup-form label {
  font-size: 14px; /* Label font size */
  color: #333; /* Dark label color */
  margin-bottom: 5px;
  display: block;
}

.signup-form input[type="password"], .signup-form input[type="email"] {
  background-color: #fff; /* White background for inputs */
}

</style>/
