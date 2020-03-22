package pwcg.campaign.plane;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public interface IPlaneMarkingManager {
    public void allocatePlaneIdCode(Campaign campaign, int squadronId, Equipment equipment, EquippedPlane equippedPlane) throws PWCGException;

    public String determineDisplayMarkings(Campaign campaign, EquippedPlane equippedPlane) throws PWCGException;

	void generatePlaneSerial(Date date, EquippedPlane plane, int service) throws PWCGException;

	void generatePlaneSerialHistoric(Campaign campaign, EquippedPlane equippedPlane, int service) throws PWCGException;
}
