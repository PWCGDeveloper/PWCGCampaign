package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.flight.balloondefense.BalloonDefenseGroup;
import pwcg.mission.mcu.Coalition;

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
        Coalition playerCoalition  = Coalition.getFriendlyCoalition(campaign.determineCountry());
        MissionBeginUnitCheckZone missionBeginUnitBalloon = new MissionBeginUnitCheckZone();
        missionBeginUnitBalloon.initialize(targetDefinition.getTargetPosition().copy(), 10000, playerCoalition);
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, missionBeginUnitBalloon, targetDefinition);
        BalloonDefenseGroup balloonUnit = new BalloonDefenseGroup(campaign, groundUnitInformation);
        balloonUnit.createUnitMission();
        return balloonUnit;
    }
}
