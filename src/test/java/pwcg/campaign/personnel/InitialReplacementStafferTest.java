package pwcg.campaign.personnel;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class InitialReplacementStafferTest
{
    
    @Test
    public void generateReplacementsTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);

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
