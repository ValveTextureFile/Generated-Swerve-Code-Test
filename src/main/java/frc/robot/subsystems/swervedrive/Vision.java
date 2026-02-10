package frc.robot.subsystems.swervedrive;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

import org.photonvision.PhotonCamera;
// import org.photonvision.proto.Photon;
import org.photonvision.targeting.PhotonPipelineResult;

public class Vision implements Subsystem {
    private PhotonCamera cFront;
    private PhotonCamera cBack;

    private Vision() {
        cFront = new PhotonCamera(Constants.Vision.kDefaultFrontCameraName);
        cBack = new PhotonCamera(Constants.Vision.kDefaultBackCameraName);
    }

    private Vision(String front, String back) {
        cFront = new PhotonCamera(front.isEmpty() ? Constants.Vision.kDefaultFrontCameraName : front);
        cBack = new PhotonCamera(back.isEmpty() ? Constants.Vision.kDefaultBackCameraName : back);
    }

    public PhotonCamera getFront() {
        return cFront;
    }

    public PhotonCamera getBack() {
        return cBack;
    }

    public PhotonPipelineResult getResult(PhotonCamera c) {
        return c.getLatestResult();
    }

    public boolean IsLookingAtApriltag(PhotonPipelineResult res) {
        return res.hasTargets();
    }

}


// public m_Vision = new Vision("frontcamera", "backcamera");
