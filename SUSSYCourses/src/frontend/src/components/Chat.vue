<template>
    <!-- Floating Chat Widget Button -->
    <div class="chat-widget" @click="toggleChatWindow">
      <img src="/assets/Icons/chat-icon.svg" alt="Chat Icon" />
    </div>
  
    <!-- Chat Window -->
    <div v-if="isChatVisible" class="chat-window">
      <div class="chat-header">
        <h3>Chat with Assistant</h3>
        <button @click="toggleChatWindow">Close</button>
      </div>
      <div class="chat-content">
      <div v-for="message in messages" :key="message.id" :class="message.type">
        <p>
          <strong>{{ message.sender }}:</strong> {{ message.text }}
        </p>
      </div>
    </div>
      <div class="chat-input">
        <el-input
          v-model="userInput"
          placeholder="Type your message"
          @keyup.enter="sendMessage"
          clearable
          prefix-icon="el-icon-chat-dot-round"
        />
        <el-button @click="sendMessage">Send</el-button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import axios from 'axios';
  
  const isChatVisible = ref(false);
  const userInput = ref('');
  const messages = ref([]);
  const apiKey = process.env.VUE_APP_OPENAI_API_KEY;  
  const apiUrl = 'https://api.openai.com/v1/chat/completions'; 
  
  const toggleChatWindow = () => {
    isChatVisible.value = !isChatVisible.value;
  };
  
  const sendMessage = async () => {
    if (userInput.value.trim() === '') return;
  
    messages.value.push({ id: Date.now(), text: userInput.value, type: 'user', sender: 'You' });

    try {
      const response = await axios.post(
        apiUrl,
        {
          model: 'gpt-4o-mini',  
          messages: [{ role: 'user', content: userInput.value + " Please keep your response short and simple and dont include markdown formatting."}],
          max_tokens: 150,
        },
        {
          headers: {
            'Authorization': `Bearer ${apiKey}`,
            'Content-Type': 'application/json',
          },
        }
      );
  
      messages.value.push({ id: Date.now(), text: response.data.choices[0].message.content, type: 'bot', sender: 'Assistant' });
        } catch (error) {
      console.error('Error while sending message:', error);
      messages.value.push({ id: Date.now(), text: 'Sorry, there was an error.', type: 'bot' , sender: 'Assistant' });
    } finally {
      userInput.value = '';  
    }
  };
  </script>
  
  <style scoped>
.chat-widget {
  position: fixed;
  bottom: 15px;
  right: 15px;
  background-color: transparent; 
  border-radius: 50%;
  padding: 15px;
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  z-index: 9999;
  transition: background-color 0.3s;
}

.chat-widget:hover {
  background-color: #0056b3; 
}

.chat-widget img {
  width: 40px;
  height: 40px;
}

.chat-window {
  position: fixed;
  bottom: 80px;
  right: 20px;
  width: 380px;
  max-width: 95%;
  height: 490px;
  background-color: white;
  border: none;
  border-radius: 12px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
  z-index: 9999;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  animation: slideIn 0.3s ease-out;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
}

@keyframes slideIn {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.chat-header {
background: linear-gradient(to bottom right, #f3e5f5, #e1f5fe); 
color: #4a148c; 
  padding: 12px 16px;
  font-size: 18px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 12px 12px 0 0;
}

.chat-header button {
  background-color: transparent;
  border: none;
  color: #00695c; 


  font-size: 20px;
  cursor: pointer;
  transition: color 0.3s;
}

.chat-header button:hover {
  color: #ffc107;
}

.chat-content {
  flex-grow: 1;
  padding: 10px 16px;
  background-color: #f9f9f9;
  overflow-y: auto;
  font-size: 14px;
  color: #333;
  line-height: 1.5;
}

.chat-content .user-message {
  background-color: #e3f2fd;
  color: #1a73e8;
  border-radius: 15px;
  padding: 8px 12px;
  margin: 5px 0;
  max-width: 80%;
}

.chat-content .assistant-message {
  background-color: #f1f1f1;
  color: #333;
  border-radius: 15px;
  padding: 8px 12px;
  margin: 5px 0;
  max-width: 80%;
  align-self: flex-start;
}

.chat-input {
  display: flex;
  padding: 12px;
  background-color: #fff;
  border-top: 1px solid #ddd;
  border-radius: 0 0 12px 12px;
}

.chat-input .el-input {
  flex-grow: 1;
  border-radius: 18px;
  padding: 12px;
  border: 1px solid #ddd;
  font-size: 14px;
  background-color: #f1f1f1;
  transition: border 0.3s;
}

.chat-input .el-input:focus {
  border-color: #007bff;
  box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
}

.chat-input .el-button {
background: linear-gradient(to bottom right, #f3e5f5, #e1f5fe); 
  color: #4a148c; 
  padding: 10px 20px;
  font-size: 14px;
  margin-left: 10px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
}

.chat-input .el-button:hover {
  background-color: #0056b3;
}

.chat-input .el-button:disabled {
  background-color: #b0c4de;
  cursor: not-allowed;
}

.chat-input .el-button {
  margin-left: 10px;
}
</style>
  