package pwcg.product.rof.ground.staticobject;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.product.rof.ground.vehicle.Vehicle;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.mission.mcu.McuTREntity;

public abstract class StaticObject extends Vehicle
{
    public StaticObject() 
    {
        super();
    }

	public StaticObject makeCopy (StaticObject staticObject) throws PWCGException 
	{
		staticObject.index = IndexGenerator.getInstance().getNextIndex();
		
		staticObject.vehicleType = this.vehicleType;
		staticObject.displayName = this.displayName;
		staticObject.linkTrId = this.linkTrId;
		staticObject.script = this.script;
		staticObject.model = this.model;
		staticObject.Desc = this.Desc;
		staticObject.aiLevel = this.aiLevel;
		staticObject.numberInFormation = this.numberInFormation;
		staticObject.vulnerable = this.vulnerable;
		staticObject.engageable = this.engageable;
		staticObject.limitAmmo = this.limitAmmo;
		staticObject.damageReport = this.damageReport;
		staticObject.country = this.country;
		staticObject.damageThreshold = this.damageThreshold; 
		
		staticObject.position = new Coordinate();
		staticObject.orientation = new Orientation();
		
		staticObject.entity = new McuTREntity();
		
		staticObject.populateEntity();
		
		return staticObject;
	}
	
	@Override
	public void write(BufferedWriter writer) throws PWCGIOException
	{
        try
        {
    		writer.write("Block");
    		writer.newLine();
    		writer.write("{");
    		writer.newLine();
    		
    		writer.write("  Name = \"" + vehicleType + "\";");
    		writer.newLine();
    		writer.write("  Index = " + index + ";");
    		writer.newLine();
    		writer.write("  LinkTrId = 0;");
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
    		writer.write("  DamageReport = " + damageReport + ";");
    		writer.newLine();
    		writer.write("  DamageThreshold = " + damageThreshold + ";");
    		writer.newLine();
    		writer.write("  DeleteAfterDeath = " + deleteAfterDeath + ";");
    		writer.newLine();
    
    		writer.write("}");
    		writer.newLine();
    		writer.newLine();
    		writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
}
