package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.validate.GroundUnitValidator;
import pwcg.mission.flight.validate.PatrolFlightValidator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

public class PlayerFlightTypeBoSFighterTest
{
    Mission mission;
    List<Campaign> campaigns = new ArrayList<>();

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Campaign germanEastCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE);
        Campaign germanWestCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE_WEST);
        Campaign americanCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.FG_362_PROFILE);
        Campaign britishCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.SQ_184_PROFILE);
        Campaign russianCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.FIGHTER_11_PROFILE);
        
        campaigns.add(germanEastCampaign);
        campaigns.add(germanWestCampaign);
        campaigns.add(americanCampaign);
        campaigns.add(britishCampaign);
        campaigns.add(russianCampaign);
    }

    @Test
    public void patrolFlightTest() throws PWCGException
    {
        for (Campaign campaign: campaigns) 
        {
            PWCGContextManager.getInstance().setCampaign(campaign);
            patrolFlightTestImpl(campaign);
        }
    }
    
    private void patrolFlightTestImpl(Campaign campaign) throws PWCGException
    {
        generateMission(campaign, FlightTypes.PATROL);

        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlight();
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
        for (Campaign campaign: campaigns) 
        {
            PWCGContextManager.getInstance().setCampaign(campaign);
            lowAltPatrolFlightTestImpl(campaign);
        }
    }

    private void lowAltPatrolFlightTestImpl(Campaign campaign) throws PWCGException
    {
        generateMission(campaign, FlightTypes.LOW_ALT_PATROL);

        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlight();
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
        for (Campaign campaign: campaigns) 
        {
            PWCGContextManager.getInstance().setCampaign(campaign);
            lowAltCapFlightTestImpl(campaign);
        }
    }

    private void lowAltCapFlightTestImpl(Campaign campaign) throws PWCGException
    {
        generateMission(campaign, FlightTypes.LOW_ALT_CAP);

        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.LOW_ALT_CAP);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() == 0);
        }
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights(campaign);
    }

    @Test
    public void interceptFlightTest() throws PWCGException
    {
        for (Campaign campaign: campaigns) 
        {
            PWCGContextManager.getInstance().setCampaign(campaign);
            interceptFlightTestImpl(campaign);
        }
    }

    private void interceptFlightTestImpl(Campaign campaign) throws PWCGException
    {
        generateMission(campaign, FlightTypes.INTERCEPT);

        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlight();
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
        for (Campaign campaign: campaigns) 
        {
            PWCGContextManager.getInstance().setCampaign(campaign);
            offensiveFlightTestImpl(campaign);
        }
    }

    private void offensiveFlightTestImpl(Campaign campaign) throws PWCGException
    {
        generateMission(campaign, FlightTypes.OFFENSIVE);
        
        OffensiveFlight flight = (OffensiveFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.OFFENSIVE);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        verifyEnemyFlights(campaign);
    }
    
    private void generateMission(Campaign campaign, FlightTypes flightType) throws PWCGException
    {
        mission = new Mission();
        mission.initialize(campaign);
        mission.generate(flightType);
        mission.finalizeMission();
    }
    
    private int verifyEnemyFlights(Campaign campaign) throws PWCGException 
    {
        Side enemySide = campaign.determineSide().getOppositeSide();
        
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
            System.out.println("!!!!!No Enemy flights found for campaign " + campaign.getCampaignData().getName() + "  Mission " + mission.getMissionFlightBuilder().getPlayerFlight().getFlightType());
        }
        else
        {
            System.out.println("Enemy flights found is " + numEnemyFlights + " for campaign " + campaign.getCampaignData().getName() + "  Mission " + mission.getMissionFlightBuilder().getPlayerFlight().getFlightType());
        }

        assert(enemyFlightFound);
        return numEnemyFlights;
    }
}
