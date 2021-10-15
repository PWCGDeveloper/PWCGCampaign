package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.resupply.personnel.SquadronPersonnelNeed;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class SquadronTransferNeedTest
{
    @Mock private Campaign campaign;
    @Mock private Squadron squadron;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private AARPersonnelLosses lossesInMissionData;
    @Mock private SquadronPersonnel squadronPersonnel;
    @Mock private SquadronMembers activeSquadronMembers;
    @Mock private SquadronMembers inactiveSquadronMembers;

    private Map<Integer, SquadronMember> activeSquadronMemberCollection = new HashMap<>();
    private Map<Integer, SquadronMember> inactiveSquadronMemberCollection = new HashMap<>();
    
    SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        activeSquadronMemberCollection.clear();
        inactiveSquadronMemberCollection.clear();
        
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170430"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getSquadronPersonnel(ArgumentMatchers.<Integer>any())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.getSquadronMembersWithAces()).thenReturn(activeSquadronMembers);
        Mockito.when(squadronPersonnel.getRecentlyInactiveSquadronMembers()).thenReturn(inactiveSquadronMembers);
        Mockito.when(activeSquadronMembers.getSquadronMemberCollection()).thenReturn(activeSquadronMemberCollection);
     }

    @Test
    public void testResupplyWithNoSquadronMembers() throws PWCGException
    {
        SquadronPersonnelNeed squadronTransferNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronTransferNeed.determineResupplyNeeded();
        assert (squadronTransferNeed.needsResupply() == true);
        
        for (int i = 0; i < Squadron.SQUADRON_STAFF_SIZE - 1; ++i)
        {
            squadronTransferNeed.noteResupply();
            assert (squadronTransferNeed.needsResupply() == true);
        }

        squadronTransferNeed.noteResupply();
        assert (squadronTransferNeed.needsResupply() == false);
    }
    

    @Test
    public void testResupplyWithActiveSquadronMembers() throws PWCGException
    {
        for (int i = 0; i < 9; ++i)
        {
            SquadronMember squadronMember = new SquadronMember();
            squadronMember.setSerialNumber(serialNumber.getNextPilotSerialNumber());
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
            activeSquadronMemberCollection.put(squadronMember.getSerialNumber(), squadronMember);
        }
                
        SquadronPersonnelNeed squadronResupplyNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        assert (squadronResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            squadronResupplyNeed.noteResupply();
            assert (squadronResupplyNeed.needsResupply() == true);
        }

        squadronResupplyNeed.noteResupply();
        assert (squadronResupplyNeed.needsResupply() == false);
    }
    

    @Test
    public void testResupplyWithActiveAndInactiveSquadronMembers() throws PWCGException
    {
        for (int i = 0; i < 7; ++i)
        {
            SquadronMember squadronMember = new SquadronMember();
            squadronMember.setSerialNumber(serialNumber.getNextPilotSerialNumber());
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
            activeSquadronMemberCollection.put(squadronMember.getSerialNumber(), squadronMember);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            SquadronMember squadronMember = new SquadronMember();
            squadronMember.setSerialNumber(serialNumber.getNextPilotSerialNumber());
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
            inactiveSquadronMemberCollection.put(squadronMember.getSerialNumber(), squadronMember);
        }
        
        Mockito.when(inactiveSquadronMembers.getActiveCount(campaign.getDate())).thenReturn(2);

        SquadronPersonnelNeed squadronResupplyNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        assert (squadronResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            squadronResupplyNeed.noteResupply();
            assert (squadronResupplyNeed.needsResupply() == true);
        }

        squadronResupplyNeed.noteResupply();
        assert (squadronResupplyNeed.needsResupply() == false);
    }

    @Test
    public void testNoResupplyNeeded() throws PWCGException
    {
        for (int i = 0; i < 10; ++i)
        {
            SquadronMember squadronMember = new SquadronMember();
            squadronMember.setSerialNumber(serialNumber.getNextPilotSerialNumber());
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
            activeSquadronMemberCollection.put(squadronMember.getSerialNumber(), squadronMember);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            SquadronMember squadronMember = new SquadronMember();
            squadronMember.setSerialNumber(serialNumber.getNextPilotSerialNumber());
            squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
            inactiveSquadronMemberCollection.put(squadronMember.getSerialNumber(), squadronMember);
        }
        
        Mockito.when(inactiveSquadronMembers.getActiveCount(campaign.getDate())).thenReturn(2);

        SquadronPersonnelNeed squadronResupplyNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        assert (squadronResupplyNeed.needsResupply() == false);
    }

}
