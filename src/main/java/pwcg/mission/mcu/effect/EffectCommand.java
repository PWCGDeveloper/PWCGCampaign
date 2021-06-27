package pwcg.mission.mcu.effect;

import java.io.BufferedWriter;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.mcu.BaseFlightMcu;

public class EffectCommand extends BaseFlightMcu
{
    public static final int START_EFFECT = 0;
    public static final int STOP_EFFECT = 1;
	
	protected int actionType = START_EFFECT;


	public EffectCommand(int actionType)
	{
	    this.actionType = actionType;
		index = IndexGenerator.getInstance().getNextIndex();
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	
	public void write(BufferedWriter writer) throws PWCGException
	{
		try
        {
            writer.write("MCU_CMD_Effect");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);
            writer.write("  ActionType = " + actionType + ";");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}
}
