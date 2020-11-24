package pwcg.mission.ground.vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.mcu.McuTREntity;

public class TrainLocomotive extends Vehicle implements IVehicle
{
    private List<IVehicle> cars = new ArrayList<>();

    public TrainLocomotive(IVehicleDefinition vehicleDefinition)
    {
        super(vehicleDefinition);
    }

    public TrainLocomotive copy()
    {
        TrainLocomotive locomotive = new TrainLocomotive(this.vehicleDefinition);

        locomotive.index = IndexGenerator.getInstance().getNextIndex();

        locomotive.vehicleType = this.vehicleType;
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

        locomotive.entity = new McuTREntity(locomotive.index);

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
            for (IVehicle car : cars)
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
            PWCGLogger.logException(e);
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

    public void addCar(IVehicle car)
    {
        this.cars.add(car);
    }
}
