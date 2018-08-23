package pwcg.aar.inmission.phase1.parse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import pwcg.aar.inmission.phase1.parse.event.IAType10;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.aar.inmission.phase1.parse.event.IAType17;
import pwcg.aar.inmission.phase1.parse.event.IAType18;
import pwcg.aar.inmission.phase1.parse.event.IAType2;
import pwcg.aar.inmission.phase1.parse.event.IAType3;
import pwcg.aar.inmission.phase1.parse.event.IAType6;
import pwcg.aar.inmission.phase1.parse.event.LogEventFactory;
import pwcg.aar.inmission.phase1.parse.event.rof.AType17;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;

public class AARLogParser implements IAARLogParser 
{
    public static String UNKNOWN_MISSION_LOG_ENTITY = "-1";

    private AARMissionLogFileSet aarLogFileMissionFile;
    private AARLogFileSetFactory aarLogFileSetFactory = new AARLogFileSetFactory();
    private AARLogEventData logEventData = new AARLogEventData();

    public AARLogParser(AARMissionLogFileSet aarLogFileMissionFile)
    {
        this.aarLogFileMissionFile = aarLogFileMissionFile;
    }

    @Override
    public AARLogEventData parseLogFilesForMission() throws PWCGException 
    {
        try
        {
            String selectedFileSet = aarLogFileMissionFile.getLogFileName();
                        
            aarLogFileSetFactory.determineMissionResultsFileForRequestedFileSet(selectedFileSet);
            List<String> aarLogFilesForThisSet = aarLogFileSetFactory.getLogFileSets();
            if (aarLogFilesForThisSet.size() == 0)
            {
                throw new PWCGException("No files found for log set " + selectedFileSet);
            }
            
            for (String filename : aarLogFilesForThisSet) 
            {
                parseLogFile(filename);
            }
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGException(e.getMessage());
        }
        
        return logEventData;
    }

    private void parseLogFile(String filename) throws FileNotFoundException, IOException, PWCGException
    {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) 
        {
            if (line.contains(Atypes.getAType(2)))
            {
                parseDamagedEvent(line);
            }
            else if (line.contains(Atypes.getAType(3)))
            {
                parseDestroyedEvent(line);
            }
            else if (line.contains(Atypes.getAType(6)))
            {
                parseLandingEvent(line);
            }
            else if (line.contains(Atypes.getAType(10)))
            {
                parsePlayerSpawnEvent(line);
            }
            else if (line.contains(Atypes.getAType(12)))
            {
                parseSpawnEvent(line);
            }
            else if (line.contains(Atypes.getAType(17)))
            {
                parseWaypointEvent(line);
            }
            else if (line.contains(Atypes.getAType(18)))
            {
                parseBailoutEvent(line);
            }
        }

        reader.close();
    }

    private void parseDamagedEvent(String line) throws PWCGException
    {
        IAType2 atype2 = LogEventFactory.createAType2(line);
        logEventData.addDamageEvent(atype2);
    }

    private void parseDestroyedEvent(String line) throws PWCGException
    {
        IAType3 atype3 = LogEventFactory.createAType3(line);
        logEventData.addDestroyedEvent(atype3);
    }

    private void parseLandingEvent(String line) throws PWCGException
    {
        IAType6 atype6 = LogEventFactory.createAType6(line);
        logEventData.addLandingEvent(atype6);
    }

    private void parsePlayerSpawnEvent(String line) throws PWCGException
    {
        IAType10 atype10 = LogEventFactory.createAType10(line);
        IAType17 atype17 = new AType17(atype10);   
        logEventData.addWaypointEvent(atype17);
    }

    private void parseSpawnEvent(String line) throws PWCGException
    {
        IAType12 atype12 = LogEventFactory.createAType12(line);
        mapSpawnToMissionArtifactType(atype12);
    }

    private void mapSpawnToMissionArtifactType(IAType12 atype12)
    {
        if(atype12.getType().contains("Bot"))
        {
            logEventData.addBot(atype12.getId(), atype12);
        }
        else if (atype12.getPid().contains(UNKNOWN_MISSION_LOG_ENTITY))
        {
            logEventData.addVehicle(atype12.getId(), atype12);
        }
        else
        {
            logEventData.addTurret(atype12.getId(), atype12);
        }
    }

    private void parseWaypointEvent(String line) throws PWCGException
    {
        IAType17 atype17 = LogEventFactory.createAType17(line);
        logEventData.addWaypointEvent(atype17);
    }

    private void parseBailoutEvent(String line) throws PWCGException
    {
        IAType18 atype18 = LogEventFactory.createAType18(line);
        logEventData.addBailoutEvent(atype18);
    }
}

