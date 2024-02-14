package org.firstinspires.ftc.teamcode.computervision;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.imgproc.Imgproc;
import java.util.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class robotDetection extends OpenCvPipeline {
    public boolean run = false;

    double max_error = 90.0;
    int min = 25;
    double forward;
    double[] yellow = {240,180,30};
    public String result = "hello";
    //etry telemetry;

    public boolean red = true;
    double[] hsv = new double[3];
    double baseline_left = 0;
    double baseline_center = 0;
    double baseline_right = 0;
    public double m1avg;
    public double m2avg;
    public double m3avg;


    public boolean is_red(double[] l) {
        // telemetry.addData("color", "" + l[0]);
        //double[] blue = {0,0,255};
        if (l[2] > 10) {
            if (((l[0]/l[1] > 1.15 && l[0]/l[2] > 1.35 ) && red) || ((l[2]/l[1] > 1.15 && l[2]/l[0] > 1.35)) && !red) {
                return true;
            }
        }

        //telemetry.addData("color", l[0]  );

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
        int left_edge = 0;
        int center_edge = 0;
        int right_edge = 0;

        if (run) {
            Mat dst = new Mat();
            Imgproc.Sobel(input1, dst, -1, 1, 0);
            for (int i = 0; i < dst.height(); i++){
                for (int j = 0; i < dst.width(); j++){
                    double[] array = dst.get(i, j);
                    if ((array[0] + array[1] +array[2])/3 >= 150) {

                    }
                }
            }
            run = false;
        }
        return input1; // Return the image that will be displayed in the viewport
        // (In this case the input mat directly)
    }
}
