package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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

@TeleOp(name="MainRobotCode", group="Driver OP")
public class driverthing extends OpMode {
    //private final ElapsedTime runtime = new ElapsedTime();
    // public Servo grabber;
    //  double powersetterr = 1;

    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    DcMotor intakeMotor;

    DcMotor linearextenderLeft;
    DcMotor linearextenderRight;
     Servo paperAirplane;
    Servo extenderRotator;
    Servo extenderPlacer;
    Boolean intake_running = false;
    double i = 0;
    @Override
    public void init() {
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
        paperAirplane = hardwareMap.get(Servo.class, "paperAirplane");


        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearextenderLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearextenderLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearextenderRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        fl.setDirection(DcMotor.Direction.FORWARD);
        fr.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.REVERSE);
        intakeMotor.setDirection(DcMotor.Direction.REVERSE);
        linearextenderLeft.setDirection(DcMotor.Direction.REVERSE);



        final double TICKS_PER_CENTIMETER = 537.7 / 11.2;
        final double CENTIMETERS_PER_TICK = 1 / TICKS_PER_CENTIMETER;

        final double MAX_SLIDE_HEIGHT = 50;
        unrotate();
        setPlane();
        open();
    }

    // Declare OpMode members.

/*




    public static final String TAG = "Vuforia VuMark Sample";
    WebcamName webcamName;
    OpenCvWebcam webcam;
    OpenCvPipeline pipeline;

 */

  //  Boolean intake_running = false;

     //final double MAX_LS_HEIGHT = 50;
    @Override
    public void loop() {

        // runs the moment robot is initialized 136
        //    waitForStart();
        //    runtime.reset();
        final double TICKS_PER_CENTIMETER = 537.7 / 11.2;
        final double CENTIMETERS_PER_TICK = 1 / TICKS_PER_CENTIMETER;
        move();

        if (gamepad1.y) {
            extenderRotator.setPosition(0.15);

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
            linearextenderLeft.setTargetPosition((int) (0*TICKS_PER_CENTIMETER));
            linearextenderRight.setTargetPosition((int) (0*TICKS_PER_CENTIMETER));
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setPower(0.9);
            linearextenderLeft.setPower(0.9);
            telemetry.addData("Slides to 0cm","0");

        }
        else if (gamepad1.x) {
            //close();
            extenderRotator.setPosition(0.15);
            linearextenderLeft.setTargetPosition((int) (45*TICKS_PER_CENTIMETER));
            linearextenderRight.setTargetPosition((int) (45*TICKS_PER_CENTIMETER));
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setPower(0.9);
            linearextenderLeft.setPower(0.9);
            telemetry.addData("Slides to 0cm", "0");
            extenderRotator.setPosition(0.1);
            close();
        }
        else if (gamepad1.a) {
            unrotate();
            //telemetry.addData("accessed: lin_ex2", "0");

            linearextenderLeft.setTargetPosition((int) (10*TICKS_PER_CENTIMETER));
            linearextenderRight.setTargetPosition((int) (10*TICKS_PER_CENTIMETER));
            linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            linearextenderRight.setPower(0.9);
            linearextenderLeft.setPower(0.9);

            telemetry.addData("Slides to 9cm","0");
            extenderRotator.setPosition(0.1);
            close();

       //     linearextenderLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      //      linearextenderRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            //linearextenderRight.setPower(-0.5);
            //linearextenderLeft.setPower(-0.5);
        }

        //else {
            //linearextenderLeft.setPower(0);
            //linearextenderRight.setPower(0);
        //}

        // if (gamepad1.dpad_down) {
        //launch();
        // }
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
    void extender(int pos) {
        final double TICKS_PER_CENTIMETER = 537.7 / 11.2;

        linearextenderLeft.setTargetPosition((int) (pos*TICKS_PER_CENTIMETER));
        linearextenderRight.setTargetPosition((int) (pos*TICKS_PER_CENTIMETER));
        linearextenderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearextenderRight.setPower(0.9);
        linearextenderLeft.setPower(0.9);

        telemetry.addData("Slides to 9cm","0");
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
        extenderRotator.setPosition(0.21);
    }
    void stopIntake(){
        intakeMotor.setPower(0.0);
    }
    void reverseIntake(){
        intakeMotor.setPower(-1.0);
    }


    void move(){
        //intakeMotor.setPower(1.0);
        double horizontal = gamepad1.left_stick_x*.5;   //the math behind this is cool. Look it up!
        double vertical =-gamepad1.left_stick_y*.5;
        double turn = -gamepad1.right_stick_x*2/3;
        fl.setPower((Range.clip((vertical + horizontal + turn), -1, 1))/**powersetterr*/);
        fr.setPower((Range.clip((vertical - horizontal - turn), -1, 1))/**powersetterr*/);
        bl.setPower((Range.clip((vertical - horizontal + turn), -1, 1))/**powersetterr*/);
        br.setPower((Range.clip((vertical + horizontal - turn), -1, 1))/**powersetterr*/);
    }

}