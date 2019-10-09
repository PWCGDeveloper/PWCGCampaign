package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.unittypes.transport.GroundTruckConvoyUnit;
import pwcg.mission.mcu.Coalition;

public class TruckUnitFactory
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public TruckUnitFactory (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public GroundTruckConvoyUnit createTruckConvoy () throws PWCGException
    {
        MissionBeginUnitCheckZone missionBeginUnit = buildMissionBegin();
        GroundTruckConvoyUnit convoy = buildTruckConvoy(missionBeginUnit);  
        return convoy;
    }

    private MissionBeginUnitCheckZone buildMissionBegin() throws PWCGException
    {
        Coalition enemyCoalition  = Coalition.getCoalitionBySide(targetDefinition.getTargetCountry().getSide().getOppositeSide());
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone(targetDefinition.getTargetPosition(), 15000);
        missionBeginUnit.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalition(enemyCoalition);
        return missionBeginUnit;
    }

    private GroundTruckConvoyUnit buildTruckConvoy(MissionBeginUnitCheckZone missionBeginUnit) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit(missionBeginUnit);
        GroundTruckConvoyUnit truckConvoy = new GroundTruckConvoyUnit(groundUnitInformation);
        truckConvoy.createUnitMission();
        return truckConvoy;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit(MissionBeginUnitCheckZone missionBeginUnit) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, missionBeginUnit, targetDefinition);
        Coordinate destination = getConvoyDestination();
        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

    private Coordinate getConvoyDestination() throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Bridge destinationBridge = groupData.getBridgeFinder().findDestinationBridge(targetDefinition.getTargetPosition(), targetDefinition.getTargetCountry().getSide(), campaign.getDate());
        if (destinationBridge != null)
        {
            return destinationBridge.getPosition();
        }
        else
        {
            return targetDefinition.getTargetPosition();
        }
    }
}
