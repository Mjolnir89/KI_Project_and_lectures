import java.util.ArrayList;
import java.util.List;

import bwapi.Color;
import bwapi.Order;
import bwapi.Position;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.TilePosition;
import bwta.*;

public class Enemy {

	static List<Unit> enemieUnits = new ArrayList<>();
	
	public static void saveEnemyUnits()
	{
		
			for(Unit eUnit: Core.enemy().getUnits())
			{
				if(eUnit.getHitPoints()>0 && eUnit.isVisible() && !enemieUnits.contains(eUnit))
				{
					enemieUnits.add(eUnit);
					System.out.println("EnemyList"+ enemieUnits);
					
				}
				else if(eUnit.getHitPoints()<=0 || !eUnit.isVisible())
				{
					enemieUnits.remove(eUnit);
				}
				
			}
		
	}
	static List<TilePosition> enemyPosition = new ArrayList<>();
	public static void saveEnemyPosition()
	{
		for(Unit eUnit:enemieUnits)
		{
			if(eUnit.getHitPoints()>0)
			{
				enemyPosition.add(eUnit.getPosition().toTilePosition());
				
			}
			
		
		}
	}
	public static void dodge()
	{
		
	}
	public static TilePosition Center()
	{
		TilePosition start = null;
		Unit Headquarter = null;
		for(BaseLocation aStart : BWTA.getStartLocations())
		{
			if(aStart.isStartLocation() && aStart.getTilePosition().getDistance(Buildings.Center().getX(), Buildings.Center().getY())>50)
			{
				 start =aStart.getTilePosition();
		}
			}
		Core.Spiel().drawBoxMap(start.getX()*32, start.getY()*32, 
				(start.getX()*32+32), (start.getY()*32+32), Color.Red);
		return start;
	}
	public static TilePosition chokePoint()
	{
		TilePosition start = Center();
		TilePosition sammeln=null;
		for(Chokepoint choke : BWTA.getChokepoints())
		{
			if(choke.getCenter().toTilePosition().getDistance(start.getX(), start.getY())<10)
			{
				sammeln = choke.getCenter().toTilePosition();
				System.out.println("ChokeEnemy1: "+ sammeln);
				Core.Spiel().drawBoxMap(((int)sammeln.getX()*32), ((int)sammeln.getY()*32), ((int)sammeln.getX()*32+32), ((int)sammeln.getY()*32+32), Color.Brown);
			}
			else if(choke.getCenter().toTilePosition().getDistance(start.getX(), start.getY())<20 )
			{
				sammeln = choke.getCenter().toTilePosition();
				Core.Spiel().drawBoxMap(((int)sammeln.getX()*32), ((int)sammeln.getY()*32), ((int)sammeln.getX()*32+32), ((int)sammeln.getY()*32+32), Color.Blue);
			}
		}
		if(sammeln !=null)
			return sammeln;
		else return start;
	}
}
