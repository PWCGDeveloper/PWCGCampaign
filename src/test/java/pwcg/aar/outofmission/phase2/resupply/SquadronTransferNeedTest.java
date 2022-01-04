package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
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
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.resupply.personnel.SquadronPersonnelNeed;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class SquadronTransferNeedTest
{
    @Mock private Campaign campaign;
    @Mock private Company squadron;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private AARPersonnelLosses lossesInMissionData;
    @Mock private CompanyPersonnel squadronPersonnel;
    @Mock private CrewMembers activeCrewMembers;
    @Mock private CrewMembers inactiveCrewMembers;

    private Map<Integer, CrewMember> activeCrewMemberCollection = new HashMap<>();
    private Map<Integer, CrewMember> inactiveCrewMemberCollection = new HashMap<>();
    
    SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        activeCrewMemberCollection.clear();
        inactiveCrewMemberCollection.clear();
        
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170430"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getCompanyPersonnel(ArgumentMatchers.<Integer>any())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.getCrewMembersWithAces()).thenReturn(activeCrewMembers);
        Mockito.when(squadronPersonnel.getRecentlyInactiveCrewMembers()).thenReturn(inactiveCrewMembers);
        Mockito.when(activeCrewMembers.getCrewMemberCollection()).thenReturn(activeCrewMemberCollection);
     }

    @Test
    public void testResupplyWithNoCrewMembers() throws PWCGException
    {
        SquadronPersonnelNeed squadronTransferNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronTransferNeed.determineResupplyNeeded();
        Assertions.assertTrue (squadronTransferNeed.needsResupply() == true);
        
        for (int i = 0; i < Company.COMPANY_STAFF_SIZE - 1; ++i)
        {
            squadronTransferNeed.noteResupply();
            Assertions.assertTrue (squadronTransferNeed.needsResupply() == true);
        }

        squadronTransferNeed.noteResupply();
        Assertions.assertTrue (squadronTransferNeed.needsResupply() == false);
    }
    

    @Test
    public void testResupplyWithActiveCrewMembers() throws PWCGException
    {
        for (int i = 0; i < 9; ++i)
        {
            CrewMember crewMember = new CrewMember();
            crewMember.setSerialNumber(serialNumber.getNextCrewMemberSerialNumber());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
            activeCrewMemberCollection.put(crewMember.getSerialNumber(), crewMember);
        }
                
        SquadronPersonnelNeed squadronResupplyNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        Assertions.assertTrue (squadronResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            squadronResupplyNeed.noteResupply();
            Assertions.assertTrue (squadronResupplyNeed.needsResupply() == true);
        }

        squadronResupplyNeed.noteResupply();
        Assertions.assertTrue (squadronResupplyNeed.needsResupply() == false);
    }
    

    @Test
    public void testResupplyWithActiveAndInactiveCrewMembers() throws PWCGException
    {
        for (int i = 0; i < 7; ++i)
        {
            CrewMember crewMember = new CrewMember();
            crewMember.setSerialNumber(serialNumber.getNextCrewMemberSerialNumber());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
            activeCrewMemberCollection.put(crewMember.getSerialNumber(), crewMember);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            CrewMember crewMember = new CrewMember();
            crewMember.setSerialNumber(serialNumber.getNextCrewMemberSerialNumber());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
            inactiveCrewMemberCollection.put(crewMember.getSerialNumber(), crewMember);
        }
        
        Mockito.when(inactiveCrewMembers.getActiveCount(campaign.getDate())).thenReturn(2);

        SquadronPersonnelNeed squadronResupplyNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        Assertions.assertTrue (squadronResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            squadronResupplyNeed.noteResupply();
            Assertions.assertTrue (squadronResupplyNeed.needsResupply() == true);
        }

        squadronResupplyNeed.noteResupply();
        Assertions.assertTrue (squadronResupplyNeed.needsResupply() == false);
    }

    @Test
    public void testNoResupplyNeeded() throws PWCGException
    {
        for (int i = 0; i < 10; ++i)
        {
            CrewMember crewMember = new CrewMember();
            crewMember.setSerialNumber(serialNumber.getNextCrewMemberSerialNumber());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
            activeCrewMemberCollection.put(crewMember.getSerialNumber(), crewMember);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            CrewMember crewMember = new CrewMember();
            crewMember.setSerialNumber(serialNumber.getNextCrewMemberSerialNumber());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
            inactiveCrewMemberCollection.put(crewMember.getSerialNumber(), crewMember);
        }
        
        Mockito.when(inactiveCrewMembers.getActiveCount(campaign.getDate())).thenReturn(2);

        SquadronPersonnelNeed squadronResupplyNeed = new SquadronPersonnelNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        Assertions.assertTrue (squadronResupplyNeed.needsResupply() == false);
    }

}
