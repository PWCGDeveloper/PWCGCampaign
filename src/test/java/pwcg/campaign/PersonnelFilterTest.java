package pwcg.campaign;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.PersonnelActiveFilter;
import pwcg.campaign.personnel.PersonnelFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class PersonnelFilterTest
{
    private static Map<Integer, CrewMember> testCrewMembers = new HashMap<Integer, CrewMember>();
    private static String[] names = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", };
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
    	testCrewMembers = new HashMap<Integer, CrewMember>();
    	for (int i = 0; i < 10; ++i)
    	{
    		CrewMember crewMember = new CrewMember();
    		crewMember.setName(names[i]);
    		crewMember.setSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + i);
    		crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
    		crewMember.setSquadronId(101003);
    		testCrewMembers.put(crewMember.getSerialNumber(), crewMember);
    	}
    }

    @Test
    public void testStatusFilterTest () throws PWCGException
    {            	
    	for (int i = 0; i < 4; ++i)
    	{
    		CrewMember crewMember = testCrewMembers.get(SerialNumber.AI_STARTING_SERIAL_NUMBER + i);
    		crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, DateUtils.getDateYYYYMMDD("19170801"), null);
    	}

    	Map<Integer, CrewMember> returnCrewMembers = new HashMap<Integer, CrewMember>();
    	
        PersonnelActiveFilter activePersonnelFilter = new PersonnelActiveFilter();
        returnCrewMembers = activePersonnelFilter.getActive(testCrewMembers);
        Assertions.assertTrue (returnCrewMembers.size() == 6);
    	
        PersonnelActiveFilter inactivePersonnelFilter = new PersonnelActiveFilter();
        returnCrewMembers = inactivePersonnelFilter.getInactive(testCrewMembers);
        Assertions.assertTrue (returnCrewMembers.size() == 4);
    }

    @Test
    public void applyAceFilterTest () throws PWCGException
    {         
    	for (int i = 3; i < 7; ++i)
    	{
    		CrewMember crewMember = testCrewMembers.get(SerialNumber.AI_STARTING_SERIAL_NUMBER + i);
    		crewMember.setSerialNumber(SerialNumber.ACE_STARTING_SERIAL_NUMBER + i);
    	}

    	Map<Integer, CrewMember> returnCrewMembers = new HashMap<Integer, CrewMember>();
    	
    	PersonnelFilter personnelFilter = new PersonnelFilter(false);    	
    	returnCrewMembers = personnelFilter.applyAceFilter(testCrewMembers);
        Assertions.assertTrue (returnCrewMembers.size() == 4);
    	
        personnelFilter = new PersonnelFilter(true);
    	returnCrewMembers = personnelFilter.applyAceFilter(testCrewMembers);
        Assertions.assertTrue (returnCrewMembers.size() == 6);
    }
    
    @Test
    public void applySquadronFilterTest () throws PWCGException
    {         
        for (int i = 3; i < 7; ++i)
        {
            CrewMember crewMember = testCrewMembers.get(SerialNumber.AI_STARTING_SERIAL_NUMBER + i);
            crewMember.setSquadronId(101005);
        }

        Map<Integer, CrewMember> returnCrewMembers = new HashMap<Integer, CrewMember>();
        
        PersonnelFilter personnelFilter = new PersonnelFilter(false);       
        returnCrewMembers = personnelFilter.applySquadronFilter(testCrewMembers, 101005);
        Assertions.assertTrue (returnCrewMembers.size() == 4);
        
        personnelFilter = new PersonnelFilter(true);
        returnCrewMembers = personnelFilter.applySquadronFilter(testCrewMembers, 101005);
        Assertions.assertTrue (returnCrewMembers.size() == 6);
     }
    
    @Test
    public void applyPlayerFilterTest () throws PWCGException
    {         
        for (int i = 3; i < 7; ++i)
        {
            CrewMember crewMember = testCrewMembers.get(SerialNumber.AI_STARTING_SERIAL_NUMBER + i);
            crewMember.setSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER + i);
        }

        Map<Integer, CrewMember> returnCrewMembers = new HashMap<Integer, CrewMember>();
        
        PersonnelFilter personnelFilter = new PersonnelFilter(false);       
        returnCrewMembers = personnelFilter.applyPlayerFilter(testCrewMembers);
        Assertions.assertTrue (returnCrewMembers.size() == 4);
        
        personnelFilter = new PersonnelFilter(true);
        returnCrewMembers = personnelFilter.applyPlayerFilter(testCrewMembers);
        Assertions.assertTrue (returnCrewMembers.size() == 6);
     }

}
