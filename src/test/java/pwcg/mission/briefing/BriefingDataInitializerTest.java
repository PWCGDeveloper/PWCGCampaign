package pwcg.mission.briefing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.rofmap.brief.BriefingDataInitializer;
import pwcg.gui.rofmap.brief.model.BriefingPilotAssignmentData;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlights;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPlanes;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BriefingDataInitializerTest
{
    @Mock protected Campaign campaign;
    @Mock protected CampaignPersonnelManager personnelManager;
    @Mock protected CampaignEquipmentManager equipmentManager;
    @Mock protected Equipment equipment;

    @Mock protected Squadron squadron;
    @Mock protected SquadronPersonnel squadronPersonnel;
    @Mock protected SquadronMembers squadronMembers;
    @Mock protected Mission mission;
    @Mock protected MissionFlights missionFlightBuilder;
    @Mock protected IFlight flight;
    @Mock protected IFlightPlanes flightPlanes;
    @Mock protected PlaneMcu plane1;
    @Mock protected PlaneMcu plane2;
    @Mock protected PlaneMcu plane3;
    @Mock protected PlaneMcu plane4;
    @Mock protected EquippedPlane equippedPlane1;
    @Mock protected EquippedPlane equippedPlane2;
    @Mock protected EquippedPlane equippedPlane3;
    @Mock protected EquippedPlane equippedPlane4;
    @Mock protected SquadronMember pilot1;
    @Mock protected SquadronMember pilot2;
    @Mock protected SquadronMember pilot3;
    @Mock protected SquadronMember pilot4;

    protected Map<Integer, SquadronMember> squadronPersonnelMap = new HashMap<>();
    protected List<PlaneMcu> planesInFlight = new ArrayList<>();
    protected Map<Integer, EquippedPlane> equippedPlanes = new HashMap<>();
    protected BriefingPilotAssignmentData briefingAssignmentData = new BriefingPilotAssignmentData();

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(mission.getCampaign()).thenReturn(campaign);
        Mockito.when(mission.getFlights()).thenReturn(missionFlightBuilder);

        Mockito.when(missionFlightBuilder.getPlayerFlightForSquadron(Mockito.anyInt())).thenReturn(flight);

        Mockito.when(flight.getFlightPlanes()).thenReturn(flightPlanes);
        Mockito.when(flightPlanes.getPlanes()).thenReturn(planesInFlight);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        
        Mockito.when(campaign.getEquipmentManager()).thenReturn(equipmentManager);
        Mockito.when(equipmentManager.getEquipmentForSquadron(Mockito.any())).thenReturn(equipment);
        Mockito.when(equipment.getActiveEquippedPlanes()).thenReturn(equippedPlanes);

        Mockito.when(personnelManager.getSquadronPersonnel(Mockito.any())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.getSquadronMembersWithAces()).thenReturn(squadronMembers);
        Mockito.when(squadronMembers.getSquadronMemberCollection()).thenReturn(squadronPersonnelMap);

        Mockito.when(squadron.getSquadronId()).thenReturn(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
        
        Mockito.when(pilot1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        Mockito.when(pilot2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        Mockito.when(pilot3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+3);
        Mockito.when(pilot4.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+4);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, pilot1);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, pilot2);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+3, pilot3);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+4, pilot4);

        Mockito.when(equippedPlane1.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+1);
        Mockito.when(equippedPlane2.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+2);
        Mockito.when(equippedPlane3.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+3);
        Mockito.when(equippedPlane4.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+4);
        Mockito.when(equippedPlane1.getType()).thenReturn("bf109f4");
        Mockito.when(equippedPlane2.getType()).thenReturn("bf109f2");
        Mockito.when(equippedPlane3.getType()).thenReturn("bf109f4");
        Mockito.when(equippedPlane4.getType()).thenReturn("bf109f2");
        equippedPlanes.put(equippedPlane1.getSerialNumber(), equippedPlane1);
        equippedPlanes.put(equippedPlane2.getSerialNumber(), equippedPlane2);
        equippedPlanes.put(equippedPlane3.getSerialNumber(), equippedPlane3);
        equippedPlanes.put(equippedPlane4.getSerialNumber(), equippedPlane4);

        Mockito.when(plane1.getPilot()).thenReturn(pilot1);
        Mockito.when(plane2.getPilot()).thenReturn(pilot2);
        Mockito.when(plane1.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+1);
        Mockito.when(plane2.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+2);

        planesInFlight.add(plane1);
        planesInFlight.add(plane2);
    }

    @Test
    public void initializePayloadsFromMissionTest () throws PWCGException
    {             
        
        BriefingDataInitializer briefingDataInitializer = new BriefingDataInitializer(mission);
        briefingAssignmentData = briefingDataInitializer.initializeFromMission(squadron);
        
        assert(briefingAssignmentData.getCrews().size() == 2);
        assert(briefingAssignmentData.getUnassignedPilots().size() == 2);
        assert(briefingAssignmentData.getUnassignedPlanes().size() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPlane().getType().equals("bf109f4"));
        assert(briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPlane().getType().equals("bf109f2"));
        assert(briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(briefingAssignmentData.findAssignedCrewPairingByPilot(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    public BriefingPilotAssignmentData getBriefingAssignmentData()
    {
        return briefingAssignmentData;
    }
}
