package org.firstinspires.ftc.teamcode.drive.opmode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp (name ="planeTester", group = "testing")
public class planeTester extends OpMode {
    Servo plane;

    @Override
    public void init() { // runs at the beginning, init is short of "initialize"
        // hardwareMap belows to OpMode, so it doesn't need to be defined by the child
        plane = hardwareMap.get(Servo.class, "servo");
//        clawMA1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    @Override
    public void loop() {
        telemetry.addData("servo position", plane.getPosition());

        if (gamepad1.left_bumper) {
            plane.setPosition(1.0);
            telemetry.addData("Bumper closed", true);
            telemetry.update();

        }
        if (gamepad1.right_bumper) {
            plane.setPosition(0.3);
            telemetry.addData("Bumper open", true);
            telemetry.update();

        }
    }
    // code for right
    /*        if (gamepad1.left_bumper) {
            servo.setPosition(0.2);
            telemetry
            .addData("Bumper closed", true);
            telemetry.update();

        }
        if (gamepad1.right_bumper) {
            servo.setPosition(0.8);
            telemetry.addData("Bumper open", true);
            telemetry.update();
            */


//

}