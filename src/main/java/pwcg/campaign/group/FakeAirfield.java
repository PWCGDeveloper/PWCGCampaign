package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.mission.mcu.McuTREntity;

public class FakeAirfield extends FixedPosition implements Cloneable
{
    protected Callsign callsign = Callsign.NONE;
    protected int callnum = 0;
    protected String chart = "";
    protected int returnPlanes = 0;
    protected int hydrodrome = 0;
    protected int repairFriendlies = 0;
    protected int rearmFriendlies = 0;
    protected int refuelFriendlies = 0;
    protected int repairTime = 0;
    protected int rearmTime = 0;
    protected int refuelTime = 0;
    protected int maintenanceRadius = 1000;
    private McuTREntity entity = new McuTREntity();

    public FakeAirfield (IAirfield airfield, Date date) throws PWCGException
    {
        super();

        name = "Fake " + airfield.getName();
        
        linkTrId = entity.getIndex();
        
        country = airfield.getCountry(date).getCountry();
        position = airfield.getFakeAirfieldLocation().getPosition().copy();
        orientation = airfield.getFakeAirfieldLocation().getOrientation().copy();
        
        model = "graphics\\airfields\\fakefield.mgm";
        script = "LuaScripts\\WorldObjects\\Airfields\\fakefield.txt";
        desc = "";
        durability = 25000;
        damageReport = 50;
        damageThreshold = 1;
        deleteAfterDeath = 1;
        
        chart = ((Airfield) airfield).getChart();

        populateEntity();
    }
    
    public void populateEntity()
    {
        this.linkTrId = entity.getIndex();
        
        entity.setPosition(position);
        entity.setOrientation(orientation);
        entity.setMisObjID(index);
        entity.setEnabled(1);
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Airfield");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);
                        
            writer.write("    Callsign = " + callsign.getNum() + ";");
            writer.newLine();
            writer.write("    Callnum = " + callnum + ";");
            writer.newLine();
            writer.write(chart);
            writer.write("    ReturnPlanes = " + returnPlanes + ";");
            writer.newLine();
            writer.write("    Hydrodrome = " + hydrodrome + ";");
            writer.newLine();
            writer.write("    RepairFriendlies = " + repairFriendlies + ";");
            writer.newLine();
            writer.write("    RearmFriendlies = " + rearmFriendlies + ";");
            writer.newLine();
            writer.write("    RefuelFriendlies = " + refuelFriendlies + ";");
            writer.newLine();
            writer.write("    RepairTime = " + repairTime + ";");
            writer.newLine();
            writer.write("    RearmTime = " + rearmTime + ";");
            writer.newLine();
            writer.write("    RefuelTime = " + refuelTime + ";");
            writer.newLine();
            writer.write("    MaintenanceRadius = " + maintenanceRadius + ";");
            writer.newLine();
            
            writer.write("}");
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
