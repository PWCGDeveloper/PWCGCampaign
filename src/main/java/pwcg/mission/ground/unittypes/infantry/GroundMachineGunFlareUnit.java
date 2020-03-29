package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.McuFlare;
import pwcg.mission.mcu.group.FlareSequence;

public class GroundMachineGunFlareUnit extends GroundUnit
{
    private List<FlareSequence> flareSequences = new ArrayList<>();
    private IFlight triggeringFlight;
    
    public GroundMachineGunFlareUnit(GroundUnitInformation pwcgGroundUnitInformation, IFlight triggeringFlight)
    {
        super(VehicleClass.MachineGun, pwcgGroundUnitInformation);
        this.triggeringFlight = triggeringFlight;
    }   

    @Override
    public void createGroundUnit() throws PWCGException
    {
        super.createSpawnTimer();
        List<Coordinate> vehicleStartPositions = createVehicleStartPositions();
        super.createVehicles(vehicleStartPositions);
        addAspects();
        super.linkElements();
        createFlares(vehicleStartPositions);
    }

    protected int calcNumUnits() throws PWCGException
    {
        return 2;
    }

    protected List<Coordinate> createVehicleStartPositions() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();

        int numMachineGun = calcNumUnits();
        
        double initialPlacementAngle = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 180.0);      
        Coordinate machineGunCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), initialPlacementAngle, 50.0);

        double startLocationOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 270);             
        double machingGunSpacing = 2000.0;
        machineGunCoords = MathUtils.calcNextCoord(machineGunCoords, startLocationOrientation, ((numMachineGun * machingGunSpacing) / 2));       
        
        double placementOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 90.0);        

        for (int i = 0; i < numMachineGun; ++i)
        {   
            spawnerLocations.add(machineGunCoords);
            machineGunCoords = MathUtils.calcNextCoord(machineGunCoords, placementOrientation, machingGunSpacing);
        }       
        return spawnerLocations;       
    }   

    protected void addAspects() throws PWCGException
    {
        super.addDirectFireAspect();
    }

    private void createFlares(List<Coordinate> vehicleStartPositions) throws PWCGException 
    {
        for (Coordinate flareCoordinate : vehicleStartPositions)
        {
            int flareColor = McuFlare.FLARE_COLOR_RED;        
            if (pwcgGroundUnitInformation.getCountry().getSide() == Side.ALLIED)
            {
                flareColor = McuFlare.FLARE_COLOR_GREEN;
            }
            
            FlareSequence flareSequence = new FlareSequence();
            flareSequence.createFlareSequence(triggeringFlight, flareCoordinate.copy(), flareColor, super.getVehicles().get(0).getEntity().getIndex());
            flareSequences.add(flareSequence);
        }
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {       
        super.write(writer);
        for (FlareSequence flareSequence : flareSequences)
        {
            flareSequence.write(writer);
        }
    }
}	
