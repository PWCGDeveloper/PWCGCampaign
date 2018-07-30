package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.factory.VehicleFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleFactory;

public class GroundTroopConcentration extends GroundUnit
{
    private ArrayList<IVehicle> tanks = new ArrayList<IVehicle>();
    private ArrayList<IVehicle> trucks = new ArrayList<IVehicle>();
    private ArrayList<IVehicle> ammo = new ArrayList<IVehicle>();
    private Campaign campaign;
 	
	protected double heading = 90.0;

	public GroundTroopConcentration(Campaign campaign, GroundUnitInformation pwcgGroundUnitInformation) throws PWCGException
	{
        super(pwcgGroundUnitInformation);
		this.campaign = campaign;
	}

    public void createUnits() throws PWCGException 
    {
        createTrucks(); 
        createTanks(); 
        createArtillery(); 
        
        enableVehicles();
    }

    private void enableVehicles()
    {
        List<IVehicle> vehicles = getVehicles(); 
        for (IVehicle vehicle : vehicles)
        {
            vehicle.getEntity().setEnabled(1);
        }
    }

    private void createTrucks() throws PWCGException
    {
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        IVehicle truckType = vehicleFactory.createCargoTruck(pwcgGroundUnitInformation.getCountry());

        int numTrucks = calcNumTrucks();

        
        int numPerRow = new Double(Math.sqrt(numTrucks)).intValue();
        
        double tentSpacing = 15.0;
        Coordinate tentInitialPosition = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), 270, ((numPerRow * tentSpacing) / 2));

        for (int i = 0; i < numTrucks; ++i)
        {
            // Form a square
            int row = i / numPerRow;
            int column = i % numPerRow;
            
            
            Coordinate columnPosition = MathUtils.calcNextCoord(tentInitialPosition, 90, (column * tentSpacing));
            Coordinate tentPosition = MathUtils.calcNextCoord(columnPosition, 180, (row * tentSpacing));
            
            
            IVehicle truck = vehicleFactory.cloneTruck(truckType);
            truck.setPosition(tentPosition);

            Orientation orient = new Orientation();
            orient.setyOri(heading);
            truck.setOrientation(orient);
            
            trucks.add(truck);
        }       
    }

    private int calcNumTrucks() throws PWCGException
    {
        int numTrucks = 5;
        int randomTrucks = 0;
        
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            numTrucks = 2;
            randomTrucks = 2;
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            numTrucks = 6;
            randomTrucks = 6;
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            numTrucks = 8;
            randomTrucks = 8;
        }        

        numTrucks = numTrucks + RandomNumberGenerator.getRandom(randomTrucks+1);

        return numTrucks;
    }

    private void createArtillery() throws PWCGException
    {
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        IVehicle artillery = vehicleFactory.createArtillery(pwcgGroundUnitInformation.getCountry());

        int numArtillery = calcNumArtillery();
        
        int numPerRow = new Double(Math.sqrt(numArtillery)).intValue();
        
        double ammoSpacing = 15.0;
        Coordinate ammoBasePosition = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), 40.0, 150.0);
        Coordinate ammoCenterPosition = MathUtils.calcNextCoord(ammoBasePosition, 270, ((numPerRow * ammoSpacing) / 2));

        for (int i = 0; i < numArtillery; ++i)
        {
            // Form a square
            int column = i % numPerRow;
            
            
            Coordinate columnPosition = MathUtils.calcNextCoord(ammoCenterPosition, 90, (column * ammoSpacing));
            Coordinate ammoPosition = MathUtils.calcNextCoord(columnPosition, 180, (i * ammoSpacing));
            
            IVehicle ammoBox = vehicleFactory.cloneArtillery(artillery);
            ammoBox.setPosition(ammoPosition);

            Orientation orient = new Orientation();
            orient.setyOri(heading);
            ammoBox.setOrientation(orient);
            
            ammo.add(ammoBox);
        }       
    }

    private int calcNumArtillery() throws PWCGException
    {
        int numArtillery = 4;
        int randomArtillery = 4;
        
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            numArtillery = 4;
            randomArtillery = 4;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            numArtillery = 8;
            randomArtillery = 8;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            numArtillery = 8;
            randomArtillery = 8;
        }

        numArtillery = numArtillery + RandomNumberGenerator.getRandom(randomArtillery+1);

        return numArtillery;
    }

    private void createTanks() throws PWCGException
    {
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        IVehicle tankType = vehicleFactory.createTank(pwcgGroundUnitInformation.getCountry());

        Coordinate basePosition = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), 300.0, 150.0);

        int numTanks = calcNumTanks();
        
        for (int i = 0; i < numTanks; ++i)
        {
            IVehicle tank = vehicleFactory.cloneTank(tankType);
            
            Coordinate truckPosition = getRandomPosition(basePosition, 100);
            tank.setPosition(truckPosition);

            Orientation orient = new Orientation();
            orient.setyOri(heading);
            tank.setOrientation(orient);
            
            tanks.add(tank);
        }       
    }

    private int calcNumTanks() throws PWCGException
    {
        int numTanks = 4;
        int randomTanks = 4;
        
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            numTanks = 4;
            randomTanks = 4;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            numTanks = 7;
            randomTanks = 7;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            numTanks = 10;
            randomTanks = 10;
        }

        numTanks = numTanks + RandomNumberGenerator.getRandom(randomTanks+1);

        return numTanks;
    }
    

	@Override
	public List<IVehicle> getVehicles()
	{
		List<IVehicle> vehicles = new ArrayList<IVehicle>();
		
        vehicles.addAll(trucks);
        vehicles.addAll(tanks);
        vehicles.addAll(ammo);
        
		return vehicles;
	}

    public void write(BufferedWriter writer) throws PWCGException
    {       
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"Troop Concentration\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Troop Concentration\";");
            writer.newLine();
    
            pwcgGroundUnitInformation.getMissionBeginUnit().write(writer);
    
            for (IVehicle truck: trucks)
            {
                truck.write(writer);
            }
            
            for (IVehicle tent: tanks)
            {
                tent.write(writer);
            }
            
            for (IVehicle ammoBox: ammo)
            {
                ammoBox.write(writer);
            }
    
            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
    
    /**
     * @return
     * @throws PWCGException
     */
    private Coordinate getRandomPosition(Coordinate centerCoordinate, int diameter) throws PWCGException
    {
        // Place units within a circle from the position
        // Randomly generate distance from center and direction from center
        int distance = RandomNumberGenerator.getRandom(diameter);

        int angleOfPlacement = RandomNumberGenerator.getRandom(360);
        
        Coordinate position = MathUtils.calcNextCoord(centerCoordinate
                        , angleOfPlacement, distance);

        return position;
    }

    @Override
    public void createUnitMission() throws PWCGException
    {
        createUnits();        
    }

    @Override
    protected void createGroundTargetAssociations()
    {
        // No target associations
    }
}	

