package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.ground.GroundUnitAAAFactory;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderAI
{
    public static GroundUnitCollection createAiTarget(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException, PWCGMissionGenerationException
    {
        GroundUnitAAAFactory groundUnitFactory =  new GroundUnitAAAFactory(targetDefinition.getTargetCountry(), targetDefinition.getTargetLocation());
        GroundUnit targetUnit = groundUnitFactory.createAAAMGBattery(1);
        GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION);
        groundUnitCollection.addGroundUnit(GroundUnitType.INFANTRY_UNIT, targetUnit);
        return groundUnitCollection;
    }
}
