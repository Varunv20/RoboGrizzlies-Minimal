package org.firstinspires.ftc.teamcode.computervision;



import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class eocvTeamProp extends OpenCvPipeline {
    boolean run = true;

    double max_error = 90.0;
    int min = 25;
    double forward;
    double[] yellow = {240,180,30};
    public String result = "hello";
    //etry telemetry;

    public boolean red = false;
    double[] hsv = new double[3];
    double baseline_left = 0;
    double baseline_center = 0;
    double baseline_right = 0;
    public double m1avg;
    public double m2avg;
    public double m3avg;

    //public double[] convertRGB(double[] rgb) {
    //double[] red = (rgb[0] >> 16) & 0xFF;
    //double[] green = (rgb >> 8) & 0xFF;
    //double[] blue = rgb & 0xFF;

    //return new double[] {red, green, blue};
    // }
    public boolean is_red(double[] l) {
        // telemetry.addData("color", "" + l[0]);
        //double[] blue = {0,0,255};
        if (l[2] > 10) {
            if ((((l[0] > 170 && l[2] < 100) && l[1] < 100 ) && red) || ((l[0] < 100) && l[1] < 100 && l[2] > 170)) {
                return true;
            }
        }

        //telemetry.addData("color", l[0]  );

        return false;
    }
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
    public double comparison(Double[] vec1, Double[] vec2) {
        double loss = 0;
        for (int i=0; i < vec1.length; i++) {
            loss += Math.abs(vec1[i] + vec2[i]);
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

    public boolean mae(double[] l, Telemetry telemetry) {
        double[] red = {255, 0, 0};
        // telemetry.addData("color", "" + l[0]);
        //double[] blue = {0,0,255};
        if (l[0] <= 255 && l[0] >= 180 && l[1] <= 120 && l[1] >= 0 && l[2] <= 120 && l[2] >= 0)
        {
            //telemetry.addData("color", "red" );
            return true;
        }
        return false;
    }
    public double avg(Integer[] arr){
        Integer sum = 0;
        for (int i = 0; i < arr.length; i++){
            sum += arr[i];

        }
        return sum/ arr.length;
    }
    public String getResult() {
        return result;
    }
    public void setRun() {
        run = true;
    }

    @Override
    public Mat processFrame(Mat input1) {
        // Executed every time a new frame is dispatched

        if (run) {

            Integer[] redheights = new Integer[input1.width()];

            for (int y = 0; y < input1.width(); y++) {
                int icounter = 0;
                long s = System.nanoTime();
                for (int x = 0; x < input1.height(); x++) {
                    double[] i = input1.get(x, y);


                    if (is_red(i)) {
                        icounter++;
                    }

                }
                long e = System.nanoTime();
                result = "" + (e - s);


                redheights[y] = icounter;
                result = "c24";

            }

            ArrayList<Integer> red_sum_list = new ArrayList<Integer>();
            Integer counter = 0;

            for (int i = 0; i < redheights.length; i++) {
                counter += redheights[i];
                if (i % 50 == 0) {
                    red_sum_list.add(counter);
                    counter = 0;
                }
            }
            Integer[] arr = new Integer[red_sum_list.size()];
            result = "c3";

            arr = red_sum_list.toArray(arr);
            Integer[] m1 = Arrays.copyOfRange(arr, 0, (int) arr.length / 3);
            Integer[] m2 = Arrays.copyOfRange(arr, arr.length / 3, (int) 2 * arr.length / 3);

            Integer[] m3 = Arrays.copyOfRange(arr, 2 * arr.length / 3, arr.length);
            m1avg = avg(m1);
            m2avg = avg(m2);
            m3avg = avg(m3);

           // if (!red) {
           //     m1avg -= 2000;
           // }
            //if (red) {
                if (m1avg / m2avg > 1.4 && m1avg / m3avg > 1.1) {
                    result = "left";
                } else if (m3avg / m2avg > 1.4 && m3avg / m1avg > 1.1) {
                    result = "right";
                } else {
                    result = "center";
                }
           // } //m3avg / m2avg > 1.17 && m3avg / m1avg > 1.2 else if
           // else {
                /*if (m1avg / m2avg < 1.1 && m3avg / m2avg < 1.1) {
                    result = "center";
                } else if (m3avg / m2avg > 1.17 && m3avg / m1avg > 1.2) {
                    result = "right";
                } else {
                    result = "left";
                }*/
           //     if (m1avg / m2avg > 1.4 && m1avg / m3avg > 1.1) {
                   // result = "left";
           //     } else if (m3avg / m2avg > 1.4 && m3avg / m1avg > 1.1) {
                    //result = "right";
          //      } else {
          //          result = "center";
           //     }
           // }

            run = false;

        }
        return input1; // Return the image that will be displayed in the viewport
        // (In this case the input mat directly)
    }
}