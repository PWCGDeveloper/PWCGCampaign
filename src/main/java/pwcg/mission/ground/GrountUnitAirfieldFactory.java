package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.staticunits.AirfieldStaticGroup;

public class GrountUnitAirfieldFactory
{
    private Campaign campaign;
    private Coordinate location;
    private Orientation orientation;
    private ICountry country;

    public GrountUnitAirfieldFactory (Campaign campaign, Coordinate location, Orientation orientation, ICountry country)
    {
        this.campaign  = campaign;
        this.location  = location.copy();
        this.orientation  = orientation.copy();
        this.country  = country;
    }

    public GroundUnit createAirfieldUnit () throws PWCGException
    {
        AirfieldStaticGroup airfieldGroup = new AirfieldStaticGroup(campaign, location, orientation);
        airfieldGroup.setFiring(false);

        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(location);
        
        airfieldGroup.initialize(missionBeginUnit, location, "Airfield Target", country);
        airfieldGroup.createUnitMission();
        
        return airfieldGroup;
    }   
}
