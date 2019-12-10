package pwcg.mission.ground;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.factory.GroundUnitAttackFactory;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;
import pwcg.testutils.FranceAttackMockCampaign;

@RunWith(MockitoJUnitRunner.class)
public class GroundUnitBuilderAttackFCTest extends FranceAttackMockCampaign
{
    private TargetDefinition targetDefinition = new TargetDefinition();


    @Before
    public void setup() throws PWCGException
    {
        mockCampaignSetup();
        
        targetDefinition.setAttackingCountry(country);
        targetDefinition.setTargetCountry(CountryFactory.makeCountryByCountry(Country.FRANCE));
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

        GroundUnitAttackFactory groundUnitBuilderAttack = new GroundUnitAttackFactory(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createAirfieldUnitTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_AIRFIELD);

        GroundUnitAttackFactory groundUnitBuilderAttack = new GroundUnitAttackFactory(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createTruckConvoyTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_TRANSPORT);

        GroundUnitAttackFactory groundUnitBuilderAttack = new GroundUnitAttackFactory(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createGroundArtilleryBatteryTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_ARTILLERY);

        GroundUnitAttackFactory groundUnitBuilderAttack = new GroundUnitAttackFactory(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createDrifterTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_DRIFTER);

        GroundUnitAttackFactory groundUnitBuilderAttack = new GroundUnitAttackFactory(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createAssaultTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_ASSAULT);

        GroundUnitAttackFactory groundUnitBuilderAttack = new GroundUnitAttackFactory(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createDefenseTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_DEFENSE);

        GroundUnitAttackFactory groundUnitBuilderAttack = new GroundUnitAttackFactory(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createShippingTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_SHIPPING);

        GroundUnitAttackFactory groundUnitBuilderAttack = new GroundUnitAttackFactory(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createTroopConcentrationTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_TROOP_CONCENTRATION);

        GroundUnitAttackFactory groundUnitBuilderAttack = new GroundUnitAttackFactory(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    private void validateTestResults(IGroundUnitCollection groundUnitCollection) throws PWCGException
    {
        List<IGroundUnit> groundUnits = groundUnitCollection.getGroundUnits();
        assert(groundUnits.size() > 0);
        assert(groundUnitCollection.getTargetCoordinatesFromGroundUnits(Side.ALLIED) != null);
    }
}
