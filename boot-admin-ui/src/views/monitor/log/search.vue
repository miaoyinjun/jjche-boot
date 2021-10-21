<template>
  <div v-if="crud.props.searchToggle">
    <el-input
      v-model="query.blurry"
      clearable
      size="small"
      placeholder="请输入你要搜索的内容"
      style="width: 200px;"
      class="filter-item"
    />
    <date-range-picker v-model="query.gmtCreate" class="date-item" />
    <el-select v-model="query.module" clearable size="small" placeholder="模块" class="filter-item" style="width: 90px">
      <el-option v-for="item in logModuleOptions" :key="item" :label="item" :value="item" />
    </el-select>
    <el-select v-model="query.logType" clearable size="small" placeholder="类型" class="filter-item" style="width: 90px">
      <el-option v-for="item in logTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
    </el-select>
    <el-select v-model="query.category" clearable size="small" placeholder="分类" class="filter-item" style="width: 90px">
      <el-option v-for="item in categoryOptions" :key="item.key" :label="item.display_name" :value="item.key" />
    </el-select>    
    <el-select v-model="query.isSuccess" clearable size="small" placeholder="状态" class="filter-item" style="width: 90px">
      <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
    </el-select>
    <rrOperation />
  </div>
</template>

<script>
import { header } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import DateRangePicker from '@/components/DateRangePicker'
import { getModules } from '@/api/monitor/log'
export default {
  components: { rrOperation, DateRangePicker },
  mixins: [header()],
    data() {
      return {
        statusOptions: [
          { key: 1, display_name: '成功' },
          { key: 0, display_name: '失败' }
        ],
        logTypeOptions: [
          { key: 'ADD', display_name: '新增' },
          { key: 'SELECT', display_name: '查询' },
          { key: 'UPDATE', display_name: '更新' },
          { key: 'DELETE', display_name: '删除' }
        ],
        categoryOptions: [
          { key: 'MANAGER', display_name: '管理员' },
          { key: 'OPERATING', display_name: '运营' },
          { key: 'OTHER', display_name: '其它' }
        ],
        logModuleOptions: []
      }
    },
  mounted() {
    this.getModuleData()
  },
  methods: {
    getModuleData() {
        getModules().then(res => {
          this.logModuleOptions = res
        }).catch(err => {
          console.log(err.response.data.message)
        })
    }
  }
}
</script>
