package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class Shooter implements Subsystem {
    private static TalonFX Shooter1;
    private static TalonFX Shooter2;

    private static Slot0Configs ShooterConfigs; // should be relativly the same for both motors, so we can just use one
                                                // config object

    private static TalonFX ShooterPivot;

    private static Slot1Configs ShooterPivotConfigs;

    public Shooter() {
        Shooter1 = new TalonFX(
                Constants.SubsystemMotors.Shotty.kShooterOut1_Kraken,
                Constants.kRIO_CAN_BUS);
        Shooter2 = new TalonFX(
                Constants.SubsystemMotors.Shotty.kShooterOut2_Kraken,
                Constants.kRIO_CAN_BUS);

        // set up pid
        ShooterConfigs = new Slot0Configs();
        ShooterConfigs.kP = Constants.MotorPIDs.Shooter.kP;
        ShooterConfigs.kI = Constants.MotorPIDs.Shooter.kI;
        ShooterConfigs.kD = Constants.MotorPIDs.Shooter.kD;

        Shooter1.getConfigurator().apply(ShooterConfigs);
        Shooter2.getConfigurator().apply(ShooterConfigs);

        ShooterPivot = new TalonFX(
                Constants.SubsystemMotors.Shotty.kShooterPivot_Kraken,
                Constants.kRIO_CAN_BUS);
        var pivotConfigs = new MotorOutputConfigs();
        pivotConfigs.Inverted = InvertedValue.Clockwise_Positive;
        ShooterPivot.getConfigurator().apply(pivotConfigs);

        ShooterPivotConfigs = new Slot1Configs();
        ShooterPivotConfigs.kP = Constants.MotorPIDs.ShooterPivot.kP;
        ShooterPivotConfigs.kI = Constants.MotorPIDs.ShooterPivot.kI;
        ShooterPivotConfigs.kD = Constants.MotorPIDs.ShooterPivot.kD;
        ShooterPivot.getConfigurator().apply(ShooterPivotConfigs);

    }

    public Command runShooter() {
        return runOnce(
                () -> {
                    Shooter1.setControl(new VelocityVoltage(
                            Constants.RPMtoRPS(Constants.MotorPIDs.Shooter.kShooterOutSpeed_RPM)).withSlot(0)
                            .withFeedForward(.5));
                    Shooter2.setControl(new VelocityVoltage(
                            Constants.RPMtoRPS(Constants.MotorPIDs.Shooter.kShooterOutSpeed_RPM)).withSlot(0)
                            .withFeedForward(.5));
                });
    }

    public Command requestRPM(double RPM) {
        return runOnce(
                () -> {
                    var request = new VelocityVoltage(0).withSlot(0);
                    Shooter1.setControl(request.withVelocity(Constants.RPMtoRPS(RPM)).withFeedForward(.5));
                });
    }

    public Command pivot(Constants.Directions direction) {
        return runOnce(
                () -> {
                    var request = new PositionVoltage(0).withSlot(1);
                    if (direction == Constants.Directions.kUp) {
                        ShooterPivot.setControl(request.withPosition(Constants.MotorPIDs.ShooterPivot.Positions.kUp));
                    } else if (direction == Constants.Directions.kDown) {
                        ShooterPivot.setControl(request.withPosition(Constants.MotorPIDs.ShooterPivot.Positions.kDown));
                    } else if (direction == Constants.Directions.kCenter) {
                        ShooterPivot.setControl(request.withPosition(Constants.MotorPIDs.ShooterPivot.Positions.kCenter));
                    } else if (direction == Constants.Directions.kStop) {
                        ShooterPivot.stopMotor();
                    }
                });
    }
}
