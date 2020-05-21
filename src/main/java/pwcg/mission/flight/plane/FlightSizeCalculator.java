package pwcg.mission.flight.plane;

import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
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
    	
    	FlightTypes flightType = flightInformation.getFlightType();
        
    	if (flightType == FlightTypes.ARTILLERY_SPOT|| flightType == FlightTypes.CONTACT_PATROL ||
   		    flightType == FlightTypes.SPY_EXTRACT || flightType == FlightTypes.LONE_WOLF ||
            flightType == FlightTypes.RECON)
    	{
    		minNumPlanes = 1;
    		randomNumPlanes = 0;
    		adjustMinMax = false;
    	}
    	else if (flightType == FlightTypes.GROUND_ATTACK)
    	{
    		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.GroundAttackMinimumKey);
    		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.GroundAttackAdditionalKey);
    	}
    	else if (flightType == FlightTypes.BOMB || flightType == FlightTypes.DIVE_BOMB || 
    			 flightType == FlightTypes.LOW_ALT_BOMB)
    	{
    		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.BombingMinimumKey);
    		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.BombingAdditionalKey);
    	}
        else if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.StrategicBombingMinimumKey);
            randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.StrategicBombingAdditionalKey);
        }
    	else if (flightType == FlightTypes.TRANSPORT || flightType == FlightTypes.CARGO_DROP || 
   			 flightType == FlightTypes.LOW_ALT_BOMB || flightType == FlightTypes.STRATEGIC_BOMB)
	   	{
	   		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.TransportMinimumKey);
	   		minNumPlanes += 2;
	   		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.TransportAdditionalKey);
	   	}
    	else
	   	{
	   		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey);
	   		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey);
	   	}

    	randomNumPlanes += 1;
    	int numPlanesInFlight = minNumPlanes + RandomNumberGenerator.getRandom(randomNumPlanes);
        
        if (shouldBeEven())
        {
            numPlanesInFlight = evenNumberOfPlanes(numPlanesInFlight);
        }

        if (adjustMinMax == true)
    	{
    		numPlanesInFlight = adjustMinMaxPlanesInFlight(numPlanesInFlight, minNumPlanes, randomNumPlanes);
    	}
    	
        return numPlanesInFlight;
    }

	private boolean shouldBeEven() throws PWCGException
    {
        Squadron squadron = flightInformation.getSquadron();
        if (squadron.getCountry().getCountry() == Country.RUSSIA)
        {
            return false;
        }
        
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return false;
        }
        
        if ((squadron.getCountry().getCountry() != Country.GERMANY) && (flightInformation.getCampaign().getDate().before(DateUtils.getDateYYYYMMDD("19420301"))))
        {
            return false;
        }
        
        if (!flightInformation.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
        {
            return false;
        }

        return true;
    }

    private int adjustMinMaxPlanesInFlight(int numPlanesInFlight, int minNumPlanes, int randomNumPlanes)
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

	private int evenNumberOfPlanes(int numPlanes)
    {
        if (numPlanes % 2 == 1)
        {
            return numPlanes + 1;
        }
        
        return numPlanes;
    }

}
