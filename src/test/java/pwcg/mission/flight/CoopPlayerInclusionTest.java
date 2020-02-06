package pwcg.mission.flight;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberReplacer;
import pwcg.core.exception.PWCGException;
import pwcg.gui.maingui.campaigngenerate.CampaignGeneratorDO;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class CoopPlayerInclusionTest
{
    private Mission mission;
    private  Campaign coopCampaign;

    @Before
    public void fighterFlightTests() throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        PWCGContext.getInstance().setCampaign(coopCampaign);
        generateHumanPilots();
    }

    private void generateHumanPilots() throws Exception
    {
    	CampaignGeneratorDO germanFighterPilot = new CampaignGeneratorDO();
    	germanFighterPilot.setPlayerPilotName("German Fighter");
    	germanFighterPilot.setRank("Leutnant");
    	germanFighterPilot.setSquadName("I./JG52");
    	germanFighterPilot.setCoopUser("GermanFighterUser");
    	createHumanPilot(germanFighterPilot);
    	
        CampaignGeneratorDO germanFighterPilotII = new CampaignGeneratorDO();
        germanFighterPilotII.setPlayerPilotName("German Secondfighter");
        germanFighterPilotII.setRank("Leutnant");
        germanFighterPilotII.setSquadName("I./JG52");
        germanFighterPilotII.setCoopUser("GermanSecondFighterUser");
        createHumanPilot(germanFighterPilotII);

    	CampaignGeneratorDO germanBomberPilot = new CampaignGeneratorDO();
    	germanBomberPilot.setPlayerPilotName("German Bomber");
    	germanBomberPilot.setRank("Leutnant");
    	germanBomberPilot.setSquadName("I./KG76");
    	germanBomberPilot.setCoopUser("GermanBomberUser");
    	createHumanPilot(germanBomberPilot);
    	
    	CampaignGeneratorDO germanStukaPilot = new CampaignGeneratorDO();
    	germanStukaPilot.setPlayerPilotName("German Stuka");
    	germanStukaPilot.setRank("Leutnant");
    	germanStukaPilot.setSquadName("II./St.G.77");
    	germanStukaPilot.setCoopUser("GermanStukaUser");
    	createHumanPilot(germanStukaPilot);
    	
    	CampaignGeneratorDO russianFighterPilot = new CampaignGeneratorDO();
    	russianFighterPilot.setPlayerPilotName("Russian Fighter");
    	russianFighterPilot.setRank("Leyitenant");
    	russianFighterPilot.setSquadName("27th Fighter Air Regiment");
    	russianFighterPilot.setCoopUser("RussianFighterUser");
    	createHumanPilot(russianFighterPilot);
    	
    	CampaignGeneratorDO russianBomberPilot = new CampaignGeneratorDO();
    	russianBomberPilot.setPlayerPilotName("Russian Bomber");
    	russianBomberPilot.setRank("Leyitenant");
    	russianBomberPilot.setSquadName("86th Bomber Air Regiment");
    	russianBomberPilot.setCoopUser("RussianBomberUser");
    	createHumanPilot(russianBomberPilot);
    	
    	CampaignGeneratorDO russianIlPilot = new CampaignGeneratorDO();
    	russianIlPilot.setPlayerPilotName("Russian Il");
    	russianIlPilot.setRank("Leyitenant");
    	russianIlPilot.setSquadName("621st Ground Attack Air Regiment");
    	russianIlPilot.setCoopUser("RussianIlUser");
    	createHumanPilot(russianIlPilot);
    	
    }
    
    private void createHumanPilot(CampaignGeneratorDO campaignGeneratorDO) throws Exception
    {
        String playerName = campaignGeneratorDO.getPlayerPilotName();
        String squadronName = campaignGeneratorDO.getSquadName();
        String rank = campaignGeneratorDO.getRank();
        String coopuser = campaignGeneratorDO.getCoopUser();

        SquadronMemberReplacer squadronMemberReplacer = new SquadronMemberReplacer(coopCampaign);
        squadronMemberReplacer.createPersona(playerName, rank, squadronName, coopuser);
    }
    
    private SquadronMember getSquadronMemberByName(String pilotName) throws PWCGException
    {
    	for (SquadronMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
    	{
    		if (player.getName().contentEquals(pilotName))
    		{
    			return player;
    		}
    	}
    	
    	throw new PWCGException("No player found for name " + pilotName);
    }

    @Test
    public void coopMultiPlayerTest() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        participatingPlayers.addSquadronMember(getSquadronMemberByName("German Fighter"));
        participatingPlayers.addSquadronMember(getSquadronMemberByName("German Bomber"));
        participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Fighter"));
        participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Il"));

        generateMission(participatingPlayers, FlightTypes.ANY);
        assert(mission.getMissionFlightBuilder().getPlayerFlights().size() == 4);
        verifyEnemyFlights();
        boolean germanFighterFound = false;
        boolean germanBomberFound = false;
        boolean russianFighterFound = false;
        boolean russianBomberFound = false;
        for (IFlight flight : mission.getMissionFlightBuilder().getPlayerFlights())
        {
            assert (flight.isPlayerFlight() == true);
            assert (flight.getFlightInformation().isAirStart() == false);
            
            List<PlaneMcu> playerPlanesForFlight = flight.getFlightPlanes().getPlayerPlanes();
            assert (playerPlanesForFlight.size() == 1);
            
            PlaneMcu playerPlane = playerPlanesForFlight.get(0);
            if (playerPlane.getName().contains("German Fighter"))
            {
                germanFighterFound = true;
                assert(playerPlane.isPrimaryRole(Role.ROLE_FIGHTER));
            }
            else if (playerPlane.getName().contains("German Bomber"))
            {
                germanBomberFound = true;
                assert(playerPlane.isPrimaryRole(Role.ROLE_BOMB));
                assert(FlightTypes.isBombingFlight(flight.getFlightType()) == true);
            }
            else if (playerPlane.getName().contains("Russian Fighter"))
            {
                russianFighterFound = true;
                assert(playerPlane.isPrimaryRole(Role.ROLE_FIGHTER));
            }
            else if (playerPlane.getName().contains("Russian Il"))
            {
                russianBomberFound = true;
                assert(playerPlane.isPrimaryRole(Role.ROLE_ATTACK));
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
        participatingPlayers.addSquadronMember(getSquadronMemberByName("German Fighter"));
        participatingPlayers.addSquadronMember(getSquadronMemberByName("German Secondfighter"));
        participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Fighter"));

        generateMission(participatingPlayers, FlightTypes.ANY);
        assert(mission.getMissionFlightBuilder().getPlayerFlights().size() == 2);
        verifyEnemyFlights();
        boolean germanFighterFound = false;
        boolean germanFighter2Found = false;
        boolean russianFighterFound = false;
        for (IFlight flight : mission.getMissionFlightBuilder().getPlayerFlights())
        {
            assert (flight.isPlayerFlight() == true);
            assert (flight.getFlightInformation().isAirStart() == false);
            
            List<PlaneMcu> playerPlanesForFlight = flight.getFlightPlanes().getPlayerPlanes();
            
            for (PlaneMcu playerPlane : playerPlanesForFlight)
            {
                if (playerPlane.getName().contains("German Fighter"))
                {
                    germanFighterFound = true;
                    assert (playerPlanesForFlight.size() == 2);
                    assert(playerPlane.isPrimaryRole(Role.ROLE_FIGHTER));
                }
                else if (playerPlane.getName().contains("German Secondfighter"))
                {
                    germanFighter2Found = true;
                    assert (playerPlanesForFlight.size() == 2);
                    assert(playerPlane.isPrimaryRole(Role.ROLE_FIGHTER));
                }
                else if (playerPlane.getName().contains("Russian Fighter"))
                {
                    russianFighterFound = true;
                    assert (playerPlanesForFlight.size() == 1);
                    assert(playerPlane.isPrimaryRole(Role.ROLE_FIGHTER));
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
        Side enemySide = mission.getMissionFlightBuilder().getPlayerFlights().get(0).getSquadron().determineEnemySide();
        
        boolean enemyFlightFound = false;
        int numEnemyFlights = 0;
        for (IFlight flight: mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            if(flight.getSquadron().determineSide() == enemySide)
            {
                enemyFlightFound = true;
                ++numEnemyFlights;
            }
        }
        
        if (!enemyFlightFound)
        {
            System.out.println("!!!!!No Enemy flights found for campaign " + coopCampaign.getCampaignData().getName() + "  Mission " + mission.getMissionFlightBuilder().getPlayerFlights().get(0).getFlightType());
        }
        else
        {
            System.out.println("Enemy flights found is " + numEnemyFlights + " for campaign " + coopCampaign.getCampaignData().getName() + "  Mission " + mission.getMissionFlightBuilder().getPlayerFlights().get(0).getFlightType());
        }

        assert(enemyFlightFound);
        return numEnemyFlights;
    }
}
