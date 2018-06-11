import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bwapi.AbstractPoint;
import bwapi.Color;
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
	public static List<TilePosition> BuildTileList = new ArrayList<>();
	
	public static void scan(UnitType gebauede)
	{
		for( int i = startX;i <= stopX;i++)
		{
			for(int j = startY; j <= stopY;j++)
			{
				
				if(Core.Spiel().canBuildHere(new TilePosition(i,j), gebauede)
				 &&(Core.Spiel().isVisible(new TilePosition(i,j)))	
				 &&Core.Spiel().isExplored(new TilePosition(i,j))
				 && Buildings.sindKeineRessourcenInDerNaehe(new TilePosition(i,j).toPosition()))
				
				{
					
					BuildTileList.add(new TilePosition(i,j));
					
				}
					
			}
		}
		Buildings.delaytimer(600);
	}
	public static TilePosition getNextFromList(UnitType geb)
	{
		TilePosition Center = Buildings.Center();
		for(TilePosition aPosition : BuildTileList)
		{
			buildTile =aPosition;
			
			double distance = Center.getDistance(aPosition.getX(), aPosition.getY());
			if(Core.Spiel().canBuildHere(buildTile, geb) && Core.Spiel().isExplored(buildTile) && distance<=10)
			{
				//System.out.println("BauPlatz1");
				Core.Spiel().drawBoxMap(((int)buildTile.getX()*32), ((int)buildTile.getY()*32), ((int)buildTile.getX()*32+32), ((int)buildTile.getY()*32+32), Color.Green);
					break;
			}
			else if(Core.Spiel().canBuildHere(buildTile, geb) && Core.Spiel().isExplored(buildTile) && distance<15 && distance>10)
			{
				//System.out.println("BauPlatz2");
				Core.Spiel().drawBoxMap(((int)buildTile.getX()*32), ((int)buildTile.getY()*32), ((int)buildTile.getX()*32+32), ((int)buildTile.getY()*32+32), Color.Blue);
					break;					
			}
			else if(Core.Spiel().canBuildHere(buildTile, geb) && Core.Spiel().isExplored(buildTile) && distance<20 && distance>=15)
			{
				//System.out.println(Core.Spiel().canBuildHere(buildTile, geb) + "\t" + Core.Spiel().isExplored(buildTile));
				Core.Spiel().drawBoxMap(((int)buildTile.getX()*32), ((int)buildTile.getY()*32), ((int)buildTile.getX()*32+32), ((int)buildTile.getY()*32+32), Color.Red);
				//testliste.remove(aPosition);
				}
			
		}
		return buildTile;
			
	}
	public static List<TilePosition> getTestliste() {
		return BuildTileList;
	}
	public static void setTestliste(List<TilePosition> BuildTileList) {
		Mapping.BuildTileList = BuildTileList;
	}

	private static TilePosition buildTile =null;
	public static Position chokePoint()
	{
		TilePosition start = Buildings.Center();
		TilePosition sammeln=null;
		for(Chokepoint choke : BWTA.getChokepoints())
		{
			if(choke.getCenter().toTilePosition().getDistance(start.getX(), start.getY())<10)
			{
				sammeln = choke.getCenter().toTilePosition();
				
			}
			else if(choke.getCenter().toTilePosition().getDistance(start.getX(), start.getY())<20)
			{
				sammeln = choke.getCenter().toTilePosition();
			}
		}
		if(sammeln !=null)
			return sammeln.toPosition();
		else return start.toPosition();
	}
	
	static Position enemyBase=null;
	static Unit scout=null;
	public static void scout()
	{
		if( scout == null ){
			Unit aUnit = AttackUnits.getMarines().get(0);
				if(aUnit.getType()== UnitType.Terran_Marine
				&& aUnit.isIdle())
				{
					scout = aUnit;
										
				}
			}
		
		
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


