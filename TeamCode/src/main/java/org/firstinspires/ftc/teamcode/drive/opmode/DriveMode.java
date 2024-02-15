package org.firstinspires.ftc.teamcode.drive.opmode;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
@Config
@TeleOp(name="DriverOP-CSCC", group="Driver OP")
public class DriveMode extends LinearOpMode {
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;
    public static double reload_constant = 1.0;
    public static double rotate_constant = 0.49;
    public static double unrotate_constant = 0.21;
    public static double unrotate_constant2 = 0.26;

    public static double open_constant = 0.0;
    public static double close_constant = 0.489;
    public static double launch_constant = 0.3;
    public static double up_constant = 1.3;

    public static double leftChopstickP1 = 0.7;
    public static double leftChopstickP2 = 0.0;
    public static double rightChopstickP1 = 0.0;
    public static double rightChopstickP2 = 0.7;

    public static int chopstickSleep = 300;






    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    int numFramesWithoutDetection = 0;

    final float DECIMATION_HIGH = 3;
    final float DECIMATION_LOW = 2;
    final float THRESHOLD_HIGH_DECIMATION_RANGE_METERS = 1.0f;
    final int THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION = 4;
    //initializing stuff. Adds every non-drive servo and motor.
    // Drive motors are done on the sampleMecanumDrive opMode.
    private AprilTagProcessor aprilTag;


    private VisionPortal visionPortal;
    DcMotor intakeMotor;
    DcMotor linearextenderLeft;
    final double TICKS_PER_CENTIMETER = 537.7 / 11.2;

    DcMotor linearextenderRight;
    Servo paperAirplane;
    Servo extenderRotator;
    Servo extenderPlacer;
    Servo leftChopstick;
    Servo rightChopstick;

    ColorSensor pixelSensor;
    ColorSensor pixelSensor2;
    double theta = 0.06; //For testing box positions. See Trigger functions.
    boolean dontTilt = true; //safety feature. Prevents some unwanted actions, so Aiden doesn't break stuff again
    boolean safetyOverride = false; //Benji Feature BC he doesn't make mistakes :)
    public SampleMecanumDrive drive;
    @Override
    public void runOpMode() throws InterruptedException {
        //sets up drive
        drive = new SampleMecanumDrive(hardwareMap);
        //no encoders, so we do this:
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //now we need to map each initialized motor to the name assigned in the hardware map
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
        leftChopstick = hardwareMap.get(Servo.class, "leftChopstick");
        rightChopstick = hardwareMap.get(Servo.class, "rightChopstick");
        // and color sensors, too.
        pixelSensor = hardwareMap.get(ColorRangeSensor.class, "pixelSensor");
        pixelSensor2 = hardwareMap.get(ColorRangeSensor.class, "pixelSensor2");
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
        boolean streaming = true;
        //moving on initialization - positions box, powers plane launcher, opens door.
        unrotate();
        theta = 0;
        reload();
        open();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(1920,1080, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
// did you use the open cv april tag
            }
        });
        waitForStart(); //THIS OPMODE IS CONFIGURED FOR LINEAROPMODE. If this line is erroring, that may be the issue. Look up opmode vs linearopmode.


        telemetry.setMsTransmissionInterval(50);
        float prev_ly = (float) 0.0;
        float prev_lx = (float) 0.0;
        float prev_rx = (float) 0.0;
        boolean s = true;
        while (!isStopRequested()) {
            //THIS IS WIZARDRY. Someone please figure it out.
            if (s) {
                prev_ly =  gamepad1.left_stick_y;
                prev_lx =  gamepad1.left_stick_x;
                prev_rx =  gamepad1.right_stick_x;
                s = false;
            }
            drive.setWeightedDrivePower(
                    new Pose2d(
                            (gamepad1.left_stick_y + prev_ly)/2,
                            (gamepad1.left_stick_x+ prev_lx)/2,
                            (gamepad1.left_stick_x+ prev_rx)/2
                    )
            );
            prev_ly =  (gamepad1.left_stick_y + prev_ly)/2;
            prev_lx =  (gamepad1.left_stick_x+ prev_lx)/2;
            prev_rx =  (gamepad1.left_stick_x+ prev_lx)/2;

            drive.update();

            //telemetry for pixel sensor prototype. To be updated.

            if(pixelSensor.green()+ pixelSensor.red()+pixelSensor.blue()> 500){
                telemetry.addData("Pixel 1: ", "LOADED");}
            else{
                telemetry.addData("Pixel 1: ", "NONE");
            }
            if(pixelSensor2.green()+ pixelSensor2.red()+pixelSensor2.blue()> 500){
                telemetry.addData("Pixel 2: ", "LOADED");}
            else{
                    telemetry.addData("Pixel 1: ", "NONE");
            }



            telemetry.addData("RotatorPosition: ", extenderRotator.getPosition());
            telemetry.addData("theta: ", theta);

            if (gamepad1.y) {
                //sends extenders to max up position. Also sets safeguard and tilts box.
                maxHeight();


            } else if (gamepad1.b) {
                //ground position. Should move box to prevent serious breaking issues.
                groundHeight();
                telemetry.addData("Slides", "Zeroed");

            } else if (gamepad1.x) {
                //medium. See above.


                telemetry.addData("Slides", "Medium");
                close();
            } else if (gamepad1.a) {
                //low. See above.
                lowHeight();
            }


            if (gamepad1.right_bumper && (!dontTilt || safetyOverride )) {
                rotate();
            }
            if (gamepad1.left_bumper) {

                unrotate();

            }
            if (gamepad1.right_trigger > 0.5){
                // toggles intake.
                //to avoid funny issues this ony works when box is down. This also helps with power draw.
                startIntake();
                //extenderRotator.setPosition(0.215);//0.24+theta);
            }
            if(gamepad1.right_trigger> 0.5 && gamepad1.guide){
                //reverses intake if the XBOX button and activate trigger are pressed together.
                // This should help with jams.
               // reverseIntake();
                openChopsticks();
            }
            if (gamepad1.left_trigger > 0.5 ) {
                // toggles intake
                //stop always works. The intake auto stops when up.
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
            if (gamepad1.left_trigger > 0.3 && gamepad1.guide) {
                eatPixels();
            }
            if (gamepad2.dpad_up){
                unrotatemore();
                cHeight();
            }
            if (gamepad1.dpad_up) {
                ArrayList<org.openftc.apriltag.AprilTagDetection> detections = aprilTagDetectionPipeline.getDetectionsUpdate();
                if(detections != null)
                {
                    telemetry.addData("FPS", camera.getFps());
                    telemetry.addData("Overhead ms", camera.getOverheadTimeMs());
                    telemetry.addData("Pipeline ms", camera.getPipelineTimeMs());

                    // If we don't see any tags
                    if(detections.size() == 0)
                    {
                        numFramesWithoutDetection++;

                        // If we haven't seen a tag for a few frames, lower the decimation
                        // so we can hopefully pick one up if we're e.g. far back
                        if(numFramesWithoutDetection >= THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION)
                        {
                            aprilTagDetectionPipeline.setDecimation(DECIMATION_LOW);
                        }
                    }
                    // We do see tags!
                    else
                    {
                        numFramesWithoutDetection = 0;

                        // If the target is within 1 meter, turn on high decimation to
                        // increase the frame rate
                        if(detections.get(0).pose.z < THRESHOLD_HIGH_DECIMATION_RANGE_METERS)
                        {
                            aprilTagDetectionPipeline.setDecimation(DECIMATION_HIGH);
                        }

                        for(org.openftc.apriltag.AprilTagDetection detection : detections)
                        {
                            Orientation rot = Orientation.getOrientation(detection.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);
                            if (rot.firstAngle < 0) {
                                turnright((int) Math.abs(rot.firstAngle));
                                if ( detection.pose.x*FEET_PER_METER > 0){
                                    straferight(Math.abs( detection.pose.x*FEET_PER_METER/12));
                                }
                                else {
                                    strafeleft(Math.abs( detection.pose.x*FEET_PER_METER/12));
                                }
                            } else if (rot.firstAngle > 0) {
                                turnleft((int) Math.abs(rot.firstAngle));
                                if (detection.pose.x*FEET_PER_METER > 0){
                                    straferight(Math.abs(detection.pose.x*FEET_PER_METER/12));
                                }
                                else {
                                    strafeleft(Math.abs(detection.pose.x*FEET_PER_METER/12));
                                }
                            }
                            telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
                            telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
                            telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
                            telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
                            telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", rot.firstAngle));
                            telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", rot.secondAngle));
                            telemetry.addLine(String.format("Rotation Roll: %.2f degrees", rot.thirdAngle));
                        }
                    }
                    telemetry.addData("frames w/o det", numFramesWithoutDetection);

                    telemetry.update();
                }
                //tilt up for pixel stuck issue

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
            if (gamepad1.guide && gamepad1.dpad_up) {
                if (streaming) {
                    visionPortal.stopStreaming();
                    streaming = false;
                }
                else {

                    visionPortal.resumeStreaming();
                    streaming = true;
                }

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
            /*if(gamepad1.right_bumper && gamepad1.guide) {
                theta+=0.01; //This code exists to recalibrate the box. Uncomment to use.
                extenderRotator.setPosition(theta);
            }
            if(gamepad1.left_bumper && gamepad1.guide) {
                theta+=0.001; //This code exists to recalibrate the box. Uncomment to use.
                extenderRotator.setPosition(theta);
            }*/
            telemetry.update();
        }//END OF DRIVEROP LOOP
    }
    class movement {

    }
    private void initAprilTag() {

        // Create the AprilTag processor the easy way.
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();

        // Create the vision portal the easy way.
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    hardwareMap.get(WebcamName.class, "webcam"), aprilTag);

    }   // end method initAprilTag()

    /**
     * Add telemetry about AprilTag detections.
     */
    private void maxHeight() {
        dontTilt = false;
        extenderRotator.setPosition(0.19);//0.21+theta);

        linearextenderLeft.setTargetPosition((int) (65 * TICKS_PER_CENTIMETER));
        linearextenderRight.setTargetPosition((int) (65 * TICKS_PER_CENTIMETER));

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(0.9);
        linearextenderLeft.setPower(0.9); //I think all 3 commands here (target, mode, power) are needed.

        telemetry.addData("Slides", "HIGH");
        close();
    }
    private void midHeight() {
        dontTilt = false;
        extenderRotator.setPosition(0.19);//0.21+theta);

        linearextenderLeft.setTargetPosition((int) (50 * TICKS_PER_CENTIMETER));
        linearextenderRight.setTargetPosition((int) (50 * TICKS_PER_CENTIMETER));

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(1.0);
        linearextenderLeft.setPower(1.0);
    }
    private void cHeight() {
        dontTilt = false;

        linearextenderLeft.setTargetPosition((int) ( up_constant* TICKS_PER_CENTIMETER));
        linearextenderRight.setTargetPosition((int) (up_constant * TICKS_PER_CENTIMETER));

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(1.0);
        linearextenderLeft.setPower(1.0);
    }
    private void lowHeight() {
        unrotate();
        dontTilt = false;

        linearextenderLeft.setTargetPosition((int) (10 * TICKS_PER_CENTIMETER));
        linearextenderRight.setTargetPosition((int) (10 * TICKS_PER_CENTIMETER));

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(1.0);
        linearextenderLeft.setPower(1.0);

        telemetry.addData("Slides", "Low");
        unrotate();
        close();
    }
    private void groundHeight() {
        dontTilt = true;
        unrotate();
        open();
        linearextenderLeft.setTargetPosition(0);
        linearextenderRight.setTargetPosition(0);

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(1.0);
        linearextenderLeft.setPower(1.0);
    }
    private void telemetryAprilTag() {

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());
        telemetry.update();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null && detection.ftcPose.yaw < 0) {
                telemetry.addData("Yaw", detection.ftcPose.yaw);
                turnright((int) Math.abs(detection.ftcPose.yaw));
                if (detection.ftcPose.x > 0){
                    straferight(Math.abs(detection.ftcPose.x));
                }
                else {
                    strafeleft(Math.abs(detection.ftcPose.x));
                }
            } else if (detection.metadata != null && detection.ftcPose.yaw > 0) {
                telemetry.addData("Yaw", detection.ftcPose.yaw);
                turnleft((int) Math.abs(detection.ftcPose.yaw));
                if (detection.ftcPose.x > 0){
                    straferight(Math.abs(detection.ftcPose.x));
                }
                else {
                    strafeleft(Math.abs(detection.ftcPose.x));
                }
            }
        }
    }
    /*
    methods. They are separate from the buttons because sometimes they are called in
    multiple places, and to improve readability.
     */

    void turnright(int degrees){
        TrajectorySequence traj1 = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .turn(degrees)
                .build();
        drive.followTrajectorySequence(traj1);

    }

    void turnleft(int degrees){
        TrajectorySequence traj1 = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .turn(-degrees)
                .build();
        drive.followTrajectorySequence(traj1);

    }

    void strafeleft(double feet){
        TrajectorySequence traj1 = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .strafeLeft(feet)
                .build();
        drive.followTrajectorySequence(traj1);
    }
    void openChopsticks() {
        rightChopstick.setPosition(rightChopstickP1);
        leftChopstick.setPosition(leftChopstickP1);

    }
    void eatPixels() {

        rightChopstick.setPosition(rightChopstickP2);
        leftChopstick.setPosition(leftChopstickP2);


    }

    void straferight(double feet){
        drive.trajectorySequenceBuilder(new Pose2d(0,0))
                .strafeRight(feet)
                .build();
    }
    void reload() {
        paperAirplane.setPosition(reload_constant);
    }
    void rotate(){
        extenderRotator.setPosition(rotate_constant);//0.23+theta);
    }
    void unrotate(){
        extenderRotator.setPosition(unrotate_constant);//0.52+theta);
    }
    void unrotatemore(){
        extenderRotator.setPosition(unrotate_constant2);//0.52+theta);
    }

    void open(){
        extenderPlacer.setPosition(open_constant);
    }
    void close(){
        extenderPlacer.setPosition(close_constant);
    }
    /*void setPlane(){
        extenderPlacer.setPosition(0.0);
    }*/
    void launchPlane(){
        paperAirplane.setPosition(launch_constant);
    }
    void startIntake(){
        intakeMotor.setPower(1.0);
        //extenderRotator.setPosition(0.15);
    }
    void stopIntake(){
        intakeMotor.setPower(0.0);
        //unrotate();
    }
    void reverseIntake(){
        intakeMotor.setPower(-1.0);
    }
}
