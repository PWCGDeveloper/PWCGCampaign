package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuCommandEffect;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.effect.Effect;
import pwcg.mission.mcu.effect.SmokeCity;
import pwcg.mission.mcu.effect.SmokeCitySmall;
import pwcg.mission.mcu.effect.SmokeVillage;

public class SmokeGroup
{
    private MissionBeginUnit missionBeginUnit;    
    private McuTimer smokeCheckZoneTimer = new McuTimer();    
    private McuCheckZone smokeStartCheckZone;    
    private McuTimer smokeStartTimer = new McuTimer();    
    private McuCommandEffect activateSmoke = new McuCommandEffect();
    private List<Effect> smokeEffects = new ArrayList<>();
    private List<Integer> triggerUnits = new ArrayList<>();
    
    private Coordinate position;

    public SmokeGroup(List<Integer> triggerUnits)
    {
        this.triggerUnits = triggerUnits;
    }

    public void buildSmokeGroup(Coordinate smokeEffectPosition, int smokeDirection, SmokeEffect requestedSmokeEffect) throws PWCGException 
    {
        this.position = smokeEffectPosition.copy();
        position.setYPos(0.0);
        missionBeginUnit = new MissionBeginUnit(position.copy());            

        addSmokeEffect(requestedSmokeEffect, smokeDirection);
        
        buildActivate();
        setTimers();
        setTargetAssociations();
        setObjectAssociations();
        setPositions();
        setNames();
    }

    private void addSmokeEffect(SmokeEffect requestedSmokeEffect, int smokeDirection) throws PWCGException
    {
        Effect smokeEffect = null;
        if (requestedSmokeEffect == SmokeEffect.SMOKE_CITY)
        {
            smokeEffect = new SmokeCity();
        }
        else  if (requestedSmokeEffect == SmokeEffect.SMOKE_CITY_SMALL)
        {
            smokeEffect = new SmokeCitySmall();
        }
        else
        {
            smokeEffect = new SmokeVillage();
        }
        
        smokeEffect.setPosition(position);
        smokeEffect.setOrientation(new Orientation(smokeDirection));
        
        smokeEffect.populateEntity();
        smokeEffects.add(smokeEffect);
    }
    
    private void setPositions()
    {
        smokeCheckZoneTimer.setPosition(position.copy());
        smokeStartCheckZone.setPosition(position.copy());
        smokeStartTimer.setPosition(position.copy());
        activateSmoke.setPosition(position.copy());
        for (Effect smokeEffect : smokeEffects)
        {
            smokeEffect.setPosition(position.copy());
        }
    }
    
    private void setNames()
    {
        smokeCheckZoneTimer.setName("smokeCheckZoneTimer");
        smokeStartCheckZone.setName("smokeStartCheckZone");
        smokeStartTimer.setName("smokeStartTimer");
        activateSmoke.setName("activateSmoke");

        for (Effect smokeEffect : smokeEffects)
        {
            smokeEffect.setName("smokeEffect");
        }
    }

    private void setTimers()
    {
        smokeCheckZoneTimer.setTime(1);
        smokeStartTimer.setTime(1);
    }

    private void setTargetAssociations()
    {        
        missionBeginUnit.linkToMissionBegin(smokeCheckZoneTimer.getIndex());
        smokeCheckZoneTimer.setTarget(smokeStartCheckZone.getIndex());
        smokeStartCheckZone.setTarget(smokeStartTimer.getIndex());
        smokeStartTimer.setTarget(activateSmoke.getIndex());
    }

    private void setObjectAssociations()
    {
        for (Effect smokeEffect : smokeEffects)
        {
            activateSmoke.setObject(smokeEffect.getLinkTrId());
        }
    }

    private void buildActivate() throws PWCGException
    {
        smokeStartCheckZone = new McuCheckZone("CheckZone Smoke Activate");
        smokeStartCheckZone.setCloser(1);
        smokeStartCheckZone.setZone(30000);
        smokeStartCheckZone.triggerCheckZoneByMultiplePlaneIds(triggerUnits);
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            missionBeginUnit.write(writer);
            smokeCheckZoneTimer.write(writer);
            smokeStartCheckZone.write(writer);
            smokeStartTimer.write(writer);
            activateSmoke.write(writer);

            for (Effect smokeEffect : smokeEffects)
            {
                smokeEffect.write(writer);
            }

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    public Coordinate getPosition()
    {
        return position;
    }

    public List<Effect> getSmokeEffects()
    {
        return smokeEffects;
    }

    public McuCheckZone getSmokeStartCheckZone()
    {
        return smokeStartCheckZone;
    }
    
    
}
