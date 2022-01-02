package pwcg.campaign.squadmember;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CrewMembersTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private CrewMember squadronMember1;
    
    @Mock
    private CrewMember squadronMember2;
    
    @Mock
    private CrewMember squadronMember3;
    
    @Mock
    private CrewMember squadronMember4;
    
    private  CrewMembers squadronMembers;
    
    @BeforeEach
    public void setupTest() throws PWCGException 
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));

        Mockito.when(squadronMember1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        Mockito.when(squadronMember2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
        Mockito.when(squadronMember3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 3);
        Mockito.when(squadronMember4.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 4);
        
        Mockito.when(squadronMember1.getName()).thenReturn("John Bonham");
        Mockito.when(squadronMember3.getName()).thenReturn("Jimmy Page");
        Mockito.when(squadronMember4.getName()).thenReturn("Robert Plant");

        squadronMembers = new CrewMembers();
        squadronMembers.addToCrewMemberCollection(squadronMember1);
        squadronMembers.addToCrewMemberCollection(squadronMember2);
    }

    @Test
    public void addCrewMember() throws PWCGException 
    {
        squadronMembers.addToCrewMemberCollection(squadronMember3);
        assert(squadronMembers.getActiveCount(campaign.getDate()) == 3);
    }

    @Test
    public void removeAnyCrewMember() throws PWCGException 
    {
        squadronMembers.addToCrewMemberCollection(squadronMember3);
        squadronMembers.addToCrewMemberCollection(squadronMember4);
        CrewMember crewMember = squadronMembers.findCrewMember();
        squadronMembers.removeCrewMember(crewMember.getSerialNumber());
        assert(squadronMembers.getActiveCount(campaign.getDate()) == 3);
    }

    @Test
    public void removeCrewMember() throws PWCGException 
    {
        squadronMembers.addToCrewMemberCollection(squadronMember3);
        squadronMembers.addToCrewMemberCollection(squadronMember4);
        CrewMember crewMember = squadronMembers.removeCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 3);
        assert(squadronMembers.getActiveCount(campaign.getDate()) == 3);
        assert(crewMember.getSerialNumber() == (SerialNumber.AI_STARTING_SERIAL_NUMBER + 3));
    }

    @Test
    public void getCrewMemberByName() throws PWCGException 
    {
        squadronMembers.addToCrewMemberCollection(squadronMember3);
        squadronMembers.addToCrewMemberCollection(squadronMember4);
        CrewMember crewMember = squadronMembers.getCrewMemberByName("Jimmy Page");
        assert(squadronMembers.getActiveCount(campaign.getDate()) == 4);
        assert(crewMember.getSerialNumber() == (SerialNumber.AI_STARTING_SERIAL_NUMBER + 3));
    }

}
