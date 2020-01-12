package pwcg.mission.flight.intercept;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToGround;
import pwcg.mission.target.TargetDefinitionBuilderStrategic;

public class InterceptPlayerCoordinateGenerator
{
    private IFlightInformation flightInformation;

    public InterceptPlayerCoordinateGenerator(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }
    
    public TargetDefinition createTargetCoordinates() throws PWCGException
    {
        if (flightInformation.getFlightType() == FlightTypes.HOME_DEFENSE)
        {
            return getStrategicTarget();
        }
        else
        {
            return getTacticaTarget();
        }        
    }
    
    private TargetDefinition getTacticaTarget() throws PWCGException
    {
        TargetDefinitionBuilderAirToGround targetDefinitionBuilder = new TargetDefinitionBuilderAirToGround(flightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        return targetDefinition;
    }

    private TargetDefinition getStrategicTarget() throws PWCGException 
    {
        TargetDefinitionBuilderStrategic targetDefinitionBuilder = new TargetDefinitionBuilderStrategic(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
