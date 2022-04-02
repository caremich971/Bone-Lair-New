
public class Weapon extends Item { //Extends Item, so we can put it in ArrayLists of Items, i.e. the player's inventory
	/* Contains:
	 *  Constructor containing the stats of all weapons
	 */
	int damage;
	int hit;
	int crit;
	
	//Weapon constructor. I like to use an ID and a switch because it's easier to use and read compared to a constructor with a bunch of variables that you never remember
	public Weapon(int id) {
		super(-1);
		switch(id) {
			case 0:
				name = "Rusty Sword";
				damage = 1;
				hit = 85;
				crit = 0;
				break;
			case 1:
				name = "Rusty Dagger";
				damage = 0;
				hit = 90;
				crit = 5;
				break;
			case 2:
				name = "Rusty Axe";
				damage = 5;
				hit = 70;
				crit = 5;
				break;
			case 3:
				name = "Squeaky Hammer";
				shorthand = "Squeaky Ham.";
				damage = 2;
				hit = 85;
				crit = -5;
				break;
			case 4:
				name = "Warrior's Axe";
				shorthand = "Warrior Axe";
				damage = 8;
				hit = 70;
				crit = 5;
				break;
			case 5:
				name = "Scorpion";
				damage = 2;
				hit = 80;
				crit = 20;
				break;
			case 6:
				name = "Steel Broadsword";
				shorthand = "Steel Sword";
				damage = 4;
				hit = 85;
				crit = 0;
				break;
			case 7:
				name = "Sharpened Dagger";
				shorthand = "Sharp Dagger";
				damage = 2;
				hit = 90;
				crit = 7;
				break;
			case 8:
				name = "Paladin's Hammer";
				shorthand = "Pal Hammer";
				damage = 5;
				hit = 85;
				crit = -2;
				break;
			case 9:
				name = "Torch";
				description = "Doesn't seem to go out. Lights up a dark area. \n(This is a weapon and will only work if equipped!)";
				damage = 2;
				hit = 80;
				crit = 0;
				break;
			case -1:
				name = "Bone of Never-Ending Rage";
				shorthand = "B.O.N.E.R.";
				description = "It's hard.";
				damage = 0;
				hit = 70;
				crit = 50;
				break;
			case -2:
				name = "Console";
				description = "kill @e[type=enemy]";
				damage = 255;
				hit = 255;
				crit = 255;
				break;
		}
		
		if(shorthand == null) {
			shorthand = name;
		}
	}

	//override use and equip
	public boolean equip(Player user, int index, Room room) {
		user.inventory.set(index, user.wep);
		user.wep = this;
		System.out.println("Equipped the " + name + ".");
		return true;
	}
	
	public boolean use(Player user, int index, Room room) {
		return equip(user, index, room);
	}
	
	//overrides from Item so we can load armour from inventory
	public Weapon toWep() {
		return this;
	}
}
