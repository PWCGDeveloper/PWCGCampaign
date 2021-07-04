package pwcg.aar.inmission.phase1.parse;

import pwcg.aar.AARFactory;
import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARLogSetValidator
{

    public void isLogSetValid(Campaign campaign, AARPreliminaryData preliminaryData) throws PWCGException
    {
        AARLogParser logParser = new AARLogParser(preliminaryData.getMissionLogFileSet());
        AARMissionLogRawData missionLogRawData = logParser.parseLogFilesForMission(campaign);
        AARLogEventData logEventData = missionLogRawData.getLogEventData();
        if (logEventData.getVehicles().isEmpty())
        {
            throw new PWCGException("Could not find any vehicle spawns in log set " + preliminaryData.getMissionLogFileSet().getLogFileName());
        }
        
        isReferencePilotInMission(campaign, preliminaryData, missionLogRawData);
    }

    private void isReferencePilotInMission(Campaign campaign, AARPreliminaryData preliminaryData, AARMissionLogRawData missionLogRawData) throws PWCGException
    {
        AARVehicleBuilder vehicleBuilder = AARFactory.makeAARVehicleBuilder(campaign, preliminaryData, missionLogRawData.getLogEventData());
        vehicleBuilder.buildVehicleListsByVehicleType(missionLogRawData.getLogEventData());
        
        boolean referencePlayerFound = false;
        for (LogPlane logPlane : vehicleBuilder.getLogPlanes().values())
        {
            if (logPlane.getPilotSerialNumber() == campaign.getReferencePlayer().getSerialNumber())
            {
                referencePlayerFound = true;
            }
        }
        
        if (!referencePlayerFound)
        {
            throw new PWCGException("Reference pilot is not in this mission.  Please change the reference pilot and rerun the AAR.  Incorrect reference pilot is " + campaign.getReferencePlayer().getNameAndRank());
        }
    }

}
