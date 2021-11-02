<template>
  <div>
    <div>
      <el-button
        class="filter-item"
        size="mini"
        style="float: right;margin-right: 10px;padding: 4px 10px"
        type="primary"
        icon="el-icon-check"
        :loading="dataPermissionRuleLoading"
        @click="saveChecked"
      >保存
      </el-button>
      <!--表格渲染-->
      <el-table stripe ref="table" v-loading="crud.loading" :data="crud.data" highlight-current-row style="width: 100%;"
                @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="name" label="名称"/>
      </el-table>
      <!--分页组件-->
      <pagination/>
    </div>
  </div>
</template>

<script>
import crudDataPermissionRule from '@/api/system/dataPermissionRuleRole'
import Crud, { presenter, header } from '@crud/crud'
import pagination from '@crud/Pagination'

export default {
  components: { pagination },
  dicts: ['dataPermissionRule_condition'],
  cruds() {
    return [
      Crud({
        title: '数据规则详情',
        url: 'admin/sys_data_permission_rule_roles',
        query: { menuId: '', roleId: '' },
        sort: ['id DESC'],
        crudMethod: { ...crudDataPermissionRule },
        optShow: {
          add: true,
          edit: true,
          del: true,
          reset: false
        },
        queryOnPresenterCreated: false
      })
    ]
  },
  mixins: [
    presenter(),
    header()
  ],
  data() {
    return {
      dataPermissionRuleLoading: false
    }
  },
  methods: {
    saveChecked() {
      this.dataPermissionRuleLoading = true
      const ids = []
      this.crud.selections.forEach(val => {
        ids.push(this.crud.getDataId(val))
      })
      const params = { roleId: this.query.roleId, menuId: this.query.menuId, dataPermissionRuleIdList: ids }
      crudDataPermissionRule.add(params).then(() => {
        this.crud.notify('保存成功', Crud.NOTIFICATION_TYPE.SUCCESS)
        this.dataPermissionRuleLoading = false
      }).catch(err => {
        this.dataPermissionRuleLoading = false
        console.log(err.response.data.message)
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
