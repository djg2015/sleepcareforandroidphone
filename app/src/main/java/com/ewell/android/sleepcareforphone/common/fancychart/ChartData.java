package com.ewell.android.sleepcareforphone.common.fancychart;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChartData {

    private static final String TAG = ChartData.class.getName();

    public static final String LINE_COLOR_PURPLE = "#5c82f5";
    public static final String LINE_COLOR_LIGHTGREEN ="#7df93c";
    public static final String LINE_COLOR_GREEN ="#4be0d3";
    public static final String LINE_COLOR_BLUE = "#56a3fd";
    public static final String LINE_COLOR_RED = "#fe4f4a";

    private List<AxisValue> yValues;
    private List<AxisValue> xValues;
    private List<Point> points;

    private int lineColor;
    private int belowLineColor;

    public ChartData(String lineColor) {
        this.yValues = new ArrayList<AxisValue>();
        this.xValues = new ArrayList<AxisValue>();
        this.points = new ArrayList<Point>();

        this.lineColor = Color.parseColor(lineColor);
        // 20% opacity
        this.belowLineColor = Color.parseColor("#22" + lineColor.replace("#", ""));
    }

    public int getLineColor() {
        return lineColor;
    }

    public int getBelowLineColor() {
        return belowLineColor;
    }

    public void addYValue(double value, String title) {
        yValues.add(new AxisValue(value, title));
        Collections.sort(yValues);
    }

    public void addXValue(double value, String title) {
        xValues.add(new AxisValue(value, title));
        Collections.sort(xValues);
    }

    public List<AxisValue> getXValues() {
        return xValues;
    }

    public List<AxisValue> getYValues() {
        return yValues;
    }

    public ChartData addPoint(int x, int y, String title, String subtitle) {
        Point point = new Point(x, y);
        point.title = title;
        point.subtitle = subtitle;
        points.add(point);

        return this;
    }

    public ChartData addPoint(int x, int y) {
        addPoint(x, y, null, null);

        return this;
    }

    public void automaticallyAddXValues() {
        double maxX = 0;
        for (Point point : points) {
            if (Double.compare(point.x, maxX) > 0) {
                maxX = point.x;
            }
        }

        double step = maxX / 10.0;
        for (double i = 0; Double.compare(i, maxX) < 0; i += step) {
            String label = Integer.toString((int) i);
            if (Double.compare(i, 0) < 0) {
                label = String.format("%.2f", i);
            }

            xValues.add(new AxisValue(i, label));
        }
    }

    public void automaticallyAddYValues() {
        double maxY = 0;
        for (Point point : points) {
            if (Double.compare(point.y, maxY) > 0) {
                maxY = point.y;
            }
        }

//修改:修正y轴值<10的情况
        double step = maxY / 10.0;
        if (step < 1.0) {
            step = 1.0;

        }
       step = Math.ceil(step);

        for (double i = 0; Double.compare(i, maxY) <= 0; i += step) {
            String label = Integer.toString((int) i);
            if (Double.compare(i, 0) < 0) {
                label = String.format("%.3f", i);
            }

            yValues.add(new AxisValue(i, label));
        }

        //y轴值全为0,则手动添加一个点:10
        if(yValues.size()==1){
            yValues.add(new AxisValue(1,Integer.toString(10)));
        }
    }

    public double getMinX() {
        if (xValues.size() == 0) {
            return 0;
        }

        return xValues.get(0).value;
    }

    public double getMaxX() {
        if (xValues.size() == 0) {
            return 0;
        }

        return xValues.get(xValues.size() - 1).value;
    }

    public double getMinY() {
        if (yValues.size() == 0) {
            return 0;
        }

        return yValues.get(0).value;
    }

    public double getMaxY() {
        if (yValues.size() == 0) {
            return 0;
        }

        return yValues.get(yValues.size() - 1).value;
    }

    public void clearValues() {
        xValues.clear();
        yValues.clear();
        points.clear();
    }

    public List<Point> getPoints() {
        return points;
    }

}
