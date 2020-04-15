package pwcg.aar.inmission.phase2.logeval.equipmentstatus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.equipmentstatus.EquipmentSurvivalCalculator;
import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentSurvivalCalculatorTest
{
    @Mock private IAirfield field;

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
