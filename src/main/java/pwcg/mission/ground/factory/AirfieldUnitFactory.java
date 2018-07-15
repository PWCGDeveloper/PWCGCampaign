package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.staticunits.AirfieldStaticGroup;

public class AirfieldUnitFactory
{
    private Campaign campaign;
    private Coordinate position;
    private Orientation orientation;
    private ICountry country;

    public AirfieldUnitFactory (Campaign campaign, Coordinate position, Orientation orientation, ICountry country)
    {
        this.campaign  = campaign;
        this.position  = position.copy();
        this.orientation  = orientation.copy();
        this.country  = country;
    }

    public GroundUnit createAirfieldUnit () throws PWCGException
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(position);

        AirfieldManager airfieldManager = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager();
        IAirfield airfield = airfieldManager.getAirfieldFinder().findClosestAirfield(position);

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, airfield.getName(), TacticalTarget.TARGET_AIRFIELD, airfield.getPosition(), airfield.getPosition(), orientation);

        AirfieldStaticGroup airfieldGroup = new AirfieldStaticGroup(campaign, groundUnitInformation);
        airfieldGroup.createUnitMission();
        
        return airfieldGroup;
    }   
}
