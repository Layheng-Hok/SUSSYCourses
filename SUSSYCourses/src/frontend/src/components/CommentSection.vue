<template>
  <div class="comment-section">
    <!-- Total Comments -->
    <h2>Total Comments: {{ comments.length }}</h2>

    <!-- Comments Box -->
    <div class="comments-box">
      <div
        v-for="comment in comments"
        :key="comment.commentId"
        class="comment"
      >
        <!-- Comment Header -->
        <div class="comment-header">
          <p>
            <img
              :src="comment.commentProfilePictureUrl || defaultProfilePic"
              class="head-shot"
              alt="Profile Picture"
            />
            <strong>{{ comment.commentFullName || "Anonymous" }}:</strong>
            {{ comment.commentMessage }}
          </p>
          <span class="timestamp">
            {{ formatDate(comment.commentCreatedAt) }}
          </span>
        </div>

        <!-- Comment Attachment -->
       <div v-if="comment.commentAttachment" class="comment-attachment">
        <template v-if="['jpg', 'png', 'jpeg', 'gif'].includes(comment.commentAttachmentFileType)">
        <img :src="comment.commentAttachment" alt="Image Attachment" class="attachment-image" />
        </template>
          <template v-else-if="comment.commentAttachmentFileType === 'mp4'">
            <video controls :src="comment.commentAttachment" class="attachment-video"></video>
          </template>
          <template v-else-if="comment.commentAttachmentFileType === 'pdf'">
          <div class="attachment-box">
            <div class="icon-container">
              <img src="/assets/Icons/pdf-icon.svg" alt="PDF Icon" class="pdf-icon" />
            </div>
            <div class="file-info">
              <p>{{ comment.commentAttachmentName || "PDF File" }}</p>
            </div>
            <div class="expand-container">
              <button @click="openInNewTab(comment.commentAttachment)" class="expand-button">
                View
              </button>
            </div>
          </div>
        </template>
          <template v-else>
            <div class="generic-attachment">
              <p>
                <strong>Attachment:</strong> {{ comment.commentAttachmentName || "Unknown File" }}
              </p>
              <a :href="comment.commentAttachment" target="_blank" class="attachment-link">
                Download
              </a>
            </div>
          </template>
        </div>

        <!-- Reply Section -->
        <div v-if="comment.replyToId" class="reply">
          <div class="reply-header">
            <p>
              <img
                :src="comment.replyToProfilePictureUrl || defaultProfilePic"
                class="head-shot"
                alt="Reply Profile Picture"
              />
              <strong>Reply to {{ comment.replyToFullName || "Anonymous" }}:</strong>
            </p>
          </div>
          <p class="reply-message">{{ comment.replyToMessage || "Unknown" }}</p>

        <!-- Reply Attachment -->
        <div v-if="comment.replyToAttachment" class="reply-attachment">
            <template v-if="comment.replyToAttachmentFileType === 'jpg'">
              <img :src="comment.replyToAttachment" alt="Image Attachment" class="attachment-image" />
            </template>
            <template v-else-if="comment.replyToAttachmentFileType === 'mp4'">
              <video controls :src="comment.replyToAttachment" class="attachment-video"></video>
            </template>
            <template v-else-if="comment.replyToAttachmentFileType === 'pdf'">
              <iframe :src="comment.replyToAttachment" class="attachment-pdf"></iframe>
            </template>
            <template v-else>
              <div class="generic-attachment">
                <p>
                  <strong>Attachment:</strong> {{ comment.replyToAttachmentName || "Unknown File" }}
                </p>
                <a :href="comment.replyToAttachment" target="_blank" class="attachment-link">
                  Download
                </a>
              </div>
            </template>
          </div>
        </div>

        <!-- Reply Action -->
        <div class="comment-actions">
          <img
            src="/assets/Icons/reply-icon.svg"
            alt="Reply"
            class="reply-button"
            @click="replyToComment(comment)"
          />
        </div>
      </div>
    </div>

    <!-- Reply Form -->
    <div v-if="replyingTo" class="reply-form">
      <h3>Replying to @{{ replyingTo.commentFullName }}</h3>
      <textarea
        v-model="newReplyMessage"
        placeholder="Enter your reply here"
      ></textarea>
      <div>
        <button class="submit-button" @click="submitReply">Submit Reply</button>
        <button class="cancel-button" @click="cancelReply">Cancel</button>
      </div>
    </div>

    <!-- New Comment Section -->
    <div class="new-comment-form" v-else>
      <h2>Leave a Comment</h2>
      <div class="textarea-container">
        <!-- Textarea -->
        <textarea
          v-model="newCommentMessage"
          placeholder="Enter your comment here"
        ></textarea>

        <!-- Icons and Attachment Display -->
        <div class="icon-container">
          <!-- Attachment Icon -->
          <div class="attachment-section">
            <label for="attachment" class="attachment-label">
              <img
                src="/assets/Icons/clip-icon.svg"
                alt="Attach File"
                class="clip-icon"
              />
            </label>
            <input
              type="file"
              id="attachment"
              class="attachment-input"
              @change="handleAttachment"
              accept=".jpg,.jpeg,.png,.gif,.pdf,.mp4"
            />
            <span v-if="attachmentName" class="attachment-name">
              {{ attachmentName }} 
              <button class="remove-attachment" @click="removeAttachment">X</button>
            </span>
          </div>

          <!-- Emoji Picker Icon -->
          <img
            src="/assets/Icons/emoji-icon.svg"
            alt="Emoji Picker"
            class="emoji-icon"
            @click="toggleEmojiPicker"
          />
        </div>
      </div>

      <!-- Emoji Picker -->
      <emoji-picker
        v-if="showEmojiPicker"
        class="emoji-picker"
        @emoji-click="addEmoji"
      ></emoji-picker>

      <!-- Submit Button -->
      <button class="submit-button" @click="submitComment">
        Submit Comment
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import 'emoji-picker-element';
import axiosInstances from "@/services/axiosInstance";

const route = useRoute();
const userId = ref(localStorage.getItem("userId"));
const courseId = route.params.courseId;
const comments = ref([]);
const replyingTo = ref(null);
const newReplyMessage = ref("");
const newCommentMessage = ref("");
const defaultProfilePic = ref("/assets/Avatars/student.jpg");
const attachment = ref(null);
const attachmentName = ref("");
const showEmojiPicker = ref(false);

const handleAttachment = (event) => {
  const file = event.target.files[0];
  if (file) {
    attachment.value = file;
    attachmentName.value = file.name;
  }
};

const openInNewTab = (url) => {
  window.open(url, "_blank");
};

// Fetch Comments
const fetchComments = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(
      `users/${userId.value}/courses/${courseId}/comments`
    );
    comments.value = response.data.map((comment) => ({
      commentId: comment.commentId,
      commentFullName: comment.commentFullName || "Anonymous",
      commentProfilePictureUrl: comment.commentProfilePictureUrl,
      commentMessage: comment.commentMessage,
      commentAttachment: comment.commentAttachment,
      commentAttachmentName: comment.commentAttachmentName,
      commentAttachmentFileType: comment.commentAttachmentFileType,
      commentCreatedAt: comment.commentCreatedAt,
      replyToId: comment.replyToId,
      replyToFullName: comment.replyToFullName,
      replyToProfilePictureUrl: comment.replyToProfilePictureUrl,
      replyToMessage: comment.replyToMessage,
      replyToAttachment: comment.replyToAttachment,
      replyToAttachmentName: comment.replyToAttachmentName,
      replyToAttachmentFileType: comment.replyToAttachmentFileType,
    }));
  } catch (error) {
    console.error("Error fetching comments:", error);
  }
};

const submitComment = async () => {
  if (!newCommentMessage.value.trim()) {
    alert("Comment cannot be empty.");
    return;
  }
  try {
    const formData = new FormData();
    formData.append("userId", parseInt(userId.value));
    formData.append("message", newCommentMessage.value);
    formData.append("replyId", null);
    formData.append("courseId", courseId);
    if (attachment.value) {
      formData.append("attachment", attachment.value);
    }
    
    await axiosInstances.axiosInstance.post(
      "http://localhost:8081/api/comments",
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
      }
    );
    await fetchComments(); // Refresh comments
    newCommentMessage.value = "";
    attachment.value = null;
    attachmentName.value = "";
  } catch (error) {
    console.error("Error posting comment:", error);
  }
};

// Cancel Reply
const cancelReply = () => {
  replyingTo.value = null;
  newReplyMessage.value = "";
};

// Format Date
const formatDate = (isoDate) => {
  const date = new Date(isoDate);
  return date.toLocaleString();
};

// Reply to Comment
const replyToComment = (comment) => {
  replyingTo.value = comment;
};

const removeAttachment = () => {
  attachment.value = null;
  attachmentName.value = "";
};

const toggleEmojiPicker = () => {
  showEmojiPicker.value = !showEmojiPicker.value;
};

const addEmoji = (event) => {
  newCommentMessage.value += event.detail.unicode;
};

// Fetch comments on component mount
onMounted(() => {
  fetchComments();
});
</script>


<style scoped>
.comment-section {
  width: 90%;
  margin: 0 auto;
  background: #f9f9f9;
  padding: 10px;
  border-radius: 8px;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
}

h2 {
  margin-bottom: 20px;
  color: #333;
}

.comments-box {
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 8px;
  max-height: 400px;
  overflow-y: auto;
  background: #fff;
}

.comment {
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-actions {
  margin-top: 8px;
}

.reply-button {
  cursor: pointer;
  width: 24px;
  height: 24px;
}

.reply {
  margin-left: 20px;
  padding: 8px;
  background: #f1f1f1;
  border-radius: 4px;
}

.reply-form,
.new-comment-form {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
}

.comment-attachment,
.reply-attachment {
  margin-top: 10px;
  padding: 10px;
}

.attachment-image {
  max-width: 80%;
  height: auto;
  border-radius: 5px;
}

.attachment-video {
  max-width: 80%;
  height: auto;
  border-radius: 5px;
}

.attachment-pdf {
  width: 80%;
  height: 300px;
  border: none;
}

.generic-attachment {
  display: flex;
  flex-direction: column;
  align-items: start;
  gap: 5px;
}

.attachment-link {
  color: #007bff;
  text-decoration: underline;
  cursor: pointer;
  font-weight: bold;
}

.attachment-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(0, 0, 0, 0.05);
  border: 1px solid #ddd;
  border-radius: 12px; 
  padding: 10px 15px;
  height: 50px;
  width: 100%;
  max-width: 270px; 
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}

.icon-container {
  flex: 0 0 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pdf-icon {
  width: 30px;
  height: 30px;
}

.file-info {
  flex: 1;
  padding: 0 10px;
  font-size: 16px;
  color: #333;
}

.expand-container {
  flex: 0 0 auto;
}

.expand-button {
  background: #007bff;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 5px 15px;
  font-size: 14px;
  cursor: pointer;
  font-family: 'Arial', sans-serif;
  transition: all 0.3s ease;
}

.expand-button:hover {
  background: #0056b3;
}

.head-shot{
  width: 20px;
  height: 20px;
  border-radius: 50%;
  object-fit: cover;
  vertical-align: middle;
  margin-right: 5px;
}

textarea {
  width: 90%;
  height: 80px;
  flex: 1;
  padding: 10px;
  margin-right: 20px;
  resize: none;
  font-size: 16px;
  border-radius: 5px;
  margin-bottom: 20px;
  border: 1px solid #ddd;
  margin: 0 auto;
}

.textarea-container {
  display: flex;
  align-items: flex-start; 
  position: relative;
}

.submit-button, .cancel-button {
  background: #007bff;
  color: #fff;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
  font-size: 16px;
  width: 160px;
  height: auto;
  margin: 10px;
  margin-top: 20px;
}
.cancel-button {
  background-color: #f8d7da;
  border: 1px solid #f5c6cb;
  color: #721c24;
}

.submit-button:hover {
  background: #0056b3;
}
.cancel-button:hover {
  opacity: 0.8;
}

.icon-container {
  display: flex;
  flex-direction: column; 
  align-items: center;
  gap: 10px;
  position: relative;
  margin-left: 10px;
}

.clip-icon,
.emoji-icon {
  width: 24px;
  height: 24px;
  cursor: pointer;
}

.attachment-input {
  display: none;
}

.attachment-section {
  display: flex;
  align-items: center;
  margin-top: 15px;
}

.attachment-label {
  cursor: pointer;
}

.attachment-name {
  font-size: 14px;
  color: #555;
  margin-left: 5px;
}

.emoji-picker {
  margin-top: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 10px;
  width: 100%;
  max-width: 300px;
}

.remove-attachment{
  margin-left: 5px;
}

</style>