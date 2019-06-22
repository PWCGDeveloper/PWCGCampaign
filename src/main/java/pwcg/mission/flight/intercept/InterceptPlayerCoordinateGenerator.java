package pwcg.mission.flight.intercept;

import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.TargetDefinitionBuilderAirToGround;
import pwcg.campaign.target.TargetDefinitionBuilderStrategic;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;

public class InterceptPlayerCoordinateGenerator
{
    private FlightInformation flightInformation;

    public InterceptPlayerCoordinateGenerator(FlightInformation flightInformation)
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
            return getAnyInterceptTarget();
        }        
    }
    
    private TargetDefinition getAnyInterceptTarget() throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 20)
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
