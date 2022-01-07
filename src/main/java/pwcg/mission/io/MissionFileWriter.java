package pwcg.mission.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.AsyncJobRunner;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.ground.MissionGroundUnitBuilder;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleSetBuilderComprehensive;
import pwcg.mission.mcu.group.SmokeGroup;
import pwcg.mission.mcu.group.StopAttackingNearAirfieldSequence;

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
            writeUnits(writer);       
            writeVehiclesForTest(writer);
            
            if (!TestDriver.getInstance().isCreatePlayerOnly())
            {
                writeMissionObjectives(writer);            
                writeIcons(writer);
                writeBlocks(writer);
                writeMissionGroundUnits(writer);
                writeWindStopSequences(writer);
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

    private void writeUnits(BufferedWriter writer) throws PWCGException
    {
        MissionUnitWriter missionFlightWriter = new MissionUnitWriter(mission);
        missionFlightWriter.writeFlights(writer);
    }

    private void writeBlocks(BufferedWriter writer) throws PWCGException
    {
        MissionBlockWriter.writeFixedPositions(writer, mission.getMissionBlocks().getAllStructuresForMission());
    }

    private void writeIcons(BufferedWriter writer) throws PWCGException
    {
        mission.getFinalizer().getWaypointIconBuilder().write(writer);            
        mission.getFinalizer().getFrontLineIconBuilder().write(writer);
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

    private String getMissionFullFilePath(String fileName) throws PWCGException 
	{
        String filepath = PWCGDirectorySimulatorManager.getInstance().getMissionFilePath(mission.getCampaign());        
		return filepath + fileName +  ".mission";
	}
	
    private void writeProductSpecific(BufferedWriter writer) throws PWCGException
    {
        writeFieldsInMission(writer);
        writeSmoke(writer);
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

    private void writeSmoke(BufferedWriter writer) throws PWCGException
    {
        for (SmokeGroup smokeGroup : mission.getMissionEffects().getSmokeGroups())
        {
            smokeGroup.write(writer);
        }
    }
}
