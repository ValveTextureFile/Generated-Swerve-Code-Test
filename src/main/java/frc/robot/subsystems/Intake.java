package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
// import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class Intake implements Subsystem {
    private static Spark IntakeIO;
    private static TalonFX IntakePivot;
    private static Slot0Configs pivotConfigs;

    public Intake() {
        IntakeIO = new Spark(Constants.SubsystemMotors.Intake.kIntakeIO_NeoVortex);
        IntakeIO.setInverted(false);

        IntakePivot = new TalonFX(Constants.SubsystemMotors.Intake.kIntakeUpDown_Kraken, Constants.kRIO_CAN_BUS);

        var pivotConfigsMOut = new MotorOutputConfigs();

        IntakePivot.getConfigurator().apply(pivotConfigsMOut);

        pivotConfigs = new Slot0Configs();
        pivotConfigs.kP = Constants.MotorConfigs.IntakePivot.kP;
        pivotConfigs.kI = Constants.MotorConfigs.IntakePivot.kI;
        pivotConfigs.kD = Constants.MotorConfigs.IntakePivot.kD;

        IntakePivot.getConfigurator().apply(pivotConfigs);
    }

    public Command pivot(Constants.Directions direction) {
        return runOnce(
                () -> {
                    var request = new PositionVoltage(0).withSlot(0);

                    if (direction == Constants.Directions.kUp)
                        IntakePivot
                                .setControl(request.withPosition(Constants.MotorConfigs.IntakePivot.Positions.kUp));

                    else if (direction == Constants.Directions.kDown)
                        IntakePivot
                                .setControl(request.withPosition(Constants.MotorConfigs.IntakePivot.Positions.kDown));

                    else if (direction == Constants.Directions.kStop)
                        IntakePivot.stopMotor();
                });
    }

    public Command runIntake(Constants.Directions direction) {
        return runOnce(
                () -> {
                    if (direction == Constants.Directions.kIn)
                        IntakeIO.set(Constants.MotorConfigs.Intake.Speeds.kIntakeIOSpeed);

                    else if (direction == Constants.Directions.kOut)
                        IntakeIO.set(-Constants.MotorConfigs.Intake.Speeds.kIntakeIOSpeed);

                    else if (direction == Constants.Directions.kStop)
                        IntakeIO.stopMotor();
                });
    }
}
