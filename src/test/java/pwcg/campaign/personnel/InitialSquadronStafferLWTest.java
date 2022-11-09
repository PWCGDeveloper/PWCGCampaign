package pwcg.campaign.personnel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class InitialSquadronStafferLWTest
{
    @Test
    public void generateFighterPersonnelTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_MOSCOW);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
        
        InitialSquadronStaffer initialSquadronStaffer = new InitialSquadronStaffer(campaign, squadron);
        SquadronPersonnel squadronPersonnel = initialSquadronStaffer.generatePersonnel();        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        

        assert(squadronMembers.getSquadronMemberList().size() == Squadron.SQUADRON_STAFF_SIZE);
    }
}
