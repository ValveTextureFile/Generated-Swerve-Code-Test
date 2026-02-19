package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SubsystemMotors;
import frc.robot.Constants;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

/**
 * IDEA:
 *  - main indexer jam detection:
 *   - if main indexer try to run, but main indexer velocity low,
 *   - reverse direction of main indexer
 */

public class Indexer extends SubsystemBase {
    private final TalonFX feeder;
    private final Slot0Configs feederConfigs;
    private final TalonFX mainIndexer;

    public Indexer() {
        feeder = new TalonFX(SubsystemMotors.Indexer.kFeeder_Kraken, Constants.kRIO_CAN_BUS);

        var feederConfigsMout = new MotorOutputConfigs();
        feederConfigsMout.Inverted = InvertedValue.CounterClockwise_Positive;

        feeder.getConfigurator().apply(feederConfigsMout);

        feederConfigs = new Slot0Configs();
        feederConfigs.kP = Constants.MotorConfigs.IndexerFeeder.kP;
        feederConfigs.kI = Constants.MotorConfigs.IndexerFeeder.kI;
        feederConfigs.kD = Constants.MotorConfigs.IndexerFeeder.kD;


    feeder.getConfigurator().apply(feederConfigs);

    // Conservative current limits for feeder motor
    var feederLimits = new TalonFXConfiguration()
        .withCurrentLimits(new CurrentLimitsConfigs()
            .withStatorCurrentLimit(30)
            .withStatorCurrentLimitEnable(true)
            .withSupplyCurrentLimit(20)
            .withSupplyCurrentLimitEnable(true));

    feeder.getConfigurator().apply(feederLimits);

        // | |
        // |\ \
        // |~\ \
        // |~~\ \
        // |~~ \ \
        // its a slide

        mainIndexer = new TalonFX(SubsystemMotors.Indexer.kMainIndexer_Kraken, Constants.kRIO_CAN_BUS);

        var mainIndexerConfigs = new MotorOutputConfigs();
        mainIndexerConfigs.Inverted = InvertedValue.Clockwise_Positive;


    mainIndexer.getConfigurator().apply(mainIndexerConfigs);

    // Conservative current limits for main indexer
    var mainIndexerLimits = new TalonFXConfiguration()
        .withCurrentLimits(new CurrentLimitsConfigs()
            .withStatorCurrentLimit(30)
            .withStatorCurrentLimitEnable(true)
            .withSupplyCurrentLimit(20)
            .withSupplyCurrentLimitEnable(true));

    mainIndexer.getConfigurator().apply(mainIndexerLimits);
    }

    public Command runIndexer() {
        return runOnce(
                () -> {
                    mainIndexer.set(Constants.MotorConfigs.Indexer.Speeds.kMainIndexerSpeed);

                    var request = new VelocityVoltage(0).withSlot(0);
                    feeder.setControl(request.withVelocity(Constants.MotorConfigs.IndexerFeeder.Speeds.kS1));
                });
    }

    public boolean isFeederPositive() {
        return feeder.get() > 0;
    }

    public Command stopIndexer() {
        return runOnce(
                () -> {
                    mainIndexer.stopMotor();
                    feeder.stopMotor();
                });
    }

    public Command feedToShotty() {
        return runOnce(
                () -> {
                    var request = new VelocityVoltage(0).withSlot(0);
                    feeder.setControl(request.withVelocity(-Constants.MotorConfigs.IndexerFeeder.Speeds.kS1));
                });
    }

    public Command stopFeederOnly() {
        return runOnce(
                () -> {
                    feeder.stopMotor();
                });
    }

    @Override
    public void periodic() {
    }
}
