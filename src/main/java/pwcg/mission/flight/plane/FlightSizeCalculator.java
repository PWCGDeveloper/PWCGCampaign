package pwcg.mission.flight.plane;

import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypeCategory;

public class FlightSizeCalculator 
{
	private ConfigManagerCampaign configManager;
	private FlightInformation flightInformation;

	public FlightSizeCalculator(FlightInformation flightInformation)
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
   	
        numPlanesInFlight = adjustfPlanesInFlightForFlightType(flightInformation, numPlanesInFlight);
        if (adjustMinMax == true)
    	{
    		numPlanesInFlight = adjustMinMaxPlanesInFlight(numPlanesInFlight);
    	}
        return numPlanesInFlight;
    }

    private int adjustfPlanesInFlightForFlightType(FlightInformation flightInformation, int numPlanesInFlight) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            if (flightInformation.getSquadron().determineSquadronPrimaryRoleCategory(flightInformation.getCampaign().getDate()) == PwcgRoleCategory.FIGHTER)
            {
                if (numPlanesInFlight <= 2)
                {
                    return numPlanesInFlight;
                }
                else if (numPlanesInFlight <= 4 && 
                        flightInformation.getSquadron().getCountry().getCountry() == Country.RUSSIA && 
                        flightInformation.getCampaign().getDate().before(DateUtils.getDateYYYYMMDD("19431001")))

                {
                    return 3;
                }
                else if (numPlanesInFlight <= 4)
                {
                    return 4;
                }
            }
        }
        return numPlanesInFlight;
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
