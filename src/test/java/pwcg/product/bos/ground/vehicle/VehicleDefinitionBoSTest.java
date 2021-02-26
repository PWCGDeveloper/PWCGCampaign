package pwcg.product.bos.ground.vehicle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.VehicleDefinitionIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleRequestDefinition;

@RunWith(MockitoJUnitRunner.class)
public class VehicleDefinitionBoSTest
{
    private List<VehicleDefinition> allVehiclesDefinitions = new ArrayList<>();

    @Before 
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        allVehiclesDefinitions = VehicleDefinitionIOJson.readJson();
    }

    @Test
    public void testVehicleCreation() throws PWCGException
    {
        for (VehicleDefinition vehicleDefinition : allVehiclesDefinitions)
        {
            assert (vehicleDefinition.getVehicleName() != null);
            assert (!vehicleDefinition.getVehicleName().isEmpty());
        }
    }

    @Test
    public void testLargeDriftersExcluded() throws PWCGException
    {
        for (VehicleDefinition vehicleDefinition : allVehiclesDefinitions)
        {
            Date availabilityDateToTest = DateUtils.advanceTimeDays(vehicleDefinition.getStartDate(), 14);
            VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(vehicleDefinition.getCountries().get(0), availabilityDateToTest, vehicleDefinition.getVehicleClass());

            assert (vehicleDefinition.getVehicleName() != null);
            assert (!vehicleDefinition.getVehicleName().isEmpty());
            if (vehicleDefinition.getVehicleClass() == VehicleClass.Drifter)
            {
                if (vehicleDefinition.getVehicleLength() < 100)
                {
                    assert (vehicleDefinition.shouldUse(requestDefinition) == true);
                }
                else
                {
                    assert (vehicleDefinition.shouldUse(requestDefinition) == false);
                }
            }
            else
            {
                assert (vehicleDefinition.shouldUse(requestDefinition) == true);
            }
        }
    }
}
