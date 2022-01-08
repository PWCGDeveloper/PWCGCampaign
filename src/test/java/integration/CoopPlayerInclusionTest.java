package integration;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberReplacer;
import pwcg.campaign.plane.PwcgRole;
import pwcg.coop.CoopUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.maingui.campaigngenerate.CampaignGeneratorDO;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoopPlayerInclusionTest
{
    private Mission mission;
    private Campaign coopCampaign;    

    @BeforeAll
    public void setupSuite() throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        generateCoopUsers();
        generateHumanCrewMembers();
    }

    @AfterAll
    public void teardownSuite() throws Exception
    {
        removeCoopUsers();
    }

    private static void generateCoopUsers() throws PWCGException
    {
        CoopUserManager.getIntance().buildCoopUser("GermanFighterUser");
        CoopUserManager.getIntance().buildCoopUser("GermanSecondFighterUser");
        CoopUserManager.getIntance().buildCoopUser("GermanBomberUser");
        CoopUserManager.getIntance().buildCoopUser("GermanStukaUser");
        CoopUserManager.getIntance().buildCoopUser("RussianFighterUser");
        CoopUserManager.getIntance().buildCoopUser("RussianBomberUser");
        CoopUserManager.getIntance().buildCoopUser("RussianIlUser");
    }

    private static void removeCoopUsers() throws PWCGException
    {
        CoopUserManager.getIntance().removeCoopUser("GermanFighterUser");
        CoopUserManager.getIntance().removeCoopUser("GermanSecondFighterUser");
        CoopUserManager.getIntance().removeCoopUser("GermanBomberUser");
        CoopUserManager.getIntance().removeCoopUser("GermanStukaUser");
        CoopUserManager.getIntance().removeCoopUser("RussianFighterUser");
        CoopUserManager.getIntance().removeCoopUser("RussianBomberUser");
        CoopUserManager.getIntance().removeCoopUser("RussianIlUser");
    }

    private void generateHumanCrewMembers() throws Exception
    {
    	CampaignGeneratorDO germanFighterCrewMember = new CampaignGeneratorDO();
    	germanFighterCrewMember.setPlayerCrewMemberName("German Fighter");
    	germanFighterCrewMember.setRank("Leutnant");
    	germanFighterCrewMember.setSquadName("I./JG52");
    	germanFighterCrewMember.setCoopUser("GermanFighterUser");
    	createHumanCrewMember(germanFighterCrewMember);
    	
        CampaignGeneratorDO germanFighterCrewMemberII = new CampaignGeneratorDO();
        germanFighterCrewMemberII.setPlayerCrewMemberName("German Secondfighter");
        germanFighterCrewMemberII.setRank("Leutnant");
        germanFighterCrewMemberII.setSquadName("I./JG52");
        germanFighterCrewMemberII.setCoopUser("GermanSecondFighterUser");
        createHumanCrewMember(germanFighterCrewMemberII);

    	CampaignGeneratorDO germanBomberCrewMember = new CampaignGeneratorDO();
    	germanBomberCrewMember.setPlayerCrewMemberName("German Bomber");
    	germanBomberCrewMember.setRank("Leutnant");
    	germanBomberCrewMember.setSquadName("I./KG76");
    	germanBomberCrewMember.setCoopUser("GermanBomberUser");
    	createHumanCrewMember(germanBomberCrewMember);
    	
    	CampaignGeneratorDO germanStukaCrewMember = new CampaignGeneratorDO();
    	germanStukaCrewMember.setPlayerCrewMemberName("German Stuka");
    	germanStukaCrewMember.setRank("Leutnant");
    	germanStukaCrewMember.setSquadName("II./St.G.77");
    	germanStukaCrewMember.setCoopUser("GermanStukaUser");
    	createHumanCrewMember(germanStukaCrewMember);
    	
    	CampaignGeneratorDO russianFighterCrewMember = new CampaignGeneratorDO();
    	russianFighterCrewMember.setPlayerCrewMemberName("Russian Fighter");
    	russianFighterCrewMember.setRank("Leyitenant");
    	russianFighterCrewMember.setSquadName("27th Fighter Air Regiment");
    	russianFighterCrewMember.setCoopUser("RussianFighterUser");
    	createHumanCrewMember(russianFighterCrewMember);
    	
    	CampaignGeneratorDO russianBomberCrewMember = new CampaignGeneratorDO();
    	russianBomberCrewMember.setPlayerCrewMemberName("Russian Bomber");
    	russianBomberCrewMember.setRank("Leyitenant");
    	russianBomberCrewMember.setSquadName("86th Bomber Air Regiment");
    	russianBomberCrewMember.setCoopUser("RussianBomberUser");
    	createHumanCrewMember(russianBomberCrewMember);
    	
    	CampaignGeneratorDO russianIlCrewMember = new CampaignGeneratorDO();
    	russianIlCrewMember.setPlayerCrewMemberName("Russian Il");
    	russianIlCrewMember.setRank("Leyitenant");
    	russianIlCrewMember.setSquadName("621st Ground Attack Air Regiment");
    	russianIlCrewMember.setCoopUser("RussianIlUser");
    	createHumanCrewMember(russianIlCrewMember);
    	
    }
    
    private void createHumanCrewMember(CampaignGeneratorDO campaignGeneratorDO) throws Exception
    {
        String playerName = campaignGeneratorDO.getPlayerCrewMemberName();
        String squadronName = campaignGeneratorDO.getSquadName();
        String rank = campaignGeneratorDO.getRank();
        String coopuser = campaignGeneratorDO.getCoopUser();

        CrewMemberReplacer squadronMemberReplacer = new CrewMemberReplacer(coopCampaign);
        squadronMemberReplacer.createPersona(playerName, rank, squadronName, coopuser);
    }
    
    private CrewMember getCrewMemberByName(String crewMemberName) throws PWCGException
    {
    	for (CrewMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
    	{
    		if (player.getName().contentEquals(crewMemberName))
    		{
    			return player;
    		}
    	}
    	
    	throw new PWCGException("No player found for name " + crewMemberName);
    }

    @Test
    public void coopMultiPlayerTest() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        participatingPlayers.addCrewMember(getCrewMemberByName("German Fighter"));
        participatingPlayers.addCrewMember(getCrewMemberByName("German Bomber"));
        participatingPlayers.addCrewMember(getCrewMemberByName("Russian Fighter"));
        participatingPlayers.addCrewMember(getCrewMemberByName("Russian Il"));

        generateMission(participatingPlayers, FlightTypes.ANY);
        assert(mission.getFlights().getUnits().size() == 4);
        verifyEnemyFlights();
        boolean germanFighterFound = false;
        boolean germanBomberFound = false;
        boolean russianFighterFound = false;
        boolean russianBomberFound = false;
        for (IFlight flight : mission.getFlights().getUnits())
        {
            Assertions.assertTrue (flight.isPlayerFlight() == true);
            Assertions.assertTrue (flight.getFlightInformation().isAirStart() == false);
            
            List<PlaneMcu> playerPlanesForFlight = flight.getFlightPlanes().getPlayerPlanes();
            Assertions.assertTrue (playerPlanesForFlight.size() == 1);
            
            PlaneMcu playerPlane = playerPlanesForFlight.get(0);
            if (playerPlane.getName().contains("German Fighter"))
            {
                germanFighterFound = true;
                assert(playerPlane.isPrimaryRole(PwcgRole.ROLE_FIGHTER));
            }
            else if (playerPlane.getName().contains("German Bomber"))
            {
                germanBomberFound = true;
                assert(playerPlane.isPrimaryRole(PwcgRole.ROLE_BOMB));
            }
            else if (playerPlane.getName().contains("Russian Fighter"))
            {
                russianFighterFound = true;
                assert(playerPlane.isPrimaryRole(PwcgRole.ROLE_FIGHTER));
            }
            else if (playerPlane.getName().contains("Russian Il"))
            {
                russianBomberFound = true;
                assert(playerPlane.isPrimaryRole(PwcgRole.ROLE_ATTACK));
            }
            
        }
        assert(germanFighterFound);
        assert(germanBomberFound);
        assert(russianFighterFound);
        assert(russianBomberFound);
    }
    

    @Test
    public void coopMultiPlayerWithSameSquadronTest() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        participatingPlayers.addCrewMember(getCrewMemberByName("German Fighter"));
        participatingPlayers.addCrewMember(getCrewMemberByName("German Secondfighter"));
        participatingPlayers.addCrewMember(getCrewMemberByName("Russian Fighter"));

        generateMission(participatingPlayers, FlightTypes.ANY);
        assert(mission.getFlights().getUnits().size() == 2);
        verifyEnemyFlights();
        boolean germanFighterFound = false;
        boolean germanFighter2Found = false;
        boolean russianFighterFound = false;
        for (IFlight flight : mission.getFlights().getUnits())
        {
            Assertions.assertTrue (flight.isPlayerFlight() == true);
            Assertions.assertTrue (flight.getFlightInformation().isAirStart() == false);
            
            List<PlaneMcu> playerPlanesForFlight = flight.getFlightPlanes().getPlayerPlanes();
            
            for (PlaneMcu playerPlane : playerPlanesForFlight)
            {
                if (playerPlane.getName().contains("German Fighter"))
                {
                    germanFighterFound = true;
                    Assertions.assertTrue (playerPlanesForFlight.size() == 2);
                    assert(playerPlane.isPrimaryRole(PwcgRole.ROLE_FIGHTER));
                }
                else if (playerPlane.getName().contains("German Secondfighter"))
                {
                    germanFighter2Found = true;
                    Assertions.assertTrue (playerPlanesForFlight.size() == 2);
                    assert(playerPlane.isPrimaryRole(PwcgRole.ROLE_FIGHTER));
                }
                else if (playerPlane.getName().contains("Russian Fighter"))
                {
                    russianFighterFound = true;
                    Assertions.assertTrue (playerPlanesForFlight.size() == 1);
                    assert(playerPlane.isPrimaryRole(PwcgRole.ROLE_FIGHTER));
                }
            }
        }
        assert(germanFighterFound);
        assert(germanFighter2Found);
        assert(russianFighterFound);
    }
    

    private void generateMission(MissionHumanParticipants participatingPlayers, FlightTypes flightType) throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(coopCampaign);
        mission = missionGenerator.makeMission(participatingPlayers);
        mission.finalizeMission();
    }
    
    private int verifyEnemyFlights() throws PWCGException 
    {
        Side enemySide = mission.getFlights().getUnits().get(0).getCompany().determineEnemySide();
        
        boolean enemyFlightFound = false;
        int numEnemyFlights = 0;
        for (IFlight flight: mission.getFlights().getAllAerialFlights())
        {
            if(flight.getCompany().determineSide() == enemySide)
            {
                enemyFlightFound = true;
                ++numEnemyFlights;
            }
        }
        
        if (!enemyFlightFound)
        {
            System.out.println("!!!!!No Enemy flights found for campaign " + coopCampaign.getCampaignData().getName() + "  Mission " + mission.getFlights().getUnits().get(0).getFlightType());
        }
        else
        {
            System.out.println("Enemy flights found is " + numEnemyFlights + " for campaign " + coopCampaign.getCampaignData().getName() + "  Mission " + mission.getFlights().getUnits().get(0).getFlightType());
        }

        assert(enemyFlightFound);
        return numEnemyFlights;
    }
}
