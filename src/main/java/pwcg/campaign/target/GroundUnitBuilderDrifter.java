package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.factory.DrifterUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;

public class GroundUnitBuilderDrifter
{

    public static GroundUnitCollection createDrifters(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException
    {
        int minUnits = GroundUnitSpawning.NUM_UNITS_BY_CONFIG;
        int maxUnits = GroundUnitSpawning.NUM_UNITS_BY_CONFIG;
        if (targetDefinition.isPlayerTarget())
        {
            minUnits = 1;
            maxUnits = 2;
        }
        
        DrifterUnitFactory groundUnitFactory = new DrifterUnitFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getTargetOrientation(), targetDefinition.getTargetCountry());
        GroundUnit targetUnit = groundUnitFactory.createDrifterUnit(minUnits, maxUnits);
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.TRANSPORT_UNIT, targetUnit);
        return groundUnitCollection;
    }
}
