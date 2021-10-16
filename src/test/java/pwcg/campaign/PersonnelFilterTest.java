package pwcg.campaign;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.personnel.PersonnelActiveFilter;
import pwcg.campaign.personnel.PersonnelFilter;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class PersonnelFilterTest
{
    private static Map<Integer, SquadronMember> testSquadronMembers = new HashMap<Integer, SquadronMember>();
    private static String[] names = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", };
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
    	testSquadronMembers = new HashMap<Integer, SquadronMember>();
    	for (int i = 0; i < 10; ++i)
    	{
    		SquadronMember squadronMember = new SquadronMember();
    		squadronMember.setName(names[i]);
    		squadronMember.setSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + i);
    		squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
    		squadronMember.setSquadronId(101003);
    		testSquadronMembers.put(squadronMember.getSerialNumber(), squadronMember);
    	}
    }

    @Test
    public void testStatusFilterTest () throws PWCGException
    {            	
    	for (int i = 0; i < 4; ++i)
    	{
    		SquadronMember squadronMember = testSquadronMembers.get(SerialNumber.AI_STARTING_SERIAL_NUMBER + i);
    		squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, DateUtils.getDateYYYYMMDD("19170801"), null);
    	}

    	Map<Integer, SquadronMember> returnSquadronMembers = new HashMap<Integer, SquadronMember>();
    	
        PersonnelActiveFilter activePersonnelFilter = new PersonnelActiveFilter();
        returnSquadronMembers = activePersonnelFilter.getActive(testSquadronMembers);
        Assertions.assertTrue (returnSquadronMembers.size() == 6);
    	
        PersonnelActiveFilter inactivePersonnelFilter = new PersonnelActiveFilter();
        returnSquadronMembers = inactivePersonnelFilter.getInactive(testSquadronMembers);
        Assertions.assertTrue (returnSquadronMembers.size() == 4);
    }

    @Test
    public void applyAceFilterTest () throws PWCGException
    {         
    	for (int i = 3; i < 7; ++i)
    	{
    		SquadronMember squadronMember = testSquadronMembers.get(SerialNumber.AI_STARTING_SERIAL_NUMBER + i);
    		squadronMember.setSerialNumber(SerialNumber.ACE_STARTING_SERIAL_NUMBER + i);
    	}

    	Map<Integer, SquadronMember> returnSquadronMembers = new HashMap<Integer, SquadronMember>();
    	
    	PersonnelFilter personnelFilter = new PersonnelFilter(false);    	
    	returnSquadronMembers = personnelFilter.applyAceFilter(testSquadronMembers);
        Assertions.assertTrue (returnSquadronMembers.size() == 4);
    	
        personnelFilter = new PersonnelFilter(true);
    	returnSquadronMembers = personnelFilter.applyAceFilter(testSquadronMembers);
        Assertions.assertTrue (returnSquadronMembers.size() == 6);
    }
    
    @Test
    public void applySquadronFilterTest () throws PWCGException
    {         
        for (int i = 3; i < 7; ++i)
        {
            SquadronMember squadronMember = testSquadronMembers.get(SerialNumber.AI_STARTING_SERIAL_NUMBER + i);
            squadronMember.setSquadronId(101005);
        }

        Map<Integer, SquadronMember> returnSquadronMembers = new HashMap<Integer, SquadronMember>();
        
        PersonnelFilter personnelFilter = new PersonnelFilter(false);       
        returnSquadronMembers = personnelFilter.applySquadronFilter(testSquadronMembers, 101005);
        Assertions.assertTrue (returnSquadronMembers.size() == 4);
        
        personnelFilter = new PersonnelFilter(true);
        returnSquadronMembers = personnelFilter.applySquadronFilter(testSquadronMembers, 101005);
        Assertions.assertTrue (returnSquadronMembers.size() == 6);
     }
    
    @Test
    public void applyPlayerFilterTest () throws PWCGException
    {         
        for (int i = 3; i < 7; ++i)
        {
            SquadronMember squadronMember = testSquadronMembers.get(SerialNumber.AI_STARTING_SERIAL_NUMBER + i);
            squadronMember.setSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER + i);
        }

        Map<Integer, SquadronMember> returnSquadronMembers = new HashMap<Integer, SquadronMember>();
        
        PersonnelFilter personnelFilter = new PersonnelFilter(false);       
        returnSquadronMembers = personnelFilter.applyPlayerFilter(testSquadronMembers);
        Assertions.assertTrue (returnSquadronMembers.size() == 4);
        
        personnelFilter = new PersonnelFilter(true);
        returnSquadronMembers = personnelFilter.applyPlayerFilter(testSquadronMembers);
        Assertions.assertTrue (returnSquadronMembers.size() == 6);
     }

}
