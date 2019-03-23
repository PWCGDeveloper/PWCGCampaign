package pwcg.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightProximityAnalyzer;

public class MissionFlightKeeper
{
    private Mission mission;
    private Campaign campaign;
    private ConfigManager configManager;

    public MissionFlightKeeper (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
        
        configManager = campaign.getCampaignConfigManager();
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
        List<Flight> axisAiFlights = mission.getMissionFlightBuilder().getAlliedAiFlights();
        int maxAlliedAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey);
        return selectFlightsToKeep(maxAlliedAiFlights, Side.ALLIED, axisAiFlights);
    }

    private List<Flight> keepLimitedAxisFlights() throws PWCGException
    {
        List<Flight> axisAiFlights = mission.getMissionFlightBuilder().getAxisAiFlights();
        int maxAxisAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisFlightsToKeepKey);
        return selectFlightsToKeep(maxAxisAiFlights, Side.AXIS, axisAiFlights);
    }

    private List<Flight> selectFlightsToKeep(int maxToKeep, Side side, List<Flight> aiFLights) throws PWCGException
    {
        int numToKeep = determineNumFlightsToKeepForSide(maxToKeep);
        
        int maxFighterToKeep = getMaxFighterFlights();
        int numFighterKept = 0;
            
        Map<Double, Flight> enemyFlightsByEncounterDistance = mapEnemyDistanceToPlayerFlight(aiFLights);
        List<Flight> keptFlights = new ArrayList<Flight>();
        for (Flight enemyFlight : enemyFlightsByEncounterDistance.values())
        {
            boolean isFighterFlight = isFighterFlightForPurposeOfKeep(enemyFlight);
            if (isFighterFlight)
            {
                if (numFighterKept < maxFighterToKeep)
                {
                    keptFlights.add(enemyFlight);
                    ++numFighterKept;
                }
            }
            else
            {
                if(keptFlights.size() < numToKeep)
                {
                    keptFlights.add(enemyFlight);
                }
            }
        }

        return keptFlights;
    }

    private int determineNumFlightsToKeepForSide(int maxToKeep)
    {
        int minToKeep = 2;
        if (maxToKeep < minToKeep)
        {
            maxToKeep = minToKeep;
        }
        
        int numToKeep = minToKeep +  RandomNumberGenerator.getRandom(maxToKeep - minToKeep + 1);
        return numToKeep;
    }    
    
    private boolean isFighterFlightForPurposeOfKeep(Flight enemyFlight)
    {
        boolean isFighterSquadron = enemyFlight.getPlanes().get(0).isPrimaryRole(Role.ROLE_FIGHTER);
        boolean isFighterFlight = enemyFlight.isFighterFlight();
        if (isFighterSquadron || isFighterFlight)
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

    private Map<Double, Flight> mapEnemyDistanceToPlayerFlight(List<Flight> aiFLights) throws PWCGException
    {
        Map<Double, Flight> enemyFlightsByEncounterDistance = new TreeMap<Double, Flight>();
        
        for (Flight flight : aiFLights)
        {
            if (flight.getClosestContactWithPlayerDistance() > 0.0)
            {
                if (!isTooCloseToPlayerBase(flight))
                {
                    enemyFlightsByEncounterDistance.put(flight.getClosestContactWithPlayerDistance(), flight);
                }
            }
        }
        return enemyFlightsByEncounterDistance;
    }

    private boolean isTooCloseToPlayerBase(Flight flight) throws PWCGException
    {
        // TODO COOP Revisit flight proximity analyzer
        FlightProximityAnalyzer flightProximityAnalyzer = new FlightProximityAnalyzer(mission);
        double distanceToBase = flightProximityAnalyzer.proximityToPlayerAirbase(flight);
        
        int DistanceFromPlayerFieldZone = configManager.getIntConfigParam(ConfigItemKeys.DistanceFromPlayerFieldZoneKey);
        int DistanceToPlayerFieldOverrideOdds = configManager.getIntConfigParam(ConfigItemKeys.DistanceToPlayerFieldOverrideOddsKey);
        if (distanceToBase < DistanceFromPlayerFieldZone)
        {
            
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll > DistanceToPlayerFieldOverrideOdds)
            {
                return true;
            }
        }
        
        return false;
    }
}
