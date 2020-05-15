package pwcg.mission.ambient;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.AAAManager;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class AmbientGroundUnitBuilder
{
    private Mission mission = null;
    private Campaign campaign = null;

    private List<IGroundUnitCollection> ambientTrains = new ArrayList<>();
    private List<IGroundUnitCollection> ambientTrucks = new ArrayList<>();
    private List<IGroundUnitCollection> AAA = new ArrayList<>();
    private List<IGroundUnitCollection> ambientBattles = new ArrayList<>();

    public AmbientGroundUnitBuilder (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }

    public void generateAmbientGroundUnits() throws PWCGException 
    {
        generateAmbientTrains();
        generateAmbientTrucks();        
        createFrontLineAAA();
        
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            generateAmbientBattles();
        }
    }

    private void generateAmbientTrains() throws PWCGException 
    {
        AmbientTrainBuilder ambientTrainBuilder = new AmbientTrainBuilder(campaign, mission);
        ambientTrains = ambientTrainBuilder.generateAmbientTrains();
    }

    private void generateAmbientTrucks() throws PWCGException 
    {
        AmbientTruckConvoyBuilder ambientTruckConvoyBuilder = new AmbientTruckConvoyBuilder(campaign, mission);
        ambientTrucks = ambientTruckConvoyBuilder.generateAmbientTrucks();
    }

    private void generateAmbientBattles() throws PWCGException 
    {
        AmbientBattleBuilder ambientBattleBuilder = new AmbientBattleBuilder(campaign, mission);
        ambientBattles = ambientBattleBuilder.generateAmbientBattles();
    }
    
    private void createFrontLineAAA() throws PWCGException 
    {
        AAAManager aaaManager = new AAAManager(campaign, mission);
        AAA = aaaManager.getAAAForMission();
    }


    public void write(BufferedWriter writer) throws PWCGException
    {
        for (IGroundUnitCollection ambientTrain : ambientTrains)
        {
            ambientTrain.write(writer);
        }

        for (IGroundUnitCollection ambientTruck : ambientTrucks)
        {
            ambientTruck.write(writer);
        }

        for (IGroundUnitCollection ambientBattle : ambientBattles)
        {
            ambientBattle.write(writer);
        }

        for (IGroundUnitCollection aaa : AAA)
        {
            aaa.write(writer);
        }
    }
    
    public List<IGroundUnitCollection> getAllAmbientGroundUnits()
    {
        List<IGroundUnitCollection> allAmbientGroundUnits = new ArrayList<>();
        allAmbientGroundUnits.addAll(ambientBattles);
        allAmbientGroundUnits.addAll(ambientTrucks);
        allAmbientGroundUnits.addAll(ambientTrains);
        allAmbientGroundUnits.addAll(AAA);
        return allAmbientGroundUnits;
    }

    public int getUnitCount()
    {
        int ambientTrainCount = 0;
        int ambientTruckCount = 0;
        int ambientAACount = 0;
        int ambientBattleCount = 0;
        for (IGroundUnitCollection groundUnitCollection : ambientTrains)
        {
            ambientTrainCount += groundUnitCollection.getUnitCount();
        }
        for (IGroundUnitCollection groundUnitCollection : ambientTrucks)
        {
            ambientTruckCount += groundUnitCollection.getUnitCount();
        }
        for (IGroundUnitCollection groundUnitCollection : AAA)
        {
            ambientAACount += groundUnitCollection.getUnitCount();
        }
        for (IGroundUnitCollection groundUnitCollection : ambientBattles)
        {
            ambientBattleCount += groundUnitCollection.getUnitCount();
        }

        System.out.println("Ambient unit count train : " + ambientTrainCount);
        System.out.println("Ambient unit count truck : " + ambientTruckCount);
        System.out.println("Ambient unit count AA : " + ambientAACount);
        System.out.println("Ambient unit count battle : " + ambientBattleCount);
        
        int ambientUnitCount = ambientTrainCount + ambientTruckCount + ambientAACount + ambientBattleCount;
        System.out.println("Ambient unit count total : " + ambientUnitCount);
        return ambientUnitCount;

    }
 }
