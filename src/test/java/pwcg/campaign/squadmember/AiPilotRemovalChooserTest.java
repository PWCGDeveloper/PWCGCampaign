package pwcg.campaign.squadmember;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.AiCrewMemberRemovalChooser;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AiCrewMemberRemovalChooserTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private CompanyPersonnel squadronPersonnel;    
    @Mock private Company squadron;
    @Mock private CrewMember squadronMember1;
    @Mock private CrewMember squadronMember2;
    @Mock private CrewMember squadronMember3;
    @Mock private CrewMember squadronMember4;
    @Mock private CrewMember squadronMember5;
    @Mock private CrewMember squadronMember6;
    
    private Date campaignDate;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaignDate = DateUtils.getDateYYYYMMDD("19420801");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getCompanyPersonnel(ArgumentMatchers.anyInt())).thenReturn(squadronPersonnel);

        squadron = PWCGContext.getInstance().getCompanyManager().getCompany(10131132); 
        
        Mockito.when(squadronMember1.getRank()).thenReturn("Major");
        Mockito.when(squadronMember1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        
        Mockito.when(squadronMember2.getRank()).thenReturn("Kapitan");
        Mockito.when(squadronMember2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);

        Mockito.when(squadronMember3.getRank()).thenReturn("Starshyi leyitenant");
        Mockito.when(squadronMember3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+3);

        Mockito.when(squadronMember4.getRank()).thenReturn("Leyitenant");
        Mockito.when(squadronMember4.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+4);

        Mockito.when(squadronMember5.getRank()).thenReturn("Leyitenant");
        Mockito.when(squadronMember5.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+5);

        Mockito.when(squadronMember6.getRank()).thenReturn("Serzhant");
    }


    @Test
    public void testRemoveSameRank() throws PWCGException
    {
        Mockito.when(squadronMember6.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+6);
        
        CrewMembers squadronMembers = new CrewMembers();
        Mockito.when(squadronPersonnel.getCrewMembers()).thenReturn(squadronMembers);
        squadronMembers.addToCrewMemberCollection(squadronMember1);
        squadronMembers.addToCrewMemberCollection(squadronMember2);
        squadronMembers.addToCrewMemberCollection(squadronMember3);
        squadronMembers.addToCrewMemberCollection(squadronMember4);
        squadronMembers.addToCrewMemberCollection(squadronMember5);
        squadronMembers.addToCrewMemberCollection(squadronMember6);

        
        AiCrewMemberRemovalChooser chooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember squadronMemberRemoved = chooser.findAiCrewMemberToRemove("Leyitenant", 10131132);
        Assertions.assertTrue (squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+4 || 
                squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+5);
    }

    @Test
    public void testRemoveSimilarRank() throws PWCGException
    {
        Mockito.when(squadronMember6.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+6);
        
        CrewMembers squadronMembers = new CrewMembers();
        Mockito.when(squadronPersonnel.getCrewMembers()).thenReturn(squadronMembers);
        squadronMembers.addToCrewMemberCollection(squadronMember1);
        squadronMembers.addToCrewMemberCollection(squadronMember2);
        squadronMembers.addToCrewMemberCollection(squadronMember3);
        squadronMembers.addToCrewMemberCollection(squadronMember6);

        
        AiCrewMemberRemovalChooser chooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember squadronMemberRemoved = chooser.findAiCrewMemberToRemove("Leyitenant", 10131132);
        Assertions.assertTrue (squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+3 || 
                squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+6);
    }

    @Test
    public void testRemoveAnyNonCommandRank() throws PWCGException
    {
        CrewMembers squadronMembers = new CrewMembers();
        Mockito.when(squadronPersonnel.getCrewMembers()).thenReturn(squadronMembers);
        squadronMembers.addToCrewMemberCollection(squadronMember1);
        squadronMembers.addToCrewMemberCollection(squadronMember2);

        
        AiCrewMemberRemovalChooser chooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember squadronMemberRemoved = chooser.findAiCrewMemberToRemove("Leyitenant", 10131132);
        Assertions.assertTrue (squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    @Test
    public void testCommanderRemoved() throws PWCGException
    {
        CrewMembers squadronMembers = new CrewMembers();
        Mockito.when(squadronPersonnel.getCrewMembers()).thenReturn(squadronMembers);
        squadronMembers.addToCrewMemberCollection(squadronMember1);
        
        AiCrewMemberRemovalChooser chooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember squadronMemberRemoved = chooser.findAiCrewMemberToRemove("Major", 10131132);
        Assertions.assertTrue (squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
    }

    @Test
    public void testNobodyRemoved() throws PWCGException
    {
        CrewMembers squadronMembers = new CrewMembers();
        Mockito.when(squadronPersonnel.getCrewMembers()).thenReturn(squadronMembers);
        squadronMembers.addToCrewMemberCollection(squadronMember1);
        
        AiCrewMemberRemovalChooser chooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember squadronMemberRemoved = chooser.findAiCrewMemberToRemove("Leyitenant", 10131132);
        Assertions.assertTrue (squadronMemberRemoved == null);
    }
}
