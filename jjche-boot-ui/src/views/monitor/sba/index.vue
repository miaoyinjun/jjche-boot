<template>
  <elFrame :src="sbaApi" />
</template>
<script>
import { mapGetters } from 'vuex'
import elFrame from '@/components/Iframe/index'
export default {
  name: 'Sba',
  components: { elFrame },
  computed: {
    ...mapGetters(['sbaApi'])
  }
}
</script>
