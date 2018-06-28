package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitBalloonFactory;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.unittypes.GroundUnit;

public class GroundUnitBuilderBalloonDefense
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    private GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.BALLOON_GROUND_UNIT_COLLECTION);

    public GroundUnitBuilderBalloonDefense(Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition = targetDefinition;
    }

    public GroundUnitCollection createBalloonDefenseUnits() throws PWCGException 
    {
        GroundUnitBalloonFactory groundUnitFactory =  new GroundUnitBalloonFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getAttackingCountry());
        GroundUnit balloonUnit = groundUnitFactory.createBalloonUnit();
        groundUnitCollection.addGroundUnit(GroundUnitType.BALLOON_UNIT, balloonUnit);        
        return groundUnitCollection;
    }
}
