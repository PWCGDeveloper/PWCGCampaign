package pwcg.mission.io;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class MissionFileNameBuilderTest
{
    @Mock Campaign campaign;
    
    @Test
    public void testBuildFileName() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420901"));
        Mockito.when(campaign.getName()).thenReturn("My Campaign");
        String missionFileName = MissionFileNameBuilder.buildMissionFileName(campaign);
        Assertions.assertEquals("My Campaign 1942-09-01", missionFileName);
    }
    
    @Test
    public void testGetDateFromMissionFileName() throws PWCGException
    {
        Date missionDate = MissionFileNameBuilder.getDateFromMissionFileName("My Campaign 1942-09-01.mission");
        Assertions.assertEquals("19420901", DateUtils.getDateStringYYYYMMDD(missionDate));
    }
    
    @Test
    public void testGetCampaignNameFromMissionFileName() throws PWCGException
    {
        String campaignName = MissionFileNameBuilder.getCampaignNameFromMissionFileName("My Campaign 1942-09-01.mission");
        Assertions.assertEquals("My Campaign", campaignName);
    }
}
