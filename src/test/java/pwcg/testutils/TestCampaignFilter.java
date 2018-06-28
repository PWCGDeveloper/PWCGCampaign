package pwcg.testutils;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;

public class TestCampaignFilter 
{
    public List<TestCampaignDescription> getCampaign(TestCampaignDescription requestedTestCampaignDescription, List<TestCampaignDescription> testCampaignsToSearch) throws PWCGException
    {
        testCampaignsToSearch = filterByService(requestedTestCampaignDescription, testCampaignsToSearch);
        testCampaignsToSearch = filterByDate(requestedTestCampaignDescription, testCampaignsToSearch);
        testCampaignsToSearch = filterByMap(requestedTestCampaignDescription, testCampaignsToSearch);
        testCampaignsToSearch = filterByRole(requestedTestCampaignDescription, testCampaignsToSearch);
        
        return testCampaignsToSearch;
    }

    private List<TestCampaignDescription> filterByService(TestCampaignDescription requestedTestCampaignDescription, List<TestCampaignDescription> testCampaignsToSearch)
    {
        List<TestCampaignDescription> testCampaignsMatching = new ArrayList<>();
        if (requestedTestCampaignDescription.getService() != null)
        {
            for (TestCampaignDescription testCampaignToSearch : testCampaignsToSearch)
            {
                if (testCampaignToSearch.getService().getServiceId() == requestedTestCampaignDescription.getService().getServiceId())
                {
                    testCampaignsMatching.add(testCampaignToSearch);
                }
            }
        }
        else
        {
        	return testCampaignsToSearch;
        }

        return testCampaignsMatching;
    }
    
    private List<TestCampaignDescription> filterByDate(TestCampaignDescription requestedTestCampaignDescription, List<TestCampaignDescription> testCampaignsToSearch)
    {
        List<TestCampaignDescription> testCampaignsMatching = new ArrayList<>();
        if (requestedTestCampaignDescription.getCampaignDate() != null)
        {
            for (TestCampaignDescription testCampaignToSearch : testCampaignsToSearch)
            {
                if (testCampaignToSearch.getCampaignDate().equals(requestedTestCampaignDescription.getCampaignDate()))
                {
                    testCampaignsMatching.add(testCampaignToSearch);
                }
            }
        }
        else
        {
        	return testCampaignsToSearch;
        }

        return testCampaignsMatching;
    }
    
    private List<TestCampaignDescription> filterByMap(TestCampaignDescription requestedTestCampaignDescription, List<TestCampaignDescription> testCampaignsToSearch)
    {
        List<TestCampaignDescription> testCampaignsMatching = new ArrayList<>();
        if (requestedTestCampaignDescription.getMap() != null)
        {
            for (TestCampaignDescription testCampaignToSearch : testCampaignsToSearch)
            {
                if (testCampaignToSearch.getMap() == requestedTestCampaignDescription.getMap())
                {
                    testCampaignsMatching.add(testCampaignToSearch);
                }
            }
        }
        else
        {
        	return testCampaignsToSearch;
        }

        return testCampaignsMatching;
    }

    
    private List<TestCampaignDescription> filterByRole(TestCampaignDescription requestedTestCampaignDescription, List<TestCampaignDescription> testCampaignsToSearch)
    {
        List<TestCampaignDescription> testCampaignsMatching = new ArrayList<>();
        if (requestedTestCampaignDescription.getRole() != null)
        {
            for (TestCampaignDescription testCampaignToSearch : testCampaignsToSearch)
            {
                if (testCampaignToSearch.getRole() == requestedTestCampaignDescription.getRole())
                {
                    testCampaignsMatching.add(testCampaignToSearch);
                }
            }
        }
        else
        {
        	return testCampaignsToSearch;
        }
        
        return testCampaignsMatching;
    }

}
