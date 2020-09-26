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
import pwcg.mission.flight.IFlightInformation;

public class MissionSquadronChooser
{
    private Map<Integer, Squadron> squadronsInUse = new HashMap<>();
    
    public void registerSquadronInUse(Squadron squadron)
    {
        squadronsInUse.put(squadron.getSquadronId(), squadron);
    }
    
    public boolean isSquadronInUse(int squadronId)
    {
        return squadronsInUse.containsKey(squadronId);
    }
    
    public Squadron determineSquadronToBeEscorted(Campaign campaign, Squadron escortSquadron) throws PWCGException
    {
        List<Role> bomberRole = new ArrayList<Role>(Arrays.asList(Role.ROLE_BOMB));
        Side friendlySide = escortSquadron.determineSide();
        List<Squadron> squadronsToExclude = new ArrayList<>(squadronsInUse.values());
        Squadron friendlyBombSquadron = PWCGContext.getInstance().getSquadronManager().getSingleViableAiSquadronByRoleAndSideAndCurrentMap(campaign, bomberRole, friendlySide, squadronsToExclude);

        if (friendlyBombSquadron == null)
        {
            throw new PWCGMissionGenerationException ("Escort mission with no viable squadrons to be escorted - please create another mission");
        }
        
        return friendlyBombSquadron;
    }


    public Squadron getEscortSquadron(Campaign campaign, IFlightInformation playerFlightInformation, List<Role> escortRole, Side friendlySide) throws PWCGException
    {
        List<Squadron> squadronsToExclude = new ArrayList<>(squadronsInUse.values());
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
