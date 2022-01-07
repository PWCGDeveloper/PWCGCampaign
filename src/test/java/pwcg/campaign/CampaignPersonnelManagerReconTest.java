package pwcg.campaign;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CampaignPersonnelFilter;
import pwcg.campaign.personnel.CrewMemberFilterSpecification;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignPersonnelManagerReconTest
{
    private Campaign campaign;    

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
    }

    @Test
    public void getCrewMembersTest () throws PWCGException
    {                               
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();

        CrewMembers squadronMembers = campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.ESC_103_PROFILE.getCompanyId()).getCrewMembersWithAces();
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembers.getCrewMemberCollection());

        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(false);  
        filterSpecification.setIncludeAI(true);  
        filterSpecification.setSpecifySquadron(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        Map<Integer, CrewMember> squadronMembersNoPlayerNoAces = filter.getFilteredCrewMembers(filterSpecification);
        Assertions.assertTrue (squadronMembersNoPlayerNoAces.size() == (Company.COMPANY_STAFF_SIZE - 2));
        
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(true);        
        filterSpecification.setIncludeAI(true);  
        Map<Integer, CrewMember> squadronMembersNoPlayerWithAces = filter.getFilteredCrewMembers(filterSpecification);
        Assertions.assertTrue (squadronMembersNoPlayerWithAces.size() == (Company.COMPANY_STAFF_SIZE - 1));

        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);        
        filterSpecification.setIncludeAI(true);  
        Map<Integer, CrewMember> squadronMembersWithPlayerWithAces = filter.getFilteredCrewMembers(filterSpecification);
        Assertions.assertTrue (squadronMembersWithPlayerWithAces.size() == Company.COMPANY_STAFF_SIZE);
    }
}

