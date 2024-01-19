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
                                .lineToLinearHeading(new Pose2d(0,28, Math.toRadians(90)))
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
        RoadRunnerBotEntity blueCENTER = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 18)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, 63.25, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(12,32.75, Math.toRadians(90)))
                                .forward(8)
                                .lineToLinearHeading(new Pose2d(53, 36, Math.toRadians(180)))
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
        RoadRunnerBotEntity blueLEFT = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, 63.25, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(24,32.75, Math.toRadians(90)))
                                .forward(8)
                                .lineToLinearHeading(new Pose2d(53, 41, Math.toRadians(180)))
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
                .addEntity(blueCENTER)
                .start();
    }
}