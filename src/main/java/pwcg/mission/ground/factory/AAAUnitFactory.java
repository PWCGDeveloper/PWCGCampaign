package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.artillery.GroundAAABattery;
import pwcg.mission.mcu.Coalition;

public class AAAUnitFactory
{    
    private Campaign campaign;
    private ICountry country;
    private Coordinate location;
    
    public AAAUnitFactory (Campaign campaign, ICountry country, Coordinate location)
    {
        this.campaign  = campaign;
        this.country  = country;
        this.location  = location.copy();
    }

    public GroundAAABattery createAAAArtilleryBattery (int minUnits, int maxUnits) throws PWCGException
    {
        return createAAABattery(minUnits, maxUnits, false);
    }

    public GroundAAABattery createAAAMGBattery (int minUnits, int maxUnits) throws PWCGException
    {
        return createAAABattery(minUnits, maxUnits, true);
    }

    private GroundAAABattery createAAABattery (int minUnits, int maxUnits, boolean isMG) throws PWCGException
    {
        Coalition enemyCoalition =  Coalition.getEnemyCoalition(country);

        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(location, 5000, enemyCoalition);

        GroundAAABattery aaaBattery = new GroundAAABattery(campaign);
        aaaBattery.setMinMaxRequested(minUnits, maxUnits);

        aaaBattery.initialize(missionBeginUnit, location, country, isMG);
        aaaBattery.createUnitMission();

        return aaaBattery;
    }
}
