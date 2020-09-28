package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypeCategory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class MissionFlightKeeper
{
    private Mission mission;
    private Campaign campaign;
    private MissionFlightProximitySorter proximitySorter;

    public MissionFlightKeeper (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }

    public List<IFlight> keepLimitedFlights() throws PWCGException 
    {
        proximitySorter = new MissionFlightProximitySorter();
        proximitySorter.mapEnemyDistanceToPlayerFlights(mission);

        List<IFlight> aiFlightsKept = new ArrayList<IFlight>();
        List<IFlight> axisAiFlights = keepLimitedAxisFlights();        
        List<IFlight> alliedAiFlights = keepLimitedAlliedFlights();

        aiFlightsKept.addAll(alliedAiFlights);
        aiFlightsKept.addAll(axisAiFlights);
        
        for (IFlight keptFlight : aiFlightsKept)
        {
            mission.getMissionSquadronChooser().registerSquadronInUse(keptFlight.getSquadron());
        }
        
        return aiFlightsKept;
    }

    private List<IFlight> keepLimitedAlliedFlights() throws PWCGException
    {
        List<IFlight> alliedAiFlights = proximitySorter.getFlightsByProximity(Side.ALLIED);
        int configuredAlliedAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey);
        int maxAlliedAiFlights =  calculateFlightsToKeepForSide(Side.ALLIED, configuredAlliedAiFlights);
        List<IFlight> alliedAiFlightsKept = selectFlightsToKeep(maxAlliedAiFlights, Side.ALLIED, alliedAiFlights);
        return alliedAiFlightsKept;
    }

    private List<IFlight> keepLimitedAxisFlights() throws PWCGException
    {
        List<IFlight> axisAiFlights = proximitySorter.getFlightsByProximity(Side.AXIS);
        int configuredAxisAiFlights = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisFlightsToKeepKey);
        int maxAxisAiFlights =  calculateFlightsToKeepForSide(Side.AXIS, configuredAxisAiFlights);
        List<IFlight> axisAiFlightsKept = selectFlightsToKeep(maxAxisAiFlights, Side.AXIS, axisAiFlights);
        return axisAiFlightsKept;
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
    
    private List<IFlight> selectFlightsToKeep(int maxToKeep, Side side, List<IFlight> aiFlights) throws PWCGException
    {
        int maxFighterToKeep = getMaxFighterFlights(maxToKeep);
        int numFighterKept = 0;
        
        List<IFlight> keptFlights = new ArrayList<IFlight>();
        for (IFlight flight : aiFlights)
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
    
    private boolean isConsideredExcessFighterFlight(IFlight aiFlight)
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
