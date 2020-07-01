package pwcg.gui;

public enum ScreenIdentifier
{
    PwcgMainScreen("PwcgMainScreen", "PWCG Main Screen", "MainFullScreen.jpg"),
    CampaignGeneratorScreen("CampaignGeneratorScreen", "Campaign Generation Screen", "CampaignGenFullScreen.jpg"),
    CampaignDeleteScreen("CampaignDeleteScreen", "Campaign Delete Screen", "CampaignGenFullScreen.jpg"),
    CampaignNewPilotScreen("CampaignNewPilotScreen", "Campaign Add New Pilot Screen", "CampaignGenFullScreen.jpg"),
    
    PwcgSkinConfigurationAnalysisScreen("PwcgSkinConfigurationAnalysisScreen", "PWCG Skin Analysis Screen", "BrickWall.jpg"),
    PwcgPlanesOwnedConfigurationScreen("PwcgPlanesOwnedConfigurationScreen", "PWCG Planes Owned Config Screen", "BrickWall.jpg"),
    PwcgMusicConfigScreen("PwcgMusicConfigScreen", "PWCG Music Config Screen", "BrickWall.jpg"),
    CampaignHomeScreen("CampaignHomeScreen", "Campaign Home Screen", "BrickWall.jpg"),
    BriefingPilotSelectionScreen("BriefingPilotSelectionScreen", "Briefing Pilot Selection Screen", "BrickWall.jpg"),
    BriefingDescriptionScreen("BriefingDescriptionScreen", "Briefing Description Screen", "BrickWall.jpg"),
    MapScreens("MapScreens", "All Map Screens", "BrickWall.jpg"),
    DebriefMissionDescriptionScreen("DebriefMissionDescriptionScreen", "Debrief Mission Description Screen", "BrickWall.jpg"),

    CampaignPilotScreen("CampaignPilotScreen", "Campaign Pilot Screen", "PilotDeskTop.png"),

    PwcgCoopGlobalAdminScreen("PwcgCoopGlobalAdminScreen", "PWCG Global Coop Admin Screen", "TableTop.jpg"),
    PwcgSkinConfigurationAnalysisDisplayScreen("PwcgSkinConfigurationAnalysisDisplayScreen", "PWCG Skin Analysis Display Screen", "TableTop.jpg"),
    PwcgGlobalConfigurationScreen("PwcgGlobalConfigurationScreen", "PWCG Global Configuration Screen", "TableTop.jpg"),
    CampaignAdvancedConfigurationScreen("CampaignAdvancedConfigurationScreen", "Campaign Advanced Configuration Screen", "TableTop.jpg"),
    CampaignSimpleConfigurationScreen("CampaignSimpleConfigurationScreen", "Campaign Simple Configuration Screen", "TableTop.jpg"),
    CampaignEquipmentDepotScreen("CampaignEquipmentDepotScreen", "Campaign Equipment Depo Screen", "TableTop.jpg"),
    CampaignCoopAdminScreen("CampaignCoopAdminScreen", "Campaign Coop Admin Screen", "TableTop.jpg"),
    CampaignReferencePilotSelectorScreen("CampaignReferencePilotSelectorScreen", "Campaign Reference Pilot Selector Screen", "TableTop.jpg"),
    CampaignIntelligenceReportScreen("CampaignIntelligenceReportScreen", "Campaign Intelligence Report Screen", "TableTop.jpg"),
    CampaignJournalScreen("CampaignJournalScreen", "Campaign Journal Screen", "TableTop.jpg"),
    CampaignSquadronLogScreen("CampaignSquadronLogScreen", "Campaign Squadron Log Screen", "TableTop.jpg"),
    CampaignMedalScreen("CampaignMedalScreen", "Campaign Medal Screen", "TableTop.jpg"),    
    CampaignPilotLogScreen("CampaignPilotLogScreen", "Campaign Pilot Log Screen", "TableTop.jpg"),    
    CampaignSkinConfigurationScreen("CampaignSkinConfigurationScreen", "Campaign Skin Configuration Screen", "TableTop.jpg"),    
    CampaignLeaveScreen("CampaignLeaveScreen", "Campaign Leave Screen", "TableTop.jpg"),    
    CampaignTransferScreen("CampaignTransferScreen", "Campaign Transfer Screen", "TableTop.jpg"),  
    BriefingCoopPersonaChooser("BriefingCoopPersonaChooser", "Briefing Coop Persona Chooser Screen", "TableTop.jpg"),    
    AARInitiationScreen("AARInitiationScreen", "AAR Initiation Screen", "TableTop.jpg"),    
    AARReportMainPanel("AARReportMainPanel", "AAR Report Tab Screen", "TableTop.jpg"),    
    MissingSkinScreen("MissingSkinScreen", "Missing Skin Screen", "TableTop.jpg"),    
        
    CampaignPilotChalkboard("CampaignPilotChalkboard", "Campaign Pilot Chalkboard", "chalkboard.png"),
    CampaignEquipmentChalkboard("CampaignEquipmentChalkboard", "Campaign Equipment Chalkboard", "chalkboard.png"),
    PlagueBronzeBackground("PlagueBronzeBackground", "Campaign Home Pilot List Plaque", "PlagueBronzeBackground.png"),
    CampaignHomeSquadronPlaque("CampaignHomeSquadronPlaque", "Campaign Home Squadron Plaque", "PlagueBronze.png"),
    Document("Document", "Document", "document.png"),
    OpenMedalBox("OpenMedalBox", "Open Medal Box", "OpenMedalBox.png"),
    OpenPilotLog("OpenPilotLog", "Open Pilot Log", "OpenPilotLog.jpg"),
    OpenJournal("OpenJournal", "Open Pilot Journal", "OpenJournal.png"),
    OpenSquadronLog("OpenSquadronLog", "Open Squadron Log Book", "OpenJournal.png");
    
    
    
    
    private String screenKey;
    private String screenDescription;
    private String defaultImageName;

    ScreenIdentifier(String screenKey, String screenDescription, String defaultImageName)
    {
        this.screenKey = screenKey;
        this.screenDescription = screenDescription;
        this.defaultImageName = defaultImageName;
    }

    public String getScreenKey()
    {
        return screenKey;
    }

    public String getScreenDescription()
    {
        return screenDescription;
    }

    public String getDefaultImageName()
    {
        return defaultImageName;
    }
}
