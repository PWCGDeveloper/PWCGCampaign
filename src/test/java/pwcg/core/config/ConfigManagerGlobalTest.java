package pwcg.core.config;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.context.PWCGDirectoryManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class ConfigManagerGlobalTest
{
    @Before
    public void setup() throws PWCGException
    {
    }
    
    @Test
    public void testGlobalConfigurationInitialize() throws PWCGException 
    {
    	ConfigManagerGlobal configManagerGlobal = ConfigManagerGlobal.getInstance();
    	Map<String, ConfigSet> globalConfigSets = configManagerGlobal.getDefaultCampaignConfigSets();
    	
        assertTrue (globalConfigSets.containsKey(ConfigSetKeys.ConfigSetGUI));
        assertTrue (globalConfigSets.containsKey(ConfigSetKeys.ConfigSetUserPref));
    }
    
    @Test
    public void testGlobalConfigurationChangeParam() throws PWCGException 
    {
        cleanUpUserDir();

    	ConfigManagerGlobal configManagerGlobal = ConfigManagerGlobal.getInstance();
    	configManagerGlobal.reset();
    	int originaMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        assertTrue (originaMusicVolume == 10);
    	
        configManagerGlobal.setParam(ConfigItemKeys.SoundVolumeKey, "3");
        configManagerGlobal.write();
    	int newMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        assertTrue (newMusicVolume == 3);

        configManagerGlobal.setParam(ConfigItemKeys.SoundVolumeKey, "3");
        configManagerGlobal.readConfig();;
    	int afterSaveMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        assertTrue (afterSaveMusicVolume == 3);
    }

    
    @Test
    public void testGlobalConfigurationResetTest() throws PWCGException 
    {
        cleanUpUserDir();

    	ConfigManagerGlobal configManagerGlobal = ConfigManagerGlobal.getInstance();
    	
        configManagerGlobal.setParam(ConfigItemKeys.SoundVolumeKey, "3");
        configManagerGlobal.write();
        
    	int newMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        assertTrue (newMusicVolume == 3);

    	configManagerGlobal.reset();
    	int originaMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        assertTrue (originaMusicVolume == 10);

        configManagerGlobal.setParam(ConfigItemKeys.SoundVolumeKey, "3");
        configManagerGlobal.readConfig();;
    	int afterSaveMusicVolume = configManagerGlobal.getIntConfigParam(ConfigItemKeys.SoundVolumeKey);
        assertTrue (afterSaveMusicVolume == 10);
    }

    private void cleanUpUserDir() throws PWCGException
    {
        String userConfigDirName = PWCGDirectoryManager.getInstance().getPwcgUserDir();
        FileUtils fileUtils = new FileUtils();
        fileUtils.deleteFilesInDirectory(userConfigDirName);
    }

}
