package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name="DRIVETRAIN_ONLY", group="Driver OP")
public class driverthing extends LinearOpMode {

    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();
   // public Servo grabber;
    double powersetterr = 1;

    public DcMotor fl;
    public DcMotor fr;
    public DcMotor bl;
    public DcMotor br;
    public DcMotor intakeMotor;

    public DcMotor linearextenderLeft;
    public DcMotor linearextenderRight;
    //public Servo paperAirplane;
    public Servo extenderRotator;
    public Servo extenderPlacer;



    //public DcMotor E;
   // public ColorSensor color_sensor;





    public static final String TAG = "Vuforia VuMark Sample";
    WebcamName webcamName;
    OpenCvWebcam webcam;
    OpenCvPipeline pipeline;

    Boolean intake_running = false;

     //final double MAX_LS_HEIGHT = 50;
    @Override public void runOpMode() {

        /*
         * Retrieve the camera we are to use.
         */
        // webcamName = hardwareMap.get(WebcamName.class, "webcam");
        //int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);        // OR...  Do Not Activate the Camera Monitor View, to save power
        //pipeline = new opencvpipelines();
        //webcam.setPipeline(pipeline);

        //webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        //{
        // @Override
        //  public void onOpened()
        //{
        //   webcam.startStreaming(1920,1080, OpenCvCameraRotation.UPRIGHT);
        //}

        // @Override
        //public void onError(int errorCode) {}
        // });


        fl = hardwareMap.get(DcMotor.class, "FL");
        fr = hardwareMap.get(DcMotor.class, "FR");
        bl = hardwareMap.get(DcMotor.class, "BL");
        br = hardwareMap.get(DcMotor.class, "BR");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        linearextenderLeft = hardwareMap.get(DcMotor.class, "linearextenderLeft");
        linearextenderRight = hardwareMap.get(DcMotor.class, "linearextenderRight");

        // paperAirplane = hardwareMap.get(Servo.class, "paperAirplane");
         extenderRotator = hardwareMap.get(Servo.class, "extenderRotator");
         extenderPlacer = hardwareMap.get(Servo.class, "extenderPlacer");

        // E = hardwareMap.get(DcMotor.class, "E");

        //grabber = hardwareMap.get(Servo.class, "grab");

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      /*  linearextenderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearextenderLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearextenderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //E.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

       */
        //E.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
linearextenderLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
linearextenderRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
   //     linearextenderLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
     //   linearextenderRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        fl.setDirection(DcMotor.Direction.FORWARD);
        fr.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.REVERSE);
        intakeMotor.setDirection(DcMotor.Direction.REVERSE);
        linearextenderLeft.setDirection(DcMotor.Direction.REVERSE);
      //  linearextenderLeft.setDirection(DcMotor.Direction.FORWARD);



        final double TICKS_PER_CENTIMETER = 537.7 / 11.2;
        final double CENTIMETERS_PER_TICK = 1 / TICKS_PER_CENTIMETER;

        final double MAX_SLIDE_HEIGHT = 50;


        // runs the moment robot is initialized 136
        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {

            move();
            if (gamepad1.dpad_left) {
                telemetry.addData("accessed: stopintake", "0");
                stopIntake();

            }
            if (gamepad1.dpad_right){
                telemetry.addData("accessed: startintake", "0");

                startIntake();

            }
            //  if (gamepad1.y) {
            //  linearextenderLeft.setTargetPosition(50);
            //    linearextenderRight.setTargetPosition(-50);
            //    linearextenderLeft.setPower(1);
            //    linearextenderRight.setPower(1);


            //  }
        }
      /*  if (gamepad1.a) {
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            linearextenderLeft.setPower(0.5);
            linearextenderRight.setPower(0.5);
        }
        if (gamepad1.y) {
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            linearextenderLeft.setPower(-0.5);
            linearextenderRight.setPower(-0.5);
        }
        telemetry.addData("Slideheightleft", linearextenderLeft.getCurrentPosition());
        telemetry.addData("Slideheightright", linearextenderRight.getCurrentPosition());*/
         /*   if (gamepad1.y) {
                linearextenderRight.setTargetPosition((int)TICKS_PER_CENTIMETER*50);
                linearextenderLeft.setTargetPosition((int)TICKS_PER_CENTIMETER*50);

                linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (gamepad1.a) {
                linearextenderRight.setTargetPosition((int)TICKS_PER_CENTIMETER*0);
                linearextenderLeft.setTargetPosition((int)TICKS_PER_CENTIMETER*0);

                linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            }

          */
            if (gamepad1.y) {
                telemetry.addData("accessed: lin_ex", "0");

                linearextenderLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                linearextenderRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                linearextenderRight.setPower(0.5);
                linearextenderLeft.setPower(0.5);
            }
        if (gamepad1.a) {
            telemetry.addData("accessed: lin_ex2", "0");

            linearextenderLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            linearextenderRight.setPower(-0.5);
            linearextenderLeft.setPower(-0.5);
        }

           // if (gamepad1.dpad_down) {
                //launch();
           // }
            if (gamepad1.right_bumper) {
                 rotateBox();

            }
            if (gamepad1.left_bumper) {
                   unrotateBox();

            }
            if (gamepad1.left_trigger > 0.3) {
                 open();

            }
            if (gamepad1.right_trigger > 0.3) {
                  close();

            }




/*
            if(gamepad1.left_stick_button && !rishi)
            {
                if (powersetterr == 1.0)
                {
                    powersetterr = 0.5;
                }
                else
                {
                    powersetterr = 1.0;
                }

            }
            rishi = gamepad1.left_stick_button;
*/


            //if(gamepad1.left_trigger > 0.5){ grabber.setPosition(.35);
            // }

            //if(gamepad1.left_bumper){
            //  E.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //while(gamepad1.left_bumper){
            ///E.setPower(-1);
            //}
            //E.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //   E.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // E.setPower(0);
            //}
            //if(gamepad1.right_trigger > 0.5){grabber.setPosition(.550);}

    /*        if(gamepad1.b){extend(linearextenderLeft, linearextenderRight, 0);}
            if(gamepad1.a){extend(linearextenderLeft, linearextenderRight, 1);}
            if(gamepad1.x){extend(linearextenderLeft, linearextenderRight, 2);}
            if(gamepad1.y){extend(linearextenderLeft, linearextenderRight, 3);}*/
            telemetry.addData("fl", fl.getPower());
            telemetry.addData("fr", fr.getPower());
            telemetry.addData("bl", bl.getPower());
            //  telemetry.addData("e",E.getCurrentPosition());
            //   telemetry.addData("grab", grabber.getPosition());
            telemetry.update();
        }


       /* void slidesToHeight (double heightCM, double power){
            linearextenderRight.setTargetPosition((int) (heightCM * TICKS_PER_CENTIMETER));
            linearextenderLeft.setTargetPosition((int) (heightCM * TICKS_PER_CENTIMETER));

            linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            linearextenderRight.setPower(power);
            linearextenderLeft.setPower(power);
        }*/
    //}
    void rotateBox(){
        extenderRotator.setPosition(0.3);
        sleep(100);
    }
    void unrotateBox(){
        extenderRotator.setPosition(0.7);
        sleep(100);
    }
    void open(){
        extenderPlacer.setPosition(0.3);
        sleep(100);
    }
   void close(){

        extenderPlacer.setPosition(0.0);
       sleep(100);
    }

   // void launch() {
     //   paperAirplane.setPosition(.35);
    //    sleep(100);
    //}
    // void grab(){
    //}
    //void ungrab(){
    //}

  /*  void extend(DcMotor left,  DcMotor right, int position) {

        final double TICKSPERCENTIMETER = 537.7/11.2;
        final double CENTIMETERSPERTICK = 1/TICKSPERCENTIMETER;
        switch (position) {
            case 0:
                if(left.getCurrentPosition()>10 ) {
                    left.setTargetPosition(0);
                    right.setTargetPosition(0);
                    left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    left.setPower(0.75);
                    right.setPower(0.75);
                }else{
                    left.setPower(0);
                    right.setPower(0);
                }
                break;
            case 1:
             //   final double TICKSPERCENTIMETER = 537.7/11.2;
            //    final double CENTIMETERSPERTICK = 1/TICKSPERCENTIMETER;
                left.setTargetPosition(((int)CENTIMETERSPERTICK)*12);
                right.setTargetPosition(((int)CENTIMETERSPERTICK)*12);
                left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                left.setPower(0.75);
                right.setPower(0.75);

                break;
            case 2:
                left.setTargetPosition(((int)CENTIMETERSPERTICK)*12);
                right.setTargetPosition(((int)CENTIMETERSPERTICK)*12);
                left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                left.setPower(0.75);
                right.setPower(0.75);

                break;
            case 3:
                left.setTargetPosition(((int)CENTIMETERSPERTICK)*50);
                right.setTargetPosition(((int)CENTIMETERSPERTICK)*50);
                left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                left.setPower(0.75);
                right.setPower(0.75);


                break;
        }
    }

   */
    void startIntake(){
        intakeMotor.setPower(1.0);
    }
    void stopIntake(){
        intakeMotor.setPower(0.0);

    }

    void move(){
        //intakeMotor.setPower(1.0);
        double horizontal = gamepad1.left_stick_x*.5;   // this works so dont question it
        double vertical =-gamepad1.left_stick_y*.5;
        double turn = gamepad1.right_stick_x*2/3;
        //  E.setPower(gamepad1.left_stick_y);
        fl.setPower((Range.clip((vertical + horizontal + turn), -1, 1))/**powersetterr*/);
        fr.setPower((Range.clip((vertical - horizontal - turn), -1, 1))/**powersetterr*/);
        bl.setPower((Range.clip((vertical - horizontal + turn), -1, 1))/**powersetterr*/);
        br.setPower((Range.clip((vertical + horizontal - turn), -1, 1))/**powersetterr*/);
    }




    }

