package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.flight.balloondefense.BalloonDefenseGroup;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.mcu.Coalition;

public class GroundUnitBalloonFactory
{    
    private Campaign campaign;
    private Coordinate location;
    private ICountry country;
    
    public GroundUnitBalloonFactory (Campaign campaign, Coordinate location, ICountry country)
    {
        this.campaign  = campaign;
        this.location  = location.copy();
        this.country  = country;
    }

    public GroundUnit createBalloonUnit () throws PWCGException 
    {
        Coalition enemyCoalition = Coalition.getEnemyCoalition(country);
        
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(location, 10000, enemyCoalition);

        BalloonDefenseGroup balloonUnit = new BalloonDefenseGroup(campaign);
        balloonUnit.initialize(missionBeginUnit, location, country);
        balloonUnit.createUnitMission();
        
        return balloonUnit;
    }   
}
