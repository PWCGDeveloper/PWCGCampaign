package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.SquadronRoleSet;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRole;
import pwcg.campaign.tank.PwcgRoleCategory;
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
    

    public int oddsShotDownByAAA(CrewMember crewMember) throws PWCGException
    {
        int shotDownOdds = intitialOddsBasedOnSquadronRole(crewMember);
        shotDownOdds = squadronMemberDeathOdds(crewMember, shotDownOdds);
        return shotDownOdds;
    }
    
    private int intitialOddsBasedOnSquadronRole(CrewMember crewMember) throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(crewMember.getCompanyId());
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
    
    private int squadronMemberDeathOdds(CrewMember crewMember, int shotDownOdds) throws PWCGException
    {
        if (crewMember.getAiSkillLevel() == AiSkillLevel.NOVICE)
        {
        	shotDownOdds += 17;
        }
        else if (crewMember.getAiSkillLevel() == AiSkillLevel.COMMON)
        {
        	shotDownOdds += 8;
        }
        else if (crewMember.getAiSkillLevel() == AiSkillLevel.VETERAN)
        {
            shotDownOdds -= 7;
        }
        else if (crewMember.getAiSkillLevel() == AiSkillLevel.ACE)
        {
            shotDownOdds -= 20;
        }
        
        ArmedService service = crewMember.determineService(campaign.getDate());
        if (service.getServiceId() == BoSServiceManager.SVV)
        {
            shotDownOdds += 20;
        }

        return shotDownOdds;
    }
}
