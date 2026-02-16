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
        kUp, kDown, kIn, kOut, kStop,
    }

    /**
     * All motors for our subsystems
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

            public static final double kFeederSpeed             =  .7; 

            /**
             * The literal Aldi's cashier station conveyor belt
             */
            public static final int kMainIndexer_Kraken         = 1;

            public static final double kMainIndexerSpeed        = .7;
        }

        /**
         * Intake
         */
        public static class Intake {
            /**
             * In and out burger
             */
            public static final int kIntakeIO_NeoVortex         = 2;
            public static final double kIntakeIOSpeed           = .4;

            /**
             * Pivot the thing
             */
            public static final int kIntakeUpDown_Kraken       = 3;
            public static final double kIntakeUpDownSpeed      = .4;
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
             * Final speed for both of the motors
             */
            public static final double kShooterOutSpeed_RPM         = 4000;

            public static class ShooterPID {
                public static final double kP = .1;
                public static final double kI = .0;
                public static final double kD = .0; 
            }

            /**
             * Shooter pivot
             */
            public static final int kShooterPivot_Kraken        = 6;

            public static final double kShooterPivotSpeed       = .4;
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
}
