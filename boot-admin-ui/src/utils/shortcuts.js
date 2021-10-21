import Date from './datetime.js'

function dateFormat(fmt, date) {
  let ret
  const opt = {
    'Y+': date.getFullYear().toString(), // 年
    'm+': (date.getMonth() + 1).toString(), // 月
    'd+': date.getDate().toString(), // 日
    'H+': date.getHours().toString(), // 时
    'M+': date.getMinutes().toString(), // 分
    'S+': date.getSeconds().toString() // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  }
  for (const k in opt) {
    ret = new RegExp('(' + k + ')').exec(fmt)
    if (ret) {
      // eslint-disable-next-line eqeqeq
      fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, '0')))
    }
  }
  return fmt
}

// eslint-disable-next-line no-unused-vars
function getBetweenDateTime(startDateTime, endDateTime) {
  const timeStart = ' 00:00:00'
  const timeEnd = ' 23:59:59'
  const dateTimeStartFMD = dateFormat('YYYY-mm-dd', startDateTime)
  const dateTimeEndFMD = dateFormat('YYYY-mm-dd', endDateTime)
  const start = dateTimeStartFMD + timeStart
  const end = dateTimeEndFMD + timeEnd
  return [start, end]
}

export const calendarBaseShortcuts = [{
  text: '今天',
  onClick(picker) {
    const date = new Date()
    picker.$emit('pick', getBetweenDateTime(date, date))
  }
}, {
  text: '昨天',
  onClick(picker) {
    const start = new Date().daysAgo(1)
    picker.$emit('pick', getBetweenDateTime(start, start))
  }
}, {
  text: '最近一周',
  onClick(picker) {
    const start = new Date().daysAgo(7)
    picker.$emit('pick', getBetweenDateTime(start, new Date()))
  }
}, {
  text: '最近30天',
  onClick(picker) {
    const start = new Date().daysAgo(30)
    picker.$emit('pick', getBetweenDateTime(start, new Date()))
  }
}, {
  text: '这个月',
  onClick(picker) {
    const start = new Date().monthBegin()
    picker.$emit('pick', getBetweenDateTime(start, new Date()))
  }
}, {
  text: '本季度',
  onClick(picker) {
    const start = new Date().quarterBegin()
    picker.$emit('pick', getBetweenDateTime(start, new Date()))
  }
}]

export const calendarMoveShortcuts = [{
  text: '‹ 往前一天 ',
  onClick(picker) {
    if (picker.value.length === 0) {
      picker.value = [new Date(), new Date()]
    }
    const start = picker.value[0].daysAgo(1)
    const end = picker.value[1].daysAgo(1)
    picker.$emit('pick', getBetweenDateTime(start, end))
  }
}, {
  text: ' 往后一天 ›',
  onClick(picker) {
    let start = new Date()
    let end = new Date()
    if (picker.value.length > 0) {
      if (end - picker.value[1] > 8.64E7) {
        start = picker.value[0].daysAgo(-1)
        end = picker.value[1].daysAgo(-1)
      } else {
        start = picker.value[0]
      }
    }
    picker.$emit('pick', getBetweenDateTime(start, end))
  }
}, {
  text: '« 往前一周 ',
  onClick(picker) {
    if (picker.value.length === 0) {
      picker.value = [new Date().daysAgo(7), new Date()]
    }
    const start = picker.value[0].daysAgo(7)
    const end = picker.value[1].daysAgo(7)
    picker.$emit('pick', getBetweenDateTime(start, end))
  }
}, {
  text: ' 往后一周 »',
  onClick(picker) {
    let start = new Date().daysAgo(7)
    let end = new Date()
    if (picker.value.length > 0) {
      if (end - picker.value[1] > 8.64E7) {
        start = picker.value[0].daysAgo(-7)
        end = picker.value[1].daysAgo(-7)
      } else {
        start = picker.value[0]
      }
    }
    picker.$emit('pick', getBetweenDateTime(start, end))
  }
}]

export const calendarShortcuts = [
  ...calendarBaseShortcuts,
  ...calendarMoveShortcuts
]
