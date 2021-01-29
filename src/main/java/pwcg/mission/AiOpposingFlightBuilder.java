package pwcg.mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.factory.WeatherFlightTypeConverter;
import pwcg.mission.options.MissionWeather;

public class AiOpposingFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private MissionSquadronFlightTypes playerFlightTypes;
    private List<IFlight> missionFlights = new ArrayList<IFlight>();

    AiOpposingFlightBuilder (Campaign campaign, Mission mission, MissionSquadronFlightTypes playerFlightTypes)
    {
        this.campaign = campaign;
        this.mission = mission;
        this.playerFlightTypes = playerFlightTypes;
    }
    

    public List<IFlight> createOpposingAiFlights() throws PWCGException 
    {
        for (Squadron squadron : playerFlightTypes.getSquadrons())
        {
            FlightTypes playerFlightType = playerFlightTypes.getFlightTypeForSquadron(squadron.getSquadronId());
            FlightTypes opposingFlightType = determineOpposingFlightType(playerFlightType, mission.getWeather());
            if (opposingFlightType != FlightTypes.ANY)
            {
                Squadron opposingSquadron = determineOpposingSquadron(squadron.determineEnemySide(), playerFlightType);
                if (opposingSquadron != null)
                {
                    IFlight flight = buildFlight(opposingFlightType, opposingSquadron);
                    if (flight != null)
                    {
                        flight.getFlightInformation().setOpposingFlight(true);
                        missionFlights.add(flight);
                    }
                }
            }
        }
        return missionFlights;
    }

    private Squadron determineOpposingSquadron(Side opposingSquadronSide, FlightTypes playerFlightType) throws PWCGException
    {
        List<Role> opposingFlightRoles = new ArrayList<>();
        if (playerFlightType == FlightTypes.BALLOON_BUST)
        {
            opposingFlightRoles = new ArrayList<>(Arrays.asList(Role.ROLE_FIGHTER));
        }
        else if (playerFlightType == FlightTypes.BALLOON_DEFENSE)
        {
            opposingFlightRoles = new ArrayList<>(Arrays.asList(Role.ROLE_FIGHTER));
        }
        else if (playerFlightType == FlightTypes.INTERCEPT)
        {
            opposingFlightRoles = new ArrayList<>(Arrays.asList(Role.ROLE_BOMB, Role.ROLE_DIVE_BOMB, Role.ROLE_TRANSPORT));
        }
        else if (playerFlightType == FlightTypes.LOW_ALT_CAP)
        {
            opposingFlightRoles = new ArrayList<>(Arrays.asList(Role.ROLE_ATTACK));
        }

        OpposingSquadronChooser opposingSquadronChooser = new OpposingSquadronChooser(campaign, opposingFlightRoles, opposingSquadronSide, 1);
        List<Squadron> viableSquadrons = opposingSquadronChooser.getOpposingSquadrons();
        if (viableSquadrons.size() > 0)
        {
            Collections.shuffle(viableSquadrons);
            return viableSquadrons.get(0);
        }
        return null;
    }


    private FlightTypes determineOpposingFlightType(FlightTypes playerFlightType, MissionWeather missionWeather) throws PWCGException 
    {
        if (playerFlightType == FlightTypes.BALLOON_BUST)
        {
            return FlightTypes.BALLOON_DEFENSE;
        }
        else if (playerFlightType == FlightTypes.BALLOON_DEFENSE)
        {
            return FlightTypes.BALLOON_BUST;
        }
        else if (playerFlightType == FlightTypes.INTERCEPT)
        {
            return WeatherFlightTypeConverter.getFlightType(FlightTypes.BOMB, missionWeather);
        }
        else if (playerFlightType == FlightTypes.LOW_ALT_CAP)
        {
            return FlightTypes.GROUND_ATTACK;
        }

        return FlightTypes.ANY;
    }

    private IFlight buildFlight(FlightTypes flightType, Squadron squadron) throws PWCGException
    {
        FlightFactory flightFactory = new FlightFactory(campaign);
        boolean isPlayerFlight = false;
        IFlight flight = flightFactory.buildFlight(mission, squadron, flightType, isPlayerFlight);
        return flight;        
    }

}
