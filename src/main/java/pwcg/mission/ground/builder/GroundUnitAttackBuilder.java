package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.factory.AirfieldUnitBuilder;
import pwcg.mission.ground.factory.ArtilleryUnitBuilder;
import pwcg.mission.ground.factory.AssaultBuilder;
import pwcg.mission.ground.factory.DrifterUnitBuilder;
import pwcg.mission.ground.factory.MinimalAiTargetBuilder;
import pwcg.mission.ground.factory.ShippingUnitBuilder;
import pwcg.mission.ground.factory.TrainUnitBuilder;
import pwcg.mission.ground.factory.TruckConvoyBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class GroundUnitAttackBuilder
{
    private Campaign campaign;
    private Mission mission;
    private TargetDefinition targetDefinition;

    public GroundUnitAttackBuilder(Campaign campaign, Mission mission, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.mission = mission;
        this.targetDefinition = targetDefinition;
    }
    
    public IGroundUnitCollection createTargetGroundUnits() throws PWCGException 
    {
        if (targetDefinition.getTargetType() == TacticalTarget.TARGET_TRAIN)
        {
            TrainUnitBuilder trainUnitBuilder = new TrainUnitBuilder(mission, targetDefinition);
            return trainUnitBuilder.createTrainUnit();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_TRANSPORT)
        {
            TruckConvoyBuilder truckConvoyBuilder = new TruckConvoyBuilder(mission, targetDefinition);
            return truckConvoyBuilder.createTruckConvoy();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_AIRFIELD)
        {
            AirfieldUnitBuilder airfieldUnitBuilder = new AirfieldUnitBuilder(campaign, targetDefinition);
            return airfieldUnitBuilder.createAirfieldUnit();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_DRIFTER)
        {
            DrifterUnitBuilder drifterUnitBuilder = new DrifterUnitBuilder(campaign, targetDefinition);
            return drifterUnitBuilder.createDrifterUnit();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_SHIPPING)
        {
            ShippingUnitBuilder shippingUnitBuilder = new ShippingUnitBuilder(campaign, targetDefinition);
            return shippingUnitBuilder.createShippingUnit();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_ARTILLERY)
        {
            ArtilleryUnitBuilder artilleryUnitBuilder = new ArtilleryUnitBuilder(campaign, targetDefinition);            
            return artilleryUnitBuilder.createArtilleryBattery();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_TROOP_CONCENTRATION)
        {
            MinimalAiTargetBuilder smallTargetBuilder = new MinimalAiTargetBuilder(campaign, targetDefinition);
            return smallTargetBuilder.createTroopConcentration();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_ASSAULT || targetDefinition.getTargetType() == TacticalTarget.TARGET_DEFENSE)
        {
            IGroundUnitCollection battleUnitCollection = AssaultBuilder.generateAssault(mission, targetDefinition);
            return battleUnitCollection;
        }
        else
        {
            MinimalAiTargetBuilder smallTargetBuilder = new MinimalAiTargetBuilder(campaign, targetDefinition);
            return smallTargetBuilder.createTroopConcentration();
        }        
    }
}
