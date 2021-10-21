<template>
  <div class="app-container">
    <!-- 菜单列表 -->
    <el-row :gutter="10">
      <el-col :xs="24" :sm="24" :md="10" :lg="11" :xl="11" style="margin-bottom: 10px">
        <el-card class="box-card">
          <!--表格渲染-->
          <el-table stripe ref="table" v-loading="crud.loading" :data="crud.data" highlight-current-row style="width: 100%;" @selection-change="crud.selectionChangeHandler" @current-change="handleCurrentChange">
            <el-table-column :show-overflow-tooltip="true" prop="title" label="菜单" />
            <el-table-column :show-overflow-tooltip="true" prop="fields" label="字段" />
          </el-table>
          <!--分页组件-->
          <pagination />
        </el-card>
      </el-col>
      <!-- 数据列详情 -->
      <el-col :xs="24" :sm="24" :md="14" :lg="13" :xl="13">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>列数据详情</span>
            <el-button
              v-if="checkPermission(['admin','data_permission:add']) && this.$refs.permissionDataDetail && this.$refs.permissionDataDetail.query.menuId"
              class="filter-item"
              size="mini"
              style="float: right;padding: 4px 10px"
              type="primary"
              icon="el-icon-plus"
              @click="$refs.permissionDataDetail && $refs.permissionDataDetail.crud.toAdd()"
            >新增</el-button>
          </div>
          <permissionDataDetail ref="permissionDataDetail" :permission="permission" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import permissionDataDetail from './permissionDataDetail'

import Crud, { presenter, header } from '@crud/crud'
import pagination from '@crud/Pagination'

export default {
  name: 'DataPermission',
  components: { pagination, permissionDataDetail },
  cruds() {
    return [
      Crud({
        title: '数据权限',
        url: 'admin/menus/page',
        params: { type: 1 },
        sort: ['menuSort asc']
      })
    ]
  },
  mixins: [presenter(), header()],
  data() {
    return {
      queryTypeOptions: [],
      rules: {},
      permission: {}
    }
  },
  methods: {
    checkPermission,
    // 获取数据前设置好接口地址
    [Crud.HOOK.beforeRefresh]() {
      if (this.$refs.permissionDataDetail) {
        this.$refs.permissionDataDetail.query.menuId = ''
      }
      return true
    },
    // 选中字典后，设置字典详情数据
    handleCurrentChange(val) {
      if (val) {
        this.$refs.permissionDataDetail.query.menuId = val.id
        this.$refs.permissionDataDetail.menuName = val.label
        this.$refs.permissionDataDetail.crud.toQuery()
      }
    }
  }
}
</script>

<style scoped>
</style>
