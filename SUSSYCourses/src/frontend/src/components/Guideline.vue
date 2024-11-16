<template>
  <div class="guideline">
    <h1>Course Creation Guideline</h1>
    <p>Follow these steps to create a course:</p>
  </div>

  <div class="steps">
    <h3>Step 1: Choose a Course Name</h3>
    <p>
      Decide on a clear and descriptive name for your course. This name will help students understand the subject matter
      of the course.
      Avoid using vague or overly complicated names.
    </p>

    <h3>Step 2: Select the Field it Belongs To</h3>
    <p>
      Choose the appropriate field or category that best represents the course content. This helps in organizing the
      courses
      and makes it easier for students to find courses in related fields.
    </p>

    <h3>Step 3: Provide a Course Description</h3>
    <p>
      Write a brief and engaging description of what the course is about. Include the key topics covered, the skills or
      knowledge
      students will gain, and any prerequisites or requirements.
    </p>

    <h3>Step 4: Review and Finalize</h3>
    <p>
      Double-check all information before submitting the course. Ensure the name, field, and description are accurate,
      clear,
      and well-written. Once you're satisfied, you can proceed with course creation.
    </p>
  </div>

  <div class="note">
    <p><strong>Note:</strong> If you need help at any step, feel free to contact the support team for assistance.</p>
  </div>
</template>

<script setup>
</script>

<style scoped>
.guideline h1 {
  color: black;
  font-family: Garamond;
  margin-bottom: 10px;
  margin-top: 40px;
}

.guideline p {
  color: black;
  margin-bottom: 70px;
}

.steps {
  margin-top: 20px;
  color: black;
  flex-direction: column;
  align-items: center;
  text-align: left;
  margin-left: 350px;
}

.steps h3 {
  font-size: 18px;
  color: black;
  text-align: left;
  margin-bottom: -5px;
}

.steps p {
  font-size: 18px;
  color: black;
  margin-bottom: 40px;
  text-align: left;
  max-width: 900px;
}

.note {
  margin-top: 50px;
  font-size: 16px;
  color: #007bff;
}
</style>
