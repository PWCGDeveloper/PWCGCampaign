package pwcg.campaign.ww2.ground.vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.ITrainLocomotive;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuTREntity;

class TrainLocomotive extends Vehicle implements ITrainLocomotive
{
	private TrainDO locomotive = null;
	
	protected List<TrainCar> cars = new ArrayList<TrainCar>();

	private TrainDO[] locomotives = 
	{
	   new TrainDO("g8", "g8", "Locomotive", 11.65),
	   new TrainDO("e", "e", "Locomotive", 11.65),
	};
	
	protected TrainLocomotive()
	{
	}

	public TrainLocomotive(ICountry country) 
	{
		super();
		
		this.country = country;
		
		
        int selectedLocomotive = RandomNumberGenerator.getRandom(locomotives.length);
        this.locomotive = locomotives[selectedLocomotive].copy();
		
		displayName = locomotive.getName();
		
		vehicleType = locomotive.getName();
		script = "LuaScripts\\WorldObjects\\Trains\\" + locomotive.getId() + ".txt";
		model = "graphics\\trains\\" + locomotive.getCategory() + "\\" + locomotive.getId() + ".mgm";
	}

	public TrainLocomotive copy () 
	{
		TrainLocomotive locomotive = new TrainLocomotive();
		
		locomotive.index = IndexGenerator.getInstance().getNextIndex();
		
		locomotive.vehicleType = this.vehicleType;
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
    		
    		writer.write("  Name = \"" + vehicleType + "\";");
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
    		
    		country.writeAdjusted(writer);

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
	
	public void setOrientation (Orientation orient)
	{
		super.setOrientation(orient);
	}

	public void setPosition (Coordinate coord)
	{
		super.setPosition(coord);
	}

	public TrainDO getLocomotive() {
		return locomotive;
	}

	public List<TrainCar> getCars() {
		return cars;
	}

	/* (non-Javadoc)
     * @see pwcg.bos.ground.vehicle.ITrainLocomotive#addCar(pwcg.bos.ground.vehicle.TrainCar)
     */
	@Override
    public void addCar(IVehicle car) {
		this.cars.add((TrainCar)car);
	}
}
