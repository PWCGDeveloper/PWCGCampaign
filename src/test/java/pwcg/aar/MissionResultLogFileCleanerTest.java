package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;

@RunWith(MockitoJUnitRunner.class)
public class MissionResultLogFileCleanerTest
{
    @Mock
    private DirectoryReader directoryReader;
    
    @Mock
    private FileUtils fileUtils;

    private List<String> filenames = new ArrayList<String>();

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        filenames = new ArrayList<String>();
        filenames.add("missionReport(2016-10-25-22-51-08)[0].txt");
        filenames.add("missionReport(2016-10-25-22-51-08)[1].txt");
        filenames.add("missionReport(2016-10-25-22-51-08)[2].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[0].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[1].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[2].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[3].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[4].txt");
        
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);
    }

    @Test
    public void testCleanMissionLogs() throws PWCGException
    {
        long oneDayAgoPlus = System.currentTimeMillis() - 86500000;
        Mockito.when(fileUtils.ageOfFilesInMillis(Matchers.<String>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        ConfigManagerGlobal.getInstance().setParam(ConfigItemKeys.DeleteAllMissionLogsKey, "1");

        MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader, fileUtils);
        cleaner.setDirectoryReader(directoryReader);
        cleaner.setFileUtils(fileUtils);
        
        int numCleaned = cleaner.cleanMissionResultLogFiles();
        
        assert(numCleaned == filenames.size());
    }

    @Test
    public void testCleanMissionLogsFilesAreNew() throws PWCGException
    {
        long oneDayAgoPlus = System.currentTimeMillis() - 86300000;
        Mockito.when(fileUtils.ageOfFilesInMillis(Matchers.<String>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);
    
        ConfigManagerGlobal.getInstance().setParam(ConfigItemKeys.DeleteAllMissionLogsKey, "1");
    
        MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader, fileUtils);
        cleaner.setDirectoryReader(directoryReader);
        cleaner.setFileUtils(fileUtils);
        
        int numCleaned = cleaner.cleanMissionResultLogFiles();
        
        assert(numCleaned == 0);
    }

    @Test
    public void testCleanMissionLogsNotEnabled() throws PWCGException
    {        
        List<String> filenames = new ArrayList<String>();
        filenames.add("missionReport(2016-10-25-22-51-08)[0].txt");
        filenames.add("missionReport(2016-10-25-22-51-08)[1].txt");
        filenames.add("missionReport(2016-10-25-22-51-08)[2].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[0].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[1].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[2].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[3].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[4].txt");

        long oneDayAgoPlus = System.currentTimeMillis() - 86500000;
        Mockito.when(fileUtils.ageOfFilesInMillis(Matchers.<String>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        ConfigManagerGlobal.getInstance().setParam(ConfigItemKeys.DeleteAllMissionLogsKey, "0");

        MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader, fileUtils);
        cleaner.setDirectoryReader(directoryReader);
        cleaner.setFileUtils(fileUtils);
        
        int numCleaned = cleaner.cleanMissionResultLogFiles();
        
        assert(numCleaned == 0);
    }

    @Test
    public void testCleanMissionLogsNoFiles() throws PWCGException
    {        
        List<String> filenames = new ArrayList<String>();

        long oneDayAgoPlus = System.currentTimeMillis() - 86500000;
        Mockito.when(fileUtils.ageOfFilesInMillis(Matchers.<String>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        ConfigManagerGlobal.getInstance().setParam(ConfigItemKeys.DeleteAllMissionLogsKey, "1");

        MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader, fileUtils);
        cleaner.setDirectoryReader(directoryReader);
        cleaner.setFileUtils(fileUtils);
        
        int numCleaned = cleaner.cleanMissionResultLogFiles();
        
        assert(numCleaned == 0);
    }
}
