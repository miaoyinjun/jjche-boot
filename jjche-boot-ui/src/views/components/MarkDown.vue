<template>
  <div class="app-container">
    <p class="warn-content">
      Markdown 基于
      <el-link
        type="primary"
        href="https://github.com/hinesboy/mavonEditor"
        target="_blank"
      >MavonEditor</el-link>
    </p>
    <mavon-editor ref="md" :style="'height:' + height" @imgAdd="imgAdd" />
  </div>
</template>

<script>
import { upload } from '@/utils/upload'
import { mapGetters } from 'vuex'
export default {
  name: 'Markdown',
  data() {
    return {
      height: document.documentElement.clientHeight - 200 + 'px'
    }
  },
  computed: {
    ...mapGetters(['imagesUploadApi', 'baseApi'])
  },
  mounted() {
    const that = this
    window.onresize = function temp() {
      that.height = document.documentElement.clientHeight - 200 + 'px'
    }
  },
  methods: {
    imgAdd(pos, $file) {
      upload(this.imagesUploadApi, $file).then((res) => {
        const url = res.data.data[0].path
        this.$refs.md.$img2Url(pos, url)
      })
    }
  }
}
</script>

<style scoped></style>
