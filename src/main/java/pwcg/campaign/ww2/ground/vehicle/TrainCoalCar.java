package pwcg.campaign.ww2.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.mcu.McuTREntity;

public class TrainCoalCar extends TrainCar
{
	private TrainDO coalCar = null;

	private TrainDO[] coalCars =
	{ new TrainDO("g8t", "g8t", "Coal Car", 8.5), new TrainDO("et", "et", "Coal Car", 8.5), };

	public TrainCoalCar()
	{
	}

	public TrainCoalCar(String trainId, ICountry country)
	{
		super();

		this.country = country;

		for (TrainDO coalCar : coalCars)
		{
			if (coalCar.getId().contains(trainId))
			{
				this.coalCar = coalCar.copy();
			}
		}

		if (this.coalCar != null)
		{
			displayName = coalCar.getName();

			name = coalCar.getName();
			script = "LuaScripts\\WorldObjects\\Trains\\" + coalCar.getCategory() + ".txt";
			model = "graphics\\trains\\" + coalCar.getCategory() + "\\" + coalCar.getId() + ".mgm";
		}
	}

	public TrainCoalCar copy()
	{
		TrainCoalCar coalCar = new TrainCoalCar();

		coalCar.index = IndexGenerator.getInstance().getNextIndex();

		coalCar.name = this.name;
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
