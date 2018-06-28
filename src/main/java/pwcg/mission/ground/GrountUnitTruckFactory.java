package pwcg.mission.ground;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.transport.GroundTruckConvoyUnit;
import pwcg.mission.mcu.Coalition;

public class GrountUnitTruckFactory
{
    private Campaign campaign;
    private Coordinate location;
    private ICountry country;
    private Date date;

    public GrountUnitTruckFactory (Campaign campaign, Coordinate location, ICountry country, Date date)
    {
        this.campaign  = campaign;
        this.location  = location.copy();
        this.country  = country;
        this.date  = date;
    }


    public GroundTruckConvoyUnit createTruckConvoy () throws PWCGException
    {
        GroundTruckConvoyUnit convoy = null;
        
        GroupManager groupData =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        Bridge destinationBridge = groupData.getBridgeFinder().findDestinationBridge(location, country.getSide(), date);
        if (destinationBridge != null)
        {
            MissionBeginUnitCheckZone missionBeginUnit = buildMissionBegin();
            convoy = buildTruckConvoy(destinationBridge, missionBeginUnit);
        }
         
        return convoy;
    }

    private MissionBeginUnitCheckZone buildMissionBegin() throws PWCGException
    {
        Coalition playerCoalition = Coalition.getFriendlyCoalition(campaign.determineCountry());
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(location, 12000, playerCoalition);
        return missionBeginUnit;
    }

    private GroundTruckConvoyUnit buildTruckConvoy(Bridge destinationBridge, MissionBeginUnitCheckZone missionBeginUnit) throws PWCGException
    {
        GroundTruckConvoyUnit convoy;
        GroundTruckConvoyUnit truckTarget = new GroundTruckConvoyUnit(campaign);
        truckTarget.initialize(missionBeginUnit, location, destinationBridge.getPosition(), destinationBridge.getOrientation(), country);
        truckTarget.createUnitMission();
        convoy = truckTarget;
        return convoy;
    }
}
