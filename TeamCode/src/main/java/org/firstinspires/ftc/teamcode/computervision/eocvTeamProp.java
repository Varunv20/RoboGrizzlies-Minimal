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
            if (((l[0]/l[1] > 1.15 && l[0]/l[2] > 1.45 ) && red) || ((l[2]/l[1] > 1.15 && l[2]/l[0] > 1.45)) && !red) {
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

            Integer[] redheights = new Integer[input1.width()/3];

            for (int y = 0; y < input1.width()/3; y++) {
                int icounter = 0;
                long s = System.nanoTime();
                for (int x = input1.height()/4; x < input1.height()-input1.height()/4; x+=3) {
                    double[] i = input1.get(x, y*3);


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
