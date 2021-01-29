package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.IFlight;

public class MissionFlightKeeper
{
    private Mission mission;
    private Campaign campaign;
    private MissionFlightProximitySorter proximitySorter;

    List<IFlight> alliedFighterFlightsKept = new ArrayList<IFlight>();
    List<IFlight> alliedBomberFlightsKept = new ArrayList<IFlight>();
    List<IFlight> alliedOtherFlightsKept = new ArrayList<IFlight>();
    List<IFlight> axisFighterFlightsKept = new ArrayList<IFlight>();
    List<IFlight> axisBomberFlightsKept = new ArrayList<IFlight>();
    List<IFlight> axisOtherFlightsKept = new ArrayList<IFlight>();

    public MissionFlightKeeper (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
        this.proximitySorter = new MissionFlightProximitySorter();
    }

    public List<IFlight> keepLimitedFlights() throws PWCGException 
    {
        proximitySorter.mapEnemyDistanceToPlayerFlights(mission);

        keepRequiredAlliedFlights();
        keepRequiredAxisFlights();

        keepLimitedAlliedFlights();
        keepLimitedAxisFlights(); 
        
        List<IFlight> aiFlightsKept = combineKeptFlights();
        return aiFlightsKept;
    }

    private List<IFlight> combineKeptFlights()
    {
        List<IFlight> aiFlightsKept = new ArrayList<>();        
        aiFlightsKept.addAll(alliedFighterFlightsKept);
        aiFlightsKept.addAll(alliedBomberFlightsKept);
        aiFlightsKept.addAll(alliedOtherFlightsKept);
        aiFlightsKept.addAll(axisFighterFlightsKept);
        aiFlightsKept.addAll(axisBomberFlightsKept);
        aiFlightsKept.addAll(axisOtherFlightsKept);

        for (IFlight keptFlight : aiFlightsKept)
        {
            mission.getMissionSquadronChooser().registerSquadronInUse(keptFlight.getSquadron());
        }
        return aiFlightsKept;
    }

    private void keepRequiredAlliedFlights()
    {
        keepRequiredFlights(proximitySorter.getFlightsByProximity(Side.ALLIED), alliedFighterFlightsKept, alliedBomberFlightsKept, alliedOtherFlightsKept);
    }

    private void keepRequiredAxisFlights()
    {
        keepRequiredFlights(proximitySorter.getFlightsByProximity(Side.AXIS), axisFighterFlightsKept, axisBomberFlightsKept, axisOtherFlightsKept);
    }
    
    private void keepRequiredFlights(List<IFlight> flights, List<IFlight> fighterFlightsKept, List<IFlight> bomberFlightsKept, List<IFlight> otherFlightsKept)
    {
        for (IFlight flight : flights)
        {
            if(isNecessaryFlight(flight))
            {
                if (flight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
                {
                    fighterFlightsKept.add(flight);
                }
                else if (flight.getFlightType().isCategory(FlightTypeCategory.BOMB))
                {
                    bomberFlightsKept.add(flight);
                }
                else
                {
                    otherFlightsKept.add(flight);
                }
            }
        }
    }

    private void keepLimitedAlliedFlights() throws PWCGException
    {
        List<IFlight> alliedAiFlights = proximitySorter.getFlightsByProximity(Side.ALLIED);
        int configuredAlliedAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey);
        int maxAlliedAiFlights =  calculateFlightsToKeepForSide(Side.ALLIED, configuredAlliedAiFlights);
        selectFlightsToKeep(maxAlliedAiFlights, Side.ALLIED, alliedAiFlights);
    }

    private void keepLimitedAxisFlights() throws PWCGException
    {
        List<IFlight> axisAiFlights = proximitySorter.getFlightsByProximity(Side.AXIS);
        int configuredAxisAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisFlightsToKeepKey);
        int maxAxisAiFlights =  calculateFlightsToKeepForSide(Side.AXIS, configuredAxisAiFlights);
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
        
        
       List<IFlight> fighterFlightsKept = alliedFighterFlightsKept;
       List<IFlight> bomberFlightsKept = alliedBomberFlightsKept;
       List<IFlight> otherFlightsKept = alliedOtherFlightsKept;
       if (side == Side.AXIS)
       {
           fighterFlightsKept = axisFighterFlightsKept;
           bomberFlightsKept = axisBomberFlightsKept;
           otherFlightsKept = axisOtherFlightsKept;
       }
        
        for (IFlight flight : aiFlights)
        {
            if (isNecessaryFlight(flight))
            {
                continue;
            }
            
            int totalFlightsKept = fighterFlightsKept.size() + bomberFlightsKept.size() + otherFlightsKept.size();

            if (flight.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
            {
                if ((fighterFlightsKept.size() < maxFighterToKeep) && (totalFlightsKept < maxToKeep))
                {
                    fighterFlightsKept.add(flight);
                }
            }
            else if (flight.getFlightType().isCategory(FlightTypeCategory.BOMB))
            {
                if ((bomberFlightsKept.size() < maxBomberToKeep) && (totalFlightsKept < maxToKeep))
                {
                    bomberFlightsKept.add(flight);
                }
            }
            else
            {
                if (totalFlightsKept < maxToKeep)
                {
                    otherFlightsKept.add(flight);
                }
            }
        }
    }
    
    private boolean isNecessaryFlight(IFlight flight)
    {
        if(flight.getFlightInformation().isPlayerFlight() || flight.getFlightInformation().isOpposingFlight())
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
