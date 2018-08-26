package pwcg.campaign.ww1.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class TrainCar extends Vehicle
{
    private List<VehicleDefinition> vehicleSet;

    private static final List<VehicleDefinition> germanTrainCar = new ArrayList<VehicleDefinition>()
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\pass\\", "pass", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\pass\\", "passc", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "platformb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "platformemptyb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "platformnb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "platformemptynb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "gondolab", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "gondolanb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\box\\", "boxb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\box\\", "boxnb", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> germanTankerCar = new ArrayList<VehicleDefinition>()
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\box\\", "tankb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\box\\", "tanknb", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> alliedTrainCar = new ArrayList<VehicleDefinition>()
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\pass\\", "pass", Country.FRANCE));
            add(new VehicleDefinition("trains\\", "trains\\pass\\", "passc", Country.FRANCE));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "platformb", Country.FRANCE));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "platformemptyb", Country.FRANCE));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "platformnb", Country.FRANCE));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "platformemptynb", Country.FRANCE));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "gondolab", Country.FRANCE));
            add(new VehicleDefinition("trains\\", "trains\\platform\\", "gondolanb", Country.FRANCE));
            add(new VehicleDefinition("trains\\", "trains\\box\\", "boxb", Country.FRANCE));
            add(new VehicleDefinition("trains\\", "trains\\box\\", "boxnb", Country.FRANCE));
        }
    };

    private static final List<VehicleDefinition> alliedTankerCar = new ArrayList<VehicleDefinition>()
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\box\\", "tankb", Country.FRANCE));
            add(new VehicleDefinition("trains\\", "trains\\box\\", "tanknb", Country.FRANCE));
        }
    };

    protected TrainCar()
    {
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanTrainCar);
        allvehicleDefinitions.addAll(germanTankerCar);
        allvehicleDefinitions.addAll(alliedTrainCar);
        allvehicleDefinitions.addAll(alliedTankerCar);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 20)
        {
            displayName = "Tanker Car";
            vehicleSet = germanTankerCar;
            if (country.getSideNoNeutral() == Side.ALLIED)
            {
                vehicleSet = alliedTankerCar;
            }
        }
        else
        {
            displayName = "Freight Car";
            vehicleSet = germanTrainCar;
            if (country.getSideNoNeutral() == Side.ALLIED)
            {
                vehicleSet = alliedTrainCar;
            }
        }

        makeRandomVehicleInstance(vehicleSet);
    }

    public TrainCar copy() throws PWCGException
    {
        TrainCar train = new TrainCar();
        train.index = IndexGenerator.getInstance().getNextIndex();

        train.vehicleType = this.vehicleType;
        train.displayName = this.displayName;
        train.linkTrId = this.linkTrId;
        train.script = this.script;
        train.model = this.model;
        train.Desc = this.Desc;
        train.aiLevel = this.aiLevel;
        train.numberInFormation = this.numberInFormation;
        train.vulnerable = this.vulnerable;
        train.engageable = this.engageable;
        train.limitAmmo = this.limitAmmo;
        train.damageReport = this.damageReport;
        train.country = this.country;
        train.damageThreshold = this.damageThreshold;

        train.position = new Coordinate();
        train.orientation = new Orientation();

        train.entity = new McuTREntity();

        train.populateEntity();

        return train;
    }
}
