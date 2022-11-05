package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronReducer;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionSquadronRegistry;

public class EscortSquadronSelector
{
    public static Squadron getEscortSquadron(Campaign campaign, Squadron referenceSquadron, Coordinate referenceCoordinate, MissionSquadronRegistry missionSquadronRegistry) throws PWCGException
    {
        List<PwcgRole> escortRole = new ArrayList<>();
        escortRole.add(PwcgRole.ROLE_FIGHTER);
        List<Squadron> inRangeSquadrons =  getSquadron(campaign, referenceSquadron, referenceCoordinate, escortRole);
        List<Squadron> availableSquadrons =  missionSquadronRegistry.removeSquadronsInUse(inRangeSquadrons);
        Squadron selectedSquadron = chooseSquadron(availableSquadrons);
        return selectedSquadron;
    }
    
    public static Squadron getEscortedSquadron(Campaign campaign, Squadron referenceSquadron, Coordinate referenceCoordinate) throws PWCGException
    {
        List<PwcgRole> escortedRoles = new ArrayList<>();
        escortedRoles.add(PwcgRole.ROLE_BOMB);
        escortedRoles.add(PwcgRole.ROLE_ATTACK);

        List<Squadron> inRangeSquadrons = getSquadron(campaign, referenceSquadron, referenceCoordinate, escortedRoles);
        Squadron selectedSquadron = chooseSquadron(inRangeSquadrons);
        return selectedSquadron;
    }

    private static List<Squadron> getSquadron(Campaign campaign, Squadron referenceSquadron, Coordinate referenceCoordinate, List<PwcgRole> acceptableRoles) throws PWCGException 
    {
        Side side = referenceSquadron.determineSide();

        List<Squadron> selectedSquadronsNoPlayer = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, side);

        List<Squadron> inRangeSquadrons = SquadronReducer.sortByProximityOnCurrentMap(campaign.getCampaignMap(), selectedSquadronsNoPlayer, campaign.getDate(), referenceCoordinate);
        return inRangeSquadrons;
    }
    
    private static Squadron chooseSquadron(List<Squadron> squadrons) throws PWCGException 
    {
        if (squadrons.size() > 0)
        {
            Collections.shuffle(squadrons);
            return squadrons.get(0);
        }
        else
        {
            return null;
        }
    }
}
