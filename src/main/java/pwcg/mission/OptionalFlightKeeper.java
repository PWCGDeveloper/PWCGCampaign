package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.KeptFlightsRecorder.KeptFlightCountType;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.IFlight;

public class OptionalFlightKeeper
{
    private Mission mission;
    private Campaign campaign;

    private List<IFlight> alliedProximitySortedFlights;
    private List<IFlight> axisProximitySortedFlights;
    private KeptFlightsRecorder keptFlightsRecorder;

    public OptionalFlightKeeper(Mission mission, List<IFlight> alliedProximitySortedFlights, List<IFlight> axisProximitySortedFlights, KeptFlightsRecorder keptFlightsRecorder)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.alliedProximitySortedFlights = alliedProximitySortedFlights;
        this.axisProximitySortedFlights = axisProximitySortedFlights;
        this.keptFlightsRecorder = keptFlightsRecorder;
    }

    public void keepLimitedFlights() throws PWCGException
    {
        PWCGLogger.log(LogLevel.DEBUG, "*** Optional Flight Keeper Started ***: ");

        keepLimitedAlliedFlights();
        keepLimitedAxisFlights();
    }

    private void keepLimitedAlliedFlights() throws PWCGException
    {
        int configuredAlliedAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey);
        int maxAlliedAiFlights = calculateFlightsToKeepForSide(Side.ALLIED, configuredAlliedAiFlights);
        selectFlightsToKeep(maxAlliedAiFlights, Side.ALLIED, alliedProximitySortedFlights);
    }

    private void keepLimitedAxisFlights() throws PWCGException
    {
        int configuredAxisAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisFlightsToKeepKey);
        int maxAxisAiFlights = calculateFlightsToKeepForSide(Side.AXIS, configuredAxisAiFlights);
        selectFlightsToKeep(maxAxisAiFlights, Side.AXIS, axisProximitySortedFlights);
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

        for (IFlight flight : aiFlights)
        {
            if (mission.getMissionSquadronRecorder().isSquadronInUse(flight.getSquadron().getSquadronId()))
            {
                continue;
            }

            int totalFlightsKept = keptFlightsRecorder.getFlightKeptCount(KeptFlightCountType.KEPT_FLIGHT_COUNT_ALL, flight);
            if (flight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
            {
                int fighterFlightsKept = keptFlightsRecorder.getFlightKeptCount(KeptFlightCountType.KEPT_FLIGHT_COUNT_FIGHTER, flight);
                if ((fighterFlightsKept < maxFighterToKeep) && (totalFlightsKept < maxToKeep))
                {
                    keptFlightsRecorder.keepFlight(flight);
                }
            }
            else if (flight.getFlightType().isCategory(FlightTypeCategory.BOMB))
            {
                int bomberFlightsKept = keptFlightsRecorder.getFlightKeptCount(KeptFlightCountType.KEPT_FLIGHT_COUNT_BOMBER, flight);
                if ((bomberFlightsKept < maxBomberToKeep) && (totalFlightsKept < maxToKeep))
                {
                    keptFlightsRecorder.keepFlight(flight);
                }
            }
            else
            {
                if (totalFlightsKept < maxToKeep)
                {
                    keptFlightsRecorder.keepFlight(flight);
                }
            }
        }
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
}
