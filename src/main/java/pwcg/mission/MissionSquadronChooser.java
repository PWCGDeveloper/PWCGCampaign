package pwcg.mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;

public class MissionSquadronChooser
{
    private Map<Integer, Squadron> squadronsInUse = new HashMap<>();
    
    public void registerSquadronInUse(Squadron squadron)
    {
        squadronsInUse.put(squadron.getSquadronId(), squadron);
    }
    
    public void deregisterSquadronInUse(Squadron squadron)
    {
        squadronsInUse.remove(squadron.getSquadronId());
    }
    
    public boolean isSquadronInUse(int squadronId)
    {
        return squadronsInUse.containsKey(squadronId);
    }
    
    public Squadron determineSquadronToBeEscorted(Campaign campaign, Squadron escortSquadron) throws PWCGException
    {
        Squadron escortedSquadron = determineSquadronForRoleToBeEscorted(campaign, escortSquadron, Role.ROLE_BOMB);
        if (escortedSquadron == null)
        {
            escortedSquadron = determineSquadronForRoleToBeEscorted(campaign, escortSquadron, Role.ROLE_ATTACK);
        }

        if (escortedSquadron == null)
        {
            throw new PWCGMissionGenerationException ("Escort mission with no viable squadrons to be escorted - please create another mission");
        }
        
        return escortedSquadron;
    }
    
    public Squadron determineSquadronForRoleToBeEscorted(Campaign campaign, Squadron escortSquadron, Role role) throws PWCGException
    {
        List<Role> bomberRole = new ArrayList<Role>(Arrays.asList(role));
        Side friendlySide = escortSquadron.determineSide();
        List<Squadron> squadronsToExclude = new ArrayList<>(squadronsInUse.values());
        Squadron escortedSquadron = PWCGContext.getInstance().getSquadronManager().getSingleViableAiSquadronByRoleAndSideAndCurrentMap(campaign, bomberRole, friendlySide, squadronsToExclude);
        return escortedSquadron;
    }


    public Squadron getEscortSquadron(Campaign campaign, Side friendlySide) throws PWCGException
    {
        List<Squadron> squadronsToExclude = new ArrayList<>(squadronsInUse.values());
        List<Role> escortRole = new ArrayList<>();
        escortRole.add(Role.ROLE_FIGHTER);
        Squadron friendlyFighterSquadron = PWCGContext.getInstance().getSquadronManager().getSingleViableAiSquadronByRoleAndSideAndCurrentMap(campaign, escortRole, friendlySide, squadronsToExclude);
        return friendlyFighterSquadron;
    }
    
    
    public List<Squadron> determineParticipatingSquadrons(Mission mission) throws PWCGException
    {
        AiSquadronIncluder aiSquadronIncluder = new AiSquadronIncluder(mission);
        List<Squadron> aiSquadronsForMission = aiSquadronIncluder.decideSquadronsForMission();
        
        return aiSquadronsForMission;
    }    
}
