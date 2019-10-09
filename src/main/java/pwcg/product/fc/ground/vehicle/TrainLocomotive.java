package pwcg.product.fc.ground.vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.mission.ground.vehicle.ITrainLocomotive;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

class TrainLocomotive extends Vehicle implements ITrainLocomotive
{
    private List<TrainCar> cars = new ArrayList<TrainCar>();

    private static final List<VehicleDefinition> germanLocomotives = new ArrayList<VehicleDefinition>()
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\g8\\", "g8", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\e\\", "e", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> britishLocomotives = new ArrayList<VehicleDefinition>()
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\g8\\", "g8", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\e\\", "e", Country.BRITAIN));
        }
    };

    protected TrainLocomotive()
    {
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanLocomotives);
        allvehicleDefinitions.addAll(britishLocomotives);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;
        ;
        if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanLocomotives;
        }
        else if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = britishLocomotives;
        }

        makeRandomVehicleInstance(vehicleSet);
        displayName = "Locomotive";
    }

    public TrainLocomotive copy()
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

            writer.write("  Desc = \"" + Desc + "\";");
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

            // Write the carriages
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

    public void setOrientation(Orientation orient)
    {
        super.setOrientation(orient);
    }

    public void setPosition(Coordinate coord)
    {
        super.setPosition(coord);
    }

    public List<TrainCar> getCars()
    {
        return cars;
    }

    @Override
    public void addCar(IVehicle car)
    {
        this.cars.add((TrainCar) car);
    }
}
