package pwcg.aar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigManagerGlobal.class, FileUtils.class})
public class MissionResultLogFileCleanerTest
{
    @Mock private DirectoryReader directoryReader;
    @Mock private ConfigManagerGlobal configManagerGlobal;

    private List<File> filenames = new ArrayList<>();

    @Mock private File file_2016_10_25_0;
    @Mock private File file_2016_10_25_1;
    @Mock private File file_2016_10_25_2;

    @Mock private File file_2016_11_22_0;
    @Mock private File file_2016_11_22_1;
    @Mock private File file_2016_11_22_2;
    @Mock private File file_2016_11_22_3;
    @Mock private File file_2016_11_22_4;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PowerMockito.mockStatic(ConfigManagerGlobal.class);
        PowerMockito.mockStatic(FileUtils.class);

        Mockito.when(ConfigManagerGlobal.getInstance()).thenReturn(configManagerGlobal);

        Mockito.when(file_2016_10_25_0.getName()).thenReturn("missionReport(2016-10-25-22-51-08)[0].txt");
        Mockito.when(file_2016_10_25_1.getName()).thenReturn("missionReport(2016-10-25-22-51-08)[1].txt");
        Mockito.when(file_2016_10_25_2.getName()).thenReturn("missionReport(2016-10-25-22-51-08)[2].txt");

        Mockito.when(file_2016_11_22_0.getName()).thenReturn("missionReport(2016-09-11-22-51-08)[0].txt");
        Mockito.when(file_2016_11_22_1.getName()).thenReturn("missionReport(2016-09-11-22-51-08)[1].txt");
        Mockito.when(file_2016_11_22_2.getName()).thenReturn("missionReport(2016-09-11-22-51-08)[2].txt");
        Mockito.when(file_2016_11_22_3.getName()).thenReturn("missionReport(2016-09-11-22-51-08)[3].txt");
        Mockito.when(file_2016_11_22_4.getName()).thenReturn("missionReport(2016-09-11-22-51-08)[4].txt");

        Mockito.when(file_2016_10_25_0.exists()).thenReturn(true);
        Mockito.when(file_2016_10_25_1.exists()).thenReturn(true);
        Mockito.when(file_2016_10_25_2.exists()).thenReturn(true);
        
        Mockito.when(file_2016_11_22_0.exists()).thenReturn(true);
        Mockito.when(file_2016_11_22_1.exists()).thenReturn(true);
        Mockito.when(file_2016_11_22_2.exists()).thenReturn(true);
        Mockito.when(file_2016_11_22_3.exists()).thenReturn(true);
        Mockito.when(file_2016_11_22_4.exists()).thenReturn(true);

        filenames = new ArrayList<File>();
        filenames.add(file_2016_10_25_0);
        filenames.add(file_2016_10_25_1);
        filenames.add(file_2016_10_25_2);

        filenames.add(file_2016_11_22_0);
        filenames.add(file_2016_11_22_1);
        filenames.add(file_2016_11_22_2);
        filenames.add(file_2016_11_22_3);
        filenames.add(file_2016_11_22_4);


        
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);
    }

    @Test
    public void testCleanMissionLogs() throws PWCGException
    {
        long oneDayAgoPlus = System.currentTimeMillis() - 86500000;
        Mockito.when(FileUtils.ageOfFilesInMillis(ArgumentMatchers.<File>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);
        Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(1);

        MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader);
        
        int numCleaned = cleaner.cleanMissionResultLogFiles();
        
        assert(numCleaned == filenames.size());
    }

    @Test
    public void testCleanMissionLogsFilesAreNew() throws PWCGException
    {
        long oneDayAgoPlus = System.currentTimeMillis() - 86300000;
        Mockito.when(FileUtils.ageOfFilesInMillis(ArgumentMatchers.<File>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);
    
        Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(1);
    
        MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader);        
        int numCleaned = cleaner.cleanMissionResultLogFiles();
        
        assert(numCleaned == 0);
    }

    @Test
    public void testCleanMissionLogsNotEnabled() throws PWCGException
    {        
        long oneDayAgoPlus = System.currentTimeMillis() - 86500000;
        Mockito.when(FileUtils.ageOfFilesInMillis(ArgumentMatchers.<File>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(0);

        MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader);
        
        int numCleaned = cleaner.cleanMissionResultLogFiles();
        
        assert(numCleaned == 0);
    }

    @Test
    public void testCleanMissionLogsNoFiles() throws PWCGException
    {        
        List<File> filenames = new ArrayList<>();

        long oneDayAgoPlus = System.currentTimeMillis() - 86500000;
        Mockito.when(FileUtils.ageOfFilesInMillis(ArgumentMatchers.<File>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(1);

        MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader);
        
        int numCleaned = cleaner.cleanMissionResultLogFiles();
        
        assert(numCleaned == 0);
    }
}
