import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bwapi.AbstractPoint;
import bwapi.Region;
import bwapi.TilePosition;
import bwapi.UnitType;
import bwapi.Position;
import bwta.BWTA;
import bwta.Chokepoint;
public class Mapping {

	public static void reset()
	{
		bauPosition.clear();
		buildingPosition.clear();
		
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
	private static List<TilePosition> buildingPosition = new ArrayList<>();
	public static void mapToListChange(Map<TilePosition,UnitType> mp)
	{
		TilePosition center = Buildings.Center();
		double distance =0;
		for(TilePosition key : mp.keySet())
		{
			if(center.getDistance(key)>0)
			{
				distance= center.getDistance(key);				
				buildingPosition.add(key);
			}
		}
		Buildings.delaytimer(500);
	}
	private static TilePosition buildTile =null;
	public static TilePosition getnext(UnitType geb)
	{
		Iterator<TilePosition> iter = getBuilder().iterator();
		
		while(iter.hasNext() )
		{
			buildTile = (TilePosition) iter.next();
			if(Core.Spiel().canBuildHere(buildTile, geb) && Core.Spiel().isExplored(buildTile))
			{
				break;
			}
			else
			{
				getBuilder().remove(iter);
			}
		}
		
		return buildTile;	
	}

	public static List<TilePosition> getBuilder() {
		return buildingPosition;
	}
	public static void setBuilder(List<TilePosition> builder) {
		Mapping.buildingPosition = builder;
	}
	public static Position chokePoint()
	{
		TilePosition start = Buildings.Center();
		TilePosition sammeln=null;
		for(Chokepoint choke : BWTA.getChokepoints())
		{
			//System.out.println(choke.getCenter().toTilePosition());
			if(choke.getCenter().toTilePosition().getDistance(start.getX(), start.getY())<20)
			{
				sammeln = choke.getCenter().toTilePosition();
				//System.out.print(choke.getCenter().toTilePosition());
			}
		}
		return sammeln.toPosition();
	}
		
	}


