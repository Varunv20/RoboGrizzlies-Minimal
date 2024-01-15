
package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="MainRobotCode", group="Driver OP")
public class NewDriveMode extends LinearOpMode {
    DcMotor intakeMotor;

    DcMotor linearextenderLeft;
    DcMotor linearextenderRight;
    Servo paperAirplane;
    Servo extenderRotator;
    Servo extenderPlacer;
    ColorSensor pixelSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



            intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
            linearextenderLeft = hardwareMap.get(DcMotor.class, "linearextenderLeft");
            linearextenderRight = hardwareMap.get(DcMotor.class, "linearextenderRight");

            extenderRotator = hardwareMap.get(Servo.class, "extenderRotator");
            extenderPlacer = hardwareMap.get(Servo.class, "extenderPlacer");
            paperAirplane = hardwareMap.get(Servo.class, "paperAirplane");

            pixelSensor = hardwareMap.get(ColorRangeSensor.class,"pixelSensor");
            intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            linearextenderLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            linearextenderRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            intakeMotor.setDirection(DcMotor.Direction.REVERSE);
            linearextenderRight.setDirection(DcMotor.Direction.REVERSE);



            final double TICKS_PER_CENTIMETER = 537.7 / 11.2;
            //final double CENTIMETERS_PER_TICK = 1 / TICKS_PER_CENTIMETER;

            //final double MAX_SLIDE_HEIGHT = 50;
            unrotate();
            setPlane();
            open();

        waitForStart();

        while (!isStopRequested()) {
            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );

            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
        }
        telemetry.addData("Red: ", pixelSensor.red());
        telemetry.addData("Green: ", pixelSensor.green());
        telemetry.addData("Blue: ", pixelSensor.blue());

        if (gamepad1.y) {
            extenderRotator.setPosition(0.14);

            linearextenderLeft.setTargetPosition((int) (65*TICKS_PER_CENTIMETER));
            linearextenderRight.setTargetPosition((int) (65*TICKS_PER_CENTIMETER));
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setPower(0.9);
            linearextenderLeft.setPower(0.9);
            telemetry.addData("Slides to 65cm", "0");
            close();


        }
        else if (gamepad1.b) {
            //open();
            unrotate();
            open();
            linearextenderLeft.setTargetPosition((int) (0));
            linearextenderRight.setTargetPosition((int) (0));
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setPower(0.9);
            linearextenderLeft.setPower(0.9);
            telemetry.addData("Slides to 0cm","0");

        }
        else if (gamepad1.x) {
            //close();
            extenderRotator.setPosition(0.14);
            linearextenderLeft.setTargetPosition((int) (50*TICKS_PER_CENTIMETER));
            linearextenderRight.setTargetPosition((int) (50*TICKS_PER_CENTIMETER));
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setPower(0.9);
            linearextenderLeft.setPower(0.9);
            telemetry.addData("Slides to 50cm", "0");
            extenderRotator.setPosition(0.1);
            close();
        }
        else if (gamepad1.a) {
            unrotate();
            //telemetry.addData("accessed: lin_ex2", "0");

            linearextenderLeft.setTargetPosition((int) (10 * TICKS_PER_CENTIMETER));
            linearextenderRight.setTargetPosition((int) (10 * TICKS_PER_CENTIMETER));
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            linearextenderRight.setPower(0.9);
            linearextenderLeft.setPower(0.9);

            telemetry.addData("Slides to 10cm", "0");
            extenderRotator.setPosition(0.1);
            close();
        }


        if (gamepad1.right_bumper) {
            rotate();
        }
        if (gamepad1.left_bumper) {
            unrotate();
            // left and right bumper swapped after first comp
        }
        if (gamepad1.right_trigger > 0.5) {
            // toggles intake
            startIntake();


        }
        if(gamepad1.left_trigger > 0.5) {
            // toggles intake
            stopIntake();
            unrotate();

        }
        if(gamepad1.dpad_right ) {
            // toggles intake
            open();

        }
        if(gamepad1.dpad_left) {
            // toggles intake
            close();
        }
        if (gamepad1.dpad_down) {
            launchPlane();
        }
        if (gamepad1.dpad_down) {
            reload();
        }

        telemetry.update();
        }


    void reload() {
        paperAirplane.setPosition(1.0);
    }
    void unrotate(){
        extenderRotator.setPosition(0.18);
        // sleep(100);
    }
    void rotate(){
        extenderRotator.setPosition(0.5);
        //    sleep(100);
    }
    void open(){
        extenderPlacer.setPosition(0.0);
        //     sleep(100);
    }
    void close(){
        extenderPlacer.setPosition(0.489);
        //    sleep(100);
    }
    void setPlane(){
        extenderPlacer.setPosition(0.0);
        //    sleep(100);
    }
    void launchPlane(){
        paperAirplane.setPosition(0.3);
        //    sleep(100);
    }
    void startIntake(){
        intakeMotor.setPower(1.0);
        extenderRotator.setPosition(0.21);
    }
    void stopIntake(){
        intakeMotor.setPower(0.0);
    }
    void reverseIntake(){
        intakeMotor.setPower(-1.0);
    }
}


