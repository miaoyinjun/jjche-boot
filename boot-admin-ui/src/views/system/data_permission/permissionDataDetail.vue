<template>
  <div>
    <div v-if="query.menuId === ''">
      <div class="my-code">点击菜单查看详情</div>
    </div>
    <div v-else>
      <!--表单组件-->
      <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="字段" prop="code">
            <el-input v-model="form.code" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model.number="form.sort" :min="0" :max="999" controls-position="right" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table stripe ref="table" v-loading="crud.loading" :data="crud.data" highlight-current-row style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column label="所属菜单">
          {{ menuName }}
        </el-table-column>
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="code" label="字段" />
        <el-table-column prop="sort" label="排序" />
        <el-table-column v-permission="['admin','data_permission:edit','data_permission:del']" label="操作" width="130px" align="center" fixed="right">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudPermissionData from '@/api/system/permissionData'
import Crud, { presenter, header, form } from '@crud/crud'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'

const defaultForm = { id: null, name: null, code: null, sort: 999 }

export default {
  components: { pagination, udOperation },
  cruds() {
    return [
      Crud({
        title: '列数据详情', url: 'admin/dataPermission', query: { menuId: '' }, sort: ['id desc'],
        crudMethod: { ...crudPermissionData },
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
    header(),
    form(function() {
      return Object.assign({ menuId: this.query.menuId }, defaultForm)
    })],
  data() {
    return {
      menuName: null,
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入字段', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入排序', trigger: 'blur', type: 'number' }
        ]
      },
      permission: {
        add: ['admin', 'data_permission:add'],
        edit: ['admin', 'data_permission:edit'],
        del: ['admin', 'data_permission:del']
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
