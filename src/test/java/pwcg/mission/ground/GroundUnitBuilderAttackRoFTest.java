package pwcg.mission.ground;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.unit.GroundUnitBuilderAttack;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.testutils.FranceAttackMockCampaign;

@RunWith(MockitoJUnitRunner.class)
public class GroundUnitBuilderAttackRoFTest extends FranceAttackMockCampaign
{
    private TargetDefinition targetDefinition = new TargetDefinition();


    @Before
    public void setup() throws PWCGException
    {
        mockCampaignSetup();
        
        targetDefinition.setAttackingCountry(country);
        targetDefinition.setTargetCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        targetDefinition.setTargetPosition(new Coordinate(100000, 0, 150000));
        targetDefinition.setTargetOrientation(new Orientation());
        targetDefinition.setDate(date);
        targetDefinition.setPreferredRadius(80000);
        targetDefinition.setMaximumRadius(120000);
        targetDefinition.setTargetName("Target");
        
    }

    @Test
    public void createTrainTargetTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_TRAIN);

        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        GroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createAirfieldUnitTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_AIRFIELD);

        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        GroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createTruckConvoyTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_TRANSPORT);

        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        GroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createGroundArtilleryBatteryTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_ARTILLERY);

        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        GroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createDrifterTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_DRIFTER);

        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        GroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createAssaultTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_ASSAULT);

        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        GroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createDefenseTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_DEFENSE);

        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        GroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createShippingTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_SHIPPING);

        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        GroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createTroopConcentrationTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_TROOP_CONCENTRATION);

        GroundUnitBuilderAttack groundUnitBuilderAttack = new GroundUnitBuilderAttack(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        GroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    private void validateTestResults(GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        List<GroundUnit> groundUnits = groundUnitCollection.getAllAlliedGroundUnits();
        assert(groundUnits.size() > 0);
        assert(groundUnitCollection.getTargetCoordinatesFromGroundUnits(Side.ALLIED) != null);
    }
}
