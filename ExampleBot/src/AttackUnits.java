import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bwapi.Color;
import bwapi.Order;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.TilePosition;

/*ToDo
 * schwadron Funktion soll Marines im chokepoint zaehlen
 */
public class AttackUnits {

	static List<Unit> Marines = new ArrayList<>();
	public static  void AttackUnitList()
	{
		Position choke = Mapping.chokePoint();
		
		for(Unit aUnit: Core.selbst().getUnits())
		{
			if(aUnit.isIdle() && ((aUnit.getType() == UnitType.Terran_Marine)
			|| (aUnit.getType() == UnitType.Terran_Firebat))		
			&& !aUnit.isTraining() && !Marines.contains(aUnit)
			&& aUnit.isCompleted())
			{
				Marines.add(aUnit);
				if(aUnit.getPosition().getDistance(choke.getX(), choke.getY())>=3)
					aUnit.move(choke);
					
			}
			
			if(aUnit.getHitPoints()<=0)
			{
				Marines.remove(aUnit);
			}
		}
	}
	static List<Unit> squad = new ArrayList<>();
	public static void schwadron()
	{
		TilePosition choke = Mapping.chokePoint().toTilePosition();
		Core.Spiel().drawBoxMap(((choke.getX()-1)*32), ((choke.getY()-1)*32), (choke.getX()*32+32), (choke.getY()*32+32), Color.Red);
		for(Unit aUnit: Marines)
		{
			if(aUnit.getPosition().toTilePosition().getDistance(choke.getX(), choke.getY())<=4
				&& !squad.contains(aUnit))
			{
				squad.add(aUnit);
			}
			else if(aUnit.getPosition().toTilePosition().getDistance(choke.getX(), choke.getY())>4)
			{
				squad.remove(aUnit);
			}
		}
		
	}
	public static void attack()
	{
		for(Unit aUnit:squad)
		{
			aUnit.attack(Mapping.enemyBase);
		}
	}
	public static List<Unit> getMarines() {
		return Marines;
	}
	public static void setMarines(List<Unit> marines) {
		Marines = marines;
	}
	public void onUnitDestroy(Unit aUnit) {
		
		if(Marines != null && Marines.contains(aUnit)){
			Marines.remove(aUnit);
		}
	}
	static List<TilePosition> squadPosition = new ArrayList<>();
	public static void TrackPosition()
	{
		for(Unit aUnit:Marines)
		{
			squadPosition.add(aUnit.getPosition().toTilePosition());
			
			
		}
	}
}
