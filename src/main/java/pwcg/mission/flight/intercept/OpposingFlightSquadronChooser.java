package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;

public class OpposingFlightSquadronChooser
{
    private FlightInformation flightInformation;
    private List<Role> opposingFlightRoles;

    public OpposingFlightSquadronChooser(FlightInformation flightInformation, List<Role> opposingFlightRoles)
    {
        this.flightInformation = flightInformation;
        this.opposingFlightRoles = opposingFlightRoles;
    }

    public List<Squadron> getOpposingSquadrons() throws PWCGException
    {
        List<Squadron> viableOpposingSquads = getViableOpposingSquadrons();
        int numSquadronsToGet = determineNumberOfOpposingFlights();
        
        if (viableOpposingSquads.size() <= numSquadronsToGet)
        {
            return viableOpposingSquads;
        }
        else
        {
            return selectOpposingSquadrons(viableOpposingSquads, numSquadronsToGet);            
        }
    }

    private List<Squadron> selectOpposingSquadrons(List<Squadron> viableOpposingSquads, int numSquadronsToGet)
    {
        Map<Integer, Squadron> selectedOpposingSquads = new HashMap<>();
        HashSet<Integer> alreadyPicked = new HashSet<>();
        while (selectedOpposingSquads.size() < numSquadronsToGet)
        {
            int index= RandomNumberGenerator.getRandom(viableOpposingSquads.size());
            Squadron opposingSquadron = viableOpposingSquads.get(index);
            if (!selectedOpposingSquads.containsKey(opposingSquadron.getSquadronId()))
            {
                selectedOpposingSquads.put(opposingSquadron.getSquadronId(), opposingSquadron);
                alreadyPicked.add(index);
            }
        }
        return new ArrayList<>(selectedOpposingSquads.values());
    }

    private List<Squadron> getViableOpposingSquadrons() throws PWCGException
    {
        List<Squadron> possibleOpposingSquadsByRole = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(
                flightInformation.getMission().getCampaign(), 
                flightInformation.getTargetCoords().copy(), 
                1, 
                250000.0, 
                opposingFlightRoles, 
                flightInformation.getSquadron().determineEnemySide(), 
                flightInformation.getCampaign().getDate());

        List<Squadron> viableOpposingSquads = new ArrayList<>();
        for (Squadron squadron : possibleOpposingSquadsByRole)
        {
            if (squadron.isSquadronViable(flightInformation.getCampaign()))
            {
                viableOpposingSquads.add(squadron);
            }
        }
        return viableOpposingSquads;
    }
    
    private int determineNumberOfOpposingFlights() 
    {
        int numOpposingFlights = 1 + RandomNumberGenerator.getRandom(3);
        return numOpposingFlights;
    }

}
