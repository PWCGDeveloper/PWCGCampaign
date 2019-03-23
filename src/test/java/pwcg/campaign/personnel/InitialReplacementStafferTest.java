package pwcg.campaign.personnel;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class InitialReplacementStafferTest
{
    
    @Test
    public void generateReplacementsTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);

        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllArmedServices();
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
