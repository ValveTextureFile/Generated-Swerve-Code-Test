package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.SubsystemMotors;
import frc.robot.Constants;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

public class Indexer implements Subsystem {
    private static TalonFX Feeder;
    private static Slot0Configs feederConfigs;
    private static TalonFX MainIndexer;

    public Indexer() {
        Feeder = new TalonFX(SubsystemMotors.Indexer.kFeeder_Kraken, Constants.kRIO_CAN_BUS);

        var feederConfigsMout = new MotorOutputConfigs();
        feederConfigsMout.Inverted = InvertedValue.CounterClockwise_Positive;

        Feeder.getConfigurator().apply(feederConfigsMout);

        feederConfigs = new Slot0Configs();
        feederConfigs.kP = Constants.MotorConfigs.IndexerFeeder.kP;
        feederConfigs.kI = Constants.MotorConfigs.IndexerFeeder.kI;
        feederConfigs.kD = Constants.MotorConfigs.IndexerFeeder.kD;

        Feeder.getConfigurator().apply(feederConfigs);

        // | |
        // |\ \
        // |~\ \
        // |~~\ \
        // |~~ \ \
        // its a slide

        MainIndexer = new TalonFX(SubsystemMotors.Indexer.kMainIndexer_Kraken, Constants.kRIO_CAN_BUS);

        var mainIndexerConfigs = new MotorOutputConfigs();
        mainIndexerConfigs.Inverted = InvertedValue.Clockwise_Positive;
        
        MainIndexer.getConfigurator().apply(mainIndexerConfigs);
    }

    public Command runIndexer() {
        return runOnce(
                () -> {
                    MainIndexer.set(Constants.MotorConfigs.Indexer.Speeds.kMainIndexerSpeed);

                    var request = new VelocityVoltage(0).withSlot(0);
                    Feeder.setControl(request.withVelocity(Constants.MotorConfigs.IndexerFeeder.Speeds.kS1));
                });
    }

    public boolean isFeederPositive() {
        return Feeder.get() > 0;
    }

    public Command stopIndexer() {
        return runOnce(
                () -> {
                    MainIndexer.stopMotor();
                    Feeder.stopMotor();
                });
    }

    public Command feedToShotty() {
        return runOnce(
                () -> {
                    var request = new VelocityVoltage(0).withSlot(0);
                    Feeder.setControl(request.withVelocity(-Constants.MotorConfigs.IndexerFeeder.Speeds.kS1));
                });
    }

    public Command stopFeederOnly() {
        return runOnce(
                () -> {
                    Feeder.stopMotor();
                });
    }
}
