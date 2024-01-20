package org.firstinspires.ftc.teamcode.drive.opmode;
// package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

//import com.noahbres.meepmeep.MeepMeep;
//import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
//import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

// openCV stuff
import org.firstinspires.ftc.teamcode.computervision.eocvTeamProp;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;



import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class farRedAUTO extends LinearOpMode {
    public Servo paperAirplane;
    public Servo extenderRotator;
    public Servo extenderPlacer;
    public DcMotor linearextenderLeft;
    public DcMotor linearextenderRight;
    public DcMotor intakeMotor;
    OpenCvWebcam webcam;
    enum State {
        traj1,
        traj2,
        traj3,
        traj4,
        traj5,
        traj6,
        traj7,
        traj8,
        traj9,
        Idle

    }


    State currentState = State.Idle;
    public void onOpened()
    {
        /*
         * Tell the webcam to start streaming images to us! Note that you must make sure
         * the resolution you specify is supported by the camera. If it is not, an exception
         * will be thrown.
         *
         * Keep in mind that the SDK's UVC driver (what OpenCvWebcam uses under the hood) only
         * supports streaming from the webcam in the uncompressed YUV image format. This means
         * that the maximum resolution you can stream at and still get up to 30FPS is 480p (640x480).
         * Streaming at e.g. 720p will limit you to up to 10FPS and so on and so forth.
         *
         * Also, we specify the rotation that the webcam is used in. This is so that the image
         * from the camera sensor can be rotated such that it is always displayed with the image upright.
         * For a front facing camera, rotation is defined assuming the user is looking at the screen.
         * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
         * away from the user.
         */
        webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
        telemetry.addData("S","STREAMING");
        telemetry.update();
    }

    public void runOpMode() { //static
        //MeepMeep meepMeep = new MeepMeep(800);

        // RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
        // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
        // .setConstraints(85, 85, Math.toRadians(180), Math.toRadians(180), 15)
        // .followTrajectorySequence(drive ->
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startpos = new Pose2d(-36, -63.25, Math.toRadians(270));
        drive.setPoseEstimate(startpos);
        // cv stuff
        // runtime.reset();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);
        eocvTeamProp pipeline = new eocvTeamProp();
        webcam.setPipeline(pipeline);
        TrajectorySequence  traj1;
        TrajectorySequence  traj2;
        TrajectorySequence  traj3;
        TrajectorySequence  traj4;
        TrajectorySequence  traj5;
        TrajectorySequence  traj6;
        TrajectorySequence  traj7;
        TrajectorySequence  traj8;
        TrajectorySequence  traj9;


        /*
         * Open the connection to the camera device. New in v1.4.0 is the ability
         * to open the camera asynchronously, and this is now the recommended way
         * to do it. The benefits of opening async include faster init time, and
         * better behavior when pressing stop during init (i.e. less of a chance
         * of tripping the stuck watchdog)
         *
         * If you really want to open synchronously, the old method is still available.
         */
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()

        {
            @Override
            public void onOpened()
            {

                webcam.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
                telemetry.addData("S","STREAMING");
                telemetry.update();
            }

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
                telemetry.addData("e",errorCode);
                telemetry.update();
            }
        });
        pipeline.red = true;
        waitForStart();
        pipeline.setRun();
        while (pipeline.run) {
            sleep(100);
        }
        String result = pipeline.getResult();
        if (result == "right") {
            traj1 = drive.trajectorySequenceBuilder(startpos)
                    .lineToLinearHeading(new Pose2d(-48,-35, Math.toRadians(270)))
                    .forward(4)
                    .lineToLinearHeading(new Pose2d(43, -30, Math.toRadians(180)))
                    .build();

        }
        else if (result == "left") {
            traj1 = drive.trajectorySequenceBuilder(startpos)
                    .lineToLinearHeading(new Pose2d(-24,-32.75, Math.toRadians(90)))
                    .forward(4)
                    .lineToLinearHeading(new Pose2d(43, -30, Math.toRadians(180)))
                    .build();
        }
        else {
            traj1 = drive.trajectorySequenceBuilder(startpos)
                    .lineToLinearHeading(new Pose2d(-36,-32.75, Math.toRadians(90)))
                    .forward(4)
                    .lineToLinearHeading(new Pose2d(43, -36, Math.toRadians(180)))
                    .build();
        }
        traj2 =  drive.trajectorySequenceBuilder(traj1.end())
                .forward(5)
                .build();
        traj3 =  drive.trajectorySequenceBuilder(traj1.end())
                .back(5)
                .build();

        traj4 =  drive.trajectorySequenceBuilder(traj2.end())
                .splineTo(new Vector2d(10, -12), Math.toRadians(180))
                .splineTo(new Vector2d(-55, -12), Math.toRadians(180))
                .build();
        traj5 =  drive.trajectorySequenceBuilder(traj2.end())
                .forward(5)
                .build();
        traj6 =  drive.trajectorySequenceBuilder(traj2.end())
                .back(5)
                .build();
        traj7 = drive.trajectorySequenceBuilder(traj2.end())
                .lineToLinearHeading(new Pose2d(-54, -60, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(50, -60, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(50, -43, Math.toRadians(180)))
                .build();
        traj8 = traj2;

        if(isStopRequested()) return;
        currentState = State.traj1;
        drive.followTrajectorySequence(traj1);
        while (opModeIsActive() && !isStopRequested()) {
            switch (currentState) {
                case traj1:

                    if (!drive.isBusy()) {
                        currentState = State.traj2;
                        maxHeight();
                        close();
                        drive.followTrajectorySequence(traj2);
                        //do code
                    }
                    break;
                case traj2:

                    if (!drive.isBusy()) {
                        rotate();
                        open();
                        sleep(1000);
                        unrotate();
                        groundHeight();
                        currentState = State.traj3;
                        drive.followTrajectorySequence(traj3);
                        //do code
                    }
                    break;
                case traj3:


                    if (!drive.isBusy()) {

                        currentState = State.traj4;
                        drive.followTrajectorySequence(traj4);
                        //do code
                    }
                    break;
                case traj4:

                    if (!drive.isBusy()) {
                        startIntake();
                        currentState = State.traj5;
                        drive.followTrajectorySequence(traj5);
                        //do code
                    }
                    break;
                case traj5:

                    if (!drive.isBusy()) {
                        stopIntake();
                        currentState = State.traj6;
                        drive.followTrajectorySequence(traj6);

                        //up+place+open
                        //do code
                    }
                    break;

                case traj6:

                    if (!drive.isBusy()) {
                        currentState = State.traj7;
                        drive.followTrajectorySequence(traj7);

                        //up+place+open
                        //do code
                    }
                    break;
                case traj7:

                    if (!drive.isBusy()) {
                        close();
                        maxHeight();
                        currentState = State.traj8;
                        drive.followTrajectorySequence(traj8);

                        //up+place+open
                        //do code
                    }
                    break;
                case traj8:

                    if (!drive.isBusy()) {
                        rotate();
                        open();
                        currentState = State.Idle;

                        //up+place+open
                        //do code
                    }
                    break;

                case Idle:
                    break;
            }

        }

        //);
        /*
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();

         */
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
    void startIntake(){
        intakeMotor.setPower(1.0);
        extenderRotator.setPosition(0.15);
    }
    void stopIntake(){
        intakeMotor.setPower(0.0);
    }
    void reverseIntake(){
        intakeMotor.setPower(-1.0);
    }

    // for the extenders
    final double TICKS_PER_CENTIMETER = 537.7 / 11.2;
    void maxHeight() {
        //sends extenders to max up position. Also sets safeguard and tilts box.
        // I commented this safety feature out. What could go wrong?
        //dontTilt = false;
        extenderRotator.setPosition(0.24);

        linearextenderLeft.setTargetPosition((int) (65 * TICKS_PER_CENTIMETER));
        linearextenderRight.setTargetPosition((int) (65 * TICKS_PER_CENTIMETER));

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(0.9);
        linearextenderLeft.setPower(0.9); //I think all 3 commands here (target, mode, power) are needed.

        telemetry.addData("Slides", "HIGH");
        close();
    }
    void groundHeight() {
        //ground position. Should move box to prevent serious breaking issues.
        // I commented this safety feature out. What could go wrong?
        // dontTilt = true;
        unrotate();
        open();

        linearextenderLeft.setTargetPosition(0);
        linearextenderRight.setTargetPosition(0);

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(0.9);
        linearextenderLeft.setPower(0.9);
        telemetry.addData("Slides", "Zeroed");
    }
    void lowHeight() {
        //low. See above.
        unrotate();
        // I commented this safety feature out. What could go wrong?
        //dontTilt = false;
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
}
