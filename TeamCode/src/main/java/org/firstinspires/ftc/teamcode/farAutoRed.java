package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
@Autonomous(name="farAutoRed", group="Autonomous")
public class farAutoRed extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();

    public Servo grabber;

    int position = 180;
    double constanter = 0.0;
    public DcMotor fl;
    public DcMotor fr;
    public DcMotor bl;
    public DcMotor br;
    public Servo paperAirplane;
    public Servo extenderRotator;
    public Servo extenderPlacer;
    public DcMotor linearextenderLeft;
    public DcMotor linearextenderRight;
    double moveconstant = 1783 * (2/2.05); //WORKS
    double motorrotation = 538; //WORKS
    double turnconstant = 11.3846625767; // per degree, so its rly small
    double strafeconstant = 1783* (1/0.84) * (1/1.08) * (1/0.95) * (2/2.05); //untested, need to test
    String color = "";

    @Override
    public void runOpMode() {



        fl = hardwareMap.get(DcMotor.class, "FL");
        fr = hardwareMap.get(DcMotor.class, "FR");
        bl = hardwareMap.get(DcMotor.class, "BL");
        br = hardwareMap.get(DcMotor.class, "BR");

        paperAirplane = hardwareMap.get(Servo.class, "paperAirplane");
        extenderRotator = hardwareMap.get(Servo.class, "extenderRotator");
        extenderPlacer = hardwareMap.get(Servo.class, "extenderPlacer");
        linearextenderLeft = hardwareMap.get(DcMotor.class, "linearextenderLeft");
        linearextenderRight = hardwareMap.get(DcMotor.class, "linearextenderRight");
        //E = hardwareMap.get(DcMotor.class, "E");
        //   color_sensor = hardwareMap.colorSensor.get("color_sensor");

        // grabber = hardwareMap.get(Servo.class,"grab"); //THE SERVO IS IN PEROCENT, BW/ 1 OR 0. BASELINE IS .5

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearextenderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearextenderLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearextenderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearextenderRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // E.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //E.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.FORWARD);

        // runs the moment robot is initialized
        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {
            moveforward(12);
            sleep(300);

            //place spike mark stuff
           // movebackward(54);
           // sleep(300);
            straferight(181);
            sleep(1000);
            moveforward(55.5);
            sleep(300);

            turnright(90);
            sleep(300);
            extend_no_enc(0);
            rotateBox();
            open();
            close();
            unrotateBox();
            extend_no_enc(1);
            //place stuff
            straferight(58);
            sleep(300);
            moveforward(49);
            sleep(300);

            break;
        }

    }
    // this is only for dc motors
    void settargetpositioner(DcMotor motor, int position){
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setTargetPosition(position);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(.4);
    }

    void moveforward(double feet){

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.FORWARD);
        int position = (int) (feet * 0.3048 * moveconstant)*-1;
        settargetpositioner(fl, position);
        settargetpositioner(fr, position);
        settargetpositioner(bl, position);
        settargetpositioner(br, position);
        while (fl.isBusy()){sleep(1);}
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
        sleep(100);}
    void straferightmeters(double meters){
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.FORWARD);
        br.setDirection(DcMotorSimple.Direction.FORWARD);

        int position = (int) (meters * strafeconstant)*-1;
        settargetpositioner(fl, position);
        settargetpositioner(fr, position);
        settargetpositioner(bl, position);
        settargetpositioner(br, position);
        while (fl.isBusy()){sleep(10);}
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);}
    void movebackward(double feet){
        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.FORWARD);
        br.setDirection(DcMotorSimple.Direction.REVERSE);

        int position = (int) (feet * 0.3048 * moveconstant)*-1;

        settargetpositioner(fl, position);
        settargetpositioner(fr, position);
        settargetpositioner(bl, position);
        settargetpositioner(br, position);
        while (fl.isBusy()){sleep(1);}
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
        sleep(100);}
    void strafeleft(double feet){
        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);

        int position = (int) (feet * 0.3048 * strafeconstant)*-1;

        settargetpositioner(fl, position);
        settargetpositioner(fr, position);
        settargetpositioner(bl, position);
        settargetpositioner(br, position);
        while (fl.isBusy()){sleep(1);}
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
        sleep(100); }
    void straferight(double feet){
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.FORWARD);
        br.setDirection(DcMotorSimple.Direction.FORWARD);

        int position = (int) (feet * 0.3048 * strafeconstant)*-1;
        settargetpositioner(fl, position);
        settargetpositioner(fr, position);
        settargetpositioner(bl, position);
        settargetpositioner(br, position);
        while (fl.isBusy()){sleep(1);}
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
        sleep(100);


    }
    void turnleft(int degrees){
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.FORWARD);
        int position = (int) (degrees * turnconstant)*-1;
        settargetpositioner(fl, -position);
        settargetpositioner(bl, -position);
        settargetpositioner(br, position);
        settargetpositioner(fr, position);
        while (fl.isBusy()){sleep(1);}
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

    }
    void turnright(int degrees){
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.FORWARD);

        int position = (int) (degrees * turnconstant)*-1;
        settargetpositioner(fl, position);
        settargetpositioner(bl, position);
        settargetpositioner(br, -position);
        settargetpositioner(fr, -position);
        while (fl.isBusy()){sleep(1);}
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

    }
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

    //void launch() {
    //  paperAirplane.setPosition(.35);
    //  sleep(100);
    //}
    // void grab(){
    //}
    //void ungrab(){
    //}
    void extend_no_enc(int position) {
        if (position == 0) {
            linearextenderLeft.setPower(0.5);
            linearextenderRight.setPower(0.5);
            sleep(800);
            linearextenderLeft.setPower(0);
            linearextenderRight.setPower(0);
        }
        else if (position == 1) {
            linearextenderLeft.setPower(-0.5);
            linearextenderRight.setPower(-0.5);
            sleep(800);
            linearextenderLeft.setPower(0);
            linearextenderRight.setPower(0);
        }
    }
    void extend(DcMotor left,  DcMotor right, int position) {

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
                left.setTargetPosition(1300);
                right.setTargetPosition(1300);
                left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                left.setPower(0.75);
                right.setPower(0.75);

                break;
            case 2:
                left.setTargetPosition(1994);
                right.setTargetPosition(1994);
                left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                left.setPower(0.75);
                right.setPower(0.75);

                break;
            case 3:
                left.setTargetPosition(2990);
                right.setTargetPosition(2990);
                left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                left.setPower(0.75);
                right.setPower(0.75);


                break;
        }
    }





}