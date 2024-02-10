package org.firstinspires.ftc.teamcode.drive.opmode;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Config
public class AutoTrajectories {
    public SampleMecanumDrive drive;
    private int red = 1;
    private boolean close = false;
    private Pose2d startpos;
    public boolean center = false;
    public boolean l = false;
    private TrajectorySequence traj1CR;
    private TrajectorySequence  traj1CL;
    private TrajectorySequence  traj1CC;
    private TrajectorySequence traj1FR;
    private TrajectorySequence  traj1FL;
    private TrajectorySequence  traj1FC;
    public Pose2d start2pos;
    private TrajectorySequence  traj2;
    private TrajectorySequence traj3;
    private TrajectorySequence  traj4;
    private TrajectorySequence traj5;
    private TrajectorySequence  traj6;
    private TrajectorySequence  traj65;
    private TrajectorySequence  traj7;
    private TrajectorySequence  traj8;
    public AutoMachine op;



    public void AutoTrajectories(SampleMecanumDrive d) {
        drive = d;

    }
    public void setRed() {
        red = -1;
    }
    public void setClose() {
        close = true;
    }
    public void setStartpos() {
        if (close) {
            if (red == -1) {
                startpos = new Pose2d(12, -63.25, Math.toRadians(270));

            }
            else {
                 startpos = new Pose2d(12, 63.25, Math.toRadians(90));
            }
        }
        else {
            if (red == -1) {
                startpos = new Pose2d(-36, -63.25, Math.toRadians(270));

            }
            else {
                 startpos = new Pose2d(-36, 63.25, Math.toRadians(90));
            }
        }
    }

    public TrajectorySequence getTraj1(String result) {
        traj1CR = drive.trajectorySequenceBuilder(startpos)
                .back(24)
                .lineToLinearHeading(new Pose2d(7,36 * red, Math.toRadians(45* red)))
                .forward(1.5)
                .lineToLinearHeading(new Pose2d(43, 30* red, Math.toRadians(180* red)))
                .build();
        traj1CL = drive.trajectorySequenceBuilder(startpos)
                .lineToLinearHeading(new Pose2d(24,34.75* red, Math.toRadians(90* red)))
                .forward(8)
                .lineToLinearHeading(new Pose2d(43, 44.25* red, Math.toRadians(180* red)))
                .build();
        traj1CC = drive.trajectorySequenceBuilder(startpos)
                .lineToLinearHeading(new Pose2d(12,34.75* red, Math.toRadians(90* red)))
                .forward(8)
                .lineToLinearHeading(new Pose2d(46, 36* red, Math.toRadians(180* red)))
                .build();
        traj1FR =  drive.trajectorySequenceBuilder(startpos)
                .back(10)
                .lineToLinearHeading(new Pose2d(-48,36* red, Math.toRadians(90* red)))
                .forward(2)
                .lineToLinearHeading(new Pose2d(-61 , 36* red, Math.toRadians(180* red)))
                .addDisplacementMarker(() -> {
                    op.openChopsticks();
                    op.startIntake();
                    op.eatPixels();

                })
                .lineToLinearHeading(new Pose2d(-57, 30* red, Math.toRadians(180* red)))
                .splineToConstantHeading(new Vector2d(-36,10* red), Math.toRadians(0* red))
                .splineToConstantHeading(new Vector2d(-30, 10* red), Math.toRadians(0* red))

                .splineToConstantHeading(new Vector2d(24,10* red), Math.toRadians(0* red))
                .splineToConstantHeading(new Vector2d(43.25,32* red), Math.toRadians(0* red))
                .build();
        traj1FL =  drive.trajectorySequenceBuilder(startpos)
                .back(24)
                .lineToLinearHeading(new Pose2d(-31,36 * red, Math.toRadians(135* red )))
                .forward(1.5)
                .lineToLinearHeading(new Pose2d(-61 , 36* red, Math.toRadians(180* red)))
                .addDisplacementMarker(() -> {
                    op.openChopsticks();
                    op.startIntake();
                    op.eatPixels();

                })
                .lineToLinearHeading(new Pose2d(-57, 30* red, Math.toRadians(180* red)))
                .splineToConstantHeading(new Vector2d(-36,10* red), Math.toRadians(0* red))
                .splineToConstantHeading(new Vector2d(-30, 10* red), Math.toRadians(0* red))

                .splineToConstantHeading(new Vector2d(24,10* red), Math.toRadians(0* red))
                .splineToConstantHeading(new Vector2d(43.25,40* red), Math.toRadians(0* red))
                .build();
        traj1FC =drive.trajectorySequenceBuilder(startpos)
                .lineToLinearHeading(new Pose2d(-36,34.75* red, Math.toRadians(90* red)))
                .forward(4)
                .lineToLinearHeading(new Pose2d(20, 36* red, Math.toRadians(90* red)))
                .lineToLinearHeading(new Pose2d(43, 36* red, Math.toRadians(180* red)))
                .build();
        if (close) {
            if (result == "center") {
                start2pos = traj1CC.end();
                return traj1CC;
            } else if (result == "left" && red == 1 || result == "right" && red == -1) {
                start2pos = traj1CL.end();

                return traj1CL;
            } else {
                start2pos = traj1CR.end();

                return traj1CR;
            }
        }
        else {
            if (result == "center") {
                start2pos = traj1FC.end();
                return traj1FC;
            } else if (result == "left" && red == 1 || result == "right" && red == -1) {
                start2pos = traj1FL.end();

                return traj1FL;
            } else {
                start2pos = traj1FR.end();

                return traj1FR;
            }
        }
    }
    public TrajectorySequence getTraj2() {
        traj2 =  drive.trajectorySequenceBuilder(start2pos)
                .back(3.9)
                .build();
        return traj2;
    }
    public TrajectorySequence getTraj3() {
        traj3 =  drive.trajectorySequenceBuilder(traj2.end())
                .forward(2)
                .build();
        return traj3;
    }
    public TrajectorySequence getParkOutside() {
        TrajectorySequence trajParkLEFT =  drive.trajectorySequenceBuilder(traj3.end())
                .lineToLinearHeading(new Pose2d(41.25, 60* red, Math.toRadians(180* red)))
                .back(15)
                .build();
        return trajParkLEFT;
    }
    public TrajectorySequence getParkCenter() {
        TrajectorySequence trajParkRIGHT =  drive.trajectorySequenceBuilder(traj3.end())
                .lineToLinearHeading(new Pose2d(41.25, 12* red, Math.toRadians(180* red)))
                .back(15)
                .build();
        return trajParkRIGHT;
    }
    public TrajectorySequence getTraj4() {
        traj4 =  drive.trajectorySequenceBuilder(traj3.end())
                .splineTo(new Vector2d(24,60* red), Math.toRadians(180* red))
                .lineToLinearHeading(new Pose2d(-36, 60* red, Math.toRadians(180* red)))
                .splineToConstantHeading(new Vector2d(-57,44* red), Math.toRadians(180* red))
                .lineToLinearHeading(new Pose2d(-58.62, 36* red, Math.toRadians(180* red)))
                .build();
        return traj4;
    }

    public TrajectorySequence getTraj5() {
        traj5 =  drive.trajectorySequenceBuilder(traj4.end())
                .back(7)
                .build();
        return traj5;
    }
    public TrajectorySequence getTraj6() {

        traj6 =  drive.trajectorySequenceBuilder(traj5.end())
                .lineToLinearHeading(new Pose2d(-58.25 , 36* red, Math.toRadians(180* red)))

                .forward(4.5)
                .build();
        return traj6;
    }
    public TrajectorySequence getTraj65() {
        traj65 = drive.trajectorySequenceBuilder(traj6.end())
                .forward(2.1)
                .build();
        return traj65;
    }
    public TrajectorySequence getTraj7() {
        traj7 = drive.trajectorySequenceBuilder(traj4.end())

                .lineToLinearHeading(new Pose2d(-57, 44* red, Math.toRadians(180* red)))
                .splineToConstantHeading(new Vector2d(-36,60* red), Math.toRadians(0* red))
                .splineToConstantHeading(new Vector2d(-30, 60* red), Math.toRadians(0* red))

                .splineToConstantHeading(new Vector2d(24,60* red), Math.toRadians(0* red))
                .splineToConstantHeading(new Vector2d(43.25,44* red), Math.toRadians(0* red))
                .build();
        return traj7;
    }
    public TrajectorySequence getTraj7Middle() {
        traj7 = drive.trajectorySequenceBuilder(traj65.end())

                .lineToLinearHeading(new Pose2d(-57, 44* red, Math.toRadians(180* red)))
                .splineToConstantHeading(new Vector2d(-36,60* red), Math.toRadians(0* red))
                .splineToConstantHeading(new Vector2d(-30, 60* red), Math.toRadians(0* red))

                .splineToConstantHeading(new Vector2d(24,60* red), Math.toRadians(0* red))
                .splineToConstantHeading(new Vector2d(43.25,44* red), Math.toRadians(0* red))
                .build();
        return traj7;
    }

    public TrajectorySequence getTraj8() {
        traj8 = drive.trajectorySequenceBuilder(traj7.end())
                .back(4)
                .build();
        return traj8;
    }




}
