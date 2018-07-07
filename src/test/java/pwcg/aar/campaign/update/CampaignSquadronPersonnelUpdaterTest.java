package pwcg.aar.campaign.update;


import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.campaign.update.CampaignPersonnelUpdater;
import pwcg.aar.data.AARContext;
import pwcg.aar.outofmission.phase2.resupply.TransferRecord;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheRoF;
import pwcg.testutils.SquadronMemberPicker;

@RunWith(MockitoJUnitRunner.class)
public class CampaignSquadronPersonnelUpdaterTest
{
    private Map<Integer, SquadronMember> squadMembersKilled = new HashMap<>();
    private Map<Integer, SquadronMember> squadMembersMaimed = new HashMap<>();
    private Map<Integer, SquadronMember> squadMembersCaptured = new HashMap<>();
    private Map<Integer, Ace> acesKilled = new HashMap<>();
    
    private Campaign campaign;
    private AARContext aarContext;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        
        campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_103_PROFILE);
        aarContext = new AARContext(campaign);

        squadMembersKilled.clear();
        squadMembersMaimed.clear();
        squadMembersCaptured.clear();
        acesKilled.clear();
    }

    @Test
    public void testSquadronMemberKilled() throws PWCGException
    {
        SquadronMember deadSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign); 
        deadSquadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate());
        squadMembersKilled.put(deadSquadronMember.getSerialNumber(), deadSquadronMember);
        aarContext.getCampaignUpdateData().getPersonnelLosses().mergePersonnelKilled(squadMembersKilled);

        CampaignPersonnelUpdater personellUpdater = new CampaignPersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(deadSquadronMember.getSerialNumber()); 
        assertTrue (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA);
    }

    @Test
    public void testSquadronMemberCaptured() throws PWCGException
    {
        SquadronMember capturedSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign);
        capturedSquadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate());
        squadMembersCaptured.put(capturedSquadronMember.getSerialNumber(), capturedSquadronMember);
        aarContext.getCampaignUpdateData().getPersonnelLosses().mergePersonnelCaptured(squadMembersCaptured);

        CampaignPersonnelUpdater personellUpdater = new CampaignPersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(capturedSquadronMember.getSerialNumber()); 
        assertTrue (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_CAPTURED);
    }

    @Test
    public void testSquadronMemberMaimed() throws PWCGException
    {
        SquadronMember maimedSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign); 
        maimedSquadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate());
        squadMembersMaimed.put(maimedSquadronMember.getSerialNumber(), maimedSquadronMember);
        aarContext.getCampaignUpdateData().getPersonnelLosses().mergePersonnelMaimed(squadMembersMaimed);

        CampaignPersonnelUpdater personellUpdater = new CampaignPersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(maimedSquadronMember.getSerialNumber()); 
        assertTrue (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
    }

    @Test
    public void testSquadronMemberTransferred() throws PWCGException
    {
        SquadronMember transferredSquadronMember = SquadronMemberPicker.pickNonAceSquadronMember(campaign); 
        TransferRecord transferRecord = new TransferRecord(transferredSquadronMember, 101103, 101048);
        
        aarContext.getCampaignUpdateData().getResupplyData().getSquadronTransferData().addTransferRecord(transferRecord);

        CampaignPersonnelUpdater personellUpdater = new CampaignPersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        SquadronMember squadronMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(transferredSquadronMember.getSerialNumber()); 
        assertTrue (squadronMemberAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE);
        
        assertTrue (squadronMemberAfterUpdate.getSquadronId() != campaign.getSquadronId());
    }

    @Test
    public void testSquadronAceKilled() throws PWCGException
    {
        Ace guynemerInCampaign = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101064); 
        acesKilled.put(101064, guynemerInCampaign);
        aarContext.getCampaignUpdateData().getPersonnelLosses().mergeAcesKilled(acesKilled);

        CampaignPersonnelUpdater personellUpdater = new CampaignPersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        Ace aceAfterUpdate = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101064); 
        assertTrue (aceAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA);
    }
    
    @Test
    public void testNonSquadronAceKilled() throws PWCGException
    {
        Ace vossInCampaign = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101175); 
        acesKilled.put(101175, vossInCampaign);
        aarContext.getCampaignUpdateData().getPersonnelLosses().mergeAcesKilled(acesKilled);

        CampaignPersonnelUpdater personellUpdater = new CampaignPersonnelUpdater(campaign, aarContext);
        personellUpdater.personnelUpdates();

        Ace aceAfterUpdate = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101175); 
        assertTrue (aceAfterUpdate.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA);
    }

}
