package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plot.FlightProximityAnalyzer;

public class MissionFlightKeeper
{
    private Mission mission;
    private Campaign campaign;
    private MissionFlightProximitySorter proximitySorter;

    Map<Integer, IFlight> alliedFighterFlightsKept = new HashMap<>();
    Map<Integer, IFlight> alliedBomberFlightsKept = new HashMap<>();
    Map<Integer, IFlight> alliedOtherFlightsKept = new HashMap<>();
    Map<Integer, IFlight> axisFighterFlightsKept = new HashMap<>();
    Map<Integer, IFlight> axisBomberFlightsKept = new HashMap<>();
    Map<Integer, IFlight> axisOtherFlightsKept = new HashMap<>();

    public MissionFlightKeeper(Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
        this.proximitySorter = new MissionFlightProximitySorter();
    }

    public List<IFlight> keepLimitedFlights() throws PWCGException
    {
        PWCGLogger.log(LogLevel.DEBUG, "*** Flight Keeper Started ***: ");
        
        FlightProximityAnalyzer proximityAnalyzer = new FlightProximityAnalyzer(mission);
        proximityAnalyzer.plotFlightEncounters();

        proximitySorter.mapEnemyDistanceToPlayerFlights(mission.getMissionFlightBuilder().getAiFlights());

        keepRequiredAlliedFlights();
        keepRequiredAxisFlights();

        keepLimitedAlliedFlights();
        keepLimitedAxisFlights();

        Map<Integer, IFlight> aiFlightsKept = combineKeptFlights();
        return new ArrayList<>(aiFlightsKept.values());
    }

    private Map<Integer, IFlight> combineKeptFlights()
    {
        Map<Integer, IFlight> aiFlightsKept = new HashMap<>();
        aiFlightsKept.putAll(alliedFighterFlightsKept);
        aiFlightsKept.putAll(alliedBomberFlightsKept);
        aiFlightsKept.putAll(alliedOtherFlightsKept);
        aiFlightsKept.putAll(axisFighterFlightsKept);
        aiFlightsKept.putAll(axisBomberFlightsKept);
        aiFlightsKept.putAll(axisOtherFlightsKept);

        for (IFlight keptFlight : aiFlightsKept.values())
        {
            mission.getMissionSquadronChooser().registerSquadronInUse(keptFlight.getSquadron());
        }
        return aiFlightsKept;
    }

    private void keepRequiredAlliedFlights() throws PWCGException
    {
        keepRequiredFlights(proximitySorter.getFlightsByProximity(Side.ALLIED), alliedFighterFlightsKept, alliedBomberFlightsKept, alliedOtherFlightsKept);
    }

    private void keepRequiredAxisFlights() throws PWCGException
    {
        keepRequiredFlights(proximitySorter.getFlightsByProximity(Side.AXIS), axisFighterFlightsKept, axisBomberFlightsKept, axisOtherFlightsKept);
    }

    private void keepRequiredFlights(List<IFlight> flights, Map<Integer, IFlight> fighterFlightsKept, Map<Integer, IFlight> bomberFlightsKept,
            Map<Integer, IFlight> otherFlightsKept) throws PWCGException
    {
        for (IFlight flight : flights)
        {
            if (isNecessaryFlight(flight))
            {
                if (flight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
                {
                    fighterFlightsKept.put(flight.getSquadron().getSquadronId(), flight);
                }
                else if (flight.getFlightType().isCategory(FlightTypeCategory.BOMB))
                {
                    bomberFlightsKept.put(flight.getSquadron().getSquadronId(), flight);
                }
                else
                {
                    otherFlightsKept.put(flight.getSquadron().getSquadronId(), flight);
                }
            }
        }
    }

    private void keepLimitedAlliedFlights() throws PWCGException
    {
        List<IFlight> alliedAiFlights = proximitySorter.getFlightsByProximity(Side.ALLIED);
        int configuredAlliedAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey);
        int maxAlliedAiFlights = calculateFlightsToKeepForSide(Side.ALLIED, configuredAlliedAiFlights);
        selectFlightsToKeep(maxAlliedAiFlights, Side.ALLIED, alliedAiFlights);
    }

    private void keepLimitedAxisFlights() throws PWCGException
    {
        List<IFlight> axisAiFlights = proximitySorter.getFlightsByProximity(Side.AXIS);
        int configuredAxisAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisFlightsToKeepKey);
        int maxAxisAiFlights = calculateFlightsToKeepForSide(Side.AXIS, configuredAxisAiFlights);
        selectFlightsToKeep(maxAxisAiFlights, Side.AXIS, axisAiFlights);
    }

    private int calculateFlightsToKeepForSide(Side side, int configuredFlightsToKeep) throws PWCGException
    {
        List<IFlight> playerFlights = mission.getMissionFlightBuilder().getPlayerFlightsForSide(side);
        int aiFlightsToKeep = configuredFlightsToKeep - playerFlights.size();
        if (aiFlightsToKeep < 0)
        {
            aiFlightsToKeep = 0;
        }
        return aiFlightsToKeep;
    }

    private void selectFlightsToKeep(int maxToKeep, Side side, List<IFlight> aiFlights) throws PWCGException
    {
        int maxBomberToKeep = getMaxBomberFlights(maxToKeep, side);
        int maxFighterToKeep = getMaxFighterFlights(maxToKeep);

        Map<Integer, IFlight> fighterFlightsKept = alliedFighterFlightsKept;
        Map<Integer, IFlight> bomberFlightsKept = alliedBomberFlightsKept;
        Map<Integer, IFlight> otherFlightsKept = alliedOtherFlightsKept;
        if (side == Side.AXIS)
        {
            fighterFlightsKept = axisFighterFlightsKept;
            bomberFlightsKept = axisBomberFlightsKept;
            otherFlightsKept = axisOtherFlightsKept;
        }

        for (IFlight flight : aiFlights)
        {
            if (isSquadronIncluded(flight.getSquadron().getSquadronId()))
            {
                continue;
            }

            int totalFlightsKept = fighterFlightsKept.size() + bomberFlightsKept.size() + otherFlightsKept.size();

            if (flight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
            {
                if ((fighterFlightsKept.size() < maxFighterToKeep) && (totalFlightsKept < maxToKeep))
                {
                    fighterFlightsKept.put(flight.getSquadron().getSquadronId(), flight);
                }
            }
            else if (flight.getFlightType().isCategory(FlightTypeCategory.BOMB))
            {
                if ((bomberFlightsKept.size() < maxBomberToKeep) && (totalFlightsKept < maxToKeep))
                {
                    bomberFlightsKept.put(flight.getSquadron().getSquadronId(), flight);
                }
            }
            else
            {
                if (totalFlightsKept < maxToKeep)
                {
                    otherFlightsKept.put(flight.getSquadron().getSquadronId(), flight);
                }
            }
        }
    }

    private boolean isNecessaryFlight(IFlight flight) throws PWCGException
    {
        if (flight.getFlightInformation().isPlayerFlight())
        {
            PWCGLogger.log(LogLevel.DEBUG, "necessary flight because player: " + flight.getSquadron().determineDisplayName(campaign.getDate()));
            return true;
        }

        if (flight.getFlightInformation().isOpposingFlight())
        {
            PWCGLogger.log(LogLevel.DEBUG, "necessary flight because opposing: " + flight.getSquadron().determineDisplayName(campaign.getDate()));
            return true;
        }

        if (mission.getSkirmish() != null && mission.getSkirmish().isIconicFlightType(flight.getFlightInformation().getFlightType()))
        {
            PWCGLogger.log(LogLevel.DEBUG, "necessary flight because iconic: " + flight.getSquadron().determineDisplayName(campaign.getDate()));
            return true;
        }

        PWCGLogger.log(LogLevel.DEBUG, "Not necessary flight: " + flight.getSquadron().determineDisplayName(campaign.getDate()));
        return false;
    }

    private int getMaxFighterFlights(int maxFlightsForSide) throws PWCGException
    {
        MaxFighterFlightCalculator maxFighterFlightCalculator = new MaxFighterFlightCalculator(campaign, mission);
        return maxFighterFlightCalculator.getMaxFighterFlightsForMission(maxFlightsForSide);
    }

    private int getMaxBomberFlights(int maxToKeep, Side side) throws PWCGException
    {
        int maxBomberFlights = 1;
        ConfigManagerCampaign configManager = mission.getCampaign().getCampaignConfigManager();
        String currentCpuAllowanceSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigCpuAllowanceKey);
        if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            Side missionSide = mission.getMissionSide();
            if (missionSide == Side.NEUTRAL)
            {
                maxBomberFlights = 1;
            }
            else if (missionSide == side)
            {
                maxBomberFlights = 0;
            }
            else
            {
                maxBomberFlights = 1;
            }
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            maxBomberFlights = 1;
        }
        else if (currentCpuAllowanceSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            maxBomberFlights = 99;
        }
        return maxBomberFlights;
    }

    private boolean isSquadronIncluded(int squadronId) throws PWCGException
    {
        if (alliedFighterFlightsKept.containsKey(squadronId))
        {
            return true;
        }
        
        if (alliedBomberFlightsKept.containsKey(squadronId))
        {
            return true;
        }
        
        if (alliedOtherFlightsKept.containsKey(squadronId))
        {
            return true;
        }
        
        if (axisFighterFlightsKept.containsKey(squadronId))
        {
            return true;
        }
        
        if (axisBomberFlightsKept.containsKey(squadronId))
        {
            return true;
        }
        
        if (axisOtherFlightsKept.containsKey(squadronId))
        {
            return true;
        }
        
        return false;
    }
}
