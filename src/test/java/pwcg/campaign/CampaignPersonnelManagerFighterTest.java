package pwcg.campaign;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CampaignPersonnelManagerFighterTest
{
	@Before
	public void setup() throws PWCGException
	{
        PWCGContext.setProduct(PWCGProduct.ROF);
	}

    @Test
    public void getSquadronMembersTest () throws PWCGException
    {            	    
        Campaign campaign = CampaignCache.makeCampaignForceCreation(SquadrontTestProfile.ESC_103_PROFILE);

        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(101103);
        SquadronMembers squadronMembersNoPlayerNoAces = SquadronMemberFilter.filterActiveAI(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
    	assert (squadronMembersNoPlayerNoAces.getSquadronMemberList().size() < (Squadron.SQUADRON_STAFF_SIZE - 1));
        
        SquadronMembers squadronMembersNoPlayerWithAces = SquadronMemberFilter.filterActiveAIAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        assert (squadronMembersNoPlayerWithAces.getSquadronMemberList().size() == (Squadron.SQUADRON_STAFF_SIZE - 1));

        SquadronMembers squadronMembersWithPlayerWithAces = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        assert (squadronMembersWithPlayerWithAces.getSquadronMemberList().size() == Squadron.SQUADRON_STAFF_SIZE);
    }
}
