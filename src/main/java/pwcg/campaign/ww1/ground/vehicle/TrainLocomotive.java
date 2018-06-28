package pwcg.campaign.ww1.ground.vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.mission.ground.vehicle.ITrainLocomotive;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuTREntity;

public class TrainLocomotive extends Vehicle implements ITrainLocomotive
{
	private TrainDO locomotive = null;
	
	protected TrainCoalCar coalCar = null;
	protected ArrayList<TrainCar> cars = new ArrayList<TrainCar>();

	private TrainDO[] locomotives = 
	{
		new TrainDO("g8", "g8", "Locomotive", 11.65),
	};
	
	public TrainLocomotive(ICountry country) 
	{
        super(country);
        
		this.locomotive = locomotives[0].copy();
		
		displayName = locomotive.getName();
		
		name = locomotive.getName();
		script = "LuaScripts\\WorldObjects\\" + locomotive.getId() + ".txt";
		model = "graphics\\trains\\" + locomotive.getCategory() + "\\" + locomotive.getId() + ".mgm";
	}

	public TrainLocomotive copy () 
	{
		TrainLocomotive locomotive = new TrainLocomotive(country);
		
		locomotive.index = IndexGenerator.getInstance().getNextIndex();
		
		locomotive.name = this.name;
		locomotive.displayName = this.displayName;
		locomotive.linkTrId = this.linkTrId;
		locomotive.script = this.script;
		locomotive.model = this.model;
		locomotive.Desc = this.Desc;
		locomotive.aiLevel = this.aiLevel;
		locomotive.numberInFormation = this.numberInFormation;
		locomotive.vulnerable = this.vulnerable;
		locomotive.engageable = this.engageable;
		locomotive.limitAmmo = this.limitAmmo;
		locomotive.damageReport = this.damageReport;
		locomotive.country = this.country;
		locomotive.damageThreshold = this.damageThreshold; 
		
		locomotive.position = new Coordinate();
		locomotive.orientation = new Orientation();
		
		locomotive.entity = new McuTREntity();
		
		locomotive.locomotive = this.locomotive.copy();
		
		locomotive.populateEntity();
		
		return locomotive;
	}
	
	public void write(BufferedWriter writer) throws PWCGIOException
	{
        try
        {
    		writer.write("Train");
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
    		writer.write("  DamageReport = " + damageReport + ";");
    		writer.newLine();
    		writer.write("  DamageThreshold = " + damageThreshold + ";");
    		writer.newLine();
    		writer.write("  DeleteAfterDeath = " + deleteAfterDeath + ";");
    		writer.newLine();
    
    		// Write  the carriages
    		writer.write("  Carriages");
    		writer.newLine();
    		writer.write("  {");
    		writer.newLine();
    		for (TrainCar car : cars)
    		{
    			writer.write("    \"" + car.getScript() + "\";");
    			writer.newLine();
    		}
    
    		writer.write("  }");
    		writer.newLine();
    
    		writer.write("}");
    		writer.newLine();
    		writer.newLine();
    		writer.newLine();
    		
    		entity.write(writer);
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

	public TrainDO getLocomotive() 
	{
		return locomotive;
	}

	public TrainCoalCar getCoalCar() 
	{
		return coalCar;
	}

	public void setCoalCar(TrainCoalCar coalCar) 
	{
		this.coalCar = coalCar;
	}

	public ArrayList<TrainCar> getCars() 
	{
		return cars;
	}

	public void addCar(IVehicle car) 
	{
		this.cars.add((TrainCar)car);
	}
}
