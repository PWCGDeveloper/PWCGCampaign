package pwcg.mission.flight.plane;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogCategory;
import pwcg.mission.flight.FlightStartPosition;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuTREntity;
import pwcg.mission.mcu.group.IPlaneRemover;
import pwcg.mission.mcu.group.WingmanMcuGroup;

/**
 * Plane is an instance of a plane. It derives from plane type and adds a crew,
 * payload, fuel state, and other elements specific to an instance of a plane
 * 
 * @author Patrick Wilson
 *
 */
public class PlaneMcu extends EquippedPlane implements Cloneable
{
    private String name = "";
    private int index;
    private int linkTrId;
    private Coordinate position;
    private Orientation orientation;
    private Skin skin = null;
    private AiSkillLevel aiSkillLevel = AiSkillLevel.NOVICE;
    private int coopStart = 0;
    private int numberInFormation = 1;
    private int vulnerable = 1;
    private int engageable = 1;
    private int limitAmmo = 1;
    private Callsign callsign = Callsign.NONE;
    private int callnum = 0;
    private int startInAir;
    private int time = 60;
    private double fuel = .6;
    private int damageReport = 50;
    private ICountry country = CountryFactory.makeNeutralCountry();
    private int damageThreshold = 1;
    private int aiRTBDecision = 1;
    private int deleteAfterDeath = 1;
    private WingmanMcuGroup wingmanCommands;

    private IPlanePayload payload = null;

    private McuTREntity entity = new McuTREntity();

    private Campaign campaign;
    private SquadronMember pilot;

    public PlaneMcu()
    {
    }

    public PlaneMcu copy()
    {
        PlaneMcu plane = new PlaneMcu();
        copyTemplate(plane);
        return plane;
    }

    private void copyTemplate(PlaneMcu plane)
    {
        super.copyTemplate(plane);

        plane.name = this.name;
        plane.index = this.index;
        plane.linkTrId = this.linkTrId;
        plane.position = this.position;
        plane.orientation = this.orientation;
        plane.skin = this.skin;
        plane.aiSkillLevel = this.aiSkillLevel;
        plane.coopStart = this.coopStart;
        plane.numberInFormation = this.numberInFormation;
        plane.vulnerable = this.vulnerable;
        plane.engageable = this.engageable;
        plane.limitAmmo = this.limitAmmo;
        plane.callsign = this.callsign;
        plane.callnum = this.callnum;
        plane.startInAir = this.startInAir;
        plane.time = this.time;
        plane.fuel = this.fuel;
        plane.damageReport = this.damageReport;
        plane.country = CountryFactory.makeCountryByCountry(this.country.getCountry());
        plane.damageThreshold = this.damageThreshold;
        plane.aiRTBDecision = this.aiRTBDecision;
        plane.deleteAfterDeath = this.deleteAfterDeath;
        plane.wingmanCommands = this.wingmanCommands;
        plane.payload = this.payload;
        plane.entity = this.entity.copy();
        plane.campaign = this.campaign;
        plane.pilot = this.pilot;
    }

    public PlaneMcu(Campaign campaign, EquippedPlane equippedPlane, ICountry country, SquadronMember pilot)
    {
        this.campaign = campaign;
        this.pilot = pilot;

        equippedPlane.copyTemplate(this);
        this.setCountry(country);
        this.setName(pilot.getNameAndRank());
        this.setDesc(pilot.getNameAndRank());

        startInAir = FlightStartPosition.START_IN_AIR;
    }

    public void populateEntity(IFlight flight, PlaneMcu flightLeader)
    {
        this.linkTrId = entity.getIndex();

        entity.setOrientation(orientation);
        entity.setMisObjID(index);

        if (flightLeader.getIndex() != index)
        {
            entity.setTarget(flightLeader.getEntity().getIndex());
        }
    }

    public boolean isActivePlayerPlane() throws PWCGException
    {
        boolean isPlayerPlane = false;
        if (getAiLevel() == AiSkillLevel.PLAYER)
        {
            isPlayerPlane = true;
        }

        SquadronMembers squadronMembers = campaign.getPersonnelManager().getAllActivePlayers();
        if (squadronMembers.isSquadronMember(serialNumber))
        {
            isPlayerPlane = true;
        }

        return isPlayerPlane;
    }

    public IPlanePayload buildPlanePayload(IFlight flight) throws PWCGException
    {
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        payload = payloadFactory.createPlanePayload(this.getType());
        payload.createWeaponsPayload(flight);
        return payload.copy();
    }

    public IPlanePayload buildStandardPlanePayload() throws PWCGException
    {
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        payload = payloadFactory.createPlanePayload(this.getType());
        payload.createStandardWeaponsPayload();
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
        Campaign campaign = PWCGContext.getInstance().getCampaign();
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
                    PWCGLogger.logByCategory(LogCategory.SKIN, "setPlaneSkin: skin rejected by skin exists");
                }
            }
            else
            {
                PWCGLogger.logByCategory(LogCategory.SKIN,
                        "setPlaneSkin: skin rejected by end date: " + DateUtils.getDateStringDashDelimitedYYYYMMDD(newSkin.getEndDate()));
            }
        }
        else
        {
            PWCGLogger.logByCategory(LogCategory.SKIN,
                    "setPlaneSkin: skin rejected by start date: " + DateUtils.getDateStringDashDelimitedYYYYMMDD(newSkin.getStartDate()));
        }
    }

    private void validateFuel()
    {
        if (fuel < .3)
        {
            fuel = .3;
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

            writer.write("  Name = \"\u0001" + name + "\";");
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
                skinName = getType() + "/" + skin.getSkinName();
                skinName = skinName.toLowerCase();
                if (!skinName.contains(".dds"))
                {
                    skinName += ".dds";
                }
            }

            writer.write("  Skin = \"" + skinName + "\";");
            writer.newLine();
            writer.write("  AILevel = " + aiSkillLevel.getAiSkillLevel() + ";");
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
                writer.write("  Callsign = " + callsign.getNum(country.getCountry()) + ";");
                writer.newLine();
                writer.write("  Callnum = " + callnum + ";");
                writer.newLine();
            }

            writer.write("  WMMask = " + payload.generateFullModificationMask() + ";");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();

            entity.write(writer);

            if (wingmanCommands != null)
            {
                wingmanCommands.write(writer);
            }
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public boolean getStartInAir()
    {
        int startInAirVal = FlightStartPosition.START_IN_AIR;
        if (startInAir == startInAirVal)
        {
            return true;
        }

        return false;
    }

    public void setWingman(WingmanMcuGroup wingmanCommands)
    {
        this.wingmanCommands = wingmanCommands;
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
        return position.copy();
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
        return aiSkillLevel;
    }

    public void setAiLevel(AiSkillLevel aiLevel)
    {
        this.aiSkillLevel = aiLevel;
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

    public double getFuel()
    {
        return fuel;
    }

    public void setFuel(double fuel)
    {
        this.fuel = fuel;

        validateFuel();
    }

    public SquadronMember getPilot()
    {
        return pilot;
    }

    public void replacePilot(SquadronMember newPilot)
    {
        this.pilot = newPilot;
    }

    public Callsign getCallsign()
    {
        return callsign;
    }

    public void setCallsign(Callsign callsign)
    {
        this.callsign = callsign;
    }

    public int getCallnum()
    {
        return callnum;
    }

    public void setCallnum(int callnum)
    {
        this.callnum = callnum;
    }

    public void setLinkTrId(int linkTrId)
    {
        this.linkTrId = linkTrId;
    }

    public IPlaneRemover getPlaneRemover()
    {
        // Removed because it deletes planes accidentally
        return null;
    }

    public int getAiRTBDecision()
    {
        return aiRTBDecision;
    }

    public void setAiRTBDecision(int aiRTBDecision)
    {
        this.aiRTBDecision = aiRTBDecision;
    }

    public Skin getSkin()
    {
        return skin;
    }
}
