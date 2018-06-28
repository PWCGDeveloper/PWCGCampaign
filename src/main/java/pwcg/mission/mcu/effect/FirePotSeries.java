package pwcg.mission.mcu.effect;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.McuMissionStart;
import pwcg.mission.mcu.McuTimer;

public class FirePotSeries extends Effect
{	
	protected McuMissionStart missionBegin = null;
	private List<Fire> firePots = new ArrayList<Fire>();
	private EffectCommand firePotCommand = new EffectCommand(EffectCommand.START_EFFECT);
	private McuTimer firePotTimer = new McuTimer();

	public FirePotSeries()
	{
	}
	
	public void createSeries(Coordinate startPosition, double orientation, double distanceBetween, int number) throws PWCGException 
	{
		missionBegin = new McuMissionStart();
		missionBegin.setPosition(startPosition.copy());
		
		firePotTimer.setTimer(2);
		firePotCommand.setPosition(startPosition);
		
		firePotCommand.setPosition(startPosition);

		missionBegin.setTarget(firePotTimer.getIndex());
		firePotTimer.setTarget(firePotCommand.getIndex());

		for (int i = 0; i < 10; ++i)
		{
			double metersAhead = (i+1) * distanceBetween;
			Coordinate firePotPosition = MathUtils.calcNextCoord(startPosition, orientation, metersAhead);
			firePotPosition.setYPos(0.0);
			
			Fire firePot = new Fire();
			firePot.setPosition(firePotPosition.copy());
			firePot.populateEntity();
			firePot.getEntity().setEnabled(1);
			firePots.add(firePot);

			firePotCommand.setObject(firePot.getEntity().getIndex());
		}
	}
	
	public void write(BufferedWriter writer) throws PWCGIOException 
	{
		if (missionBegin != null)
		{
			missionBegin.write(writer);
			firePotTimer.write(writer);
			firePotCommand.write(writer);
			
			for (Fire firePot : firePots)
			{
				firePot.write(writer);
			}
		}
	}
}
