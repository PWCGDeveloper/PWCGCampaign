package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.aaatruck.AAATruckPositionBuilder;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleFactory;

public class MissionPlayerVehicle
{
    private IVehicle playerVehicle = null;

    public void buildPlayerVehicle(Mission mission, VehicleDefinition playerVehicleDefinition, Side truckSide, Date date) throws PWCGException
    {
        if (truckSide == Side.ALLIED)
        {
            playerVehicle = VehicleFactory.createVehicleFromDefinition(CountryFactory.makeCountryByCountry(Country.RUSSIA), mission.getCampaign().getDate(), playerVehicleDefinition);

        }
        else
        {
            playerVehicle = VehicleFactory.createVehicleFromDefinition(CountryFactory.makeCountryByCountry(Country.GERMANY), mission.getCampaign().getDate(), playerVehicleDefinition);
        }
        
        setPlayerVehiclePosition(mission, truckSide);
        playerVehicle.setAiLevel(AiSkillLevel.PLAYER);
    }

    private void setPlayerVehiclePosition(Mission mission, Side truckSide) throws PWCGException
    {
        AAATruckPositionBuilder truckPositionBuilder = new AAATruckPositionBuilder(mission);
        truckPositionBuilder.buildTruckPosition(truckSide);
        
        playerVehicle.setPosition(truckPositionBuilder.getTruckPosition().copy());
        playerVehicle.getEntity().setPosition(truckPositionBuilder.getTruckPosition().copy());
        
        playerVehicle.setOrientation(truckPositionBuilder.getTruckOrientation().copy());
        playerVehicle.getEntity().setOrientation(truckPositionBuilder.getTruckOrientation().copy());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        if (playerVehicle != null)
        {
            playerVehicle.write(writer);
        }
    }

    public List<Integer> getPlayerVehicleIds()
    {
        List<Integer> truckIds = new ArrayList<>();
        if (playerVehicle != null)
        {
            truckIds.add(playerVehicle.getLinkTrId());
        }
        return truckIds;
    }
    
    public Coordinate getPosition()
    {
        return playerVehicle.getPosition().copy();
    }
}
