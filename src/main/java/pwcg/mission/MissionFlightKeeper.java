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
    private MissionFlightBuilder missionFlightBuilder;
    private Campaign campaign;
    private ConfigManager configManager;

    public MissionFlightKeeper (Campaign campaign, MissionFlightBuilder missionFlightBuilder)
    {
        this.missionFlightBuilder = missionFlightBuilder;
        this.campaign = campaign;
        
        configManager = campaign.getCampaignConfigManager();
    }

    public List<Flight> keepLimitedFlights() throws PWCGException 
    {
        List<Flight> missionFlights = new ArrayList<Flight>();
        
        List<Flight> enemyFlights = keepLimitedEnemyFlights();
        
        List<Flight> friendlyFlights = keepLimitedFriendlyFlights();

        missionFlights.addAll(friendlyFlights);
        missionFlights.addAll(enemyFlights);
        
        return missionFlights;
    }

    private List<Flight> keepLimitedFriendlyFlights() throws PWCGException
    {
        List<Flight> friendlyFlights = null;
        if (campaign.determineCountry().getSide() == Side.AXIS)
        {
            friendlyFlights = missionFlightBuilder.getAxisAiFlights();
        }
        else
        {
            friendlyFlights = missionFlightBuilder.getAlliedAiFlights();
        }

        int minToKeep = configManager.getIntConfigParam(ConfigItemKeys.FriendlyFlightsToKeepMinKey);
        int maxToKeep = configManager.getIntConfigParam(ConfigItemKeys.FriendlyFlightsToKeepMaxKey);
        
        if (maxToKeep < minToKeep)
        {
            minToKeep = maxToKeep - 1;
        }
        
        return selectFlightsToKeep(minToKeep, maxToKeep, false, friendlyFlights);
    }

    private List<Flight> keepLimitedEnemyFlights() throws PWCGException
    {
        List<Flight> enemyFlights = null;
        if (campaign.determineCountry().getSide() == Side.AXIS)
        {
            enemyFlights = missionFlightBuilder.getAlliedAiFlights();
        }
        else
        {
            enemyFlights = missionFlightBuilder.getAxisAiFlights();
        }

        int minToKeep = configManager.getIntConfigParam(ConfigItemKeys.EnemyFlightsToKeepMinKey);
        int maxToKeep = configManager.getIntConfigParam(ConfigItemKeys.EnemyFlightsToKeepMaxKey);
        
        if (maxToKeep < minToKeep)
        {
            minToKeep = maxToKeep - 1;
        }
        
        return selectFlightsToKeep(minToKeep, maxToKeep, true, enemyFlights);
    }

    private List<Flight> selectFlightsToKeep(int minToKeep, int maxToKeep, boolean isEnemy, List<Flight> aiFLights) throws PWCGException
    {
        int numToKeep = minToKeep +  RandomNumberGenerator.getRandom(maxToKeep - minToKeep + 1);
        int maxFighterToKeep = getMaxFighterFlights();
        int numFighterKept = 0;
            
        Map<Double, Flight> enemyFlightsByEncounterDistance = mapEnemyDistanceToPlayerFlight(isEnemy, aiFLights);
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
        MaxFighterFlightCalculator maxFighterFlightCalculator = new MaxFighterFlightCalculator(campaign, missionFlightBuilder.getPlayerFlight());
        return maxFighterFlightCalculator.getMaxFighterFlightsForMission();
    }

    private Map<Double, Flight> mapEnemyDistanceToPlayerFlight(boolean isEnemy, List<Flight> aiFLights) throws PWCGException
    {
        Map<Double, Flight> enemyFlightsByEncounterDistance = new TreeMap<Double, Flight>();
        
        for (Flight flight : aiFLights)
        {
            if (flight.getClosestContactWithPlayerDistance() > 0.0)
            {
                if (!isTooCloseToPlayerBase(flight, isEnemy))
                {
                    enemyFlightsByEncounterDistance.put(flight.getClosestContactWithPlayerDistance(), flight);
                }
            }
        }
        return enemyFlightsByEncounterDistance;
    }

    private boolean isTooCloseToPlayerBase(Flight flight, boolean isEnemy) throws PWCGException
    {
        if (!isEnemy)
        {
            return false;
        }

        double distanceToBase = FlightProximityAnalyzer.proximityToPlayerAirbase(flight);
        
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
