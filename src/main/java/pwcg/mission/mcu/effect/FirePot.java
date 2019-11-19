package pwcg.mission.mcu.effect;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.mission.mcu.McuTimer;

public class FirePot
{	
    private McuTimer firePotTimer = new McuTimer();
    private EffectCommand firePotCommand = new EffectCommand(EffectCommand.START_EFFECT);
	private Fire firePot = new Fire();

	public FirePot()
	{
	}
	
	public void createFirePot(Coordinate firePotPosition, double orientation, int time) throws PWCGException 
	{
		firePotTimer.setTimer(time);
		firePotTimer.setPosition(firePotPosition);

		firePotCommand.setPosition(firePotPosition);

		firePotTimer.setTarget(firePotCommand.getIndex());
        
        firePot.setPosition(firePotPosition.copy());
        firePot.populateEntity();
        firePot.getEntity().setEnabled(1);
        firePotCommand.setObject(firePot.getEntity().getIndex());
	}
	
	public void write(BufferedWriter writer) throws PWCGIOException 
	{
        firePotTimer.write(writer);
        firePotCommand.write(writer);
        firePot.write(writer);
	}

    public McuTimer getFirePotTimer()
    {
        return firePotTimer;
    }
}
