package pwcg.aar.inmission.prelim;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.prelim.AARLogSetFinder;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;

@RunWith(MockitoJUnitRunner.class)
public class AARLogSetFinderTest
{
    @Mock private DirectoryReader directoryReader;
    
    @Mock private File file_2016_10_25;
    @Mock private File file_2016_10_26;
    @Mock private File file_2016_10_27;

    private List<File> sortedLogSets = new ArrayList<>();
    
    @Before
    public void setup() throws PWCGException
    {
        Mockito.when(file_2016_10_25.getName()).thenReturn("missionReport(2016-10-25-22-51-08)[0].txt");
        Mockito.when(file_2016_10_26.getName()).thenReturn("missionReport(2016-10-26-22-51-08)[0].txt");
        Mockito.when(file_2016_10_27.getName()).thenReturn("missionReport(2016-10-27-22-51-08)[0].txt");

        sortedLogSets.add(file_2016_10_25);
        sortedLogSets.add(file_2016_10_27);
        sortedLogSets.add(file_2016_10_26);
        
        Mockito.when(directoryReader.getSortedFilesWithFilter("[0].txt")).thenReturn(sortedLogSets);
    }

    
    @Test
    public void testGetLogSets () throws PWCGException
    {
        AARLogSetFinder logSetFinderTest = new AARLogSetFinder(directoryReader);
        List<File> returnSortedLogSets = logSetFinderTest.getSortedLogFileSets();
        assert(returnSortedLogSets.size() == 3);
        assert(returnSortedLogSets.get(2).getName().equals(file_2016_10_27.getName()));
    }
}
