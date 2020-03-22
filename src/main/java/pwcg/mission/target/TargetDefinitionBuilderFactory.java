package pwcg.mission.target;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;

public class TargetDefinitionBuilderFactory
{
    public static ITargetDefinitionBuilder createFlightTargetDefinitionBuilder (IFlightInformation flightInformation) throws PWCGException
    {
    	FlightTypes flightType = flightInformation.getFlightType();
        if(flightType.isCategory(FlightTypeCategory.STRATEGIC))
    	{
    		return new TargetDefinitionBuilderStrategic(flightInformation);
    	}
    	else if(flightType.isCategory(FlightTypeCategory.ATTACK))
    	{
    	    return new TargetDefinitionBuilderAirToGround(flightInformation);
    	}
        else if(flightType.isCategory(FlightTypeCategory.FIGHTER) || flightType.isCategory(FlightTypeCategory.OTHER))
        {
            return new TargetDefinitionBuilderAirToAir(flightInformation);
        }

    	throw new PWCGException("No target definition builder for flight type " + flightInformation.getFlightType());
    }
}
