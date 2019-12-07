package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.group.PlaneCounter;

public class SinglePlayerMissionPlaneLimiter
{
    private int maxAlliedPlanes = 8;
    private int maxAxisPlanes = 8;
    
    private PlaneCounter alliedPlaneCounter = new PlaneCounter();
    private PlaneCounter axisPlaneCounter = new PlaneCounter();
    private Mission mission;

    public void createPlaneCountersToLimitPlanesSpawned(Mission mission) throws PWCGException 
    {        
        this.mission = mission;
        initMaxPlanesPerSide();        
        adjustForPlayerFlights();        
        setPlaneCounter(mission);
    }

    private void setPlaneCounter(Mission mission) throws PWCGException 
    {
        Coordinate mcuCoordinate = mission.getMissionFlightBuilder().getReferencePlayerFlight().getPosition();
        alliedPlaneCounter.initialize(mcuCoordinate);
        axisPlaneCounter.initialize(mcuCoordinate);
        
        alliedPlaneCounter.setPlaneCounter(maxAlliedPlanes);
        axisPlaneCounter.setPlaneCounter(maxAxisPlanes);

        for (Flight flight : mission.getMissionFlightBuilder().getAiFlightsForSide(Side.ALLIED))
        {
            alliedPlaneCounter.setPlaneCounterForFlight(flight);
        }
        
        for (Flight flight : mission.getMissionFlightBuilder().getAiFlightsForSide(Side.AXIS))
        {
            axisPlaneCounter.setPlaneCounterForFlight(flight);
        }
    }

    private void initMaxPlanesPerSide() throws PWCGException 
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();
        
        maxAlliedPlanes = configManager.getIntConfigParam(ConfigItemKeys.AlliedPlanesToSpawnMaxKey);
        maxAxisPlanes = configManager.getIntConfigParam(ConfigItemKeys.AxisPlanesToSpawnMaxKey);
    }

    private void adjustForPlayerFlights() throws PWCGException
    {
        for (Flight playerFlight : mission.getMissionFlightBuilder().getPlayerFlights())
        {
            int planesInPlayerFlight = playerFlight.getPlanes().size();
            
            if (playerFlight.getSquadron().determineSide() == Side.ALLIED)
            {
                maxAlliedPlanes -= planesInPlayerFlight;
                if (maxAlliedPlanes < 2)
                {
                    maxAlliedPlanes = 2;
                }
            }
            else
            {
                maxAxisPlanes -= planesInPlayerFlight;
                if (maxAxisPlanes < 2)
                {
                    maxAxisPlanes = 2;
                }
            }
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
