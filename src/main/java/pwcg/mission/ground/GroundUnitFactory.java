package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;
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

    public ArtillerySpotArtilleryGroup createFriendlyArtilleryBattery (Coordinate targetPosition) throws PWCGException 
    {
        ArtillerySpotBatteryFactory artillerySpotBatteryFactory = new ArtillerySpotBatteryFactory(campaign, location, targetPosition, country);
        ArtillerySpotArtilleryGroup friendlyArtilleryGroup =artillerySpotBatteryFactory.createFriendlyArtilleryBattery();
        friendlyArtilleryGroup.createUnitMission();
        return friendlyArtilleryGroup;
    }

    public GroundUnit createArtilleryUnit (
                    MissionBeginUnit missionBeginUnit, 
                    ICountry country, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords) throws PWCGException
    {
        String nationality = country.getNationality();
        String name = nationality + " Artillery";

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_ARTILLERY, startCoords, destinationCoords);

        GroundArtilleryUnit artilleryUnit = new GroundArtilleryUnit(campaign, groundUnitInformation);
        artilleryUnit.setMinMaxRequested(1, 3);
        artilleryUnit.createUnitMission();

        return artilleryUnit;
    }

    public SpotLightGroup createSpotLightGroup() throws PWCGException 
    {
        Coalition playerCoalition = Coalition.getFriendlyCoalition(campaign.determineCountry());
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(location, 12000, playerCoalition);

        String nationality = country.getNationality();
        String name = nationality + " Spotlight Battery";

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_NONE, location, location);

        SpotLightGroup spotLightGroup = new SpotLightGroup(groundUnitInformation, 8);
        spotLightGroup.createUnitMission();
        
        return spotLightGroup;
    }
}
