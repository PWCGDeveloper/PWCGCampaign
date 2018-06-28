package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;
import pwcg.mission.flight.balloondefense.BalloonDefenseGroup;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.SpotLightGroup;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryUnit;
import pwcg.mission.mcu.Coalition;

public class GroundUnitFactory
{    
    private Campaign campaign;
    private Coordinate location;
    private ICountry country;
    
    public GroundUnitFactory (Campaign campaign, Coordinate location, ICountry country)
    {
        this.campaign  = campaign;
        this.location  = location.copy();
        this.country  = country;
    }

    public ArtillerySpotArtilleryGroup createFriendlyArtilleryBattery () throws PWCGException 
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(location);
               
        ArtillerySpotArtilleryGroup friendlyArtilleryGroup = new ArtillerySpotArtilleryGroup(campaign);
        friendlyArtilleryGroup.initialize(missionBeginUnit, location, country);
        friendlyArtilleryGroup.createUnitMission();

        return friendlyArtilleryGroup;
    }

    public GroundUnit createBalloonUnit () throws PWCGException 
    {
        Coalition enemyCoalition = Coalition.getEnemyCoalition(country);
        
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(location, 7000, enemyCoalition);

        BalloonDefenseGroup balloonUnit = new BalloonDefenseGroup(campaign);
        balloonUnit.initialize(missionBeginUnit, location, country);
        balloonUnit.createUnitMission();
        
        return balloonUnit;
    }   

    public GroundUnit createArtilleryUnit (
                    MissionBeginUnit missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException
    {
        GroundArtilleryUnit artilleryUnit = new GroundArtilleryUnit(campaign);
        
        artilleryUnit.setMinRequested(1);
        artilleryUnit.setMinRequested(3);
        
        artilleryUnit.initialize(missionBeginUnit, startCoords, destinationCoords, country);
        artilleryUnit.createUnitMission();

        return artilleryUnit;
    }


    public SpotLightGroup createSpotLightGroup() throws PWCGException 
    {
        Coalition playerCoalition = Coalition.getFriendlyCoalition(campaign.determineCountry());

        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(location, 12000, playerCoalition);

        SpotLightGroup spotLightGroup = new SpotLightGroup();
        spotLightGroup.initialize(missionBeginUnit, country, location, 8);
        spotLightGroup.createUnitMission();
        
        return spotLightGroup;
    }
}
