<template>
  <div class="attachment-box">
    <div class="icon-container">
      <img :src="icon" :alt="`${type} icon`" class="file-icon" />
    </div>

    <div class="file-info">
      <p>{{ title || `${type.toUpperCase()} File` }}</p>
    </div>

    <div class="expand-container">
      <button @click="viewFile" class="expand-button">
        View
      </button>

      <button v-if="!isNonDownloadable" @click="downloadFile" class="download-button">
        Download
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed, defineProps } from 'vue';
import { marked } from 'marked';

const props = defineProps({
  type: {
    type: String,
    required: true,
  },
  title: {
    type: String,
    required: false,
    default: '',
  },
  url: {
    type: String,
    required: true,
  },
  isNonDownloadable: {
    type: Boolean,
    required: true,
    default: false,
  },
});

const icon = computed(() => {
  switch (props.type) {
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
  const link = document.createElement('a');
  link.href = props.url;
  link.download = props.title || 'file';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

const viewFile = () => {
  if (props.type === 'pdf') {
    openPdfInViewer(props.url);
  } else if (props.type === 'pptx') {
    openPptxInGoogleViewer(props.url);
  } else if (props.type === 'md') {
    viewMarkdownInNewTab(props.url);
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

.expand-button  {
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

.expand-button:hover  {
  background: #0056b3;
}

.download-button {
  background: #28a745;
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

.download-button:hover {
  background: #218838;
}

  </style>
  