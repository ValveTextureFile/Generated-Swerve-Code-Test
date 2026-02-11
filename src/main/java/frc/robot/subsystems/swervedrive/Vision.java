package frc.robot.subsystems.swervedrive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class Vision extends SubsystemBase {
    private PhotonCamera cFront;
    private PhotonCamera cBack;

    public Vision() {
        cFront = new PhotonCamera(Constants.Vision.kDefaultFrontCameraName);
        cBack = new PhotonCamera(Constants.Vision.kDefaultBackCameraName);
    }

    public PhotonTrackedTarget getFrontTarget() {
        PhotonPipelineResult result = cFront.getLatestResult();
        if (result.hasTargets()) {
            return result.getBestTarget();
        } else {
            return null;
        }
    }

    public PhotonTrackedTarget getBackTarget() {
        PhotonPipelineResult result = cBack.getLatestResult();
        if (result.hasTargets()) {
            return result.getBestTarget();
        } else {
            return null;
        }
    }

}


// public m_Vision = new Vision("frontcamera", "backcamera");
