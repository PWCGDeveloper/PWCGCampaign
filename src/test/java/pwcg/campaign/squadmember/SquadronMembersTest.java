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
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SquadronMembersTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private SquadronMember squadronMember1;
    
    @Mock
    private SquadronMember squadronMember2;
    
    @Mock
    private SquadronMember squadronMember3;
    
    @Mock
    private SquadronMember squadronMember4;
    
    private  SquadronMembers squadronMembers;
    
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

        squadronMembers = new SquadronMembers();
        squadronMembers.addToSquadronMemberCollection(squadronMember1);
        squadronMembers.addToSquadronMemberCollection(squadronMember2);
    }

    @Test
    public void addSquadronMember() throws PWCGException 
    {
        squadronMembers.addToSquadronMemberCollection(squadronMember3);
        assert(squadronMembers.getActiveCount(campaign.getDate()) == 3);
    }

    @Test
    public void removeAnySquadronMember() throws PWCGException 
    {
        squadronMembers.addToSquadronMemberCollection(squadronMember3);
        squadronMembers.addToSquadronMemberCollection(squadronMember4);
        SquadronMember squadronMember = squadronMembers.findSquadronMember();
        squadronMembers.removeSquadronMember(squadronMember.getSerialNumber());
        assert(squadronMembers.getActiveCount(campaign.getDate()) == 3);
    }

    @Test
    public void removeSquadronMember() throws PWCGException 
    {
        squadronMembers.addToSquadronMemberCollection(squadronMember3);
        squadronMembers.addToSquadronMemberCollection(squadronMember4);
        SquadronMember squadronMember = squadronMembers.removeSquadronMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 3);
        assert(squadronMembers.getActiveCount(campaign.getDate()) == 3);
        assert(squadronMember.getSerialNumber() == (SerialNumber.AI_STARTING_SERIAL_NUMBER + 3));
    }

    @Test
    public void getSquadronMemberByName() throws PWCGException 
    {
        squadronMembers.addToSquadronMemberCollection(squadronMember3);
        squadronMembers.addToSquadronMemberCollection(squadronMember4);
        SquadronMember squadronMember = squadronMembers.getSquadronMemberByName("Jimmy Page");
        assert(squadronMembers.getActiveCount(campaign.getDate()) == 4);
        assert(squadronMember.getSerialNumber() == (SerialNumber.AI_STARTING_SERIAL_NUMBER + 3));
    }

}
