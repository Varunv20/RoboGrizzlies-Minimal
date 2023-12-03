package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.computervision.eocvTeamProp;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

@Autonomous(name="farAutoRed", group="Autonomous")
public class farAutoRed extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();

    double turnconstant = 12.76;
    double moveconstant = 1713;
    double strafeconstant = 1990;
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
    // double moveconstant = 1300; //WORKS
    double motorrotation = 538; //WORKS
    OpenCvWebcam webcam;


    //  double turnconstant = 11.3846625767; // per degree, so its rly small
    //  double strafeconstant = 1300; //untested, need to test
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
        linearextenderLeft.setDirection(DcMotor.Direction.REVERSE);

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
        runtime.reset();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View
        //webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"));

        /*
         * Specify the image processing pipeline we wish to invoke upon receipt
         * of a frame from the camera. Note that switching pipelines on-the-fly
         * (while a streaming session is in flight) *IS* supported.
         */

        eocvTeamProp pipeline = new eocvTeamProp();
        webcam.setPipeline(pipeline);


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
        sleep(10000);
        String r = pipeline.getResult();
        telemetry.addData("rishi", r);
        telemetry.addData("m1", pipeline.m1avg);
        telemetry.addData("m2", pipeline.m2avg);
        telemetry.addData("m3", pipeline.m3avg);


        telemetry.update();
        waitForStart();
        unrotate();
        close();
        // extender(5);

        while(opModeIsActive()) {
            //     extender(5);



            if (r== "right"){
                extender(5);
                moveforward(45);
                turnright(40);
                moveforward(23);



            }
            else if (r== "left"){
                moveforward(45);
                turnleft(40);
                moveforward(23);

            }
            else {
                moveforward(73);

            }

            break;
        }

    }
    void place(){
        extender(30);
        sleep(500);
        rotate();
        sleep(500);
        open();
        sleep(300);
        unrotate();
        close();
        sleep(300);
        extender(5);
        sleep(500);
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
        int position = (int) ((feet/(2.54*12)) * 0.3048 * moveconstant)*-1;
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

        int position = (int) ((feet/(2.54*12)) * 0.3048 * moveconstant)*-1;

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

        int position = (int) ((feet/(2.54*12)) * 0.3048 * strafeconstant)*-1;

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

        int position = (int) ((feet/(2.54*12)) * 0.3048 * strafeconstant)*-1;
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
    void turnright(int degrees){
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
    void turnleft(int degrees){
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