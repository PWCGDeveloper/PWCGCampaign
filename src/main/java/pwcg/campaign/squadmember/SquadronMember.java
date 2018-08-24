package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.PictureManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.utils.StringValidity;

public class SquadronMember implements Cloneable
{

    public static String BAVARIA = "Bavaria";
    public static String WURTTEMBURG = "Wurttemberg";
    public static String SAXONY = "Saxony";
    public static String PRUSSIA = "Prussia";

    protected String name = "";
    protected int serialNumber = SerialNumber.NO_SERIAL_NUMBER;
    protected String rank = "";

    protected String picName = "";

    protected int pilotActiveStatus = SquadronMemberStatus.STATUS_ACTIVE;
    protected Country country = Country.NEUTRAL;
    protected int missionFlown = 0;
    protected int aggressiveness = 0;
    protected AiSkillLevel aiSkillLevel = AiSkillLevel.NOVICE;
    protected int skill = AiSkillLevel.NOVICE.getAiSkillLevel();
    protected int commonSense = 0;
    protected List<Skin> skins = new ArrayList<Skin>();
    protected List<Victory> victories = new ArrayList<Victory>();
    protected List<Victory> groundVictories = new ArrayList<Victory>();
    protected List<Medal> medals = new ArrayList<Medal>();
    protected String playerRegion = "";
    protected int squadronId = 0;
    protected Date inactiveDate;
    protected Date recoveryDate;

    public SquadronMember()
    {
    }

    public SquadronMember copy()
    {
        SquadronMember clone = new SquadronMember();

        clone.name = this.name;
        clone.rank = this.rank;
        clone.picName = this.picName;
        clone.pilotActiveStatus = this.pilotActiveStatus;
        clone.missionFlown = this.missionFlown;
        clone.aggressiveness = this.aggressiveness;
        clone.aiSkillLevel = this.aiSkillLevel;
        clone.commonSense = this.commonSense;
        clone.playerRegion = this.playerRegion;
        clone.squadronId = this.squadronId;
        clone.serialNumber = this.serialNumber;

        clone.country = this.country;

        clone.skins = new ArrayList<Skin>();
        clone.skins.addAll(this.skins);

        clone.victories = new ArrayList<Victory>();
        clone.victories.addAll(this.victories);

        clone.groundVictories = new ArrayList<Victory>();
        clone.groundVictories.addAll(this.groundVictories);

        clone.medals = new ArrayList<Medal>();
        clone.medals.addAll(this.medals);
        clone.inactiveDate = new Date(this.inactiveDate.getTime());

        return clone;
    }

    public ImageIcon determinePilotPicture()
    {

        ImageIcon imageIcon = null;
        try
        {
            String picPath = PictureManager.getPicturePath(this);

            imageIcon = ImageCache.getInstance().getImageIcon(picPath);
            if (imageIcon == null)
            {
                Logger.log(LogLevel.ERROR, "Got null image picture for " + getName() + " at " + picPath);
            }
        }
        catch (Exception ex)
        {
            Logger.logException(ex);
        }

        return imageIcon;
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
            throw new PWCGException("Invalid skill level for pilot " + name + ".  Skill = " + aiSkillLevel);
        }
    }

    public Map<String, Skin> determineSkinsByPlane()
    {
        Map<String, Skin> pilotSkinsByPlane = new HashMap<String, Skin>();

        for (Skin skin : skins)
        {
            pilotSkinsByPlane.put(skin.getPlane(), skin);
        }

        return pilotSkinsByPlane;
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

    public boolean isPilotName(String name)
    {
        if (getName().equalsIgnoreCase(name) || 
           (getRank() + " " + getName()).equalsIgnoreCase(name) || 
           (determineRankAbbrev() + " " + getName()).equalsIgnoreCase(name))
        {
            return true;
        }

        return false;
    }

    public String determineSortKey(Campaign campaign) throws PWCGException
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPos = rankObj.getRankPosByService(rank, campaign.getService());

        int numVictoriesKey = 999 - getSquadronMemberVictories().getAirToAirVictories();
        String victoriesString = String.format("%03d", numVictoriesKey);

        String sortKey = "" + rankPos + victoriesString + name;

        return sortKey;
    }

    public ArmedService determineService(Date date) throws PWCGException
    {
        ArmedService service = null;
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        Squadron squadron = squadronManager.getSquadron(squadronId);

        // Use the pilots squadron
        if (squadron != null)
        {
            service = squadron.determineServiceForSquadron(date);
        }
        else
        {
            throw new PWCGException("No service found. Squadron is " + squadronId);
        }

        return service;
    }

    public ICountry determineCountry(Date date) throws PWCGException
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        Squadron squadron = squadronManager.getSquadron(squadronId);

        if (squadron == null)
        {
            throw new PWCGException("No country found. Squadron is " + squadronId);
        }

        return squadron.getCountry();
    }

    public Squadron determineSquadron() throws PWCGException
    {
        Squadron squadron = null;
        if (squadronId > 0)
        {
            squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(squadronId);
        }

        return squadron;
    }

    public boolean determineIsSquadronMemberCommander() throws PWCGException
    {
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPos = rankObj.getRankPosByService(getRank(), determineService(campaign.getDate()));
        if (rankPos == 0)
        {
            return true;
        }

        return false;
    }

    public SquadronMemberVictories getSquadronMemberVictories()
    {
        List<Victory> victories = new ArrayList<>();
        victories.addAll(this.getVictories());
        victories.addAll(this.getGroundVictories());
        return new SquadronMemberVictories(victories);
    }

    public void setSquadronId(int squadronId)
    {
        this.squadronId = squadronId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) throws PWCGException
    {
        if (StringValidity.isAlpha(name))
        {
            this.name = name;
        }
        // Non English characters cause RoF and BoS to CTD
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

    public int getPilotActiveStatus()
    {
        return pilotActiveStatus;
    }

    public void setPilotActiveStatus(int pilotActiveStatus, Date statusDate, Date updatedRecoveryDate)
    {
        if (isPlayer())
        {
            setPlayerPilotActiveStatus(pilotActiveStatus, statusDate, updatedRecoveryDate);
        }
        else
        {
            setAiPilotActiveStatus(pilotActiveStatus, statusDate, updatedRecoveryDate);
        }
    }

    private void setPlayerPilotActiveStatus(int pilotActiveStatus, Date statusDate, Date updatedRecoveryDate)
    {
        this.pilotActiveStatus = pilotActiveStatus;
        if (pilotActiveStatus <= SquadronMemberStatus.STATUS_CAPTURED)
        {
            recoveryDate = null;            
            inactiveDate = new Date(statusDate.getTime());
        }
        else if (pilotActiveStatus <= SquadronMemberStatus.STATUS_WOUNDED)
        {
            recoveryDate = new Date(updatedRecoveryDate.getTime());            
            inactiveDate = null;            
        }
        else if (pilotActiveStatus <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            recoveryDate = new Date(recoveryDate.getTime());            
            inactiveDate = null;            
        }
        else if (pilotActiveStatus <= SquadronMemberStatus.STATUS_TRANSFERRED)
        {
            pilotActiveStatus = SquadronMemberStatus.STATUS_ACTIVE;
            recoveryDate = null;            
            inactiveDate = null;            
        }
        else if (pilotActiveStatus == SquadronMemberStatus.STATUS_ACTIVE)
        {
            inactiveDate = null;            
            recoveryDate = null;            
        }
    }

    private void setAiPilotActiveStatus(int pilotActiveStatus, Date statusDate, Date updatedRecoveryDate)
    {
        this.pilotActiveStatus = pilotActiveStatus;
        if (pilotActiveStatus <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            recoveryDate = null;            
            inactiveDate = new Date(statusDate.getTime());
        }
        else if (pilotActiveStatus <= SquadronMemberStatus.STATUS_WOUNDED)
        {
            recoveryDate = new Date(updatedRecoveryDate.getTime());            
            inactiveDate = null;            
        }
        else if (pilotActiveStatus <= SquadronMemberStatus.STATUS_TRANSFERRED)
        {
            recoveryDate = null;            
            inactiveDate = new Date(statusDate.getTime());
        }
        else if (pilotActiveStatus == SquadronMemberStatus.STATUS_ACTIVE)
        {
            inactiveDate = null;            
            recoveryDate = null;            
        }
    }

    public int getMissionFlown()
    {
        return missionFlown;
    }

    public void setMissionFlown(int missionFlown)
    {
        this.missionFlown = missionFlown;
    }

    public int getAggressiveness()
    {
        return aggressiveness;
    }

    public void setAggressiveness(int aggressiveness)
    {
        this.aggressiveness = aggressiveness;
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

    public int getCommonSense()
    {
        return commonSense;
    }

    public void setCommonSense(int commonSense)
    {
        this.commonSense = commonSense;
    }

    public List<Victory> getVictories()
    {
        return victories;
    }

    public List<Victory> getGroundVictories()
    {
        return groundVictories;
    }

    public void addVictory(Victory victory)
    {
        victories.add(victory);
    }

    public void addGroundVictory(Victory victory)
    {
        groundVictories.add(victory);
    }

    public void setVictories(List<Victory> victories)
    {
        this.victories = victories;
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

    public String getPlayerRegion()
    {
        return playerRegion;
    }

    public void setPlayerRegion(String playerRegion)
    {
        this.playerRegion = playerRegion;
    }

    public Country getCountry()
    {
        return country;
    }

    public void setCountry(Country country)
    {
        this.country = country;
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

    public int getSquadronId()
    {
        return squadronId;
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
