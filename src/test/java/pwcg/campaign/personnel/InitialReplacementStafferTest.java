package pwcg.campaign.personnel;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;

@ExtendWith(MockitoExtension.class)
public class InitialReplacementStafferTest
{
    
    @Test
    public void generateReplacementsTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_MOSCOW);

        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService service : armedServices)
        {
            InitialReplacementStaffer initialReplacementStaffer = new InitialReplacementStaffer(campaign, service);
            SquadronMembers squadronMembers = initialReplacementStaffer.staffReplacementsForService();
            
            assert(squadronMembers.getSquadronMemberCollection().size() == InitialReplacementStaffer.NUM_INITIAL_REPLACEMENTS);
            for (SquadronMember replacement : squadronMembers.getSquadronMemberCollection().values())
            {
                assert(replacement.getSquadronId() == Squadron.REPLACEMENT);
            }
        }
    }

}
