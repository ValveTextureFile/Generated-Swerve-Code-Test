package frc.robot;

import com.ctre.phoenix6.CANBus;

public class Constants {
    public static class PhysicalProperties {
        /**
         * Wheel diameter, duh.
         */
        public static final double kWheelDiameter_In = 4;
        /**
         * Swervedrive Specialties - MK5n
         */
        public static class Drivetrain {
            /**
             * MK5n has 3 different gear ratios, R1, R2, and R3. R1 is the fastest, and R3 is the slowest, but also the most powerful. We will be using R2 for this year, but we may switch to R1 or R3 depending on how testing goes.
             */
            public static class GearRatios {
                public static final double kR1 = 7.03;
                public static final double kR2 = 6.03;
                public static final double kR3 = 5.27;
            }
        }
    }

    /**
     * Default RIO canbus
     */
    public static final CANBus kRIO_CAN_BUS = CANBus.roboRIO();

    public static double RPMtoRPS(double RPM) {
        return RPM / 60;
    }

    public static double MeterstoRPS(double Meters, double GearRatio) {
        return Meters / (Math.PI * PhysicalProperties.kWheelDiameter_In * GearRatio);
    }

    /**
     * Personal types
     */
    public static enum Directions {
        kUp, kDown, kCenter, kIn, kOut, kStop,
    }

    /**
     * All motors for our subsystems
     *
     * NOTE: Only hardware IDs remain here. Speeds/positions are centralized under MotorPIDs.
     */
    public static class SubsystemMotors {

        /**
         * Indexer
         */
        public static class Indexer {
            /**
             * The green wheel thingy
             */
            public static final int kFeeder_Kraken              = 0;

            /**
             * The literal Aldi's cashier station conveyor belt
             */
            public static final int kMainIndexer_Kraken         = 1;
        }

        /**
         * Intake
         */
        public static class Intake {
            /**
             * In and out burger
             */
            public static final int kIntakeIO_NeoVortex         = 2;

            /**
             * Pivot the thing
             */
            public static final int kIntakeUpDown_Kraken       = 3;
        }

        /**
         * Shooter
         */
        public static class Shotty {
            /**
             * Shooter part 1
             */
            public static final int kShooterOut1_Kraken         = 4;
            /**
             * Shooter part 2
             */
            public static final int kShooterOut2_Kraken         = 5;

            /**
             * Shooter pivot
             */
            public static final int kShooterPivot_Kraken        = 6;
        }

        /**
         * Climber
         * @apiNote Not implemented
         */
        public static class Climber {
            public static final int kClimber1_Kraken        = 7;
            public static final int kClimber2_Kraken        = 8;
        }
    }

    /**
     * Centralized motor PID tunings moved out of SubsystemMotors.
     * Maintains the same PID class names.
     *
     * All speed and position related constants live here.
     */
    public static class MotorPIDs {
        public static class IntakePivot {
            public static final double kP = .0001;
            public static final double kI = .0;
            public static final double kD = .0;

            public static class Positions {
                public static final double kUp = 0;
                public static final double kDown = 90;
            }
        }

        public static class Shooter {
            public static final double kP = .1;
            public static final double kI = .0;
            public static final double kD = .0;

            /**
             * Shooter speed in RPM. Converted to RPS for runtime use below.
             */
            public static final double kShooterOutSpeed_RPM = 4000;

            /**
             * Pivot speed for shooter pivot motor (unitless scalar).
             */
            public static final double kShooterPivotSpeed = .4;

            public static class Speeds {
                public static final double kShooterOut_S1_RPS = RPMtoRPS(Shooter.kShooterOutSpeed_RPM);
            }
        }

        public static class ShooterPivot {
            public static final double kP = .0001;
            public static final double kI = .0;
            public static final double kD = .0;

            public static class Positions {
                public static final double kUp = 0;
                public static final double kCenter = 45;
                public static final double kDown = 90;
            }
        }

        public static class Intake {
            /**
             * Scalar speed for the intake in/out motor.
             */
            public static final double kIntakeIOSpeed = .4;

            /**
             * Scalar speed for the intake pivot motor.
             */
            public static final double kIntakeUpDownSpeed = .4;
        }

        public static class Indexer {
            /**
             * Scalar speed for feeder wheel.
             */
            public static final double kFeederSpeed = .7;

            /**
             * Scalar speed for the main indexer conveyor.
             */
            public static final double kMainIndexerSpeed = .7;
        }

        public static class Climber {
            // Climber tuning placeholders (if/when needed)
        }
    }
}
