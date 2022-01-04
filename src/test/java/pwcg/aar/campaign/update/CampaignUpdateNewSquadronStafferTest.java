package pwcg.aar.campaign.update;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyViability;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignUpdateNewSquadronStafferTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void testSquadronAdded() throws PWCGException
    {
        Date newDate = DateUtils.getDateYYYYMMDD("19420801");
        campaign.setDate(newDate);
        
        CampaignUpdateNewCompanyStaffer newSquadronStaffer = new CampaignUpdateNewCompanyStaffer(campaign);
        List<Integer> squadronsAdded = newSquadronStaffer.staffNewCompanies();
        assert(squadronsAdded.size() > 0);
        for (int squadronId : squadronsAdded)
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronId);
            assert(CompanyViability.isCompanyActive(squadron, campaign.getDate()));
            
            CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadronId);
            assert(squadronPersonnel != null);

            CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(),campaign.getDate());
            assert(squadronMembers != null);
            assert(squadronMembers.getActiveCount(campaign.getDate()) >= Company.COMPANY_STAFF_SIZE);
        }
    }
}
