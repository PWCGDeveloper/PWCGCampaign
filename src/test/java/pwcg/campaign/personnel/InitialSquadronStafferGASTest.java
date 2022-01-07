package pwcg.campaign.personnel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class InitialSquadronStafferGASTest
{
    @Test
    public void generatePersonnelWithAcesTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);

        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.JASTA_11_PROFILE.getCompanyId());
        
        InitialCompanyStaffer initialSquadronStaffer = new InitialCompanyStaffer(campaign, squadron);
        CompanyPersonnel squadronPersonnel = initialSquadronStaffer.generatePersonnel();        
        CrewMembers squadronMembers = squadronPersonnel.getCrewMembersWithAces();
        CrewMembers filteredCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronMembers.getCrewMemberCollection(), campaign.getDate());        
        
        assert(filteredCrewMembers.getCrewMemberList().size() == Company.COMPANY_STAFF_SIZE);
    }

}
