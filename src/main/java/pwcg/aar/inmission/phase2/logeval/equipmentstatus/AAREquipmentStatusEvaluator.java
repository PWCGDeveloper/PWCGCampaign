package pwcg.aar.inmission.phase2.logeval.equipmentstatus;

import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType3;

public class AAREquipmentStatusEvaluator
{
    private Campaign campaign;
    private LogEventData logEventData;
    private AARVehicleBuilder aarVehicleBuilder;

    public AAREquipmentStatusEvaluator(Campaign campaign, LogEventData logEventData, AARVehicleBuilder aarVehicleBuilder)
    {
        this.campaign = campaign;
        this.logEventData = logEventData;
        this.aarVehicleBuilder = aarVehicleBuilder;
    }

    public void determineFateOfPlanesInMission () throws PWCGException 
    {        
        for (LogPlane logPlane : aarVehicleBuilder.getLogPlanes().values())
        {
            IAType3 destroyedEventForPlane = logEventData.getDestroyedEvent(logPlane.getId());
            if (destroyedEventForPlane != null)
            {
                Airfield playerSquadronField = PWCGContext.getInstance().getCompanyManager().getCompany(logPlane.getSquadronId()).determineCurrentAirfieldAnyMap(campaign.getDate());
                if (playerSquadronField != null)
                {
                    EquipmentSurvivalCalculator equipmentSurvivalCalculator = new EquipmentSurvivalCalculator(destroyedEventForPlane.getLocation(), playerSquadronField);
                    if (equipmentSurvivalCalculator.isPlaneDestroyed())
                    {
                        logPlane.setPlaneStatus(TankStatus.STATUS_DESTROYED);
                    }
                }
            }
        }
    }
}
