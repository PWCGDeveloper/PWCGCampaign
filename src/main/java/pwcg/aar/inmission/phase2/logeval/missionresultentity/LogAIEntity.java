package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.exception.PWCGException;

public abstract class LogAIEntity extends LogBase
{
    protected String id = "";
    protected String name;
    protected String vehicleType = "";
    protected PwcgRole role = PwcgRole.ROLE_UNKNOWN;
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
        
        PlaneType plane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(atype12.getType());
        if (plane != null)
        {
            PwcgRole approximateRole = PwcgRole.getApproximateRole(plane.determinePrimaryRole());
            setRole(approximateRole);
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

    public PwcgRole getRole()
    {
        return role;
    }

    public void setRole(PwcgRole role)
    {
        this.role = role;
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
