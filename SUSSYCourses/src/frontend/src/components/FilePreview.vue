<template>
  <div>
  </div>
  <div v-if="material.fileType === 'mp4'" class="video-container">
    <video
     :id="`videoPlayer-${material.coursewareId}`"
        @play="onPlay(material.coursewareId)"
      @ended="markAsCompleted(material.coursewareId)"
      controls
      :src="material.url"
      controlsList="nodownload"
      class="video-player"
    ></video>
    <button v-if="material.downloadable" @click="downloadFile" class="download-button" style="margin-top: 1em;">
        Download
      </button>
    </div>

  <div v-else class="attachment-box">
    <div class="icon-container">
      <img :src="icon" :alt="`${material.fileType} icon`" class="file-icon" />
    </div>

    <div class="file-info">
      <p>{{ material.fileName || `${material.fileType.toUpperCase()} File` }}</p>
    </div>

    <div class="expand-container">
      <button @click="viewFile(material.coursewareId)" class="expand-button">
        View
      </button>

      <button v-if="material.downloadable" @click="downloadFile" class="download-button">
        Download
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed, defineProps } from 'vue';
import { marked } from 'marked';
import { useRouter } from 'vue-router';
import axiosInstances from '@/services/axiosInstance';

const router = useRouter();
const userId = localStorage.getItem('userId');
const props = defineProps({
  material: Object,
});

const forcePause = (coursewareId) => {
  const videos = document.querySelectorAll('video');
  videos.forEach((video) => {
    if(video.id !== `videoPlayer-${coursewareId}`){
      video.pause();
    }
  });
};

const onPlay = (coursewareId) => {
  forcePause(coursewareId);
  const video = document.querySelector(`#videoPlayer-${coursewareId}`);

  if (video) {
    console.log(`Video with coursewareId: ${coursewareId} started playing`);
  }
};

const icon = computed(() => {
  const fileType = props.material.fileType;
  switch (fileType) {
    case 'pdf':
      return '/assets/Icons/pdf-icon.svg';
    case 'pptx':
      return '/assets/Icons/pptx-icon.svg';
    case 'md':
      return '/assets/Icons/md-icon.svg';
    default:
      return '/assets/Icons/md-icon.svg';
  }
});

const downloadFile = () => {
  const fileUrl = props.material.url;
  if (!fileUrl) {
    console.error('No file URL found');
    return;
  }

  const link = document.createElement('a');
  link.href = fileUrl;
  link.download = props.title || 'file';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};


const viewFile = async (coursewareId) => {
  if (props.material.fileType === 'pdf') {
    openPdfInViewer(props.material.url);
  } else if (props.material.fileType === 'pptx') {
    openPptxInGoogleViewer(props.material.url);
  } else if (props.material.fileType === 'md') {
    viewMarkdownInNewTab(props.material.url);
  } else{
    window.open(props.material.url, '_blank');
  }

  markAsCompleted(coursewareId);
};

const markAsCompleted = async (coursewareId) => {
  try {
    const userId = localStorage.getItem('userId');

    const response = await axiosInstances.axiosInstance.put(`/students/${userId}/coursewares/${coursewareId}/completed`);
    await fetchUserData(); // refreshes points
    // window.location.reload();
    if (response.status === 200) {
      console.log(`Courseware ${coursewareId} marked as completed after viewing.`);
    }
  } catch (error) {
    console.error('Error marking courseware as completed:', error);
  }
};

const fetchUserData = async () => {
  try {
    await axiosInstances.axiosInstance.get(`students/${userId}`);
  } catch (error) {

    console.log("Error Details:", error);
    if (error.response && error.response.status === 403) {
      router.push({ name: 'ForbiddenPage' });
    } else {
      console.error("Unexpected error occurred:", error);
    }    
  }
};

const openPdfInViewer = (url) => {
  const pdfViewerUrl = `https://drive.google.com/viewerng/viewer?url=${encodeURIComponent(url)}`;
  window.open(pdfViewerUrl, '_blank');
};

const openPptxInGoogleViewer = (url) => {
  const googleSlidesUrl = `https://docs.google.com/viewer?url=${encodeURIComponent(url)}`;
  window.open(googleSlidesUrl, '_blank');
};

const viewMarkdownInNewTab = (url) => {
  fetch(url)
    .then(response => response.text())
    .then(markdown => {
      const htmlContent = marked(markdown);
      const newTab = window.open();
      newTab.document.write(htmlContent);
      newTab.document.close(); 
    })
    .catch(err => console.error('Error fetching markdown:', err));
};
</script>
  
  <style scoped>

.video-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin: 10px 0;
  max-width: 100%;
  width: 100%;
}

.video-player {
  width: 80%;
  height: 80%;
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
  max-width: 340px; 
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  margin: 20px auto;
}

.file-icon {
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

.expand-button, .download-button {
  background: #007bff;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 5px 15px;
  font-size: 14px;
  cursor: pointer;
  font-family: 'Arial', sans-serif;
  transition: all 0.3s ease;
  margin: 0 5px;
}

.expand-button:hover, .download-button:hover {
  background: #0056b3;
}

.download-button {
  background: #28a745;
}

.download-button:hover {
  background: #218838;
}
  </style>
  