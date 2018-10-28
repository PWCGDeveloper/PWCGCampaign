package pwcg.aar;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.ExtendedTimeReason;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

@RunWith(MockitoJUnitRunner.class)
public class AARExtendedTimeHandlerTest
{
    private Campaign campaign;
    private AARContext aarContext;
    
    @Mock LogPilot playerLogPilot;
    
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.JG_51_PROFILE);
        campaign.getCampaignData().setCoop(false);
        aarContext = new AARContext(campaign);
    }
    
    @Test
    public void testTimeNotPassedForViableSquadron () throws PWCGException
    {
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(campaign.getPersonnelManager().getPlayerPersonnel().getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberList())
        {
            if (!squadronMember.isPlayer())
            {
                squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
            }
        }

        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForSquadronNotViable();
        Date endCampaignDate = campaign.getDate();
        
        assert (aarContext.getReasonForExtendedTime() == ExtendedTimeReason.NO_EXTENDED_TIME);
        assert(endCampaignDate.equals(startCampaignDate));
    }
    
    @Test
    public void testTimePassedForViableAndNonViableSquadron () throws PWCGException
    {
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(campaign.getPersonnelManager().getPlayerPersonnel().getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberList())
        {
            if (!squadronMember.isPlayer())
            {
                squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
            }
            SquadronMembers squadronMembersLeft = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(campaign.getPersonnelManager().getPlayerPersonnel().getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
            if (squadronMembersLeft.getSquadronMemberCollection().size() < 6)
            {
                break;
            }
        }
        
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForSquadronNotViable();
        Date endCampaignDate = campaign.getDate();
        
        assert (aarContext.getReasonForExtendedTime() == ExtendedTimeReason.NO_PILOTS);
        assert(endCampaignDate.after(startCampaignDate));
        SquadronMembers squadronMembersAfter = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(campaign.getPersonnelManager().getPlayerPersonnel().getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        assert(squadronMembersAfter.getSquadronMemberCollection().size() >= 6);
    }

    @Test
    public void testTimePassedForLeave () throws PWCGException
    {
        Mockito.when(playerLogPilot.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(playerLogPilot.getStatus()).thenReturn(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
            
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForLeave(21);
        Date endCampaignDate = campaign.getDate();
        
        assert (aarContext.getReasonForExtendedTime() == ExtendedTimeReason.LEAVE);
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed > 19);
        assert(daysPassed < 23);
    }
    
    @Test
    public void testTimePassedForTransfer () throws PWCGException
    {
        Mockito.when(playerLogPilot.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(playerLogPilot.getStatus()).thenReturn(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
            
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForTransfer(10);
        Date endCampaignDate = campaign.getDate();
        
        assert (aarContext.getReasonForExtendedTime() == ExtendedTimeReason.TRANSFER);
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed >= 9);
        assert(daysPassed <= 13);
    }
}
