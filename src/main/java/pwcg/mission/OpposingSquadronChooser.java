package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlightInformation;

public class OpposingSquadronChooser
{
    private IFlightInformation playerFlightInformation;
    private List<Role> opposingRoles = new ArrayList<>();
    private int numberOfOpposingFlights = 1;

    public OpposingSquadronChooser(IFlightInformation playerFlightInformation, List<Role> opposingRoles, int numberOfOpposingFlights)
    {
        this.playerFlightInformation = playerFlightInformation;
        this.opposingRoles = opposingRoles;
        this.numberOfOpposingFlights = numberOfOpposingFlights;
    }

    public List<Squadron> getOpposingSquadrons() throws PWCGException
    {
        List<Squadron> viableOpposingSquads = getViableOpposingSquadrons();        
        if (viableOpposingSquads.size() <= numberOfOpposingFlights)
        {
            return viableOpposingSquads;
        }
        else
        {
            return selectOpposingSquadrons(viableOpposingSquads);            
        }
    }

    private List<Squadron> selectOpposingSquadrons(List<Squadron> viableOpposingSquads)
    {
        Map<Integer, Squadron> selectedOpposingSquads = new HashMap<>();
        HashSet<Integer> alreadyPicked = new HashSet<>();
        while (selectedOpposingSquads.size() < numberOfOpposingFlights)
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
        List<Squadron> possibleOpposingSquadsByRole = PWCGContext.getInstance().getSquadronManager().getNearestSquadronsByRole(
                playerFlightInformation.getMission().getCampaign(), 
                playerFlightInformation.getTargetPosition().copy(), 
                1, 
                250000.0, 
                opposingRoles, 
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
}
