package pwcg.dev.utils;

import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.ScreenIdentifierOverride;
import pwcg.gui.ScreenIdentifierOverrideManager;

public class ScreenOverridesFileBuilder
{
    private ScreenIdentifierOverrideManager screenIdentifierOverrideManager;

    public static void main(String[] args)
    {
        try
        {
            ScreenOverridesFileBuilder screenOverridesFileBuilder = new ScreenOverridesFileBuilder();
            screenOverridesFileBuilder.makeEmptyScreenOverrides();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void makeEmptyScreenOverrides() throws PWCGException
    {
        screenIdentifierOverrideManager = ScreenIdentifierOverrideManager.getInstance();
        
        for (ScreenIdentifier screenIdentifier : ScreenIdentifier.values()) 
        {
            addDefaultOverride(screenIdentifier);
        }
        
//        addDefaultOverride(ScreenIdentifier.PwcgMainScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignGeneratorScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignDeleteScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignNewCrewMemberScreen);
//        
//        addDefaultOverride(ScreenIdentifier.PwcgSkinConfigurationAnalysisScreen);
//        addDefaultOverride(ScreenIdentifier.PwcgPlanesOwnedConfigurationScreen);
//        addDefaultOverride(ScreenIdentifier.PwcgMusicConfigScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignHomeScreen);
//        addDefaultOverride(ScreenIdentifier.BriefingCrewMemberSelectionScreen);
//        addDefaultOverride(ScreenIdentifier.BriefingDescriptionScreen);
//        addDefaultOverride(ScreenIdentifier.MapScreens);
//        addDefaultOverride(ScreenIdentifier.DebriefMissionDescriptionScreen);
//
//        addDefaultOverride(ScreenIdentifier.PwcgCoopGlobalAdminScreen);
//        addDefaultOverride(ScreenIdentifier.PwcgSkinConfigurationAnalysisDisplayScreen);
//        addDefaultOverride(ScreenIdentifier.PwcgGlobalConfigurationScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignAdvancedConfigurationScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignSimpleConfigurationScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignEquipmentDepotScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignCoopAdminScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignReferenceCrewMemberSelectorScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignIntelligenceReportScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignJournalScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignSquadronLogScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignMedalScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignCrewMemberLogScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignSkinConfigurationScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignLeaveScreen);
//        addDefaultOverride(ScreenIdentifier.CampaignTransferScreen);
//        addDefaultOverride(ScreenIdentifier.BriefingCoopPersonaChooser);
//        addDefaultOverride(ScreenIdentifier.AARInitiationScreen);
//        addDefaultOverride(ScreenIdentifier.AARReportMainPanel);
//        addDefaultOverride(ScreenIdentifier.MissingSkinScreen);
//        
//        addDefaultOverride(ScreenIdentifier.CampaignCrewMemberChalkboard);
//        addDefaultOverride(ScreenIdentifier.CampaignEquipmentChalkboard);
//        addDefaultOverride(ScreenIdentifier.PlaqueBronzeBackground);
//        addDefaultOverride(ScreenIdentifier.CampaignHomeSquadronPlaque);
//        addDefaultOverride(ScreenIdentifier.Document);
//        addDefaultOverride(ScreenIdentifier.OpenMedalBox);
//        addDefaultOverride(ScreenIdentifier.OpenCrewMemberLog);
//        addDefaultOverride(ScreenIdentifier.OpenJournal);
//        addDefaultOverride(ScreenIdentifier.OpenSquadronLog);

        screenIdentifierOverrideManager.writeScreenIdentifierOverrides();
    }


    private void addDefaultOverride(ScreenIdentifier screenIdentifier)
    {
        ScreenIdentifierOverride screenIdentifierOverride = new ScreenIdentifierOverride(screenIdentifier);
        screenIdentifierOverride.setConfiguredImageName(screenIdentifier.getDefaultImageName());
        screenIdentifierOverrideManager.addOverride(screenIdentifierOverride);
    }
}
