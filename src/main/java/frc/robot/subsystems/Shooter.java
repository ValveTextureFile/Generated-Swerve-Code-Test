package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {
    private final TalonFX shooter1;
    private final TalonFX shooter2;

    private final Slot0Configs shooterConfigs; // should be relatively the same for both motors, so we can just use one

    private final TalonFX shooterPivot;

    private final Slot1Configs shooterPivotConfigs;

    public Shooter() {
    shooter1 = new TalonFX(Constants.SubsystemMotors.Shotty.kShooterOut1_Kraken, Constants.kRIO_CAN_BUS);

    shooter2 = new TalonFX(Constants.SubsystemMotors.Shotty.kShooterOut2_Kraken, Constants.kRIO_CAN_BUS);

    // set up pid
    shooterConfigs = new Slot0Configs();
    shooterConfigs.kP = Constants.MotorConfigs.Shooter.kP;
    shooterConfigs.kI = Constants.MotorConfigs.Shooter.kI;
    shooterConfigs.kD = Constants.MotorConfigs.Shooter.kD;

    shooter1.getConfigurator().apply(shooterConfigs);
    shooter2.getConfigurator().apply(shooterConfigs);

    // Conservative current limits for shooter motors
    var shooterCurrentLimits = new TalonFXConfiguration()
        .withCurrentLimits(new CurrentLimitsConfigs()
            .withStatorCurrentLimit(40)
            .withStatorCurrentLimitEnable(true)
            .withSupplyCurrentLimit(30)
            .withSupplyCurrentLimitEnable(true));

    shooter1.getConfigurator().apply(shooterCurrentLimits);
    shooter2.getConfigurator().apply(shooterCurrentLimits);

    shooterPivot = new TalonFX(Constants.SubsystemMotors.Shotty.kShooterPivot_Kraken, Constants.kRIO_CAN_BUS);

    var pivotConfigs = new MotorOutputConfigs();
    pivotConfigs.Inverted = InvertedValue.Clockwise_Positive;


    shooterPivot.getConfigurator().apply(pivotConfigs);

    // Conservative current limits for shooter pivot
    var shooterPivotLimits = new TalonFXConfiguration()
        .withCurrentLimits(new CurrentLimitsConfigs()
            .withStatorCurrentLimit(20)
            .withStatorCurrentLimitEnable(true)
            .withSupplyCurrentLimit(15)
            .withSupplyCurrentLimitEnable(true));

    shooterPivot.getConfigurator().apply(shooterPivotLimits);

    shooterPivotConfigs = new Slot1Configs();
    shooterPivotConfigs.kP = Constants.MotorConfigs.ShooterPivot.kP;
    shooterPivotConfigs.kI = Constants.MotorConfigs.ShooterPivot.kI;
    shooterPivotConfigs.kD = Constants.MotorConfigs.ShooterPivot.kD;

    shooterPivot.getConfigurator().apply(shooterPivotConfigs);

    }

    public Command runShooter() {
        return runOnce(
                () -> {
                    var request = new VelocityVoltage(
                            Constants.rpmToRps(Constants.MotorConfigs.Shooter.Speeds.kShooterOutSpeed_RPM))
                            .withSlot(0)
                            .withFeedForward(.5);
                    shooter1.setControl(request);
                    shooter2.setControl(request);
                });
    }

    public Command requestRPM(double RPM) {
        return runOnce(
                () -> {
                    var request = new VelocityVoltage(Constants.rpmToRps(RPM)).withSlot(0);
                    shooter1.setControl(request);
                    shooter2.setControl(request);
                });
    }

    public Command pivot(Constants.Directions direction) {
        return runOnce(
                () -> {
                    var request = new PositionVoltage(0).withSlot(1);

                    if (direction == Constants.Directions.kUp)
                        shooterPivot.setControl(request.withPosition(Constants.MotorConfigs.ShooterPivot.Positions.kUp));

                    else if (direction == Constants.Directions.kDown)
                        shooterPivot.setControl(request.withPosition(Constants.MotorConfigs.ShooterPivot.Positions.kDown));

                    else if (direction == Constants.Directions.kCenter)
                        shooterPivot.setControl(request.withPosition(Constants.MotorConfigs.ShooterPivot.Positions.kCenter));

                    else if (direction == Constants.Directions.kStop)
                        shooterPivot.stopMotor();

                });
    }
}
