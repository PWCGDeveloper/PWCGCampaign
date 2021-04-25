package pwcg.campaign.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.mcu.McuTREntity;

public class FakeAirfield extends FixedPosition implements Cloneable
{
    private Callsign callsign = Callsign.NONE;
    private int callnum = 0;
    private String chart = "";
    private int returnPlanes = 0;
    private int hydrodrome = 0;
    private int repairFriendlies = 0;
    private int rearmFriendlies = 0;
    private int refuelFriendlies = 0;
    private int repairTime = 0;
    private int rearmTime = 0;
    private int refuelTime = 0;
    private int maintenanceRadius = 1000;
    private McuTREntity entity;

    public FakeAirfield (Airfield airfield, Mission mission) throws PWCGException
    {
        super();
        
        entity = new McuTREntity(index);
        linkTrId = entity.getIndex();

        name = "Fake " + airfield.getName();
                
        position = airfield.getFakeAirfieldLocation(mission).getPosition().copy();
        orientation = airfield.getFakeAirfieldLocation(mission).getOrientation().copy();
        
        model = "graphics\\airfields\\fakefield.mgm";
        script = "LuaScripts\\WorldObjects\\Airfields\\fakefield.txt";
        desc = "";
        durability = 25000;
        damageReport = 50;
        damageThreshold = 1;
        deleteAfterDeath = 1;
        
        chart = ((Airfield) airfield).getChart(mission);

        populateEntity();
    }
    
    public void populateEntity()
    {
        
        entity.setPosition(position);
        entity.setOrientation(orientation);
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
                        
            writer.write("    Callsign = " + callsign.getNum(determineCountry().getCountry()) + ";");
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
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
}
