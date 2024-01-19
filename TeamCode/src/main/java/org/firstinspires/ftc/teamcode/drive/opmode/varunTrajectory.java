package org.firstinspires.ftc.teamcode.drive.opmode;
// package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower;
import com.acmerobotics.roadrunner.followers.TrajectoryFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.acmerobotics.roadrunner.util.NanoClock;

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
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class varunTrajectory extends LinearOpMode {
    public Servo paperAirplane;
    public Servo extenderRotator;
    public Servo extenderPlacer;
    public DcMotor linearextenderLeft;
    public DcMotor linearextenderRight;
    public DcMotor intakeMotor;
    OpenCvWebcam webcam;
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
        drive.setPoseEstimate(new Pose2d(12, -63.25, Math.toRadians(270)));
        // cv stuff
        // runtime.reset();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);
        // end cv stuff
        TrajectorySequence varunTrajectory = drive.trajectorySequenceBuilder(new Pose2d(12, 63, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(0,25, Math.toRadians(90)))
                //.addDisplacementMarker(() -> {
                //})
                .lineToLinearHeading(new Pose2d(0, 36, Math.toRadians(180)))

                .lineTo(new Vector2d(53, 30))

                //up pixel
                .forward(5)
                //  .splineTo(new Vector2d(0, 6), Math.toRadians(180))
                .splineTo(new Vector2d(10, 12), Math.toRadians(180))
                .splineTo(new Vector2d(-60, 12), Math.toRadians(180))
                .back(6)
                .lineToLinearHeading(new Pose2d(-54, 60, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(50, 60, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(50, 40, Math.toRadians(180)))
                .back(5)
                .build();


                                // .back(5)
                                //intake
                                // .forward(5)
                                // .splineTo(new Vector2d(0, 6), Math.toRadians(180))

                                // .splineTo(new Vector2d(53, 30), Math.toRadians(0))
        waitForStart();
        if(isStopRequested()) return;

        drive.followTrajectorySequence(varunTrajectory);
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
