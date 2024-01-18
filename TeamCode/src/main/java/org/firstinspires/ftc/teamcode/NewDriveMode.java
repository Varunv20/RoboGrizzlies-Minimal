
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


@TeleOp(name="ExperimentalDrive", group="Driver OP")
public class NewDriveMode extends LinearOpMode {
    DcMotor intakeMotor;

    DcMotor linearextenderLeft;
    DcMotor linearextenderRight;
    Servo paperAirplane;
    Servo extenderRotator;
    Servo extenderPlacer;
    ColorSensor pixelSensor;
    double theta = 0; //For testing box positions. See Trigger functions.
    boolean dontTilt = true;
    boolean safetyOverride = false;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        linearextenderLeft = hardwareMap.get(DcMotor.class, "linearextenderLeft");
        linearextenderRight = hardwareMap.get(DcMotor.class, "linearextenderRight");

        linearextenderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearextenderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        extenderRotator = hardwareMap.get(Servo.class, "extenderRotator");
        extenderPlacer = hardwareMap.get(Servo.class, "extenderPlacer");
        paperAirplane = hardwareMap.get(Servo.class, "paperAirplane");

        pixelSensor = hardwareMap.get(ColorRangeSensor.class, "pixelSensor");
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

        unrotate();
        setPlane();
        open();

        waitForStart();
        //extenderRotator.setPosition(theta); //Testing holdover
        //THIS IS WIZARDRY. Someone please figure it out.
        while (!isStopRequested()) {
            drive.setWeightedDrivePower(
                    new Pose2d(
                            gamepad1.left_stick_y,
                            gamepad1.left_stick_x,
                            gamepad1.right_stick_x
                    )
            );

            drive.update();


            telemetry.addData("Red: ", pixelSensor.red());
            telemetry.addData("Green: ", pixelSensor.green());
            telemetry.addData("Blue: ", pixelSensor.blue());
            telemetry.addData("RotatorPosition: ", extenderRotator.getPosition());

            if (gamepad1.y) {
                dontTilt = false;
                extenderRotator.setPosition(0.24);

                linearextenderLeft.setTargetPosition((int) (65 * TICKS_PER_CENTIMETER));
                linearextenderRight.setTargetPosition((int) (65 * TICKS_PER_CENTIMETER));
                linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setPower(0.9);
                linearextenderLeft.setPower(0.9);
                telemetry.addData("Slides", "HIGH");
                close();


            } else if (gamepad1.b) {
                //open();
                dontTilt = true;
                unrotate();
                open();
                linearextenderLeft.setTargetPosition((int) (0));
                linearextenderRight.setTargetPosition((int) (0));
                linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setPower(0.9);
                linearextenderLeft.setPower(0.9);
                telemetry.addData("Slides", "Zeroed");

            } else if (gamepad1.x) {
                //close();
                dontTilt = false;
                extenderRotator.setPosition(0.24);
                linearextenderLeft.setTargetPosition((int) (50 * TICKS_PER_CENTIMETER));
                linearextenderRight.setTargetPosition((int) (50 * TICKS_PER_CENTIMETER));
                linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setPower(0.9);
                linearextenderLeft.setPower(0.9);
                telemetry.addData("Slides", "Medium");
                unrotate();
                close();
            } else if (gamepad1.a) {
                unrotate();
                dontTilt = false;
                extenderRotator.setPosition(0.24);
                linearextenderLeft.setTargetPosition((int) (10 * TICKS_PER_CENTIMETER));
                linearextenderRight.setTargetPosition((int) (10 * TICKS_PER_CENTIMETER));
                linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                linearextenderRight.setPower(0.9);
                linearextenderLeft.setPower(0.9);

                telemetry.addData("Slides", "Low");
                unrotate();
                close();
            }


            if (gamepad1.right_bumper && !dontTilt) {
                //theta+=0.001; //This code exists to recalibrate the box. Uncomment to use.
                //extenderRotator.setPosition(theta);
                //DontTilt is a boolean which is true when the box is on the ground.
                rotate();
            }
            if (gamepad1.left_bumper) {
                //theta+=0.01; //This code exists to recalibrate the box. Uncomment to use.
                //extenderRotator.setPosition(theta);
                unrotate();

            }
            if (gamepad1.right_trigger > 0.5&&(dontTilt||safetyOverride)){
                // toggles intake.
                //to avoid funny issues this ony works when box is down.
                startIntake();
                extenderRotator.setPosition(0.24);


            }
            if(gamepad1.right_trigger> 0.5 && gamepad1.guide &&(dontTilt||safetyOverride)){
                //reverses intake if the XBOX button and activate trigger are pressed together.
                // This should help with jams.
                reverseIntake();
            }
            if (gamepad1.left_trigger > 0.5 && (dontTilt||safetyOverride/*DeMorgan's Laws W*/)) {
                // toggles intake
                //stop always works. The box auto stops when up.
                stopIntake();
                unrotate();

            }
            if (gamepad1.dpad_right){
                // door control. Fairly intuitive.
                open();

            }
            if (gamepad1.dpad_left && (!dontTilt||safetyOverride)) {
                // also door control. Again Don'tTilt is used to prevent errors.
                close();
            }
            if (gamepad1.dpad_down) {
                launchPlane();
                //Self Explanatory, no? this is the benefit of not naming everything beans.
            }
            if (gamepad1.dpad_down) {
                reload();
                //This will never matter in game (probably). It's for practice only.
            }
            if (gamepad1.left_stick_button && gamepad1.right_stick_button){
                safetyOverride = !safetyOverride;
                //see below.
            }
            if(safetyOverride){
                dontTilt = false;
                telemetry.addData("SAFETY OVERRIDE", "ALL SAFEGUARDS DISABLED!!!");
                //the "I KNOW WHAT I'M DOING" method. Use to override dontTilt and all associated safety features.
            }
            if(gamepad1.start){
                linearextenderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                linearextenderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                telemetry.addData("ENCODERS RESET", "!");
                //Resets encoder zero position. Use when having issues.
            }
            if(gamepad1.back){
                //lowers extenders. Meant to be used with above function. BE CAREFUL WHEN USING
                unrotate();
                linearextenderLeft.setTargetPosition((int) (linearextenderLeft.getTargetPosition()-1* TICKS_PER_CENTIMETER));
                linearextenderRight.setTargetPosition((int) (linearextenderRight.getTargetPosition()-1 * TICKS_PER_CENTIMETER));
                linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                linearextenderRight.setPower(0.9);
                linearextenderLeft.setPower(0.9);

                telemetry.addData("EMERGENCY DOWNSHIFT", "!!!!!");

            }

            telemetry.update();
        }
    }

    void reload() {
        paperAirplane.setPosition(1.0);
    }
    void unrotate(){
        extenderRotator.setPosition(0.214);
        // sleep(100);
    }
    void rotate(){
        extenderRotator.setPosition(0.52);
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
        extenderRotator.setPosition(0.195);
    }
    void stopIntake(){
        intakeMotor.setPower(0.0);
    }
    void reverseIntake(){
        intakeMotor.setPower(-1.0);
    }
}


