package pwcg.mission.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.tank.TankType;
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

        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            alliedPlaneMap.put(TankType.BALLOON, TankType.BALLOON);
            axisPlaneMap.put(TankType.BALLOON, TankType.BALLOON);
        }
    }

    private Side determinePlaneSide(Campaign campaign, PwcgGeneratedMissionPlaneData missionPlane) throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(missionPlane.getSquadronId());            
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
