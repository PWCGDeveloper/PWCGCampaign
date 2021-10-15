package pwcg.campaign.squadmember;

import java.util.Date;

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
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AiPilotRemovalChooserTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private SquadronPersonnel squadronPersonnel;    
    @Mock private Squadron squadron;
    @Mock private SquadronMember squadronMember1;
    @Mock private SquadronMember squadronMember2;
    @Mock private SquadronMember squadronMember3;
    @Mock private SquadronMember squadronMember4;
    @Mock private SquadronMember squadronMember5;
    @Mock private SquadronMember squadronMember6;
    
    private Date campaignDate;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaignDate = DateUtils.getDateYYYYMMDD("19420801");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getSquadronPersonnel(ArgumentMatchers.anyInt())).thenReturn(squadronPersonnel);

        squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(10131132); 
        
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
        
        SquadronMembers squadronMembers = new SquadronMembers();
        Mockito.when(squadronPersonnel.getSquadronMembers()).thenReturn(squadronMembers);
        squadronMembers.addToSquadronMemberCollection(squadronMember1);
        squadronMembers.addToSquadronMemberCollection(squadronMember2);
        squadronMembers.addToSquadronMemberCollection(squadronMember3);
        squadronMembers.addToSquadronMemberCollection(squadronMember4);
        squadronMembers.addToSquadronMemberCollection(squadronMember5);
        squadronMembers.addToSquadronMemberCollection(squadronMember6);

        
        AiPilotRemovalChooser chooser = new AiPilotRemovalChooser(campaign);
        SquadronMember squadronMemberRemoved = chooser.findAiPilotToRemove("Leyitenant", 10131132);
        assert (squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+4 || 
                squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+5);
    }

    @Test
    public void testRemoveSimilarRank() throws PWCGException
    {
        Mockito.when(squadronMember6.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+6);
        
        SquadronMembers squadronMembers = new SquadronMembers();
        Mockito.when(squadronPersonnel.getSquadronMembers()).thenReturn(squadronMembers);
        squadronMembers.addToSquadronMemberCollection(squadronMember1);
        squadronMembers.addToSquadronMemberCollection(squadronMember2);
        squadronMembers.addToSquadronMemberCollection(squadronMember3);
        squadronMembers.addToSquadronMemberCollection(squadronMember6);

        
        AiPilotRemovalChooser chooser = new AiPilotRemovalChooser(campaign);
        SquadronMember squadronMemberRemoved = chooser.findAiPilotToRemove("Leyitenant", 10131132);
        assert (squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+3 || 
                squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+6);
    }

    @Test
    public void testRemoveAnyNonCommandRank() throws PWCGException
    {
        SquadronMembers squadronMembers = new SquadronMembers();
        Mockito.when(squadronPersonnel.getSquadronMembers()).thenReturn(squadronMembers);
        squadronMembers.addToSquadronMemberCollection(squadronMember1);
        squadronMembers.addToSquadronMemberCollection(squadronMember2);

        
        AiPilotRemovalChooser chooser = new AiPilotRemovalChooser(campaign);
        SquadronMember squadronMemberRemoved = chooser.findAiPilotToRemove("Leyitenant", 10131132);
        assert (squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    @Test
    public void testCommanderRemoved() throws PWCGException
    {
        SquadronMembers squadronMembers = new SquadronMembers();
        Mockito.when(squadronPersonnel.getSquadronMembers()).thenReturn(squadronMembers);
        squadronMembers.addToSquadronMemberCollection(squadronMember1);
        
        AiPilotRemovalChooser chooser = new AiPilotRemovalChooser(campaign);
        SquadronMember squadronMemberRemoved = chooser.findAiPilotToRemove("Major", 10131132);
        assert (squadronMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
    }

    @Test
    public void testNobodyRemoved() throws PWCGException
    {
        SquadronMembers squadronMembers = new SquadronMembers();
        Mockito.when(squadronPersonnel.getSquadronMembers()).thenReturn(squadronMembers);
        squadronMembers.addToSquadronMemberCollection(squadronMember1);
        
        AiPilotRemovalChooser chooser = new AiPilotRemovalChooser(campaign);
        SquadronMember squadronMemberRemoved = chooser.findAiPilotToRemove("Leyitenant", 10131132);
        assert (squadronMemberRemoved == null);
    }
}
