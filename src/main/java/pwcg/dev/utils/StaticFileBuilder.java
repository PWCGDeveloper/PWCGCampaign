package pwcg.dev.utils;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.Country;
import pwcg.campaign.io.json.StaticObjectIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class StaticFileBuilder
{
    
    private static final List<VehicleDefinition> staticObjects = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            try
            {
                List<Country> axis = new ArrayList<>();
                axis.add(Country.GERMANY);
                add(new VehicleDefinition("Blocks\\", "blocks\\", "static_opel", "truck", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 50, VehicleClass.StaticAirfield));
                add(new VehicleDefinition("Blocks\\", "blocks\\", "static_opel_fuel", "truck", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 50, VehicleClass.StaticAirfield));
                
                List<Country> allied = new ArrayList<>();
                allied.add(Country.RUSSIA);
                allied.add(Country.USA);
                allied.add(Country.BRITAIN);
                
                add(new VehicleDefinition("Blocks\\", "blocks\\", "static_zis_fuel", "truck", allied, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 60, VehicleClass.StaticAirfield));
                add(new VehicleDefinition("Blocks\\", "blocks\\", "static_zis", "truck", allied, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.StaticAirfield));
                add(new VehicleDefinition("Blocks\\", "blocks\\", "static_gazaa", "truck", allied, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.StaticAirfield));
            }
            catch(PWCGException exp)
            {
                
            }
        }
    };

    
    public static void main(String[] args)
    {
        try
        {
            StaticObjectIOJson.writeJson(staticObjects);
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
    }
}
