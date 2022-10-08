package pwcg.mission.flight.opposing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.OpposingSquadronChooser;
import pwcg.mission.flight.FlightFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.factory.WeatherFlightTypeConverter;

public class InterceptOpposingFlightBuilder implements IOpposingFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private Squadron playerSquadron;
    
    InterceptOpposingFlightBuilder (Mission mission, Squadron playerSquadron)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.playerSquadron = playerSquadron;
    }

    @Override
    public List<IFlight> createOpposingFlight() throws PWCGException 
    {
        Squadron opposingSquadron = determineOpposingSquadron();
        FlightTypes opposingFlightType = determineOpposingFlightType(opposingSquadron);
        if (opposingSquadron != null)
        {
            List<IFlight> flight = buildFlight(opposingFlightType, opposingSquadron);
            if (flight != null)
            {
                return flight;
            }
        }
        return new ArrayList<>();
    }

    private FlightTypes determineOpposingFlightType(Squadron opposingSquadron) throws PWCGException 
    {
        if (opposingSquadron.isSquadronThisRole(campaign.getDate(), PwcgRole.ROLE_DIVE_BOMB))
        {
            return WeatherFlightTypeConverter.getFlightType(FlightTypes.DIVE_BOMB, mission.getWeather());
        }
        else if (opposingSquadron.isSquadronThisRole(campaign.getDate(), PwcgRole.ROLE_BOMB))
        {
            return WeatherFlightTypeConverter.getFlightType(FlightTypes.BOMB, mission.getWeather());
        }
        
        return FlightTypes.GROUND_ATTACK;
    }

    private Squadron determineOpposingSquadron() throws PWCGException
    {
        List<PwcgRole> opposingFlightRoles = new ArrayList<>(Arrays.asList(PwcgRole.ROLE_BOMB, PwcgRole.ROLE_DIVE_BOMB, PwcgRole.ROLE_RAIDER));
        OpposingSquadronChooser opposingSquadronChooser = new OpposingSquadronChooser(campaign, opposingFlightRoles, playerSquadron.determineEnemySide(), 1);
        List<Squadron> viableSquadrons = opposingSquadronChooser.getOpposingSquadrons();
        if (viableSquadrons.size() > 0)
        {
            Collections.shuffle(viableSquadrons);
            return viableSquadrons.get(0);
        }
        return null;
    }
    
    private List<IFlight> buildFlight(FlightTypes opposingFlightType, Squadron opposingSquadron) throws PWCGException
    {
        FlightFactory flightFactory = new FlightFactory(campaign);
        List<IFlight> flights = flightFactory.buildFlight(mission, opposingSquadron, opposingFlightType, NecessaryFlightType.OPPOSING_FLIGHT);
        return flights;
    }
}
