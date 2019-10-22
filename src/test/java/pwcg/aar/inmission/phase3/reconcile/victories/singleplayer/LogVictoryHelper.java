package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBalloon;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.Country;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.location.Coordinate;
import pwcg.product.fc.country.FCCountry;
import pwcg.testutils.SquadronTestProfile;

public class LogVictoryHelper
{
    private List<LogVictory> logVictories = new ArrayList<>();

    public void createPlaneVictory()
    {
        LogPlane victor = makeVictor();
        
        LogPlane victim = new LogPlane(1);
        victim.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new FCCountry(Country.GERMANY));
        victim.setSquadronId(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        victim.intializePilot(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);

        makeVictory(victor, victim);
    }

    public void createFuzzyPlaneVictory()
    {
        LogUnknown victor = new LogUnknown();
        
        LogPlane victim = new LogPlane(1);
        victim.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new FCCountry(Country.GERMANY));
        victim.setSquadronId(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        victim.intializePilot(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);

        makeVictory(victor, victim);
    }

    public void createBalloonVictory()
    {
        LogPlane victor = makeVictor();
        
        LogBalloon victim = new LogBalloon(10000);
        victim.setVehicleType("drachen");
        victim.setCountry(new FCCountry(Country.GERMANY));

        makeVictory(victor, victim);
    }

    public void createFuzzyBalloonVictory()
    {
        LogUnknown victor = new LogUnknown();
        
        LogBalloon victim = new LogBalloon(10000);
        victim.setVehicleType("drachen");
        victim.setCountry(new FCCountry(Country.GERMANY));

        makeVictory(victor, victim);
    }

    public void createGroundVictory()
    {
        LogPlane victor = makeVictor();
        
        LogGroundUnit victim = new LogGroundUnit(1000);
        victim.setVehicleType("tank");
        victim.setCountry(new FCCountry(Country.GERMANY));

        makeVictory(victor, victim);
    }

    public LogPlane makeVictor()
    {
        LogPlane victor = new LogPlane(1);
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setVehicleType("spad13");
        victor.setCountry(new FCCountry(Country.FRANCE));
        victor.setSquadronId(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        victor.intializePilot(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        return victor;
    }

    public void makeVictory(LogAIEntity victor, LogAIEntity victim)
    {
        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setLocation(new Coordinate(100.0, 0.0, 100.0));
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);
        logVictories.add(resultVictory);
    }

    public List<LogVictory> getLogVictories()
    {
        return logVictories;
    }
}
