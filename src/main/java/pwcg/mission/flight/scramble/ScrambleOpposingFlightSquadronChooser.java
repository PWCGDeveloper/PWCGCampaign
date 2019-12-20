package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;

public class ScrambleOpposingFlightSquadronChooser
{
    private FlightInformation playerFlightInformation;

    public ScrambleOpposingFlightSquadronChooser(FlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
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
        List<Role> opposingFlightRoles = determineOpposingRoles();
        
        List<Squadron> possibleOpposingSquadsByRole = PWCGContext.getInstance().getSquadronManager().getNearestSquadronsByRole(
                playerFlightInformation.getMission().getCampaign(), 
                playerFlightInformation.getTargetPosition().copy(), 
                1, 
                250000.0, 
                opposingFlightRoles, 
                playerFlightInformation.getSquadron().determineEnemySide(), 
                playerFlightInformation.getCampaign().getDate());

        List<Squadron> viableOpposingSquads = new ArrayList<>();
        for (Squadron squadron : possibleOpposingSquadsByRole)
        {
            if (squadron.isSquadronViable(playerFlightInformation.getCampaign()))
            {
                viableOpposingSquads.add(squadron);
            }
        }
        return viableOpposingSquads;
    }
    
    private List<Role> determineOpposingRoles()
    {
        return new ArrayList<>(Arrays.asList(Role.ROLE_FIGHTER, Role.ROLE_BOMB, Role.ROLE_ATTACK, Role.ROLE_DIVE_BOMB, Role.ROLE_STRAT_BOMB, Role.ROLE_RECON));
    }

    private int determineNumberOfOpposingFlights() 
    {
        int numOpposingFlights = 1 + RandomNumberGenerator.getRandom(3);
        if (playerFlightInformation.getCampaign().isCoop())
        {
            numOpposingFlights = 1;
        }
        return numOpposingFlights;
    }

}
