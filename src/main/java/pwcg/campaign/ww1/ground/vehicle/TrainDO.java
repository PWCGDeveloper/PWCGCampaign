package pwcg.campaign.ww1.ground.vehicle;

public class TrainDO implements Cloneable
{
	String category;
	String id;
	String name;
	double distance;
	
	private TrainDO()
	{
	}
	
	public TrainDO (String category,
					String id,
					String name,
					double distance) 
	{
		this.category = category;
		this.id = id;
		this.name = name;
		this.distance = distance;
	}
	
	public TrainDO copy () 
	{
		TrainDO trainDO = new TrainDO();

		trainDO.category = this.category;
		trainDO.id = this.id;
		trainDO.name = this.name;
		trainDO.distance = this.distance;
		
		return trainDO;
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
}
