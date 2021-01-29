package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class OpposingSquadronChooser
{
    private Campaign campaign;
    private List<Role> opposingRoles = new ArrayList<>();
    private Side opposingSide;
    private int numberOfOpposingFlights = 1;

    public OpposingSquadronChooser(Campaign campaign, List<Role> opposingRoles, Side opposingSide, int numberOfOpposingFlights)
    {
        this.campaign = campaign;
        this.opposingRoles = opposingRoles;
        this.opposingSide = opposingSide;
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
        List<Squadron> viableOpposingSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, opposingRoles, opposingSide);
        return viableOpposingSquads;
    }
}
