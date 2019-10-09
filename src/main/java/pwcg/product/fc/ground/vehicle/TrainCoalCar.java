package pwcg.product.fc.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

public class TrainCoalCar extends TrainCar
{
    private TrainLocomotive trainLocomotive;
    
    private static final List<VehicleDefinition> germanCoalCars = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\g8t\\", "g8t", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\et\\", "et", Country.GERMANY));
        }
    };
    
    private static final List<VehicleDefinition> britishCoalCars = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\g8t\\", "g8t", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\et\\", "et", Country.BRITAIN));
        }
    };

    public TrainCoalCar()
	{
        super();
	}

	public TrainCoalCar(TrainLocomotive trainLocomotive)
	{
		super();
		this.trainLocomotive = trainLocomotive;
	}

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanCoalCars);
        allvehicleDefinitions.addAll(britishCoalCars);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = britishCoalCars;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanCoalCars;
        }

        VehicleDefinition matchingCoalCarDefinition = matchCoalCarToLocomotive(vehicleSet);
        if (matchingCoalCarDefinition != null)
        {
            this.makeVehicleFromDefinition(matchingCoalCarDefinition);
        }
        else
        {
            makeRandomVehicleInstance(vehicleSet);
        }
        
        displayName = "Coal Car";
    }

    private VehicleDefinition matchCoalCarToLocomotive(List<VehicleDefinition> vehicleSet)
    {
        VehicleDefinition matchingCoalCarDefinition = null;
        if (trainLocomotive != null)
        {
            for (VehicleDefinition coalCarDefinition : vehicleSet)
            {
                if (coalCarDefinition.getVehicleType().contains(trainLocomotive.getVehicleType()))
                {
                    matchingCoalCarDefinition = coalCarDefinition;
                    break;
                }
            }
        }
        return matchingCoalCarDefinition;
    }

	public TrainCoalCar copy()
	{
		TrainCoalCar coalCar = new TrainCoalCar(trainLocomotive);

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

		coalCar.populateEntity();

		return coalCar;
	}
}
