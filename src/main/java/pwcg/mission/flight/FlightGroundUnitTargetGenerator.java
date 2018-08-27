package pwcg.mission.flight;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.GeneralTargetLocationGenerator;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetBuilder;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.TargetDefinitionBuilder;
import pwcg.campaign.target.TargetTypeAttackGenerator;
import pwcg.campaign.target.TargetTypeAvailabilityInputGenerator;
import pwcg.campaign.target.TargetTypeAvailabilityInputs;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitCollection;

public class FlightGroundUnitTargetGenerator
{
    protected Mission mission;
    protected Campaign campaign;
    protected Squadron squadron;
    protected FlightTypes flightType;
    protected boolean isPlayerFlight;

    public FlightGroundUnitTargetGenerator(Mission mission, Campaign campaign, Squadron squadron, FlightTypes flightType, boolean isPlayerFlight)
    {
        this.mission = mission;
        this.campaign = campaign;
        this.squadron = squadron;
        this.flightType = flightType;
        this.isPlayerFlight = isPlayerFlight;
    }

    public GroundUnitCollection createGroundUnitsForFlight () throws PWCGException 
    {
        Coordinate targetGeneralLocation = GeneralTargetLocationGenerator.createTargetGeneralLocation(campaign, mission, squadron);
        TargetTypeAvailabilityInputs targetTypeAvailabilityInputs = createTargetingInputs(targetGeneralLocation);
        
        TacticalTarget targetType = createTargetType(targetTypeAvailabilityInputs);        
        TargetDefinition targetDefinition = createTargetDefinition(targetGeneralLocation, targetType);
        
        GroundUnitCollection groundUnitCollection = buildTarget(targetDefinition);
        return groundUnitCollection;
    }

    public GroundUnitCollection createGroundUnitsForFlightWithOverride (TacticalTarget targetType) throws PWCGException 
    {
        Coordinate targetGeneralLocation = GeneralTargetLocationGenerator.createTargetGeneralLocation(campaign, mission, squadron);
        TargetDefinition targetDefinition = createTargetDefinition(targetGeneralLocation, targetType);
        GroundUnitCollection groundUnitCollection = buildTarget(targetDefinition);
        return groundUnitCollection;
    }

    private GroundUnitCollection buildTarget(TargetDefinition targetDefinition) throws PWCGException
    {
        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(flightType);
        return targetBuilder.getGroundUnits();
    }

    private TargetTypeAvailabilityInputs createTargetingInputs(Coordinate targetGeneralLocation) throws PWCGException
    {
        TargetTypeAvailabilityInputGenerator targetTypeAvailabilityInputGenerator = new TargetTypeAvailabilityInputGenerator();
        ICountry enemyCountry = squadron.determineEnemyCountry(campaign, campaign.getDate());
        TargetTypeAvailabilityInputs targetTypeAvailabilityInputs = targetTypeAvailabilityInputGenerator.createTargetAvailabilityInputs(campaign, flightType, enemyCountry.getSide(), targetGeneralLocation);
        return targetTypeAvailabilityInputs;
    }

    private TacticalTarget createTargetType(TargetTypeAvailabilityInputs targetTypeAvailabilityInputs) throws PWCGException
    {
        TargetTypeAttackGenerator targetTypeGenerator = new TargetTypeAttackGenerator(targetTypeAvailabilityInputs);
        TacticalTarget targetType = targetTypeGenerator.createTargetType();
        return targetType;
    }

    private TargetDefinition createTargetDefinition(Coordinate targetGeneralLocation, TacticalTarget targetType) throws PWCGException
    {
        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder();
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionForTacticalFlight(campaign, squadron, flightType, targetType, targetGeneralLocation);
        return targetDefinition;
    }
}
