package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightProximityAnalyzer;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;

public class MissionFlightKeeper
{
    private Mission mission;
    private Campaign campaign;
    private FlightProximityAnalyzer flightAnalyzer;
    private MissionFlightProximitySorter proximitySorter;

    public MissionFlightKeeper (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }

    public List<Flight> keepLimitedFlights() throws PWCGException 
    {
        flightAnalyzer = new FlightProximityAnalyzer(mission);
        flightAnalyzer.plotFlightEncounters();
        
        proximitySorter = new MissionFlightProximitySorter();
        proximitySorter.mapEnemyDistanceToPlayerFlights(mission.getMissionFlightBuilder().getAiFlights());

        List<Flight> aiFlightsKept = new ArrayList<Flight>();
        List<Flight> axisAiFlights = keepLimitedAxisFlights();        
        List<Flight> alliedAiFlights = keepLimitedAlliedFlights();

        aiFlightsKept.addAll(alliedAiFlights);
        aiFlightsKept.addAll(axisAiFlights);
        
        return aiFlightsKept;
    }

    private List<Flight> keepLimitedAlliedFlights() throws PWCGException
    {
        List<Flight> alliedAiFlights = proximitySorter.getFlightsByProximity(Side.ALLIED);
        int configuredAlliedAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey);
        int maxAlliedAiFlights =  calculateFlightsToKeepForSide(Side.ALLIED, configuredAlliedAiFlights);
        List<Flight> alliedAiFlightsKept = selectFlightsToKeep(maxAlliedAiFlights, Side.ALLIED, alliedAiFlights);
        return alliedAiFlightsKept;
    }

    private List<Flight> keepLimitedAxisFlights() throws PWCGException
    {
        List<Flight> axisAiFlights = proximitySorter.getFlightsByProximity(Side.AXIS);
        int configuredAxisAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisFlightsToKeepKey);
        int maxAxisAiFlights =  calculateFlightsToKeepForSide(Side.AXIS, configuredAxisAiFlights);
        List<Flight> axisAiFlightsKept = selectFlightsToKeep(maxAxisAiFlights, Side.AXIS, axisAiFlights);
        return axisAiFlightsKept;
    }

    private int calculateFlightsToKeepForSide(Side side, int configuredFlightsToKeep) throws PWCGException
    {
        List<Flight> playerFlights = mission.getMissionFlightBuilder().getPlayerFlightsForSide(side);
        int aiFlightsToKeep = configuredFlightsToKeep - playerFlights.size();
        if (aiFlightsToKeep < 0)
        {
            aiFlightsToKeep = 0;
        }
        return aiFlightsToKeep;
    }
    
    private List<Flight> selectFlightsToKeep(int maxToKeep, Side side, List<Flight> aiFlights) throws PWCGException
    {
        int maxFighterToKeep = getMaxFighterFlights(maxToKeep);
        int numFighterKept = 0;
        
        List<Flight> keptFlights = new ArrayList<Flight>();
        for (Flight flight : aiFlights)
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
    
    private boolean isConsideredExcessFighterFlight(Flight aiFlight)
    {
    	boolean isPlayerFlightFighter = mission.getMissionFlightBuilder().hasPlayerFlightWithFlightTypes(FlightTypes.getFlightTypesByCategory(FlightTypeCategory.FIGHTER));
        boolean isAiFighterSquadron = aiFlight.isFlightHasFighterPlanes();
        boolean isAiFighterFlight = aiFlight.getFlightType().isCategory(FlightTypeCategory.FIGHTER);
        
        if (isAiFighterFlight)
        {
        	return true;
        }

        if (!isPlayerFlightFighter && isAiFighterSquadron)
        {
            return true;
        }
        
        return false;
    }

    private int getMaxFighterFlights(int maxFlightsForSide) throws PWCGException
    {
        MaxFighterFlightCalculator maxFighterFlightCalculator = new MaxFighterFlightCalculator(campaign, mission);
        return maxFighterFlightCalculator.getMaxFighterFlightsForMission(maxFlightsForSide);
    }
}
