<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { latest } from '@/api/system/version'
import store from '@/store'
export default {
  name: 'App',
    mounted() {
      latest().then(res => {
        store.state.settings.versionNumber = res
      })
  }
}
</script>
