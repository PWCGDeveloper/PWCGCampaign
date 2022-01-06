package pwcg.campaign;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.MapFinderForCampaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.factory.CampaignModeFactory;
import pwcg.campaign.io.json.CampaignIOJson;
import pwcg.campaign.mode.ICampaignActive;
import pwcg.campaign.mode.ICampaignDescriptionBuilder;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.InitialCompanyBuilder;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.options.MapSeasonalParameters.Season;

public class Campaign
{
    private CampaignData campaignData = new CampaignData();
    private ConfigManagerCampaign campaignConfigManager = null;
    private CampaignLogs campaignLogs = null;

    private Mission currentMission = null;
    private CampaignPersonnelManager personnelManager;
    private CampaignEquipmentManager equipmentManager;
    private SquadronMoveEvent squadronMoveEvent;

    public Campaign()
    {
        personnelManager = new CampaignPersonnelManager(this);
        equipmentManager = new CampaignEquipmentManager(this);
        campaignLogs = new CampaignLogs();
    }

    public boolean open(String campaignName) throws PWCGException
    {
        campaignData.setName(campaignName);

        initializeCampaignConfigs();

        if (!readValidCampaign())
        {
            return false;
        }

        InitialCompanyBuilder initialSquadronBuilder = new InitialCompanyBuilder();
        initialSquadronBuilder.buildNewCompanies(this);

        verifyRepresentativePlayer();
        
        return true;
    }

    private void verifyRepresentativePlayer() throws PWCGException
    {
        if (campaignData.getReferencePlayerSerialNumber() == 0)
        {
            CrewMembers activePlayers = personnelManager.getAllActivePlayers();
            if (activePlayers.getCrewMemberList().size() > 0)
            {
                CrewMember referencePlayer = personnelManager.getAllActivePlayers().getCrewMemberList().get(0);
                campaignData.setReferencePlayerSerialNumber(referencePlayer.getSerialNumber());
            }
        }
    }

    public void write() throws PWCGException
    {
        if (TestDriver.getInstance().isEnabled())
        {
            if (!TestDriver.getInstance().isWriteCampaignFile())
            {
                return;
            }
        }
        
        CampaignDirectoryBuilder.initializeCampaignDirectories(this);
        CampaignIOJson.writeJson(this);
    }

    private boolean readValidCampaign()
    {
        try
        {
            CampaignIOJson.readJson(this);
            if (!isValidCampaignForProduct())
            {
                return false;
            }
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
            return false;
        }

        return true;
    }

    public void initializeCampaignConfigs() throws PWCGException
    {
        String campaignConfigDir = getCampaignPathAutoCreateDirectory() + "config\\";
        campaignConfigManager = new ConfigManagerCampaign(campaignConfigDir);
        campaignConfigManager.initialize();
    }

    public boolean isHumanSquadron(int squadronId)
    {
        CompanyPersonnel squadronPersonnel = personnelManager.getCompanyPersonnel(squadronId);
        return squadronPersonnel.isPlayerSquadron();
    }

    public String getCampaignDescription() throws PWCGException
    {
        ICampaignDescriptionBuilder campaignDescriptionBuilder = CampaignModeFactory.makeCampaignDescriptionBuilder(this);
        return campaignDescriptionBuilder.getCampaignDescription();
    }

    public static List<String> getCampaignNames() throws PWCGUserException
    {
        List<String> campaignList = new ArrayList<String>();
        String campaignRootDirName = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir();
        File campaignRootDir = new File(campaignRootDirName);

        if (!campaignRootDir.exists() || !campaignRootDir.isDirectory())
        {
            throw new PWCGUserException("Campaign directory does not exist: " + campaignRootDirName);
        }

        String[] campaignNames = campaignRootDir.list();
        if (campaignNames != null)
        {
            for (String campaignName : campaignNames)
            {
                String campaignDirPath = campaignRootDirName + "\\" + campaignName;

                File campaignDir = new File(campaignDirPath);
                if (campaignDir.isDirectory())
                {
                    String[] fileNames = campaignDir.list();
                    if (fileNames != null)
                    {
                        for (String fileName : fileNames)
                        {
                            if (fileName.equals("Campaign.json"))
                            {
                                campaignList.add(campaignName);
                            }
                        }
                    }
                }
            }
        }

        return campaignList;
    }

    public String getCampaignPathAutoCreateDirectory()
    {
        String campaignPath = getCampaignPath();
        File campaignDir = new File(campaignPath);
        if (!campaignDir.exists())
        {
            campaignDir.mkdir();
        }

        return campaignPath;
    }

    public String getCampaignPath()
    {
        String dir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir();
        String campaignPath = dir + campaignData.getName() + "\\";
        return campaignPath;
    }

    public boolean isLongRange() throws PWCGException
    {
        for (CrewMember player : this.personnelManager.getAllActivePlayers().getCrewMemberList())
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(player.getCompanyId());
            PwcgRoleCategory squadronPrimaryRoleCategory = squadron.determineSquadronPrimaryRoleCategory(this.getDate());
            if (squadronPrimaryRoleCategory == PwcgRoleCategory.FIGHTER)
            {
                return false;
            }
            
            if (squadronPrimaryRoleCategory == PwcgRoleCategory.ATTACK)
            {
                return false;
            }
        }

        return true;
    }

    public boolean isValidCampaignForProduct() throws PWCGException
    {
        Date campaignDate = campaignData.getDate();
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            if (campaignDate.after(DateUtils.getDateYYYYMMDD("19300101")))
            {
                return false;
            }

        }
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            if (campaignDate.before(DateUtils.getDateYYYYMMDD("19300101")))
            {
                return false;
            }

        }

        return true;
    }

    public boolean isCampaignActive() throws PWCGException
    {
        ICampaignActive campaignActive = CampaignModeFactory.makeCampaignActive(this);
        return campaignActive.isCampaignActive();
    }

    public boolean isCampaignCanOperate() throws PWCGException
    {
        if (personnelManager.getPlayersInMission().getCrewMemberList().size() > 0)
        {
            return true;
        }
        return false;
    }

    public List<Company> determinePlayerCompanies() throws PWCGException
    {
        List<Company> playerSquadrons = new ArrayList<>();
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        for (CrewMember player : personnelManager.getAllActivePlayers().getCrewMemberList())
        {
            Company playerSquadron = squadronManager.getCompany(player.getCompanyId());
            playerSquadrons.add(playerSquadron);
        }
        return playerSquadrons;
    }

    public boolean isCoop()
    {
        if (campaignData.getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            return false;
        }

        return true;
    }

    public boolean isInMemory()
    {
        File campaignPathFile = new File(getCampaignPath());
        if (!campaignPathFile.exists())
        {
            return true;
        }
        return false;
    }

    public Season getSeason()
    {
        return PWCGContext.getInstance().getCurrentMap().getMapClimate().getSeason(getDate());
    }

    public CrewMember findReferencePlayer() throws PWCGException
    {
        return personnelManager.getAnyCampaignMember(campaignData.getReferencePlayerSerialNumber());
    }

    public Company findReferenceSquadron() throws PWCGException
    {
        CrewMember referencePlayer = findReferencePlayer();
        Company referencePlayerSquadron = PWCGContext.getInstance().getCompanyManager().getCompany(referencePlayer.getCompanyId());
        return referencePlayerSquadron;
    }

    public String getName()
    {
        return campaignData.getName();
    }

    public Date getDate()
    {
        return campaignData.getDate();
    }

    public void setDate(Date date) throws PWCGException
    {
        campaignData.setDate(date);
    }

    public SerialNumber getSerialNumber()
    {
        return campaignData.getSerialNumber();
    }

    public CampaignData getCampaignData()
    {
        return campaignData;
    }

    public void setCampaignData(CampaignData campaignData)
    {
        this.campaignData = campaignData;
    }

    public ConfigManagerCampaign getCampaignConfigManager()
    {
        return campaignConfigManager;
    }

    public void setCampaignConfigManager(ConfigManagerCampaign campaignConfigManager)
    {
        this.campaignConfigManager = campaignConfigManager;
    }

    public CampaignLogs getCampaignLogs()
    {
        return campaignLogs;
    }

    public void setCampaignLogs(CampaignLogs campaignLog)
    {
        this.campaignLogs = campaignLog;
    }

    public Mission getCurrentMission()
    {
        return currentMission;
    }

    public void setCurrentMission(Mission currentMission)
    {
        this.currentMission = currentMission;
    }

    public CampaignPersonnelManager getPersonnelManager()
    {
        return personnelManager;
    }

    public SquadronMoveEvent getSquadronMoveEvent()
    {
        return squadronMoveEvent;
    }

    public CampaignEquipmentManager getEquipmentManager()
    {
        return equipmentManager;
    }

    public void setEquipmentManager(CampaignEquipmentManager equipmentManager)
    {
        this.equipmentManager = equipmentManager;
    }

    public void setPersonnelManager(CampaignPersonnelManager personnelManager)
    {
        this.personnelManager = personnelManager;
    }
    
    public FrontMapIdentifier getCampaignMap() throws PWCGException
    {
        FrontMapIdentifier mapIdentifier = MapFinderForCampaign.findMapForCampaign(this);
        return mapIdentifier;
    }

    public CrewMember getReferencePlayer() throws PWCGException
    {
        CrewMember referencePlayer = personnelManager.getAnyCampaignMember(campaignData.getReferencePlayerSerialNumber());
        if (referencePlayer == null)
        {
            throw new PWCGException("reference player not found on request for serial number" + campaignData.getReferencePlayerSerialNumber());
        }
        return referencePlayer;
    }
}
