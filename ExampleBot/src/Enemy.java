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
		if(Core.enemy().getRace()== Race.Terran)
		{
			for(Unit eUnit: Core.enemy().getUnits())
			{
				if(eUnit.getType() == UnitType.Terran_Marine && eUnit.getHitPoints()>0 && eUnit.isVisible())
				{
					enemieUnits.add(eUnit);
					
				}
			}
		}
	}
	static List<TilePosition> enemyPosition = new ArrayList<>();
	public static void saveEnemyPosition()
	{
		for(Unit eUnit:enemieUnits)
		{
			enemyPosition.add(eUnit.getPosition().toTilePosition());
			Core.Spiel().drawBoxMap(eUnit.getPosition().toTilePosition().getX()*32, eUnit.getPosition().toTilePosition().getY()*32, 
					(eUnit.getPosition().toTilePosition().getX()*32+32), (eUnit.getPosition().toTilePosition().getY()*32+32), Color.Red);
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
