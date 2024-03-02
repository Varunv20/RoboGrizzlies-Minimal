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
public class floorPixelDetection extends OpenCvPipeline {
    public boolean run = false;
    Telemetry telemetry;

    public boolean right = false;
    public boolean center = false;
    public boolean left = false;
    public double[][] kernel =  {{-1,-0.5, 0,0.5,1}, {-2,-1, 0,1,2},{-3,2, 0,2,3}, {-2,-1, 0,1,2},{-1,-0.5, 0,0.5,1}};
    public double max_s = 0;
    public int max_x = 0;
    public int max_y = 0;

    public int ceil = 400;

    public int floor = 40;

    public double max_b = 0;

    public void setRun() {
        run = true;
    }

    public floorPixelDetection() {

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
        max_x = 0;
        max_y = 0;

    }

    public int[][] setArrayVal(int[][] arr, int x, int y) {
        arr[y][x] += 1;
        return arr;
    }
    public int[][] initArray(int height, int width) {

        int[][] coords = new int[height][width];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {

                coords[i][j] = 0;
            }
        }
        return coords;
    }

    @Override
    public Mat processFrame(Mat input1) {
        // Executed every time a new frame is dispatched

        if (run) {
            int[][] coords = initArray(5, 12);
            Random r = new Random();
            double max_a = 0;
            int ec = 0;
            int x10 = 0;
            int y10 = 0;

            int x11 = 0;


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

                    if (edge) {
                        ec += 1;
                        int p_x = (int) (12 * j_r/ input1.width());
                        int p_y = (int)  (5 * i_r/ input1.height());
                        x10 = p_x;
                        y10 = p_y;

                        coords = setArrayVal(coords, p_x, p_y);
                        x11 = coords[p_y][p_x];
                    }
                    if (j_r == 2 || j_r == 1) {
                        i +=6;
                    }


                }
            }
            int c10 = coords[y10][x10];
            int ck = 0;
            int edge_max = 0;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 12; j++) {
                    ck = coords[i][j];
                    if (ck > edge_max){
                        edge_max = coords[i][j];
                        max_x = j;
                        max_y = i;
                    }
                }
            }
            max_y -= 2.5;
            max_x -= 6;


            String text = "x" + Integer.toString(max_x) + " " + "y" + Integer.toString(max_y) + "ex" + Integer.toString(coords[2][2] );
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
