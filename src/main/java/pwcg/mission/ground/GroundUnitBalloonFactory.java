package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.flight.balloondefense.BalloonDefenseGroup;
import pwcg.mission.mcu.CoalitionFactory;

public class GroundUnitBalloonFactory
{    
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public GroundUnitBalloonFactory (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign  = campaign;
        this.targetDefinition  = targetDefinition;
    }
    
    public BalloonDefenseGroup createBalloonUnit() throws PWCGException
    {
        MissionBeginUnitCheckZone missionBeginUnitBalloon = new MissionBeginUnitCheckZone(targetDefinition.getTargetPosition().copy(), 15000);
        missionBeginUnitBalloon.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalitions(CoalitionFactory.getAllCoalitions());
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, missionBeginUnitBalloon, targetDefinition);
        BalloonDefenseGroup balloonUnit = new BalloonDefenseGroup(campaign, groundUnitInformation);
        balloonUnit.createUnitMission();
        return balloonUnit;
    }
}
