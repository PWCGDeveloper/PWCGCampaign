package pwcg.mission.object;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuTREntity;

public class WindSock
{
	protected String name = "Windsock";
	protected int index = 0;
	protected int linkTrId = 0;
	protected Coordinate position;
	protected Orientation orientation = new Orientation();
	protected String script = "";
	protected String model = "graphics\\flag\\windsock.mgm";
	protected String Desc = "";
	
	protected ICountry country = CountryFactory.makeNeutralCountry();
	protected int startHeight = 0;
	protected int speedFactor = 1;
	protected int blockThreshold = 1;
	protected int radius = 1000;
	protected int type = 0;
	protected int countPlanes = 1;
	protected int countVehicles = 1; 
	
	protected McuTREntity entity = new McuTREntity();

    public static WindSock createWindSock(IFlight flight) throws PWCGException 
    {
        IAirfield flightAirfield = flight.getFlightData().getFlightInformation().getAirfield();
        double takeoffOrientation = flightAirfield.getTakeoffLocation().getOrientation().getyOri();

        Double angleWindSockLeft = MathUtils.adjustAngle(takeoffOrientation, -90);
        
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();
        int windsockDistance = configManager.getIntConfigParam(ConfigItemKeys.WindsockDistanceKey);
        Coordinate windSockCoordMoveLeft = MathUtils.calcNextCoord(flightAirfield.getTakeoffLocation().getPosition(), angleWindSockLeft, windsockDistance);

        double angleBack = MathUtils.adjustAngle(takeoffOrientation, 180);
        Coordinate windsockPos = MathUtils.calcNextCoord(windSockCoordMoveLeft, angleBack, -20.0);

        return new WindSock(windsockPos);
    }

	
	public WindSock(Coordinate position) 
	{
		index = IndexGenerator.getInstance().getNextIndex();
		this.position = position;
		name = "WindSock";
	
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
		if (productSpecificConfiguration.useFlagDir())
		{
            script = "LuaScripts\\WorldObjects\\Flags\\windsock.txt";
		}
		else
		{
            script = "LuaScripts\\WorldObjects\\windsock.txt";
		}
		
		// Set entity linkage
		linkTrId = entity.getIndex();
		entity.setMisObjID(index);
		entity.setPosition(position.copy());
		entity.setEnabled(1);
	}

	public int getIndex() {
		return index;
	}

	public int getLinkTrId() {
		return linkTrId;
	}

	public Coordinate getPosition() {
		return position;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public String getScript() {
		return script;
	}

	public String getModel() {
		return model;
	}

	public void setCountry(ICountry country) {
		this.country = country;
	}

	public String getDesc() {
		return Desc;
	}

	public void setDesc(String desc) {
		Desc = desc;
	}

	public ICountry getCountry() {
		return country;
	}

	public void write(BufferedWriter writer) throws PWCGException 
	{
		try
        {
            writer.write("Flag");
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
            
            country.writeAdjusted(writer);
            
            writer.write("  Desc = \"" +  Desc + "\";");
            writer.newLine();
            writer.write("  StartHeight = " + startHeight + ";");
            writer.newLine();
            writer.write("  SpeedFactor = " + speedFactor + ";");
            writer.newLine();
            writer.write("  BlockThreshold = " + blockThreshold + ";");
            writer.newLine();
            writer.write("  Radius = " + radius + ";");
            writer.newLine();
            writer.write("  Type = " + type + ";");
            writer.newLine();
            writer.write("  CountPlanes = " + countPlanes + ";");
            writer.newLine();
            writer.write("  CountVehicles = " + countVehicles + ";");
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
}
