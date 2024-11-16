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
        <div class="comment-header">
          <p>
            <strong>{{ comment.fullName || "Loading..." }}:</strong>
            {{ comment.message }}
          </p>
          <span class="timestamp">
            {{ new Date(comment.createdAt).toLocaleString() }}
          </span>
        </div>
        <div class="comment-actions">
          <img
              src="/assets/Icons/reply-icon.png"
              alt="Reply"
              class="reply-button"
              @click="replyToComment(comment)"
          />
        </div>
        <div v-if="comment.replyId" class="reply">
          <p><strong>Reply to @{{ comment.userId === comment.replyId ? comment.fullName: comment.replyId }}:</strong></p>
          <p>{{ getCommentById(comment.replyId)?.message || "Unknown" }}</p>
        </div>
      </div>
    </div>

    <!-- Reply Form -->
    <div v-if="replyingTo" class="reply-form">
      <h3>Replying to @{{ replyingTo.fullName }}</h3>
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
      <h3>Leave a Comment</h3>
      <textarea
          v-model="newCommentMessage"
          placeholder="Enter your comment here"
      ></textarea>
      <button class="submit-button" @click="submitComment">
        Submit Comment
      </button>
    </div>
  </div>
</template>


<script>
import axiosInstances from "@/services/axiosInstance";

export default {
  props: {
    studentId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      comments: [],
      courseId: parseInt(this.$route.params.courseId),
      replyingTo: null, // Will hold the comment object being replied to
      newReplyMessage: "",
      newCommentMessage: "",
      userMap: {}, // Cache for userId -> username mappings
    };
  },
  created() {
    this.fetchComments();
  },
  methods: {
    replyToComment(comment) {
      this.replyingTo = comment; // Store the comment being replied to
    },
    cancelReply() {
      this.replyingTo = null; // Reset reply mode
      this.newReplyMessage = ""; // Clear input area
    },
    async fetchComments() {
      try {
        const response = await axiosInstances.axiosInstance.get(`http://localhost:8081/api/comments/${this.courseId}`);
        this.comments = response.data;
        console.log("Hello", response.data)
        await this.fetchUsernames(); // Fetch usernames after loading comments
      } catch (error) {
        console.error("Error fetching comments:", error);
      }
    },
    async fetchUsernames() {
      try {
        // Extract unique userIds from comments
        const userIds = [...new Set(this.comments.map((comment) => comment.userId))];

        for (const userId of userIds) {
          if (!this.userMap[userId]) {
            const response = await axiosInstances.axiosInstance.get(`http://localhost:8081/api/users/${userId}`);
            // Directly assign the username to userMap
            this.userMap[userId] = response.data.fullName; // Assuming fullName is the field in the response
          }
        }
      } catch (error) {
        console.error("Error fetching usernames:", error);
      }
    },

    async submitComment() {
      try {
        const newComment = {
          userId: parseInt(this.studentId),
          message: this.newCommentMessage,
          replyId: null,
          courseId: this.courseId,
        };
        const response = await axiosInstances.axiosInstance.post("http://localhost:8081/api/comments", newComment);
        this.comments.push(response.data);
        await this.fetchUsernames(); // Update usernames after adding a comment
        this.newCommentMessage = "";
      } catch (error) {
        console.error("Error posting comment:", error);
      }
    },
    async submitReply() {
      try {
        const newComment = {
          commentId: this.replyingTo.commentId,
          message: this.newReplyMessage,
          userId: this.studentId,
          courseId: this.courseId,
        };
        const response = await axiosInstances.axiosInstance.post("http://localhost:8081/api/comments/reply", newComment);
        this.comments.push(response.data);
        await this.fetchUsernames(); // Update usernames after adding a comment
        this.newCommentMessage = "";
      } catch (error) {
        console.error("Error posting comment:", error);
      }
    },
    getCommentById(commentId) {
      return this.comments.find((comment) => comment.commentId === commentId);
    },
    getUsername(userId) {
      return this.userMap[userId] || "Loading..."; // Return "Loading..." if username isn't ready
    },
  },
};
</script>


<style scoped>
.comment-section {

  width: 80%;
  margin: 0 auto;
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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

textarea {
  width: 100%;
  height: 80px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 8px;
  font-size: 14px;
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
  margin-left: 10px;
  margin-right: 10px;
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
</style>