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
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class InitialSquadronStafferGASTest
{
    @Test
    public void generatePersonnelWithAcesTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        
        InitialSquadronStaffer initialSquadronStaffer = new InitialSquadronStaffer(campaign, squadron);
        SquadronPersonnel squadronPersonnel = initialSquadronStaffer.generatePersonnel();        
        SquadronMembers squadronMembers = squadronPersonnel.getSquadronMembersWithAces();
        SquadronMembers filteredSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronMembers.getSquadronMemberCollection(), campaign.getDate());        
        
        assert(filteredSquadronMembers.getSquadronMemberList().size() == Squadron.SQUADRON_STAFF_SIZE);
    }

}
