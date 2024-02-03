package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="motorTester", group="Driver OP")

public class motorPower extends LinearOpMode {
    DcMotor motor;
    @Override
    public void runOpMode() throws InterruptedException {

        motor = hardwareMap.get(DcMotor.class, "motor");
        waitForStart();
        if (gamepad1.left_bumper) {
            motor.setPower(1.0);
        }
        if (gamepad1.right_bumper) {
            motor.setPower(-1.0);
        }
    }


}
