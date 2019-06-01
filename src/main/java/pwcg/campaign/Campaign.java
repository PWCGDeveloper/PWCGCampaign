package pwcg.campaign;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.io.json.CampaignIOJson;
import pwcg.campaign.personnel.InitialSquadronBuilder;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;

public class Campaign
{
    private CampaignData campaignData = new CampaignData();
    private ConfigManagerCampaign campaignConfigManager = null;
    private CampaignLogs campaignLogs = null;

    private Mission currentMission = null;    
    private CampaignPersonnelManager personnelManager = null;
    private CampaignEquipmentManager equipmentManager = new CampaignEquipmentManager();
    private SquadronMoveEvent squadronMoveEvent = new SquadronMoveEvent();

    public Campaign() 
    {
        personnelManager = new CampaignPersonnelManager(this);
        campaignLogs = new CampaignLogs();
    }

    public boolean open(String campaignName) throws PWCGException 
    {
        campaignData.setName(campaignName);

        initializeCampaignDirectories();
        initializeCampaignConfigs();
        if (!readValidCampaign())
        {
            return false;
        }
        
        CampaignV5V6Converter converter = new CampaignV5V6Converter(this);
        converter.convert();

        InitialSquadronBuilder initialSquadronBuilder = new InitialSquadronBuilder();
        initialSquadronBuilder.buildNewSquadrons(this);

        return true;
    }

    public void write() throws PWCGException
    {
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
            Logger.logException(e);
            return false;
		}

		return true;
	}

	public void initializeCampaignDirectories() 
	{
        FileUtils fileUtils = new FileUtils();
		
        String campaignCombatReportsDir = getCampaignPath() + "CombatReports\\";
        fileUtils.createConfigDirIfNeeded(campaignCombatReportsDir);

        String campaignConfigDir = getCampaignPath() + "config\\";
        fileUtils.createConfigDirIfNeeded(campaignConfigDir);
		
        String campaignEquipmentDir = getCampaignPath() + "Equipment\\";
        fileUtils.createConfigDirIfNeeded(campaignEquipmentDir);
		
        String campaignMissionDataDir = getCampaignPath() + "MissionData\\";
        fileUtils.createConfigDirIfNeeded(campaignMissionDataDir);
		
        String campaignPersonnelDir = getCampaignPath() + "Personnel\\";
        fileUtils.createConfigDirIfNeeded(campaignPersonnelDir);
	}

    public void  initializeCampaignConfigs() throws PWCGException 
    {
        String campaignConfigDir = getCampaignPath() + "config\\";
        campaignConfigManager = new ConfigManagerCampaign(campaignConfigDir);
        campaignConfigManager.initialize();
    }

    public boolean isHumanSquadron(int squadronId)
    {
    	SquadronPersonnel squadronPersonnel = personnelManager.getSquadronPersonnel(squadronId);
    	return squadronPersonnel.isPlayerSquadron();
    }

    public Date getDate()
    {
        return campaignData.getDate();
    }

    public void setDate(Date date) throws PWCGException 
    {
    	campaignData.setDate(date);
    }

    public String getCampaignDescription() throws PWCGException
    {
        String campaignDescription = "";        

        if (this.campaignData.isCoop())
        {
            campaignDescription = "Coop Campaign";
        }
        else
        {
            SquadronMember referencePlayer = getReferenceCampaignMember();

            campaignDescription += referencePlayer.getNameAndRank();
            campaignDescription += "     " + DateUtils.getDateString(getDate());
            
            Squadron squadron =  PWCGContextManager.getInstance().getSquadronManager().getSquadron(referencePlayer.getSquadronId());
            campaignDescription += "     " + squadron.determineDisplayName(getDate());
            campaignDescription += "     " + squadron.determineCurrentAirfieldName(campaignData.getDate());
        }
        
        return campaignDescription;
    }

    public boolean useMovingFrontInCampaign() throws PWCGException
    {
        boolean useMovingFront = true;
        if (getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MovingFrontKey) == 0)
        {
            useMovingFront = false;
        }
        
        return useMovingFront;
    }

    public static List<String> getCampaignNames() throws PWCGUserException 
    {       
        List<String> campaignList = new ArrayList<String>();
        String campaignRootDirName = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir();
        File campaignRootDir = new File(campaignRootDirName);

        if (!campaignRootDir.exists() || !campaignRootDir.isDirectory())
        {
            throw new PWCGUserException ("Campaign directory does not exist: " + campaignRootDirName);
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
                            if(fileName.equals("Campaign.json")) 
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
	
	public String getCampaignPath()
	{
		String dir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir();
		String campaignPath = dir + campaignData.getName() + "\\"; 
		
		File campaignDir = new File(campaignPath); 
		if (!campaignDir.exists())
		{
			campaignDir.mkdir();
		}
		
		return campaignPath; 
	}

	public boolean isBattle() throws PWCGException
	{
        for (SquadronMember player : this.personnelManager.getAllPlayers().getSquadronMemberList())
        {
            Squadron squadron =  PWCGContextManager.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
		    if (PWCGContextManager.getInstance().getBattleManager().getBattleForCampaign(this.campaignData.getCampaignMap(), squadron.determineCurrentPosition(this.getDate()), this.getDate()) != null)
		    {
		        return true;
		    }
        }
        
	    return false;
	}
	
    public boolean isFighterCampaign() throws PWCGException 
    {
        for (SquadronMember player : this.personnelManager.getAllPlayers().getSquadronMemberList())
        {
            Squadron squadron =  PWCGContextManager.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
            if (squadron.isSquadronThisPrimaryRole(getDate(), Role.ROLE_FIGHTER))
            {
                return true;
            }
        }
        
        return false;
    }

	public boolean isValidCampaignForProduct() throws PWCGException 
	{
		Date campaignDate = campaignData.getDate();
        if (campaignDate.before(DateUtils.getDateYYYYMMDD("19300101")) && !PWCGContextManager.isRoF())
        {
            return false;
        }
        
        if (campaignDate.after(DateUtils.getDateYYYYMMDD("19300101")) && PWCGContextManager.isRoF())
        {
            return false;
        }
		
		return true;
	}

    public boolean isCampaignActive() throws PWCGException
    {
    	if (this.getCampaignData().isCoop())
    	{
    		return true;
    	}
    	
        for (SquadronMember player : this.personnelManager.getAllPlayers().getSquadronMemberList())
        {
            if (player.getPilotActiveStatus() > SquadronMemberStatus.STATUS_CAPTURED)
            {
                return true;
            }
        }
        
        return false;
    }

    public boolean isCampaignCanFly() throws PWCGException
    {
        for (SquadronMember player : this.personnelManager.getAllPlayers().getSquadronMemberList())
        {
            if (player.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
            {
                return true;
            }
        }
        return false;
    }

    public Side determineCampaignSide() throws PWCGException
    {
    	if (campaignData.isCoop())
    	{
    		int diceRoll = RandomNumberGenerator.getRandom(100);
    		if (diceRoll < 50)
    		{
    			return Side.ALLIED;
    		}
    		else
    		{
    			return Side.AXIS;
    		}
    	}
    	else
    	{
    		SquadronMember referencePlayer = getReferenceCampaignMember();
    		return referencePlayer.determineCountry(this.getDate()).getSide();
    	}
     }

    private SquadronMember getReferenceCampaignMember() throws PWCGException
    {
        List<SquadronMember> players = this.getPersonnelManager().getAllPlayers().getSquadronMemberList();
        int index = RandomNumberGenerator.getRandom(players.size());
        SquadronMember referencePlayer = players.get(index);
        return referencePlayer;
    }

    // TODo COOP Examine need for this method.  prefer not to do anything by reference
    public ICountry determineCampaignCountry() throws PWCGException
    {
        if (campaignData.isCoop())
        {
            return CountryFactory.makeNeutralCountry();
        }
        else
        {
            SquadronMember referencePlayer = getReferenceCampaignMember();
            return CountryFactory.makeCountryByCountry(referencePlayer.getCountry());
        }
     }

    public SerialNumber getSerialNumber()
    {
        return campaignData.getSerialNumber();
    }

    public void resetFerryMission()
    {
        this.squadronMoveEvent = new SquadronMoveEvent();
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

	public void setSquadronMoveEvent(SquadronMoveEvent squadronMoveEvent)
	{
		this.squadronMoveEvent = squadronMoveEvent;
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

    public List<Squadron> determinePlayerSquadrons() throws PWCGException
    {
        List<Squadron> playerSquadrons = new ArrayList<>();
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        for (SquadronMember player : personnelManager.getAllPlayers().getSquadronMemberList())
        {
            Squadron playerSquadron = squadronManager.getSquadron(player.getSquadronId());
            playerSquadrons.add(playerSquadron);
        }
        return playerSquadrons;
    }
}
