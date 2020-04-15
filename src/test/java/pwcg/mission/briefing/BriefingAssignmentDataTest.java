package pwcg.mission.briefing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.Silent.class) 
public class BriefingAssignmentDataTest extends BriefingDataInitializerTest
{
    @Before
    public void setup() throws PWCGException
    {
        briefingAssignmentData.reset();        
        planesInFlight.clear();       
        
        super.setup();
        super.initializePayloadsFromMissionTest();
    }

    @Test
    public void modifyPlaneTypeTest () throws PWCGException
    {             
        briefingAssignmentData.changePlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, SerialNumber.PLANE_STARTING_SERIAL_NUMBER+4);
        
        assert(briefingAssignmentData.getAssignedPilots().size() == 2);
        assert(briefingAssignmentData.getUnassignedPilots().size() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPlane().getType().equals("bf109f2"));
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPlane().getType().equals("bf109f2"));
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPlane().getSerialNumber() == SerialNumber.PLANE_STARTING_SERIAL_NUMBER+4);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPlane().getSerialNumber() == SerialNumber.PLANE_STARTING_SERIAL_NUMBER+2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    @Test
    public void assignPilotFromBriefingTest () throws PWCGException
    {             
        briefingAssignmentData.assignPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+3, SerialNumber.PLANE_STARTING_SERIAL_NUMBER+3);
        
        assert(briefingAssignmentData.getAssignedPilots().size() == 3);
        assert(briefingAssignmentData.getUnassignedPilots().size() == 1);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPlane().getType().equals("bf109f4"));
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPlane().getType().equals("bf109f2"));
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+3).getPlane().getType().equals("bf109f4"));
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+3).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+3);
    }

    @Test
    public void unassignPilotFromBriefing () throws PWCGException
    {             
        briefingAssignmentData.unassignPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        
        assert(briefingAssignmentData.getAssignedPilots().size() == 1);
        assert(briefingAssignmentData.getUnassignedPilots().size() == 3);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPlane().getType().equals("bf109f4"));
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
    }

}
