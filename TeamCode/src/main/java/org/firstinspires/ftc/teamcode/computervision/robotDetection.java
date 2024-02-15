package org.firstinspires.ftc.teamcode.computervision;




import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.imgproc.Imgproc;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class robotDetection extends OpenCvPipeline {
    public boolean run = false;
    Telemetry telemetry;

    public boolean right = false;
    public boolean center = false;
    public boolean left = false;

    public void setRun() {
        run = true;
    }

    public void robotDetection(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input1) {
        // Executed every time a new frame is dispatched
        int left_edge = 0;
        int center_edge = 0;
        int right_edge = 0;

        if (run) {
            Mat dst = new Mat();
            Imgproc.blur(input1, dst,new Size(5,5));
            Imgproc.Canny(input1, dst,  70, 90, 3);

            for (int i = 0; i < dst.height(); i+=3){
                for (int j = 0; j < dst.width(); j+=3){
                    double[] array = dst.get(i, j);

                    if ((int) (j*3)/dst.height() ==0) {
                        if ((array[0] + array[1] +array[2])/3 >= 150) {
                            left_edge += 1;
                        }
                    }
                    else if ((int) (j*3)/dst.height() ==1) {
                        if ((array[0] + array[1] +array[2])/3 >= 150) {
                            center_edge += 1;
                        }
                    }
                    else {
                        if ((array[0] + array[1] +array[2])/3 >= 150) {
                            right_edge += 1;
                        }
                    }

                }
            }
            if (left_edge > right_edge *2 && left_edge > center_edge*2) {
                left = true;
            }
            else if (right_edge > left_edge*2 && right_edge > center_edge*2) {
                right = true;
            }
            else if (center_edge > left_edge*2 && center_edge > right_edge*2) {
                center = true;
            }
            run = false;


            return dst;
        }
        return input1; // Return the image that will be displayed in the viewport
        // (In this case the input mat directly)
    }
}
