package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;

import java.util.function.DoubleSupplier;

public class FieldRelativeHeadingDrive extends Command {
    private final CommandSwerveDrivetrain drivetrain;
    private final DoubleSupplier leftX;
    private final DoubleSupplier leftY;
    private final DoubleSupplier rightX;
    private final DoubleSupplier rightY;
    
    // SwerveRequest for field-centric driving
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
        .withDeadband(0.1)
        .withRotationalDeadband(0.1)
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    
    // PID controller for heading control
    private final PIDController headingController;
    
    // Deadband threshold for joysticks
    private static final double DEADBAND = 0.1;
    
    // Maximum speeds
    private final double maxSpeed;
    private final double maxAngularRate;
    
    // Target heading (maintained when joystick is centered)
    private Rotation2d targetHeading;

    /**
     * Creates a new field-relative drive command with heading control
     * 
     * @param drivetrain The swerve drivetrain subsystem
     * @param leftX Left joystick X (strafe)
     * @param leftY Left joystick Y (forward/back)
     * @param rightX Right joystick X (aiming)
     * @param rightY Right joystick Y (aiming)
     * @param maxSpeed Maximum translation speed (m/s)
     * @param maxAngularRate Maximum rotation rate (rad/s)
     */
    public FieldRelativeHeadingDrive(
            CommandSwerveDrivetrain drivetrain,
            DoubleSupplier leftX,
            DoubleSupplier leftY,
            DoubleSupplier rightX,
            DoubleSupplier rightY,
            double maxSpeed,
            double maxAngularRate) {
        
        this.drivetrain = drivetrain;
        this.leftX = leftX;
        this.leftY = leftY;
        this.rightX = rightX;
        this.rightY = rightY;
        this.maxSpeed = maxSpeed;
        this.maxAngularRate = maxAngularRate;
        
        // Create PID controller for heading
        headingController = new PIDController(5.0, 0.0, 0.1);
        headingController.enableContinuousInput(-Math.PI, Math.PI);
        headingController.setTolerance(0.02); // ~1 degree tolerance
        
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        // Start with current heading as target
        targetHeading = drivetrain.getState().Pose.getRotation();
    }

    @Override
    public void execute() {
        // Get joystick inputs with deadband
        double xSpeed = applyDeadband(-leftY.getAsDouble(), DEADBAND) * maxSpeed;
        double ySpeed = applyDeadband(-leftX.getAsDouble(), DEADBAND) * maxSpeed;
        double rightJoyX = applyDeadband(rightX.getAsDouble(), DEADBAND);
        double rightJoyY = applyDeadband(-rightY.getAsDouble(), DEADBAND);
        
        // Calculate rotation speed
        double rotationSpeed;
        
        // Check if right joystick is being used
        double rightJoyMagnitude = Math.hypot(rightJoyX, rightJoyY);
        
        if (rightJoyMagnitude > 0.2) {
            // Right joystick is deflected - calculate desired heading
            targetHeading = new Rotation2d(rightJoyX, rightJoyY);
        }
        
        // Use PID to rotate toward target heading
        Rotation2d currentHeading = drivetrain.getState().Pose.getRotation();
        rotationSpeed = headingController.calculate(
            currentHeading.getRadians(),
            targetHeading.getRadians()
        );
        
        // Clamp rotation speed
        rotationSpeed = MathUtil.clamp(rotationSpeed, -maxAngularRate, maxAngularRate);
        
        // Apply the field-centric request with calculated rotation
        drivetrain.setControl(
            drive.withVelocityX(xSpeed)
                 .withVelocityY(ySpeed)
                 .withRotationalRate(rotationSpeed)
        );
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.setControl(
            drive.withVelocityX(0)
                 .withVelocityY(0)
                 .withRotationalRate(0)
        );
    }

    /**
     * Apply deadband to joystick input
     */
    private double applyDeadband(double value, double deadband) {
        if (Math.abs(value) < deadband) {
            return 0.0;
        }
        return (value - Math.copySign(deadband, value)) / (1.0 - deadband);
    }
}