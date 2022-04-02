
public class Enemy {
	/* Contains:
	 *  Constructor containing the stats of all enemies
	 */
	String name;
	int damage;
	int HP;
	int hit;
	int speed;
	int luck;
	int defense;
	int crit;
	int maxHP;
	
	Item[] drops = null;

	//Enemy constructor. I like to use an ID and a switch because it's easier to use and read compared to a constructor with a bunch of variables that you never remember
	public Enemy(int id) {
		switch(id) {
			default:
			case 0:
				name = "BONSEY"; //All caps for stylization
				maxHP = 30;
				HP = maxHP;
				damage = 6;
				hit = 82;
				speed = 5;
				luck = 2;
				defense = 0;
				crit = 1;
				drops = new Item[] { new Item(0) };
				break;
				
			case 1:
				name = "WIZARD"; //All caps for stylization
				maxHP = 26;
				HP = maxHP;
				damage = 10;
				hit = 68;
				speed = 7;
				luck = 5;
				defense = 0;
				crit = 1;
				drops = new Item[] { new Item(16) };
				break;
		}
	}
}