package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
@Config
public class pixelStick extends LinearOpMode {
    double stickConstant;
    Servo pixelStick;

    @Override
    public void runOpMode() throws InterruptedException {
        while(opModeIsActive() && !isStopRequested()) {
            if(gamepad1.a) {
                pixelStick.setPosition(stickConstant);
            }
        }
    }
}
