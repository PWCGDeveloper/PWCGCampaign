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
import pwcg.product.fc.country.FCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTransferNeedTest
{
    private Campaign campaign;
    private static final int JASTA_16 = 401016;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);
     }

    @Test
    public void testTransfersWithNoSquadronMembers() throws PWCGException
    {
        deactivateSquadronPersonnel();
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.PERSONNEL);
        ServiceResupplyNeed serviceTransferNeed = new ServiceResupplyNeed(campaign, FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE, squadronNeedFactory);
        serviceTransferNeed.determineResupplyNeed();
        
        assert (serviceTransferNeed.hasNeedySquadron() == true);
        
        boolean jasta16Needs = false;
        while (serviceTransferNeed.hasNeedySquadron())
        {
            ISquadronNeed selectedSquadronNeed = serviceTransferNeed.chooseNeedySquadron();
            if (selectedSquadronNeed.getSquadronId() == JASTA_16)
            {
                jasta16Needs = true;
                break;
            }
        }
        assert (jasta16Needs);
    }
    

    private void deactivateSquadronPersonnel() throws PWCGException
    {
        SquadronMembers jasta16SquadronMembers = SquadronMemberFilter.filterActiveAI(campaign.getPersonnelManager().getSquadronPersonnel(JASTA_16).getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        int numInactivated = 0;
        for (SquadronMember squadronMember : jasta16SquadronMembers.getSquadronMemberList())
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
