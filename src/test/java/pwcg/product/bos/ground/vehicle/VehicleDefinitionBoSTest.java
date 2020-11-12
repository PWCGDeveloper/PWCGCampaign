package pwcg.product.bos.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.VehicleDefinitionIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinition;

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
}
