package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;

public class MissionFlightKeeper
{
    private Mission mission;
    private Campaign campaign;

    public MissionFlightKeeper (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }

    public List<Flight> keepLimitedFlights() throws PWCGException 
    {
        List<Flight> missionFlights = new ArrayList<Flight>();
        List<Flight> axisFlights = keepLimitedAxisFlights();        
        List<Flight> alliedFlights = keepLimitedAlliedFlights();

        missionFlights.addAll(alliedFlights);
        missionFlights.addAll(axisFlights);
        
        return missionFlights;
    }

    private List<Flight> keepLimitedAlliedFlights() throws PWCGException
    {
        List<Flight> alliedAiFlights = mission.getMissionFlightBuilder().getAlliedAiFlights();
        System.out.println("Allied flights before " + alliedAiFlights.size());
        int maxAlliedAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey);
        List<Flight> alliedAiFlightsKept = selectFlightsToKeep(maxAlliedAiFlights, Side.ALLIED, alliedAiFlights);
        System.out.println("Axis flights kept " + alliedAiFlightsKept.size() + " out of max " + maxAlliedAiFlights);
        return alliedAiFlightsKept;
    }

    private List<Flight> keepLimitedAxisFlights() throws PWCGException
    {
        List<Flight> axisAiFlights = mission.getMissionFlightBuilder().getAxisAiFlights();
        int maxAxisAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisFlightsToKeepKey);
        List<Flight> axisAiFlightsKept = selectFlightsToKeep(maxAxisAiFlights, Side.AXIS, axisAiFlights);
        return axisAiFlightsKept;
    }

    private List<Flight> selectFlightsToKeep(int maxToKeep, Side side, List<Flight> enemyAiFlights) throws PWCGException
    {
        int maxFighterToKeep = getMaxFighterFlights();
        int numFighterKept = 0;
            
        List<Flight> keptFlights = new ArrayList<Flight>();
        for (Flight flight : enemyAiFlights)
        {
            boolean isFighterFlight = isConsideredExcessFighterFlight(flight);
            if (isFighterFlight)
            {
                if ((numFighterKept < maxFighterToKeep) && (keptFlights.size() < maxToKeep))
                {
                    keptFlights.add(flight);
                    ++numFighterKept;
                }
            }
            else
            {
                if(keptFlights.size() < maxToKeep)
                {
                    keptFlights.add(flight);
                }
            }
        }

        return keptFlights;
    }
    
    private boolean isConsideredExcessFighterFlight(Flight enemyFlight)
    {
    	boolean isPlayerFlightFighter = mission.getMissionFlightBuilder().hasPlayerFlightWithFlightTypes(FlightTypes.getFlightTypesByCategory(FlightTypeCategory.FIGHTER));
        boolean isEnemyFighterSquadron = enemyFlight.getPlanes().get(0).isPrimaryRole(Role.ROLE_FIGHTER);
        boolean isEnemyFighterFlight = enemyFlight.getFlightType().isCategory(FlightTypeCategory.FIGHTER);
        
        if (isEnemyFighterFlight)
        {
        	return true;
        }

        if (!isPlayerFlightFighter && isEnemyFighterSquadron)
        {
            return true;
        }
        
        return false;
    }

    private int getMaxFighterFlights() throws PWCGException
    {
        MaxFighterFlightCalculator maxFighterFlightCalculator = new MaxFighterFlightCalculator(campaign, mission);
        return maxFighterFlightCalculator.getMaxFighterFlightsForMission();
    }
}
