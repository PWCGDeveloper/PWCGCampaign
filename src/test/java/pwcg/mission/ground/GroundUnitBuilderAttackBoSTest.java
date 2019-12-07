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
import pwcg.mission.ground.builder.GroundUnitAttackBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;
import pwcg.testutils.KubanAttackMockCampaign;

@RunWith(MockitoJUnitRunner.class)
public class GroundUnitBuilderAttackBoSTest extends KubanAttackMockCampaign
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

        GroundUnitAttackBuilder groundUnitBuilderAttack = new GroundUnitAttackBuilder(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createAirfieldUnitTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_AIRFIELD);

        GroundUnitAttackBuilder groundUnitBuilderAttack = new GroundUnitAttackBuilder(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createTruckConvoyTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_TRANSPORT);

        GroundUnitAttackBuilder groundUnitBuilderAttack = new GroundUnitAttackBuilder(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createGroundArtilleryBatteryTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_ARTILLERY);

        GroundUnitAttackBuilder groundUnitBuilderAttack = new GroundUnitAttackBuilder(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createDrifterTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_DRIFTER);

        GroundUnitAttackBuilder groundUnitBuilderAttack = new GroundUnitAttackBuilder(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createAssaultTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_ASSAULT);

        GroundUnitAttackBuilder groundUnitBuilderAttack = new GroundUnitAttackBuilder(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createDefenseTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_DEFENSE);

        GroundUnitAttackBuilder groundUnitBuilderAttack = new GroundUnitAttackBuilder(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createShippingTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_SHIPPING);

        GroundUnitAttackBuilder groundUnitBuilderAttack = new GroundUnitAttackBuilder(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    @Test
    public void createTroopConcentrationTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_TROOP_CONCENTRATION);

        GroundUnitAttackBuilder groundUnitBuilderAttack = new GroundUnitAttackBuilder(campaign, mission, targetDefinition);
        groundUnitBuilderAttack.createTargetGroundUnits();
        IGroundUnitCollection groundUnitCollection = groundUnitBuilderAttack.createTargetGroundUnits();
        validateTestResults(groundUnitCollection);
    }

    private void validateTestResults(IGroundUnitCollection groundUnitCollection) throws PWCGException
    {
        List<IGroundUnitCollection> groundUnits = groundUnitCollection.getAllAlliedGroundUnits();
        assert(groundUnits.size() > 0);
        assert(groundUnitCollection.getTargetCoordinatesFromGroundUnits(Side.ALLIED) != null);
    }
}
