package pwcg.aar.inmission.phase2.logeval.victory;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase2.logeval.victory.AARAreaOfCombat;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class AARAreaOfCombatTest
{
    AARAreaOfCombat areaOfCombat;

    @Test
    public void testAreaOfCombatOneCoordTrue() throws PWCGException
    {
        setup(1);
        
        Coordinate testCoord = new Coordinate();
        testCoord.setXPos(100000.0);
        testCoord.setZPos(104999.0);

        assert(areaOfCombat.isNearAreaOfCombat(testCoord) == true);
    }
    
    @Test
    public void testAreaOfCombatOneCoordFalse() throws PWCGException
    {
        setup(1);

        Coordinate testCoord = new Coordinate();
        testCoord.setXPos(100000.0);
        testCoord.setZPos(105001.0);

        assert(areaOfCombat.isNearAreaOfCombat(testCoord) == false);
    }
    
    @Test
    public void testAreaOfCombatOneCoordCentered() throws PWCGException
    {
        setup(2);

        Coordinate testCoord = new Coordinate();
        testCoord.setXPos(103000.0);
        testCoord.setZPos(103000.0);

        assert(areaOfCombat.isNearAreaOfCombat(testCoord) == true);
    }
    
    @Test
    public void testAreaOfCombatManyCoordTrue() throws PWCGException
    {
        setup(2);

        Coordinate testCoord = new Coordinate();
        testCoord.setXPos(100000.0);
        testCoord.setZPos(105001.0);

        assert(areaOfCombat.isNearAreaOfCombat(testCoord) == true);
    }

    @Test
    public void testAreaOfCombatManyCoordFalse() throws PWCGException
    {
        setup(2);

        Coordinate testCoord = new Coordinate();
        testCoord.setXPos(100000.0);
        testCoord.setZPos(111001.0);

        assert(areaOfCombat.isNearAreaOfCombat(testCoord) == false);
    }
    
    public void setup(int numCoords) throws PWCGException
    {
        double zpos = 100000.0;
        
        List<LogVictory> victories = new ArrayList<>();
        for (int i = 0; i < numCoords; ++i)
        {
            LogVictory victory = new LogVictory(10);
            
            Coordinate crashLocation = new Coordinate();
            crashLocation.setXPos(100000.0);
            crashLocation.setZPos(zpos);
            
            zpos += 5000;

            victory.setLocation(crashLocation);
            victories.add(victory);
        }

        areaOfCombat = new AARAreaOfCombat(victories);
        areaOfCombat.noteLocationOfCrashes();
    }
    
}
