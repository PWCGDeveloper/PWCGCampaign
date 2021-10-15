package pwcg.campaign;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignPersonnelManagerFighterTest
{
	@BeforeAll
	public void setupSuite() throws PWCGException
	{
        PWCGContext.setProduct(PWCGProduct.FC);
	}

    @Test
    public void getSquadronMembersTest () throws PWCGException
    {            	    
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);

        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        SquadronMembers squadronMembersNoPlayerNoAces = SquadronMemberFilter.filterActiveAI(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
    	assert (squadronMembersNoPlayerNoAces.getSquadronMemberList().size() < (Squadron.SQUADRON_STAFF_SIZE - 1));
        
        SquadronMembers squadronMembersNoPlayerWithAces = SquadronMemberFilter.filterActiveAIAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        assert (squadronMembersNoPlayerWithAces.getSquadronMemberList().size() == (Squadron.SQUADRON_STAFF_SIZE - 1));

        SquadronMembers squadronMembersWithPlayerWithAces = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        assert (squadronMembersWithPlayerWithAces.getSquadronMemberList().size() == Squadron.SQUADRON_STAFF_SIZE);
    }
}
