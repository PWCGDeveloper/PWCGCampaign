package pwcg.aar.inmission.prelim;

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
    @Mock
    private DirectoryReader directoryReader;
    
    private List<String> sortedLogSets = new ArrayList<String>();
    
    @Before
    public void setup() throws PWCGException
    {
        sortedLogSets.add("LogSet1");
        sortedLogSets.add("LogSet2");
        sortedLogSets.add("LogSet3");
        
        Mockito.when(directoryReader.getSortedFilesWithFilter("[0].txt")).thenReturn(sortedLogSets);
    }

    
    @Test
    public void testGetLogSets () throws PWCGException
    {
        AARLogSetFinder logSetFinderTest = new AARLogSetFinder(directoryReader);
        List<String> returnSortedLogSets = logSetFinderTest.getSortedLogFileSets();
        assert(returnSortedLogSets.size() == 3);
    }
}
