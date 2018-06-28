package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.GrountUnitTroopConcentrationFactory;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderTroopConcentration
{
    public static GroundUnitCollection createTroopConcentration(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException, PWCGMissionGenerationException
    {
        GrountUnitTroopConcentrationFactory groundUnitFactory = new GrountUnitTroopConcentrationFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getTargetCountry());
        GroundUnit targetUnit = groundUnitFactory.createTroopConcentration();
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.INFANTRY_UNIT, targetUnit);
        return groundUnitCollection;
    }

}
