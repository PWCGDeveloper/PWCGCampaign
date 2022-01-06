package pwcg.product.bos.ground.vehicle;

import java.util.ArrayList;
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
import pwcg.mission.ground.vehicle.VehicleDefinition;

@ExtendWith(MockitoExtension.class)
public class VehicleDefinitionFCTest
{
    private  List<VehicleDefinition> allVehiclesDefinitions = new ArrayList<>();

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
}
