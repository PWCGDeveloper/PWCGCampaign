package pwcg.mission.ground.builder;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.factory.AAAUnitBuilder;
import pwcg.mission.ground.factory.AssaultBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetDefinition;

public class TargetBuilderGenerator
{
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;    
    private IGroundUnitCollection groundUnitCollection;

    public TargetBuilderGenerator(FlightInformation flightInformation) throws PWCGException 
    { 
        this.flightInformation = flightInformation;
        this.targetDefinition = flightInformation.getTargetDefinition();
    }

	public void buildTarget() throws PWCGException 
	{
	    FlightTypes flightType = flightInformation.getFlightType();
        if (flightType == FlightTypes.CONTACT_PATROL)
        {
            createAssaultGroundUnits();        
        }
        else if (flightType == FlightTypes.CARGO_DROP)
        {
            createAssaultGroundUnits();        
        }
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            createAssaultGroundUnits();        
        }   
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            createAAAGroundUnits();        
        }   
        else
        {
            createTargetGroundUnits();
        }
	}

    private void createTargetGroundUnits() throws PWCGException
    {
        GroundUnitAttackBuilder groundUnitBuilderAttack = new GroundUnitAttackBuilder(flightInformation.getCampaign(), flightInformation.getMission(), targetDefinition);
        groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(targetDefinition.getTargetCountry().getSide());
        if (targetCoordinates != null)
        {
            targetDefinition.setTargetPosition(targetCoordinates);
        }
    }

    private void createAssaultGroundUnits() throws PWCGException
    {
        groundUnitCollection = AssaultBuilder.generateAssault(flightInformation.getMission(), targetDefinition);
    }

    private void createAAAGroundUnits() throws PWCGException
    {
        AAAUnitBuilder aaaUnitBuilder = new AAAUnitBuilder(flightInformation.getCampaign(), targetDefinition.getTargetCountry(), targetDefinition.getTargetPosition());
        groundUnitCollection = aaaUnitBuilder.createAAAArtilleryBatteryFromMission(flightInformation.getMission(), GroundUnitSize.GROUND_UNIT_SIZE_HIGH);
    }
    
    public IGroundUnitCollection getGroundUnits()
    {
        return groundUnitCollection;
    }
 }
