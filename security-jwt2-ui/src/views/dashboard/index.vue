<template>
     <el-row :gutter="20">
        <el-col :span="6">
            <el-card shadow="hover">
                <i class="el-icon-user" style="font-size: 55px;color:lightseagreen"></i>
                <div class="headerTitleBox">
                    <div class="headerTitle">用户数</div>
                    <div class="headerNumber">53210</div>
                </div>
            </el-card>
        </el-col>
        <el-col :span="6">
            <el-card shadow="hover">
                <i class="el-icon-camera" style="font-size: 55px;color:coral"></i>
                <div class="headerTitleBox">
                    <div class="headerTitle">角色数</div>
                    <div class="headerNumber">27</div>
                </div>
            </el-card>
        </el-col>
        <el-col :span="6">
            <el-card shadow="hover">
                <i class="el-icon-picture-outline-round" style="font-size: 55px;color:cornflowerblue"></i>
                <div class="headerTitleBox">
                    <div class="headerTitle">菜单数</div>
                    <div class="headerNumber">83</div>
                </div>
            </el-card>
        </el-col>

        <el-col :span="6">
            <el-card shadow="hover">
                <i class="el-icon-view" style="font-size: 55px;color:hotpink"></i>
                <div class="headerTitleBox">
                    <div class="headerTitle">访问次数</div>
                    <div class="headerNumber">836267</div>
                </div>
            </el-card>
        </el-col>



        <el-col :span="8" style="margin-top: 20px">
            <el-card shadow="hover">
                <div class="user">
                    <img :src="userImg" />
                    <div class="userinfo">
                        <p class="name">用户名: admin</p>
                        <p class="access">角色: 超级管理员</p>
                    </div>
                </div>
                <div class="login-info">
                    <p>上次登录时间：<span>2022-10-16</span></p>
                    <p>上次登录地点：<span>广东河源</span></p>
                </div>
            </el-card>
            
        </el-col>

        <!-- charts1 -->
        <el-col :span="15">

            <el-card shadow="hover">
                <div id="charts1" style="width: 790px;height:400px;">

                </div>
            </el-card>

        </el-col>

        <!-- charts2 -->
        <el-col :span="12">

        <el-card shadow="hover">
            <div id="charts2" style="width: 627px;height:400px;">

            </div>
        </el-card>

        </el-col>


        <!-- charts3 -->
        <el-col :span="11">

        <el-card shadow="hover">
            <div id="charts3" style="width: 573px;height:400px;">

            </div>
        </el-card>

        </el-col>

    </el-row>




</template>

<style lang="less" scoped>

.headerTitleBox{
    width: 100px;
    float: right;
}
.headerTitle{
    height: 30px;
    color: rgba(0,0,0,.45);
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    overflow: hidden;
}

.headerNumber {
    height: 30px;
    color: rgba(15, 14, 14, 0.903);
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    overflow: hidden;
}


.home {
    .user {
      display: flex;
      align-items: center;
      padding-bottom: 20px;
      margin-bottom: 20px;
      border-bottom: 1px solid #ccc;
      img {
        width: 150px;
        height: 150px;
        border-radius: 50%;
        margin-right: 40px;
      }
      &info {
        .name {
          font-size: 32px;
          margin-bottom: 10px;
        }
        .access {
          color: #999999;
        }
      }
    }
    .login-info {
      p {
        line-height: 28px;
        font-size: 14px;
        color: #999999;
        span {
          color: #666666;
          margin-left: 60px;
        }
      }
    }
    
    
  }

</style>


</style>


<script>

/* 引入echarts */
import * as echarts from 'echarts';

export default {
    name: 'DashBoard',
    components: {

    },
    data () {
        return {
            userImg: require("../../../src/assets/logo.png")
        }
    },
    mounted(){
        //初始化charts1
        this.initCharts1();     

        //初始化charts2
        this.initCharts2();   

        //初始化charts3
        this.initCharts3();   

    },
    methods:{

        //初始化echarts图表1(折线图)
        initCharts1(){

            let chartDom = document.getElementById('charts1');
            let myChart = echarts.init(chartDom);
            let option;

            option = {
            xAxis: {
                type: 'category',
                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                data: [150, 230, 224, 218, 135, 147, 260],
                type: 'line'
                }
            ]
            };

            //最后一定要加上这段代码才会生效
            option && myChart.setOption(option);

        },

        //初始化echarts图表2
        initCharts2(){
            let chartDom = document.getElementById('charts2');
            let myChart = echarts.init(chartDom);
            let option;

            option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                type: 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                type: 'category',
                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                axisTick: {
                    alignWithLabel: true
                }
                }
            ],
            yAxis: [
                {
                type: 'value'
                }
            ],
            series: [
                {
                name: 'Direct',
                type: 'bar',
                barWidth: '60%',
                data: [10, 52, 200, 334, 390, 330, 220]
                }
            ]
            };

            //最后一定要加上这段代码才会生效
            option && myChart.setOption(option);

        },

        //初始化echarts图表3
        initCharts3(){

            var chartDom = document.getElementById('charts3');
            var myChart = echarts.init(chartDom);
            var option;

            option = {
            tooltip: {
                trigger: 'item'
            },
            legend: {
                top: '5%',
                left: 'center'
            },
            series: [
                {
                name: 'Access From',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 2
                },
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                    show: true,
                    fontSize: '40',
                    fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: [
                    { value: 1048, name: 'Search Engine' },
                    { value: 735, name: 'Direct' },
                    { value: 580, name: 'Email' },
                    { value: 484, name: 'Union Ads' },
                    { value: 300, name: 'Video Ads' }
                ]
                }
            ]
            };

            //最后一定要加上这段代码才会生效
            option && myChart.setOption(option);

        }
        

    }
}
 
</script>
