package pwcg.campaign.ww2.ground.vehicle;

import java.io.BufferedWriter;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class Drifter extends Vehicle
{
    private String[][] drifters = 
    {
        { "rivershipgeorgia", "rivershipgeorgia" },
        { "rivershipgeorgia", "rivershipgeorgiaaaa" },
        { "1124bm13", "1124bm13" },
        { "1124", "1124" },
        { "rivergunshipa", "rivergunshipa" },
    };
    
    protected Drifter()
    {
    }

    public Drifter(ICountry country) throws PWCGException 
    {
        super();
        
        this.country = country;
        
        int selectedDrifter = RandomNumberGenerator.getRandom(drifters.length);
        String drifterId = drifters[selectedDrifter] [0];
        String drifterDir = drifters[selectedDrifter] [1];
        displayName = "River Ship";
        
        vehicleType = drifterId;
        script = "LuaScripts\\WorldObjects\\Ships\\" + drifterId + ".txt";
        model = "graphics\\ships\\" + drifterDir + "\\" + drifterId + ".mgm";
    }


    public Drifter copy () 
    {
        Drifter barge = new Drifter();
        
        barge.index = IndexGenerator.getInstance().getNextIndex();
        
        barge.vehicleType = this.vehicleType;
        barge.displayName = this.displayName;
        barge.linkTrId = this.linkTrId;
        barge.script = this.script;
        barge.model = this.model;
        barge.Desc = this.Desc;
        barge.aiLevel = this.aiLevel;
        barge.numberInFormation = this.numberInFormation;
        barge.vulnerable = this.vulnerable;
        barge.engageable = this.engageable;
        barge.limitAmmo = this.limitAmmo;
        barge.damageReport = this.damageReport;
        barge.country = this.country;
        barge.damageThreshold = this.damageThreshold; 
        
        barge.position = new Coordinate();
        barge.orientation = new Orientation();
        
        barge.entity = new McuTREntity();
        
        barge.populateEntity();
        
        return barge;
    }
    
    public void write(BufferedWriter writer) throws PWCGIOException
    {
        super.write(writer);
    }
    
    public void setOrientation (Orientation orient)
    {
        super.setOrientation(orient);
    }

    public void setPosition (Coordinate coord)
    {
        super.setPosition(coord);
    }
}
