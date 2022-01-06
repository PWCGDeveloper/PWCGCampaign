package pwcg.mission.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.FakeAirfield;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.AsyncJobRunner;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.MissionGroundUnitBuilder;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleFactory;
import pwcg.mission.ground.vehicle.VehicleSetBuilderComprehensive;
import pwcg.mission.mcu.effect.FirePotSeries;
import pwcg.mission.mcu.group.SmokeGroup;
import pwcg.mission.mcu.group.StopAttackingNearAirfieldSequence;
import pwcg.mission.object.WindSock;

public class MissionFileWriter implements IMissionFile 
{    
    private Mission mission = null;
	
	public MissionFileWriter (Mission mission)
	{
		this.mission = mission;
	}

    @Override
    public void writeMission() throws PWCGException 
	{
		try
        {
		    BufferedWriter writer = createWriter();

		    writeMissionFileHeader(writer);            
            writeMissionOptions(writer);
            writePlayerAAA(writer);       
            writeFlights(writer);       
            writeVehiclesForTest(writer);
            
            if (!TestDriver.getInstance().isCreatePlayerOnly())
            {
                writeMissionObjectives(writer);            
                writeIcons(writer);
                writeBlocks(writer);
                writeMissionGroundUnits(writer);
                writeWindStopSequences(writer);
                writeWindSock(writer);
                writeProductSpecific(writer);
            }
            
            writeMissionTrailer(writer);
            writer.flush();
            writer.close();
            
            runAsyncTasks();
            
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}

    private void writeWindStopSequences(BufferedWriter writer) throws PWCGException
    {
        for (StopAttackingNearAirfieldSequence stopAttackingNearAirfieldSequence : mission.getFinalizer().getStopSequenceForMission())
        {
            stopAttackingNearAirfieldSequence.write(writer);
        }
        
    }

    private void runAsyncTasks() throws PWCGException
    {
        if (MissionFileBinaryBuilder.canRunResaver())
        {
            AsyncJobRunner runner = new AsyncJobRunner("Generating mission");
            buildMissionBinaryFile(runner);
            runner.finish();
        }
    }
    
    private void buildMissionBinaryFile(AsyncJobRunner runner) throws PWCGException
    {
        int buildConfigFile = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.BuildBinaryMissionFileKey);
        if (buildConfigFile != 0)
        {
            runner.add("Generating binary mission file...", () -> MissionFileBinaryBuilder.buildMissionBinaryFile(mission.getCampaign(), MissionFileNameBuilder.buildMissionFileName(mission.getCampaign())));
        }
    }

    private void writeMissionOptions(BufferedWriter writer) throws PWCGException
    {
        MissionFileOptionWriter optionWriter = new MissionFileOptionWriter(mission);
        optionWriter.writeMissionOptions(writer);
    }

    private BufferedWriter createWriter() throws PWCGException, IOException
    {
        String filename = MissionFileNameBuilder.buildMissionFileName(mission.getCampaign()) ;
        String filePsth = getMissionFullFilePath(filename);
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePsth));
        return writer;
    }

    private void writeMissionFileHeader(BufferedWriter writer) throws IOException
    {
        writer.write("# Mission File Version = 1.0;");
        writer.newLine();
        writer.newLine();
    }

    private void writeMissionTrailer(BufferedWriter writer) throws IOException
    {
        writer.newLine();
        writer.write("# end of file");
        writer.newLine();
    }

    private void writeMissionObjectives(BufferedWriter writer) throws PWCGException
    {
        if (!mission.getCampaign().isCoop())
        {
            mission.getFinalizer().getMissionObjectiveSuccess().write(writer);
            mission.getFinalizer().getMissionObjectiveFailure().write(writer);
        }
    }

    private void writeFlights(BufferedWriter writer) throws PWCGException
    {
        MissionFlightWriter missionFlightWriter = new MissionFlightWriter(mission);
        missionFlightWriter.writeFlights(writer);
    }

    private void writePlayerAAA(BufferedWriter writer) throws PWCGException
    {
        mission.getMissionAAATrucks().write(writer);
    }

    private void writeBlocks(BufferedWriter writer) throws PWCGException
    {
        MissionBlockWriter.writeFixedPositions(writer, mission.getMissionBlocks().getAllStructuresForMission());
    }

    private void writeIcons(BufferedWriter writer) throws PWCGException
    {
        mission.getFinalizer().getWaypointIconBuilder().write(writer);            
        mission.getFinalizer().getFrontLineIconBuilder().write(writer);
        mission.getFinalizer().getAirfieldIconBuilder().write(writer);
        mission.getFinalizer().getSquadronIconBuilder().write(writer);
        mission.getFinalizer().getAssaultIconBuilder().write(writer);
    }

    private void writeMissionGroundUnits(BufferedWriter writer) throws PWCGException
    {
        MissionGroundUnitBuilder groundUnitBuilder = mission.getGroundUnitBuilder();
        groundUnitBuilder.write(writer);
    }

    private void writeVehiclesForTest(BufferedWriter writer) throws PWCGException
    {
        VehicleSetBuilderComprehensive vehicleSetBuilderComprehensive = mission.getVehicleSetBuilder();
        for (IVehicle vehicle: vehicleSetBuilderComprehensive.getAllVehicles())
        {
            vehicle.write(writer);
        }
    }

    private void writeWindSock(BufferedWriter writer) throws PWCGException
    {
    	for (IFlight playerFlight: mission.getFlights().getPlayerUnits())
    	{
	        WindSock windSock = WindSock.createWindSock(playerFlight);
	        if (windSock != null)
	        {
	            windSock.write(writer);
	        }
    	}
    }

    private String getMissionFullFilePath(String fileName) throws PWCGException 
	{
        String filepath = PWCGDirectorySimulatorManager.getInstance().getMissionFilePath(mission.getCampaign());        
		return filepath + fileName +  ".mission";
	}
	
    private void writeProductSpecific(BufferedWriter writer) throws PWCGException
    {
        writeFieldsInMission(writer);
        writeRadioBeacon(writer);
        writeRunwayT(writer);
        writeFakeAirfieldForAiReturnToBase(writer);
        writeSmoke(writer);
        writeFirePots(writer);
    }

    private void writeFieldsInMission(BufferedWriter writer) throws PWCGException
    {
        List<Airfield>  fieldsForPatrol = mission.getFieldsForPatrol();
        for (Airfield field : fieldsForPatrol)
        {
            if (!field.isGroup())
            {
                field.write(writer);
            }
        }
    }

    private void writeRadioBeacon(BufferedWriter writer) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            for (IFlight playerFlight:  mission.getFlights().getPlayerUnits())
            {
                IVehicle radioBeacon = VehicleFactory.createVehicle(
                        playerFlight.getFlightInformation().getCountry(), mission.getCampaign().getDate(), VehicleClass.RadioBeacon);
                if (radioBeacon != null)
                {
                    Airfield flightAirfield = playerFlight.getFlightInformation().getAirfield();
                    double takeoffOrientation = flightAirfield.getTakeoffLocation(mission).getOrientation().getyOri();

                    Double angleBeaconLeft = MathUtils.adjustAngle(takeoffOrientation, -90);

                    Campaign campaign = PWCGContext.getInstance().getCampaign();
                    ConfigManager configManager = campaign.getCampaignConfigManager();
                    int windsockDistance = configManager.getIntConfigParam(ConfigItemKeys.WindsockDistanceKey);
                    Coordinate beaconCoordMoveLeft = MathUtils.calcNextCoord(flightAirfield.getTakeoffLocation(mission).getPosition(), angleBeaconLeft, windsockDistance);

                    Coordinate beaconPos = MathUtils.calcNextCoord(beaconCoordMoveLeft, takeoffOrientation, 200.0);

                    radioBeacon.setPosition(beaconPos);
                    radioBeacon.setOrientation(flightAirfield.getOrientation().copy());
                    radioBeacon.setBeaconChannel(1);
                    radioBeacon.getEntity().enableEntity();
                    radioBeacon.write(writer);
                }
            }
        }
    }

    private void writeRunwayT(BufferedWriter writer) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            for (IFlight playerFlight:  mission.getFlights().getPlayerUnits())
            {
                IVehicle landCanvas = VehicleFactory.createVehicle(
                        playerFlight.getFlightInformation().getCountry(), mission.getCampaign().getDate(), VehicleClass.LandCanvas);
                if (landCanvas != null)
                {
                    Airfield flightAirfield = playerFlight.getFlightInformation().getAirfield();
                    Orientation takeoffOrientation = flightAirfield.getTakeoffLocation(mission).getOrientation().copy();
                    IVehicle landCanvas2 = VehicleFactory.createVehicle(
                            playerFlight.getFlightInformation().getCountry(), mission.getCampaign().getDate(), VehicleClass.LandCanvas);

                    Double angleRight = MathUtils.adjustAngle(takeoffOrientation.getyOri(), 90);

                    Campaign campaign = PWCGContext.getInstance().getCampaign();
                    ConfigManager configManager = campaign.getCampaignConfigManager();
                    int windsockDistance = configManager.getIntConfigParam(ConfigItemKeys.WindsockDistanceKey);
                    Coordinate pos1 = MathUtils.calcNextCoord(flightAirfield.getTakeoffLocation(mission).getPosition(), angleRight, windsockDistance);
                    pos1 = MathUtils.calcNextCoord(pos1, takeoffOrientation.getyOri(), -15.0);

                    landCanvas.setPosition(pos1);
                    landCanvas.setOrientation(takeoffOrientation);
                    landCanvas.getEntity().enableEntity();
                    landCanvas.write(writer);

                    Coordinate pos2 = MathUtils.calcNextCoord(pos1, takeoffOrientation.getyOri(), 5.0);
                    Orientation orient2 = takeoffOrientation.copy();
                    orient2.setyOri(angleRight);

                    landCanvas2.setPosition(pos2);
                    landCanvas2.setOrientation(orient2);
                    landCanvas2.getEntity().enableEntity();
                    landCanvas2.write(writer);
                }
            }
        }
    }

    private void writeFakeAirfieldForAiReturnToBase(BufferedWriter writer) throws PWCGException
    {
        Map<String, Airfield> allAirFields =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAllAirfields();
        for (Airfield airfield : allAirFields.values())
        {
            FakeAirfield fakeAirfiield = new FakeAirfield(airfield, mission);
            fakeAirfiield.write(writer);
        }
    }

    private void writeSmoke(BufferedWriter writer) throws PWCGException
    {
        for (SmokeGroup smokeGroup : mission.getMissionEffects().getSmokeGroups())
        {
            smokeGroup.write(writer);
        }
    }

    private void writeFirePots(BufferedWriter writer) throws PWCGException
    {
        for (FirePotSeries firePotGroup : mission.getMissionEffects().getFirePotsForPlayerRunways())
        {
            firePotGroup.write(writer);
        }
    }
}
