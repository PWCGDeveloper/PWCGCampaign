package pwcg.campaign.target;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderStructural;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TargetDefinitionBuilderStructuralTest
{
    private Campaign campaign;
    private static Mission mission;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);

        if (mission == null)
        {
            MissionGenerator missionGenerator = new MissionGenerator(campaign);
            mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);
        }
    }
    
    @Test
    public void verifyEnemyStructuresTest()  throws PWCGException
    {
        TargetDefinitionBuilderStructural targetSelector = new TargetDefinitionBuilderStructural(mission.getFlights().getUnits().get(0).getFlightInformation());
        List<TargetDefinition> targetaDefinitionsForStructures = targetSelector.findTargetStructures();
        Assertions.assertFalse(targetaDefinitionsForStructures.isEmpty());

        for (TargetDefinition targetDefinition : targetaDefinitionsForStructures)
        {
            Assertions.assertNotEquals(Country.NEUTRAL, targetDefinition.getCountry().getCountry());
        }
    }
    
    @Test
    public void verifyBridgeStructuresTest()  throws PWCGException
    {
        TargetDefinitionBuilderStructural targetSelector = new TargetDefinitionBuilderStructural(mission.getFlights().getUnits().get(0).getFlightInformation());
        List<TargetDefinition> targetaDefinitionsForStructures = targetSelector.findTargetStructures();
        Assertions.assertFalse(targetaDefinitionsForStructures.isEmpty());

        boolean bridgeFound = false;
        for (TargetDefinition targetDefinition : targetaDefinitionsForStructures)
        {
            if (targetDefinition.getTargetType() == TargetType.TARGET_BRIDGE)
            {
                bridgeFound = true;
            }
        }
        Assertions.assertTrue(bridgeFound);
    }
    
    @Test
    public void verifyAirfieldStructuresTest()  throws PWCGException
    {
        TargetDefinitionBuilderStructural targetSelector = new TargetDefinitionBuilderStructural(mission.getFlights().getUnits().get(0).getFlightInformation());
        List<TargetDefinition> targetaDefinitionsForStructures = targetSelector.findTargetStructures();
        Assertions.assertFalse(targetaDefinitionsForStructures.isEmpty());

        boolean airfieldFound = false;
        for (TargetDefinition targetDefinition : targetaDefinitionsForStructures)
        {
            if (targetDefinition.getTargetType() == TargetType.TARGET_AIRFIELD)
            {
                airfieldFound = true;
            }
        }
        Assertions.assertTrue(airfieldFound);
    }
    
    @Test
    public void verifyRailStructuresTest()  throws PWCGException
    {
        TargetDefinitionBuilderStructural targetSelector = new TargetDefinitionBuilderStructural(mission.getFlights().getUnits().get(0).getFlightInformation());
        List<TargetDefinition> targetaDefinitionsForStructures = targetSelector.findTargetStructures();
        Assertions.assertFalse(targetaDefinitionsForStructures.isEmpty());

        boolean ralwayStationFound = false;
        for (TargetDefinition targetDefinition : targetaDefinitionsForStructures)
        {
            if (targetDefinition.getTargetType() == TargetType.TARGET_RAIL)
            {
                ralwayStationFound = true;
            }
        }
        Assertions.assertTrue(ralwayStationFound);
    }
}
