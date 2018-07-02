package pwcg.mission.briefing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

@RunWith(MockitoJUnitRunner.class)
public class BriefingPayloadHelperTest extends BriefingDataInitializerTest
{

    @Mock private IPlanePayload payload1;
    @Mock private IPlanePayload payload2;
    @Mock private IPlanePayload payload3;
    @Mock private IPlanePayload payload4;
    @Mock private PayloadDesignation payloadDesignation1;
    @Mock private PayloadDesignation payloadDesignation2;
    @Mock private PayloadDesignation payloadDesignation3;
    @Mock private PayloadDesignation payloadDesignation4;
    
    @Before
    public void setup() throws PWCGException
    {
        super.setup();
        super.initializePayloadsFromMissionTest();

        Mockito.when(payload1.getSelectedPayloadDesignation()).thenReturn(payloadDesignation1);
        Mockito.when(payload2.getSelectedPayloadDesignation()).thenReturn(payloadDesignation2);
        Mockito.when(payload3.getSelectedPayloadDesignation()).thenReturn(payloadDesignation3);
        Mockito.when(payload4.getSelectedPayloadDesignation()).thenReturn(payloadDesignation4);

        Mockito.when(payloadDesignation1.getPayloadId()).thenReturn(2);
        Mockito.when(payloadDesignation2.getPayloadId()).thenReturn(2);
        Mockito.when(payloadDesignation3.getPayloadId()).thenReturn(2);
        
        Mockito.when(plane1.getPlanePayload()).thenReturn(payload1);
        Mockito.when(plane2.getPlanePayload()).thenReturn(payload2);
        Mockito.when(plane3.getPlanePayload()).thenReturn(payload3);
        Mockito.when(plane4.getPlanePayload()).thenReturn(payload4);
    }
    

    @Test
    public void initializePayloadsFromMissionTest () throws PWCGException
    {             
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        
        assert(briefingAssignmentData.getAssignedCrewPlanes().size() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);        
    }

    @Test
    public void modifyPayloadTest () throws PWCGException
    {             
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        payloadHelper.modifyPayload(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, 1);
        
        assert(briefingAssignmentData.getAssignedCrewPlanes().size() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 1);
    }

    @Test
    public void modifyPayloadPlaneNotFoundTest () throws PWCGException
    {             
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        payloadHelper.modifyPayload(SerialNumber.AI_STARTING_SERIAL_NUMBER+3, 1);
        
        assert(briefingAssignmentData.getAssignedCrewPlanes().size() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);
    }

    @Test
    public void setPayloadForAddedPlaneSamePlaneTest () throws PWCGException
    {       
        Mockito.when(equippedPlane1.getType()).thenReturn("bf109f4");
        Mockito.when(equippedPlane2.getType()).thenReturn("bf109f4");
        Mockito.when(plane1.getType()).thenReturn("bf109f4");
        Mockito.when(plane2.getType()).thenReturn("bf109f4");

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);

        CrewPlanePayloadPairing crew2 = new CrewPlanePayloadPairing(pilot2, plane2);
        briefingAssignmentData.getAssignedCrewPlanes().put(pilot2.getSerialNumber(), crew2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == -1);

        payloadHelper.setPayloadForChangedPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        
        assert(briefingAssignmentData.getAssignedCrewPlanes().size() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);
    }

    @Test
    public void setPayloadForAddedPlaneDifferentPlaneTest () throws PWCGException
    {       
        Mockito.when(equippedPlane1.getType()).thenReturn("bf109f4");
        Mockito.when(equippedPlane2.getType()).thenReturn("bf109f2");
        Mockito.when(plane1.getType()).thenReturn("bf109f4");
        Mockito.when(plane1.getType()).thenReturn("bf109f2");

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);

        CrewPlanePayloadPairing crew2 = new CrewPlanePayloadPairing(pilot2, plane2);
        crew2.setPlane(plane2);
        briefingAssignmentData.getAssignedCrewPlanes().put(pilot2.getSerialNumber(), crew2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == -1);


        payloadHelper.setPayloadForChangedPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().size() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.getAssignedCrewPlanes().get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 0);
    }
}
