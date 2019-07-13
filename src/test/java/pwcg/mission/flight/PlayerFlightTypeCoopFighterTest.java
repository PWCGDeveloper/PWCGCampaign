package pwcg.mission.flight;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.validate.GroundUnitValidator;
import pwcg.mission.flight.validate.PatrolFlightValidator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

public class PlayerFlightTypeCoopFighterTest
{
    Mission mission;
    Map<SquadrontTestProfile, Campaign> campaigns = new HashMap<>();

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Campaign germanEastCampaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
        Campaign germanWestCampaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_WEST);
        Campaign americanCampaign = CampaignCache.makeCampaign(SquadrontTestProfile.FG_362_PROFILE);
        Campaign britishCampaign = CampaignCache.makeCampaign(SquadrontTestProfile.RAF_184_PROFILE);
        Campaign russianCampaign = CampaignCache.makeCampaign(SquadrontTestProfile.REGIMENT_11_PROFILE);
        
        campaigns.put(SquadrontTestProfile.JG_51_PROFILE_MOSCOW, germanEastCampaign);
        campaigns.put(SquadrontTestProfile.JG_51_PROFILE_WEST, germanWestCampaign);
        campaigns.put(SquadrontTestProfile.FG_362_PROFILE, americanCampaign);
        campaigns.put(SquadrontTestProfile.RAF_184_PROFILE, britishCampaign);
        campaigns.put(SquadrontTestProfile.REGIMENT_11_PROFILE, russianCampaign);
    }

    @Test
    public void patrolFlightTest() throws PWCGException
    {
        for (SquadrontTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            PWCGContextManager.getInstance().setCampaign(campaign);
            patrolFlightTestImpl(profile, campaign);
        }
    }
    
    private void patrolFlightTestImpl(SquadrontTestProfile profile, Campaign campaign) throws PWCGException
    {
        generateMission(profile, campaign, FlightTypes.PATROL);

        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.PATROL);
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights(campaign);
    }

    @Test
    public void lowAltPatrolFlightTest() throws PWCGException
    {
        for (SquadrontTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            PWCGContextManager.getInstance().setCampaign(campaign);
            lowAltPatrolFlightTestImpl(profile, campaign);
        }
    }

    private void lowAltPatrolFlightTestImpl(SquadrontTestProfile profile, Campaign campaign) throws PWCGException
    {
        generateMission(profile, campaign, FlightTypes.LOW_ALT_PATROL);

        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.LOW_ALT_PATROL);        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights(campaign);
    }

    @Test
    public void lowAltCapFlightTest() throws PWCGException
    {
        for (SquadrontTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            PWCGContextManager.getInstance().setCampaign(campaign);
            lowAltCapFlightTestImpl(profile, campaign);
        }
    }

    private void lowAltCapFlightTestImpl(SquadrontTestProfile profile, Campaign campaign) throws PWCGException
    {
        generateMission(profile, campaign, FlightTypes.LOW_ALT_CAP);

        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.LOW_ALT_CAP);
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights(campaign);
    }

    @Test
    public void interceptFlightTest() throws PWCGException
    {
        for (SquadrontTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            PWCGContextManager.getInstance().setCampaign(campaign);
            interceptFlightTestImpl(profile, campaign);
        }
    }

    private void interceptFlightTestImpl(SquadrontTestProfile profile, Campaign campaign) throws PWCGException
    {
        generateMission(profile, campaign, FlightTypes.INTERCEPT);

        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.INTERCEPT);        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights(campaign);
    }

    @Test
    public void offensiveFlightTest() throws PWCGException
    {
        for (SquadrontTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            PWCGContextManager.getInstance().setCampaign(campaign);
            offensiveFlightTestImpl(profile, campaign);
        }
    }

    private void offensiveFlightTestImpl(SquadrontTestProfile profile, Campaign campaign) throws PWCGException
    {
        generateMission(profile, campaign, FlightTypes.OFFENSIVE);
        
        OffensiveFlight flight = (OffensiveFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.OFFENSIVE);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights(campaign);
    }
    
    private void generateMission(SquadrontTestProfile profile, Campaign campaign, FlightTypes flightType) throws PWCGException
    {
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate(flightType);
        mission.finalizeMission();
    }
    
    private int verifyEnemyFlights(Campaign campaign) throws PWCGException 
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
            System.out.println("!!!!!No Enemy flights found for campaign " + campaign.getCampaignData().getName() + "  Mission " + mission.getMissionFlightBuilder().getPlayerFlights().get(0).getFlightType());
        }
        else
        {
            System.out.println("Enemy flights found is " + numEnemyFlights + " for campaign " + campaign.getCampaignData().getName() + "  Mission " + mission.getMissionFlightBuilder().getPlayerFlights().get(0).getFlightType());
        }

        assert(enemyFlightFound);
        return numEnemyFlights;
    }
}
