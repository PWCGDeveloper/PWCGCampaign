package pwcg.product.fc.plane;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.IPlaneMarkingManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public class FCPlaneMarkingManager implements IPlaneMarkingManager
{

    @Override
    public void initialize(Campaign campaign) throws PWCGException
    {        
    }

    @Override
    public void recordPlaneIdCode(Campaign campaign, int squadronId, EquippedPlane equippedPlane) throws PWCGException
    {
    }

    @Override
    public String determineDisplayMarkings(Campaign campaign, EquippedPlane equippedPlane) throws PWCGException
    {
        return "";
    }

    @Override
    public void writeTacticalCodes(BufferedWriter writer, Campaign campaign, PlaneMcu planeMcu) throws PWCGException
    {
    }
}
