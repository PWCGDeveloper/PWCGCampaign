package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class McuBehavior extends BaseFlightMcu
{
    private int filter = 1;
    private int vulnerable = 1;
    private int engageable = 1;
    private int limitAmmo = 1;
    private int repairFriendlies = 0;
    private int rearmFriendlies = 0;
    private int refuelFriendlies = 0;
    private AiSkillLevel aILevel = AiSkillLevel.NOVICE;
    private int floatParam = 0;

    public McuBehavior()
    {
        super();
        
        name = "Behavior";
        desc = "Behavior";
        
    }

    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            writer.write("MCU_CMD_Behaviour");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("  Filter = " + filter + ";");
            writer.write("  Vulnerable = " + vulnerable + ";");
            writer.write("  Engageable = " + engageable + ";");
            writer.write("  LimitAmmo = " + limitAmmo + ";");
            writer.write("  RepairFriendlies = " + repairFriendlies + ";");
            writer.write("  RearmFriendlies = " + rearmFriendlies + ";");
            writer.write("  RefuelFriendlies = " + refuelFriendlies + ";");
            writer.write("  AILevel = " + aILevel.getAiSkillLevel() + ";");
            writer.write("  FloatParam = " + floatParam + ";");

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
}
