package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.AirfieldUnitBuilder;
import pwcg.mission.ground.builder.ArtilleryUnitBuilder;
import pwcg.mission.ground.builder.AssaultBuilder;
import pwcg.mission.ground.builder.DrifterUnitBuilder;
import pwcg.mission.ground.builder.ShipTypeChooser;
import pwcg.mission.ground.builder.ShippingUnitBuilder;
import pwcg.mission.ground.builder.TrainUnitBuilder;
import pwcg.mission.ground.builder.TruckConvoyBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class GroundUnitAttackFactory
{
    private Campaign campaign;
    private Mission mission;
    private TargetDefinition targetDefinition;

    public GroundUnitAttackFactory(Campaign campaign, Mission mission, TargetDefinition targetDefinition)
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
            VehicleClass shipType = ShipTypeChooser.chooseShipType(targetDefinition.getTargetCountry().getSide());
            ShippingUnitBuilder shippingUnitBuilder = new ShippingUnitBuilder(campaign, targetDefinition);
            return shippingUnitBuilder.createShippingUnit(shipType);
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_ARTILLERY)
        {
            ArtilleryUnitBuilder artilleryUnitBuilder = new ArtilleryUnitBuilder(campaign, targetDefinition);            
            return artilleryUnitBuilder.createArtilleryBattery();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_TROOP_CONCENTRATION)
        {
            MinimalAiTargetFactory smallTargetBuilder = new MinimalAiTargetFactory(campaign, targetDefinition);
            return smallTargetBuilder.createTroopConcentration();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_ASSAULT || targetDefinition.getTargetType() == TacticalTarget.TARGET_DEFENSE)
        {
            IGroundUnitCollection battleUnitCollection = AssaultBuilder.generateAssault(mission, targetDefinition);
            return battleUnitCollection;
        }
        else
        {
            MinimalAiTargetFactory smallTargetBuilder = new MinimalAiTargetFactory(campaign, targetDefinition);
            return smallTargetBuilder.createTroopConcentration();
        }        
    }
}
