package pwcg.campaign.plane;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public interface IPlaneMarkingManager {
    public void recordPlaneIdCode(Campaign campaign, int squadronId, EquippedPlane equippedPlane) throws PWCGException;
    void initialize(Campaign campaign) throws PWCGException;
}
