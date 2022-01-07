package pwcg.campaign.tank;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.skin.TacticalCodeColor;
import pwcg.campaign.tank.payload.TankPayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class TankType implements Cloneable
{
    public enum PlaneSize
    {
        PLANE_SIZE_SMALL, PLANE_SIZE_MEDIUM, PLANE_SIZE_LARGE;
    }

    public static final String BALLOON = "Balloon";

    protected String type = "";
    protected String archType = "";
    protected String displayName = "";
    protected String script = "";
    protected String model = "";
    protected String desc = "";
    protected int cruisingSpeed;
    protected int goodness = 50;
    protected ArrayList<PwcgRoleCategory> roleCategories = new ArrayList<>();
    protected Date introduction;
    protected Date withdrawal;
    protected Date endProduction;;
    protected Side side = null;
    protected List<Country> primaryUsedBy = new ArrayList<>();
    protected List<TankPayloadElement> stockModifications = new ArrayList<>();
    protected TacticalCodeColor tacticalCodeColor = TacticalCodeColor.BLACK;


    public TankType()
    {
    }

    public TankType copy()
    {
        TankType planeType = new TankType();
        copyTemplate(planeType);
        return planeType;
    }

    public void copyTemplate(TankType planeType)
    {
        planeType.type = this.type;
        planeType.archType = this.archType;
        planeType.displayName = this.displayName;
        planeType.script = this.script;
        planeType.model = this.model;
        planeType.desc = this.desc;

        planeType.cruisingSpeed = this.cruisingSpeed;
        planeType.goodness = this.goodness;

        planeType.roleCategories = new ArrayList<>();
        planeType.roleCategories.addAll(roleCategories);

        planeType.introduction = this.introduction;
        planeType.withdrawal = this.withdrawal;
        planeType.endProduction = this.endProduction;

        planeType.side = this.side;
        planeType.primaryUsedBy = new ArrayList<>(this.primaryUsedBy);
        planeType.tacticalCodeColor = this.tacticalCodeColor;
    }

    public int getCruisingSpeed()
    {
        return cruisingSpeed;
    }

    public void setCruisingSpeed(int cruisingSpeed)
    {
        this.cruisingSpeed = cruisingSpeed;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getArchType()
    {
        return archType;
    }

    public void setArchType(String archType)
    {
        this.archType = archType;
    }

    public void setScript(String script)
    {
        this.script = script;
    }

    public void setScriptAndModelWithPath(String name)
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (productSpecificConfiguration.usePlaneDir())
        {
            script = "LuaScripts\\WorldObjects\\Planes\\" + name + ".txt";
        }
        else
        {
            script = "LuaScripts\\WorldObjects\\" + name + ".txt";
        }

        model = "graphics\\planes\\" + name + "\\" + name + ".mgm";
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("  Script = \"" + script + "\";");
            writer.newLine();
            writer.write("  Model = \"" + model + "\";");
            writer.newLine();
            writer.write("  Desc = \"" + desc + "\";");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    public boolean isRoleCategory(PwcgRoleCategory roleCategoryToFind)
    {
        if (roleCategories.size() == 0)
        {
            PWCGLogger.log(LogLevel.ERROR, "No roles for: " + getType());
        }
        
        for (PwcgRoleCategory roleCategory : roleCategories)
        {
            if (roleCategory == roleCategoryToFind)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isPrimaryRole(PwcgRole role)
    {
        if (roleCategories.size() == 0)
        {
            PWCGLogger.log(LogLevel.ERROR, "No roles for: " + getType());
        }

        if (role.getRoleCategory() == determinePrimaryRoleCategory())
        {
            return true;
        }

        return false;
    }

    public boolean isPlaneActive(Date date)
    {
        if (getIntroduction().before(date))
        {
            if (getWithdrawal().after(date))
            {
                return true;
            }
        }

        return false;
    }
    
    public boolean isStockModification(TankPayloadElement modification)
    {
        for (TankPayloadElement stockModification : getStockModifications())
        {
            if (stockModification == modification)
            {
                return true;
            }
        }
        return false;
    }

    public PwcgRoleCategory determinePrimaryRoleCategory()
    {
        return getRoleCategories().get(0);
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getScript()
    {
        return script;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public Date getWithdrawal()
    {
        return this.withdrawal;
    }

    public void setWithdrawal(Date withdrawal)
    {
        this.withdrawal = withdrawal;
    }

    public Date getEndProduction()
    {
        return endProduction;
    }

    public void setEndProduction(Date endProduction)
    {
        this.endProduction = endProduction;
    }

    public Date getIntroduction()
    {
        return this.introduction;
    }

    public void setIntroduction(Date introduction)
    {
        this.introduction = introduction;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public ArrayList<PwcgRoleCategory> getRoleCategories()
    {
        return roleCategories;
    }

    public int getGoodness()
    {
        return goodness;
    }

    public void setGoodness(int goodness)
    {
        this.goodness = goodness;
    }

    public Side getSide()
    {
        return side;
    }

    public void setSide(Side side)
    {
        this.side = side;
    }

    public boolean isUsedBy(ICountry country)
    {
        for (Country countryEnum: primaryUsedBy) 
        {
            if (countryEnum == country.getCountry())
            {
                return true;
            }
        }

        return false;
    }

    public List<TankPayloadElement> getStockModifications()
    {
        return stockModifications;
    }

    public List<Country> getPrimaryUsedBy()
    {
        return primaryUsedBy;
    }

    public TacticalCodeColor getTacticalCodeColor()
    {
        return tacticalCodeColor;
    }
}
