package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuAttackArea.AttackAreaType;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;

public class PointDefenseMcuSequence
{
    private static final int POINT_DEFENSE_COVER_TIME = 1200000;
    private static final int POINT_DEFENSE_COVER_AREA = 15000;
    
    private McuAttackArea attackArea;
    private McuTimer attackAreaTimer;
    private McuTimer forceCompleteTimer;
    private McuForceComplete forceComplete;
    
    private IFlight coveringFlight;
    
    public PointDefenseMcuSequence(IFlight coveringFlight)
    {
        this.coveringFlight = coveringFlight;
    }

    public void createPointDefenseSequence() throws PWCGException
    {
        createCover();
        createForceComplete();
        createTargetAssociations();
    }
    
    private void createCover() throws PWCGException
    {
        PlaneMcu leadPlane = coveringFlight.getFlightData().getFlightPlanes().getFlightLeader();
        Coordinate coverAreaPoint = coveringFlight.getFlightData().getFlightInformation().getTargetPosition();

        attackAreaTimer = new McuTimer();
        attackAreaTimer.setName("Attack Area Timer");
        attackAreaTimer.setDesc("Attack Area Timer");
        attackAreaTimer.setPosition(coverAreaPoint);
        attackAreaTimer.setTimer(1);

        attackArea = new McuAttackArea(AttackAreaType.AIR_TARGETS);
        attackArea.setName("Point Defense Attack Area");
        attackArea.setDesc("Point Defense Attack Area");
        attackArea.setAttackRadius(POINT_DEFENSE_COVER_AREA);      
        attackArea.setOrientation(new Orientation());       
        attackArea.setPosition(coverAreaPoint); 
        attackArea.setObject(leadPlane.getLinkTrId());
    }
    
    private void createForceComplete() throws PWCGException
    {
        PlaneMcu leadPlane = coveringFlight.getFlightData().getFlightPlanes().getFlightLeader();
        Coordinate rendevousPoint = coveringFlight.getFlightData().getFlightInformation().getTargetPosition();

        forceCompleteTimer = new McuTimer();
        forceCompleteTimer.setName("Point Defense Force Complete Timer");
        forceCompleteTimer.setDesc("Point Defense Force Complete Timer");
        forceCompleteTimer.setOrientation(new Orientation());
        forceCompleteTimer.setPosition(rendevousPoint);
        forceCompleteTimer.setTimer(POINT_DEFENSE_COVER_TIME);

        forceComplete = new McuForceComplete();
        forceComplete.setName("Point Defense Force Complete");
        forceComplete.setDesc("Point Defense Force Complete");
        forceComplete.setOrientation(new Orientation());
        forceComplete.setPosition(rendevousPoint);
        forceComplete.setObject(leadPlane.getEntity().getIndex());
    }
    
    private void createTargetAssociations()
    {
        attackAreaTimer.setTarget(attackArea.getIndex());
        attackAreaTimer.setTarget(forceCompleteTimer.getIndex());
        forceCompleteTimer.setTarget(forceComplete.getIndex());
    }
    
    public int getCoverEntry()
    {
        return attackAreaTimer.getIndex();
    }

    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        forceCompleteTimer.setTarget(nextTargetIndex);  
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        attackAreaTimer.write(writer);
        attackArea.write(writer);
        forceCompleteTimer.write(writer);
        forceComplete.write(writer);
    }

    public Coordinate getPosition()
    {
        return attackArea.getPosition().copy();
    }
}
