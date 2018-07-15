package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.infantry.DrifterUnit;

public class DrifterUnitFactory
{
    private Campaign campaign;
    private Coordinate location;
    private Orientation orientation;
    private ICountry country;

    public DrifterUnitFactory (Campaign campaign, Coordinate location, Orientation orientation, ICountry country)
    {
        this.campaign  = campaign;
        this.location  = location.copy();
        this.orientation  = orientation.copy();
        this.country  = country;
    }

    public GroundUnit createDrifterUnit (int minUnits, int maxUnits) throws PWCGException 
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(location);

        String countryName = country.getNationality();
        String name = countryName + " Drifter";
        
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                missionBeginUnit, country, name, TacticalTarget.TARGET_DRIFTER, location, location, orientation);

        DrifterUnit drifterUnit = new DrifterUnit(campaign, groundUnitInformation);
        drifterUnit.setMinMaxRequested(minUnits, maxUnits);
        drifterUnit.createUnitMission();

        return drifterUnit;
    }
}
