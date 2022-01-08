package pwcg.campaign.crewmember;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.PictureManager;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.skin.Skin;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.utils.PWCGStringValidator;

public class CrewMember implements Cloneable
{
    protected String name = "";
    protected int serialNumber = SerialNumber.NO_SERIAL_NUMBER;
    protected String rank = "";

    protected String picName = "";

    protected int activeStatus = CrewMemberStatus.STATUS_ACTIVE;
    protected Country country = Country.NEUTRAL;
    protected int battlesFought = 0;
    protected AiSkillLevel aiSkillLevel = AiSkillLevel.NOVICE;
    protected int skill = AiSkillLevel.NOVICE.getAiSkillLevel();
    protected List<Skin> skins = new ArrayList<Skin>();
    protected List<Victory> airVictories = new ArrayList<Victory>();
    protected List<Victory> groundVictories = new ArrayList<Victory>();
    protected List<Medal> medals = new ArrayList<Medal>();
    protected int companyId = 0;
    protected Date inactiveDate;
    protected Date recoveryDate;

    public CrewMember()
    {
    }

    public CrewMember copy()
    {
        CrewMember clone = new CrewMember();

        clone.name = this.name;
        clone.rank = this.rank;
        clone.picName = this.picName;
        clone.activeStatus = this.activeStatus;
        clone.battlesFought = this.battlesFought;
        clone.aiSkillLevel = this.aiSkillLevel;
        clone.companyId = this.companyId;
        clone.serialNumber = this.serialNumber;

        clone.country = this.country;

        clone.skins = new ArrayList<Skin>();
        clone.skins.addAll(this.skins);

        clone.airVictories = new ArrayList<Victory>();
        clone.airVictories.addAll(this.airVictories);

        clone.groundVictories = new ArrayList<Victory>();
        clone.groundVictories.addAll(this.groundVictories);

        clone.medals = new ArrayList<Medal>();
        clone.medals.addAll(this.medals);
        clone.inactiveDate = new Date(this.inactiveDate.getTime());

        return clone;
    }

    public ImageIcon getCrewMemberPictureAsImageIcon()
    {

        ImageIcon imageIcon = null;
        try
        {
            String picPath = PictureManager.getPicturePath(this);

            imageIcon = ImageIconCache.getInstance().getImageIcon(picPath);
            if (imageIcon == null)
            {
                PWCGLogger.log(LogLevel.ERROR, "Got null image picture for " + getName() + " at " + picPath);
            }
        }
        catch (Exception ex)
        {
            PWCGLogger.logException(ex);
        }

        return imageIcon;
    }

    public BufferedImage getCrewMemberPictureAsBufferedImage()
    {

        BufferedImage bufferedImage = null;
        try
        {
            String picPath = PictureManager.getPicturePath(this);
            bufferedImage = ImageIconCache.getInstance().getBufferedImage(picPath);
            if (bufferedImage == null)
            {
                PWCGLogger.log(LogLevel.ERROR, "Got null image picture for " + getName() + " at " + picPath);
            }
        }
        catch (Exception ex)
        {
            PWCGLogger.logException(ex);
        }

        return bufferedImage;
    }

    public String skillAsString() throws PWCGException
    {
        if (aiSkillLevel == AiSkillLevel.NOVICE)
        {
            return "Novice";
        }
        else if (aiSkillLevel == AiSkillLevel.COMMON)
        {
            return "Competent";
        }
        else if (aiSkillLevel == AiSkillLevel.VETERAN)
        {
            return "Veteran";
        }
        else if (aiSkillLevel == AiSkillLevel.ACE)
        {
            return "Ace";
        }
        else if (aiSkillLevel == AiSkillLevel.PLAYER)
        {
            return "";
        }
        else
        {
            throw new PWCGException("Invalid skill level for crewMember " + name + ".  Skill = " + aiSkillLevel);
        }
    }

    public Map<String, Skin> determineSkinsByPlane()
    {
        Map<String, Skin> crewMemberSkinsByPlane = new HashMap<String, Skin>();

        for (Skin skin : skins)
        {
            crewMemberSkinsByPlane.put(skin.getPlane(), skin);
        }

        return crewMemberSkinsByPlane;
    }

    public void removeSkinForPlane(String planeName)
    {
        for (Skin skin : skins)
        {
            if (skin.getPlane().equals(planeName))
            {
                skins.remove(skin);
                break;
            }
        }
    }

    public void addSkin(Skin skin)
    {
        if (skin != null)
        {
            skins.add(skin);
        }
        else
        {

        }
    }

    public String determineRankAbbrev()
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        return rankObj.getRankAbbrev(rank);
    }

    public int determineRankPos(Date date) throws PWCGException
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        return rankObj.getRankPosByService(rank, this.determineService(date));
    }

    public boolean isCommander(Date date) throws PWCGException
    {
    	int rankPos = determineRankPos(date);
    	return (rankPos == 0);
    }

    public boolean isCrewMemberName(String searchName)
    {
        if (searchName == null || searchName.isEmpty())
        {
            return false;
        }
        
        String truncatedSearchName = truncateNameToGameMax(searchName);
        String truncatedName = truncateNameToGameMax(this.getName());
        String truncatedNameAndRank = truncateNameToGameMax(this.getNameAndRank());
                
        if (truncatedName.equalsIgnoreCase(truncatedSearchName)           || 
            truncatedNameAndRank.equalsIgnoreCase(truncatedSearchName))
        {
            return true;
        }

        return false;
    }
    
    private String truncateNameToGameMax(String originalName) 
    {
        if (originalName.length() > 22)
        {
            String truncatedName = originalName.substring(0,22);
            return truncatedName;
        }
        else
        {
            return originalName;
        }
    }

    public String determineSortKey(Date date) throws PWCGException
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPos = rankObj.getRankPosByService(rank, determineService(date));

        int numVictoriesKey = 999 - getCrewMemberVictories().getAirToAirVictoryCount();
        String victoriesString = String.format("%03d", numVictoriesKey);

        String sortKey = "" + rankPos + victoriesString + name;

        return sortKey;
    }

    public ArmedService determineService(Date date) throws PWCGException
    {
        ArmedService service = null;
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        Company squadron = squadronManager.getCompany(companyId);

        if (squadron != null)
        {
            service = squadron.determineServiceForSquadron(date);
        }
        else
        {
            HistoricalAce historicalAce = PWCGContext.getInstance().getAceManager().getHistoricalAceBySerialNumber(serialNumber);
            if (historicalAce != null)
            {
                Country aceCountry = historicalAce.getCountry();
                IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
                service = armedServiceManager.getPrimaryServiceForNation(aceCountry, date);
            }            
        }

        if (service == null)
        {
            throw new PWCGException("No service found. Squadron is " + companyId);
        }

        return service;
    }
    
    public boolean isHistoricalAce()
    {
        HistoricalAce historicalAce = PWCGContext.getInstance().getAceManager().getHistoricalAceBySerialNumber(serialNumber);
        if (historicalAce != null)
        {
            return true;
        }
        return false;
    }

    public ICountry determineCountry(Date date) throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        Company squadron = squadronManager.getCompany(companyId);
        if (squadron == null)
        {
            throw new PWCGException("No country found. Squadron is " + companyId);
        }

        return squadron.getCountry();
    }

    public Company determineSquadron() throws PWCGException
    {
        Company squadron = null;
        if (companyId > 0)
        {
            squadron = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
        }

        return squadron;
    }

    public boolean determineIsCrewMemberCommander() throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPos = rankObj.getRankPosByService(getRank(), determineService(campaign.getDate()));
        if (rankPos == 0)
        {
            return true;
        }

        return false;
    }

    public CrewMemberVictories getCrewMemberVictories() throws PWCGException
    {
        List<Victory> victories = new ArrayList<>();
        victories.addAll(this.airVictories);
        victories.addAll(this.groundVictories);
        return new CrewMemberVictories(victories);
    }

    public void setSquadronId(int squadronId)
    {
        this.companyId = squadronId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) throws PWCGException
    {
        if (PWCGStringValidator.isValidName(name))
        {
            this.name = name;
        }
        else
        {
            throw new PWCGException("Invalid name.  Name must be English alpha characters only: " + name);
        }
    }

    public String getPicName()
    {
        return picName;
    }

    public void setPicName(String picName)
    {
        this.picName = picName;
    }

    public int getCrewMemberActiveStatus()
    {
        return activeStatus;
    }

    public void setCrewMemberActiveStatus(int crewMemberActiveStatus, Date statusDate, Date updatedRecoveryDate)
    {
        if (isPlayer())
        {
            setPlayerCrewMemberActiveStatus(crewMemberActiveStatus, statusDate, updatedRecoveryDate);
        }
        else
        {
            setAiCrewMemberActiveStatus(crewMemberActiveStatus, statusDate, updatedRecoveryDate);
        }
    }

    private void setPlayerCrewMemberActiveStatus(int crewMemberActiveStatus, Date statusDate, Date updatedRecoveryDate)
    {
        this.activeStatus = crewMemberActiveStatus;
        if (crewMemberActiveStatus <= CrewMemberStatus.STATUS_CAPTURED)
        {
            recoveryDate = null;            
            inactiveDate = new Date(statusDate.getTime());
        }
        else if (crewMemberActiveStatus <= CrewMemberStatus.STATUS_WOUNDED)
        {
            recoveryDate = new Date(updatedRecoveryDate.getTime());            
            inactiveDate = null;            
        }
        else if (crewMemberActiveStatus <= CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            recoveryDate = new Date(recoveryDate.getTime());            
            inactiveDate = null;            
        }
        else if (crewMemberActiveStatus <= CrewMemberStatus.STATUS_TRANSFERRED)
        {
            crewMemberActiveStatus = CrewMemberStatus.STATUS_ACTIVE;
            recoveryDate = null;            
            inactiveDate = null;            
        }
        else if (crewMemberActiveStatus == CrewMemberStatus.STATUS_ACTIVE)
        {
            inactiveDate = null;            
            recoveryDate = null;            
        }
    }

    private void setAiCrewMemberActiveStatus(int crewMemberActiveStatus, Date statusDate, Date updatedRecoveryDate)
    {
        this.activeStatus = crewMemberActiveStatus;
        if (crewMemberActiveStatus <= CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            recoveryDate = null;            
            inactiveDate = new Date(statusDate.getTime());
        }
        else if (crewMemberActiveStatus <= CrewMemberStatus.STATUS_WOUNDED)
        {
            recoveryDate = new Date(updatedRecoveryDate.getTime());            
            inactiveDate = null;            
        }
        else if (crewMemberActiveStatus <= CrewMemberStatus.STATUS_TRANSFERRED)
        {
            recoveryDate = null;            
            inactiveDate = new Date(statusDate.getTime());
        }
        else if (crewMemberActiveStatus == CrewMemberStatus.STATUS_ACTIVE)
        {
            inactiveDate = null;            
            recoveryDate = null;            
        }
    }

    public boolean isPlayer()
    {
        boolean isPlayer = false;
        if (serialNumber != SerialNumber.NO_SERIAL_NUMBER)
        {
            if (SerialNumber.getSerialNumberClassification(serialNumber) == SerialNumberClassification.PLAYER)
            {
                isPlayer = true;
            }
        }
        
        return isPlayer;
    }

    public int getBattlesFought()
    {
        return battlesFought;
    }

    public void setBattlesFought(int batttlesFought)
    {
        this.battlesFought = batttlesFought;
    }

    public AiSkillLevel getAiSkillLevel()
    {
        if (aiSkillLevel == null)
        {
            aiSkillLevel = AiSkillLevel.createAiSkilLLevel(skill);
        }

        return aiSkillLevel;
    }

    public void setAiSkillLevel(AiSkillLevel skill)
    {
        this.aiSkillLevel = skill;
    }

    public int getSkill() throws PWCGException
    {
        return aiSkillLevel.getAiSkillLevel();
    }

    public void setSkill(int skill)
    {
        this.skill = skill;
        aiSkillLevel = AiSkillLevel.createAiSkilLLevel(skill);
    }

    public void addVictory(Victory victory)
    {
        airVictories.add(victory);
    }

    public void addGroundVictory(Victory victory)
    {
        groundVictories.add(victory);
    }

    public void setVictories(List<Victory> victories)
    {
        this.airVictories = victories;
    }

    public void setGroundVictories(List<Victory> victories)
    {
        this.groundVictories = victories;
    }

    public List<Medal> getMedals()
    {
        return medals;
    }

    public void addMedal(Medal medal)
    {
        medals.add(medal);
    }

    public void setMedals(List<Medal> medals)
    {
        this.medals = medals;
    }

    public String getRank()
    {
        return rank;
    }

    public void setRank(String rank)
    {
        this.rank = rank;
    }

    public List<Skin> getSkins()
    {
        return skins;
    }

    public void setSkins(List<Skin> skins)
    {
        this.skins = skins;
    }

    public Country getCountry()
    {
        return country;
    }

    public Side getSide()
    {
        ICountry icountry = CountryFactory.makeCountryByCountry(country);
        return icountry.getSide();
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    public int getCompanyId()
    {
        return companyId;
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public Date getInactiveDate()
    {
        return inactiveDate;
    }

    public void setInactiveDate(Date inactiveDate)
    {
        this.inactiveDate = inactiveDate;
    }

    public String getNameAndRank()
    {
        return determineRankAbbrev() + " " + name;
    }

    public Date getRecoveryDate()
    {
        return recoveryDate;
    }
}
