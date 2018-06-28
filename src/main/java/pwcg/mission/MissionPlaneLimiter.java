package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.group.PlaneCounter;

/**
 * MissionPlaneCalculator determines the maximum number of planes that can spawn per side.
 * Creates PlaneCounter MCU groups to prevent an excessive number of planes from spawning.
 *
 * @author Patrick Wilson
 * 
 */
public class MissionPlaneLimiter
{
    private int maxAlliedPlanes = 8;
    private int maxAxisPlanes = 8;
    
    private PlaneCounter alliedPlaneCounter = new PlaneCounter();
    private PlaneCounter axisPlaneCounter = new PlaneCounter();

    public void createPlaneCountersToLimitPlanesSpawned(Mission mission, int planesInMyFlight) throws PWCGException 
    {        
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();
        
        int randomizePlaneCount = configManager.getIntConfigParam(ConfigItemKeys.RandomizePlanesPerSideKey);
        if (randomizePlaneCount == 1)
        {
            getRandomizedPlanes();
        }
        else
        {
            calcMaxAlliedPlanes();
            calcMaxAxisPlanes();
        }
        
        adjustForMyFlight(planesInMyFlight);
        
        setPlaneCounter(mission);
    }

    private void setPlaneCounter(Mission mission) throws PWCGException 
    {
        Coordinate mcuCoordinate = mission.getMissionFlightBuilder().getPlayerFlight().getPosition().copy();
        alliedPlaneCounter.initialize(mcuCoordinate);
        axisPlaneCounter.initialize(mcuCoordinate);
        
        alliedPlaneCounter.setPlaneCounter(maxAlliedPlanes);
        axisPlaneCounter.setPlaneCounter(maxAxisPlanes);

        for (Flight flight : mission.getMissionFlightBuilder().getAlliedAiFlights())
        {
            alliedPlaneCounter.setPlaneCounterForFlight(flight);
        }
        
        for (Flight flight : mission.getMissionFlightBuilder().getAxisAiFlights())
        {
            axisPlaneCounter.setPlaneCounterForFlight(flight);
        }
    }

    private void getRandomizedPlanes() throws PWCGException 
    {
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();
        
        int maxFriendlyPlanes = configManager.getIntConfigParam(ConfigItemKeys.FriendlyPlanesMaxKey);
        int maxEnemyPlanes = configManager.getIntConfigParam(ConfigItemKeys.EnemyPlanesMaxKey);
        
        int totalPlanes = maxFriendlyPlanes + maxEnemyPlanes;
        int oneThirdTotalPlanes = (totalPlanes / 3);
        if ((totalPlanes % 3) != 0)
        {
            oneThirdTotalPlanes = (totalPlanes / 3) + 1;
        }

        int upToOneThirdRandom = RandomNumberGenerator.getRandom(oneThirdTotalPlanes);
        maxAlliedPlanes = oneThirdTotalPlanes + upToOneThirdRandom;
        
        if (campaign.determineCountry().getSide() == Side.ALLIED)
        {
            --maxAlliedPlanes;
        }
        else
        {
            ++maxAlliedPlanes;
        }
        
        maxAxisPlanes = totalPlanes - maxAlliedPlanes;
        
        if (maxAlliedPlanes < 4)
        {
            maxAlliedPlanes = 4;
        }
        
        if (maxAxisPlanes < 4)
        {
            maxAxisPlanes = 4;
        }
        
        if ((maxAlliedPlanes % 2) > 0)
        {
            ++maxAlliedPlanes;
        }
        
        if ((maxAxisPlanes % 2) > 0)
        {
            ++maxAxisPlanes;
        }
    }

    private void adjustForMyFlight(int planesInMyFlight) throws PWCGException
    {
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        
        if (campaign.determineCountry().getSide() == Side.ALLIED)
        {
            maxAlliedPlanes -= planesInMyFlight;
            maxAlliedPlanes = (maxAlliedPlanes / 2);
            if (maxAlliedPlanes < 2)
            {
                maxAlliedPlanes = 2;
            }
        }
        else
        {
            maxAxisPlanes -= planesInMyFlight;
            maxAxisPlanes = (maxAxisPlanes / 2);
            if (maxAxisPlanes < 2)
            {
                maxAxisPlanes = 2;
            }
        }
    }

    private void calcMaxAlliedPlanes() throws PWCGException 
    {
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();
        
        maxAlliedPlanes = configManager.getIntConfigParam(ConfigItemKeys.EnemyPlanesMaxKey);
        if (campaign.determineCountry().getSide() == Side.ALLIED)
        {
            maxAlliedPlanes = configManager.getIntConfigParam(ConfigItemKeys.FriendlyPlanesMaxKey);
        }
    }

    private void calcMaxAxisPlanes() throws PWCGException 
    {
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();

        maxAxisPlanes = configManager.getIntConfigParam(ConfigItemKeys.EnemyPlanesMaxKey);
        if (campaign.determineCountry().getSide() == Side.AXIS)
        {
            maxAxisPlanes = configManager.getIntConfigParam(ConfigItemKeys.FriendlyPlanesMaxKey);
        }
    }

    public PlaneCounter getAlliedPlaneCounter()
    {
        return alliedPlaneCounter;
    }

    public PlaneCounter getAxisPlaneCounter()
    {
        return axisPlaneCounter;
    }
    
}
