package pwcg.dev.utils;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.Country;
import pwcg.campaign.io.json.VehicleDefinitionIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class VehicleFileBuilder
{
    
    private static final List<VehicleDefinition> testTruck = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            try
            {
                List<Country> axis = new ArrayList<>();
                axis.add(Country.GERMANY);

                List<Country> russia = new ArrayList<>();
                russia.add(Country.RUSSIA);

                List<Country> all = new ArrayList<>();
                all.add(Country.GERMANY);
                all.add(Country.RUSSIA);
                all.add(Country.USA);
                all.add(Country.BRITAIN);

                List<Country> allied = new ArrayList<>();
                allied.add(Country.RUSSIA);
                allied.add(Country.USA);
                allied.add(Country.BRITAIN);

                List<Country> western = new ArrayList<>();
                western.add(Country.USA);
                western.add(Country.BRITAIN);

                add(new VehicleDefinition("vehicles\\", "vehicles\\ford\\", "ford-g917", "truck", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 50, VehicleClass.Truck));
                add(new VehicleDefinition("vehicles\\", "vehicles\\opel\\", "opel-blitz", "truck", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 100, VehicleClass.Truck));
                add(new VehicleDefinition("vehicles\\", "vehicles\\sdkfz251\\", "sdkfz251-1c", "half track", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.Truck));
                add(new VehicleDefinition("vehicles\\", "vehicles\\sdkfz251\\", "sdkfz251-szf", "half track", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 5, VehicleClass.Truck));
                add(new VehicleDefinition("vehicles\\", "vehicles\\gaz\\", "gaz-aa", "truck", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 100, VehicleClass.Truck));
                add(new VehicleDefinition("vehicles\\", "vehicles\\zis\\", "zis5", "truck", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 100, VehicleClass.Truck));
                add(new VehicleDefinition("vehicles\\", "vehicles\\zis\\", "bm13", "truck", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 5, VehicleClass.Truck));                
                add(new VehicleDefinition("vehicles\\", "vehicles\\gmc-cckw\\", "gmc-cckw", "truck", western, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 100, VehicleClass.Truck));                
                
                add(new VehicleDefinition("vehicles\\", "vehicles\\sdkfz10-flak38\\", "sdkfz10-flak38", "half track", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.TruckAAA));
                add(new VehicleDefinition("vehicles\\", "vehicles\\sdkfz7\\", "sdkfz7-flakvierling38", "half track", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 5, VehicleClass.TruckAAA));
                add(new VehicleDefinition("vehicles\\", "vehicles\\gaz\\", "gaz-aa-m4-aa", "truck", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.TruckAAA));
                add(new VehicleDefinition("vehicles\\", "vehicles\\zis\\", "zis5-72k", "truck", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.TruckAAA));
                add(new VehicleDefinition("vehicles\\", "vehicles\\m16\\", "m16", "half track", western, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TruckAAA));
                
                add(new VehicleDefinition("vehicles\\", "vehicles\\horch\\", "horch830", "staff car", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.Car));
                add(new VehicleDefinition("vehicles\\", "vehicles\\gaz\\", "gaz-m", "staff car", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.Car));
                add(new VehicleDefinition("vehicles\\", "vehicles\\willys\\", "willysmb", "jeep", western, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.Car));

                
                add(new VehicleDefinition("trains\\", "trains\\g8\\", "g8", "locomotive", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.TrainLocomotive));
                add(new VehicleDefinition("trains\\", "trains\\e\\", "e", "locomotive", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.TrainLocomotive));
                add(new VehicleDefinition("trains\\", "trains\\g8t\\", "g8t", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.TrainCoalCar));
                add(new VehicleDefinition("trains\\", "trains\\et\\", "et", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.TrainCoalCar));
                add(new VehicleDefinition("trains\\", "trains\\traincars\\", "boxb", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 50, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\traincars\\", "boxnb", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 50, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\traincars\\", "gondolab", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\traincars\\", "gondolanb", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\traincars\\", "pass", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\traincars\\", "passc", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformb", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformnb", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformemptyb", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformemptynb", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\tankb\\", "tankb", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\tanknb\\", "tanknb", "train car", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TrainCar));
                add(new VehicleDefinition("trains\\", "trains\\platformaa-mg34\\", "platformaa-mg34", "train car", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TrainCarAAA));
                add(new VehicleDefinition("trains\\", "trains\\platformaa-flak38\\", "platformaa-flak38", "train car", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TrainCarAAA));
                add(new VehicleDefinition("trains\\", "trains\\platformaa-m4\\", "platformaa-m4", "train car", allied, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TrainCarAAA));
                add(new VehicleDefinition("trains\\", "trains\\platformaa-61k\\", "platformaa-61k", "train car", allied, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.TrainCarAAA));

                add(new VehicleDefinition("vehicles\\", "vehicles\\pz38t\\", "pz38t", "Pz 38t", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("19430201"), 20, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\pziii-h\\", "pziii-h", "PzKw Mk III", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("19430301"), 40, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\pziii-l\\", "pziii-l", "PzKw Mk III", axis, DateUtils.getDateYYYYMMDD("19420201"), DateUtils.getDateYYYYMMDD("19440301"), 30, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\pziv-f1\\", "pziv-f1", "PzKw Mk IV", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("19420901"), 10, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\pziv-g\\", "pziv-g", "PzKw Mk IV", axis, DateUtils.getDateYYYYMMDD("19420301"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\pziv-h1\\", "pziv-h1", "Tiger I", axis, DateUtils.getDateYYYYMMDD("19430101"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\stug37l24\\", "stug37l24", "Stug III", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("19430201"), 20, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\stug40l43\\", "stug40l43", "Stug III", axis, DateUtils.getDateYYYYMMDD("19420201"), DateUtils.getDateYYYYMMDD("194500601"), 40, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\pz38t\\", "marderiii-h", "Marder III", axis, DateUtils.getDateYYYYMMDD("19420101"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\jagdpz4l70\\", "jagdpz4l70", "Jpz IV", axis, DateUtils.getDateYYYYMMDD("19430601"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.Tank));

                add(new VehicleDefinition("vehicles\\", "vehicles\\ba10m\\", "ba10m", "Armored car", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("19420501"), 10, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\ba64\\", "ba64", "Armored car", russia, DateUtils.getDateYYYYMMDD("194100401"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\bt7m\\", "bt7m", "Light Tank", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194200801"), 40, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\t70\\", "t70", "Light Tank", russia, DateUtils.getDateYYYYMMDD("194200401"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\kv1s\\", "kv1s", "KV1", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("19430501"), 10, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\kv1-41\\", "kv1-41", "KV1", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("19420501"), 10, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\kv1-42\\", "kv1-42", "KV1", russia, DateUtils.getDateYYYYMMDD("19410201"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\t34-76stz\\", "t34-76stz", "T34", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("19420801"), 80, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\t34-76stz-41\\", "t34-76stz-41", "T34", russia, DateUtils.getDateYYYYMMDD("19410201"), DateUtils.getDateYYYYMMDD("19430901"), 80, VehicleClass.Tank));
                add(new VehicleDefinition("vehicles\\", "vehicles\\t34-76-43\\", "t34-76-43", "T34", allied, DateUtils.getDateYYYYMMDD("19421101"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.Tank));

                add(new VehicleDefinition("vehicles\\", "vehicles\\t34-76stz-41\\", "t34-76stz-41", "T34", russia, DateUtils.getDateYYYYMMDD("19410201"), DateUtils.getDateYYYYMMDD("19430901"), 80, VehicleClass.Tank));

                
                add(new VehicleDefinition("vehicles\\", "artillery\\lefh18\\", "lefh18", "howitzer", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.ArtilleryHowitzer));
                add(new VehicleDefinition("vehicles\\", "artillery\\ml20\\", "ml20", "howitzer", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.ArtilleryHowitzer));
                add(new VehicleDefinition("vehicles\\", "artillery\\m30\\", "m30", "howitzer", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.ArtilleryHowitzer));
                add(new VehicleDefinition("vehicles\\", "artillery\\m1gun\\", "m1gun", "howitzer", western, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.ArtilleryHowitzer));
                add(new VehicleDefinition("vehicles\\", "artillery\\pak35\\", "pak35", "anti tank gun", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("19430101"), 80, VehicleClass.ArtilleryAntiTank));
                add(new VehicleDefinition("vehicles\\", "artillery\\pak38\\", "pak38", "anti tank gun", axis, DateUtils.getDateYYYYMMDD("19420601"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.ArtilleryAntiTank));
                add(new VehicleDefinition("vehicles\\", "artillery\\pak40\\", "pak40", "anti tank gun", axis, DateUtils.getDateYYYYMMDD("19420601"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.ArtilleryAntiTank));                
                add(new VehicleDefinition("vehicles\\", "artillery\\53k\\", "53k", "anti tank gun", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.ArtilleryAntiTank));
                add(new VehicleDefinition("vehicles\\", "artillery\\m42\\", "m42", "anti tank gun", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.ArtilleryAntiTank));
                add(new VehicleDefinition("vehicles\\", "artillery\\zis2gun\\", "zis2gun", "anti tank gun", russia, DateUtils.getDateYYYYMMDD("19420601"), DateUtils.getDateYYYYMMDD("194500601"), 40, VehicleClass.ArtilleryAntiTank));
                add(new VehicleDefinition("vehicles\\", "artillery\\zis3gun\\", "zis3gun", "anti tank gun", russia, DateUtils.getDateYYYYMMDD("19430101"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.ArtilleryAntiTank));
                add(new VehicleDefinition("vehicles\\", "artillery\\m5gun\\", "m5gun", "anti tank gun", western, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.ArtilleryAntiTank));                
                add(new VehicleDefinition("vehicles\\", "artillery\\flak36\\", "flak36", "anti aircraft gun", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.AAAArtillery));
                add(new VehicleDefinition("vehicles\\", "artillery\\flak37\\", "flak37", "anti aircraft gun", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.AAAArtillery));
                add(new VehicleDefinition("vehicles\\", "artillery\\flak38\\", "flak38", "anti aircraft gun", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.AAAArtillery));
                add(new VehicleDefinition("vehicles\\", "artillery\\flakvierling38\\", "flakvierling38", "anti aircraft gun", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.AAAArtillery));
                add(new VehicleDefinition("vehicles\\", "artillery\\52k\\", "52k", "anti aircraft gun", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.AAAArtillery));
                add(new VehicleDefinition("vehicles\\", "artillery\\61k\\", "61k", "anti aircraft gun", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.AAAArtillery));
                add(new VehicleDefinition("vehicles\\", "artillery\\72k\\", "72k", "anti aircraft gun", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.AAAArtillery));
                add(new VehicleDefinition("vehicles\\", "artillery\\m1a1gun-aa\\", "m1a1gun-aa", "anti aircraft gun", western, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.AAAArtillery));
                add(new VehicleDefinition("vehicles\\", "artillery\\mg34-aa\\", "mg34-aa", "AAA machine gun", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.AAAMachineGun));
                add(new VehicleDefinition("vehicles\\", "artillery\\dshk-aa\\", "dshk-aa", "AAA machine gun", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.AAAMachineGun));
                add(new VehicleDefinition("vehicles\\", "artillery\\maksim4-aa\\", "maksim4-aa", "AAA machine gun", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.AAAMachineGun));
                add(new VehicleDefinition("vehicles\\", "artillery\\m2mg-aa\\", "m2mg-aa", "AAA machine gun", western, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.AAAMachineGun));
                add(new VehicleDefinition("vehicles\\", "artillery\\mg34\\", "mg34", "machine gun", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.MachineGun));
                add(new VehicleDefinition("vehicles\\", "artillery\\dshk\\", "dshk", "machine gun", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.MachineGun));
                add(new VehicleDefinition("vehicles\\", "artillery\\m2mg\\", "m2mg", "machine gun", western, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.MachineGun));

                add(new VehicleDefinition("vehicles\\", "vehicles\\ndb\\", "ndb", "radio beacon", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.RadioBeacon));

                add(new VehicleDefinition("ships\\", "ships\\largecargoshiptype1\\", "largecargoshiptype1", "cargo ship", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.ShipCargo));
                add(new VehicleDefinition("ships\\", "ships\\largetankershiptype1\\", "largetankershiptype1", "tanker", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.ShipCargo));
                add(new VehicleDefinition("ships\\", "ships\\landboata\\", "landboata", "landing craft", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 5, VehicleClass.ShipCargo));
                add(new VehicleDefinition("ships\\", "ships\\torpboats38\\", "torpboats38", "torpedo boat", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.ShipWarship));
                add(new VehicleDefinition("ships\\", "ships\\destroyertype7\\", "destroyertype7", "destroyer", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 20, VehicleClass.ShipWarship));
                add(new VehicleDefinition("ships\\", "ships\\torpboatg5s11b\\", "torpboatg5s11b", "torpedo boat", allied, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.ShipWarship));
                add(new VehicleDefinition("ships\\", "ships\\torpboatg5s11b213\\", "torpboatg5s11b213", "torpedo boat", allied, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.ShipWarship));
                               
                add(new VehicleDefinition("ships\\", "ships\\rivershipgeorgia\\", "rivershipgeorgia", "river boat", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 60, VehicleClass.Drifter));
                add(new VehicleDefinition("ships\\", "ships\\rivergunshipa\\", "rivergunshipa", "river boat", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.Drifter));
                add(new VehicleDefinition("ships\\", "ships\\1124bm13\\", "1124bm13", "drifter", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 60, VehicleClass.Drifter));
                add(new VehicleDefinition("ships\\", "ships\\1124\\", "1124", "drifter", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 60, VehicleClass.Drifter));
                add(new VehicleDefinition("ships\\", "ships\\rivershipgeorgia\\", "rivershipgeorgiaaaa", "river boat", all, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 10, VehicleClass.DrifterAAA));
                
                
                add(new VehicleDefinition("vehicles\\", "artillery\\searchlightger\\", "searchlightger", "search light", axis, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.SearchLight));
                add(new VehicleDefinition("vehicles\\", "artillery\\searchlightsu\\", "searchlightsu", "search light", russia, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.SearchLight));
                add(new VehicleDefinition("vehicles\\", "artillery\\searchlightus\\", "searchlightus", "search light", western, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.SearchLight));
                
                add(new VehicleDefinition("ships\\", "ships\\subtypesh10\\", "subtypesh10", "torpedo boat", allied, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 30, VehicleClass.Submarine));
                add(new VehicleDefinition("ships\\", "ships\\subtype2b\\", "subtype2b", "torpedo boat", allied, DateUtils.getDateYYYYMMDD("19390901"), DateUtils.getDateYYYYMMDD("194500601"), 80, VehicleClass.Submarine));
            }
            catch(PWCGException exp)
            {
                
            }
        }
    };

    
    public static void main(String[] args)
    {
        try
        {
            VehicleDefinitionIOJson.writeJson(testTruck);
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
    }
}
