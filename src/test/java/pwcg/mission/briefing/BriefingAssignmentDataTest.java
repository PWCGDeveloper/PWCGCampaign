package pwcg.mission.briefing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BriefingAssignmentDataTest extends BriefingDataInitializerTest
{
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        briefingAssignmentData.reset();        
        planesInFlight.clear();       
        
        super.setupTest();
        super.initializePayloadsFromMissionTest();
    }

    @Test
    public void modifyPlaneTypeTest () throws PWCGException
    {             
        briefingAssignmentData.changePlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, SerialNumber.PLANE_STARTING_SERIAL_NUMBER+4);
        
        assert(briefingAssignmentData.getCrews().size() == 2);
        assert(briefingAssignmentData.getUnassignedPilots().size() == 2);
        
        CrewPlanePayloadPairing crewPlane1 = briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        CrewPlanePayloadPairing crewPlane2 = briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        
        assert(crewPlane1.getPlane().getType().equals("bf109f2"));
        assert(crewPlane2.getPlane().getType().equals("bf109f2"));
        assert(crewPlane1.getPlane().getSerialNumber() == SerialNumber.PLANE_STARTING_SERIAL_NUMBER+4);
        assert(crewPlane2.getPlane().getSerialNumber() == SerialNumber.PLANE_STARTING_SERIAL_NUMBER+2);
        assert(crewPlane1.getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(crewPlane2.getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    @Test
    public void assignPilotFromBriefingTest () throws PWCGException
    {             
        briefingAssignmentData.assignPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+3, SerialNumber.PLANE_STARTING_SERIAL_NUMBER+3);

        CrewPlanePayloadPairing crewPlane1 = briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        CrewPlanePayloadPairing crewPlane2 = briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);

        assert(briefingAssignmentData.getCrews().size() == 3);
        assert(briefingAssignmentData.getUnassignedPilots().size() == 1);
        assert(crewPlane1.getPlane().getType().equals("bf109f4"));
        assert(crewPlane2.getPlane().getType().equals("bf109f2"));
        assert(briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+3).getPlane().getType().equals("bf109f4"));
        assert(crewPlane1.getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(crewPlane2.getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        assert(briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+3).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+3);
    }

    @Test
    public void unassignPilotFromBriefing () throws PWCGException
    {             
        briefingAssignmentData.unassignPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        
        CrewPlanePayloadPairing crewPlane1 = briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);

        assert(briefingAssignmentData.getCrews().size() == 1);
        assert(briefingAssignmentData.getUnassignedPilots().size() == 3);
        assert(crewPlane1.getPlane().getType().equals("bf109f4"));
        assert(crewPlane1.getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
    }

}
