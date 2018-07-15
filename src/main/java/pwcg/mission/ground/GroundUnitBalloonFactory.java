package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.flight.balloondefense.BalloonDefenseGroup;
import pwcg.mission.mcu.Coalition;

public class GroundUnitBalloonFactory
{    
    private Campaign campaign;
    private Coordinate position;
    private ICountry country;
    
    public GroundUnitBalloonFactory (Campaign campaign, Coordinate position, ICountry country)
    {
        this.campaign  = campaign;
        this.position  = position.copy();
        this.country  = country;
    }
    
    public BalloonDefenseGroup createBalloonUnit() throws PWCGException
    {
        Coalition playerCoalition  = Coalition.getFriendlyCoalition(campaign.determineCountry());
        MissionBeginUnitCheckZone missionBeginUnitBalloon = new MissionBeginUnitCheckZone();
        missionBeginUnitBalloon.initialize(position.copy(), 10000, playerCoalition);

        String countryName = country.getNationality();
        String name = countryName + " Balloon";
       
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnitBalloon, country, name, TacticalTarget.TARGET_BALLOON, position, position);
        
        BalloonDefenseGroup balloonUnit = new BalloonDefenseGroup(campaign, groundUnitInformation);
        balloonUnit.createUnitMission();
        return balloonUnit;
    }
}
