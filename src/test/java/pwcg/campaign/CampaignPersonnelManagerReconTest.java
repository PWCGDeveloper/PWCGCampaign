package pwcg.campaign;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.CampaignPersonnelFilter;
import pwcg.campaign.personnel.SquadronMemberFilterSpecification;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignPersonnelManagerReconTest
{
    private Campaign campaign;    

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.ESC_103_PROFILE);
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
        Assertions.assertTrue (squadronMembersNoPlayerNoAces.size() == (Squadron.SQUADRON_STAFF_SIZE - 2));
        
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(true);        
        filterSpecification.setIncludeAI(true);  
        Map<Integer, SquadronMember> squadronMembersNoPlayerWithAces = filter.getFilteredSquadronMembers(filterSpecification);
        Assertions.assertTrue (squadronMembersNoPlayerWithAces.size() == (Squadron.SQUADRON_STAFF_SIZE - 1));

        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);        
        filterSpecification.setIncludeAI(true);  
        Map<Integer, SquadronMember> squadronMembersWithPlayerWithAces = filter.getFilteredSquadronMembers(filterSpecification);
        Assertions.assertTrue (squadronMembersWithPlayerWithAces.size() == Squadron.SQUADRON_STAFF_SIZE);
    }
}

