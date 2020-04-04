package pwcg.mission.flight.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.utils.WeightedOddsCalculator;
import pwcg.mission.flight.FlightTypes;

public class FCFlightTypeFactory implements IFlightTypeFactory
{
    private Campaign campaign;
    private List<Integer> weightedOdds = new ArrayList<>();
    private Map<Integer, FlightTypes> flightTypesByIndex = new HashMap<>();

    public FCFlightTypeFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    @Override
    public FlightTypes getFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        Role missionRole = squadron.getSquadronRoles().selectRoleForMission(campaign.getDate());

        FlightTypes flightType = FlightTypes.ANY;
        if (missionRole == Role.ROLE_BOMB || missionRole == Role.ROLE_ARTILLERY_SPOT)
        {
            flightType = getBomberFlightType(squadron);
        }
        else if (missionRole == Role.ROLE_FIGHTER)
        {
            flightType = getFighterFlightType(squadron, isPlayerFlight);
        }
        else if (missionRole == Role.ROLE_ATTACK)
        {
            flightType = getAttackFlightType();
        }
        else if (missionRole == Role.ROLE_RECON)
        {
            flightType = getReconFlightType(squadron);
        }
        else
        {
            throw new PWCGMissionGenerationException("No valid role for squadron: " + squadron.determineDisplayName(campaign.getDate()));
        }
        
        flightType = convertAiFlights(flightType, isPlayerFlight);
        return flightType;
    }


    private FlightTypes convertAiFlights(FlightTypes flightType, boolean isPlayerFlight)
    {
        if (!isPlayerFlight)
        {
            if (flightType == FlightTypes.BALLOON_DEFENSE || flightType == FlightTypes.BALLOON_BUST)
            {
                return FlightTypes.PATROL;
            }
        }
        return flightType;
    }

    private FlightTypes getFighterFlightType(Squadron squadron, boolean isPlayerFlight) throws PWCGException
    {
        int currentIndex = 0;
        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedBalloonBustMissionKey, FlightTypes.BALLOON_BUST, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedBalloonDefenseMissionKey, FlightTypes.BALLOON_DEFENSE, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedOffensiveMissionKey, FlightTypes.OFFENSIVE, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedInterceptMissionKey, FlightTypes.INTERCEPT, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedEscortMissionKey, FlightTypes.ESCORT, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedPatrolMissionKey, FlightTypes.PATROL, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedLowAltPatrolMissionKey, FlightTypes.LOW_ALT_PATROL, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedLowAltCapMissionKey, FlightTypes.LOW_ALT_CAP, currentIndex);
        }
        else
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisBalloonBustMissionKey, FlightTypes.BALLOON_BUST, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisBalloonDefenseMissionKey, FlightTypes.BALLOON_DEFENSE, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisOffensiveMissionKey, FlightTypes.OFFENSIVE, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisInterceptMissionKey, FlightTypes.INTERCEPT, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisEscortMissionKey, FlightTypes.ESCORT, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisPatrolMissionKey, FlightTypes.PATROL, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisLowAltPatrolMissionKey, FlightTypes.LOW_ALT_PATROL, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisLowAltCapMissionKey, FlightTypes.LOW_ALT_CAP, currentIndex);
        }
        
        int selectedIndex = WeightedOddsCalculator.calculateWeightedodds(weightedOdds);
        FlightTypes flightType = flightTypesByIndex.get(selectedIndex);

        if (flightType == FlightTypes.ESCORT || flightType == FlightTypes.BALLOON_BUST || flightType == FlightTypes.BALLOON_DEFENSE)
        {
            if (!isPlayerFlight)
            {
                flightType = FlightTypes.PATROL;
            }
        }

        return flightType;
    }    

    private FlightTypes getAttackFlightType() throws PWCGException 
    {
        FlightTypes flightType = FlightTypes.GROUND_ATTACK;
        return flightType;
    }

    private FlightTypes getBomberFlightType(Squadron squadron) throws PWCGException 
    {
        int currentIndex = 0;
        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedBombingMissionKey, FlightTypes.BOMB, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedLowAltBombingMissionKey, FlightTypes.LOW_ALT_BOMB, currentIndex);
        }
        else
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisBombingMissionKey, FlightTypes.BOMB, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisLowAltBombingMissionKey, FlightTypes.LOW_ALT_BOMB, currentIndex);
        }
        
        int selectedIndex = WeightedOddsCalculator.calculateWeightedodds(weightedOdds);
        FlightTypes flightType = flightTypesByIndex.get(selectedIndex);

        return flightType;
    }

    private FlightTypes getReconFlightType(Squadron squadron) throws PWCGException
    {
        int currentIndex = 0;
        if (squadron.determineSquadronCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedReconKey, FlightTypes.RECON, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedContactPatrolKey, FlightTypes.CONTACT_PATROL, currentIndex);
        }
        else
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisReconKey, FlightTypes.RECON, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisContactPatrolKey, FlightTypes.CONTACT_PATROL, currentIndex);
        }
        
        int selectedIndex = WeightedOddsCalculator.calculateWeightedodds(weightedOdds);
        FlightTypes flightType = flightTypesByIndex.get(selectedIndex);

        return flightType;
    }
    

    private int addItemToWeightedList(String configKey, FlightTypes flightType, int currentIndex) throws PWCGException
    {
        int oddsOfMission = campaign.getCampaignConfigManager().getIntConfigParam(configKey);
        weightedOdds.add(oddsOfMission);
        flightTypesByIndex.put(currentIndex, flightType);
        return ++currentIndex;
    }
}
