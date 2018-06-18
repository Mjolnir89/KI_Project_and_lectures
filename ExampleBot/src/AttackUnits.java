import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bwapi.Color;
import bwapi.Order;
import bwapi.Position;
import bwapi.TechType;
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
			|| (aUnit.getType() == UnitType.Terran_Vulture
			|| (aUnit.getType() == UnitType.Terran_Medic)))		
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
	static List<Unit> tanks = new ArrayList<>();
	public static void taketanks()
	{
		Position choke = Mapping.chokePoint();
		for(Unit aUnit: Core.selbst().getUnits())
		{
			if(aUnit.isIdle() 
			&& aUnit.getType() == UnitType.Terran_Siege_Tank_Tank_Mode		
			&& !aUnit.isTraining() 
			&& !tanks.contains(aUnit)
			&& aUnit.isCompleted())
			{
				tanks.add(aUnit);
				
				if(aUnit.getPosition().getDistance(choke.getX(), choke.getY())>=3)
					aUnit.move(choke);
				//System.out.println(aUnit.getPosition().toTilePosition());
					
			}
			
			if(aUnit.getHitPoints()<=0)
			{
				tanks.remove(aUnit);
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
				
				//System.out.println(aUnit.getGroundWeaponCooldown());
			}
			else if(aUnit.getPosition().toTilePosition().getDistance(choke.getX(), choke.getY())>4)
			{
				squad.remove(aUnit);
			}
		}
		
	}
	public static void moveToChokePoint()
	{	
		Position choke = Mapping.chokePoint();
		for(Unit vUnit : AttackUnits.getMarines())
		{
			if((vUnit.canAttack())
			&& !vUnit.isTraining()
			&& vUnit.isCompleted()
			&& vUnit.isIdle()
			&& !vUnit.isUnderAttack()
			&& vUnit.getPosition().getDistance(choke.getX(), choke.getY())>=3)
			{
				vUnit.move(Mapping.chokePoint());
			}
		}
	
	}
	public static boolean attack()
	{
		if(squad.size()>10 && !Einheiten.attackFirstUnit())
		{
			for(Unit aUnit:squad)
			{
				if(!aUnit.isUnderAttack())
				{
					aUnit.attack(Enemy.chokePoint().toPosition());
				}
				else
				{
					aUnit.attack(aUnit.getPosition());
				}
				
			}
			return true;
		}
		return false;
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
	public static void spidermines()
	{
		for(Unit aUnit : Marines)
		{
			if(aUnit.getType() == UnitType.Terran_Vulture
			&& aUnit.canUseTech(TechType.Spider_Mines)
			&& aUnit.move(Mapping.enemyBase))
			{
				System.out.println("mines");
				aUnit.useTech(TechType.Spider_Mines);
			}
		}
	}
	//Belagerungsmodus von Panzern
	public static void tank_Siege_Mode()
	{
		for(Unit aUnit : tanks)
		{
			if(aUnit.getType() == UnitType.Terran_Siege_Tank_Tank_Mode
			&& aUnit.canUseTech(TechType.Tank_Siege_Mode)
			&& !aUnit.attack(aUnit.getPosition())
			&& aUnit.getDistance(Mapping.chokePoint())<4)
			{
				System.out.println("Sieg Mode");
				aUnit.useTech(TechType.Tank_Siege_Mode);
			}
		}
	}
	//Marine Stimpacks
	public static void stimpacks()
	{
		for(Unit aUnit: Marines)
		{
			if(aUnit.getType() == UnitType.Terran_Marine
			&& aUnit.isUnderAttack()
			&& aUnit.canUseTech(TechType.Stim_Packs))
			{
				aUnit.useTech(TechType.Stim_Packs);
			}
		}
	}
}
