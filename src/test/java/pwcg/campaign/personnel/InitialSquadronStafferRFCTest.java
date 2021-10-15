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
public class InitialSquadronStafferRFCTest
{
    @Test
    public void generateReconPersonnelTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RFC_2_PROFILE);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.RFC_2_PROFILE.getSquadronId());
        
        InitialSquadronStaffer initialSquadronStaffer = new InitialSquadronStaffer(campaign, squadron);
        SquadronPersonnel squadronPersonnel = initialSquadronStaffer.generatePersonnel();        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        
        assert(squadronMembers.getSquadronMemberList().size() == Squadron.SQUADRON_STAFF_SIZE);
    }
}
