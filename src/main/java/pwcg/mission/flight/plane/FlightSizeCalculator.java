package pwcg.mission.flight.plane;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.IFlightInformation;

public class FlightSizeCalculator 
{
	private ConfigManagerCampaign configManager;
	private IFlightInformation flightInformation;

	public FlightSizeCalculator(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
        configManager = flightInformation.getCampaign().getCampaignConfigManager();
    }

    public int calcPlanesInFlight() throws PWCGException
    {
    	int minNumPlanes = 2;
    	int randomNumPlanes = 2;
    	boolean adjustMinMax = true;
    	        
        if (flightInformation.getFlightType().isCategory(FlightTypeCategory.SINGLE))
    	{
    		minNumPlanes = 1;
    		randomNumPlanes = 0;
    		adjustMinMax = false;
    	}
        else if (flightInformation.getFlightType().isCategory(FlightTypeCategory.ATTACK))
    	{
    		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.GroundAttackMinimumKey);
    		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.GroundAttackAdditionalKey);
    	}
    	else if (flightInformation.getFlightType().isCategory(FlightTypeCategory.BOMB))
    	{
    		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.BombingMinimumKey);
    		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.BombingAdditionalKey);
    	}
        else if (flightInformation.getFlightType().isCategory(FlightTypeCategory.STRATEGIC))
        {
            minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.StrategicBombingMinimumKey);
            randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.StrategicBombingAdditionalKey);
        }
        else if (flightInformation.getFlightType().isCategory(FlightTypeCategory.TRANSPORT))
	   	{
	   		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.TransportMinimumKey);
	   		minNumPlanes += 2;
	   		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.TransportAdditionalKey);
	   	}
    	else
    	{
            minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.PatrolMinimumKey);
            randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey);
    	}

    	int numPlanesInFlight = calculateNumberOfPlanesInFlight(minNumPlanes, randomNumPlanes, adjustMinMax);
    	
        return numPlanesInFlight;
    }

    private int calculateNumberOfPlanesInFlight(int minNumPlanes, int randomNumPlanes, boolean adjustMinMax) throws PWCGException
    {
        randomNumPlanes += 1;
    	int numPlanesInFlight = minNumPlanes + RandomNumberGenerator.getRandom(randomNumPlanes);
   	
        if (adjustMinMax == true)
    	{
    		numPlanesInFlight = adjustMinMaxPlanesInFlight(numPlanesInFlight);
            if (flightInformation.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
            {
                numPlanesInFlight = adjustForElementSize(numPlanesInFlight);
            }
    	}
        return numPlanesInFlight;
    }

	private int adjustForElementSize(int originalNumPlanesInFlight) throws PWCGException
    {
	    int maxElements = 2;
	    
        int elementSize = FlightElementSizeCalculator.calculateElementSizeForFighters(flightInformation);
        int excessPlanes = originalNumPlanesInFlight % elementSize;
        int adjustedNumPlanesInFlight = originalNumPlanesInFlight - excessPlanes;
        if (adjustedNumPlanesInFlight < 2)
        {
            adjustedNumPlanesInFlight = elementSize;
        }
        
        if ((adjustedNumPlanesInFlight / elementSize) > maxElements)
        {
            adjustedNumPlanesInFlight = maxElements * elementSize;
        }
        
        return adjustedNumPlanesInFlight;
    }

    private int adjustMinMaxPlanesInFlight(int numPlanesInFlight)
	{
        if (numPlanesInFlight > 8)
        {
            numPlanesInFlight = 8;
        }
        else if (numPlanesInFlight < 2)
        {
        	numPlanesInFlight = 2;
        }
		return numPlanesInFlight;
	}
}
