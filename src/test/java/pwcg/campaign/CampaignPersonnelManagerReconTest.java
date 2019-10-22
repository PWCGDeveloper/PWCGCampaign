package pwcg.campaign;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.CampaignPersonnelFilter;
import pwcg.campaign.personnel.SquadronMemberFilterSpecification;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CampaignPersonnelManagerReconTest
{
	private Campaign campaign;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
    }

    @Test
    public void getSquadronMembersTest () throws PWCGException
    {                               
        SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();

        SquadronMembers squadronMembers = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.ESC_103_PROFILE.getSquadronId()).getSquadronMembersWithAces();
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembers.getSquadronMemberCollection());

        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(false);  
        filterSpecification.setIncludeAI(true);  
        filterSpecification.setSpecifySquadron(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        Map<Integer, SquadronMember> squadronMembersNoPlayerNoAces = filter.getFilteredSquadronMembers(filterSpecification);
        assert (squadronMembersNoPlayerNoAces.size() == (Squadron.SQUADRON_STAFF_SIZE - 2));
        
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(true);        
        filterSpecification.setIncludeAI(true);  
        Map<Integer, SquadronMember> squadronMembersNoPlayerWithAces = filter.getFilteredSquadronMembers(filterSpecification);
        assert (squadronMembersNoPlayerWithAces.size() == (Squadron.SQUADRON_STAFF_SIZE - 1));

        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);        
        filterSpecification.setIncludeAI(true);  
        Map<Integer, SquadronMember> squadronMembersWithPlayerWithAces = filter.getFilteredSquadronMembers(filterSpecification);
        assert (squadronMembersWithPlayerWithAces.size() == Squadron.SQUADRON_STAFF_SIZE);
    }
}

