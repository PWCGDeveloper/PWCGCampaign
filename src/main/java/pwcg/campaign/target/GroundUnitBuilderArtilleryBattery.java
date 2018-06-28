package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.GrountUnitArtilleryBatteryFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderArtilleryBattery
{
    public static GroundUnitCollection createArtilleryBattery(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException, PWCGMissionGenerationException
    {
        GrountUnitArtilleryBatteryFactory groundUnitFactory = new GrountUnitArtilleryBatteryFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getTargetCountry());
        GroundUnit targetUnit = groundUnitFactory.createGroundArtilleryBattery();        
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.ARTILLERY_UNIT, targetUnit);
        return groundUnitCollection;
    }
}
