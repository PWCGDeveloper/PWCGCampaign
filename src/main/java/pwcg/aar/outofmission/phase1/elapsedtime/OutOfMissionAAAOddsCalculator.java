package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronRoleSet;
import pwcg.campaign.ww2.country.BoSServiceManager;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;

public class OutOfMissionAAAOddsCalculator
{
    private Campaign campaign;
    
    public OutOfMissionAAAOddsCalculator (Campaign campaign)
    {
        this.campaign = campaign;
    }
    

    public int oddsShotDownByAAA(SquadronMember squadronMember) throws PWCGException
    {
        int shotDownOdds = intitialOddsBasedOnSquadronRole(squadronMember);
        shotDownOdds = squadronMemberDeathOdds(squadronMember, shotDownOdds);
        return shotDownOdds;
    }
    
    private int intitialOddsBasedOnSquadronRole(SquadronMember squadronMember) throws PWCGException
    {
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(squadronMember.getSquadronId());
        SquadronRoleSet squadronRoles = squadron.getSquadronRoles();
        Role roleThisMission = squadronRoles.selectRoleForMission(campaign.getDate());
        
        int shotDownOdds = 5;
        if (roleThisMission == Role.ROLE_ATTACK)
        {
            shotDownOdds += 20;
        }
        else if (roleThisMission == Role.ROLE_DIVE_BOMB)
        {
            shotDownOdds += 10;
        }
        else if (roleThisMission == Role.ROLE_ARTILLERY_SPOT)
        {
            shotDownOdds += 10;
        }
        else if (roleThisMission == Role.ROLE_RECON)
        {
            shotDownOdds += 10;
        }
        else if (roleThisMission == Role.ROLE_BOMB)
        {
            shotDownOdds += 10;
        }
        else if (roleThisMission == Role.ROLE_STRAT_BOMB)
        {
            shotDownOdds += 10;
        }
        else if (roleThisMission == Role.ROLE_FIGHTER)
        {
            shotDownOdds -= 10;
        }
        else if (roleThisMission == Role.ROLE_TRANSPORT)
        {
            shotDownOdds -= 15;
        }
        
        return shotDownOdds;
    }
    
    private int squadronMemberDeathOdds(SquadronMember squadronMember, int shotDownOdds) throws PWCGException
    {
        if (squadronMember.getAiSkillLevel() == AiSkillLevel.NOVICE)
        {
        	shotDownOdds += 20;
        }
        else if (squadronMember.getAiSkillLevel() == AiSkillLevel.COMMON)
        {
        	shotDownOdds += 10;
        }
        else if (squadronMember.getAiSkillLevel() == AiSkillLevel.VETERAN)
        {
            shotDownOdds -= 5;
        }
        else if (squadronMember.getAiSkillLevel() == AiSkillLevel.ACE)
        {
            shotDownOdds -= 20;
        }
        
        ArmedService service = squadronMember.determineService(campaign.getDate());
        if (service.getServiceId() == BoSServiceManager.VVS)
        {
            shotDownOdds += 20;
        }

        return shotDownOdds;
    }
}
