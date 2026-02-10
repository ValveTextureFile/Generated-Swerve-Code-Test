package frc.robot.subsystems;

import org.opencv.ml.ANN_MLP;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.Vision;

public class AimBot implements Subsystem {
    public static Vision vision;
    
    public AimBot(Vision v) {
        this.vision = v;
    }
}
