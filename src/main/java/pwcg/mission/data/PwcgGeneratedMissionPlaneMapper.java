package pwcg.mission.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class PwcgGeneratedMissionPlaneMapper
{
    private Map<String, String> alliedPlaneMap = new HashMap<String, String>();
    private Map<String, String> axisPlaneMap = new HashMap<String, String>();

    public void createPlaneMapforFlight(Campaign campaign, List<PwcgGeneratedMissionPlaneData> missionPlanes) throws PWCGException 
    {
        for (PwcgGeneratedMissionPlaneData missionPlaneData : missionPlanes)
        {
            Side side = determinePlaneSide(campaign, missionPlaneData);
                            
            if (side == Side.AXIS)
            {
                axisPlaneMap.put(missionPlaneData.getAircraftType(), missionPlaneData.getAircraftType());
            }
            else
            {
                alliedPlaneMap.put(missionPlaneData.getAircraftType(), missionPlaneData.getAircraftType());
            }
            
        }
        
        // Always add balloons for RoF
        if (PWCGContextManager.isRoF())
        {
            alliedPlaneMap.put(PlaneType.BALLOON, PlaneType.BALLOON);
            axisPlaneMap.put(PlaneType.BALLOON, PlaneType.BALLOON);
        }
    }

    private Side determinePlaneSide(Campaign campaign, PwcgGeneratedMissionPlaneData missionPlane) throws PWCGException
    {
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(missionPlane.getSquadronId());            
        Side side = squadron.determineSquadronCountry(campaign.getDate()).getSide();
        return side;
    }

    public Map<String, String> getAlliedPlaneMap()
    {
        return alliedPlaneMap;
    }

    public Map<String, String> getAxisPlaneMap()
    {
        return axisPlaneMap;
    }

    
}
