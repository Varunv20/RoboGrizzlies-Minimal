package org.firstinspires.ftc.teamcode.computervision;



import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class eocvTeamProp extends OpenCvPipeline {
    public boolean run = false;
    public String result = "hello";
    //etry telemetry;

    public boolean red = true;


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
            int left_counter = 0;
            int center_counter = 0;
            int right_counter = 0;


            for (int y = 0; y < input1.width()/3; y++) {
                int icounter = 0;
                long s = System.nanoTime();
                for (int x = input1.height()/4; x < input1.height()-input1.height()/4; x+=3) {

                    double[] i = input1.get(x, y*3);


                    if (is_red(i)) {
                        int i111 =(int)y*9/ (input1.width()/3);
                        if (i111 == 0){
                            left_counter++;
                        }
                        else if (i111 == 1) {
                            center_counter++;
                        }
                        else {
                            right_counter++;
                        }

                    }

                }
                long e = System.nanoTime();


                result = "c24";

            }


            if (left_counter / center_counter > 1.4 && left_counter / right_counter > 1.15) {
                result = "left";
            } else if (right_counter / center_counter > 1.4 && right_counter / left_counter > 1.15) {
                result = "right";
            } else {
                result = "center";
            }


            run = true;

        }
        return input1; // Return the image that will be displayed in the viewport
        // (In this case the input mat directly)
    }
}
