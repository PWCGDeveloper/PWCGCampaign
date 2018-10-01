package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;

public class OpposingFlightSquadronChooser
{
    private Mission mission;
    private Coordinate targetCoordinates;
    private List<Role> opposingFlightRoles;

    public OpposingFlightSquadronChooser(Mission mission, Coordinate targetCoordinates, List<Role> opposingFlightRoles)
    {
        this.mission = mission;
        this.targetCoordinates = targetCoordinates;
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
        List<Squadron> selectedOpposingSquads = new ArrayList<>();;
        HashSet<Integer> alreadyPicked = new HashSet<>();
        while (selectedOpposingSquads.size() < numSquadronsToGet)
        {
            int index= RandomNumberGenerator.getRandom(viableOpposingSquads.size());
            if (!selectedOpposingSquads.contains(index))
            {
                selectedOpposingSquads.add(viableOpposingSquads.get(index));
                alreadyPicked.add(index);
            }
        }
        return selectedOpposingSquads;
    }

    private List<Squadron> getViableOpposingSquadrons() throws PWCGException
    {
        List<Squadron> possibleOpposingSquadsByRole = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(
                mission.getCampaign(), targetCoordinates.copy(), 1, 250000.0, opposingFlightRoles, mission.getCampaign().determineCountry().getSide().getOppositeSide(), mission.getCampaign().getDate());

        List<Squadron> viableOpposingSquads = new ArrayList<>();
        for (Squadron squadron : possibleOpposingSquadsByRole)
        {
            if (squadron.isSquadronViable(mission.getCampaign()))
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
