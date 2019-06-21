package pwcg.campaign.target;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;

public class TargetDefinitionBuilderFactory
{
    public static ITargetDefinitionBuilder createFlightTargetDefinitionBuilder (FlightInformation flightInformation) throws PWCGException
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
