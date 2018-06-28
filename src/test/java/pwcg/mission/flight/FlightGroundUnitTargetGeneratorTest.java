package pwcg.mission.flight;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.testutils.KubanAttackMockCampaign;

@RunWith(MockitoJUnitRunner.class)
public class FlightGroundUnitTargetGeneratorTest extends KubanAttackMockCampaign
{
    
    @Before
    public void setup() throws PWCGException
    {
        mockCampaignSetup();
    }

    @Test
    public void diveBombTargetCreationTest() throws PWCGException
    {
        FlightGroundUnitTargetGenerator flightGroundUnitTargetGenerator = new FlightGroundUnitTargetGenerator(mission, campaign, squadron, FlightTypes.DIVE_BOMB, true);
        for (int i = 0; i < 1000; ++i)
        {
            GroundUnitCollection groundUnitCollection = flightGroundUnitTargetGenerator.createGroundUnitsForFlight();
            assert(groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide()) != null);
        }
    }

    @Test
    public void bombTargetCreationTest() throws PWCGException
    {
        FlightGroundUnitTargetGenerator flightGroundUnitTargetGenerator = new FlightGroundUnitTargetGenerator(mission, campaign, squadron, FlightTypes.BOMB, true);
        for (int i = 0; i < 1000; ++i)
        {
            GroundUnitCollection groundUnitCollection = flightGroundUnitTargetGenerator.createGroundUnitsForFlight();
            assert(groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide()) != null);
        }
    }

    @Test
    public void lowAltBombTargetCreationTest() throws PWCGException
    {
        FlightGroundUnitTargetGenerator flightGroundUnitTargetGenerator = new FlightGroundUnitTargetGenerator(mission, campaign, squadron, FlightTypes.LOW_ALT_BOMB, true);
        GroundUnitCollection groundUnitCollection = flightGroundUnitTargetGenerator.createGroundUnitsForFlightWithOverride (TacticalTarget.TARGET_ASSAULT);
        assert(groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide()) != null);
        assert(groundUnitCollection.getTargetDefinition().getTargetType() == TacticalTarget.TARGET_ASSAULT);
    }

    @Test
    public void strategicBombTargetCreationTest() throws PWCGException
    {
        FlightGroundUnitTargetGenerator flightGroundUnitTargetGenerator = new FlightGroundUnitTargetGenerator(mission, campaign, squadron, FlightTypes.STRATEGIC_BOMB, true);
        for (int i = 0; i < 1000; ++i)
        {
            GroundUnitCollection groundUnitCollection = flightGroundUnitTargetGenerator.createGroundUnitsForFlight();
            assert(groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide()) != null);
        }
    }

    @Test
    public void attackTargetCreationTest() throws PWCGException
    {
        FlightGroundUnitTargetGenerator flightGroundUnitTargetGenerator = new FlightGroundUnitTargetGenerator(mission, campaign, squadron, FlightTypes.GROUND_ATTACK, true);
        for (int i = 0; i < 1000; ++i)
        {
            GroundUnitCollection groundUnitCollection = flightGroundUnitTargetGenerator.createGroundUnitsForFlight();
            assert(groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide()) != null);
        }
    }
    
    
}
