package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARPlayerStatusAdjusterTest
{
    @Mock private Campaign campaign;
    @Mock private ConfigManagerCampaign configManager;
    
    
    @Test
    public void testKiAToSeriousWoundFromAdjustment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);        
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryKey)).thenReturn(AARPlayerStatusAdjuster.MAX_INJURY_DEAD);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryAdjustKey)).thenReturn(1);
        
        LogPilot playerCrewMember = new LogPilot();
        playerCrewMember.setSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1);
        playerCrewMember.setStatus(SquadronMemberStatus.STATUS_KIA);
        
        AARPlayerStatusAdjuster statusAdjuster = new AARPlayerStatusAdjuster(campaign);
        statusAdjuster.adjustForPlayer(playerCrewMember);
        
        Assertions.assertEquals(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, playerCrewMember.getStatus());
    }
    
    @Test
    public void testKiAToWoundFromAdjustment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);        
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryKey)).thenReturn(AARPlayerStatusAdjuster.MAX_INJURY_DEAD);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryAdjustKey)).thenReturn(2);
        
        LogPilot playerCrewMember = new LogPilot();
        playerCrewMember.setSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1);
        playerCrewMember.setStatus(SquadronMemberStatus.STATUS_KIA);
        
        AARPlayerStatusAdjuster statusAdjuster = new AARPlayerStatusAdjuster(campaign);
        statusAdjuster.adjustForPlayer(playerCrewMember);
        
        Assertions.assertEquals(SquadronMemberStatus.STATUS_WOUNDED, playerCrewMember.getStatus());
    }
    
    
    @Test
    public void testKiAToSeriousWoundFromWithMaxWoundedAdjustment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);        
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryKey)).thenReturn(AARPlayerStatusAdjuster.MAX_INJURY_WOUNDED);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryAdjustKey)).thenReturn(1);
        
        LogPilot playerCrewMember = new LogPilot();
        playerCrewMember.setSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1);
        playerCrewMember.setStatus(SquadronMemberStatus.STATUS_KIA);
        
        AARPlayerStatusAdjuster statusAdjuster = new AARPlayerStatusAdjuster(campaign);
        statusAdjuster.adjustForPlayer(playerCrewMember);
        
        Assertions.assertEquals(SquadronMemberStatus.STATUS_WOUNDED, playerCrewMember.getStatus());
    }
    
    
    @Test
    public void testNoAdjustmentForAI() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);        
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryKey)).thenReturn(AARPlayerStatusAdjuster.MAX_INJURY_DEAD);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryAdjustKey)).thenReturn(1);
        
        LogPilot playerCrewMember = new LogPilot();
        playerCrewMember.setSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        playerCrewMember.setStatus(SquadronMemberStatus.STATUS_KIA);
        
        AARPlayerStatusAdjuster statusAdjuster = new AARPlayerStatusAdjuster(campaign);
        statusAdjuster.adjustForPlayer(playerCrewMember);
        
        Assertions.assertEquals(SquadronMemberStatus.STATUS_KIA, playerCrewMember.getStatus());
    }

}
