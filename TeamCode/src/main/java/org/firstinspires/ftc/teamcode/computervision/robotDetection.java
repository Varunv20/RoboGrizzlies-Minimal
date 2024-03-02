package org.firstinspires.ftc.teamcode.computervision;


import java.util.Arrays;
import java.util.Random;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.imgproc.Imgproc;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class robotDetection extends OpenCvPipeline {
    public boolean run = false;
    Telemetry telemetry;

    public boolean right = false;
    public boolean center = false;
    public boolean left = false;
    public double[][] kernel =  {{-1,-0.5, 0,0.5,1}, {-2,-1, 0,1,2},{-3,2, 0,2,3}, {-2,-1, 0,1,2},{-1,-0.5, 0,0.5,1}};
    public double max_s = 0;
    public int ceil = 400;

    public int floor = 40;

    public double max_b = 0;

    public void setRun() {
        run = true;
    }

    public robotDetection() {
      //  this.telemetry = telemetry;
    }
    public double avg(double[] a) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        if (sum > max_b ) {
            max_b = sum;
        }
        return sum/a.length;
    }
    public double multiply( double[][][] b) {
        double sum = 0;
      //  double[][] ks = new double[3][3];
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                /*
                telemetry.addData("A", kernel[i][j]);
                telemetry.addData("H", i + " " + j);
                telemetry.addData("B", b[i][j][0] + " " + b[i][j][1] + " " + b[i][j][2]);
                */
                double[] k = scalarVectorMultiplication(kernel[i][j], b[i][j]);


                double s = avg(k);
              //  ks[i][j] = s;

                //telemetry.addData("K4", s);


                sum = sum + s;


            }
        }
        //telemetry.addData("KS3", ks[0][0] + " " + ks[0][1] + " " + ks[0][2]);
        //telemetry.addData("KS31", ks[1][0] + " " + ks[1][1] + " " + ks[1][2]);
        //telemetry.addData("KS32", ks[2][0] + " " + ks[2][1] + " " + ks[2][2]);
        //telemetry.addData("K3", b[0][0][0] + " " + b[0][1][0] + " " + b[0][2][0]);
        //telemetry.addData("K31", b[1][0][0] + " " + b[1][1][0] + " " + b[1][2][0]);
        //telemetry.addData("K32", b[2][0][0] + " " + b[2][1][0] + " " + b[2][2][0]);

        //telemetry.addData("S", sum);

        return sum;

    }
    public double[] scalarVectorMultiplication(double a, double[] b) {
        double[] r = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            r[i] = a * b[i];
        }
        return r;
    }
    public void reset() {
        left = false;
        center = false;
        right = false;
    }

    @Override
    public Mat processFrame(Mat input1) {
        // Executed every time a new frame is dispatched
        int left_edge = 0;
        int center_edge = 0;
        int right_edge = 0;
        Random r = new Random();
        double max_a = 0;
        if (run) {

            for (int i = input1.height()/2; i < input1.height()-40; i+=40){
                for (int j = 20; j < input1.width()-20; j+=9){
                    int i_r = r.nextInt(5) + i;
                    int j_r = r.nextInt(5) + j;

                    double[][][] arr = new double[kernel.length][kernel[0].length][3];
                    for (int x = -1; x < kernel.length -1; x+=1) {
                        for (int y = -1; y < kernel[0].length -1; y+=1) {

                            arr[x + 1][ y + 1] = input1.get(i_r + x, j_r + y);

                        }

                    }
                    double a = multiply(arr);

                    boolean edge = Math.abs(a) > floor && Math.abs(a)  < ceil;



                    if ((int)( (j_r*3) )/input1.width() ==0) {
                        if (edge) {
                            left_edge += 1;
                        }
                    }
                    else if ((int) ( (j_r*3) )/input1.width() ==1) {
                        if (edge) {
                            center_edge += 1;
                        }
                    }
                    else {
                        if (edge) {
                            right_edge += 1;
                        }
                    }
                    if (j_r == 2 || j_r == 1) {
                        i +=12;
                    }



                }
            }
            if (left_edge > right_edge *1.5 && left_edge > center_edge*1.5) {
                left = true;
                Imgproc.rectangle (
                        input1,                    //Matrix obj of the image
                        new Point(10, 10),        //p1
                        new Point(input1.width()/3 - 10, input1.height() - 10),       //p2
                        new Scalar(0, 0, 255),     //Scalar object for color
                        5                          //Thickness of the line
                );
            }
            else if (right_edge > left_edge*1.5 && right_edge > center_edge*1.5) {
                right = true;
                Imgproc.rectangle (
                        input1,                    //Matrix obj of the image
                        new Point(10 + 2*input1.width()/3, 10),        //p1
                        new Point(3 * input1.width()/3 - 10, input1.height() - 10),       //p2
                        new Scalar(0, 0, 255),     //Scalar object for color
                        5                          //Thickness of the line
                );
            }
            else if (center_edge > left_edge*1.5 && center_edge > right_edge*1.5) {
                center = true;

                Imgproc.rectangle (
                        input1,                    //Matrix obj of the image
                        new Point(10 + input1.width()/3, 10),        //p1
                        new Point(2 * input1.width()/3 - 10, input1.height() - 10),       //p2
                        new Scalar(0, 0, 255),     //Scalar object for color
                        5                          //Thickness of the line
                );
            }

            String text = "left" + Integer.toString(left_edge) + " " + "center" + Integer.toString(center_edge) + " " + "right" + Integer.toString(right_edge) + " MaxA " + Double.toString(max_a)+ " MaxS " + Double.toString(max_s)+ " MaxB " + Double.toString(max_b);
            Point position = new Point(170, 280);
            Scalar color = new Scalar(0, 0, 255);
            int font = Imgproc.FONT_HERSHEY_SIMPLEX;
            int scale = 1;
            int thickness = 3;
            //Adding text to the image
            Imgproc.putText(input1, text, position, font, scale, color, thickness);
            run = true;


            return input1;
        }
        return input1; // Return the image that will be displayed in the viewport
        // (In this case the input mat directly)
    }
}
