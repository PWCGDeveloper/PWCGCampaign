package pwcg.campaign.personnel;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class InitialReplacementStafferTest
{
    
    @Test
    public void generateReplacementsTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);

        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService service : armedServices)
        {
            InitialReplacementStaffer initialReplacementStaffer = new InitialReplacementStaffer(campaign, service);
            CrewMembers squadronMembers = initialReplacementStaffer.staffReplacementsForService();
            
            assert(squadronMembers.getCrewMemberCollection().size() == InitialReplacementStaffer.NUM_INITIAL_REPLACEMENTS);
            for (CrewMember replacement : squadronMembers.getCrewMemberCollection().values())
            {
                assert(replacement.getCompanyId() == Company.REPLACEMENT);
            }
        }
    }

}
