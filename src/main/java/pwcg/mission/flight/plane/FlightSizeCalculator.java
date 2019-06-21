package pwcg.mission.flight.plane;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;

public class FlightSizeCalculator 
{
	private ConfigManagerCampaign configManager;
	private FlightTypes flightType;
	
	public FlightSizeCalculator(Campaign campaign, FlightTypes flightType)
	{
		this.flightType = flightType;
		this.configManager = campaign.getCampaignConfigManager();
	}

	public int calcPlanesInFlight() throws PWCGException
    {
    	int minNumPlanes = 2;
    	int randomNumPlanes = 2;
    	boolean adjustMinMax = true;
        
    	if (flightType == FlightTypes.ARTILLERY_SPOT|| flightType == FlightTypes.CONTACT_PATROL ||
   		    flightType == FlightTypes.SPY_EXTRACT || flightType == FlightTypes.LONE_WOLF ||
            flightType == FlightTypes.RECON || flightType == FlightTypes.PORT_RECON)
    	{
    		minNumPlanes = 1;
    		randomNumPlanes = 0;
    		adjustMinMax = false;
    	}
    	else if (flightType == FlightTypes.GROUND_ATTACK || flightType == FlightTypes.ANTI_SHIPPING)
    	{
    		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.GroundAttackMinimumKey);
    		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.GroundAttackAdditionalKey) + 1;
    	}
    	else if (flightType == FlightTypes.BOMB || flightType == FlightTypes.DIVE_BOMB || 
    			 flightType == FlightTypes.LOW_ALT_BOMB || flightType == FlightTypes.STRATEGIC_BOMB)
    	{
    		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.BombingMinimumKey);
    		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.BombingAdditionalKey) + 1;
    	}
    	else if (flightType == FlightTypes.TRANSPORT || flightType == FlightTypes.CARGO_DROP || 
   			 flightType == FlightTypes.LOW_ALT_BOMB || flightType == FlightTypes.STRATEGIC_BOMB)
	   	{
	   		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.TransportMinimumKey);
	   		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.TransportAdditionalKey) + 1;
	   	}
    	else
	   	{
	   		minNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey);
	   		randomNumPlanes = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey) + 1;
	   	}

    	int numPlanesInFlight = minNumPlanes + RandomNumberGenerator.getRandom(randomNumPlanes);
    	if (adjustMinMax == true)
    	{
    		numPlanesInFlight = adjustMinMaxPlanesInFlight(numPlanesInFlight, minNumPlanes, randomNumPlanes);
    	}
    	
        return numPlanesInFlight;
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

    protected int modifyNumPlanes(int numPlanes)
    {
        if (numPlanes % 2 == 1)
        {
            return numPlanes + 1;
        }
        
        return numPlanes;
    }

}
