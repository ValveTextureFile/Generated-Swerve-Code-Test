
package frc.robot.commands;

// ===== IMPORTS GO HERE =====
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.swervedrive.CommandSwerveDrivetrain;
import frc.robot.subsystems.swervedrive.Vision;

import org.photonvision.targeting.PhotonTrackedTarget;

public class AimBot extends Command {

    private final CommandSwerveDrivetrain drivetrain;
    private final Vision vision;

    // This PID controls HEADING, not angular velocity
    private final PIDController headingPID =
            new PIDController(5.0, 0.0, 0.2);

    private final SwerveRequest.RobotCentricFacingAngle faceTag =
            new SwerveRequest.RobotCentricFacingAngle()
                    .withVelocityX(0)
                    .withVelocityY(0);

    public AimBot(
            CommandSwerveDrivetrain drivetrain,
            Vision vision
    ) {
        this.drivetrain = drivetrain;
        this.vision = vision;

        addRequirements(drivetrain);

        headingPID.enableContinuousInput(-Math.PI, Math.PI);
        headingPID.setTolerance(Math.toRadians(1.0));
    }

    @Override
    public void execute() {
        PhotonTrackedTarget target = vision.getFrontTarget();
        if (target == null) return;

        // Current robot heading
        Rotation2d currentHeading = drivetrain.getState().Pose.getRotation();

        // Desired heading = current heading + yaw error
        Rotation2d desiredHeading =
                currentHeading.plus(
                        Rotation2d.fromDegrees(-target.getYaw())
                );

        drivetrain.applyRequest(() ->
                faceTag.withTargetDirection(desiredHeading)
        );
    }

    @Override
    public boolean isFinished() {
        return headingPID.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.applyRequest(() ->
                faceTag.withVelocityX(0)
                       .withVelocityY(0)
        );
    }
}
