import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bwapi.AbstractPoint;
import bwapi.Region;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.Position;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
public class Mapping {

	public static void reset()
	{
		bauPosition.clear();
		
	}
	private static Map<TilePosition,UnitType> bauPosition=new HashMap<>();
	
	public static Map<TilePosition, UnitType> getBauPosition() {
		return bauPosition;
	}
	public static void setBauPosition(Map<TilePosition, UnitType> bauPosition) {
		Mapping.bauPosition = bauPosition;
	}
	
	
	
	private static int startX = Core.Spiel().getRegion(1).getBoundsLeft();
	private static int startY = Core.Spiel().getRegion(1).getBoundsTop();
	private static int stopX = Core.Spiel().getRegion(1).getBoundsRight();
	private static int stopY = Core.Spiel().getRegion(1).getBoundsBottom();
	
	public static void startscan(UnitType gebauede)
	{		
		for( int i = startX;i <= stopX;i++)
		{
			for(int j = startY; j <= stopY;j++)
			{
				if(Core.Spiel().canBuildHere(new TilePosition(i,j), gebauede)
				&& Core.Spiel().isExplored(new TilePosition(i,j))
				&& Buildings.sindkeineresindernaehe(new TilePosition(i,j).toPosition()))
				{
					bauPosition.put(new TilePosition(i,j), gebauede);
					break;
				}
					
			}	
		}
		Buildings.delaytimer(6000);
	}
	public static void listeAufraemen(UnitType gebauede)
	{
		for( int i = startX;i <= stopX;i++)
		{
			for(int j = startY; j <= stopY;j++)
			{
				if(!Core.Spiel().canBuildHere(new TilePosition(i,j), gebauede))
				{
					bauPosition.remove(new TilePosition(i,j));
					break;
				}
				
			}
		}
		Buildings.delaytimer(6000);
	}
	private static Map<Double,TilePosition> buildingPosition = new HashMap<>();
	private static List<TilePosition> buildingPosition2 = new ArrayList<>();
	public static void mapToListChange()
	{
		Map<TilePosition,UnitType> map = bauPosition;
		TilePosition center = Buildings.Center();
		double distance =0;
		for(TilePosition key : map.keySet())
		{
			if(center.getDistance(key)>0)
			{
				distance= center.getDistance(key);
				
				buildingPosition.put(distance, key);
			}
		}
		
		//System.out.println(buildingPosition);
		
		//System.out.println(center.getDistance(buildingPosition.get(2)));
		Buildings.delaytimer(500);
	}
	public static void sort()
	{
		Map<Double, TilePosition> map = buildingPosition;
		Double distanceMaximum = 20.0;
		for(Double distance : map.keySet())
		{
			if(distance<distanceMaximum)
			{
				buildingPosition2.add(map.get(distance));
			}
		}
		System.out.print(buildingPosition2);
	}
	private static TilePosition buildTile =null;
	public static TilePosition getnext(UnitType geb)
	{
		Map<Double, TilePosition> test = buildingPosition;
		for(Double distance: test.keySet())
		{
			buildTile = test.get(distance);
			if(Core.Spiel().canBuildHere(buildTile, geb) && Core.Spiel().isExplored(buildTile) && distance<10)
			{
					break;
			}
			
			
		}
		return buildTile;
			
	}

	
	public static Position chokePoint()
	{
		TilePosition start = Buildings.Center();
		TilePosition sammeln=null;
		for(Chokepoint choke : BWTA.getChokepoints())
		{
			if(choke.getCenter().toTilePosition().getDistance(start.getX(), start.getY())<30)
			{
				sammeln = choke.getCenter().toTilePosition();
				
			}
		}
		if(sammeln !=null)
			return sammeln.toPosition();
		else return start.toPosition();
	}
	
	static Position enemyBase=null;
	
	public static void scout()
	{
		Unit scout = Einheiten.getArbeiter().get(5);
		BaseLocation unexploredLocation=null;
		
		for(BaseLocation aLocation: BWTA.getStartLocations())
		{
			if(!Core.Spiel().isExplored(aLocation.getTilePosition()))
			{
				unexploredLocation=aLocation;		
				enemyBase=aLocation.getPosition();
			}
		}	
		if(scout!=null && unexploredLocation !=null && !scout.attack(unexploredLocation.getPosition()))
		{
			System.out.println("Scout");
			scout.attack(unexploredLocation.getPosition());	
		}
		
	}
	
	
	
	
		
}


