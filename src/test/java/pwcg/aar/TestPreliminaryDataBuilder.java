package pwcg.aar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogFileSet;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;

public class TestPreliminaryDataBuilder
{
    private Campaign campaign;
    private AARPreliminaryData preliminaryData = new AARPreliminaryData();
    private List<Company> squadronsInMission = new ArrayList<>();

    public TestPreliminaryDataBuilder (Campaign campaign, List<Company> squadronsInMission)
    {
        this.campaign = campaign;
        this.squadronsInMission = squadronsInMission;
    }
    
    public AARPreliminaryData makePreliminaryForTestMission() throws PWCGException
    {
        makeCampaignMembersInMission();
        makeMissionLogFileSet();
        makeClaimData();
        makePWCGMMissionData();
        
        return preliminaryData;
    }

    private void makeCampaignMembersInMission() throws PWCGException
    {
        CrewMembers squadronMembersInMission = new CrewMembers();
        
        CrewMember player = campaign.getPersonnelManager().getFlyingPlayers().getCrewMemberList().get(0);
        squadronMembersInMission.addToCrewMemberCollection(player);

        for (Company squadron : squadronsInMission)
        {
            CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                            campaign.getPersonnelManager().getCompanyPersonnel(squadron.getSquadronId()).getCrewMembersWithAces().getCrewMemberCollection(),campaign.getDate());
            List<CrewMember> squadronMembersList = squadronMembers.getCrewMemberList();
            for (int i = 0; i < 4; ++i)
            {
                squadronMembersInMission.addToCrewMemberCollection(squadronMembersList.get(i));
            }
        }
                
        preliminaryData.setCampaignMembersInMission(squadronMembersInMission);
    }

    private void makeMissionLogFileSet()
    {
        LogFileSet logFileSet = new LogFileSet();
        logFileSet.setLogFileName("missionReport(2018-05-07_21-17-16)");
        preliminaryData.setMissionLogFileSet(logFileSet);
    }

    private void makeClaimData()
    {
        List<String> enemyPlaneTypesInMission = new ArrayList<>();
        enemyPlaneTypesInMission.add("pe2s35");
        enemyPlaneTypesInMission.add("yak1s69");
        enemyPlaneTypesInMission.add("il2m41");
        
        AARClaimPanelData claimPanelData = new AARClaimPanelData();
        claimPanelData.setEnemyPlaneTypesInMission(enemyPlaneTypesInMission);
        
        preliminaryData.setClaimPanelData(claimPanelData);
    }

    private void makePWCGMMissionData() throws PWCGException
    {
        PwcgMissionData pwcgMissionData = new PwcgMissionData();
        
        MissionHeader missionHeader = makePwcgMissionDataHeader();
        Map<Integer, PwcgGeneratedMissionPlaneData> missionPlanes = makePwcgMissionDataPlanes();

        pwcgMissionData.setMissionHeader(missionHeader);
        pwcgMissionData.setMissionDescription("A test mission");
        pwcgMissionData.setMissionPlanes(missionPlanes);
        
        preliminaryData.setPwcgMissionData(pwcgMissionData);
    }

    private MissionHeader makePwcgMissionDataHeader()
    {
        MissionHeader missionHeader = new MissionHeader();
        missionHeader.setMissionFileName("Test Campaign Patrol 01-11-1941");
        
        missionHeader.setAirfield("My Airfield");
        missionHeader.setDate("10411101");
        missionHeader.setSquadron("I/JG51");
        missionHeader.setAircraftType("Bf109 F-2");

        
        missionHeader.setDuty("PATROL");
        missionHeader.setAltitude(3000); 
        
        missionHeader.setMapName(PWCGContext.getInstance().getCurrentMap().getMapName()); 

        String formattedTime = DateUtils.getDateAsMissionFileFormat(campaign.getDate());
        missionHeader.setTime(formattedTime);
        return missionHeader;
    }
    

    private Map<Integer, PwcgGeneratedMissionPlaneData> makePwcgMissionDataPlanes() throws PWCGException
    {
        Map<Integer, PwcgGeneratedMissionPlaneData> missionPlanes  = new HashMap<>();
        
        CrewMembers squadronMembersInMission = preliminaryData.getCampaignMembersInMission();
        for (CrewMember crewMember : squadronMembersInMission.getCrewMemberCollection().values())
        {
            Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(crewMember.getCompanyId());
            List<EquippedPlane> planesForSquadron = new ArrayList<>(equipment.getActiveEquippedPlanes().values());
            int planeIndex = RandomNumberGenerator.getRandom(planesForSquadron.size());
            EquippedPlane equippedPlane = planesForSquadron.get(planeIndex);

            PwcgGeneratedMissionPlaneData missionPlaneData = new PwcgGeneratedMissionPlaneData();
            missionPlaneData.setAircraftType(equippedPlane.getType());
            missionPlaneData.setSquadronId(crewMember.getCompanyId());
            missionPlaneData.setCrewMemberName(crewMember.getName());
            missionPlaneData.setCrewMemberSerialNumber(crewMember.getSerialNumber());
            missionPlaneData.setPlaneSerialNumber(equippedPlane.getSerialNumber());
            
            missionPlanes.put(crewMember.getSerialNumber(), missionPlaneData);
        }
        
        return missionPlanes;
    }

}
