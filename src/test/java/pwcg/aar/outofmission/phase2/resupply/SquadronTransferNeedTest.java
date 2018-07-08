package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.outofmission.phase2.resupply.SquadronPersonnelNeed;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class SquadronTransferNeedTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private Squadron squadron;
    
    @Mock
    private CampaignPersonnelManager campaignPersonnelManager;

    @Mock
    private AARPersonnelLosses lossesInMissionData;

    @Mock
    private SquadronPersonnel squadronPersonnel;

    @Mock
    private SquadronMembers activeSquadronMembers;

    @Mock
    private SquadronMembers inactiveSquadronMembers;

    @Mock
    private SquadronMember squadronMember;

    private Map<Integer, SquadronMember> activeSquadronMemberCollection = new HashMap<>();
    private Map<Integer, SquadronMember> inactiveSquadronMemberCollection = new HashMap<>();
    
    SerialNumber serialNumber = new SerialNumber();
    
    @Before
    public void setup() throws PWCGException
    {
        activeSquadronMemberCollection.clear();
        inactiveSquadronMemberCollection.clear();
        
        PWCGContextManager.setRoF(false);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170430"));
        Mockito.when(campaign.getSquadronId()).thenReturn(501011);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getSquadronPersonnel(Matchers.<Integer>any())).thenReturn(squadronPersonnel);

        Mockito.when(squadronPersonnel.getActiveSquadronMembersWithAces()).thenReturn(activeSquadronMembers);
        Mockito.when(activeSquadronMembers.getSquadronMemberCollection()).thenReturn(activeSquadronMemberCollection);

        Mockito.when(squadronPersonnel.getRecentlyInactiveSquadronMembers()).thenReturn(inactiveSquadronMembers);
        Mockito.when(inactiveSquadronMembers.getSquadronMemberCollection()).thenReturn(inactiveSquadronMemberCollection);
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
            activeSquadronMemberCollection.put(serialNumber.getNextPilotSerialNumber(), squadronMember);
        }
        
        
        Mockito.when(activeSquadronMembers.getActiveCount(campaign.getDate())).thenReturn(9);
        
        SquadronPersonnelNeed squadronResupplyNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        assert (squadronResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < Squadron.SQUADRON_STAFF_SIZE - 10; ++i)
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
            activeSquadronMemberCollection.put(serialNumber.getNextPilotSerialNumber(), squadronMember);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            inactiveSquadronMemberCollection.put(serialNumber.getNextPilotSerialNumber(), squadronMember);
        }
        
        Mockito.when(activeSquadronMembers.getActiveCount(campaign.getDate())).thenReturn(7);
        Mockito.when(inactiveSquadronMembers.getActiveCount(campaign.getDate())).thenReturn(2);

        SquadronPersonnelNeed squadronResupplyNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        assert (squadronResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < Squadron.SQUADRON_STAFF_SIZE - 10; ++i)
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
            activeSquadronMemberCollection.put(serialNumber.getNextPilotSerialNumber(), squadronMember);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            inactiveSquadronMemberCollection.put(serialNumber.getNextPilotSerialNumber(), squadronMember);
        }
        
        Mockito.when(activeSquadronMembers.getActiveCount(campaign.getDate())).thenReturn(10);
        Mockito.when(inactiveSquadronMembers.getActiveCount(campaign.getDate())).thenReturn(2);

        SquadronPersonnelNeed squadronResupplyNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        assert (squadronResupplyNeed.needsResupply() == false);
    }

}
