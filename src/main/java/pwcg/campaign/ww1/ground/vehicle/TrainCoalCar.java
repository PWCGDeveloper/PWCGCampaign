package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.mcu.McuTREntity;

public class TrainCoalCar extends TrainCar
{
	private TrainDO coalCar = null;

	private TrainDO[] coalCars =
	{ new TrainDO("g8", "g8t", "Coal Car", 8.5), };

	public TrainCoalCar(ICountry country)
	{
		super(country);

		this.country = country;

		this.coalCar = coalCars[0].copy();

		displayName = coalCar.getName();

		vehicleType = coalCar.getName();
		script = "LuaScripts\\WorldObjects\\Trains\\" + coalCar.getCategory() + ".txt";
		model = "graphics\\trains\\" + coalCar.getCategory() + "\\" + coalCar.getId() + ".mgm";
	}

	public TrainCoalCar copy() throws PWCGException
	{
		TrainCoalCar coalCar = new TrainCoalCar(country);

		coalCar.index = IndexGenerator.getInstance().getNextIndex();

		coalCar.vehicleType = this.vehicleType;
		coalCar.displayName = this.displayName;
		coalCar.linkTrId = this.linkTrId;
		coalCar.script = this.script;
		coalCar.model = this.model;
		coalCar.Desc = this.Desc;
		coalCar.aiLevel = this.aiLevel;
		coalCar.numberInFormation = this.numberInFormation;
		coalCar.vulnerable = this.vulnerable;
		coalCar.engageable = this.engageable;
		coalCar.limitAmmo = this.limitAmmo;
		coalCar.damageReport = this.damageReport;
		coalCar.country = this.country;
		coalCar.damageThreshold = this.damageThreshold;

		coalCar.position = new Coordinate();
		coalCar.orientation = new Orientation();

		coalCar.entity = new McuTREntity();

		coalCar.coalCar = this.coalCar.copy();

		coalCar.populateEntity();

		return coalCar;
	}
	public TrainDO getCoalCar()
	{
		return coalCar;
	}
}
