<script>
// export default {
//   name: "Echarts"
// }
import * as echarts from "echarts";
import {ref} from "vue";
import {updateZ} from "@/api/Echarts";

export default {

    name: "echartsBox",
    components: {
        ref
    },

    data() {

        return {

            loading: false,


            getInfo: {
                "cpu": 3.1,
                "memoryInfo": {
                    "memoryPercent": 51.91170541593871,
                    "usedMemory": 1.50888671875,
                    "allMemory": 2.906640625,
                    "freeMemory": 1.3977539062500002
                },
                ssdInfo: {
                    usedSSD: 10.0,
                    allSSD: 88.0,
                    freeSSD: 78.0,
                    ssdpercent: 11.363636363636363
                }
            },
            zxtoption123: {
                visualMap: [{
                    show: false,
                    type: 'continuous',
                    seriesIndex: 0,
                    min: 0,
                    max: 400
                }],
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            var msg = "";
                            if (params.seriesData.length > 0) {
                                msg = params.value + ': ' + params.seriesData[0].data + "M内存"
                            }
                            return msg;
                        }
                    }
                },

                title: [{
                    left: 'center',
                    text: '内存占使用率（MB）图'
                }],
                tooltip: {
                    trigger: 'axis'
                },
                xAxis: {
                    data: ['60秒前', '59秒前', '58秒前', '57秒前', '56秒前', '55秒前', '54秒前', '53秒前', '52秒前', '51秒前', '50秒前', '49秒前', '48秒前', '47秒前', '46秒前', '45秒前', '44秒前', '43秒前', '42秒前', '41秒前', '40秒前', '39秒前', '38秒前', '37秒前', '36秒前', '35秒前', '34秒前', '33秒前', '32秒前', '31秒前', '30秒前', '29秒前', '28秒前', '27秒前', '26秒前', '25秒前', '24秒前', '23秒前', '22秒前', '21秒前', '20秒前', '19秒前', '18秒前', '17秒前', '16秒前', '15秒前', '14秒前', '13秒前', '12秒前', '11秒前', '10秒前', '9秒前', '8秒前', '7秒前', '6秒前', '5秒前', '4秒前', '3秒前', '2秒前', '实时']
                },
                yAxis: {
                    splitLine: {show: false},
                    max: 8191
                },
                series: {
                    type: 'line',
                    showSymbol: false,
                    data: ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0']
                }
            },
            zxtoption: {
                title: {
                    text: '内存仪表盘'
                },
                tooltip: {},
                toolbox: {
                    feature: {
                        restore: {},
                        saveAsImage: {}
                    }
                },
                series: [{
                    name: '内存使用率',
                    type: 'gauge',
                    detail: {
                        formatter: '{value}%'
                    },
                    data: [{
                        value: 0,
                    }]
                }]
            },

            ybpOption: {
                title: {
                    text: 'CPU仪表盘'
                },
                tooltip: {},
                toolbox: {
                    feature: {
                        restore: {},
                        saveAsImage: {}
                    }
                },
                series: [{
                    name: 'CPU使用率',
                    type: 'gauge',
                    detail: {
                        formatter: '{value}%'
                    },
                    data: [{
                        value: 0,
                    }]
                }]
            },


        }
    },
    mounted() {
        this.updateZxtChart();

        this.initCharts();
        // this.ups();

    },
    unmounted() {
        // 在这里清理资源（例如清除定时器）
        this.clearIntervals();

    },
    methods: {

        initCharts() {
            this.ups2();
            // 在这里初始化你的 echarts 图表并设置数据获取定时器
            this.initYbpChart();
            this.initZxtChart();

        },

        clearIntervals() {
            // 在这里清除你设置的定时器

        },
        // 在这里添加初始化和更新图表的函数
        initYbpChart() {
            var zxtDom = document.getElementById("zxt");
            var zxtChart = echarts.init(zxtDom);
            if (this.zxtoption && typeof this.zxtoption === "object") {
                zxtChart.setOption(this.zxtoption, true);
            }

            /*
                  setInterval(
                      this.updateZxtChart()

                      /!* var address = "/sys/info/memoryUsedPerc";
                       $.get(address, "", function(data) {
                         zxtoption.series.data = data;
                         zxtChart.setOption(zxtoption, true);
                       })*!/
                  );*/

        },
        // 例如：updateYbpChart() { ... },
        initZxtChart() {
            var ybpDom = document.getElementById("ybp");
            var ybpChart = echarts.init(ybpDom);

            if (this.ybpOption && typeof this.ybpOption === "object") {
                ybpChart.setOption(this.ybpOption, true);
            }

        },
        ups2() {


            // this.ybpOption.series[0].data[0].value = this.getInfo.cpu;
            // console.log(this.ybpOption.series[0].data[0].value);
            // this.zxtoption.series[0].data[0].value = this.getInfo.memoryInfo.memoryPercent.toFixed();
            // console.log(this.zxtoption.series[0].data[0].value)


        },
        ups() {
            updateZ(this.$route.params.id).then(response => {
                console.log('response')

                console.log(response)
                // this.getInfo.cpu = response.data.cpu
                this.ybpOption.series[0].data[0].value =  response.data.cpu;
                console.log(this.ybpOption.series[0].data[0].value);
                this.zxtoption.series[0].data[0].value =  ((response.data.memoryInfo.usedMemory/response.data.memoryInfo.allMemory)*100).toFixed();
                console.log(this.zxtoption.series[0].data[0].value)


                this.getInfo.allSSD=response.data.ssdInfo.allSSD
                console.log(this.getInfo.allSSD)

                this.getInfo.usedSSD=response.data.ssdInfo.usedSSD
                this.getInfo.freeSSD=response.data.ssdInfo.freeSSD

                // this.loading = true;
                this.initYbpChart();
                this.initZxtChart();
            });
        },
        updateZxtChart() {
            this.ups();

            setTimeout(() => {
                this.ups();
                // 方法区
            }, 100000000);

        },
    },
};


</script>

<template>
    <div class="echarts-box">

        <div id="ybp" style="height: 250px; width: 250PX; float: right;"></div>
        <div id="zxt" style="height: 250px; width: 250PX;float: right;"></div>


        <table align="center" width="100%" class="table xunw_table_form" border="0">
            <tbody>

            <tr>
                <th class="zxstyle">硬盘全部</th>
                <th class="zxstyle">硬盘已用</th>
                <th class="zxstyle">硬盘剩余</th>

            </tr>
            <tr>
                <td>{{ getInfo.allSSD }}</td>
                <td>{{ getInfo.usedSSD }}</td>
                <td>{{ getInfo.freeSSD }}</td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<style scoped>

</style>
