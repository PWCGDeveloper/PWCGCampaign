package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.resupply.ServiceResupplyNeed;
import pwcg.campaign.resupply.SquadronNeedFactory;
import pwcg.campaign.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTransferNeedTest
{
    private Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JASTA_11_PROFILE);
     }

    @Test
    public void testTransfersWithNoSquadronMembers() throws PWCGException
    {
        deactivateSquadronPersonnel();
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.PERSONNEL);
        ServiceResupplyNeed serviceTransferNeed = new ServiceResupplyNeed(campaign, 50101, squadronNeedFactory);
        serviceTransferNeed.determineResupplyNeed();
        
        assert (serviceTransferNeed.hasNeedySquadron() == true);
        
        boolean jasta12Needs = false;
        while (serviceTransferNeed.hasNeedySquadron())
        {
            ISquadronNeed selectedSquadronNeed = serviceTransferNeed.chooseNeedySquadron();
            if (selectedSquadronNeed.getSquadronId() == 501012)
            {
                jasta12Needs = true;
                break;
            }
        }
        assert (jasta12Needs);
    }
    

    private void deactivateSquadronPersonnel() throws PWCGException
    {
        SquadronMembers jasta12SquadronMembers = SquadronMemberFilter.filterActiveAI(campaign.getPersonnelManager().getSquadronPersonnel(501012).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        int numInactivated = 0;
        for (SquadronMember squadronMember : jasta12SquadronMembers.getSquadronMemberList())
        {
            if (!squadronMember.isPlayer())
            {
                squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
                Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 9);
                squadronMember.setInactiveDate(inactiveDate);
                ++numInactivated;
                
                System.out.println(squadronMember.getName() + " deactivated for test");
            }
            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
}
