package pwcg.product.bos.ground.vehicle;

import java.io.BufferedWriter;
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
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

public class Drifter extends Vehicle
{
    private static final List<VehicleDefinition> germanDrifters = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("ships\\", "ships\\rivershipgeorgia\\", "rivershipgeorgia", Country.GERMANY));
            add(new VehicleDefinition("ships\\", "ships\\rivershipgeorgia\\", "rivershipgeorgiaaaa", Country.GERMANY));
            add(new VehicleDefinition("ships\\", "ships\\1124bm13\\", "1124bm13", Country.GERMANY));
            add(new VehicleDefinition("ships\\", "ships\\1124\\", "1124", Country.GERMANY));
            add(new VehicleDefinition("ships\\", "ships\\rivergunshipa\\", "rivergunshipa", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> russianDrifters = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("ships\\", "ships\\rivershipgeorgia\\", "rivershipgeorgia", Country.RUSSIA));
            add(new VehicleDefinition("ships\\", "ships\\rivershipgeorgia\\", "rivershipgeorgiaaaa", Country.RUSSIA));
            add(new VehicleDefinition("ships\\", "ships\\1124bm13\\", "1124bm13", Country.RUSSIA));
            add(new VehicleDefinition("ships\\", "ships\\1124\\", "1124", Country.RUSSIA));
            add(new VehicleDefinition("ships\\", "ships\\rivergunshipa\\", "rivergunshipa", Country.RUSSIA));
        }
    };
    
    public Drifter()
    {
        super();
    }
    
    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanDrifters);
        allvehicleDefinitions.addAll(russianDrifters);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = russianDrifters;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanDrifters;
        }
        
        displayName = "River Ship";
        makeRandomVehicleInstance(vehicleSet);
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
