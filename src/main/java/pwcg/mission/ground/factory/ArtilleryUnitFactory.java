package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryUnit;

public class ArtilleryUnitFactory
{
    private Campaign campaign;
    private Coordinate location;
    private ICountry country;

    public ArtilleryUnitFactory (Campaign campaign, Coordinate location, ICountry country)
    {
        this.campaign = campaign;
        this.location = location.copy();
        this.country = country;
    }


    public GroundUnit createGroundArtilleryBattery () throws PWCGException
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(location);
        
        GroundArtilleryUnit artilleryUnit = new GroundArtilleryUnit(campaign);
        artilleryUnit.initialize(missionBeginUnit, location, location, country);
        artilleryUnit.createUnitMission();

        return artilleryUnit;
    }
}
