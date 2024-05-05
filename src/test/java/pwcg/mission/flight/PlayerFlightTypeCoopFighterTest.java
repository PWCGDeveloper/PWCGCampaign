package pwcg.mission.flight;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.cap.CAPFlight;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.validate.PatrolFlightValidator;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerFlightTypeCoopFighterTest
{
    private static Map<SquadronTestProfile, Campaign> campaigns = new HashMap<>();

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Campaign germanEastCampaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        Campaign germanWestCampaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_26_PROFILE_WEST);
        Campaign americanCampaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.FG_362_PROFILE);
        Campaign britishCampaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.RAF_184_PROFILE);
        Campaign russianCampaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.REGIMENT_11_PROFILE);
        
        campaigns.put(SquadronTestProfile.JG_51_PROFILE_MOSCOW, germanEastCampaign);
        campaigns.put(SquadronTestProfile.JG_26_PROFILE_WEST, germanWestCampaign);
        campaigns.put(SquadronTestProfile.FG_362_PROFILE, americanCampaign);
        campaigns.put(SquadronTestProfile.RAF_184_PROFILE, britishCampaign);
        campaigns.put(SquadronTestProfile.REGIMENT_11_PROFILE, russianCampaign);
    }

    @Test
    public void patrolFlightTest() throws PWCGException
    {
        for (SquadronTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            patrolFlightTestImpl(campaign);
        }
    }
    
    private void patrolFlightTestImpl(Campaign campaign) throws PWCGException
    {
        Mission mission = generateMission(campaign, FlightTypes.PATROL);

        PatrolFlight flight = (PatrolFlight) mission.getFlights().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        Assertions.assertTrue (flight.getFlightType() == FlightTypes.PATROL);
        verifyEnemyFlights(campaign, mission);
    }

    @Test
    public void lowAltPatrolFlightTest() throws PWCGException
    {
        for (SquadronTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            lowAltPatrolFlightTestImpl( campaign);
        }
    }

    private void lowAltPatrolFlightTestImpl(Campaign campaign) throws PWCGException
    {
        Mission mission = generateMission(campaign, FlightTypes.LOW_ALT_PATROL);

        PatrolFlight flight = (PatrolFlight) mission.getFlights().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        Assertions.assertTrue (flight.getFlightType() == FlightTypes.LOW_ALT_PATROL);        
        verifyEnemyFlights(campaign, mission);
    }

    @Test
    public void lowAltCapFlightTest() throws PWCGException
    {
        for (SquadronTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            lowAltCapFlightTestImpl(campaign);
        }
    }

    private void lowAltCapFlightTestImpl(Campaign campaign) throws PWCGException
    {
        Mission mission = generateMission(campaign, FlightTypes.LOW_ALT_CAP);

        CAPFlight flight = (CAPFlight) mission.getFlights().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        Assertions.assertTrue (flight.getFlightType() == FlightTypes.LOW_ALT_CAP);
        verifyEnemyFlights(campaign, mission);
    }

    @Test
    public void interceptFlightTest() throws PWCGException
    {
        for (SquadronTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            interceptFlightTestImpl(campaign);
        }
    }

    private void interceptFlightTestImpl(Campaign campaign) throws PWCGException
    {
        Mission mission = generateMission(campaign, FlightTypes.INTERCEPT);

        InterceptFlight flight = (InterceptFlight) mission.getFlights().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        Assertions.assertTrue (flight.getFlightType() == FlightTypes.INTERCEPT);        
        verifyEnemyFlights(campaign, mission);
    }

    @Test
    public void offensiveFlightTest() throws PWCGException
    {
        for (SquadronTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            offensiveFlightTestImpl(campaign);
        }
    }

    private void offensiveFlightTestImpl(Campaign campaign) throws PWCGException
    {
        Mission mission = generateMission(campaign, FlightTypes.OFFENSIVE);
        
        OffensiveFlight flight = (OffensiveFlight) mission.getFlights().getPlayerFlights().get(0);
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        Assertions.assertTrue (flight.getFlightType() == FlightTypes.OFFENSIVE);        
        verifyEnemyFlights(campaign, mission);
    }
    
    private Mission generateMission(Campaign campaign, FlightTypes flightType) throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), flightType, MissionProfile.DAY_TACTICAL_MISSION);
        mission.finalizeMission();
        return mission;
    }
    
    private int verifyEnemyFlights(Campaign campaign,Mission mission) throws PWCGException 
    {
        Side enemySide = mission.getFlights().getPlayerFlights().get(0).getSquadron().determineEnemySide();
        
        boolean enemyFlightFound = false;
        int numEnemyFlights = 0;
        for (IFlight flight: mission.getFlights().getAllAerialFlights())
        {
            if(flight.getSquadron().determineSide() == enemySide)
            {
                enemyFlightFound = true;
                ++numEnemyFlights;
            }
        }
        
        if (!enemyFlightFound)
        {
            System.out.println("!!!!!No Enemy flights found for campaign " + campaign.getCampaignData().getName() + "  Mission " + mission.getFlights().getPlayerFlights().get(0).getFlightType());
        }
        else
        {
            System.out.println("Enemy flights found is " + numEnemyFlights + " for campaign " + campaign.getCampaignData().getName() + "  Mission " + mission.getFlights().getPlayerFlights().get(0).getFlightType());
        }

        assert(enemyFlightFound);
        return numEnemyFlights;
    }
}
