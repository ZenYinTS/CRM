package com.crm.util;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;

import java.util.HashMap;
import java.util.Map;

public class ChartUtils {

    public static GsonOption getBarJsonOption(String title,String legend, String[] xLabels, Long[] values) {
        //Bar
//        String[] xLabels = {"广州", "深圳", "珠海", "汕头", "韶关", "佛山"};
//        int[] values = {6030, 7800, 5200, 3444, 2666, 5708};
//        String title = "地市数据";

        GsonOption option = new GsonOption();

        option.title(title); // 标题
        // 工具栏
        option.toolbox().show(true).feature(Tool.mark, // 辅助线
                Tool.dataView, // 数据视图
                new MagicType(Magic.line, Magic.bar),// 线图、柱状图切换
                Tool.restore,// 还原
                Tool.saveAsImage);// 保存为图片

        option.tooltip().show(true).formatter("{a} <br/>{b} : {c}");//显示工具提示,设置提示格式

        option.legend(legend);// 图例

        Bar bar = new Bar(legend);// 图类别(柱状图)
        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data(xLabels);// 轴数据类别
        // 循环数据
        for (int i = 0; i < xLabels.length; i++) {
            Long value = values[i];
            String color = "rgb(186,73,46)";
            // 类目对应的柱状图
            Map<String, Object> map = new HashMap<String, Object>(2);
            map.put("value", value);
            map.put("itemStyle", new ItemStyle().normal(new Normal().color(color)));
            bar.data(map);
        }
        boolean isHorizontal = true;
        if (isHorizontal) {// 横轴为类别、纵轴为值
            option.xAxis(category);// x轴
            option.yAxis(new ValueAxis());// y轴
        } else {// 横轴为值、纵轴为类别
            option.xAxis(new ValueAxis());// x轴
            option.yAxis(category);// y轴
        }

        option.series(bar);
        return option;
    }

    public static GsonOption getBarComplexJsonOption(String title,String legend, String[] xLabels, int[][] values) {
        //Bar
//        String[] xLabels = {"广州", "深圳", "珠海", "汕头", "韶关", "佛山"};
//        int[] values = {6030, 7800, 5200, 3444, 2666, 5708};
//        String title = "地市数据";

        GsonOption option = new GsonOption();

        option.title(title); // 标题
        // 工具栏
        option.toolbox().show(true).feature(Tool.mark, // 辅助线
                Tool.dataView, // 数据视图
                new MagicType(Magic.line, Magic.bar),// 线图、柱状图切换
                Tool.restore,// 还原
                Tool.saveAsImage);// 保存为图片

        option.tooltip().show(true).formatter("{a} <br/>{b} : {c}");//显示工具提示,设置提示格式

        option.legend(legend);// 图例

        Bar bar = new Bar(legend);// 图类别(柱状图)
        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data(xLabels);// 轴数据类别
        // 循环数据
        for (int i = 0; i < xLabels.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                int data = values[i][j];
                String color = "rgb(186,73,46)";
                // 类目对应的柱状图
                Map<String, Object> map = new HashMap<String, Object>(2);
                map.put("value", data);
                map.put("itemStyle", new ItemStyle().normal(new Normal().color(color)));
                bar.data(map);
            }
        }
        boolean isHorizontal = true;
        if (isHorizontal) {// 横轴为类别、纵轴为值
            option.xAxis(category);// x轴
            option.yAxis(new ValueAxis());// y轴
        } else {// 横轴为值、纵轴为类别
            option.xAxis(new ValueAxis());// x轴
            option.yAxis(category);// y轴
        }

        option.series(bar);
        return option;
    }


    public static GsonOption getLineJsonOption(String title,String type,int[] values,String[] xLabels) {
        //        Line
//        String[] types = {"邮件营销", "联盟广告", "视频广告"};
//        int[][] datas = {{120, 132, 101, 134, 90, 230, 210}, {220, 182, 191, 234, 290, 330, 310}, {150, 232, 201, 154, 190, 330, 410}};
//        String title = "广告数据";

        GsonOption option = new GsonOption();
        option.title().

                text(title).
                x("left");// 大标题、小标题、位置
        // 提示工具
        option.tooltip().

                trigger(Trigger.axis);// 在轴上触发提示数据
        // 工具栏
        option.toolbox().

                show(true).

                feature(Tool.saveAsImage);// 显示保存为图片

        option.legend(type);// 图例

        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data(xLabels);
        category.boundaryGap(false);// 起始和结束两端空白策略

//        // 循环数据
//        for (
//                int i = 0;
//                i < types.length; i++) {
//            Line line = new Line();// 三条线，三个对象
//            String type = types[i];
//            line.name(type).stack("总量");
//            for (int j = 0; j < datas[i].length; j++)
//                line.data(datas[i][j]);
//            option.series(line);
//        }
        Line line = new Line();
        line.name(type).stack("总量");
        for (int i = 0; i < values.length; i++)
            line.data(values[i]);
        option.series(line);

        boolean isHorizontal = true;
        if (isHorizontal) {// 横轴为类别、纵轴为值
            option.xAxis(category);// x轴
            option.yAxis(new ValueAxis());// y轴
        } else {// 横轴为值、纵轴为类别
            option.xAxis(new ValueAxis());// x轴
            option.yAxis(category);// y轴
        }
        return option;
    }
}
