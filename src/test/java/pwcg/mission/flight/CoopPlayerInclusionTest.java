package pwcg.mission.flight;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberReplacer;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.gui.maingui.campaigngenerate.CampaignGeneratorDO;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.validate.GroundUnitValidator;
import pwcg.mission.flight.validate.PatrolFlightValidator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

public class CoopPlayerInclusionTest
{
    private Mission mission;
    private  Campaign coopCampaign;

    @Before
    public void fighterFlightTests() throws Exception
    {
        PWCGContextManager.setRoF(false);
        coopCampaign = CampaignCache.makeCampaign(SquadrontTestProfile.COOP_PROFILE);
        PWCGContextManager.getInstance().setCampaign(coopCampaign);
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
        squadronMemberReplacer.createPilot(playerName, rank, squadronName, coopuser);
    }
    
    private SquadronMember getSquadronMemberByName(String pilotName) throws PWCGException
    {
    	for (SquadronMember player : coopCampaign.getPersonnelManager().getAllPlayers().getSquadronMemberList())
    	{
    		if (player.getName().contentEquals(pilotName))
    		{
    			return player;
    		}
    	}
    	
    	throw new PWCGException("No player found for name " + pilotName);
    }

    @Test
    public void patrolFlightTest() throws PWCGException
    {
    	MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("German Fighter"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("German Bomber"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Fighter"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Il"));
    	
        patrolFlightTestImpl(participatingPlayers);
    }
    
    private void patrolFlightTestImpl(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        generateMission(participatingPlayers, FlightTypes.PATROL);

        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.PATROL);
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights();
    }

    @Test
    public void lowAltPatrolFlightTest() throws PWCGException
    {
    	MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("German Fighter"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("German Bomber"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Fighter"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Il"));
    	
        patrolFlightTestImpl(participatingPlayers);
        lowAltPatrolFlightTestImpl(participatingPlayers);
    }

    private void lowAltPatrolFlightTestImpl(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        generateMission(participatingPlayers, FlightTypes.LOW_ALT_PATROL);

        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.LOW_ALT_PATROL);        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights();
    }

    @Test
    public void lowAltCapFlightTest() throws PWCGException
    {
    	MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("German Fighter"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("German Bomber"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Fighter"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Il"));
    	
    	lowAltCapFlightTestImpl(participatingPlayers);
    }

    private void lowAltCapFlightTestImpl(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        generateMission(participatingPlayers, FlightTypes.LOW_ALT_CAP);

        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.LOW_ALT_CAP);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() == 0);
        }
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights();
    }

    @Test
    public void interceptFlightTest() throws PWCGException
    {
    	MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("German Fighter"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("German Bomber"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Fighter"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Il"));
    	
    	interceptFlightTestImpl(participatingPlayers);
    }

    private void interceptFlightTestImpl(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        generateMission(participatingPlayers, FlightTypes.INTERCEPT);

        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.INTERCEPT);        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights();
    }

    @Test
    public void offensiveFlightTest() throws PWCGException
    {
    	MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("German Fighter"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("German Bomber"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Fighter"));
    	participatingPlayers.addSquadronMember(getSquadronMemberByName("Russian Il"));
    	
    	offensiveFlightTestImpl(participatingPlayers);
    }

    private void offensiveFlightTestImpl(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        generateMission(participatingPlayers, FlightTypes.OFFENSIVE);
        
        OffensiveFlight flight = (OffensiveFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.OFFENSIVE);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights();
    }
    
    private void generateMission(MissionHumanParticipants participatingPlayers, FlightTypes flightType) throws PWCGException
    {
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        mission = new Mission(coopCampaign, participatingPlayers, missionBorders);
        mission.generate(flightType);
        mission.finalizeMission();
    }
    
    private int verifyEnemyFlights() throws PWCGException 
    {
        Side enemySide = mission.getMissionFlightBuilder().getPlayerFlights().get(0).getSquadron().determineEnemySide();
        
        boolean enemyFlightFound = false;
        int numEnemyFlights = 0;
        for (Flight flight: mission.getMissionFlightBuilder().getAllAerialFlights())
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
