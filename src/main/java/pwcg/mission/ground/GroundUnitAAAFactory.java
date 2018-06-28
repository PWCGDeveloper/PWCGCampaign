package pwcg.mission.ground;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.artillery.GroundAAABattery;
import pwcg.mission.mcu.Coalition;

public class GroundUnitAAAFactory
{    
    private ICountry country;
    private Coordinate location;
    
    public GroundUnitAAAFactory (ICountry country, Coordinate location)
    {
        this.country  = country;
        this.location  = location.copy();
    }

    public GroundAAABattery createAAAArtilleryBattery (int numAAA) throws PWCGException
    {
        return createAAABattery(numAAA, false);
    }

    public GroundAAABattery createAAAMGBattery (int numAAA) throws PWCGException
    {
        return createAAABattery(numAAA, true);
    }

    private GroundAAABattery createAAABattery (int numAAA, boolean isMG) throws PWCGException
    {
        Coalition enemyCoalition =  Coalition.getEnemyCoalition(country);

        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(location, 5000, enemyCoalition);

        GroundAAABattery aaaBattery = new GroundAAABattery();
        if (numAAA > 0)
        {
            aaaBattery.setMinRequested(numAAA);
            aaaBattery.setMaxRequested(numAAA);
        }
        
        aaaBattery.initialize(missionBeginUnit, location, country, isMG);
        aaaBattery.createUnitMission();

        return aaaBattery;
    }
}
