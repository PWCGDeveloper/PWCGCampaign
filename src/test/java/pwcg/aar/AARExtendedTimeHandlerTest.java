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
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
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
        campaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE);
        aarContext = new AARContext(campaign);
    }
    
    @Test
    public void testTimeNotPassedForNonViableSquadron () throws PWCGException
    {
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForSquadronNotViable();
        Date endCampaignDate = campaign.getDate();
        
        assert (aarContext.getReasonForExtendedTime() == ExtendedTimeReason.NO_EXTENDED_TIME);
        assert(endCampaignDate.equals(startCampaignDate));
    }
    
    @Test
    public void testTimePassedForNonViableSquadron () throws PWCGException
    {
        for (SquadronMember squadronMember : campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId()).getActiveSquadronMembers().getSquadronMembers().values())
        {
            if (!squadronMember.isPlayer())
            {
                squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate());
            }
            if (campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId()).getActiveSquadronMembersWithAces().getSquadronMembers().size() < 6)
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
        assert(campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId()).getActiveSquadronMembersWithAces().getSquadronMembers().size() >= 6);
    }
    
    @Test
    public void testTimePassedForWounds () throws PWCGException
    {
        Mockito.when(playerLogPilot.getSerialNumber()).thenReturn(SerialNumber.PLAYER_SERIAL_NUMBER);
        Mockito.when(playerLogPilot.getStatus()).thenReturn(SquadronMemberStatus.STATUS_WOUNDED);
            
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForWounds(playerLogPilot);
        Date endCampaignDate = campaign.getDate();
        
        assert (aarContext.getReasonForExtendedTime() == ExtendedTimeReason.WOUND);
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed > 6);
        assert(daysPassed < 40);
    }
    
    @Test
    public void testTimePassedForSeriousWounds () throws PWCGException
    {
        Mockito.when(playerLogPilot.getSerialNumber()).thenReturn(SerialNumber.PLAYER_SERIAL_NUMBER);
        Mockito.when(playerLogPilot.getStatus()).thenReturn(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
            
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForWounds(playerLogPilot);
        Date endCampaignDate = campaign.getDate();
        
        assert (aarContext.getReasonForExtendedTime() == ExtendedTimeReason.WOUND);
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed > 29);
        assert(daysPassed < 90);
    }
    
    @Test
    public void testTimePassedForLeave () throws PWCGException
    {
        Mockito.when(playerLogPilot.getSerialNumber()).thenReturn(SerialNumber.PLAYER_SERIAL_NUMBER);
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
        Mockito.when(playerLogPilot.getSerialNumber()).thenReturn(SerialNumber.PLAYER_SERIAL_NUMBER);
        Mockito.when(playerLogPilot.getStatus()).thenReturn(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
            
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForTransfer(10);
        Date endCampaignDate = campaign.getDate();
        
        assert (aarContext.getReasonForExtendedTime() == ExtendedTimeReason.TRANSFER);
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed > 9);
        assert(daysPassed < 13);
    }
}
