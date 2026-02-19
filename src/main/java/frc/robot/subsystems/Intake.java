package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private final SparkMax intakeIO;
    private final SparkMaxConfig intakeIOConfig;
    private final SparkClosedLoopController intakeIOController;
    private final RelativeEncoder intakeIOEncoder;

    private final TalonFX intakePivot;
    private final Slot0Configs pivotConfigs;

    public Intake() {
    intakeIO = new SparkMax(Constants.SubsystemMotors.Intake.kIntakeIO_NeoVortex, MotorType.kBrushless);
    intakeIOController = intakeIO.getClosedLoopController();
    intakeIOEncoder = intakeIO.getEncoder();
    intakeIOConfig = new SparkMaxConfig();

    // Use conversion factors from Constants.GearRatios
    intakeIOConfig.encoder
        .positionConversionFactor(Constants.GearRatios.kIntakePositionConversion)
        .velocityConversionFactor(Constants.GearRatios.kIntakeVelocityConversion);

    intakeIOConfig.closedLoop
        .p(Constants.MotorConfigs.Intake.kP)
        .i(Constants.MotorConfigs.Intake.kI)
        .d(Constants.MotorConfigs.Intake.kD)
        .outputRange(-1, 1);

    intakeIO.configure(intakeIOConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    intakePivot = new TalonFX(Constants.SubsystemMotors.Intake.kIntakeUpDown_Kraken, Constants.kRIO_CAN_BUS);

    var pivotConfigsMOut = new MotorOutputConfigs();

    intakePivot.getConfigurator().apply(pivotConfigsMOut);

    pivotConfigs = new Slot0Configs();
    pivotConfigs.kP = Constants.MotorConfigs.IntakePivot.kP;
    pivotConfigs.kI = Constants.MotorConfigs.IntakePivot.kI;
    pivotConfigs.kD = Constants.MotorConfigs.IntakePivot.kD;

    intakePivot.getConfigurator().apply(pivotConfigs);

    // Conservative current limits for intake pivot
    var intakePivotLimits = new TalonFXConfiguration()
        .withCurrentLimits(new CurrentLimitsConfigs()
            .withStatorCurrentLimit(20)
            .withStatorCurrentLimitEnable(true)
            .withSupplyCurrentLimit(15)
            .withSupplyCurrentLimitEnable(true));

    intakePivot.getConfigurator().apply(intakePivotLimits);
    }

    public Command pivot(Constants.Directions direction) {
        return runOnce(
                () -> {
                    var request = new PositionVoltage(0).withSlot(0);
                    if (direction == Constants.Directions.kUp)
                        intakePivot.setControl(request.withPosition(Constants.MotorConfigs.IntakePivot.Positions.kUp));

                    else if (direction == Constants.Directions.kDown)
                        intakePivot.setControl(request.withPosition(Constants.MotorConfigs.IntakePivot.Positions.kDown));

                    else if (direction == Constants.Directions.kStop)
                        intakePivot.stopMotor();
                });
    }

    public Command runIntake(Constants.Directions direction) {
        return runOnce(
                () -> {
                    if (direction == Constants.Directions.kIn)
                        intakeIOController.setSetpoint(Constants.MotorConfigs.Intake.Speeds.kIntakeIOSpeed,
                                ControlType.kVelocity);

                    else if (direction == Constants.Directions.kOut)
                        intakeIOController.setSetpoint(-Constants.MotorConfigs.Intake.Speeds.kIntakeIOSpeed,
                                ControlType.kVelocity);

                    else if (direction == Constants.Directions.kStop)
                        intakeIO.stopMotor();
                });
    }
}
