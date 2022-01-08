package integration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerVictoryDeclaration;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.logfiles.event.AType0;
import pwcg.core.logfiles.event.AType12;
import pwcg.core.logfiles.event.AType2;
import pwcg.core.logfiles.event.AType3;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBorderBuilder;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.io.MissionFileNameBuilder;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class VictoryEvaluationTest
{
    private int ENEMY_START_PLANE_ID = 301000;
    private int GERMAN_START_PLANE_ID = 401000;

    private Campaign campaign;
    
    @BeforeEach
    public void testSetup() throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaignOnDisk(SquadronTestProfile.JASTA_11_PROFILE);
    }
    
    @Test
    public void testSingleVictoryClaimedAndAwarded() throws Exception
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        
        Company playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(FlightTypes.PATROL, playerSquadron);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null, playerFlightTypes);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);
        mission.finalizeMission();
        mission.write();

        BufferedWriter writer = makeLogFileWriter();
        int playerAid = buildMissionEntities(writer, mission);

        makeDestroyedEntry(writer, playerAid, ENEMY_START_PLANE_ID+1);
        writer.close();

        PlayerVictoryDeclaration playerVictoryDeclaration1 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration1.setAircraftType("spad13");
        
        PlayerDeclarations playerDeclarations = new PlayerDeclarations();
        playerDeclarations.addDeclaration(playerVictoryDeclaration1);

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getAirToAirVictories().size());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(1, playerAfter.getCrewMemberVictories().getAirToAirVictories().size());
    }

    @Test
    public void testTwoVictoriesClaimedAndAwardedWithOneFuzzyVictory() throws Exception
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        
        Company playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(FlightTypes.PATROL, playerSquadron);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null, playerFlightTypes);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);
        mission.finalizeMission();
        mission.write();

        BufferedWriter writer = makeLogFileWriter();
        int playerAid = buildMissionEntities(writer, mission);

        makeDamageEntry(writer, playerAid, ENEMY_START_PLANE_ID);
        makeDamageEntry(writer, playerAid, ENEMY_START_PLANE_ID+1);

        makeDestroyedEntry(writer, playerAid, ENEMY_START_PLANE_ID);
        makeDestroyedEntry(writer, -1, ENEMY_START_PLANE_ID+1);
        writer.close();

        PlayerVictoryDeclaration playerVictoryDeclaration1 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration1.setAircraftType("sopcamel");

        PlayerVictoryDeclaration playerVictoryDeclaration2 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration2.setAircraftType("sopcamel");
        
        PlayerDeclarations playerDeclarations = new PlayerDeclarations();
        playerDeclarations.addDeclaration(playerVictoryDeclaration1);
        playerDeclarations.addDeclaration(playerVictoryDeclaration2);

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getAirToAirVictories().size());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(2, playerAfter.getCrewMemberVictories().getAirToAirVictories().size());
    }

    @Test
    public void testTwoVictoriesClaimedOneAwardedBecauseNoDamage() throws Exception
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        
        Company playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(FlightTypes.PATROL, playerSquadron);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null, playerFlightTypes);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);
        mission.finalizeMission();
        mission.write();

        BufferedWriter writer = makeLogFileWriter();
        int playerAid = buildMissionEntities(writer, mission);

        makeDestroyedEntry(writer, playerAid, ENEMY_START_PLANE_ID);
        makeDestroyedEntry(writer, -1, ENEMY_START_PLANE_ID+1);
        writer.close();

        PlayerVictoryDeclaration playerVictoryDeclaration1 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration1.setAircraftType("sopcamel");

        PlayerVictoryDeclaration playerVictoryDeclaration2 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration2.setAircraftType("sopcamel");
        
        PlayerDeclarations playerDeclarations = new PlayerDeclarations();
        playerDeclarations.addDeclaration(playerVictoryDeclaration1);
        playerDeclarations.addDeclaration(playerVictoryDeclaration2);

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getAirToAirVictories().size());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(1, playerAfter.getCrewMemberVictories().getAirToAirVictories().size());
    }

    @Test
    public void testNoVictoriesClaimedAndAwarded() throws Exception
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        
        Company playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(FlightTypes.PATROL, playerSquadron);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null, playerFlightTypes);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);
        mission.finalizeMission();
        mission.write();
        
        List<PlaneMcu> playerFlightPlanes = mission.getFlights().getUnits().get(0).getFlightPlanes().getPlanes();
        Map<Integer, Integer> playerFlightVictories = new HashMap<>();
        for(PlaneMcu plane : playerFlightPlanes)
        {
            playerFlightVictories.put(plane.getCrewMember().getSerialNumber(), plane.getCrewMember().getCrewMemberVictories().getAirToAirVictories().size());
        }


        BufferedWriter writer = makeLogFileWriter();
        int playerAid = buildMissionEntities(writer, mission);

        makeDestroyedEntry(writer, playerAid, ENEMY_START_PLANE_ID+1);
        writer.close();

        PlayerDeclarations playerDeclarations = new PlayerDeclarations();

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getAirToAirVictories().size());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, playerAfter.getCrewMemberVictories().getAirToAirVictories().size());
        
        boolean wasAwardedToCrewMember = false;
        for (int serialNumber : playerFlightVictories.keySet())
        {
            int numVictoriesBefore = playerFlightVictories.get(serialNumber);
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (numVictoriesBefore < crewMember.getCrewMemberVictories().getAirToAirVictories().size())
            {
                wasAwardedToCrewMember = true;
            }
        }
        Assertions.assertTrue(wasAwardedToCrewMember);
    }

    private int buildMissionEntities(BufferedWriter writer, Mission mission) throws Exception
    {
        formMissionheader(writer, mission);
        makeEnemyPlanes(writer, mission);
        int playerAid = makeGermanPlanes(writer, mission);
        return playerAid;
    }

    private BufferedWriter makeLogFileWriter() throws Exception
    {
        String missionLogFileName = buildFileName();

        String filePath = "..\\data\\" + missionLogFileName;
        File logFile = new File(filePath);     
        BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
        return writer;
    }

    private String buildFileName()
    {
        String dateTimeString = DateUtils.getDateStringDashDelimitedYYYYMMDD(new Date());
        dateTimeString += "_12-00-00";
        String filename = "missionReport(" + dateTimeString +  ")[0].txt";
        return filename;
    }

    private void formMissionheader(BufferedWriter writer, Mission mission) throws PWCGException
    {        
        String missionFileName = MissionFileNameBuilder.buildMissionFileName(mission.getCampaign()) + ".mission" ;

        AType0 aType0 = new AType0(mission.getCampaign().getName());
        aType0.setMissionFileName(missionFileName);
        aType0.write(writer);
    }

    private void makeEnemyPlanes(BufferedWriter writer, Mission mission) throws PWCGException
    {       
        Coordinate location = new Coordinate(500000, 0, 50000);

        List<PlaneMcu> enemyPlanes = findEnemyCrewMembers(mission);
        for (PlaneMcu enemyPlane : enemyPlanes)
        {
            int planeId =  ENEMY_START_PLANE_ID + enemyPlane.getNumberInFormation() - 1;
            int botId =  planeId +100 ;
            
            AType12 aType12 = new AType12(Integer.valueOf(planeId).toString(), enemyPlane.getType(), enemyPlane.getName(), 
                    enemyPlane.getCountry(), "-1", location);
            aType12.write(writer);

            AType12 aType1Bot = new AType12(Integer.valueOf(botId).toString(), "BotCrewMember_" + enemyPlane.getType(), "BotCrewMember_" + enemyPlane.getType(), 
                    enemyPlane.getCountry(), Integer.valueOf(planeId).toString(), location);
            aType1Bot.write(writer);
        }
    }
    
    private List<PlaneMcu> findEnemyCrewMembers(Mission mission) throws PWCGException
    {
        for(IFlight flight : mission.getFlights().getAiFlightsForSide(Side.ALLIED))
        {
            List<PlaneMcu> enemyPlanesForFlight = flight.getFlightPlanes().getAiPlanes();
            if (enemyPlanesForFlight.size() >= 2)
            {
                if (flight.getCompany().determineSquadronPrimaryRoleCategory(mission.getCampaign().getDate()) == PwcgRoleCategory.FIGHTER)
                {
                     return enemyPlanesForFlight;                    
                }
            }
        }
        
        throw new PWCGException("No enemy flight large enough");
    }

    private int makeGermanPlanes(BufferedWriter writer, Mission mission) throws PWCGException
    {        
        int playerAid = -1;
        
        Coordinate location = new Coordinate(500000, 0, 50000);
        List<PlaneMcu> playerFlightPlanes = mission.getFlights().getUnits().get(0).getFlightPlanes().getPlanes();
        
        for (PlaneMcu friendlyPlane : playerFlightPlanes)
        {
            int planeId =  GERMAN_START_PLANE_ID + friendlyPlane.getNumberInFormation() - 1;
            int botId =  planeId +100;
            
            AType12 aType12 = new AType12(Integer.valueOf(planeId).toString(), friendlyPlane.getType(), friendlyPlane.getName(), 
                    CountryFactory.makeCountryByCountry(Country.GERMANY), "-1", location);
            aType12.write(writer);

            AType12 aType1Bot = new AType12(Integer.valueOf(botId).toString(), "BotCrewMember_" + friendlyPlane.getType(), "BotCrewMember_" + friendlyPlane.getType(), 
                    CountryFactory.makeCountryByCountry(Country.GERMANY), Integer.valueOf(planeId).toString(), location);
            aType1Bot.write(writer);
            
            if (friendlyPlane.isActivePlayerPlane())
            {
                playerAid = planeId;
            }            
        }
        
        return playerAid;
    }

    private void makeDamageEntry(BufferedWriter writer, int shooter, int victim) throws PWCGException
    {        
        Coordinate location = new Coordinate(500000, 0, 50000);
        AType2 aType2 = new AType2(Integer.valueOf(shooter).toString(), Integer.valueOf(victim).toString(), 0.50, location);
        aType2.write(writer);
    }

    private void makeDestroyedEntry(BufferedWriter writer, int victor, int victim) throws PWCGException
    {        
        Coordinate location = new Coordinate(500000, 0, 50000);
        AType3 aType3 = new AType3(Integer.valueOf(victor).toString(), Integer.valueOf(victim).toString(), location);
        aType3.write(writer);
    }
}
