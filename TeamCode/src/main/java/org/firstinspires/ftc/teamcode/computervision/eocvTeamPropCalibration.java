package org.firstinspires.ftc.teamcode.computervision;



import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.util.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class eocvTeamPropCalibration extends OpenCvPipeline {
    double[] red = {255,0,0};
    boolean run = false;

    double max_error = 90.0;
    int min = 25;
    double forward;
    double[] yellow = {240,180,30};
    String result = "";
    Integer[] left = {};
    Integer[] center = {};
    Integer[] right = {};
    // Telemetry telemetry;
    Telemetry telemetry;
    public eocvTeamPropCalibration (Telemetry telemetry){
        this.telemetry = telemetry;
    }
    double[] hsv = new double[3];
    //public double[] convertRGB(double[] rgb) {
    //double[] red = (rgb[0] >> 16) & 0xFF;
    //double[] green = (rgb >> 8) & 0xFF;
    //double[] blue = rgb & 0xFF;

    //return new double[] {red, green, blue};
    // }
    public double[] RGBtoHSV(double r, double g, double b) {

        double h, s, v;

        double min, max, delta;
        min = Math.min(Math.min(r, g), b);
        max = Math.max(Math.max(r, g), b);

        // V/* www  .  j  a v a 2 s . co  m*/
        v = max;

        delta = max - min;

        // S
        if (max != 0)
            s = delta / max;
        else {
            s = 0;
            h = -1;
            return new double[] { h, s, v };
        }

        // H
        if (r == max)
            h = (g - b) / delta; // between yellow & magenta
        else if (g == max)
            h = 2 + (b - r) / delta; // between cyan & yellow
        else
            h = 4 + (r - g) / delta; // between magenta & cyan

        h *= 60; // degrees

        if (h < 0)
            h += 360;

        h = h * 1.0;
        s = s * 100.0;
        v = (v / 256.0) * 100.0;
        return new double[] { h, s, v };
    }
    public double comparison(Integer[] vec1, Integer[] vec2) {
        double loss = 0;
        for (int i=0; i < vec1.length; i++) {
            loss += (vec1[i] + vec2[i]) *  (vec1[i] + vec2[i]);
        }
        return loss;
    }
    public static int[] decodehex(String color) {
        int hex = Integer.parseInt(color);
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        return new int[] {r,g,b};
    }
    public boolean is_red(double[] l, Telemetry telemetry) {
        double[] red = {255, 0, 0};
        // telemetry.addData("color", "" + l[0]);
        //double[] blue = {0,0,255};
        if (l[1] > 90 && l[2] > 90){
            if (l[0] <= 30 || (l[0] > 115 && l[0] < 180)) {
                return false;
            }
        }
        //telemetry.addData("color", "red" +l[0] );

        return true;
    }


    @Override
    public Mat processFrame(Mat input) {
        // Executed every time a new frame is dispatched

        Mat input1 = input.clone();
        if (true) {
            ArrayList<Integer> redheights = new ArrayList<Integer>();
            for (int x = 0; x < input1.height(); x++) {
                int icounter = 0;
                for (int y = 0; y < input1.width(); y++) {
                    double[] i = input1.get(x, y);
                    if (is_red(i, telemetry)) {
                        icounter++;
                    }
                }
                redheights.add(icounter);
            }
            ArrayList<Integer> red_sum_list = new ArrayList<Integer>();
            Integer counter = 0;
            for (int i = 0; i < redheights.size(); i++) {
                counter += redheights.get(i);
                if (i % 100 == 0) {
                    red_sum_list.add(counter/100 );
                    counter = 0;
                }
            }
            Integer[] red_frequency = red_sum_list.toArray(new Integer[0]);
            //double left_comp = comparison(red_frequency, left);
            /*
            double center_comp = comparison(red_frequency, center);
            double right_comp = comparison(red_frequency, right);

            if (left_comp > center_comp && left_comp > center_comp) {
                result = "left";
            } else if (right_comp > center_comp && right_comp > center_comp) {
                result = "right";
            } else {
                result ="center";
            }
                            */
            telemetry.addData("histogram", "" + red_sum_list);
            telemetry.update();
        }
        return input1; // Return the image that will be displayed in the viewport
        // (In this case the input mat directly)
    }



}