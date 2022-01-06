package pwcg.mission.briefing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.mission.playerunit.crew.CrewVehiclePayloadPairing;

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
    public void modifyTankTypeTest () throws PWCGException
    {             
        briefingAssignmentData.changePlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, SerialNumber.PLANE_STARTING_SERIAL_NUMBER+4);
        
        assert(briefingAssignmentData.getCrews().size() == 2);
        assert(briefingAssignmentData.getUnassignedCrewMembers().size() == 2);
        
        CrewVehiclePayloadPairing crewPlane1 = briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        CrewVehiclePayloadPairing crewPlane2 = briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        
        assert(crewPlane1.getPlane().getType().equals("bf109f2"));
        assert(crewPlane2.getPlane().getType().equals("bf109f2"));
        assert(crewPlane1.getPlane().getSerialNumber() == SerialNumber.PLANE_STARTING_SERIAL_NUMBER+4);
        assert(crewPlane2.getPlane().getSerialNumber() == SerialNumber.PLANE_STARTING_SERIAL_NUMBER+2);
        assert(crewPlane1.getCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(crewPlane2.getCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    @Test
    public void assignCrewMemberFromBriefingTest () throws PWCGException
    {             
        briefingAssignmentData.assignCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+3, SerialNumber.PLANE_STARTING_SERIAL_NUMBER+3);

        CrewVehiclePayloadPairing crewPlane1 = briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        CrewVehiclePayloadPairing crewPlane2 = briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);

        assert(briefingAssignmentData.getCrews().size() == 3);
        assert(briefingAssignmentData.getUnassignedCrewMembers().size() == 1);
        assert(crewPlane1.getPlane().getType().equals("bf109f4"));
        assert(crewPlane2.getPlane().getType().equals("bf109f2"));
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+3).getPlane().getType().equals("bf109f4"));
        assert(crewPlane1.getCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(crewPlane2.getCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+3).getCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+3);
    }

    @Test
    public void unassignCrewMemberFromBriefing () throws PWCGException
    {             
        briefingAssignmentData.unassignCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        
        CrewVehiclePayloadPairing crewPlane1 = briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);

        assert(briefingAssignmentData.getCrews().size() == 1);
        assert(briefingAssignmentData.getUnassignedCrewMembers().size() == 3);
        assert(crewPlane1.getPlane().getType().equals("bf109f4"));
        assert(crewPlane1.getCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
    }

}
