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
public class InitialSquadronStafferRFCTest
{
    @Test
    public void generateReconPersonnelTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RFC_2_PROFILE);

        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.RFC_2_PROFILE.getCompanyId());
        
        InitialCompanyStaffer initialSquadronStaffer = new InitialCompanyStaffer(campaign, squadron);
        CompanyPersonnel squadronPersonnel = initialSquadronStaffer.generatePersonnel();        
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        
        assert(squadronMembers.getCrewMemberList().size() == Company.COMPANY_STAFF_SIZE);
    }
}
