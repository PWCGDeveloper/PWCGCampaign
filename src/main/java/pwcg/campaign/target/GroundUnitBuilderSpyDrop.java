package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.GroundUnitFactory;
import pwcg.mission.ground.GrountUnitTroopConcentrationFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.SpotLightGroup;

public class GroundUnitBuilderSpyDrop
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    private GroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION);

    public GroundUnitBuilderSpyDrop(Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition = targetDefinition;
    }

    public GroundUnitCollection createSpyDropTargets() throws PWCGException 
    {
        GrountUnitTroopConcentrationFactory groundUnitFactory = new GrountUnitTroopConcentrationFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getTargetCountry());
        GroundUnit targetUnit = groundUnitFactory.createTroopConcentration();
        groundUnitCollection.addGroundUnit(GroundUnitType.INFANTRY_UNIT, targetUnit);
        
        addSpotLight();
        
        return groundUnitCollection;
    }

    private void addSpotLight() throws PWCGException 
    {
        GroundUnitFactory groundUnitFactory = new GroundUnitFactory(campaign, targetDefinition.getTargetLocation(), targetDefinition.getTargetCountry());
        SpotLightGroup spotLightGroup = groundUnitFactory.createSpotLightGroup();
        if (spotLightGroup != null)
        {
            groundUnitCollection.addGroundUnit(GroundUnitType.STATIC_UNIT, spotLightGroup);
        }
    }
}
