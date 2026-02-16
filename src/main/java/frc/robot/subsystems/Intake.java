package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class Intake implements Subsystem {
    private static Spark IntakeIO;
    private static TalonFX IntakePivot;

    public Intake() {
        IntakeIO = new Spark(Constants.SubsystemMotors.Intake.kIntakeIO_NeoVortex);
        IntakeIO.setInverted(false);

        IntakePivot = new TalonFX(Constants.SubsystemMotors.Intake.kIntakeUpDown_Kraken, Constants.kRIO_CAN_BUS);
        var pivotConfigs = new MotorOutputConfigs();
        
        IntakePivot.getConfigurator().apply(pivotConfigs);
    }

    public Command pivot(Constants.Directions direction) {
        return runOnce(
            () -> {
                if (direction == Constants.Directions.kUp) {
                    IntakePivot.set(Constants.SubsystemMotors.Intake.kIntakeUpDownSpeed);
                } else if (direction == Constants.Directions.kDown) {
                    IntakePivot.set(-Constants.SubsystemMotors.Intake.kIntakeUpDownSpeed);
                } else if (direction == Constants.Directions.kStop) {
                    IntakePivot.stopMotor();
                }
            }
        );
    }

    public Command runIntake(Constants.Directions direction) {
        return runOnce(
            () -> {
                if (direction == Constants.Directions.kIn) {
                    IntakeIO.set(Constants.SubsystemMotors.Intake.kIntakeIOSpeed);
                } else if (direction == Constants.Directions.kOut) {
                    IntakeIO.set(-Constants.SubsystemMotors.Intake.kIntakeIOSpeed);
                } else if (direction == Constants.Directions.kStop) {
                    IntakeIO.stopMotor();
                }
            }
        );
    }
}
