package org.firstinspires.ftc.teamcode.drive.opmode;

import android.hardware.Sensor;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
@Config
public class pixelSensor extends LinearOpMode {

    ColorRangeSensor pixelSensor;
    ColorRangeSensor pixelSensor2;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while(opModeIsActive() && !isStopRequested()) {
            telemetry.addData("pixelSensor argb", pixelSensor.argb());
            telemetry.addData("pixelSensor2 argb", pixelSensor2.argb());
        }
    }
}
