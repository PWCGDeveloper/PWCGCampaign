package pwcg.mission.ground.factory;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.builder.AssaultBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetDefinition;

public class TargetFactory
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;    
    private IGroundUnitCollection groundUnitCollection;

    public TargetFactory(IFlightInformation flightInformation, TargetDefinition targetDefinition) throws PWCGException 
    { 
        this.flightInformation = flightInformation;
        this.targetDefinition = targetDefinition;
    }

	public void buildTarget() throws PWCGException 
	{
	    FlightTypes flightType = flightInformation.getFlightType();
	    if (!flightInformation.isPlayerFlight())
	    {
	        createTinyAAAGroundUnits();
	    }
	    else if (flightType == FlightTypes.CONTACT_PATROL)
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
        GroundUnitAttackFactory groundUnitBuilderAttack = new GroundUnitAttackFactory(flightInformation.getCampaign(), flightInformation.getMission(), targetDefinition);
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

    private void createTinyAAAGroundUnits() throws PWCGException
    {
        AAAUnitBuilder aaaUnitBuilder = new AAAUnitBuilder(flightInformation.getCampaign(), targetDefinition.getTargetCountry(), targetDefinition.getTargetPosition());
        groundUnitCollection = aaaUnitBuilder.createAAAArtilleryBatteryFromMission(flightInformation.getMission(), GroundUnitSize.GROUND_UNIT_SIZE_TINY);
    }

    public IGroundUnitCollection getGroundUnits()
    {
        return groundUnitCollection;
    }
 }
