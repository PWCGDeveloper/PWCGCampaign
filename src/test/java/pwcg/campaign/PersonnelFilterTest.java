package pwcg.campaign;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.personnel.PersonnelActiveFilter;
import pwcg.campaign.personnel.PersonnelFilter;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class PersonnelFilterTest
{
    private Map<Integer, SquadronMember> testSquadronMembers = new HashMap<Integer, SquadronMember>();
    private String[] names = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", };
    
    @Before
    public void setup() throws PWCGException
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
        assert (returnSquadronMembers.size() == 6);
    	
        PersonnelActiveFilter inactivePersonnelFilter = new PersonnelActiveFilter();
        returnSquadronMembers = inactivePersonnelFilter.getInactive(testSquadronMembers);
        assert (returnSquadronMembers.size() == 4);
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
        assert (returnSquadronMembers.size() == 4);
    	
        personnelFilter = new PersonnelFilter(true);
    	returnSquadronMembers = personnelFilter.applyAceFilter(testSquadronMembers);
        assert (returnSquadronMembers.size() == 6);
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
        assert (returnSquadronMembers.size() == 4);
        
        personnelFilter = new PersonnelFilter(true);
        returnSquadronMembers = personnelFilter.applySquadronFilter(testSquadronMembers, 101005);
        assert (returnSquadronMembers.size() == 6);
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
        assert (returnSquadronMembers.size() == 4);
        
        personnelFilter = new PersonnelFilter(true);
        returnSquadronMembers = personnelFilter.applyPlayerFilter(testSquadronMembers);
        assert (returnSquadronMembers.size() == 6);
     }

}
