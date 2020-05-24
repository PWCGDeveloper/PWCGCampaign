package pwcg.mission.flight;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.validate.BombingAirStartFlightValidator;
import pwcg.mission.flight.validate.GroundAttackFlightValidator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

public class BomberAirStartTest 
{    
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);
        
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.AllowAirStartsKey, "1");
    }

    @Test
    public void bombFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.BOMB, MissionProfile.DAY_TACTICAL_MISSION);
        BombingFlight flight = (BombingFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        mission.finalizeMission();

        BombingAirStartFlightValidator airStartFlightValidator = new BombingAirStartFlightValidator();
        airStartFlightValidator.validateBomberFlight(flight);

        GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
        groundAttackFlightValidator.validateGroundAttackFlight(flight);
        assert (flight.getFlightType() == FlightTypes.BOMB);
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() >= 0);
        }
    }

    @Test
    public void lowAltBombFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.LOW_ALT_BOMB, MissionProfile.DAY_TACTICAL_MISSION);
        BombingFlight flight = (BombingFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        mission.finalizeMission();

        BombingAirStartFlightValidator airStartFlightValidator = new BombingAirStartFlightValidator();
        airStartFlightValidator.validateBomberFlight(flight);

        GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
        groundAttackFlightValidator.validateGroundAttackFlight(flight);
        assert (flight.getFlightType() == FlightTypes.LOW_ALT_BOMB);
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() >= 0);
        }
    }

}
