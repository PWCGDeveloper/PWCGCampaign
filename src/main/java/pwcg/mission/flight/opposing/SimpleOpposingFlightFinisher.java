package pwcg.mission.flight.opposing;

import java.util.ArrayList;
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

    public List<IFlight> createOpposingFlights(FlightTypes opposingFlightType, List<Role> opposingFlightRoles) throws PWCGException 
    {
        Squadron opposingSquadron = determineOpposingSquadron(opposingFlightRoles);
        if (opposingSquadron != null)
        {
            return buildFlight(opposingFlightType, opposingSquadron);
        }
        return new ArrayList<>();
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
    
    private List<IFlight> buildFlight(FlightTypes opposingFlightType, Squadron opposingSquadron) throws PWCGException
    {
        FlightFactory flightFactory = new FlightFactory(campaign);
        List<IFlight> flights = flightFactory.buildFlight(mission, opposingSquadron, opposingFlightType, NecessaryFlightType.OPPOSING_FLIGHT);
        return flights;
    }
}
