
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class McuDamage extends BaseFlightMcu
{
	private int damageType = 1;
	private int damage = 1;

	public McuDamage ()
	{
		super();
		
		name = "Damage";
		desc = "Damage";
	}
	
	public void write(BufferedWriter writer) throws PWCGException
	{
		try
        {
            writer.write("MCU_CMD_Damage");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("  Damage = " + damage + ";");
            writer.newLine();
            writer.write("  Type = " + damageType + ";");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}

    public int getDamageType()
    {
        return this.damageType;
    }

    public void setDamageType(int damageType)
    {
        this.damageType = damageType;
    }

    public int getDamage()
    {
        return this.damage;
    }

    public void setDamage(int damage)
    {
        this.damage = damage;
    }
}
