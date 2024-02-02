package org.firstinspires.ftc.teamcode.computervision;



import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class eocvTeamProp extends OpenCvPipeline {
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
            if (((l[0]/l[1] > 1.45 && l[0]/l[2] > 1.45 ) && red) || ((l[2]/l[1] > 1.45 && l[2]/l[0] > 1.45)) && !red) {
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

        if (run) {

            Integer[] redheights = new Integer[3];
            redheights[0] = 0;
            redheights[1] = 0;
            redheights[2] = 0;
            int i1 = 0;
            
            for (int y = 0; y < input1.width()/3; y++) {
                
                i1 = (int) y/input1.width()*3;
                long s = System.nanoTime();
                for (int x = input1.height()/3; x < input1.height()-input1.height()/3; x+=3) {
                    double[] i = input1.get(x, y*3);
                    

                    if (is_red(i)) {
                        redheights[i1]++;
                    }

                }
                long e = System.nanoTime();
                result = "" + (e - s);


                redheights[y] = icounter;
                result = "c24";

            }

            m1avg = redheights[0];
            m2avg = redheights[1];
            m3avg = redheights[2];

            // if (!red) {
            //     m1avg -= 2000;
            // }
            //if (red) {
            if (m1avg / m2avg > 1.4 && m1avg / m3avg > 1.15) {
                result = "left";
            } else if (m3avg / m2avg > 1.4 && m3avg / m1avg > 1.15) {
                result = "right";
            } else {
                result = "center";
            }
         /*   telemetry.addData("r", result);
            telemetry.addData("m1", m1avg);
            telemetry.addData("m2", m2avg);
            telemetry.addData("m3", m3avg);
            telemetry.update();*/

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

