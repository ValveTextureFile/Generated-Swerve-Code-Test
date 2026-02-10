package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;

public class Vision implements Subsystem {
    private static Vision instance;

    public static Vision getInstance() {
        if (instance == null) {
            instance = new Vision();
        }
        return instance;
    }

    private Vision() {
        // Initialize vision system here
    }

    // Add methods to interact with the vision system here
    
}
