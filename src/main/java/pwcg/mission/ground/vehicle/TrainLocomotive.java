package pwcg.mission.ground.vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;

public class TrainLocomotive extends Vehicle implements IVehicle
{
    private List<IVehicle> cars = new ArrayList<>();

    public TrainLocomotive(VehicleDefinition vehicleDefinition)
    {
        super(vehicleDefinition);
    }

    public void write(BufferedWriter writer) throws PWCGException
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
            throw new PWCGException(e.getMessage());
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
