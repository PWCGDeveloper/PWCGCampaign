package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;
import pwcg.mission.mcu.Coalition;

public class TrainUnitFactory
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public TrainUnitFactory (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public GroundTrainUnit createTrainUnit () throws PWCGException
    {
        MissionBeginUnitCheckZone missionBeginUnit = createMissionBegin();        
        GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit(missionBeginUnit);
        
        GroundTrainUnit trainTarget = new GroundTrainUnit(groundUnitInformation);
        trainTarget.createUnitMission();
        return trainTarget;
    }

    private MissionBeginUnitCheckZone createMissionBegin() throws PWCGException
    {
        Coalition enemyCoalition  = Coalition.getCoalitionBySide(targetDefinition.getTargetCountry().getSide().getOppositeSide());
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone(targetDefinition.getTargetPosition(), 20000);
        missionBeginUnit.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalition(enemyCoalition);
        return missionBeginUnit;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit(MissionBeginUnitCheckZone missionBeginUnit) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, missionBeginUnit, targetDefinition);
        Coordinate destination = getTrainDestination();
        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

    private Coordinate getTrainDestination() throws PWCGException
    {
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Block destinationStation = groupData.getRailroadStationFinder().getDestinationTrainPosition(targetDefinition.getTargetPosition(), targetDefinition.getTargetCountry(), campaign.getDate());
        if (destinationStation != null)
        {
            return destinationStation.getPosition();
        }
        else
        {
            return targetDefinition.getTargetPosition();
        }
    }

}
