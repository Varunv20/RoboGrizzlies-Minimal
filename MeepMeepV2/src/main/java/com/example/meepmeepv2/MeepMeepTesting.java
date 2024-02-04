package com.example.meepmeepv2;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity blueRIGHT = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(0), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, 63.25, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(0,35, Math.toRadians(90)))
                                .forward(4)
                                .lineToLinearHeading(new Pose2d(53, 30, Math.toRadians(180)))
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


                                // .back(5)
                                //intake
                                // .forward(5)
                                // .splineTo(new Vector2d(0, 6), Math.toRadians(180))

                                // .splineTo(new Vector2d(53, 30), Math.toRadians(0))



                                .build()
                );
        RoadRunnerBotEntity blueCENTER = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 18)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, 63.25, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(24,34.75, Math.toRadians(90)))
                                .forward(8)
                                .lineToLinearHeading(new Pose2d(43, 44.25, Math.toRadians(180)))
                                //traj2
                                .back(4.9) //6.9
                                //traj3
                                .forward(3)
                                //traj4
                                .splineTo(new Vector2d(24,60), Math.toRadians(180))
                                .lineToLinearHeading(new Pose2d(-36, 60, Math.toRadians(180)))
                                .splineToConstantHeading(new Vector2d(-57,44), Math.toRadians(180))
                                .lineToLinearHeading(new Pose2d(-58.25, 42.5, Math.toRadians(180)))
                                //traj5
                                .back(7)
                                //traj5B
                                .lineToLinearHeading(new Pose2d(-57, 29, Math.toRadians(190)))
                                //traj6
                                .strafeLeft(5.7)
                                .forward(4.5)
                                //traj65
                                .forward(2.1)
                                //traj7
                                .lineToLinearHeading(new Pose2d(-57, 44, Math.toRadians(180)))
                                .splineToConstantHeading(new Vector2d(-36,60), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(-30, 60), Math.toRadians(0))

                                .splineToConstantHeading(new Vector2d(24,60), Math.toRadians(0))
                                //   .back(1)
                                //  .splineToConstantHeading(new Pose2d(24, 60), Math.toRadians(180))
                                .splineToConstantHeading(new Vector2d(43.25,44), Math.toRadians(0))
                                //traj8
                                .back(4)




                                // .back(5)
                                //intake
                                // .forward(5)
                                // .splineTo(new Vector2d(0, 6), Math.toRadians(180))

                                // .splineTo(new Vector2d(53, 30), Math.toRadians(0))


                                .build()
                );
        RoadRunnerBotEntity blueLEFT = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, 63.25, Math.toRadians(90)))
                                .back(24)
                                .lineToLinearHeading(new Pose2d(7,36, Math.toRadians(30)))
                                .forward(3)
                                .lineToLinearHeading(new Pose2d(43, 31.5, Math.toRadians(180)))


                                // .back(5)
                                //intake
                                // .forward(5)
                                // .splineTo(new Vector2d(0, 6), Math.toRadians(180))

                                // .splineTo(new Vector2d(53, 30), Math.toRadians(0))

                                .build()
                );
        RoadRunnerBotEntity blueFARLEFT = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, 63.25, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(24,32.75, Math.toRadians(90)))
                                .forward(8)
                                .lineToLinearHeading(new Pose2d(53, 30, Math.toRadians(180)))
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


                                // .back(5)
                                //intake
                                // .forward(5)
                                // .splineTo(new Vector2d(0, 6), Math.toRadians(180))

                                // .splineTo(new Vector2d(53, 30), Math.toRadians(0))



                                .build()
                );
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(blueLEFT)
                .start();
    }
}