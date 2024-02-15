package org.firstinspires.ftc.teamcode.drive.opmode;
// package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.noahbres.meepmeep.MeepMeep;
//import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
//import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

// openCV stuff
import org.firstinspires.ftc.teamcode.computervision.eocvTeamProp;
import org.firstinspires.ftc.teamcode.computervision.robotDetection;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.drive.opmode.AutoTrajectories;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import com.acmerobotics.dashboard.config.Config;

import java.util.List;

@Config
public class AutoMachine  {
    public Servo paperAirplane;
    public Servo extenderRotator;
    public Servo extenderPlacer;
    public DcMotor linearextenderLeft;
    public DcMotor linearextenderRight;
    public DcMotor intakeMotor;

    public Servo claws;
    public static double reload_constant = 1.0;
    public static double rotate_constant = 0.49;
    public static double unrotate_constant = 0.21;
    public static double unrotate_constant2 = 0.26;

    public static double open_constant = 0.0;
    public static double close_constant = 0.489;
    public static double launch_constant = 0.3;
    public static double up_constant = 1.3;
    public static double leftChopstickP1 = 0.6;
    public static double leftChopstickP2 = 0.0;
    public static double rightChopstickP1 = 0.6;
    public static double rightChopstickP2 = 0.0;
    public static double sleep1 = 9000;
    public static double sleep2 = 1000;
    public static double sleep3 = 9000;


    final double TICKS_PER_CENTIMETER = 537.7 / 11.2;

    Servo leftChopstick;
    Servo rightChopstick;
    OpenCvWebcam webcam;
        enum State {
            traj1,
            traj2,
            traj3,
            traj4,
            traj5,
            traj5B,
            traj6,
            traj65,
            traj7,
            traj8,
            traj9,
            park,
            Idle

        }
        State currentState = State.Idle;
        AutoTrajectories a;
    SampleMecanumDrive drive;


    public   void runAUTO(   LinearOpMode l , Boolean close, Boolean red, Boolean parkoutside, Boolean cycle){

            drive = new SampleMecanumDrive(l.hardwareMap);
            intakeMotor = l.hardwareMap.get(DcMotor.class, "intakeMotor");
            linearextenderLeft = l.hardwareMap.get(DcMotor.class, "linearextenderLeft");
            linearextenderRight = l.hardwareMap.get(DcMotor.class, "linearextenderRight");

            linearextenderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            linearextenderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //Servos also need to be mapped!
            extenderRotator = l.hardwareMap.get(Servo.class, "extenderRotator");
            extenderPlacer = l.hardwareMap.get(Servo.class, "extenderPlacer");
            paperAirplane = l.hardwareMap.get(Servo.class, "paperAirplane");
              leftChopstick = l.hardwareMap.get(Servo.class, "leftChopstick");
            rightChopstick = l.hardwareMap.get(Servo.class, "rightChopstick");
            // and color sensors, too.
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
            int cameraMonitorViewId = l.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", l.hardwareMap.appContext.getPackageName());
            webcam = OpenCvCameraFactory.getInstance().createWebcam(l.hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);
            final double TICKS_PER_CENTIMETER = 537.7 / 11.2;
            AutoTrajectories a = new AutoTrajectories();
            a.drive = drive;
            a.op = this;
            if (red) {
                a.setRed();
            }
            if (close) {
                a.setClose();

            }
            a.setStartpos();
            drive.setPoseEstimate(a.startpos);
            eocvTeamProp pipeline = new eocvTeamProp();
            robotDetection r = new robotDetection();
            webcam.setPipeline(pipeline);

            webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()

            {
                @Override
                public void onOpened()
                {

                    webcam.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);

                }

                @Override
                public void onError(int errorCode)
                {
                    /*
                     * This will be called if the camera could not be opened
                     */

                }
            });
            pipeline.red = false;
            l.waitForStart();

            pipeline.setRun();
            while (pipeline.run) {
                l.sleep(100);
            }
            String result = pipeline.getResult();
            webcam.setPipeline( r);


            TrajectorySequence traj1;
            TrajectorySequence  traj2;
            TrajectorySequence  traj3;
            TrajectorySequence  traj4;
            TrajectorySequence  traj5;
            TrajectorySequence traj4e;
            TrajectorySequence  traj6;
            TrajectorySequence  traj65;

            TrajectorySequence  traj7;
            TrajectorySequence  traj8;
            TrajectorySequence  traj9;
            TrajectorySequence  park;

            traj1 = a.getTraj1(result);
            traj2 = a.getTraj2();
            traj3 = a.getTraj3();
            traj4 = a.getTraj4();
            traj4e = a.getTraj4Edge();
            traj5 = a.getTraj5();
            traj6 = a.getTraj6();
            traj65 = a.getTraj65();
            traj7 = a.getTraj7();
            traj8 = a.getTraj8();
            if (parkoutside) {
                park = a.getParkOutside();
            }
            else {
                park = a.getParkCenter();
            }


            if(l.isStopRequested()) return;
            if (close) {
                lowHeight();
            }

            //stickUp();
            currentState = State.traj1;
            drive.followTrajectorySequence(traj1);
            Long t = System.currentTimeMillis();
            while (l.opModeIsActive() && !l.isStopRequested()) {
                switch (currentState) {

                    case traj1:
                        Long t2 = System.currentTimeMillis();
                        if (t2-t > sleep1) {
                            lowHeight();
                        }

                        unrotate();
                        if (!drive.isBusy()) {

                            currentState = State.traj2;
                            lowHeight();
                            rotate();
                            close();
                            drive.followTrajectorySequence(traj2);
                            //do code
                        }
                        break;
                    case traj2:

                        if (!drive.isBusy()) {

                            open();
                            l.sleep((long) sleep2);
                            // unrotate();
                            //  groundHeight();


                            //  unrotate();
                            // groundHeight();
                            currentState = State.traj3;
                            drive.followTrajectorySequence(traj3);
                            //do code
                        }
                        break;
                    case traj3:


                        if (!drive.isBusy()) {
                            groundHeight();


                            if (cycle) {
                                currentState = State.traj4;

                                r.run = true;
                                while (r.run) {
                                    l.sleep(100);
                                }
                                if (r.center) {
                                    drive.followTrajectorySequence(traj4);

                                }
                                else {
                                    drive.followTrajectorySequence(traj4e);
                                }

                            }
                            else {
                                currentState= State.park;
                                drive.followTrajectorySequence(park);
                            }
                            //do code
                        }
                        break;
                    case traj4:
                        unrotate();
                        groundHeight();
                        if (!drive.isBusy()) {
                            startIntake();
                            eatPixels();
                            l.sleep(400);
                            openChopsticks();
                            eatPixels();

                            currentState = State.traj7;
                            drive.followTrajectorySequence(traj7);
                            //do code
                        }
                        break;
                    case traj5:
                        unrotate();
                       // stickDown();
                        l.sleep(400);
                        if (!drive.isBusy()) {
                            //rotatemore();
                            //startIntake();
                           // stickUp();
                            l.sleep(400);
                            currentState = State.traj6;
                            drive.followTrajectorySequence(traj6);

                            //up+place+open
                            //do code
                        }
                        break;
                    case traj5B:
                        if(!drive.isBusy()) {
                            currentState = State.Idle;
                        }

                    case traj6:
                       // stickUp();
                        //rotatemore();

                        if (!drive.isBusy()) {
                            //  rotatemore();
                            unrotate();
                            groundHeight();
                            //  sleep(300);
                            startIntake();

                            currentState = State.traj65;

                            drive.followTrajectorySequence(traj65);

                            //up+place+open
                            //do code
                        }
                        break;
                    case traj65:
                        //rotatemore();

                        if (!drive.isBusy()) {

                            l.sleep(1100);
                            stopIntake();
                         //   reverseIntake();
                           // stickUp();
                            unrotate();
                            groundHeight();
                            currentState = State.traj7;
                            drive.followTrajectorySequence(traj7);

                            //up+place+open
                            //do code
                        }
                        break;
                    case traj7:
                        unrotate();
                        stopIntake();
                        if (!drive.isBusy()) {
                            close();
                            maxHeight();
                            t = System.currentTimeMillis();
                            currentState = State.traj8;
                            drive.followTrajectorySequence(traj8);

                            //up+place+open
                            //do code
                        }
                        break;
                    case traj8:
                        unrotate();
                         t2 = System.currentTimeMillis();
                         if (t2 - t > sleep3) {
                             lowHeight();
                             l.sleep(500);
                             rotate();

                         }
                        if (!drive.isBusy()) {
                            open();
                            l.sleep(1000);
                            currentState = State.park;

                            //up+place+open
                            //do code
                        }
                        break;
                    case park:
                        if (!drive.isBusy()) {
                            currentState = State.Idle;
                        }

                    case Idle:
                        break;
                }
                unrotate();

            }
            unrotate();
        }


    /**
     * Add telemetry about AprilTag detections.
     */
    private void maxHeight() {
        extenderRotator.setPosition(0.19);//0.21+theta);

        linearextenderLeft.setTargetPosition((int) (65 * TICKS_PER_CENTIMETER));
        linearextenderRight.setTargetPosition((int) (65 * TICKS_PER_CENTIMETER));

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(0.9);
        linearextenderLeft.setPower(0.9); //I think all 3 commands here (target, mode, power) are needed.

        close();
    }
    private void midHeight() {
        extenderRotator.setPosition(0.19);//0.21+theta);

        linearextenderLeft.setTargetPosition((int) (50 * TICKS_PER_CENTIMETER));
        linearextenderRight.setTargetPosition((int) (50 * TICKS_PER_CENTIMETER));

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(1.0);
        linearextenderLeft.setPower(1.0);
    }
    private void cHeight() {

        linearextenderLeft.setTargetPosition((int) ( up_constant* TICKS_PER_CENTIMETER));
        linearextenderRight.setTargetPosition((int) (up_constant * TICKS_PER_CENTIMETER));

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(1.0);
        linearextenderLeft.setPower(1.0);
    }
    private void lowHeight() {
        unrotate();

        linearextenderLeft.setTargetPosition((int) (10 * TICKS_PER_CENTIMETER));
        linearextenderRight.setTargetPosition((int) (10 * TICKS_PER_CENTIMETER));

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(1.0);
        linearextenderLeft.setPower(1.0);

        unrotate();
        close();
    }
    private void groundHeight() {
        linearextenderLeft.setTargetPosition(0);
        linearextenderRight.setTargetPosition(0);

        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(1.0);
        linearextenderLeft.setPower(1.0);
    }
    /*
    methods. They are separate from the buttons because sometimes they are called in
    multiple places, and to improve readability.
     */

    void turnright(int degrees){
        TrajectorySequence traj1 = drive.trajectorySequenceBuilder(new Pose2d(0,0))
                .turn(degrees)
                .build();
        drive.followTrajectorySequence(traj1);

    }

    void turnleft(int degrees){
        TrajectorySequence traj1 = drive.trajectorySequenceBuilder(new Pose2d(0,0))
                .turn(-degrees)
                .build();
        drive.followTrajectorySequence(traj1);

    }

    void strafeleft(double feet){
        TrajectorySequence traj1 = drive.trajectorySequenceBuilder(new Pose2d(0,0))
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
}
