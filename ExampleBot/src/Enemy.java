import java.util.ArrayList;
import java.util.List;

import bwapi.Color;
import bwapi.Order;
import bwapi.Position;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.TilePosition;

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
}
