package pwcg.campaign;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.CampaignPersonnelFilter;
import pwcg.campaign.personnel.SquadronMemberFilterSpecification;
import pwcg.campaign.squadmember.SquadronMember;
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

        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
        filterSpecification.setDate(campaign.getDate());

        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(
                campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId()).getActiveSquadronMembersWithAces().getSquadronMembers());
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludePlayer(false);
        filterSpecification.setSpecifySquadron(campaign.getSquadronId());

        Map<Integer, SquadronMember> squadronMembersNoPlayerNoAces = filter.getFilteredSquadronMembers(filterSpecification);
    	assert (squadronMembersNoPlayerNoAces.size() < (Squadron.SQUADRON_STAFF_SIZE - 1));
        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludePlayer(false);
        Map<Integer, SquadronMember> squadronMembersNoPlayerWithAces = filter.getFilteredSquadronMembers(filterSpecification);
        assert (squadronMembersNoPlayerWithAces.size() == (Squadron.SQUADRON_STAFF_SIZE - 1));

        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludePlayer(true);
        Map<Integer, SquadronMember> squadronMembersWithPlayerWithAces = filter.getFilteredSquadronMembers(filterSpecification);
        assert (squadronMembersWithPlayerWithAces.size() == Squadron.SQUADRON_STAFF_SIZE);
    }
}
