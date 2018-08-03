package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PositionsManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;

public class ArtillerySpotBatteryFactory
{  
    public static final int MAX_ARTILLERY_RANGE = 15000;

    private Campaign campaign;
    private Coordinate position;
    private Coordinate targetPosition;
    private ICountry country;
    
    public ArtillerySpotBatteryFactory (Campaign campaign, Coordinate location, Coordinate targetPosition, ICountry country)
    {
        this.campaign  = campaign;
        this.position  = location.copy();
        this.targetPosition  = targetPosition.copy();
        this.country  = country;
    }

    public ArtillerySpotArtilleryGroup createFriendlyArtilleryBattery (boolean isPlayerFlight) throws PWCGException 
    {
        Coordinate artilleryPosition = determineArtilleryPosition();    
        MissionBeginUnit missionBeginUnit = createMBU(artilleryPosition);
        String name = createUnitName();

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, missionBeginUnit, country, name, TacticalTarget.TARGET_ARTILLERY, position, targetPosition, null, isPlayerFlight);

        ArtillerySpotArtilleryGroup friendlyArtilleryGroup = new ArtillerySpotArtilleryGroup(campaign, groundUnitInformation);
        friendlyArtilleryGroup.createUnitMission();

        return friendlyArtilleryGroup;
    }

    private String createUnitName()
    {
        String countryName = country.getNationality();
        String name = countryName + " Artillery Group";
        return name;
    }

    private MissionBeginUnit createMBU(Coordinate artilleryPosition) throws PWCGException
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(artilleryPosition);
        return missionBeginUnit;
    }

    private Coordinate determineArtilleryPosition() throws PWCGException
    {
        PositionsManager positionsManager = new PositionsManager(campaign.getDate());
        String airfieldName = campaign.getAirfieldName();
        IAirfield field =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
        Coordinate artilleryPosition = positionsManager.getClosestDefinitePosition(country.getSide(), field.getPosition().copy());
        
        // Make sure that the artillery is in range
        if (MathUtils.calcDist(targetPosition, artilleryPosition) > MAX_ARTILLERY_RANGE)
        {
            double angle = MathUtils.calcAngle(targetPosition, artilleryPosition);
            artilleryPosition = MathUtils.calcNextCoord(targetPosition, angle, MAX_ARTILLERY_RANGE);
        }

        return artilleryPosition;
    }
}
