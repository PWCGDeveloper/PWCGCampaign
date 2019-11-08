package pwcg.campaign.plane;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;
import pwcg.product.fc.plane.FCPlaneAttributeMapping;

public class Balloon
{
    public static int BALLOON_ALTITUDE = 1500;
    
    protected String name = "";
    protected String type = "";
	protected String displayName = "";
	protected int index;
	protected int linkTrId;
	protected Coordinate position;
	protected Orientation orientation;
	protected String script = "";
	protected String model = "";
	protected String Desc = "";
	protected AiSkillLevel aiLevel = AiSkillLevel.NOVICE;
	protected int vulnerable = 1;
	protected int engageable = 1;
	protected int limitAmmo = 1;
	protected int startInAir = 1;
	protected int damageReport = 50;
    protected ICountry country = CountryFactory.makeCountryByCountry(Country.NEUTRAL);
	protected int damageThreshold = 1; 

	protected McuTREntity entity = new McuTREntity();

	public Balloon(ICountry country) 
	{
		index = IndexGenerator.getInstance().getNextIndex();
		
		type = "parseval";
		if  (country.getSide() == Side.AXIS)
		{
			type = "drachen";
			displayName = "German Balloon";			
			
			int odds = RandomNumberGenerator.getRandom(100);
			if (odds < 50)
			{
				type = "aetype";
				displayName = "German Balloon";
			}
		}
		else
		{
            type = "parseval";
            displayName = "British Balloon";         
            
            int odds = RandomNumberGenerator.getRandom(100);
            if (odds < 50)
            {
                type = "caquot";
                displayName = "British Balloon";
            }
		}
		
		this.name = displayName;
		this.country = country;
		script = "LuaScripts\\WorldObjects\\Balloons\\" + ".txt";
		model = "graphics\\balloons\\" + type + "\\" + type + ".mgm";
	}

	public void populateEntity(Coordinate position, Orientation orientation)
	{
		// Link this balloon to the MCU
		this.linkTrId = entity.getIndex();
				
		// Position is same as balloon
		entity.setPosition(position);
		entity.setOrientation(orientation);
		
		// Link this MCU back to the balloon
		entity.setTarget(index);
		entity.setMisObjID(index);
	}
	
	public int getCruisingSpeed()
	{
		int cruisingSpeed = 0;
		return cruisingSpeed;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getLinkTrId() {
		return linkTrId;
	}

	public void setLinkTrId(int linkTrId) {
		this.linkTrId = linkTrId;
	}

	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDesc() {
		return Desc;
	}

	public void setDesc(String desc) {
		Desc = desc;
	}

	public AiSkillLevel getAiLevel() {
		return aiLevel;
	}

	public void setAiLevel(AiSkillLevel aiLevel) {
		this.aiLevel = aiLevel;
	}

	public int getVulnerable() {
		return vulnerable;
	}

	public void setVulnerable(int vulnerable) {
		this.vulnerable = vulnerable;
	}

	public int getEngageable() {
		return engageable;
	}

	public void setEngageable(int engageable) {
		this.engageable = engageable;
	}

	public int getLimitAmmo() {
		return limitAmmo;
	}

	public void setLimitAmmo(int limitAmmo) {
		this.limitAmmo = limitAmmo;
	}

	public int getStartInAir() {
		return startInAir;
	}

	public void setStartInAir(int startInAir) {
		this.startInAir = startInAir;
	}

	public int getDamageReport() {
		return damageReport;
	}

	public void setDamageReport(int damageReport) {
		this.damageReport = damageReport;
	}

	public int getDamageThreshold() {
		return damageThreshold;
	}

	public void setDamageThreshold(int damageThreshold) {
		this.damageThreshold = damageThreshold;
	}
	
	public McuTREntity getEntity() {
		return entity;
	}

	public void setEntity(McuTREntity entity) {
		this.entity = entity;
	}

	public ICountry getCountry() {
		return country;
	}

	public void setCountry(ICountry country) {
		this.country = country;
	}


    public static boolean isBalloonName(String name) 
    {
  
       if (name.toLowerCase().contains(new String("parseval").toLowerCase()))
       {
           return true;
       }
       else if (name.toLowerCase().contains(new String("drachen").toLowerCase()))
       {
           return true;
       }
       else if (name.toLowerCase().contains(new String("caquot").toLowerCase()))
       {
           return true;
       }
       else if (name.toLowerCase().contains(new String("aetype").toLowerCase()))
       {
           return true;
       }
       else if (name.toLowerCase().contains(new String(FCPlaneAttributeMapping.BALLOON.getPlaneType()).toLowerCase()))
       {
           return true;
       }

       return false;
    }

	public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void write(BufferedWriter writer) 
	{
		try
        {
            writer.write("Aerostat");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"" + name + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  LinkTrId = " + linkTrId + ";");
            writer.newLine();
            
            position.write(writer);
            orientation.write(writer);		

            writer.write("  Script = \"" + script + "\";");
            writer.newLine();
            writer.write("  Model = \"" + model + "\";");
            writer.newLine();
            
            writer.write("  Country = " + country.getCountryCode() + ";");

            writer.newLine();
            writer.write("  Desc = \"" +  Desc + "\";");
            writer.newLine();
            writer.write("  AILevel = " + aiLevel.getAiSkillLevel() + ";");
            writer.newLine();
            writer.write("  Vulnerable = " + vulnerable + ";");
            writer.newLine();
            writer.write("  Engageable = " + engageable + ";");
            writer.newLine();
            writer.write("  LimitAmmo = " + limitAmmo + ";");
            writer.newLine();
            writer.write("  StartInAir = " + startInAir + ";");
            writer.newLine();
            writer.write("  DamageReport = " + damageReport + ";");
            writer.newLine();
            writer.write("  DamageThreshold = " + damageThreshold + ";");
            writer.newLine();
            writer.write("  DeleteAfterDeath = 1;");
            writer.newLine();
            writer.write("  Fuel = 1;");
            writer.newLine();
            writer.write("  PayloadId = 0;");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
            
            entity.write(writer);
        }
        catch (PWCGIOException exp)
        {
            exp.printStackTrace();
        }
        catch (IOException exp)
        {
            exp.printStackTrace();
        }
	}
}
