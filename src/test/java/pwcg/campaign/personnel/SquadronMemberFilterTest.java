package pwcg.campaign.personnel;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CrewMemberPicker;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class CrewMemberFilterTest
{
    private Campaign campaign;
    private Map<Integer, CrewMember> woundedCrewMembers = new HashMap<>();
    private Map<Integer, CrewMember> maimedCrewMembers = new HashMap<>();
    private Map<Integer, CrewMember> capturedCrewMembers = new HashMap<>();
    private Map<Integer, CrewMember> deadCrewMembers = new HashMap<>();
    
    @BeforeEach
    public void setupTest() throws PWCGException
     {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        
        while (woundedCrewMembers.size() < 5)
        {
            CrewMember crewMember = CrewMemberPicker.pickNonAceCampaignMember(campaign, SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_WOUNDED, campaign.getDate(), DateUtils.advanceTimeDays(campaign.getDate(), 10));
            if (!campaignMemberUsed(crewMember.getSerialNumber()) && crewMember.isPlayer() == false)
            {
                woundedCrewMembers.put(crewMember.getSerialNumber(), crewMember);
            }
        }
        
        while (maimedCrewMembers.size() < 4)
        {
            CrewMember crewMember = CrewMemberPicker.pickNonAceCampaignMember(campaign, SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), null);
            if (!campaignMemberUsed(crewMember.getSerialNumber()) && crewMember.isPlayer() == false)
            {
                maimedCrewMembers.put(crewMember.getSerialNumber(), crewMember);
            }
        }
        
        while (deadCrewMembers.size() < 3)
        {
            CrewMember crewMember = CrewMemberPicker.pickNonAceCampaignMember(campaign, SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
            if (!campaignMemberUsed(crewMember.getSerialNumber()) && crewMember.isPlayer() == false)
            {
                deadCrewMembers.put(crewMember.getSerialNumber(), crewMember);
            }
        }
        
        while (capturedCrewMembers.size() < 2)
        {
            CrewMember crewMember = CrewMemberPicker.pickNonAceCampaignMember(campaign, SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
            if (!campaignMemberUsed(crewMember.getSerialNumber()) && crewMember.isPlayer() == false)
            {
                capturedCrewMembers.put(crewMember.getSerialNumber(), crewMember);
            }
        }
    }
    
    private boolean campaignMemberUsed(int serialNumber)
    {
        if (woundedCrewMembers.containsKey(serialNumber))
        {
            return true;
        }
        
        if (maimedCrewMembers.containsKey(serialNumber))
        {
            return true;
        }
        
        if (capturedCrewMembers.containsKey(serialNumber))
        {
            return true;
        }

        if (deadCrewMembers.containsKey(serialNumber))
        {
            return true;
        }
        
        return false;
    }
    
    @Test
    public void validateFilters() throws PWCGException
    {
        Map<Integer, CrewMember> allCampaignMembers = campaign.getPersonnelManager().getAllCampaignMembers();
        int numAces = campaign.getPersonnelManager().getCampaignAces().getAllCampaignAces().size();
        int numPlayers = CrewMemberFilter.filterActivePlayers(allCampaignMembers, campaign.getDate()).getCrewMemberList().size();
        int numInactive = maimedCrewMembers.size() + capturedCrewMembers.size() + deadCrewMembers.size();
        int numWounded = woundedCrewMembers.size();
 
        assert(numPlayers == 1);

        CrewMembers squadronMembers;
        squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getCrewMemberList().size() == (allCampaignMembers.size() - numInactive));
        
        squadronMembers = CrewMemberFilter.filterActiveAI(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getCrewMemberList().size() == (allCampaignMembers.size() - (numInactive + numAces + numPlayers)));
        
        squadronMembers = CrewMemberFilter.filterActiveAIAndPlayer(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getCrewMemberList().size() == (allCampaignMembers.size() - (numInactive + numAces)));
        
        squadronMembers = CrewMemberFilter.filterActiveAIAndAces(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getCrewMemberList().size() == (allCampaignMembers.size() - (numInactive + numPlayers)));
        
        squadronMembers = CrewMemberFilter.filterInactiveAIAndPlayerAndAces(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getCrewMemberList().size() == (numInactive));
        
        squadronMembers = CrewMemberFilter.filterActiveAINoWounded(allCampaignMembers, campaign.getDate());
        assert(squadronMembers.getCrewMemberList().size() == (allCampaignMembers.size() - (numInactive + numAces + numPlayers + numWounded)));
        
        validateFiltersOnActive(squadronMembers.getCrewMemberList().size());
    }
    
    public void validateFiltersOnActive(int numActiveExpected) throws PWCGException
    {
        Map<Integer, CrewMember> allActiveCampaignMembers = campaign.getPersonnelManager().getActiveCampaignMembers();
        int numAces = campaign.getPersonnelManager().getCampaignAces().getActiveCampaignAces().size();
        int numPlayers = CrewMemberFilter.filterActivePlayers(allActiveCampaignMembers, campaign.getDate()).getCrewMemberList().size();
 
        assert(numPlayers == 1);
        assert(numAces > 0);
        assert(allActiveCampaignMembers.size() == (numActiveExpected + numAces + numPlayers));
    }

}
