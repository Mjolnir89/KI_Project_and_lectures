import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;


public class AttackUnits {
	
	
	
	public static  Unit[] attackMarines()
	{
		Unit Marine1=null;
		Unit Marine2=null;
		Unit Marine3=null;
		Unit Marine4=null;
		Unit Marine5=null;
		Unit Marine6=null;
		
		if(Marine1== null || Marine1.getHitPoints()<=0)
		{
			for(Unit aUnit: Core.selbst().getUnits())
			{
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining())
				{
					Marine1=aUnit;
				}
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining()&& Marine2 !=Marine1)
				{
					Marine2=aUnit;
				}
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining()&& Marine3 !=Marine2)
				{
					Marine3=aUnit;
				}
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining()&& Marine4 !=Marine3)
				{
					Marine4=aUnit;
				}
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining()&& Marine5 !=Marine4)
				{
					Marine5=aUnit;
				}
				if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
				&& !aUnit.isTraining()&& Marine6 !=Marine5)
				{
					Marine6=aUnit;
				}
				
			}
		}		
		return new Unit[] {Marine1,Marine2,Marine3,Marine4,Marine5,Marine6};
	}
	private static List<Unit> Marines = new ArrayList<>();
	public static  void AttackUnitList()
	{
		Position choke = Mapping.chokePoint();
		
		for(Unit aUnit: Core.selbst().getUnits())
		{
			if(aUnit.isIdle() && (aUnit.getType() == UnitType.Terran_Marine) 
			&& !aUnit.isTraining() && !Marines.contains(aUnit))
			{
				Marines.add(aUnit);
				if(aUnit.getPosition().getDistance(choke.getX(), choke.getY())>=2)
					aUnit.move(choke);
				System.out.println(Marines);
					
			}
			if(aUnit.getHitPoints()<=0)
			{
				Marines.remove(aUnit);
			}
		
		}
	}
	
	public static void angreifen()
	{
		for(Unit aUnit : getMarines())
		{
			if(aUnit.canAttack() && !aUnit.isTraining() && aUnit.isIdle())
			{
				aUnit.attack(Mapping.enemyBase);
			}
		}
	}
	
	public static List<Unit> getMarines() {
		return Marines;
	}
	public static void setMarines(List<Unit> marines) {
		Marines = marines;
	}
}
