package pwcg.campaign.plane;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class PlaneType implements Cloneable
{
    public enum PlaneSize
    {
        PLANE_SIZE_SMALL, PLANE_SIZE_MEDIUM, PLANE_SIZE_LARGE;
    }

    public static final String BALLOON = "Balloon";

    protected String type = "";
    protected String displayName = "";
    protected String script = "";
    protected String model = "";
    protected String desc = "";
    protected int cruisingSpeed;
    protected int climbOutRate;
    protected int goodness = 50;
    protected double endurance = 2.0;
    protected boolean isFlyable = false;
    protected PlaneSize planeSize = PlaneSize.PLANE_SIZE_SMALL;
    protected ArrayList<Role> roles = new ArrayList<Role>();
    protected Date introduction;
    protected Date withdrawal;
    protected Side side = null;

    public PlaneType()
    {
        try
        {
            withdrawal = DateUtils.getEndOfWar();
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
    }

    public PlaneType copy()
    {
        PlaneType planeType = new PlaneType();
        copyTemplate(planeType);
        return planeType;
    }

    public PlaneType copyTemplate(PlaneType planeType)
    {
        planeType.type = this.type;
        planeType.displayName = this.displayName;
        planeType.script = this.script;
        planeType.model = this.model;
        planeType.desc = this.desc;

        planeType.cruisingSpeed = this.cruisingSpeed;
        planeType.climbOutRate = this.climbOutRate;
        planeType.goodness = this.goodness;
        planeType.endurance = this.endurance;

        planeType.isFlyable = this.isFlyable;
        planeType.planeSize = this.planeSize;

        planeType.roles = new ArrayList<Role>();
        planeType.roles.addAll(roles);

        planeType.introduction = this.introduction;
        planeType.withdrawal = this.withdrawal;

        planeType.side = this.side;

        return planeType;
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
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public void setRole(Role role) throws PWCGException
    {
        if (role == Role.ROLE_FIGHTER || 
            role == Role.ROLE_RECON || 
            role == Role.ROLE_ARTILLERY_SPOT || 
            role == Role.ROLE_ATTACK || 
            role == Role.ROLE_BOMB || 
            role == Role.ROLE_STRAT_BOMB || 
            role == Role.ROLE_SEA_PLANE || 
            role == Role.ROLE_SEA_PLANE_SMALL || 
            role == Role.ROLE_SEA_PLANE_LARGE || 
            role == Role.ROLE_DIVE_BOMB || 
            role == Role.ROLE_TRANSPORT)
        {
            roles.add(role);
        }
        else
        {
            String errorMsg = "Invalid aircraft role: " + role;
            throw new PWCGException(errorMsg);
        }
    }

    public boolean isRole(Role role)
    {
        if (roles.size() == 0)
        {
            Logger.log(LogLevel.ERROR, "No roles for: " + getType());
        }
        for (int i = 0; i < roles.size(); ++i)
        {
            Role squadRole = roles.get(i);
            if (squadRole == role)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isPrimaryRole(Role role)
    {
        if (roles.size() == 0)
        {
            Logger.log(LogLevel.ERROR, "No roles for: " + getType());
        }

        if (role == determinePrimaryRole())
        {
            return true;
        }

        return false;
    }

    public boolean isOtherRole(Role role)
    {
        if (roles.size() == 0)
        {
            Logger.log(LogLevel.ERROR, "No roles for: " + getType());
        }
        for (int i = 0; i < roles.size(); ++i)
        {
            Role squadRole = roles.get(i);
            if (!(squadRole == role))
            {
                return true;
            }
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

    public Role determinePrimaryRole()
    {
        return getRoles().get(0);
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

    public boolean isFlyable()
    {
        return isFlyable;
    }

    public void setFlyable(boolean isFlyable)
    {
        this.isFlyable = isFlyable;
    }

    public ArrayList<Role> getRoles()
    {
        return roles;
    }

    public double getEndurance()
    {
        return endurance;
    }

    public void setEndurance(double endurance)
    {
        this.endurance = endurance;
    }

    public int getGoodness()
    {
        return goodness;
    }

    public void setGoodness(int goodness)
    {
        this.goodness = goodness;
    }

    public int getClimbOutRate()
    {
        return climbOutRate;
    }

    public void setClimbOutRate(int climbOutRate)
    {
        this.climbOutRate = climbOutRate;
    }

    public PlaneSize getPlaneSize()
    {
        return planeSize;
    }

    public void setPlaneSize(PlaneSize planeSize)
    {
        this.planeSize = planeSize;
    }

    public Side getSide()
    {
        return side;
    }

    public void setSide(Side side)
    {
        this.side = side;
    }
}
