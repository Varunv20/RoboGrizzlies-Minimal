


import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.util.*;

public class eocvTeamProp extends OpenCvPipeline {
    double[] red = {255,0,0};
    int max = 65;
    int min = 25;
    double forward;
    double[] yellow = {240,180,30};

    // Telemetry telemetry;

    public opencvpipelines() {
        //this.telemetry = telemetry;
    }
    double[] hsv = new double[3];
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
    public boolean mae(double[] l) {
        hsv =  RGBtoHSV(l[0],l[1], l[2]);
        return ( hsv[0] < max &&  hsv[0] > min );
    }
    @Override
    public Mat processFrame(Mat input) {
        // Executed every time a new frame is dispatched


        ArrayList<Integer> redheights = new ArrayList<Integer>();
        for (int x = 0; x < input.height(); x++) {
            for (int y = 0; y < input.height(); y++) {
                double[] i = input.get(x,y );

            }
        }
        return input; // Return the image that will be displayed in the viewport
        // (In this case the input mat directly)
    }



}