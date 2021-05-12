package pwcg.mission.flight.opposing;

import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.OpposingSquadronChooser;
import pwcg.mission.flight.FlightFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;

public class SimpleOpposingFlightFinisher
{
    private Campaign campaign;
    private Mission mission;
    private Squadron playerSquadron;
    
    SimpleOpposingFlightFinisher (Campaign campaign, Mission mission, Squadron playerSquadron)
    {
        this.campaign = campaign;
        this.mission = mission;
        this.playerSquadron = playerSquadron;
    }

    public IFlight createOpposingFlights(FlightTypes opposingFlightType, List<Role> opposingFlightRoles) throws PWCGException 
    {
        Squadron opposingSquadron = determineOpposingSquadron(opposingFlightRoles);
        if (opposingSquadron != null)
        {
            IFlight flight = buildFlight(opposingFlightType, opposingSquadron);
            if (flight != null)
            {
                return flight;
            }
        }
        return null;
    }

    private Squadron determineOpposingSquadron(List<Role> opposingFlightRoles) throws PWCGException
    {
        OpposingSquadronChooser opposingSquadronChooser = new OpposingSquadronChooser(campaign, opposingFlightRoles, playerSquadron.determineEnemySide(), 1);
        List<Squadron> viableSquadrons = opposingSquadronChooser.getOpposingSquadrons();
        if (viableSquadrons.size() > 0)
        {
            Collections.shuffle(viableSquadrons);
            return viableSquadrons.get(0);
        }
        return null;
    }
    
    private IFlight buildFlight(FlightTypes opposingFlightType, Squadron opposingSquadron) throws PWCGException
    {
        FlightFactory flightFactory = new FlightFactory(campaign);
        IFlight flight = flightFactory.buildFlight(mission, opposingSquadron, opposingFlightType, NecessaryFlightType.OPPOSING_FLIGHT);
        return flight;
    }
}
