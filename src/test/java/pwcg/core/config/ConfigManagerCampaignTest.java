package pwcg.core.config;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.CampaignIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfigManagerCampaignTest
{
    private Campaign campaign;

    @Test
    public void testGlobalConfigurationInitialize() throws PWCGException 
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        CampaignIOJson.writeJson(campaign);
        
        String campaignConfigDir = campaign.getCampaignPathAutoCreateDirectory() + "config\\";
        ConfigManagerCampaign campaignConfigManager = new ConfigManagerCampaign(campaignConfigDir);
        campaignConfigManager.initializeDefault();
        
        Map<String, ConfigSet> defaultConfigSets = campaignConfigManager.getDefaultCampaignConfigSets();
        Map<String, ConfigSet> initialConfigSetsFromFiles = campaignConfigManager.readInitialConfigSets();

        Assertions.assertEquals(defaultConfigSets.size(), 14);
        Assertions.assertEquals(initialConfigSetsFromFiles.size(), 17);
                
        boolean allConfigsOk = verifyConfigSets(defaultConfigSets, initialConfigSetsFromFiles);
        
        Assertions.assertTrue(allConfigsOk);
    }
    
    private boolean verifyConfigSets(Map<String, ConfigSet> defaultConfigSets, Map<String, ConfigSet> initialConfigSetsFromFiles)
    {
        boolean allConfigsOk = true;
        
        for (String configSetId : defaultConfigSets.keySet())
        {
            ConfigSet defaultConfigSet = defaultConfigSets.get(configSetId);
            ConfigSet initialConfigSet = initialConfigSetsFromFiles.get(configSetId);
            boolean configSetOk =  verifyConfigSet(defaultConfigSet, initialConfigSet);
            if (!configSetOk)
            {
                allConfigsOk = false;
            }
        }
        
        return allConfigsOk;
    }

    private boolean verifyConfigSet(ConfigSet defaultConfigSet, ConfigSet initialConfigSet)
    {
        boolean configSetOk =  true;
        for (String defaultConfigName : defaultConfigSet.getConfigItemNames())
        {
            if (!initialConfigSet.hasConfigItem(defaultConfigName))
            {
                System.out.println("Missing from initial config set  " + defaultConfigSet.getConfigSetName() + " " + defaultConfigName);
                configSetOk = false;
            }
        }

        for (String initialConfigName : initialConfigSet.getConfigItemNames())
        {
            if (!defaultConfigSet.hasConfigItem(initialConfigName))
            {
                System.out.println("Missing from default config set " + defaultConfigSet.getConfigSetName() + " " + initialConfigName);
                configSetOk = false;
            }
        }
    
        return configSetOk;
    }
}
