package pwcg.mission.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.FakeAirfield;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.AmbientBalloonBuilder;
import pwcg.mission.Mission;
import pwcg.mission.MissionBlockBuilder;
import pwcg.mission.MissionBlockDamage;
import pwcg.mission.MissionBlockSmoke;
import pwcg.mission.ambient.AmbientGroundUnitBuilder;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleFactory;
import pwcg.mission.ground.vehicle.VehicleSetBuilderComprehensive;
import pwcg.mission.mcu.effect.FirePotSeries;
import pwcg.mission.mcu.group.SmokeGroup;
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
            writeFlights(writer);		
            writeVehiclesForTest(writer);
            
            if (!TestDriver.getInstance().isCreatePlayerOnly())
            {
                writeMissionObjectives(writer);            
                writeIcons(writer);
                writeBlocks(writer);
                writeAmbientGroundUnits(writer);
                writeWindSock(writer);
                writeProductSpecific(writer);
            }
            
            writeMissionTrailer(writer);
            writer.flush();
            writer.close();
            
            buildMissionBinaryFile();
            
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
        catch (InterruptedException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
    
    private void buildMissionBinaryFile() throws PWCGException, InterruptedException
    {
        int buildConfigFile = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.BuildBinaryMissionFileKey);
        if (buildConfigFile != 0)
        {
            MissionFileBinaryBuilder.buildMissionBinaryFile(mission.getCampaign(), getMissionFileName(mission.getCampaign()));
        }
    }

    private void writeMissionOptions(BufferedWriter writer) throws PWCGException
    {
        MissionFileOptionWriter optionWriter = new MissionFileOptionWriter(mission);
        optionWriter.writeMissionOptions(writer);
    }

    private BufferedWriter createWriter() throws PWCGException, IOException
    {
        String filename = getMissionFileName(mission.getCampaign()) ;
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
            mission.getMissionObjectiveSuccess().write(writer);
            mission.getMissionObjectiveFailure().write(writer);
        }
    }

    private void writeFlights(BufferedWriter writer) throws PWCGException
    {
        MissionFlightWriter missionFlightWriter = new MissionFlightWriter(mission);
        missionFlightWriter.writeFlights(writer);
    }

    private void writeBlocks(BufferedWriter writer) throws PWCGException
    {
        MissionBlockBuilder missionBlockBuilder = new MissionBlockBuilder(mission);
        missionBlockBuilder.buildFixedPositionsForMission();
        
        List<FixedPosition> damagedFixedPositions = adjustBlockDamage(missionBlockBuilder.getPositionsForMission());
        adjustBlockSmoke(damagedFixedPositions);
        
        MissionBlockWriter missionBlockWriter = new MissionBlockWriter(missionBlockBuilder);
        missionBlockWriter.writeFixedPositions(writer, missionBlockBuilder.getPositionsForMission());
    }

    private void writeIcons(BufferedWriter writer) throws PWCGIOException
    {
        mission.getMissionWaypointIconBuilder().write(writer);            
        mission.getMissionFrontLineIconBuilder().write(writer);
        mission.getMissionAirfieldIconBuilder().write(writer);
    }

    private void writeAmbientGroundUnits(BufferedWriter writer) throws PWCGException
    {
        AmbientGroundUnitBuilder ambientGroundUnitBuilder = mission.getAmbientGroundUnitBuilder();
        ambientGroundUnitBuilder.write(writer);

        AmbientBalloonBuilder ambientBalloonBuilder = mission.getAmbientBalloonBuilder();
        ambientBalloonBuilder.write(writer);
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
    	for (IFlight playerFlight: mission.getMissionFlightBuilder().getPlayerFlights())
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
        String filepath = PWCGContext.getInstance().getDirectoryManager().getMissionFilePath(mission.getCampaign());        
		return filepath + fileName +  ".mission";
	}

	public static String getMissionFileName(Campaign campaign) 
	{
		String dateStr = DateUtils.getDateStringDashDelimitedYYYYMMDD(campaign.getDate());
		String filename = campaign.getCampaignData().getName() + " " + dateStr;
		
		return filename;
	}
	
    
    private List<FixedPosition> adjustBlockDamage(List<FixedPosition> fixedPositions) throws PWCGException
    {
        MissionBlockDamage missionBlockDamage = new MissionBlockDamage(mission);      
        return missionBlockDamage.setDamageToFixedPositions(fixedPositions);
    }
    
    private void adjustBlockSmoke(List<FixedPosition> fixedPositions) throws PWCGException
    {
        MissionBlockSmoke missionBlockSmoke = new MissionBlockSmoke(mission);      
        missionBlockSmoke.addSmokeToDamagedAreas(fixedPositions);
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
        List<IAirfield>  fieldsForPatrol = mission.getFieldsForPatrol();
        for (IAirfield field : fieldsForPatrol)
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
            for (IFlight playerFlight:  mission.getMissionFlightBuilder().getPlayerFlights())
            {
                IVehicle radioBeacon = VehicleFactory.createVehicle(
                        playerFlight.getFlightInformation().getCountry(), mission.getCampaign().getDate(), VehicleClass.RadioBeacon);
                if (radioBeacon != null)
                {
                    IAirfield flightAirfield = playerFlight.getFlightInformation().getAirfield();
                    double takeoffOrientation = flightAirfield.getTakeoffLocation().getOrientation().getyOri();

                    Double angleBeaconLeft = MathUtils.adjustAngle(takeoffOrientation, -90);

                    Campaign campaign = PWCGContext.getInstance().getCampaign();
                    ConfigManager configManager = campaign.getCampaignConfigManager();
                    int windsockDistance = configManager.getIntConfigParam(ConfigItemKeys.WindsockDistanceKey);
                    Coordinate beaconCoordMoveLeft = MathUtils.calcNextCoord(flightAirfield.getTakeoffLocation().getPosition(), angleBeaconLeft, windsockDistance);

                    Coordinate beaconPos = MathUtils.calcNextCoord(beaconCoordMoveLeft, takeoffOrientation, 200.0);

                    radioBeacon.setPosition(beaconPos);
                    radioBeacon.setOrientation(flightAirfield.getOrientation().copy());
                    radioBeacon.setBeaconChannel(1);
                    radioBeacon.getEntity().setEnabled(1);
                    radioBeacon.write(writer);
                }
            }
        }
    }

    private void writeRunwayT(BufferedWriter writer) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            for (IFlight playerFlight:  mission.getMissionFlightBuilder().getPlayerFlights())
            {
                IVehicle landCanvas = VehicleFactory.createVehicle(
                        playerFlight.getFlightInformation().getCountry(), mission.getCampaign().getDate(), VehicleClass.LandCanvas);
                if (landCanvas != null)
                {
                    IAirfield flightAirfield = playerFlight.getFlightInformation().getAirfield();
                    Orientation takeoffOrientation = flightAirfield.getTakeoffLocation().getOrientation().copy();
                    IVehicle landCanvas2 = VehicleFactory.createVehicle(
                            playerFlight.getFlightInformation().getCountry(), mission.getCampaign().getDate(), VehicleClass.LandCanvas);

                    Double angleRight = MathUtils.adjustAngle(takeoffOrientation.getyOri(), 90);

                    Campaign campaign = PWCGContext.getInstance().getCampaign();
                    ConfigManager configManager = campaign.getCampaignConfigManager();
                    int windsockDistance = configManager.getIntConfigParam(ConfigItemKeys.WindsockDistanceKey);
                    Coordinate pos1 = MathUtils.calcNextCoord(flightAirfield.getTakeoffLocation().getPosition(), angleRight, windsockDistance);
                    pos1 = MathUtils.calcNextCoord(pos1, takeoffOrientation.getyOri(), -15.0);

                    landCanvas.setPosition(pos1);
                    landCanvas.setOrientation(takeoffOrientation);
                    landCanvas.getEntity().setEnabled(1);
                    landCanvas.write(writer);

                    Coordinate pos2 = MathUtils.calcNextCoord(pos1, takeoffOrientation.getyOri(), 5.0);
                    Orientation orient2 = takeoffOrientation.copy();
                    orient2.setyOri(angleRight);

                    landCanvas2.setPosition(pos2);
                    landCanvas2.setOrientation(orient2);
                    landCanvas2.getEntity().setEnabled(1);
                    landCanvas2.write(writer);
                }
            }
        }
    }

    private void writeFakeAirfieldForAiReturnToBase(BufferedWriter writer) throws PWCGException
    {
        Map<String, IAirfield> allAirFields =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAllAirfields();
        for (IAirfield airfield : allAirFields.values())
        {
            FakeAirfield fakeAirfiield = new FakeAirfield(airfield, mission.getCampaign().getDate());
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
