package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.mission.aaatruck.AAATruckPositionBuilder;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleFactory;

public class MissionAAATruck
{
    private IVehicle aaaTruck = null;

    public void buildAAATruck(Mission mission, Side truckSide, Date date) throws PWCGException
    {
        if (truckSide == Side.ALLIED)
        {
            VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionByVehicleType("_gaz-mm-72k");            
            aaaTruck = VehicleFactory.createVehicleFromDefinition(CountryFactory.makeCountryByCountry(Country.RUSSIA), DateUtils.getDateYYYYMMDD("19420801"), vehicleDefinition);

        }
        else
        {
            VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionByVehicleType("_gaz-mm-72k");            
            aaaTruck = VehicleFactory.createVehicleFromDefinition(CountryFactory.makeCountryByCountry(Country.GERMANY), DateUtils.getDateYYYYMMDD("19420801"), vehicleDefinition);
        }
        
        setAAATruckPosition(mission, truckSide);
        aaaTruck.setAiLevel(AiSkillLevel.PLAYER);
    }

    private void setAAATruckPosition(Mission mission, Side truckSide) throws PWCGException
    {
        AAATruckPositionBuilder truckPositionBuilder = new AAATruckPositionBuilder(mission);
        truckPositionBuilder.buildTruckPosition(truckSide);
        
        aaaTruck.setPosition(truckPositionBuilder.getTruckPosition().copy());
        aaaTruck.getEntity().setPosition(truckPositionBuilder.getTruckPosition().copy());
        
        aaaTruck.setOrientation(truckPositionBuilder.getTruckOrientation().copy());
        aaaTruck.getEntity().setOrientation(truckPositionBuilder.getTruckOrientation().copy());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        if (aaaTruck != null)
        {
            aaaTruck.write(writer);
        }
    }

    public List<Integer> getPlayerTruckIds()
    {
        List<Integer> truckIds = new ArrayList<>();
        if (aaaTruck != null)
        {
            truckIds.add(aaaTruck.getLinkTrId());
        }
        return truckIds;
    }
    
    public Coordinate getPosition()
    {
        return aaaTruck.getPosition().copy();
    }
}
