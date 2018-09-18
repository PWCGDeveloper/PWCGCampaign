package pwcg.campaign.plane;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public interface IPlaneMarkingManager {
    public void allocatePlaneIdCode(Campaign campaign, int squadronId, Equipment equipment, EquippedPlane equippedPlane) throws PWCGException;
}
