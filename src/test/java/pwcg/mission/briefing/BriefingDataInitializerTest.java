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
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.rofmap.brief.BriefingDataInitializer;
import pwcg.gui.rofmap.brief.model.BriefingCrewMemberAssignmentData;
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

    @Mock protected Company squadron;
    @Mock protected CompanyPersonnel squadronPersonnel;
    @Mock protected CrewMembers squadronMembers;
    @Mock protected Mission mission;
    @Mock protected MissionFlights missionFlightBuilder;
    @Mock protected IFlight flight;
    @Mock protected IFlightPlanes flightPlanes;
    @Mock protected PlaneMcu plane1;
    @Mock protected PlaneMcu plane2;
    @Mock protected PlaneMcu plane3;
    @Mock protected PlaneMcu plane4;
    @Mock protected EquippedTank equippedPlane1;
    @Mock protected EquippedTank equippedPlane2;
    @Mock protected EquippedTank equippedPlane3;
    @Mock protected EquippedTank equippedPlane4;
    @Mock protected CrewMember crewMember1;
    @Mock protected CrewMember crewMember2;
    @Mock protected CrewMember crewMember3;
    @Mock protected CrewMember crewMember4;

    protected Map<Integer, CrewMember> squadronPersonnelMap = new HashMap<>();
    protected List<PlaneMcu> planesInFlight = new ArrayList<>();
    protected Map<Integer, EquippedTank> equippedPlanes = new HashMap<>();
    protected BriefingCrewMemberAssignmentData briefingAssignmentData = new BriefingCrewMemberAssignmentData();

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
        Mockito.when(equipmentManager.getEquipmentForCompany(Mockito.any())).thenReturn(equipment);
        Mockito.when(equipment.getActiveEquippedTanks()).thenReturn(equippedPlanes);

        Mockito.when(personnelManager.getCompanyPersonnel(Mockito.any())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.getCrewMembersWithAces()).thenReturn(squadronMembers);
        Mockito.when(squadronMembers.getCrewMemberCollection()).thenReturn(squadronPersonnelMap);

        Mockito.when(squadron.getCompanyId()).thenReturn(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId());
        
        Mockito.when(crewMember1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        Mockito.when(crewMember2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        Mockito.when(crewMember3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+3);
        Mockito.when(crewMember4.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+4);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, crewMember1);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, crewMember2);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+3, crewMember3);
        squadronPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+4, crewMember4);

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

        Mockito.when(plane1.getCrewMember()).thenReturn(crewMember1);
        Mockito.when(plane2.getCrewMember()).thenReturn(crewMember2);
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
        assert(briefingAssignmentData.getUnassignedCrewMembers().size() == 2);
        assert(briefingAssignmentData.getUnassignedPlanes().size() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getTank().getType().equals("bf109f4"));
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getTank().getType().equals("bf109f2"));
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    public BriefingCrewMemberAssignmentData getBriefingAssignmentData()
    {
        return briefingAssignmentData;
    }
}
