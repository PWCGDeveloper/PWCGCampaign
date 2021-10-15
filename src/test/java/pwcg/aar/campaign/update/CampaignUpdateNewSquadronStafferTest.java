package pwcg.aar.campaign.update;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronViability;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignUpdateNewSquadronStafferTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void testSquadronAdded() throws PWCGException
    {
        Date newDate = DateUtils.getDateYYYYMMDD("19420801");
        campaign.setDate(newDate);
        
        CampaignUpdateNewSquadronStaffer newSquadronStaffer = new CampaignUpdateNewSquadronStaffer(campaign);
        List<Integer> squadronsAdded = newSquadronStaffer.staffNewSquadrons();
        assert(squadronsAdded.size() > 0);
        for (int squadronId : squadronsAdded)
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            assert(SquadronViability.isSquadronActive(squadron, campaign.getDate()));
            
            SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);
            assert(squadronPersonnel != null);

            SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(),campaign.getDate());
            assert(squadronMembers != null);
            assert(squadronMembers.getActiveCount(campaign.getDate()) >= Squadron.SQUADRON_STAFF_SIZE);
        }
    }
}
