package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronRoleSet;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSServiceManager;

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
        PwcgRole roleThisMission = squadronRoles.selectRoleForMission(campaign.getDate());
        
        int shotDownOdds = 5;
        if (roleThisMission.isRoleCategory(PwcgRoleCategory.ATTACK))
        {
            shotDownOdds += 10;
        }
        if (roleThisMission.isRoleCategory(PwcgRoleCategory.BOMBER))
        {
            shotDownOdds += 7;
        }
        if (roleThisMission.isRoleCategory(PwcgRoleCategory.RECON))
        {
            shotDownOdds += 7;
        }
        if (roleThisMission.isRoleCategory(PwcgRoleCategory.FIGHTER))
        {
            shotDownOdds -= 10;
        }
        if (roleThisMission.isRoleCategory(PwcgRoleCategory.TRANSPORT))
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
