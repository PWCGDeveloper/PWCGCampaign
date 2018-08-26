package pwcg.campaign.ww2.ground.vehicle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

@RunWith(MockitoJUnitRunner.class)
public class VehicleSetBuilderComprehensiveTest
{
    Mission mission;
    Campaign campaign;

    @Before 
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19411103"));
    }

    @Test
    public void testVehicleCreation() throws PWCGException
    {
        VehicleSetBuilderComprehensive vehicleSetBuilder = new VehicleSetBuilderComprehensive();
        vehicleSetBuilder.makeOneOfEachType();
        
        for (IVehicle vehicle : vehicleSetBuilder.getAllVehicles())
        {
            assert (vehicle.getAllVehicleDefinitions().size() > 0);
        }
    }

    @Test
    public void patrolFlightTest() throws PWCGException
    {
        mission = new Mission();
        mission.initialize(campaign);
        mission.generate(FlightTypes.PATROL);
        mission.generateAllGroundUnitTypes();
        mission.finalizeMission();
    }

}
