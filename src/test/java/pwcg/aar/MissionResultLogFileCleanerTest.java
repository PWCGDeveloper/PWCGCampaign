package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigManagerGlobal.class, FileUtils.class})
public class MissionResultLogFileCleanerTest
{
    @Mock private DirectoryReader directoryReader;
    @Mock private FileUtils fileUtils;
    @Mock private ConfigManagerGlobal configManagerGlobal;

    private List<String> filenames = new ArrayList<String>();

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PowerMockito.mockStatic(ConfigManagerGlobal.class);
        PowerMockito.mockStatic(FileUtils.class);

        Mockito.when(ConfigManagerGlobal.getInstance()).thenReturn(configManagerGlobal);

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
        Mockito.when(FileUtils.ageOfFilesInMillis(Matchers.<String>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);
        Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(1);

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
        Mockito.when(FileUtils.ageOfFilesInMillis(Matchers.<String>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);
    
        Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(1);
    
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

        FileUtils mock = PowerMockito.mock(FileUtils.class);
        long oneDayAgoPlus = System.currentTimeMillis() - 86500000;
        Mockito.when(FileUtils.ageOfFilesInMillis(Matchers.<String>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(0);

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
        Mockito.when(FileUtils.ageOfFilesInMillis(Matchers.<String>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(1);

        MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader, fileUtils);
        cleaner.setDirectoryReader(directoryReader);
        cleaner.setFileUtils(fileUtils);
        
        int numCleaned = cleaner.cleanMissionResultLogFiles();
        
        assert(numCleaned == 0);
    }
}
