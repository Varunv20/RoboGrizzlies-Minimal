package org.firstinspires.ftc.teamcode;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="DriverOP-CSM3", group="Driver OP")
public class NewDriveMode extends LinearOpMode {
    //initializing stuff. Adds every non-drive servo and motor.
    // Drive motors are done on the sampleMecanumDrive opMode.
    DcMotor intakeMotor;
    DcMotor linearextenderLeft;
    DcMotor linearextenderRight;
    Servo paperAirplane;
    Servo extenderRotator;
    Servo extenderPlacer;
    ColorSensor pixelSensor;
    //double theta = 0; //For testing box positions. See Trigger functions.
    boolean dontTilt = true; //safety feature. Prevents some unwanted actions, so Aiden doesn't break stuff again
    boolean safetyOverride = false; //Benji Feature BC he doesn't make mistakes :)

    @Override
    public void runOpMode() throws InterruptedException {
        //sets up drive
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        //no encoders, so we do this:
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //now we need to map each initalized motor to the name assigned in the hardware map
        //which is just the config.
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        linearextenderLeft = hardwareMap.get(DcMotor.class, "linearextenderLeft");
        linearextenderRight = hardwareMap.get(DcMotor.class, "linearextenderRight");

        linearextenderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearextenderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Servos also need to be mapped!
        extenderRotator = hardwareMap.get(Servo.class, "extenderRotator");
        extenderPlacer = hardwareMap.get(Servo.class, "extenderPlacer");
        paperAirplane = hardwareMap.get(Servo.class, "paperAirplane");
        // and color sensors, too.
        pixelSensor = hardwareMap.get(ColorRangeSensor.class, "pixelSensor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearextenderLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //controls what these do when not actively going somewhere. Usually extenders should be BRAKE.
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearextenderLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearextenderRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Now and then gobilda motors get reversed. It's a known bug and the inelegant solution is to reverse them here too.
        intakeMotor.setDirection(DcMotor.Direction.REVERSE);
        linearextenderRight.setDirection(DcMotor.Direction.REVERSE);

        /*This value corresponds encoder units to distance. Ticks per rotation is a function of a motor's
        drive encoder. Find wheel radius and solve for ticks per unit distance.
        I quite honestly have no idea if this is still correct. We arent using it meaningfully.
         */
        final double TICKS_PER_CENTIMETER = 537.7 / 11.2;

        //moving on initialization - positions box, powers plane launcher, opens door.
        unrotate();
        setPlane();
        open();
        //extenderRotator.setPosition(theta); //Testing holdover
        waitForStart(); //THIS OPMODE IS CONFIGURED FOR LINEAROPMODE. If this line is erroring, that may be the issue. Look up opmode vs linearopmode.


        while (!isStopRequested()) {
            //THIS IS WIZARDRY. Someone please figure it out.
            drive.setWeightedDrivePower(
                    new Pose2d(
                            gamepad1.left_stick_y,
                            gamepad1.left_stick_x,
                            gamepad1.right_stick_x
                    )
            );

            drive.update();

            //telemetry for pixel sensor prototype. To be updated.
            telemetry.addData("Red: ", pixelSensor.red());
            telemetry.addData("Green: ", pixelSensor.green());
            telemetry.addData("Blue: ", pixelSensor.blue());
            telemetry.addData("RotatorPosition: ", extenderRotator.getPosition());

            if (gamepad1.y) {
                //sends encoders to max up position. Also sets safeguard and tilts box.
                dontTilt = false;
                extenderRotator.setPosition(0.24);

                linearextenderLeft.setTargetPosition((int) (65 * TICKS_PER_CENTIMETER));
                linearextenderRight.setTargetPosition((int) (65 * TICKS_PER_CENTIMETER));

                linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                linearextenderRight.setPower(0.9);
                linearextenderLeft.setPower(0.9); //I think all 3 commands here (target, mode, power) are needed.

                telemetry.addData("Slides", "HIGH");
                close();


            } else if (gamepad1.b) {
                //ground position. Should move box to prevent serious breaking issues.
                dontTilt = true;
                unrotate();
                open();

                linearextenderLeft.setTargetPosition(0);
                linearextenderRight.setTargetPosition(0);

                linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                linearextenderRight.setPower(0.9);
                linearextenderLeft.setPower(0.9);
                telemetry.addData("Slides", "Zeroed");

            } else if (gamepad1.x) {
                //close();
                //medium. See above.
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
                //low. See above.
                unrotate();
                dontTilt = false;
                extenderRotator.setPosition(0.25);

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


            if (gamepad1.right_bumper && (!dontTilt || safetyOverride )) {
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
                //to avoid funny issues this ony works when box is down. This also helps with power draw.
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
                //Toggle switch for safeguard override. see below.
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
                //lowers extenders. Meant to be used with above function. BE CAREFUL WHEN USING.
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
        }//END OF DRIVEROP LOOP
    }
    /*
    methods. They are separate from the buttons because sometimes they are called in
    multiple places, and to improve readability.
     */
    void reload() {
        paperAirplane.setPosition(1.0);
    }
    void unrotate(){
        extenderRotator.setPosition(0.23);
    }
    void rotate(){
        extenderRotator.setPosition(0.52);
    }
    void open(){
        extenderPlacer.setPosition(0.0);
    }
    void close(){
        extenderPlacer.setPosition(0.489);
    }
    void setPlane(){
        extenderPlacer.setPosition(0.0);
    }
    void launchPlane(){
        paperAirplane.setPosition(0.3);
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