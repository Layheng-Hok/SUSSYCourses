<template>
  <!-- Modal -->
  <div class="modal-backdrop" v-if="archiveDialogVisible">
    <div class="modal">
      <slot name="footer">
        <button class="close-button" @click="archiveDialogVisible = false">Close</button>
      </slot>
      <slot name="header">
        <h2>Courseware Details</h2>
      </slot>

      <slot name="body">
        <div v-for="(courseware, index) in selectedCourseware" :key="index"
             :class="['courseware-item', { 'selected-version': courseware.coursewareId === selectedCoursewareId }]">
          <!-- <div class="modal-item-left">
            <video controls :src="`${courseware.url}`" width="60%" class="modal-video-frame"></video>
          </div> -->
          <div class="modal-item-middle">
            <h3>Courseware {{ index + 1 }}</h3>
            <p><strong>Category:</strong> {{ courseware.category }}</p>
            <p><strong>Chapter:</strong> {{ courseware.chapter }}</p>
            <p><strong>Downloadable:</strong> {{ courseware.downloadable }}</p>
            <p><strong>Courseware Order:</strong> {{ courseware.coursewareOrder }}</p>
            <p><strong>Creation Time:</strong> {{ courseware.createTime }}</p>
            <p><strong>Display Version:</strong> {{ courseware.displayVersion }}</p>
            <p><strong>Downloadable:</strong> {{ courseware.downloadable }}</p>
            <p><strong>Variant Of:</strong> {{ courseware.variantOf }}</p>
          </div>
          <div class="modal-item-right">
            <button class="modal-set-button" @click="setActiveVersion(courseware)">Set as Active Version</button>
            <button class="modal-update-button" @click="openEditDialog(courseware.coursewareId)">Update Version</button>
          </div>
        </div>
      </slot>
    </div>
  </div>

  <!--Add Courseware Form -->
  <div class="update-form-modal-backdrop" v-if="addCoursewareModal">
    <div class="update-form-modal">
      <div class="update-form-top">
        <slot name="header">
          <h2>Add Courseware</h2>
        </slot>
      </div>

      <div class="update-form-bottom">
        <slot name="body">
          <form @submit.prevent="submitCourseware($props)">
            <!-- Category Field -->
            <div class="form-group">
              <label for="category">Category:</label>
              <select id="category" v-model="coursewareData.category" required>
                <option :value="'assignment'">Assignment</option>
                <option :value="'lecture'">Lecture</option>
                <option :value="'project'">Project</option>
                <option :value="'attachment'">Attachment</option>
              </select>
            </div>

            <!-- Downloadable Field -->
            <div class="form-group">
              <label for="downloadable">Downloadable:</label>
              <select id="downloadable" v-model="coursewareData.downloadable" required>
                <option :value="true">Yes</option>
                <option :value="false">No</option>
              </select>
            </div>

            <!-- Chapter Field -->
            <div class="form-group">
              <label for="chapter">Chapter:</label>
              <input type="number" id="chapter" v-model="coursewareData.chapter" required
                     placeholder="Enter chapter number"/>
            </div>

  

            <!-- File Upload Field -->
            <div class="form-group">
              <label for="file">Upload New File:</label>
              <input type="file" id="file" @change="handleFileChange"/>
            </div>

            <button type="submit" class="save-button">Save</button>
            <button type="button" class="close-button" @click="addCoursewareModal = false">Close</button>
          </form>
        </slot>
      </div>
    </div>

  </div>


  <!--Update Courseware Form -->
  <div class="update-form-modal-backdrop" v-if="editDialogVisible">
    <div class="update-form-modal">
      <div class="update-form-top">
        <slot name="header">
          <h2>Edit Courseware</h2>
        </slot>
      </div>

      <div class="update-form-bottom">
        <slot name="body">
          <form @submit.prevent="submitUpdate">
            <!-- Category Field -->
            <div class="form-group">
              <label for="category">Category:</label>
              <select id="category" v-model="updateData.category" required>
                <option :value="'assignment'">Assignment</option>
                <option :value="'lecture'">Lecture</option>
                <option :value="'project'">Project</option>
              </select>
            </div>

            <!-- Downloadable Field -->
            <div class="form-group">
              <label for="downloadable">Downloadable:</label>
              <select id="downloadable" v-model="updateData.downloadable" placeholder="Select if downloadable">
                <option :value="true">Yes</option>
                <option :value="false">No</option>
              </select>
            </div>

            <!-- Chapter Field -->
            <div class="form-group">
              <label for="chapter">Chapter:</label>
              <input type="number" id="chapter" v-model="updateData.chapter" required
                     placeholder="Enter chapter number"/>
            </div>

            <!-- Change File Checkbox -->
            <div class="form-group">
              <label for="changeFile">Change File:</label>
              <input type="checkbox" id="changeFile" v-model="updateData.changeFile"/>
            </div>

            <!-- File Upload Field -->
            <div class="form-group">
              <label for="file">Upload New File:</label>
              <input type="file" id="file" @change="handleFileChange"/>
            </div>

            <button type="submit" class="save-button">Save</button>
            <button type="button" class="close-button" @click="editDialogVisible = false">Cancel</button>
          </form>
        </slot>
      </div>
    </div>

  </div>


  <!-- Course Content Section with Interactive Media -->
  <el-card class="course-content" shadow="hover">
    <div class="course-content-header">
      <div class="course-content-left"></div>
      <div class="course-content-middle"><h2>Course Content</h2></div>
      <div class="course-content-right">
        <button type="button" class="add-button" @click="addCoursewareModal = true">Add</button>
      </div>
    </div>
    <el-collapse>
      <el-collapse-item title="Teaching Chapters" name="1">
        <div v-for="chapter in course.teachingChapters" :key="chapter.name" class="chapter">
          <h3>{{ chapter.name }}</h3>
          <el-list>
            <el-list-item v-for="material in chapter.materials" :key="material.url">
              <div class="courseware-icons">
                <el-icon class="archive-icon" @click="openArchiveModal(material)">
                  <MessageBox/>
                </el-icon>
                <el-icon class="edit-icon" @click="openEditDialog(material.coursewareId)">
                  <Edit/>
                </el-icon>
                <el-icon class="delete-icon">
                  <Delete/>
                </el-icon>
              </div>
              <a :href="`${material.url}`" target="_blank">
                <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;"/>
                {{ material.title }}
              </a>
            </el-list-item>
          </el-list>
        </div>
      </el-collapse-item>

      <el-collapse-item title="Homework Chapters" name="2">
        <div v-for="chapter in course.homeworkChapters" :key="chapter.name" class="chapter">
          <h3>{{ chapter.name }}</h3>
          <el-list>
            <el-list-item v-for="material in chapter.materials" :key="material.url">
              <div class="courseware-icons">
                <el-icon class="archive-icon" @click="openArchiveModal(material)">
                  <MessageBox/>
                </el-icon>
                <el-icon class="edit-icon" @click="openEditDialog(material.coursewareId)">
                  <Edit/>
                </el-icon>
                <el-icon class="delete-icon">
                  <Delete/>
                </el-icon>
              </div>
              <a :href="`${material.url}`" target="_blank">
                <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;"/>
                {{ material.title }}
              </a>
            </el-list-item>
          </el-list>
        </div>
      </el-collapse-item>

      <el-collapse-item title="Project Chapters" name="3">
        <div v-for="chapter in course.projectChapters" :key="chapter.name" class="chapter">
          <h3>{{ chapter.name }}</h3>
          <el-list>
            <el-list-item v-for="material in chapter.materials" :key="material.url">
              <div class="courseware-icons">
                <el-icon class="archive-icon" @click="openArchiveModal(material)">
                  <MessageBox/>
                </el-icon>
                <el-icon class="edit-icon" @click="openEditDialog(material.coursewareId)">
                  <Edit/>
                </el-icon>
                <el-icon class="delete-icon">
                  <Delete/>
                </el-icon>
              </div>
              <a :href="`${material.url}`" target="_blank">
                <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;"/>
                {{ material.title }}
              </a>
            </el-list-item>
          </el-list>
        </div>
      </el-collapse-item>

      <el-collapse-item title="Attachments" name="4">
        <div v-for="chapter in course.attachments" :key="chapter.name" class="chapter">
          <el-list>
            <el-list-item v-for="material in chapter.materials" :key="material.url">
              <div class="courseware-icons">
                <el-icon class="archive-icon" @click="openArchiveModal(material)">
                  <MessageBox/>
                </el-icon>
                <el-icon class="edit-icon" @click="openEditDialog(material.coursewareId)">
                  <Edit/>
                </el-icon>
                <el-icon class="delete-icon">
                  <Delete/>
                </el-icon>
              </div>
              <a :href="`${material.url}`" target="_blank">
                <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;"/>
                {{ material.title }}
              </a>
            </el-list-item>
          </el-list>
        </div>
      </el-collapse-item>
    </el-collapse>
  </el-card>

  <el-dialog
      title="Archive Courseware"
      v-model:visible="archiveDialogVisible"
      width="100%"
      @close="handleCloseArchiveModal"
  >
    <div class="archive-content">
      <p>Are you sure you want to archive this courseware?</p>
      <div>
        <strong>{{ selectedCourseware.name }}</strong>
        <p>Version: {{ selectedCourseware.version }}</p>
        <p>URL: <a :href="selectedCourseware.url" target="_blank">{{ selectedCourseware.url }}</a></p>
      </div>
    </div>
    <template #footer>
      <el-button @click="archiveDialogVisible = false">Cancel</el-button>
      <el-button type="primary" @click="handleArchive">Archive</el-button>
    </template>
  </el-dialog>
</template>

<script>
import {onMounted, ref} from 'vue';
import {DataBoard, Delete, Document, Edit, Files, MessageBox, VideoCamera} from '@element-plus/icons-vue';
import axiosInstances from "@/services/axiosInstance";
import {useRouter} from "vue-router";

const userId = localStorage.getItem("userId")
const router = useRouter();
export default {
  data() {
    return {
      storeCourseId: null,
      selectedCoursewareId: null,
      archiveDialogVisible: false,
      editDialogVisible: false,
      addCoursewareModal: false,
      coursewareData: {
        courseId: null,
        fileType: '',
        category: null,
        downloadable: null,
        chapter: null,
        order: null,
        variant_of: null,
        version: null,
      },
      updateData: {
        coursewareId: null,
        courseId: '',
        fileType: '',
        category: null,
        downloadable: null,
        chapter: null,
        order: null,
        variant_of: null,
        version: null,
        changeFile: false,
      },
      selectedFile: null,
      selectedCourseware: {},
    };
  },
  methods: {
    async openArchiveModal(courseware) {
      const variantOf = courseware.variantOf;
      const variants = await axiosInstances.axiosInstance.get(`courseware/${variantOf}/allVersions`);
      const activeVariant = variants.data.find(coursewareVersion => coursewareVersion.displayVersion === true);
      this.selectedCoursewareId = activeVariant.coursewareId
      this.selectedCourseware = variants.data;
      this.archiveDialogVisible = !(this.archiveDialogVisible);
    },
    deleteVersion(coursewareId) {
      console.log(`Delete version with ID: ${coursewareId}`);
    },
    async openEditDialog(coursewareId) {
      const data = await axiosInstances.axiosInstance.get(`courseware/${coursewareId}`);
      const courseware = data.data
      this.updateData.coursewareId = coursewareId;
      this.updateData.courseId = courseware.courseId;
      this.updateData.fileType = courseware.fileType;
      this.updateData.downloadable = courseware.downloadable;
      this.updateData.category = courseware.category;
      this.updateData.chapter = courseware.chapter;
      this.updateData.order = courseware.order;
      this.updateData.variant_of = courseware.variant_of;
      this.updateData.version = courseware.version;
      this.updateData.changeFile = false;
      this.editDialogVisible = true;
      console.log(this.updateData.variant_of)
    },
    handleFileChange(event) {
      this.selectedFile = event.target.files[0];
    },
    async submitUpdate() {
      const formData = new FormData();

      formData.append("coursewareId", this.updateData.coursewareId)
      formData.append("courseId", this.updateData.courseId)
      formData.append("fileType", this.updateData.fileType)
      formData.append("category", this.updateData.category)
      formData.append("downloadable", this.updateData.downloadable)
      formData.append("chapter", this.updateData.chapter)
      formData.append("order", 1)
      formData.append("variant_of", this.updateData.variant_of)
      formData.append("version", this.updateData.version)
      formData.append("changeFile", this.updateData.changeFile)
      if (this.updateData.changeFile) {
        formData.append('file', this.selectedFile || null);
      } else {
        formData.append('file', new Blob([], {type: 'application/octet-stream'}));
      }

      for (const [key, value] of formData.entries()) {
        console.log(`${key}: ${value}`);
      }
      await axiosInstances.axiosInstance.put(`courseware/update`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
    },
    async submitCourseware(props) {
      const formData = new FormData();
      formData.append("courseId", props.courseId)
      formData.append("fileType", this.selectedFile.name.slice(this.selectedFile.name.lastIndexOf('.') + 1));
      formData.append("fileName", this.selectedFile.name);
      formData.append("category", this.coursewareData.category)
      formData.append("downloadable", this.coursewareData.downloadable)
      formData.append("chapter", this.coursewareData.chapter)
      formData.append("order", 1)
      formData.append("variantOf", -1)
      formData.append("version", 1)
      formData.append('file', this.selectedFile);
      for (const [key, value] of formData.entries()) {
        console.log(`${key}: ${value}`);
      }
      const response = await axiosInstances.axiosInstance.post(`courseware/create`, formData)
      console.log(response)
    },
    async setActiveVersion(courseware) {
      this.selectedCoursewareId = courseware.coursewareId;
      await axiosInstances.axiosInstance.put(`courseware/${courseware.coursewareId}/setActive`);
    },
    handleCloseArchiveModal() {
      this.archiveDialogVisible = false;
    },
    handleArchive() {
      console.log('Archiving courseware:', this.selectedCourseware);
      this.archiveDialogVisible = false;
    },
  },
  watch: {
    'coursewareData.category': 'onCategoryChange'
  },
  name: "CourseContent",
  components: {MessageBox, Delete, Edit},
  props: {
    courseId: {
      type: String,
      required: true,
    },
  },
  setup(props) {
    const course = ref({
      teachingChapters: [],
      homeworkChapters: [],
      projectChapters: [],
      attachments: []
    });

    const materialIcon = (type) => {
      switch (type) {
        case "pdf":
          return Document;
        case "pptx":
          return DataBoard;
        case "mp4":
          return VideoCamera;
        default:
          return Files;
      }
    };
    onMounted(async () => {
      try {
        const response = await axiosInstances.axiosInstance.get(`/users/${userId}/courses/${props.courseId}/coursewares`);
        const coursesData = response.data
        console.log(coursesData)
        course.value = coursesData.find((c) => c.id === props.courseId);

        if (!course.value) {
          console.error("Course not found!");
        }
      } catch (error) {
        if (error.response && error.response.status === 403) {
          await router.push({name: 'ForbiddenPage'});
        } else if (error.response && error.response.status === 404) {
          await router.push({name: 'NotFound'});
        } else {
          console.error("Unexpected error occurred:", error);
        }
      }
    });

    return {
      course,
      materialIcon,
    };
  },
};
</script>

<style scoped>
.course-content {
  margin: 20px 0;
}

.chapter h3 {
  color: #333;
  margin: 10px 0;
}

.iframe-container {
  width: 50%;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  background-color: #eee;
  padding: 0;
  margin: 0;
  display: flex; /* Ensure no extra inline spacing */
  justify-content: center; /* Center iframe content if needed */
  align-items: center; /* Vertically align iframe content */
}

.iframe-viewer {
  width: 100%;
  height: 100%;
  border: none;
  margin: 0; /* Reset default margin */
  padding: 0; /* Reset default padding */
  display: block; /* Prevent inline gaps */
}

.video-frame {
  height: 300px;
  width: 50%;
  border-radius: 8px; /* Rounded video corners */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15); /* Subtle shadow for video */
}

.video-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin: 20px 0;
  padding: 15px;
  background-color: #f9f9f9; /* Light background for contrast */
  border: 1px solid #e0e0e0; /* Subtle border */
  border-radius: 8px; /* Rounded corners */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Light shadow */
  gap:20px;
}

.non-video-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin: 20px 0;
  padding: 15px;
  background-color: #f9f9f9; /* Light background for contrast */
  border: 1px solid #e0e0e0; /* Subtle border */
  border-radius: 8px; /* Rounded corners */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Light shadow */
  gap:20px;
}

.courseware-right-side {
  display: flex;
  width: 50%;
  flex-direction: column;
  justify-items: end;
}

.courseware-icons {
  display: flex;
  flex-direction: row;
  justify-content: end;
}

.edit-icon {
  padding-right: 10px;
  padding-left: 10px;
  color: #409eff;
}

.delete-icon {
  color: red;
}

.courseware-information {
  margin: 20px 0; /* Add spacing around the container */
  padding: 20px; /* Add internal padding */
  background-color: #ffffff; /* White background */
  border: 1px solid #e0e0e0; /* Subtle border */
  border-radius: 12px; /* Rounded corners */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Light shadow for depth */
  font-family: 'Arial', sans-serif; /* Clean font */
  max-width: 600px; /* Limit the container width */
  gap:20px
}

.courseware-information-header {
  font-size: 20px; /* Larger font size for the header */
  font-weight: bold; /* Make the header bold */
  color: #333; /* Darker text */
  margin-bottom: 15px; /* Space below the header */
  border-bottom: 2px solid #409eff; /* Underline with primary color */
  padding-bottom: 5px;
}

.courseware-info-item {
  margin: 10px 0; /* Space between items */
  display: flex; /* Use flex for alignment */
  justify-content: space-between; /* Spread label and value */
  align-items: center; /* Align vertically */
}

.courseware-info-label {
  font-weight: 600; /* Bold labels for emphasis */
  color: #555; /* Slightly darker text */
  flex-shrink: 0; /* Prevent shrinking */
  width: auto; /* Fixed width for alignment */
}

.courseware-info-value {
  font-weight: 400; /* Regular font for values */
  color: #333; /* Darker text */
  text-align: left;
  flex-grow: 1; /* Allow the value to grow */
}

.courseware-info-value a {
  color: #409eff; /* Primary color for links */
  text-decoration: none; /* Remove underline */
  transition: color 0.3s; /* Smooth color transition */
}

.courseware-info-value a:hover {
  color: #66b1ff; /* Slightly lighter color on hover */
}

.archive-icon {
  cursor: pointer;
  color: #ff6600; /* Example color for the archive icon */
  transition: color 0.3s;
}

.archive-icon:hover {
  color: #ff3300; /* Change color on hover */
}


.el-list-item {
  display: flex;
  align-items: center;
  margin: 10px 0;
}

.el-list-item a {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #409eff;
  transition: color 0.3s ease;
}

.el-list-item a:hover {
  color: #66b1ff;
}

/* Reduce the size of the inner collapse header */
.inner-collapse-item .el-collapse-item__header {
  font-size: 12px; /* Smaller font size */
  padding: 5px 10px; /* Reduce padding */
  margin-left: -10px; /* Move further to the left */
}

/* Adjust arrow alignment for inner collapse */
.inner-collapse-item .el-collapse-item__header .el-icon-arrow-right {
  font-size: 10px; /* Smaller arrow size */
  margin-right: 5px; /* Adjust spacing from text */
}

/* Add spacing between outer and inner collapse */
.inner-collapse {
  margin-top: 10px; /* Separate inner collapse from outer header */
}

.edit-button {
  margin-left: 10px;
  color: #409eff;
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: #ffffff;
  padding: 20px;
  border-radius: 8px;
  width: 100%;
  max-width: 1000px;
  max-height: 90%; /* Set maximum height */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  text-align: left;
  overflow-y: auto; /* Enable vertical scrolling */
}

.modal h2 {
  margin-bottom: 15px;
  font-size: 24px;
  color: #333333;
  position: sticky; /* Keeps header visible while scrolling */
  top: 0;
  background: #ffffff; /* Ensures header doesn't overlap text */
  padding: 10px 0;
  z-index: 10;
}

.courseware-item {
  margin-bottom: 20px;
  display: flex;
  flex-direction: row;
  background-color: #ffffff; /* White background */
  padding: 15px; /* Inner padding for spacing */
  border-radius: 8px; /* Rounded corners */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Subtle shadow for depth */
  border: 1px solid #dddddd; /* Light border for structure */
}

.modal-item-left {
  width: 50%;
  height: auto;
  border-radius: 0.3em;
  border-color: black;
}

.modal-item-middle {
  margin-left: 10px;
  width: 25%;
  padding: 20px; /* Add internal padding */
  background-color: #ffffff; /* White background */
  border: 1px solid #e0e0e0; /* Subtle border */
  border-radius: 12px; /* Rounded corners */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Light shadow for depth */
  font-family: 'Arial', sans-serif; /* Clean font */
  max-width: 600px; /* Limit the container width */
}

.modal-item-right {
  display: flex;
  width: 30%;
  flex-direction: column;
  gap: 50px;
  align-content: space-around;
  margin-left: 50px;
  padding: 20px; /* Add internal padding */
  font-family: 'Arial', sans-serif; /* Clean font */
  max-width: 600px; /* Limit the container width */
}


.modal-video-frame {
  border-radius: 8px; /* Rounded video corners */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15); /* Subtle shadow for video */
  width: 100%;
  height: 100%;
  margin-right: 10px;
}

.courseware-item h3 {
  margin-bottom: 10px;
  font-size: 20px;
  color: #444444;
}

.courseware-item p {
  margin: 5px 0;
  font-size: 16px;
  color: #555555;
}

.courseware-item a {
  color: #007BFF;
  text-decoration: none;
}

.courseware-item a:hover {
  text-decoration: underline;
}

.modal-delete-button,
.modal-update-button,
.modal-set-button {
  display: inline-block;
  padding: 16px 40px;
  width: 220px;
  font-size: 12px;
  font-weight: bold;
  color: #ffffff;
  text-align: center;
  text-decoration: none;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.modal-delete-button {
  background-color: #FF4D4D; /* Vibrant red */
}

.modal-delete-button:hover {
  background-color: #CC0000; /* Darker red */
  box-shadow: 0 6px 12px rgba(204, 0, 0, 0.3); /* Enhanced shadow */
}

.modal-update-button {
  background-color: #FFC107; /* Bright yellow */
}

.modal-update-button:hover {
  background-color: #E0A800; /* Darker yellow */
  box-shadow: 0 6px 12px rgba(224, 168, 0, 0.3); /* Enhanced shadow */
}

.modal-set-button {
  background-color: #28A745; /* Fresh green */
}

.modal-set-button:hover {
  background-color: #218838; /* Darker green */
  box-shadow: 0 6px 12px rgba(33, 136, 56, 0.3); /* Enhanced shadow */
}


.close-button {
  background-color: #007BFF;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
  align-self: flex-end;
}

.close-button:hover {
  background-color: #0056b3;
}

hr {
  border: none;
  border-top: 1px solid #dddddd;
  margin: 15px 0;
}

.update-form-modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.update-form-modal {
  background: #ffffff;
  padding: 20px;
  border-radius: 8px;
  width: 40%;
  max-width: 1000px;
  max-height: 40%; /* Set maximum height */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  text-align: left;
  overflow-y: auto;
}

.update-form-bottom {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
}

.form-group {
  margin-bottom: 15px;
  width: 40%;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.save-button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  margin-right: 10px;
}

.save-button:hover {
  background-color: #45a049;
}

/* Add shadow effect or highlight when a version is displayed */
.selected-version {
  box-shadow: 0px 4px 8px rgba(0, 128, 0, 0.7); /* Green shadow for selected version */
  border: 2px solid green; /* Optional: add border for better emphasis */
  background-color: rgba(0, 128, 0, 0.1); /* Optional: slightly change background color */
}

.courseware-item {
  transition: all 0.3s ease; /* Smooth transition for shadow and background */
}

.course-content-header {
  display: flex;
  flex-direction: row;
  gap: 10px;
}

.course-content-left {
  width: 33%;
}

.course-content-middle {
  width: 33%;
}

.course-content-right {
  display: flex;
  justify-content: right;
  width: 33%;
  align-items: center;
  align-content: center;
  margin-bottom: 15px;
}

.add-button {
  background-color: #007BFF;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
  align-self: flex-end;
}


</style>
