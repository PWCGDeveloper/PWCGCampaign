package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitCollection;

public class GroundUnitBuilderAttack
{
    private Campaign campaign;
    private Mission mission;
    private TargetDefinition targetDefinition;

    public GroundUnitBuilderAttack(Campaign campaign, Mission mission, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.mission = mission;
        this.targetDefinition = targetDefinition;
    }
    
    public GroundUnitCollection createTargetGroundUnits() throws PWCGException 
    {
        if (targetDefinition.getTargetType() == TacticalTarget.TARGET_TRAIN)
        {
            return GroundUnitBuilderTrain.createTrainTarget(campaign, mission, targetDefinition);
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_TRANSPORT)
        {
            return GroundUnitBuilderTruckConvoy.createTruckConvoy(campaign, mission, targetDefinition);
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_AIRFIELD)
        {
            return GroundUnitBuilderAirfield.createAirfieldUnits(campaign, targetDefinition);
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_DRIFTER)
        {
            return GroundUnitBuilderDrifter.createDrifters(campaign, targetDefinition);
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_SHIPPING)
        {
            return GroundUnitBuilderShipping.createShipping(campaign, targetDefinition);
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_ARTILLERY)
        {
            return GroundUnitBuilderArtilleryBattery.createArtilleryBattery(campaign, targetDefinition);
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_TROOP_CONCENTRATION)
        {
            return GroundUnitBuilderTroopConcentration.createTroopConcentration(campaign, targetDefinition);
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_ASSAULT || targetDefinition.getTargetType() == TacticalTarget.TARGET_DEFENSE)
        {
            return GroundUnitBuilderAssault.createAssault(campaign, mission, targetDefinition);
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_AI)
        {
            return GroundUnitBuilderAI.createAiTarget(campaign, targetDefinition);
        }
        else
        {
            return GroundUnitBuilderTroopConcentration.createTroopConcentration(campaign, targetDefinition);
        }        
    }
}
