
public class Item {
	/* Contains:
	 *  Constructor containing the stats of all non-weapon or armour items
	 *  Use effects of all items
	 */
	String name;
	String shorthand = null; //displays in inventory for items with names longer than 15 characters, or 12 for weapons and armour
	String description = "";
	
	//keyword booleans
	boolean edible = false;
	boolean drinkable = false;
	
	public Item(int id) {
		switch(id) {
			default:
				name = "Missing Number";
				shorthand = "Missing No.";
				description = "Item not found!";
				break;
			case -1: //for weapons and armour
				return;
			case 0:
				name = "Bone-Healing Juice";
				shorthand = "BoneHealJuice";
				description = "A small vial of calcium-rich fluid. Heals 15 health.";
				drinkable = true;
				break;
			case 1:
				name = "Bone First Aid";
				shorthand = "Boned-Aid";
				description = "For when you're really boned. Heals 30 health.";
				break;
			case 2:
				name = "Tough Steak";
				description = "Probably should've cooked this. Heals 10 health, but also might do something else.";
				edible = true;
				break;
			case 3:
				name = "Red Potion";
				description = "A red potion. Smells spicy.";
				drinkable = true;
				break;
			case 4:
				name = "Honeycomb";
				description = "Full of delicious honey.";
				edible = true;
				drinkable = true;
				break;
			case 5:
				name = "Green Potion";
				description = "A green potion. Smells like almonds.";
				drinkable = true;
				break;
			case 6:
				name = "Blue Potion";
				description = "A blue potion. Smells... puzzling.";
				drinkable = true;
				break;
			case 7:
				name = "Fast Clock";
				description = "A strange clock. The hands are moving far too quickly.";
				break;
			case 8:
				name = "Golden Omlette";
				description = "It fell on the floor, but it's probably still good.";
				edible = true;
				break;
			case 9:
				name = "Ambrosia";
				description = "The drink of the gods. +5 to all stats.";
				drinkable = true;
				edible = true;
				break;
			case 10:
				name = "Green Ambrosia";
				description = "I don't think you should put this in your mouth.";
				drinkable = true;
				edible = true;
				break;
			case 11:
				name = "Bone";
				description = "It looks like a femur.";
				break;
			case 12:
				name = "Fighting Ring";
				description = "Gives you fighting spirit. +1 STR while in your inventory.";
				break;
			case 13:
				name = "Ornate Key";
				description = "A fancy-looking key. It's ticking softly.";
				break;
			case 14:
				name = "Rope";
				description = "A very long rope.";
				break;
			case 15:
				name = "Champion's Belt";
				shorthand = "Champ Belt";
				description = "A comically large belt. +2 STR and SKL while in your inventory.";
				break;
			case 16:
				name = "Empty Flask";
				description = "An empty flask. Can be filled with liquid.";
				break;
			case 17:
				name = "Bone-Hurting Juice";
				shorthand = "BoneHurtJuice";
				description = "Ouch oof my bones";
				drinkable = true;
				edible = true;
				break;
		}
		
		if(shorthand == null) {
			shorthand = name;
		}
	}
	
	//Use item. These are booleans so that they don't use a turn in combat if nothing happens
	public boolean use(Player user, int index, Room room) { 
		switch(name) {
			case "Bone-Healing Juice":
				if(user.curHP != user.maxHP) {
					int restore = 15;
					if(user.curHP + restore > user.maxHP) {
						restore = user.maxHP - user.curHP;
					}
					user.curHP += restore;
					System.out.println("You drink the juice, and your bones are repaired. " + restore + " HP was restored.");
					user.inventory.set(index, new Item(16));
					return true;
				} else {
					System.out.println("You're at full health.");
					return false;
				}
			case "Bone First Aid":
				if(user.curHP != user.maxHP) {
					int restore = 30;
					if(user.curHP + restore > user.maxHP) {
						restore = user.maxHP - user.curHP;
					}
					user.curHP += restore;
					System.out.println("A small skeleton pops out of the kit and aids your bones. " + restore + " HP was restored.");
					user.inventory.remove(index);
					return true;
				} else {
					System.out.println("You're at full health.");
					return false;
				}
		}
		return false;
	}
	
	//item-specific synonyms
	public boolean eat(Player user, int index, Room room) {
		if(edible) {
			return use(user, index, new Room(0));
		} else {
			System.out.println("Probably wouldn't go down well...");
			return false;
		}
	}

	public boolean drink(Player user, int index, Room room) {
		if(drinkable) {
			return use(user, index, new Room(0));
		} else {
			System.out.println("Probably wouldn't go down well...");
			return false;
		}
	}
	
	//equip weapon or armour. does nothing for normal items
	public boolean equip(Player user, int index, Room room) { 
		System.out.println("You can only equip weapons and armour.");
		return false;
	}
	
	//awful way to load items from the inventory.
	//normally, when you try to set an armour or weapon to an item, it doesn't let you, because not all items are armour or weapons - even if you know that the item is an armour or weapon.
	//the easiest solution is to have these functions on all items that return the item as an extended class, but only if it is that class
	//i hate it here
	public Weapon toWep() {
		return null;
	}

	public Armour toArmour() {
		return null;
	}
}
