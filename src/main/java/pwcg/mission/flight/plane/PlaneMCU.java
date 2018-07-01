package pwcg.mission.flight.plane;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogCategory;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuAttack;
import pwcg.mission.mcu.McuTREntity;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.group.PlaneRemover;

/**
 * Plane is an instance of a plane.  It derives from plane type and adds a crew, payload, fuel state, and
 * other elements specific to an instance of a plane
 * 
 * @author Patrick Wilson
 *
 */
public class PlaneMCU extends EquippedPlane implements Cloneable
{    
    protected String name = "";
    protected int index;
    protected int linkTrId;
    protected Coordinate position;
    protected Orientation orientation;
    protected Skin skin = null;
    protected AiSkillLevel aiLevel = AiSkillLevel.NOVICE;
    protected int coopStart = 0;
    protected int numberInFormation = 0;
    protected int vulnerable = 1;
    protected int engageable = 1;
    protected int limitAmmo = 1;
    protected int callsign = 0;
    protected int callnum = 0;
    protected int startInAir;
    protected int time = 60;
    protected double fuel = .6;
    protected int damageReport = 50;
    protected ICountry country = CountryFactory.makeNeutralCountry();
    protected int damageThreshold = 1;
    protected int aiRTBDecision = 1;
    protected int deleteAfterDeath = 1;

    protected PlaneRemover planeRemover = null;
    protected IPlanePayload payload = null;

    protected McuTREntity entity = new McuTREntity();

    protected McuTimer onSpawnTimer = new McuTimer();
    protected McuTimer attackTimer = new McuTimer();
    protected McuAttack attackEntity = new McuAttack();
    
    private SquadronMember pilot;
    
    public PlaneMCU(PlaneType planeType, ICountry country, SquadronMember pilot)
    {
        this.pilot = pilot;
        
        planeType.copyTemplate(this);
        this.setCountry(country);
        this.setName(pilot.getNameAndRank());
        this.setDesc(pilot.getNameAndRank());
        
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        startInAir = productSpecificConfiguration.startInAir();
    }

    public void initializeAttackEntity(int index)
    {

        onSpawnTimer.setName(pilot.getSerialNumber() + ": On Spawn Timer");       
        onSpawnTimer.setDesc(pilot.getSerialNumber() + ": On Spawn Timer");       
        onSpawnTimer.setPosition(position);  
        // offset the timers to allow some separation
        onSpawnTimer.setTimer(2 + index);

        attackTimer.setName(pilot.getSerialNumber() + ": Attack Timer");       
        attackTimer.setDesc("Attack Timer for " + pilot.getSerialNumber());
        attackTimer.setPosition(position);  
        attackTimer.setTarget(attackEntity.getIndex());
        attackTimer.setTimer(10);

        attackEntity.setName(pilot.getSerialNumber() + ": Attack Entity");       
        attackEntity.setDesc("Attack Entity for " + pilot.getSerialNumber());
        attackEntity.setPosition(position);  
        attackEntity.setPriority(WaypointPriority.PRIORITY_HIGH);

        // Chain this from the on spawn timer
        onSpawnTimer.setTarget(attackTimer.getIndex());
        attackTimer.setTarget(attackEntity.getIndex());

        // This plane is the object for attack
        attackEntity.setObject(getEntity().getIndex());
    }

    public void populateEntity(Flight flight, PlaneMCU flightLeader)
    {
        // Link this plane to the MCU
        this.linkTrId = entity.getIndex();

        // Position is same as plane
        entity.setPosition(position);
        entity.setOrientation(orientation);

        // Link this MCU back to the plane
        entity.setMisObjID(index);

        // All planes other than the flight leader have the flight
        // leader as a target
        if (flightLeader.getIndex() != index)
        {
            entity.setTarget(flightLeader.getEntity().getIndex());
        }
    }

    public void createPlaneRemover (Flight flight, PlaneMCU playerPlane) throws PWCGException 
    {
        planeRemover = new PlaneRemover();
        planeRemover.initialize(flight, this, playerPlane);
    }

    public boolean isPlayerPlane(Integer playerSerialNumber)
    {
        boolean isPlayerPlane = false;
        if (getAiLevel() == AiSkillLevel.PLAYER)
        {
            isPlayerPlane = true;
        }

        if (pilot.getSerialNumber() == playerSerialNumber)
        {
            isPlayerPlane = true;
        }

        return isPlayerPlane;
    }

    public IPlanePayload buildPlanePayload(Flight flight) throws PWCGException
    {
        IPayloadFactory payloadFactory = PWCGContextManager.getInstance().getPayloadFactory();
        payload = payloadFactory.createPlanePayload(this.getType());
        payload.createWeaponsPayload(flight);
                
        return payload.copy();
     }

    public void setPlanePayload(IPlanePayload payload) throws PWCGException
    {
        if (payload != null)
        {
            this.payload = payload.copy();
        }
    }

    public IPlanePayload getPlanePayload() throws PWCGException
    {
        if (payload != null)
        {
            return this.payload.copy();
        }
        
        return null;
    }

    public void setPlaneSkinWithCheck(Skin newSkin)
    {
        Campaign campaign =     PWCGContextManager.getInstance().getCampaign();
        Date campaignDate = campaign.getDate();

        if (!(campaignDate.before(newSkin.getStartDate())))
        {
            if (!(campaignDate.after(newSkin.getEndDate())))
            {
                if (newSkin.skinExists(Skin.PRODUCT_SKIN_DIR) || newSkin.isDefinedInGame())
                {
                    this.skin = newSkin;
                }
                else
                {
                    Logger.logByCategory(LogCategory.SKIN, "setPlaneSkin: skin rejected by skin exists");
                }
            }
            else
            {
                Logger.logByCategory(LogCategory.SKIN, "setPlaneSkin: skin rejected by end date: " + DateUtils.getDateStringDashDelimitedYYYYMMDD(newSkin.getEndDate()));
            }
        }
        else
        {
            Logger.logByCategory(LogCategory.SKIN, "setPlaneSkin: skin rejected by start date: " + DateUtils.getDateStringDashDelimitedYYYYMMDD(newSkin.getStartDate()));
        }
    }

    private void validateFuel()
    {
        if (fuel < .6)
        {
            fuel = .6;
        }

        if (fuel > 1.0)
        {
            fuel = 1.0;
        }
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            writer.write("Plane");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("  Name = \"" + name + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  LinkTrId = " + linkTrId + ";");
            writer.newLine();

            position.write(writer);
            orientation.write(writer);

            country.writeAdjusted(writer);

            String skinName = "";
            if (skin != null)
            {
                skinName = getType() + "\\" + skin.getSkinName();
                if (!skinName.contains(".dds"))
                {
                    skinName += ".dds";
                }
            }
            
            writer.write("  Skin = \"" + skinName + "\";");
            writer.newLine();
            writer.write("  AILevel = " + aiLevel.getAiSkillLevel() + ";");
            writer.newLine();
            writer.write("  CoopStart = " + coopStart + ";");
            writer.newLine();
            writer.write("  NumberInFormation = " + numberInFormation + ";");
            writer.newLine();
            writer.write("  Vulnerable = " + vulnerable + ";");
            writer.newLine();
            writer.write("  Engageable = " + engageable + ";");
            writer.newLine();
            writer.write("  LimitAmmo = " + limitAmmo + ";");
            writer.newLine();
            writer.write("  StartInAir = " + startInAir + ";");
            writer.newLine();
            writer.write("  Time = " + time + ";");
            writer.newLine();
            writer.write("  DamageReport = " + damageReport + ";");
            writer.newLine();
            writer.write("  DamageThreshold = " + damageThreshold + ";");
            writer.newLine();
            writer.write("  PayloadId = " + payload.getSelectedPayloadId() + ";");
            writer.newLine();
            writer.write("  AiRTBDecision = " + aiRTBDecision + ";");
            writer.newLine();
            writer.write("  DeleteAfterDeath = " + deleteAfterDeath + ";");
            writer.newLine();
            writer.write("  Fuel = " + fuel + ";");
            writer.newLine();
            
            // BoS specific parameters
            IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            if (productSpecificConfiguration.useCallSign())
            {
                writer.write("  Callsign = " + callsign + ";");
                writer.newLine();
                writer.write("  Callnum = " + callnum + ";");
                writer.newLine();
                writer.write("  WMMask = " + payload.generateFullModificationMask() + ";");
                writer.newLine();
            }
            

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();

            entity.write(writer);

            onSpawnTimer.write(writer);
            attackTimer.write(writer);
            attackEntity.write(writer);

            if (planeRemover != null)
            {
                planeRemover.write(writer);
            }
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
    

    /**
     * @return
     */
    public boolean getStartInAir()
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int startInAirVal = productSpecificConfiguration.startInAir();
        if (startInAir == startInAirVal)
        {
            return true;
        }
        
        return false;
    }

    public void addPlaneTarget(int targetPlaneIndex)
    {
        attackEntity.setTarget(targetPlaneIndex);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getLinkTrId()
    {
        return linkTrId;
    }

    public Coordinate getPosition()
    {
        return position;
    }

    public void setPosition(Coordinate position)
    {
        this.position = position;
        
        if (entity != null)
        {
            entity.setPosition(position);
        }
    }

    public Orientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
        
        if (entity != null)
        {
            entity.setOrientation(orientation);
        }
    }

    public Skin getPlaneSkin()
    {
        return skin;
    }

    public void setPlaneSkin(Skin newSkin)
    {
        if (newSkin != null)
        {
            this.skin = newSkin;
        }
    }

    public AiSkillLevel getAiLevel()
    {
        return aiLevel;
    }

    public void setAiLevel(AiSkillLevel aiLevel)
    {
        this.aiLevel = aiLevel;
    }

    public int getCoopStart()
    {
        return coopStart;
    }

    public void setCoopStart(int coopStart)
    {
        this.coopStart = coopStart;
    }

    public int getNumberInFormation()
    {
        return numberInFormation;
    }

    public void setNumberInFormation(int numberInFormation)
    {
        this.numberInFormation = numberInFormation;
    }

    public int getVulnerable()
    {
        return vulnerable;
    }

    public void setVulnerable(int vulnerable)
    {
        this.vulnerable = vulnerable;
    }

    public int getEngageable()
    {
        return engageable;
    }

    public void setEngageable(int engageable)
    {
        this.engageable = engageable;
    }

    public int getLimitAmmo()
    {
        return limitAmmo;
    }

    public void setLimitAmmo(int limitAmmo)
    {
        this.limitAmmo = limitAmmo;
    }

    public void setStartInAir(int startInAir)
    {
        this.startInAir = startInAir;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public int getDamageReport()
    {
        return damageReport;
    }

    public void setDamageReport(int damageReport)
    {
        this.damageReport = damageReport;
    }

    public int getDamageThreshold()
    {
        return damageThreshold;
    }

    public void setDamageThreshold(int damageThreshold)
    {
        this.damageThreshold = damageThreshold;
    }

    public McuTREntity getEntity()
    {
        return entity;
    }

    public void setEntity(McuTREntity entity)
    {
        this.entity = entity;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;        
        this.setSide(country.getSide());
    }

    public PlaneRemover getPlaneRemover()
    {
        return this.planeRemover;
    }

    public double getFuel()
    {
        return fuel;
    }

    public void setFuel(double fuel)
    {
        this.fuel = fuel;

        validateFuel();
    }

    public McuTimer getAttackTimer()
    {
        return attackTimer;
    }

    public McuTimer getOnSpawnTimer()
    {
        return this.onSpawnTimer;
    }

    public SquadronMember getPilot()
    {
        return pilot;
    }
    
    public void replacePilot(SquadronMember newPilot)
    {
        this.pilot = newPilot;
    }
}
