package pwcg.mission.mcu.effect;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.McuTimer;

public class FirePotPair
{	
	private List<FirePot> firePots = new ArrayList<>();
	private McuTimer sourceTimer;

	public FirePotPair(McuTimer sourceTimer)
	{
	    this.sourceTimer = sourceTimer;
	}
	
	public void createSeries(Coordinate firstFirePotPosition, double orientation, double distanceBetween) throws PWCGException 
	{
		for (int i = 0; i < 2; ++i)
		{
		    Coordinate firePotPosition = calculateFirePotPosition(firstFirePotPosition, orientation, distanceBetween, i);
		    int time = calculateFirePotStartTime(i);
		    
			FirePot firePot = new FirePot();
			firePot.createFirePot(firePotPosition, orientation, time);
			firePots.add(firePot);
		}
		
        sourceTimer.setTimerTarget(firePots.get(0).getFirePotTimer().getIndex());
        firePots.get(0).getFirePotTimer().setTimerTarget(firePots.get(1).getFirePotTimer().getIndex());
	}
    
    public Coordinate calculateFirePotPosition(Coordinate firstFirePotPosition, double orientation, double distanceBetween, int firepotIndex) throws PWCGException
    {
        Coordinate firePotPosition = firstFirePotPosition;          
        if (firepotIndex%2 == 0)
        {
            firePotPosition = firstFirePotPosition;
        }
        else
        {
            firePotPosition = MathUtils.calcNextCoord(firstFirePotPosition, orientation, distanceBetween);
        }
        firePotPosition.setYPos(0.0);
        return firePotPosition;
    }
    
    public int calculateFirePotStartTime(int firepotIndex) throws PWCGException
    {
        int time = 2;
        if (firepotIndex%2 == 0)
        {
            time = 2;
        }
        else
        {
            time = 1;
        }
        return time;
    }
	
	public void write(BufferedWriter writer) throws PWCGException 
	{
        for (FirePot firePot : firePots)
        {
            firePot.write(writer);
        }
	}
	
    public McuTimer getFirePotTriggerTimer()
    {
        return firePots.get(0).getFirePotTimer();
    }
}
