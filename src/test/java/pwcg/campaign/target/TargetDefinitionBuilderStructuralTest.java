package pwcg.campaign.target;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

public class TargetDefinitionBuilderStructuralTest
{
    private Campaign campaign;
    private Mission mission;
    
    @Before
    public void setup() throws PWCGException
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
        TargetDefinitionBuilderStructural targetSelector = new TargetDefinitionBuilderStructural(mission.getMissionFlights().getPlayerFlights().get(0).getFlightInformation());
        List<TargetDefinition> targetaDefinitionsForStructures = targetSelector.findTargetStructures();
        Assert.assertFalse(targetaDefinitionsForStructures.isEmpty());

        for (TargetDefinition targetDefinition : targetaDefinitionsForStructures)
        {
            Assert.assertNotEquals(Country.NEUTRAL, targetDefinition.getCountry().getCountry());
        }
    }
    
    @Test
    public void verifyBridgeStructuresTest()  throws PWCGException
    {
        TargetDefinitionBuilderStructural targetSelector = new TargetDefinitionBuilderStructural(mission.getMissionFlights().getPlayerFlights().get(0).getFlightInformation());
        List<TargetDefinition> targetaDefinitionsForStructures = targetSelector.findTargetStructures();
        Assert.assertFalse(targetaDefinitionsForStructures.isEmpty());

        boolean bridgeFound = false;
        for (TargetDefinition targetDefinition : targetaDefinitionsForStructures)
        {
            if (targetDefinition.getTargetType() == TargetType.TARGET_BRIDGE)
            {
                bridgeFound = true;
            }
        }
        Assert.assertTrue(bridgeFound);
    }
    
    @Test
    public void verifyAirfieldStructuresTest()  throws PWCGException
    {
        TargetDefinitionBuilderStructural targetSelector = new TargetDefinitionBuilderStructural(mission.getMissionFlights().getPlayerFlights().get(0).getFlightInformation());
        List<TargetDefinition> targetaDefinitionsForStructures = targetSelector.findTargetStructures();
        Assert.assertFalse(targetaDefinitionsForStructures.isEmpty());

        boolean airfieldFound = false;
        for (TargetDefinition targetDefinition : targetaDefinitionsForStructures)
        {
            if (targetDefinition.getTargetType() == TargetType.TARGET_AIRFIELD)
            {
                airfieldFound = true;
            }
        }
        Assert.assertTrue(airfieldFound);
    }
    
    @Test
    public void verifyRailStructuresTest()  throws PWCGException
    {
        TargetDefinitionBuilderStructural targetSelector = new TargetDefinitionBuilderStructural(mission.getMissionFlights().getPlayerFlights().get(0).getFlightInformation());
        List<TargetDefinition> targetaDefinitionsForStructures = targetSelector.findTargetStructures();
        Assert.assertFalse(targetaDefinitionsForStructures.isEmpty());

        boolean ralwayStationFound = false;
        for (TargetDefinition targetDefinition : targetaDefinitionsForStructures)
        {
            if (targetDefinition.getTargetType() == TargetType.TARGET_RAIL)
            {
                ralwayStationFound = true;
            }
        }
        Assert.assertTrue(ralwayStationFound);
    }
}
