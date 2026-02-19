package frc.robot;

import com.ctre.phoenix6.CANBus;

public final class Constants {
    private Constants() {
    }

    public static final class DriverStation {
        public static final class Controllers {
            public static final int kYanny = 0;
            public static final int kLaurel = 1;
        }

    }

    /**
     * Standard RIO canbus
     */
    public static final CANBus kRIO_CAN_BUS = CANBus.roboRIO();

    public static final class Physical {
        /** Wheel diameter in inches (hardware spec). */
        public static final double kWheelDiameterIn = 4.0;

        /** Conversion factor: meters per inch. */
        public static final double kMetersPerInch = 0.0254;

        /** Wheel diameter in meters (derived). */
        public static final double kWheelDiameterM = kWheelDiameterIn * kMetersPerInch;

        public static final class Drivetrain {
            public static final class GearRatios {
                public static final double kR1 = 7.03;
                public static final double kR2 = 6.03;
                public static final double kR3 = 5.27;
            }
        }
    }

    // Utility conversions
    public static double rpmToRps(double rpm) {
        return rpm / 60.0;
    }

    /**
     * Convert a linear speed (m/s) to wheel rotations-per-second (wheel RPS).
     * wheelRPS = linearSpeed / (pi * wheelDiameterMeters)
     */
    public static double linearMetersPerSecToWheelRps(double metersPerSec) {
        return metersPerSec / (Math.PI * Physical.kWheelDiameterM);
    }

    /**
     * Convert a linear speed (m/s) to motor rotations-per-second (motor RPS),
     * given a gear ratio defined as (motor rotations) / (wheel rotations).
     */
    public static double linearMetersPerSecToMotorRps(double metersPerSec, double motorPerWheelRatio) {
        return linearMetersPerSecToWheelRps(metersPerSec) * motorPerWheelRatio;
    }

    /**
     * Convert wheel rotations-per-second (wheel RPS) to motor RPS using a gear
     * ratio
     * defined as (motor rotations) / (wheel rotations).
     */
    public static double wheelRpsToMotorRps(double wheelRps, double motorPerWheelRatio) {
        return wheelRps * motorPerWheelRatio;
    }

    /** General direction enums for ease. */
    public enum Directions {
        kUp, kDown, kCenter, kIn, kOut, kStop,
    }

    public static final class SubsystemMotors {
        public static final class Indexer {
            public static final int kFeeder_Kraken = 0;
            public static final int kMainIndexer_Kraken = 1;
        }

        public static final class Intake {
            public static final int kIntakeIO_NeoVortex = 2;
            public static final int kIntakeUpDown_Kraken = 3;
        }

        public static final class Shotty {
            public static final int kShooterOut1_Kraken = 4;
            public static final int kShooterOut2_Kraken = 5;
            public static final int kShooterPivot_Kraken = 6;
        }

        public static final class Climber {
            public static final int kClimber1_Kraken = 7;
            public static final int kClimber2_Kraken = 8;
        }
    }

    public static final class MotorConfigs {
        public static final class Intake {
            public static final double kP = 0.001;
            public static final double kI = 0.0;
            public static final double kD = 0.0;

            public static final class Speeds {
                public static final double kIntakeIOSpeed = 0.4;
                public static final double kIntakeUpDownSpeed = 0.4;
            }
        }

        public static final class IntakePivot {
            public static final double kP = 0.0001;
            public static final double kI = 0.0;
            public static final double kD = 0.0;

            public static final class Positions {
                public static final double kUp = 0.0;
                public static final double kDown = 90.0;
            }
        }

        public static final class Shooter {
            public static final double kP = 0.1;
            public static final double kI = 0.0;
            public static final double kD = 0.0;

            public static final class Speeds {
                public static final double kShooterOutSpeed_RPM = 4000.0;
                public static final double kShooterOut_S1_RPS = rpmToRps(kShooterOutSpeed_RPM);
                public static final double kShooterOut_S2_RPS = kShooterOut_S1_RPS / 2.0;
                public static final double kShooterOut_S3_RPS = kShooterOut_S1_RPS * 2.0;

                public static final double kShooterPivotSpeed = 0.4;
            }
        }

        public static final class ShooterPivot {
            public static final double kP = 0.0001;
            public static final double kI = 0.0;
            public static final double kD = 0.0;

            public static final class Positions {
                public static final double kUp = 0.0;
                public static final double kCenter = 45.0;
                public static final double kDown = 90.0;
            }
        }

        public static final class Indexer {
            public static final class Speeds {
                public static final double kMainIndexerSpeed = 0.5;
            }
        }

        public static final class IndexerFeeder {
            public static final double kP = 0.001;
            public static final double kI = 0.0;
            public static final double kD = 0.0;

            public static final class Speeds {
                public static final double kBaseRPM = 4000.0;
                public static final double kS1 = rpmToRps(kBaseRPM);
                public static final double kS2 = kS1 / 2.0;
                public static final double kS3 = kS1 * 2.0;
            }
        }

        public static final class Climber {
            // placeholders?
        }
    }

    /**
     * Placeholder gear ratio / encoder conversion constants for subsystems.
     */
    public static final class GearRatios {
        // Shooter: motor rotations per wheel/roller rotation (motor:output)
        public static double kShooterMotorToWheel = 1.0;

        // Intake roller: motor rotations per roller rotation
        public static double kIntakeMotorToRoller = 1.0;

        // Indexer: motor rotations per roller rotation
        public static double kIndexerMotorToRoller = 1.0;

        // Pivot gear ratios (motor rotations per arm degree/rotation); keep 1.0 as placeholder
        public static double kIntakePivotMotorToOutput = 1.0;
        public static double kShooterPivotMotorToOutput = 1.0;

        // Encoder conversion factors (default to 1.0).
        public static double kIntakePositionConversion = 1.0;
        public static double kIntakeVelocityConversion = 1.0;
    }
}
