package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;

@ExtendWith(MockitoExtension.class)
public class MissionResultLogFileCleanerTest
{
    @Mock private DirectoryReader directoryReader;
    @Mock private ConfigManagerGlobal configManagerGlobal;

    private List<String> filenames = new ArrayList<String>();

    public MissionResultLogFileCleanerTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @BeforeEach
    public void setupTest() throws PWCGException
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
    }

    @Test
    public void testCleanMissionLogs() throws PWCGException
    {
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        long oneDayAgoPlus = System.currentTimeMillis() - 86500000;
        try (MockedStatic<ConfigManagerGlobal> mocked = Mockito.mockStatic(ConfigManagerGlobal.class)) {
            mocked.when(() -> ConfigManagerGlobal.getInstance()).thenReturn(configManagerGlobal);

            try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
                mockedFileUtils.when(() -> FileUtils.ageOfFilesInMillis(ArgumentMatchers.<String>any())).thenReturn(oneDayAgoPlus);
                mockedFileUtils.when(() -> FileUtils.fileExists(ArgumentMatchers.<String>any())).thenReturn(true);
    
                Mockito.when(directoryReader.getFiles()).thenReturn(filenames);
                Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(1);
            
                MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader);
                    
                int numCleaned = cleaner.cleanMissionResultLogFiles();
                    
                assert(numCleaned == filenames.size());
            }
        }
    }

    @Test
    public void testCleanMissionLogsFilesAreNew() throws PWCGException
    {
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        long lessThanOneDayAgo = System.currentTimeMillis() - 86300000;
        try (MockedStatic<ConfigManagerGlobal> mocked = Mockito.mockStatic(ConfigManagerGlobal.class)) {
            mocked.when(() -> ConfigManagerGlobal.getInstance()).thenReturn(configManagerGlobal);

            try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
                mockedFileUtils.when(() -> FileUtils.ageOfFilesInMillis(ArgumentMatchers.<String>any())).thenReturn(lessThanOneDayAgo);
                mockedFileUtils.when(() -> FileUtils.fileExists(ArgumentMatchers.<String>any())).thenReturn(true);

                Mockito.when(directoryReader.getFiles()).thenReturn(filenames);
    
                Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(1);
    
                MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader);        
                int numCleaned = cleaner.cleanMissionResultLogFiles();
        
                assert(numCleaned == 0);
            }
        }
    }

    @Test
    public void testCleanMissionLogsNotEnabled() throws PWCGException
    {        
        long oneDayAgoPlus = System.currentTimeMillis() - 86500000;
        try (MockedStatic<ConfigManagerGlobal> mocked = Mockito.mockStatic(ConfigManagerGlobal.class)) {
            mocked.when(() -> ConfigManagerGlobal.getInstance()).thenReturn(configManagerGlobal);

            try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
                mockedFileUtils.when(() -> FileUtils.ageOfFilesInMillis(ArgumentMatchers.<String>any())).thenReturn(oneDayAgoPlus);
                mockedFileUtils.when(() -> FileUtils.fileExists(ArgumentMatchers.<String>any())).thenReturn(true);

                Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(0);
        
                MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader);
                
                int numCleaned = cleaner.cleanMissionResultLogFiles();
                
                assert(numCleaned == 0);
            }
        }
    }

    @Test
    public void testCleanMissionLogsNoFiles() throws PWCGException
    {        
        long oneDayAgoPlus = System.currentTimeMillis() - 86500000;
        try (MockedStatic<ConfigManagerGlobal> mocked = Mockito.mockStatic(ConfigManagerGlobal.class)) {
            mocked.when(() -> ConfigManagerGlobal.getInstance()).thenReturn(configManagerGlobal);

            try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
                mockedFileUtils.when(() -> FileUtils.ageOfFilesInMillis(ArgumentMatchers.<String>any())).thenReturn(oneDayAgoPlus);
                mockedFileUtils.when(() -> FileUtils.fileExists(ArgumentMatchers.<String>any())).thenReturn(true);

                List<String> filenames = new ArrayList<String>();
        
                Mockito.when(FileUtils.ageOfFilesInMillis(ArgumentMatchers.<String>any())).thenReturn(Long.valueOf(oneDayAgoPlus));
                Mockito.when(directoryReader.getFiles()).thenReturn(filenames);
        
                Mockito.when(configManagerGlobal.getIntConfigParam(ConfigItemKeys.DeleteAllMissionLogsKey)).thenReturn(1);
        
                MissionResultLogFileCleaner cleaner = new MissionResultLogFileCleaner(directoryReader);
                
                int numCleaned = cleaner.cleanMissionResultLogFiles();
                
                assert(numCleaned == 0);
            }
        }
    }
}
