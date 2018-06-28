package pwcg.campaign.plane.payload;

public enum PayloadElement 
{
	//WW I
    RADIO(PayloadElementCategory.PLANE_PART, "Radio", 20),
    CAMERA(PayloadElementCategory.PLANE_PART, "Camera", 10),
    LB20_X1(PayloadElementCategory.ORDNANCE_BOMB, "20 lb Bomb x1", 10),
    LB20_X2(PayloadElementCategory.ORDNANCE_BOMB, "20 lb Bomb x2", 18),
    LB20_X4(PayloadElementCategory.ORDNANCE_BOMB, "20 lb Bomb x4", 35),
    LB20_X8(PayloadElementCategory.ORDNANCE_BOMB, "20 lb Bomb x8", 150),
    LB20_X12(PayloadElementCategory.ORDNANCE_BOMB, "20 lb Bomb x12", 110),
    LB20_X16(PayloadElementCategory.ORDNANCE_BOMB, "20 lb Bomb x16", 150),
    LB40_X8(PayloadElementCategory.ORDNANCE_BOMB, "40 lb Bomb x8", 150),
    LB65_X2(PayloadElementCategory.ORDNANCE_BOMB, "65 lb Bomb x2", 60),
    LB65_X4(PayloadElementCategory.ORDNANCE_BOMB, "65 lb Bomb x4", 120),
    LB112_X1(PayloadElementCategory.ORDNANCE_BOMB, "112 lb Bomb x1", 50),
    LB112_X2(PayloadElementCategory.ORDNANCE_BOMB, "112 lb Bomb x2", 110),
    LB112_X3(PayloadElementCategory.ORDNANCE_BOMB, "112 lb Bomb x3", 180),
    LB112_X4(PayloadElementCategory.ORDNANCE_BOMB, "112 lb Bomb x4", 220),
    LB230_X1(PayloadElementCategory.ORDNANCE_BOMB, "230 lb Bomb x1", 115),
    LB230_X2(PayloadElementCategory.ORDNANCE_BOMB, "230 lb Bomb x2", 230),
    LB112x8(PayloadElementCategory.ORDNANCE_BOMB, "112 lb Bomb x8", 480),
    LB112x16(PayloadElementCategory.ORDNANCE_BOMB, "112 lb Bomb x16", 960),
    LB250x2(PayloadElementCategory.ORDNANCE_BOMB, "250 lb Bomb x2", 240),
    LB250x3(PayloadElementCategory.ORDNANCE_BOMB, "250 lb Bomb x3", 360),
    LB250x4(PayloadElementCategory.ORDNANCE_BOMB, "250 lb Bomb x4", 480),
    LB250x8(PayloadElementCategory.ORDNANCE_BOMB, "250 lb Bomb x8", 960),
    LB1650x1(PayloadElementCategory.ORDNANCE_BOMB, "1650 lb Bomb x1`", 800),
    POOD_1_10X(PayloadElementCategory.ORDNANCE_BOMB, "10x1 pood 1", 100),
    POOD_2_10X(PayloadElementCategory.ORDNANCE_BOMB, "10x2 pood 2", 200),
    POOD_2_5X(PayloadElementCategory.ORDNANCE_BOMB, "2x5 pood 2", 100),
    KG8_X32(PayloadElementCategory.ORDNANCE_BOMB, "8 kg Bomb x32", 260),
    KG10_X4(PayloadElementCategory.ORDNANCE_BOMB, "10 kg Bomb x4", 40),
    KG10_X8(PayloadElementCategory.ORDNANCE_BOMB, "10 kg Bomb x8", 80),
    KG12_X4(PayloadElementCategory.ORDNANCE_BOMB, "12.5 kg Bomb x4", 50),
    KG12_X8(PayloadElementCategory.ORDNANCE_BOMB, "12.5 kg Bomb x8", 100),
    KG12_X10(PayloadElementCategory.ORDNANCE_BOMB, "12 kg Bomb x10", 120),
    KG12_X12(PayloadElementCategory.ORDNANCE_BOMB, "12.5 kg Bomb x12", 150),
    KG12_X16(PayloadElementCategory.ORDNANCE_BOMB, "12.5 kg Bomb x16", 200),
    KG12_X20(PayloadElementCategory.ORDNANCE_BOMB, "12 kg Bomb x20", 240),
    KG50x1(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x1", 50),
    KG50x3(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x3", 150),
    KG50x7(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x7", 350),
    KG100x7(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x7", 700),
    KG100x4(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x4", 400),
    KG300x1(PayloadElementCategory.ORDNANCE_BOMB, "300 kg Bomb x1", 300),
    LE_PRIEUR_ROCKETS(PayloadElementCategory.ORDNANCE_BOMB, "Le Prieur rockets", 20),

    //WW II
	REMOVE_HEADREST(PayloadElementCategory.PLANE_PART, "Remove Headrest", 0),
	GLASS_HEADREST(PayloadElementCategory.PLANE_PART, "Glass Headrest", 0),
	ARMORED_WINDSCREEN(PayloadElementCategory.PLANE_PART, "Armored Windscreen", 10),
	EXTRA_ARMOR(PayloadElementCategory.PLANE_PART, "Armor", 300),
	TURRET(PayloadElementCategory.PLANE_PART, "Turret", 250),
	SIREN(PayloadElementCategory.PLANE_PART, "Siren", 5),
	WINDSCREEN(PayloadElementCategory.PLANE_PART, "Windscreen", 2),
    MIRROR(PayloadElementCategory.PLANE_PART, "Mirror", 0),
	BELLY_TURRET(PayloadElementCategory.PLANE_PART, "Belly Turret", 250),
    NOSE_TURRET(PayloadElementCategory.PLANE_PART, "Nose Turret", 250),
    M82F_ENGINE(PayloadElementCategory.PLANE_PART, "M82F engine", 100),
    MERLIN_ENGINE(PayloadElementCategory.PLANE_PART, "Merlin engine", 100),
    RPK10(PayloadElementCategory.PLANE_PART, "RPK 10", 100),
    LANDING_LIGHTS(PayloadElementCategory.PLANE_PART, "Landing Lights", 0),
    LA5_AMMO(PayloadElementCategory.PLANE_PART, "LA5 Ammo Scheme", 0),
    PEILG6(PayloadElementCategory.PLANE_PART, "Peil G6", 0),
    U_17(PayloadElementCategory.PLANE_PART, "U/17", 300),
    
    

    EMPTY(PayloadElementCategory.CARGO, "EMpty", 0),
    PARATROOPERS(PayloadElementCategory.CARGO, "Paratroopers", 1000),
	CARGO(PayloadElementCategory.CARGO, "Cargo", 1000),

	STANDARD(PayloadElementCategory.WEAPON, "Standard Payload", 0),
    ADDITIONJAL_AMMO(PayloadElementCategory.WEAPON, "Additional Ammo", 200),

    MAB_250(PayloadElementCategory.ORDNANCE_BOMB, "MAB 250", 1000),
	FAB50SV_X2(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x2", 100),
	FAB50SV_X4(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x4", 200),
	FAB50SV_X6(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x6", 300),
    FAB100M_X1(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x1", 100),
    FAB100M_X2(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x2", 200),
	FAB100M_X4(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x4", 400),
    FAB100M_X6(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x6", 600),
    FAB100M_X8(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x8", 800),
    FAB100M_X10(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x10", 1000),
    FAB100M_X16(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x16", 1600),
    FAB100M_X20(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x20", 2000),
	FAB250SV_X1(PayloadElementCategory.ORDNANCE_BOMB, "250 kg Bomb x1", 250),
	FAB250SV_X2(PayloadElementCategory.ORDNANCE_BOMB, "250 kg Bomb x2", 500),
	FAB250SV_X4(PayloadElementCategory.ORDNANCE_BOMB, "250 kg Bomb x4", 1000),
	FAB500M_X1(PayloadElementCategory.ORDNANCE_BOMB, "500 kg Bomb x1", 500),
	FAB500M_X2(PayloadElementCategory.ORDNANCE_BOMB, "500 kg Bomb x2", 1000),

	ROS82_X2(PayloadElementCategory.ORDNANCE_ROCKET, "RoS 82mm Rocket x2", 12),
	ROS82_X4(PayloadElementCategory.ORDNANCE_ROCKET, "RoS 82mm Rocket x4", 24),
	ROS82_X6(PayloadElementCategory.ORDNANCE_ROCKET, "RoS 82mm Rocket x6", 36),
	ROS82_X8(PayloadElementCategory.ORDNANCE_ROCKET, "RoS 82mm Rocket x8", 48),
	ROFS132_X4(PayloadElementCategory.ORDNANCE_ROCKET, "RoFS 132mm Rocket x4", 92),
	ROFS132_X6(PayloadElementCategory.ORDNANCE_ROCKET, "RoFS 132mm Rocket x6", 138),
	ROFS132_X8(PayloadElementCategory.ORDNANCE_ROCKET, "RoFS 132mm Rocket x8", 184),
	ROS132_X10(PayloadElementCategory.ORDNANCE_ROCKET, "RoFS 132mm Rocket x10", 230),
	
    SHVAK_GUNPOD(PayloadElementCategory.WEAPON, "Shvak Gun Pod", 50),
    SHVAK_UPGRADE(PayloadElementCategory.WEAPON, "Shvak Upgrade", 50),
	BK_GUNPOD(PayloadElementCategory.WEAPON, "BK Gun Pod", 50),
    VYA23_APHE_GUNPOD(PayloadElementCategory.WEAPON, "VYA 23mm Autocannon (AP,HE)", 50),
    VYA23_AP_GUNPOD(PayloadElementCategory.WEAPON, "VYA 23mm Autocannon (AP)", 50),
	VYA23_HE_GUNPOD(PayloadElementCategory.WEAPON, "VYA 23mm Autocannon (HE)", 50),
    SH37_APHE_GUNPOD(PayloadElementCategory.WEAPON, "SH 37mm Autocannon (AP,HE)", 50),
    SH37_AP_GUNPOD(PayloadElementCategory.WEAPON, "SH 37mm Autocannon (AP)", 50),
	SH37_HE_GUNPOD(PayloadElementCategory.WEAPON, "SH 37mm Autocannon (HE)", 50),
	NS37_AP_GUNPOD(PayloadElementCategory.WEAPON, "NS 37mm Autocannon (AP)", 50),
	NS37_HE_GUNPOD(PayloadElementCategory.WEAPON, "NS 37mm Autocannon (HE)", 50),
	NS37_APHE_GUNPOD(PayloadElementCategory.WEAPON, "NS 37mm Autocannon (AP,HE)", 50),
    P40_4MG(PayloadElementCategory.WEAPON, "4x .50 Cal MG", 0),
    REM_M230(PayloadElementCategory.WEAPON, "REM M230", 50),
    P3937MM_AP(PayloadElementCategory.WEAPON, "37mm AP", 50),
	

	SC50_X2(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x2", 100),
	SC50_X4(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x4", 200),
	SC50_X6(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x6", 300),
	SC50_X8(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x8", 400),
	SC50_X12(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x12", 600),
	SC50_X16(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x16", 800),
	SC50_X18(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x18", 900),
	SC50_X28(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x28", 1400),
	SC50_X32(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x32", 1600),
	SC50_X44(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x44", 2200),
	SC250_X1(PayloadElementCategory.ORDNANCE_BOMB, "250 kg Bomb x1", 250),
	SC250_X2(PayloadElementCategory.ORDNANCE_BOMB, "250 kg Bomb x2", 500),
	SC250_X3(PayloadElementCategory.ORDNANCE_BOMB, "250 kg Bomb x3", 750),
	SC250_X4(PayloadElementCategory.ORDNANCE_BOMB, "250 kg Bomb x4", 1000),
	SC250_X6(PayloadElementCategory.ORDNANCE_BOMB, "250 kg Bomb x6", 1500),
	SC250_X8(PayloadElementCategory.ORDNANCE_BOMB, "250 kg Bomb x8", 2000),
	SC500_X1(PayloadElementCategory.ORDNANCE_BOMB, "500 kg Bomb x1", 500),
	SC500_X2(PayloadElementCategory.ORDNANCE_BOMB, "500 kg Bomb x2", 1000),
	SC500_X4(PayloadElementCategory.ORDNANCE_BOMB, "500 kg Bomb x4", 2000),
	SC1000_X1(PayloadElementCategory.ORDNANCE_BOMB, "1000 kg Bomb x1", 1000),
	SC1000_X2(PayloadElementCategory.ORDNANCE_BOMB, "1000 kg Bomb x2", 2000),
	SC1800_X1(PayloadElementCategory.ORDNANCE_BOMB, "1800 kg Bomb x1", 1800),
	SC1800_X2(PayloadElementCategory.ORDNANCE_BOMB, "1800 kg Bomb x2", 3600),
	SC2500_X1(PayloadElementCategory.ORDNANCE_BOMB, "2500 kg Bomb x1", 2500),
	SD70_X4(PayloadElementCategory.ORDNANCE_BOMB, "70 kg Bomb x4", 280),
	T50_X2(PayloadElementCategory.ORDNANCE_BOMB, "50 kg Bomb x2", 100),
	T100_X2(PayloadElementCategory.ORDNANCE_BOMB, "100 kg Bomb x2", 200),

	MG151_15_GUNPOD(PayloadElementCategory.WEAPON, "MG 151 15mm Gun Pod", 50),
	MG151_20_GUNPOD(PayloadElementCategory.WEAPON, "MG 151 20mm Gun Pod", 50),
    MG151_20_UPGRADE(PayloadElementCategory.WEAPON, "MG151 20mm Upgrade", 50),
	MGFF_WING_GUNS(PayloadElementCategory.WEAPON, "MG FF Wing Guns", 0),
	MK101_30_AP_GUNPOD(PayloadElementCategory.WEAPON, "MK 101 30mm Gun Pod (AP)", 50),
	MK101_30_HE_GUNPOD(PayloadElementCategory.WEAPON, "MK 101 30mm Gun Pod (HE)", 50),
	MK103_30_AP_GUNPOD(PayloadElementCategory.WEAPON, "MK 103 30mm Gun Pod (AP)", 50),
	MK103_30_HE_GUNPOD(PayloadElementCategory.WEAPON, "MK 103 30mm Gun Pod (HE)", 50),
    MK108_30(PayloadElementCategory.WEAPON, "MK 108 30mm", 50),
	BK37_HE_GUNPOD(PayloadElementCategory.WEAPON, "37mm Gun Pod (HE)", 100),
	BK37_AP_GUNPOD(PayloadElementCategory.WEAPON, "37mm Gun Pod (AP)", 100),
	BREDA_GUNPOD(PayloadElementCategory.WEAPON, "Breda Gun Pod", 50),

	NONE(PayloadElementCategory.PLANE_PART, "No Modifications", 0);
	
	private String description;
    private int weight;
	private PayloadElementCategory category;
	 
	PayloadElement(PayloadElementCategory category, String description, int weight)
	{
        this.category = category;
        this.description = description;
		this.weight = weight;		
	}

	public String getDescription() 
	{
		return description;
	}

	public int getWeight() 
	{
		return weight;
	}

	public PayloadElementCategory getCategory() 
	{
		return category;
	}
}
