package pwcg.aar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogFileSet;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

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
        
        CrewMember player = campaign.getPersonnelManager().getPlayersInMission().getCrewMemberList().get(0);
        squadronMembersInMission.addToCrewMemberCollection(player);

        for (Company squadron : squadronsInMission)
        {
            CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                            campaign.getPersonnelManager().getCompanyPersonnel(squadron.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(),campaign.getDate());
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
        List<String> enemyTankTypesInMission = new ArrayList<>();
        enemyTankTypesInMission.add("pe2s35");
        enemyTankTypesInMission.add("yak1s69");
        enemyTankTypesInMission.add("il2m41");
        
        AARClaimPanelData claimPanelData = new AARClaimPanelData();
        claimPanelData.setEnemyTankTypesInMission(enemyTankTypesInMission);
        
        preliminaryData.setClaimPanelData(claimPanelData);
    }

    private void makePWCGMMissionData() throws PWCGException
    {
        PwcgMissionData pwcgMissionData = new PwcgMissionData();
        
        MissionHeader missionHeader = makePwcgMissionDataHeader();
        Map<Integer, PwcgGeneratedMissionVehicleData> missionPlanes = makePwcgMissionDataPlanes();

        pwcgMissionData.setMissionHeader(missionHeader);
        pwcgMissionData.setMissionDescription("A test mission");
        pwcgMissionData.setMissionPlanes(missionPlanes);
        
        preliminaryData.setPwcgMissionData(pwcgMissionData);
    }

    private MissionHeader makePwcgMissionDataHeader()
    {
        MissionHeader missionHeader = new MissionHeader();
        missionHeader.setMissionFileName("Test Campaign Patrol 01-11-1941");
        
        missionHeader.setBase("My Airfield");
        missionHeader.setDate("10411101");
        missionHeader.setSquadron("I/JG51");
        missionHeader.setVehicleType("Bf109 F-2");

        
        missionHeader.setDuty("PATROL");
        missionHeader.setAltitude(3000); 
        
        missionHeader.setMapName(PWCGContext.getInstance().getCurrentMap().getMapName()); 

        String formattedTime = DateUtils.getDateAsMissionFileFormat(campaign.getDate());
        missionHeader.setTime(formattedTime);
        return missionHeader;
    }
    

    private Map<Integer, PwcgGeneratedMissionVehicleData> makePwcgMissionDataPlanes() throws PWCGException
    {
        Map<Integer, PwcgGeneratedMissionVehicleData> missionPlanes  = new HashMap<>();
        
        CrewMembers squadronMembersInMission = preliminaryData.getCampaignMembersInMission();
        for (CrewMember crewMember : squadronMembersInMission.getCrewMemberCollection().values())
        {
            Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(crewMember.getCompanyId());
            List<EquippedTank> planesForSquadron = new ArrayList<>(equipment.getActiveEquippedTanks().values());
            int planeIndex = RandomNumberGenerator.getRandom(planesForSquadron.size());
            EquippedTank equippedPlane = planesForSquadron.get(planeIndex);

            PwcgGeneratedMissionVehicleData missionPlaneData = new PwcgGeneratedMissionVehicleData();
            missionPlaneData.setVehicleType(equippedPlane.getType());
            missionPlaneData.setCompanyId(crewMember.getCompanyId());
            missionPlaneData.setCrewMemberName(crewMember.getName());
            missionPlaneData.setCrewMemberSerialNumber(crewMember.getSerialNumber());
            missionPlaneData.setVehicleSerialNumber(equippedPlane.getSerialNumber());
            
            missionPlanes.put(crewMember.getSerialNumber(), missionPlaneData);
        }
        
        return missionPlanes;
    }

}
