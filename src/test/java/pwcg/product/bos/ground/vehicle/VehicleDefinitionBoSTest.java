package pwcg.product.bos.ground.vehicle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.VehicleDefinitionIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleRequestDefinition;

@ExtendWith(MockitoExtension.class)
public class VehicleDefinitionBoSTest
{
    private List<VehicleDefinition> allVehiclesDefinitions = new ArrayList<>();

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        allVehiclesDefinitions = VehicleDefinitionIOJson.readJson();
    }

    @Test
    public void testVehicleCreation() throws PWCGException
    {
        for (VehicleDefinition vehicleDefinition : allVehiclesDefinitions)
        {
            Assertions.assertTrue (vehicleDefinition.getVehicleName() != null);
            Assertions.assertTrue (!vehicleDefinition.getVehicleName().isEmpty());
        }
    }

    @Test
    public void testLargeDriftersExcluded() throws PWCGException
    {
        for (VehicleDefinition vehicleDefinition : allVehiclesDefinitions)
        {
            Date availabilityDateToTest = DateUtils.advanceTimeDays(vehicleDefinition.getStartDate(), 14);
            VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(vehicleDefinition.getCountries().get(0), availabilityDateToTest, vehicleDefinition.getVehicleClass());

            Assertions.assertTrue (vehicleDefinition.getVehicleName() != null);
            Assertions.assertTrue (!vehicleDefinition.getVehicleName().isEmpty());
            if (vehicleDefinition.getVehicleClass() == VehicleClass.Drifter)
            {
                if (vehicleDefinition.getVehicleLength() < 100)
                {
                    Assertions.assertTrue (vehicleDefinition.shouldUse(requestDefinition) == true);
                }
                else
                {
                    Assertions.assertTrue (vehicleDefinition.shouldUse(requestDefinition) == false);
                }
            }
            else
            {
                Assertions.assertTrue (vehicleDefinition.shouldUse(requestDefinition) == true);
            }
        }
    }
}
