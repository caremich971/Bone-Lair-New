
public class Battler { 
	/* Contains:
	 *  Battle function
	 *  Battle commands
	 *  Battle text
	 */
	
	//Combat is a bit boring. If we have time, maybe improve it?
	public static void battle(Player p, int enemy) {
		Enemy e = new Enemy(enemy);
		int turn = 1;
		
		//strength and weapon strength increase damage, but target's defense reduces. for enemies, strength and weapons are unnecessary so they just have "damage"
		int pDamage = p.strength + p.wep.damage - e.defense;
		int eDamage = e.damage - p.armour.defense;
		
		//calculate the effect that armour has on speed. enemies dont have armour so they use their base speed
		int attackSpeed = p.speed - p.armour.encumbrance;

		//skill and weapon hit increase hit rate, but target's speed reduces. for enemies, skill and weapons are unnecessary so they just have "hit". enemies do have speed, however, because it's used for turn order
		int pHit = p.wep.hit + p.skill*3 - e.speed*3;
		int eHit = e.hit - attackSpeed*3;
		
		//luck and skill slightly increase crit chance, while target's luck slightly decreases. enemies don't have skill but this can be factored into their crit rate
		int pCrit = p.wep.crit + p.skill/3 + p.luck/2 - e.luck/2 + 3; //crits do 1.5x damage and ignore defense
		int eCrit = e.crit + e.luck/2 - p.luck/2 + 3;
		
		//solid hits and grazes are how I decided to do damage variance. it lets you do more interesting hit text and makes it easier to tell how much damage you can do
		int pSolid = 0;
		if (pHit >= 50) pSolid = pCrit + 20; //solid hits do 1.25x damage. has a 20% chance, unless crit < 0 in which case it will be lower. if hit is too low then it will be a 0% chance instead
		
		int eSolid = 0;
		if (eHit >= 50) eSolid = eCrit + 20;
		
		int pGraze = pHit - 20; //grazes do 0.8x damage. has a 20% chance, unless hit > 100 in which case it will be lower
		int eGraze = eHit - 20;
		
		//negative dmg is necessary for crit calculation but it should be displayed as 0
		int pDmgDisplay = pDamage;
		if(pDmgDisplay < 0) pDmgDisplay = 0;
		
		int eDmgDisplay = eDamage;
		if(eDmgDisplay < 0) eDmgDisplay = 0;
		
		//cap crit and hit rate at 0 or 100
		if(pCrit < 0) pCrit = 0;
		if(eCrit < 0) eCrit = 0;
		
		if(pCrit > 100) pCrit = 100;
		if(eCrit > 100) eCrit = 100;
		
		if(pHit > 100) pHit = 100;
		if(eHit > 100) eHit = 100;
		
		if(pHit < 0) pHit = 0;
		if(eHit < 0) eHit = 0;
		
		//actual battle
		while(e.HP > 0 & p.curHP > 0) {
			//print out the battle menu
			if(turn == 1) {
				System.out.printf("%n%s %s%n--------------------%nYou:%n%d/%d HP%n%d DMG%n%d HIT%n%d CRIT%n%n vs. %n%n%s:%n%d/%d HP%n%d DMG%n%d HIT%n%d CRIT%n%nWhat will you do? %n%n > ", e.name, battleText(0, e.name), p.curHP, p.maxHP, pDmgDisplay, pHit, pCrit, e.name, e.HP, e.maxHP, eDmgDisplay, eHit, eCrit);
			} else {

				System.out.printf("%n%s %s%n--------------------%nYou:%n%d/%d HP%n%d DMG%n%d HIT%n%d CRIT%n%n vs. %n%n%s:%n%d/%d HP%n%d DMG%n%d HIT%n%d CRIT%n%nWhat will you do? %n%n > ", e.name, battleText(11, e.name), p.curHP, p.maxHP, pDmgDisplay, pHit, pCrit, e.name, e.HP, e.maxHP, eDmgDisplay, eHit, eCrit);
			}
			//get input and execute the option selected
			String[] in = Main.parseCommand(Main.s.nextLine().trim().toLowerCase());
			
			switch(in[0]) {
				default:
					System.out.println("\n(Invalid input; try again)\n");
					continue;
				case "a": //Attack
					System.out.println();
					
					//deal damage; higher speed goes first. in case of a tie will always go to the player
					if(attackSpeed >= e.speed) { //player goes first
						int d = Main.rollDice(100, 1); //damage roll
						
						//checks the damage roll against the player's damage rates to determine damage
						int dmgDealt = 0;
						if(d > pHit) { //miss
							System.out.print(battleText(9, e.name));
						} else if(d > pGraze) { //graze
							dmgDealt = (int)(pDamage*0.8);
							System.out.print(battleText(2, e.name));
						} else if(d > pSolid & pSolid != 0 | d > pCrit & pSolid == 0) { //normal hit
							dmgDealt = pDamage;
							System.out.print(battleText(1, e.name));
						} else if(d > pCrit) { //strong hit
							dmgDealt = (int)(pDamage*1.25);
							System.out.print(battleText(3, e.name));
						} else { //crit
							dmgDealt = (pDamage+e.defense)*2;
							System.out.print(battleText(4, e.name));
						}
						if(dmgDealt < 0) dmgDealt = 0;
						
						System.out.println(" " + dmgDealt + " damage!\n");
						
						e.HP -= dmgDealt;
						if(e.HP <= 0) {
							break;
						}
						
						//do above, but for enemy
						d = Main.rollDice(100, 1);
						dmgDealt = 0;
						
						if(d > eHit) { //miss
							System.out.print(battleText(10, e.name));
						} else if(d > eGraze) { //graze
							dmgDealt = (int)(eDamage*0.8);
							System.out.print(battleText(6, e.name));
						} else if(d > eSolid & eSolid != 0 | d > eCrit & eSolid == 0) { //normal hit
							dmgDealt = eDamage;
							System.out.print(battleText(5, e.name));
						} else if(d > eCrit) { //strong hit
							dmgDealt = (int)(eDamage*1.25);
							System.out.print(battleText(7, e.name));
						} else { //crit
							dmgDealt = (eDamage+p.armour.defense)*2;
							System.out.print(battleText(8, e.name));
						}
						if(dmgDealt < 0) dmgDealt = 0;
						
						System.out.println(" " + dmgDealt + " damage!\n");
						
						p.curHP -= dmgDealt;
						
					} else { //enemy goes first
						//does the enemy's damage first...
						int d = Main.rollDice(100, 1);
						int dmgDealt = 0;
						if(d > eHit) { //miss
							System.out.print(battleText(10, e.name));
						} else if(d > eGraze) { //graze
							dmgDealt = (int)(eDamage*0.8);
							System.out.print(battleText(6, e.name));
						} else if(d > eSolid & eSolid != 0 | d > eCrit & eSolid == 0) { //normal hit
							dmgDealt = eDamage;
							System.out.print(battleText(5, e.name));
						} else if(d > eCrit) { //strong hit
							dmgDealt = (int)(eDamage*1.25);
							System.out.print(battleText(7, e.name));
						} else { //crit
							dmgDealt = (int)((eDamage+p.armour.defense)*2);
							System.out.print(battleText(8, e.name));
						}
						if(dmgDealt < 0) dmgDealt = 0;
						
						System.out.println(" " + dmgDealt + " damage!\n");
						p.curHP -= dmgDealt;
						p.checkHP();
						
						//then the player's
						d = Main.rollDice(100, 1);
						dmgDealt = 0;
						if(d > pHit) { //miss
							System.out.print(battleText(9, e.name));
						} else if(d > pGraze) { //graze
							dmgDealt = (int)(pDamage*0.8);
							System.out.print(battleText(2, e.name));
						} else if(d > pSolid & pSolid != 0 | d > pCrit & pSolid == 0) { //normal hit
							dmgDealt = pDamage;
							System.out.print(battleText(1, e.name));
						} else if(d > pCrit) { //strong hit
							dmgDealt = (int)(pDamage*1.25);
							System.out.print(battleText(3, e.name));
						} else { //crit
							dmgDealt = (int)((pDamage+e.defense)*2);
							System.out.print(battleText(4, e.name));
						}
						if(dmgDealt < 0) dmgDealt = 0;
						
						System.out.println(" " + dmgDealt + " damage!\n");
						e.HP -= dmgDealt;
					}
					break;
			}
			turn++;
		}
		p.checkHP();
		System.out.println("You defeated " + e.name + "!\n");
		
		//item drops
		if(e.drops != null) {
			for(Item i : e.drops) {
				System.out.println("You got the " + i.name + "!");
				p.addToInv(i);
			}
		}
		
		Main.enterAnything();
	}
	
	//Randomly selects a text based on an ID. Used for battles
	public static String battleText(int type, String target) {
		String[] t;
		switch(type) {
			default:
				t = new String[] { "Text not found!" };
				break;
			case 0: //Enemy approaches
				t = new String[] { 
					"appears!",
					"appears!",
					"approaches!",
					"approaches!",
					"jumps out!",
					"jumps out!",
					"drops in!",
					"drops in!",
					"crashes through the ceiling!",
					"waltzes in!",
					"arrives, like a sudden windstorm at a kindergarten picnic!",
					"just noticed you're here!",
				};
				break;
			case 11: //Enemy in-battle
				t = new String[] { 
					"quivers with anticipation!",
					"quivers with anticipation!",
					"glares angrily!",
					"glares angrily!",
					"is just standing there! Menacingly!",
					"bobs up and down idly!",
					"mocks your poor combat skills!",
					"is a bit bored!",
					"cooes seductively!",
					"screams incoherently!",
					"is shaking violently!",
					"does a fabulous dance!",
					"jives mockingly!",
					"makes an odd squeaking noise!",
					"is French!",
				};
				break;
			case 1: //Player hits
				t = new String[] {
					"You attack " + target + "!",
					"You strike " + target + "!",
					"You hit " + target + "!",
				};
				break;
			case 2: //Player grazes
				t = new String[] {
					"Your attack grazes " + target + "!",
					"Your attack grazes " + target + "!",
					"You nip " + target + "!",
					"You technically didn't miss " + target + "!",
				};
				break;
			case 3: //Player lands a solid hit
				t = new String[] {
					"You squarely hit " + target + "!",
					"You land a solid hit on " + target + "!",
					"You land an expert strike on " + target + "!",
					"You bonk " + target + " on the head!",
					"You do an epic spin and then strike " + target + "!",
					"You hit " + target + " while they're turned away!",
				};
				break;
			case 4: //Player crits
				t = new String[] {
					"You slam " + target + " with a critical hit!",
					"You smash " + target + " with a critical hit!",
					"You disembowel " + target + " with a critical hit!",
					"You break " + target + "'s kneecaps with a critical hit!",
					"You obliterate " + target + " with a well-deserved critical hit!",
				};
				break;
			case 5: //Enemy hits
				t = new String[] {
					target + " attacks!",
				};
				break;
			case 6: //Enemy grazes
				t = new String[] {
					target + " attacks, but almost misses!",
					target + " attacks, but almost misses!",
					target + " grazes your arm!",
					target + " grazes your arm!",
					target + " kisses you softly!",
				};
				break;
			case 7: //Enemy lands a solid hit
				t = new String[] {
					target + " hits you squarely!",
					target + " lands a solid blow!",
				};
				break;
			case 8: //Enemy crits
				t = new String[] {
					target + " delivers a bone-shattering critical hit!",
					target + " delivers a bone-shattering critical hit!",
					target + " obliterates your genitals with a crit!",
					target + " lands an unfair critical hit!",
				};
				break;
			case 9: //Player misses
				t = new String[] {
					"Your attack misses " + target + "!",
					"Your attack misses " + target + "!",
					"Your attack misses " + target + "!",
					"You attack, but " + target + " dodges!",
					"You attack, but " + target + " dodges!",
					"You attack, but " + target + " dodges!",
					"You trip on a rock and miss embarassingly!",
					"You drop your weapon. Oops!",
					"Your attack hits the wall. Completely on purpose, of course!",
					"You have the aim of a Stormtrooper!",
					"You expertly strike to the left of " + target + "!",
				};
				break;
			case 10: //Enemy misses
				t = new String[] {
					target + "'s attack misses!",
					target + "'s attack misses!",
				};
				break;
		}
		return t[(int)(Math.random()*t.length)];	
	}
}
