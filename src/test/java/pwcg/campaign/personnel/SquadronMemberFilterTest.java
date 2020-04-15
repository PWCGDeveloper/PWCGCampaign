package pwcg.campaign.personnel;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronMemberPicker;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class SquadronMemberFilterTest
{
    private Campaign campaign;
    private Map<Integer, SquadronMember> woundedSquadronMembers = new HashMap<>();
    private Map<Integer, SquadronMember> maimedSquadronMembers = new HashMap<>();
    private Map<Integer, SquadronMember> capturedSquadronMembers = new HashMap<>();
    private Map<Integer, SquadronMember> deadSquadronMembers = new HashMap<>();
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        
        while (woundedSquadronMembers.size() < 5)
        {
            SquadronMember squadronMember = SquadronMemberPicker.pickNonAceCampaignMember(campaign, SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_WOUNDED, campaign.getDate(), DateUtils.advanceTimeDays(campaign.getDate(), 10));
            if (!campaignMemberUsed(squadronMember.getSerialNumber()))
            {
                woundedSquadronMembers.put(squadronMember.getSerialNumber(), squadronMember);
            }
        }
        
        while (maimedSquadronMembers.size() < 4)
        {
            SquadronMember squadronMember = SquadronMemberPicker.pickNonAceCampaignMember(campaign, SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), null);
            if (!campaignMemberUsed(squadronMember.getSerialNumber()))
            {
                maimedSquadronMembers.put(squadronMember.getSerialNumber(), squadronMember);
            }
        }
        
        while (deadSquadronMembers.size() < 3)
        {
            SquadronMember squadronMember = SquadronMemberPicker.pickNonAceCampaignMember(campaign, SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
            if (!campaignMemberUsed(squadronMember.getSerialNumber()))
            {
                deadSquadronMembers.put(squadronMember.getSerialNumber(), squadronMember);
            }
        }
        
        while (capturedSquadronMembers.size() < 2)
        {
            SquadronMember squadronMember = SquadronMemberPicker.pickNonAceCampaignMember(campaign, SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
            if (!campaignMemberUsed(squadronMember.getSerialNumber()))
            {
                capturedSquadronMembers.put(squadronMember.getSerialNumber(), squadronMember);
            }
        }
    }
    
    private boolean campaignMemberUsed(int serialNumber)
    {
        if (woundedSquadronMembers.containsKey(serialNumber))
        {
            return true;
        }
        
        if (maimedSquadronMembers.containsKey(serialNumber))
        {
            return true;
        }
        
        if (capturedSquadronMembers.containsKey(serialNumber))
        {
            return true;
        }

        if (deadSquadronMembers.containsKey(serialNumber))
        {
            return true;
        }
        
        return false;
    }
    
    @Test
    public void validateFilters() throws PWCGException
    {
        Map<Integer, SquadronMember> allCampaignMembers = campaign.getPersonnelManager().getAllCampaignMembers();
        int numAces = campaign.getPersonnelManager().getCampaignAces().getCampaignAces().size();
        int numPlayers = SquadronMemberFilter.filterActivePlayers(allCampaignMembers, campaign.getDate()).getSquadronMemberList().size();
        int numInactive = maimedSquadronMembers.size() + capturedSquadronMembers.size() + deadSquadronMembers.size();
        int numWounded = woundedSquadronMembers.size();
 
        assert(numPlayers == 1);

        SquadronMembers squadronMembers;
        squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getSquadronMemberList().size() == (allCampaignMembers.size() - numInactive));
        
        squadronMembers = SquadronMemberFilter.filterActiveAI(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getSquadronMemberList().size() == (allCampaignMembers.size() - (numInactive + numAces + numPlayers)));
        
        squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayer(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getSquadronMemberList().size() == (allCampaignMembers.size() - (numInactive + numAces)));
        
        squadronMembers = SquadronMemberFilter.filterActiveAIAndAces(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getSquadronMemberList().size() == (allCampaignMembers.size() - (numInactive + numPlayers)));
        
        squadronMembers = SquadronMemberFilter.filterInactiveAIAndPlayerAndAces(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getSquadronMemberList().size() == (numInactive));
        
        squadronMembers = SquadronMemberFilter.filterActiveAINoWounded(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getSquadronMemberList().size() == (allCampaignMembers.size() - (numInactive + numAces + numPlayers + numWounded)));
    }

}
