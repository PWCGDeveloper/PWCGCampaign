package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.event.IAType12;

public abstract class LogAIEntity extends LogBase
{
    protected String id = "";
    protected String name;
    protected String vehicleType = "";
    protected PwcgRoleCategory roleCategory = PwcgRoleCategory.OTHER;
    protected ICountry country;

    public LogAIEntity(int sequenceNumber)
    {
        super(sequenceNumber);
    }

    public void initializeEntityFromEvent(IAType12 atype12) throws PWCGException
    {
        setId(atype12.getId());
        setCountry(atype12.getCountry());
        setName(atype12.getName());
        setVehicleType(atype12.getType());
        
        TankType plane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(atype12.getType());
        if (plane != null)
        {
            setRoleCategory(plane.determinePrimaryRoleCategory());
        }
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    public PwcgRoleCategory getRoleCategory()
    {
        return roleCategory;
    }

    public void setRoleCategory(PwcgRoleCategory roleCategory)
    {
        this.roleCategory = roleCategory;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}
