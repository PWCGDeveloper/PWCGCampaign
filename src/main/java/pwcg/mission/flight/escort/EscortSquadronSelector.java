package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronReducer;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionSquadronRegistry;

public class EscortSquadronSelector
{
    public static Squadron getEscortSquadron(Campaign campaign, Squadron referenceSquadron, MissionSquadronRegistry missionSquadronRegistry) throws PWCGException
    {
        List<Role> escortRole = new ArrayList<>();
        escortRole.add(Role.ROLE_FIGHTER);
        List<Squadron> inRangeSquadrons =  getSquadron(campaign, referenceSquadron, escortRole);
        List<Squadron> availableSquadrons =  missionSquadronRegistry.removeSquadronsInUse(inRangeSquadrons);
        Squadron selectedSquadron = chooseSquadron(availableSquadrons);
        return selectedSquadron;
    }
    
    public static Squadron getEscortedSquadron(Campaign campaign, Squadron referenceSquadron) throws PWCGException
    {
        List<Role> escortedRoles = new ArrayList<>();
        escortedRoles.add(Role.ROLE_BOMB);
        escortedRoles.add(Role.ROLE_ATTACK);

        List<Squadron> inRangeSquadrons = getSquadron(campaign, referenceSquadron, escortedRoles);
        Squadron selectedSquadron = chooseSquadron(inRangeSquadrons);
        return selectedSquadron;
    }

    private static List<Squadron> getSquadron(Campaign campaign, Squadron referenceSquadron, List<Role> acceptableRoles) throws PWCGException 
    {
        Side side = referenceSquadron.determineSide();
        Coordinate squadronAirfieldPosition = referenceSquadron.determineCurrentPosition(campaign.getDate());

        List<Squadron> selectedSquadronsNoPlayer = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, side);

        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int squadronSearchRadius = productSpecific.getLargeMissionRadius();

        List<Squadron> inRangeSquadrons = SquadronReducer.reduceToProximityOnCurrentMap(selectedSquadronsNoPlayer, campaign.getDate(), squadronAirfieldPosition, squadronSearchRadius);
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
