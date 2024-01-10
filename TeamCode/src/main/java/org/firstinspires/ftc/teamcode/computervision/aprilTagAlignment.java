package org.firstinspires.ftc.teamcode.computervision;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.util.*;

public class aprilTagAlignment extends OpenCvPipeline{
    int chunkSize = 30;

    @Override
    public Mat processFrame(Mat input){
        // Getting colors of all pixels into 2D array
        ArrayList<Object> bigArrList = new ArrayList<Object>(); // Empty 2D array
        for (int i = 0; i < input.height(); i++){
            ArrayList<Object> rowArrList = new ArrayList<Object>();
            for (int j = 0; j < input.width(); j++){
                double[] colorArray = input.get(i, j);
                ArrayList<Double> colorArrList = new ArrayList<Double>();
                double colorR = colorArray[0];
                double colorG = colorArray[1];
                double colorB = colorArray[2];
                colorArrList.add(colorR);
                colorArrList.add(colorG);
                colorArrList.add(colorB);
                rowArrList.add(colorArrList);
            }
            bigArrList.add(rowArrList); // Filled 2D array
        }

        // Splitting 2D array into smaller 2D arrays of chunkSize



        return input;
    }
}
