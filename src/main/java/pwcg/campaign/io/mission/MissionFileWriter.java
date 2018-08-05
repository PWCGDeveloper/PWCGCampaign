package pwcg.campaign.io.mission;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IMissionFile;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.mission.AmbientGroundUnitBuilder;
import pwcg.mission.AssaultInformation;
import pwcg.mission.Mission;
import pwcg.mission.MissionBlockBuilder;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;
import pwcg.mission.ground.unittypes.transport.GroundTruckConvoyUnit;
import pwcg.mission.object.WindSock;

/**
 * @author Patrick Wilson
 * 
 * Writes a mission file from a mission object.
 * Overridden by BoS and Rof specific
 *
 */
public abstract class MissionFileWriter implements IMissionFile 
{
    protected String missionFileName = "";
    
    protected Mission mission = null;
	
	public MissionFileWriter (Mission mission)
	{
		this.mission = mission;
	}
	
    protected abstract List<FixedPosition> adjustBlockDamage(List<FixedPosition> fixedPositions) throws PWCGException;
    protected abstract void adjustBlockSmoke(List<FixedPosition> fixedPositions) throws PWCGException;
    protected abstract void writeProductSpecific(BufferedWriter writer) throws PWCGException;
    protected abstract void writeMissionOptions(BufferedWriter writer) throws PWCGIOException, PWCGException;

    @Override
    public void writeMission() throws PWCGException 
	{
		try
        {
		    BufferedWriter writer = createWriter();

		    writeMissionFileHeader(writer);            
            writeMissionOptions(writer);
            writeFlights(writer);		
            
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
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

    private BufferedWriter createWriter() throws PWCGException, IOException
    {
        String filename = getMissionFileName(mission.getCampaign()) ;
        String filePsth = getMissionFilePath(filename) + ".mission";
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
        mission.getMissionObjectiveSuccess().write(writer);
        mission.getMissionObjectiveFailure().write(writer);
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

        for (GroundTrainUnit trainUnit: ambientGroundUnitBuilder.getAmbientTrains())
        {
            trainUnit.write(writer);
        }
        
        for (GroundTruckConvoyUnit truckUnit: ambientGroundUnitBuilder.getAmbientTrucks())
        {
            truckUnit.write(writer);
        }
        
        for (AssaultInformation battle: ambientGroundUnitBuilder.getAmbientBattles())
        {
            for (GroundUnit grountUnit : battle.getGroundUnitCollection().getAllGroundUnits())
            {
                grountUnit.write(writer);
            }
        }
        
        for (GroundUnitSpawning aaa : ambientGroundUnitBuilder.getAAA())
        {
            aaa.write(writer);
        }
    }

    private void writeWindSock(BufferedWriter writer) throws PWCGException
    {
        WindSock windSock = WindSock.createWindSock(mission.getMissionFlightBuilder().getPlayerFlight());
        if (windSock != null)
        {
            windSock.write(writer);
        }
    }

    protected String getMissionFilePath(String fileName) throws PWCGException 
	{
		String filepath = "..\\Data\\Missions\\" + fileName;
		if (mission.getCampaign().getCampaignData().isCoop())
		{
			filepath = PWCGContextManager.getInstance().getDirectoryManager().getSimulatorDataDir() + "Multiplayer\\Cooperative\\" + fileName;

		}
		
		return filepath;
	}

	public static String getMissionFileName(Campaign campaign) 
	{
		String dateStr = DateUtils.getDateStringDashDelimitedYYYYMMDD(campaign.getDate());
		String filename = campaign.getCampaignData().getName() + " " + dateStr;
		
		return filename;
	}
}
