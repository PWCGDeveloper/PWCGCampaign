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
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlightBuilder;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.plane.Plane;

@RunWith(MockitoJUnitRunner.class)
public class BriefingPilotHelperTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private MissionFlightBuilder missionFlightBuilder;
    @Mock private SquadronPersonnel squadronPersonnel;
    @Mock private SquadronMembers inSquadron;
    @Mock private Squadron squadron;
    @Mock private Mission mission;
    @Mock private SquadronMember pilot1;
    @Mock private SquadronMember pilot2;
    @Mock private SquadronMember pilot3;
    @Mock private SquadronMember pilot4;
    @Mock private Plane plane1;
    @Mock private Plane plane2;
    @Mock private Plane plane3;
    @Mock private Plane plane4;
    @Mock private Flight flight;
    
    
    
    private Map <Integer, SquadronMember> squadronPersonnelMap = new HashMap <>();

    private Map <Integer, CrewPlanePayloadPairing> unassignedCrewMap = new HashMap <>();
    private Map <Integer, CrewPlanePayloadPairing> assignedCrewMap = new HashMap <>();
    private List <Plane> planesInFlight = new ArrayList <>();
    private List<PlaneType> aircraftTypes = new ArrayList <>();

    @Before
    public void setup() throws PWCGException
    {
        assignedCrewMap.clear();        
        unassignedCrewMap.clear();       
        planesInFlight.clear();       
        aircraftTypes.clear();       
        
        PWCGContextManager.setRoF(false);
        
        Mockito.when(mission.getCampaign()).thenReturn(campaign);
        Mockito.when(mission.getMissionFlightBuilder()).thenReturn(missionFlightBuilder);
        Mockito.when(missionFlightBuilder.getPlayerFlight()).thenReturn(flight);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(campaign.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineCurrentAircraftList(Mockito.any())).thenReturn(aircraftTypes);
        
        PlaneType plane = PWCGContextManager.getInstance().getPlaneTypeFactory().getPlaneTypeByAnyName("bf109f4");
        aircraftTypes.add(plane);
        
        Mockito.when(personnelManager.getSquadronPersonnel(Mockito.any())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.getActiveSquadronMembersWithAces()).thenReturn(inSquadron);
        Mockito.when(inSquadron.getSquadronMembers()).thenReturn(squadronPersonnelMap);
        
        Mockito.when(pilot1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        Mockito.when(pilot2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        Mockito.when(pilot3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+3);
        Mockito.when(pilot4.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+4);

        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, pilot1);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, pilot2);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+3, pilot3);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+4, pilot4);
        
        Mockito.when(pilot1.determineSortKey(Mockito.any())).thenReturn("100");
        Mockito.when(pilot2.determineSortKey(Mockito.any())).thenReturn("200");
        Mockito.when(pilot3.determineSortKey(Mockito.any())).thenReturn("300");
        Mockito.when(pilot4.determineSortKey(Mockito.any())).thenReturn("400");

        Mockito.when(plane1.getPilot()).thenReturn(pilot1);
        Mockito.when(plane2.getPilot()).thenReturn(pilot2);
        Mockito.when(plane3.getPilot()).thenReturn(pilot3);
        Mockito.when(plane4.getPilot()).thenReturn(pilot4);
        Mockito.when(plane1.getType()).thenReturn("bf109f4");
        Mockito.when(plane2.getType()).thenReturn("bf109f2");
        Mockito.when(plane3.getType()).thenReturn("bf109f4");
        Mockito.when(plane4.getType()).thenReturn("bf109f2");
        
        planesInFlight.add(plane1);
        planesInFlight.add(plane2);
        
        Mockito.when(flight.getPlanes()).thenReturn(planesInFlight);

        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
        
        CrewPlanePayloadPairing crew1 = new CrewPlanePayloadPairing(pilot1);
        crew1.setPlaneType("bf109f4");
        
        CrewPlanePayloadPairing crew2 = new CrewPlanePayloadPairing(pilot2);
        crew2.setPlaneType("bf109f4");

        CrewPlanePayloadPairing crew3 = new CrewPlanePayloadPairing(pilot3);
        crew3.setPlaneType("bf109f2");

        CrewPlanePayloadPairing crew4 = new CrewPlanePayloadPairing(pilot4);
        crew4.setPlaneType("bf109f2");
    }

    @Test
    public void initializePayloadsFromMissionTest () throws PWCGException
    {             
        BriefingPilotHelper pilotHelper = new BriefingPilotHelper(mission, assignedCrewMap, unassignedCrewMap);
        pilotHelper.initializeFromMission();
        
        assert(assignedCrewMap.size() == 2);
        assert(unassignedCrewMap.size() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPlaneType().equals("bf109f4"));
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPlaneType().equals("bf109f2"));
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    @Test
    public void modifyPlaneTypeTest () throws PWCGException
    {             
        BriefingPilotHelper pilotHelper = new BriefingPilotHelper(mission, assignedCrewMap, unassignedCrewMap);
        pilotHelper.initializeFromMission();
        pilotHelper.modifyPlaneType(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, "bf109f2");
        
        assert(assignedCrewMap.size() == 2);
        assert(unassignedCrewMap.size() == 2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPlaneType().equals("bf109f2"));
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPlaneType().equals("bf109f2"));
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    @Test
    public void assignPilotFromBriefingTest () throws PWCGException
    {             
        BriefingPilotHelper pilotHelper = new BriefingPilotHelper(mission, assignedCrewMap, unassignedCrewMap);
        pilotHelper.initializeFromMission();
        pilotHelper.assignPilotFromBriefing(SerialNumber.AI_STARTING_SERIAL_NUMBER+3);
        
        assert(assignedCrewMap.size() == 3);
        assert(unassignedCrewMap.size() == 1);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPlaneType().equals("bf109f4"));
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPlaneType().equals("bf109f2"));
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+3).getPlaneType().equals("bf109f4"));
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+3).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+3);
    }

    @Test
    public void unassignPilotFromBriefing () throws PWCGException
    {             
        BriefingPilotHelper pilotHelper = new BriefingPilotHelper(mission, assignedCrewMap, unassignedCrewMap);
        pilotHelper.initializeFromMission();
        pilotHelper.unassignPilotFromBriefing(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        
        assert(assignedCrewMap.size() == 1);
        assert(unassignedCrewMap.size() == 3);
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPlaneType().equals("bf109f4"));
        assert(assignedCrewMap.get(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
    }

}
