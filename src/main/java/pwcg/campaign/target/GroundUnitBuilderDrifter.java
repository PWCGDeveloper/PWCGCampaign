package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.factory.DrifterUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderDrifter
{

    public static GroundUnitCollection createDrifters(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException
    {
        DrifterUnitFactory groundUnitFactory = new DrifterUnitFactory(campaign, targetDefinition);
        GroundUnit targetUnit = groundUnitFactory.createDrifterUnit();
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.TRANSPORT_UNIT, targetUnit);
        return groundUnitCollection;
    }
}
