package pwcg.mission.flight.opposing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class ScrambleOpposingFlightSquadronChooser
{
    private Campaign campaign;
    private Side opposingSquadronSide;
    
    public ScrambleOpposingFlightSquadronChooser(Campaign campaign, Side opposingSquadronSide)
    {
        this.campaign = campaign;
        this.opposingSquadronSide = opposingSquadronSide;
    }

    public Squadron getOpposingSquadrons() throws PWCGException
    {
        List<Squadron> viableOpposingSquads = getViableOpposingSquadrons();
        return selectOpposingSquadrons(viableOpposingSquads);            
    }

    private Squadron selectOpposingSquadrons(List<Squadron> viableOpposingSquads)
    {
        Collections.shuffle(viableOpposingSquads);
        return viableOpposingSquads.get(0);
    }

    private List<Squadron> getViableOpposingSquadrons() throws PWCGException
    {
        List<Role> opposingFlightRoles = determineOpposingRoles();
        List<Squadron> possibleOpposingSquadsByRole = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(
                campaign, opposingFlightRoles, opposingSquadronSide);

        List<Squadron> viableOpposingSquads = new ArrayList<>();
        for (Squadron squadron : possibleOpposingSquadsByRole)
        {
            viableOpposingSquads.add(squadron);
        }
        return viableOpposingSquads;
    }
    
    private List<Role> determineOpposingRoles()
    {
        return new ArrayList<>(Arrays.asList(Role.ROLE_BOMB, Role.ROLE_ATTACK, Role.ROLE_DIVE_BOMB));
    }
}
