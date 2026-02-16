package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.SubsystemMotors;
import frc.robot.Constants;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

public class Indexer implements Subsystem {
    private static TalonFX Feeder;
    private static TalonFX MainIndexer;

    public Indexer() {
        Feeder = new TalonFX(SubsystemMotors.Indexer.kFeeder_Kraken, Constants.kRIO_CAN_BUS);
        var feederConfigs = new MotorOutputConfigs();
        feederConfigs.Inverted = InvertedValue.CounterClockwise_Positive;
        Feeder.getConfigurator().apply(feederConfigs);

        MainIndexer = new TalonFX(SubsystemMotors.Indexer.kMainIndexer_Kraken, Constants.kRIO_CAN_BUS);
        var mainIndexerConfigs = new MotorOutputConfigs();
        mainIndexerConfigs.Inverted = InvertedValue.Clockwise_Positive;
        MainIndexer.getConfigurator().apply(mainIndexerConfigs);
    }

    public Command runIndexer() {
        return runOnce(
            () -> {
                MainIndexer.set(SubsystemMotors.Indexer.kMainIndexerSpeed);
                Feeder.set(SubsystemMotors.Indexer.kFeederSpeed);
            }
        );
    }

    public Command runIndexer(double Speed) {
        return runOnce(
            () -> {
                MainIndexer.set(Speed);
                Feeder.set(Speed);
            }
        );
    }

    public boolean isFeederPositive() {
        return Feeder.get() > 0;
    }

    public Command stopIndexer() {
        return runOnce(
            () -> {
                MainIndexer.stopMotor();
                Feeder.stopMotor();
            }
        );
    }

    public Command feedToShotty() {
        return runOnce(
            () -> {
                Feeder.set(-SubsystemMotors.Indexer.kFeederSpeed);
            }
        );
    }

    public Command stopFeederOnly() {
        return runOnce(
            () -> {
                Feeder.stopMotor();
            }
        );
    }
}
