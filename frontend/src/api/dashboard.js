import request from '@/utils/request'

// 获取统计数据
export function getStatistics() {
  // 实际项目中应该调用后端接口
  // return request({
  //   url: '/api/dashboard/statistics',
  //   method: 'get'
  // })
  
  // 模拟数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        studentTotal: 1250,
        classTotal: 24,
        averageScore: 85.6
      })
    }, 1000)
  })
}

// 获取成绩分布数据
export function getScoreDistribution() {
  // 实际项目中应该调用后端接口
  // return request({
  //   url: '/api/dashboard/score-distribution',
  //   method: 'get'
  // })
  
  // 模拟数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([
        { name: '优秀(90-100)', value: 320 },
        { name: '良好(80-89)', value: 450 },
        { name: '中等(70-79)', value: 280 },
        { name: '及格(60-69)', value: 150 },
        { name: '不及格(<60)', value: 50 }
      ])
    }, 1200)
  })
}

// 获取班级成绩趋势数据
export function getClassScoreTrend() {
  // 实际项目中应该调用后端接口
  // return request({
  //   url: '/api/dashboard/class-score-trend',
  //   method: 'get'
  // })
  
  // 模拟数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        categories: ['一班', '二班', '三班', '四班', '五班', '六班'],
        series: [
          {
            name: '语文',
            data: [85, 88, 82, 90, 87, 89]
          },
          {
            name: '数学',
            data: [78, 85, 88, 82, 90, 86]
          },
          {
            name: '英语',
            data: [82, 80, 85, 88, 84, 87]
          }
        ]
      })
    }, 800)
  })
}