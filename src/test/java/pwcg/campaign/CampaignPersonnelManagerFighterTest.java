package pwcg.campaign;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCacheBase;

@RunWith(MockitoJUnitRunner.class)
public class CampaignPersonnelManagerFighterTest
{
	@Before
	public void setup() throws PWCGException
	{
        PWCGContextManager.setRoF(true);
	}

    @Test
    public void getSquadronMembersTest () throws PWCGException
    {            	    
        CampaignGeneratorModel generatorModel = CampaignCacheBase.makeCampaignModelForProfile("19170901", 101103);
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();

        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getPlayerPersonnel();
        SquadronMembers squadronMembersNoPlayerNoAces = SquadronMemberFilter.filterActiveAI(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
    	assert (squadronMembersNoPlayerNoAces.getSquadronMemberList().size() < (Squadron.SQUADRON_STAFF_SIZE - 1));
        
        SquadronMembers squadronMembersNoPlayerWithAces = SquadronMemberFilter.filterActiveAIAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        assert (squadronMembersNoPlayerWithAces.getSquadronMemberList().size() == (Squadron.SQUADRON_STAFF_SIZE - 1));

        SquadronMembers squadronMembersWithPlayerWithAces = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        assert (squadronMembersWithPlayerWithAces.getSquadronMemberList().size() == Squadron.SQUADRON_STAFF_SIZE);
    }
}
