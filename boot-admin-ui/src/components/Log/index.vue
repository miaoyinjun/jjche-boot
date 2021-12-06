<template>
  <div>
    <div>
      <!--表格渲染-->
      <el-table stripe ref="table" v-loading="crud.loading" :data="crud.data" highlight-current-row style="width: 100%;"
                @selection-change="crud.selectionChangeHandler">
        <el-table-column :show-overflow-tooltip="true" prop="description" label="描述" />
        <el-table-column :show-overflow-tooltip="true" prop="detail" label="详情" />
        <el-table-column prop="gmtCreate" label="操作时间" width="180px" />
        <el-table-column prop="username" label="操作人" />
      </el-table>
      <!--分页组件-->
      <pagination/>
    </div>
  </div>
</template>

<script>
import crudLog from '@/api/monitor/log'
import Crud, { presenter, header, form } from '@crud/crud'
import pagination from '@crud/Pagination'
import rrOperation from '@crud/RR.operation'
import udOperation from '@crud/UD.operation'
import crudOperation from '@crud/CRUD.operation'

export default {
  methods: {
  },
  components: { pagination, rrOperation, udOperation, crudOperation },
  cruds() {
    return [
      Crud({
        title: '日志', url: 'sys/logs/biz', query: { bizKey: '', bizNo: '' },
        crudMethod: { ...crudLog },
        optShow: {
          add: false,
          edit: false,
          del: false,
          reset: false
        },
        queryOnPresenterCreated: false
      })
    ]
  },
  mixins: [
    presenter(),
    header(),],
  data() {
    return {
      rules: {
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
