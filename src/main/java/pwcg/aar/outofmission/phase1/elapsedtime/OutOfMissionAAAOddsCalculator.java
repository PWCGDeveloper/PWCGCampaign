package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronRoleSet;
import pwcg.product.bos.country.BoSServiceManager;
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
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronMember.getSquadronId());
        SquadronRoleSet squadronRoles = squadron.getSquadronRoles();
        Role roleThisMission = squadronRoles.selectRoleForMission(campaign.getDate());
        
        int shotDownOdds = 5;
        if (roleThisMission == Role.ROLE_ATTACK)
        {
            shotDownOdds += 12;
        }
        else if (roleThisMission == Role.ROLE_DIVE_BOMB)
        {
            shotDownOdds += 7;
        }
        else if (roleThisMission == Role.ROLE_ARTILLERY_SPOT)
        {
            shotDownOdds += 7;
        }
        else if (roleThisMission == Role.ROLE_RECON)
        {
            shotDownOdds += 7;
        }
        else if (roleThisMission == Role.ROLE_BOMB)
        {
            shotDownOdds += 7;
        }
        else if (roleThisMission == Role.ROLE_STRAT_BOMB)
        {
            shotDownOdds += 7;
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
        	shotDownOdds += 17;
        }
        else if (squadronMember.getAiSkillLevel() == AiSkillLevel.COMMON)
        {
        	shotDownOdds += 8;
        }
        else if (squadronMember.getAiSkillLevel() == AiSkillLevel.VETERAN)
        {
            shotDownOdds -= 7;
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
