package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
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

    public Shooter() {
        Shooter1 = new TalonFX(
                Constants.SubsystemMotors.Shotty.kShooterOut1_Kraken,
                Constants.kRIO_CAN_BUS);
        Shooter2 = new TalonFX(
                Constants.SubsystemMotors.Shotty.kShooterOut2_Kraken,
                Constants.kRIO_CAN_BUS);

        // set up pid
        ShooterConfigs = new Slot0Configs();
        ShooterConfigs.kP = Constants.SubsystemMotors.Shotty.ShooterPID.kP;
        ShooterConfigs.kI = Constants.SubsystemMotors.Shotty.ShooterPID.kI;
        ShooterConfigs.kD = Constants.SubsystemMotors.Shotty.ShooterPID.kD;

        Shooter1.getConfigurator().apply(ShooterConfigs);
        Shooter2.getConfigurator().apply(ShooterConfigs);

        ShooterPivot = new TalonFX(
                Constants.SubsystemMotors.Shotty.kShooterPivot_Kraken,
                Constants.kRIO_CAN_BUS);
        var pivotConfigs = new MotorOutputConfigs();
        pivotConfigs.Inverted = InvertedValue.Clockwise_Positive;
        ShooterPivot.getConfigurator().apply(pivotConfigs);
    }

    public Command runShooter() {
        return runOnce(
                () -> {
                    Shooter1.setControl(new VelocityVoltage(
                            Constants.RPMtoRPS(Constants.SubsystemMotors.Shotty.kShooterOutSpeed_RPM)).withSlot(0)
                            .withFeedForward(.5));
                    Shooter2.setControl(new VelocityVoltage(
                            Constants.RPMtoRPS(Constants.SubsystemMotors.Shotty.kShooterOutSpeed_RPM)).withSlot(0)
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
                    if (direction == Constants.Directions.kUp) {
                        ShooterPivot.set(Constants.SubsystemMotors.Shotty.kShooterPivotSpeed);
                    } else if (direction == Constants.Directions.kDown) {
                        ShooterPivot.set(-Constants.SubsystemMotors.Shotty.kShooterPivotSpeed);
                    } else if (direction == Constants.Directions.kStop) {
                        ShooterPivot.stopMotor();
                    }
                });
    }
}
