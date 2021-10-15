package pwcg.aar.inmission.phase2.logeval.equipmentstatus;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@ExtendWith(MockitoExtension.class)
public class EquipmentSurvivalCalculatorTest
{
    @Mock private Airfield field;

    @Test
    public void testEquipmentNotDestroyed () throws PWCGException
    {
        Coordinate downAt = new Coordinate(100, 0, 100);        
        Mockito.when(field.getPosition()).thenReturn(new Coordinate(1000, 0, 1000));
        
        EquipmentSurvivalCalculator equipmentSurvivalCalculator = new EquipmentSurvivalCalculator(downAt, field);
        assert(equipmentSurvivalCalculator.isPlaneDestroyed() == false);
    }

    @Test
    public void testEquipmentDestroyed () throws PWCGException
    {
        Coordinate downAt = new Coordinate(100, 0, 100);        
        Mockito.when(field.getPosition()).thenReturn(new Coordinate(1000, 0, 6000));
        
        EquipmentSurvivalCalculator equipmentSurvivalCalculator = new EquipmentSurvivalCalculator(downAt, field);
        assert(equipmentSurvivalCalculator.isPlaneDestroyed() == true);
    }
}
