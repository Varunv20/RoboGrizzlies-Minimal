package org.firstinspires.ftc.teamcode.computervision;



import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.util.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class eocvTeamProp extends OpenCvPipeline {
    double[] red = {255,0,0};
    double max_error = 90.0;
    int min = 25;
    double forward;
    double[] yellow = {240,180,30};

    // Telemetry telemetry;
    Telemetry telemetry;
    public eocvTeamProp (Telemetry telemetry){
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
    public static int[] decodehex(String color) {
        int hex = Integer.parseInt(color);
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        return new int[] {r,g,b};
    }
    public boolean mae(double[] l, Telemetry telemetry) {
        // double[] red = {255, 0, 0};
       // telemetry.addData("color", "" + l[0]);
        //double[] blue = {0,0,255};
       double[] h = RGBtoHSV(l[0],l[1],l[2]);
       // l[0] <= 255 && l[0] >= 180 && l[1] <= 120 && l[1] >= 0 && l[2] <= 120 && l[2] >= 0
        if ((h[0] <= 30 && h[0] >= 0 || h[0] <= 350 && h[0] >= 300) && h[1] > 40 && h[2] > 40)
        {
            //telemetry.addData("color", "red" );
            return true;
        }
        return false;
    }
    @Override
    public Mat processFrame(Mat input) {
        // Executed every time a new frame is dispatched
        Mat input1 = input.clone();

        ArrayList<Integer> redheights = new ArrayList<Integer>();
        for (int x = 0; x < input1.height(); x++) {
            int icounter = 0;
            for (int y = 0; y < input1.width(); y++) {
                double[] i = input1.get(x,y);
                if (mae(i, telemetry)) {
                   icounter ++;
                }
            }
            redheights.add(icounter);
        }
        ArrayList<Integer> red_sum_list = new ArrayList<Integer>();
        Integer counter = 0;

        for (int i = 0;i < redheights.size(); i++ ) {
            counter += redheights.get(i);
            if (i%100 == 0) {
                red_sum_list.add(counter/100);
                counter = 0;
            }
        }
        telemetry.addData("histogram", "" + red_sum_list );
        telemetry.update();
        return input1; // Return the image that will be displayed in the viewport
        // (In this case the input mat directly)
    }



}