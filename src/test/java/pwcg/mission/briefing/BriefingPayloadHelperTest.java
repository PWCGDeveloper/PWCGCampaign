package pwcg.mission.briefing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.target.TargetCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlightBuilder;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.plane.PlaneMCU;

@RunWith(MockitoJUnitRunner.class)
public class BriefingPayloadHelperTest
{
    @Mock private Campaign campaign;
    @Mock private MissionFlightBuilder missionFlightBuilder;
    @Mock private Mission mission;
    @Mock private SquadronMember pilot1;
    @Mock private SquadronMember pilot2;
    @Mock private SquadronMember pilot3;
    @Mock private PlaneMCU plane1;
    @Mock private PlaneMCU plane2;
    @Mock private PlaneMCU plane3;
    @Mock private Flight flight;
    @Mock private IPlanePayload payload1;
    @Mock private IPlanePayload payload2;
    @Mock private IPlanePayload payload3;
    @Mock private PayloadDesignation payloadDesignation1;
    @Mock private PayloadDesignation payloadDesignation2;
    @Mock private PayloadDesignation payloadDesignation3;
    
    
    private Map <Integer, CrewPlanePayloadPairing> assignedCrewMap = new HashMap <>();
    private List <PlaneMCU> planesInFlight = new ArrayList <>();

    @Before
    public void setup() throws PWCGException
    {
        assignedCrewMap.clear();        
        PWCGContextManager.setRoF(false);
        
        Mockito.when(mission.getMissionFlightBuilder()).thenReturn(missionFlightBuilder);
        Mockito.when(missionFlightBuilder.getPlayerFlight()).thenReturn(flight);

        
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
        Mockito.when(pilot1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        Mockito.when(pilot2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        Mockito.when(pilot3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+3);

        Mockito.when(pilot1.determineSortKey(Mockito.any())).thenReturn("100");
        Mockito.when(pilot2.determineSortKey(Mockito.any())).thenReturn("200");
        Mockito.when(pilot3.determineSortKey(Mockito.any())).thenReturn("300");

        Mockito.when(plane1.getPilot()).thenReturn(pilot1);
        Mockito.when(plane2.getPilot()).thenReturn(pilot2);
        Mockito.when(plane3.getPilot()).thenReturn(pilot3);
        Mockito.when(plane1.getType()).thenReturn("bf109f4");
        Mockito.when(plane2.getType()).thenReturn("bf109f4");
        Mockito.when(plane3.getType()).thenReturn("bf109f4");
        
        
        
        planesInFlight.add(plane1);
        planesInFlight.add(plane2);
        planesInFlight.add(plane3);
        Mockito.when(flight.getPlanes()).thenReturn(planesInFlight);
        
        Mockito.when(plane1.getPlanePayload()).thenReturn(payload1);
        Mockito.when(plane2.getPlanePayload()).thenReturn(payload2);
        Mockito.when(plane3.getPlanePayload()).thenReturn(payload3);

        Mockito.when(payload1.getSelectedPayloadDesignation()).thenReturn(payloadDesignation1);
        Mockito.when(payload2.getSelectedPayloadDesignation()).thenReturn(payloadDesignation2);
        Mockito.when(payload3.getSelectedPayloadDesignation()).thenReturn(payloadDesignation3);

        Mockito.when(payloadDesignation1.getPayloadId()).thenReturn(2);
        Mockito.when(payloadDesignation2.getPayloadId()).thenReturn(2);
        Mockito.when(payloadDesignation3.getPayloadId()).thenReturn(2);
        
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
        Mockito.when(flight.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
        
        CrewPlanePayloadPairing crew1 = new CrewPlanePayloadPairing(pilot1);
        crew1.setPlaneType("bf109f4");
        
        CrewPlanePayloadPairing crew2 = new CrewPlanePayloadPairing(pilot2);
        crew2.setPlaneType("bf109f4");

        CrewPlanePayloadPairing crew3 = new CrewPlanePayloadPairing(pilot3);
        crew3.setPlaneType("bf109f2");
        
        assignedCrewMap.put(pilot1.getSerialNumber(), crew1);
        assignedCrewMap.put(pilot2.getSerialNumber(), crew2);
    }
    

    @Test
    public void initializePayloadsFromMissionTest () throws PWCGException
    {             
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, assignedCrewMap);
        payloadHelper.initializePayloadsFromMission();
        
        assert(assignedCrewMap.size() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);
        
    }

    @Test
    public void modifyPayloadTest () throws PWCGException
    {             
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, assignedCrewMap);
        payloadHelper.initializePayloadsFromMission();
        payloadHelper.modifyPayload(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, 1);
        
        assert(assignedCrewMap.size() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 1);
        
    }

    @Test
    public void modifyPayloadPlaneNotFoundTest () throws PWCGException
    {             
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, assignedCrewMap);
        payloadHelper.initializePayloadsFromMission();
        payloadHelper.modifyPayload(SerialNumber.AI_STARTING_SERIAL_NUMBER+3, 1);
        
        assert(assignedCrewMap.size() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);
        
    }

    @Test
    public void setPayloadForAddedPlaneSamePlaneTest () throws PWCGException
    {       

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, assignedCrewMap);
        payloadHelper.initializePayloadsFromMission();
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);

        CrewPlanePayloadPairing crew2 = new CrewPlanePayloadPairing(pilot2);
        crew2.setPlaneType("bf109f4");
        assignedCrewMap.put(pilot2.getSerialNumber(), crew2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == -1);

        payloadHelper.setPayloadForChangedPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        assert(assignedCrewMap.size() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);
    }

    @Test
    public void setPayloadForAddedPlaneDifferentPlaneTest () throws PWCGException
    {       

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, assignedCrewMap);
        payloadHelper.initializePayloadsFromMission();
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);

        CrewPlanePayloadPairing crew2 = new CrewPlanePayloadPairing(pilot2);
        crew2.setPlaneType("bf109f2");
        assignedCrewMap.put(pilot2.getSerialNumber(), crew2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == -1);


        payloadHelper.setPayloadForChangedPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        assert(assignedCrewMap.size() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);
    }
}
