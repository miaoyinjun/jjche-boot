<!--分页-->
<template>
  <el-pagination
    background
    :page-size.sync="page.pageSize"
    :total="page.total"
    :current-page.sync="page.pageIndex"
    style="margin-top: 8px;"
    layout="sizes, total,prev, pager, next"
    @size-change="crud.sizeChangeHandler($event)"
    @current-change="crud.pageChangeHandler"
  />
</template>
<script>
import { pagination } from '@crud/crud'
export default {
  mixins: [pagination()]
}
</script>
