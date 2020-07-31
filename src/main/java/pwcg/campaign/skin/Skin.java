package pwcg.campaign.skin;

import java.io.File;
import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;

public class Skin implements Cloneable
{
    public static final String PRODUCT_SKIN_DIR = PWCGContext.getInstance().getDirectoryManager().getSimulatorDataDir() + "graphics\\skins\\";
    
	private String skinName = "";
	private String planeType = "";
	private String[] archTypes = {};
	private Date startDate = DateUtils.getBeginningOfGame();
	private Date endDate = DateUtils.getEndOfWar();
    private int squadId = PERSONAL_SKIN;
    private String country = "";
    private String category = "";
    private boolean definedInGame = false;
    private boolean winter = false;
	
    public static int FACTORY_GENERIC = -2;
    public static int PERSONAL_SKIN = -1;
	
	public Skin() throws PWCGException 
	{
	}
	
	public Skin copy () 
	{
		Skin skin = null;
		
		try
		{
			skin = new Skin();
			
			skin.skinName = this.skinName;
			skin.planeType = this.planeType;
			skin.archTypes = this.archTypes;
			skin.startDate = this.startDate;
			skin.endDate = this.endDate;
			skin.squadId = this.squadId;
			skin.country = this.country;
			skin.category = this.category;
            skin.definedInGame = this.definedInGame;
            skin.winter = this.winter;
		}
		catch (Exception e)
		{
            PWCGLogger.logException(e);
		}
		
		return skin;
	}

    public boolean skinExists(String directory)
    {
        boolean exists = false;

        String skinNameForLookup = skinName;
        if (!skinNameForLookup.contains(".dds"))
        {
            skinNameForLookup = skinNameForLookup + ".dds";
        }

        String filename = directory + planeType + "\\" + skinNameForLookup;
        File file = new File(filename);
        if (file.exists())
        {
            exists = true;
        }
        else
        {
            exists = false;
        }

        return exists;
    }


	public String getSkinName() {
		return skinName;
	}

	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}

	public String getPlane() {
		return planeType;
	}

	public void setPlane(String plane) {
		this.planeType = plane;
	}

	public String[] getArchTypes() {
        return archTypes;
    }

    public void setArchTypes(String[] archTypes) {
        this.archTypes = archTypes;
    }

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date date) {
		this.startDate = date;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

    public int getSquadId()
    {
        return this.squadId;
    }

    public void setSquadId(int squadId)
    {
        this.squadId = squadId;
    }

    public String getCountry()
    {
        return this.country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public boolean isDefinedInGame()
    {
        return definedInGame;
    }

    public void setDefinedInGame(boolean definedInGame)
    {
        this.definedInGame = definedInGame;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public boolean isWinter()
    {
        return winter;
    }

    public void setWinter(boolean winter)
    {
        this.winter = winter;
    }
}
