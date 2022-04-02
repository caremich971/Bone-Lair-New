//By Michael Carere and Griffin Shuit
import java.util.*;

/* Contains:
 *  main function
 *  roll dice function
 *  command parser
 *  array containing all rooms
 */
public class Main {
	public static Player p;
	public static Scanner s = new Scanner(System.in);
	public static HashMap<String, Room> rooms = new HashMap<String, Room>();
	
	public static void main(String[] args) {
		new Main().startAdventure();
	}
	
	public void startAdventure() {
		//create new a player; stats generated in constructor
		p = new Player();
		
		//create rooms
		int numRooms = 23; //this should be equal to the numerical id of the room with the highest numerical id; i.e. if the last room in the room constructor is room #23, this should be equal to 23. 
						  //having to set this manually sucks but it's easier than manually creating all the rooms
		for(int i = 1; i <= numRooms; i++) {
			Room r = new Room(i);
			rooms.put(r.id, r);
		}
		
		Room.enterRoom("start", "s");
	}
	
	//Dice Roller - used for stats, combat, skill checks, and basically anything which revolves around rng
	public static int rollDice(int d, int n) { //rolls n d-sided dice. e.g: if d = 6 and n = 2, it rolls 2 6-sided dice. rolling 1 dice makes all outcomes equally likely, while rolling multiple favours values towards the middle.
		int sum = 0;
		for(int i = 0; i < n; i++) {
			sum += (int)(Math.random()*d)+1;
		}
		return sum;
	}
	
	//used when we want the player to enter anything to continue. 
	public static void enterAnything() {
		System.out.print("(Enter anything to continue...)");
		String in = Main.s.nextLine();
		
		System.out.println();
	}
	
	//Command Parser
	public static String[] parseCommand(String text) {
		text = text.trim().toLowerCase();
		
		//Word replacement
		text = text.replaceAll("pick up ", "pickup");
		text = text.replaceAll("lookat ", "look");
		text = text.replaceAll(" over ", " across ");
		text = text.replaceAll("treasure chest", "chest");
		
		//Word removal
		String[] words = text.split(" ");
		String[] badWords = new String[] { //Filler words that we don't care about that someone might put in front of a command. "Move to the north" is the same command as "n" and "Go through the red door" is the same command as "red"
				"the",
				"to",
				"go",
				"through",
				"at",
				"in",
				"into",
				"move",
				"towards",
				"climb", //just use up and down
		};
		//Convert to an arraylist so we can remove items more easily
		ArrayList<String> wordList = new ArrayList<String>(Arrays.asList(words));	
		ArrayList<String> badWordList = new ArrayList<String>(Arrays.asList(badWords));
		
		for(int i = 0; i < wordList.size(); i++) {
			if(badWordList.contains(wordList.get(i))) wordList.remove(i--);
		}

		//add blank words so that commands with multiple parameters dont break if there aren't enough parameters
		while(wordList.size() < 5) {
			wordList.add("");
		}
		
		//Synonyms. This way, instead of having to check for 5 commands every time, it turns all 5 commands into the 1 that you check for.
		//Another nice thing is that since we're only writing this code once, it's a lot more practical to add more synonyms, which is nice.
		String[] t = new String[wordList.size()];
		t = wordList.toArray(t);
		
		for(int i = 0; i < t.length; i++) {
			switch(t[i]) {
				//directions
				case "north": case "northward": case "northwards":
					t[i] = "n";
					break;
				case "south": case "southward": case "southwards":
					t[i] = "s";
					break;
				case "east": case "eastward": case "eastwards": 
					t[i] = "e";
					break;
				case "west": case "westward": case "westwards":
					t[i] = "w";
					break;
				case "up": case "upward": case "upwards":
					t[i] = "u";
					break;
				case "down": case "downward": case "downwards":
					t[i] = "d";
					break;
				case "northwest": case "north-west": case "westnorth": case "west-north": case "wn": //accommodates psycopaths who type horizontal direction before vertical direction
					t[i] = "nw";
					break;
				case "northeast": case "north-east": case "eastnorth": case "east-north": case "en": 
					t[i] = "ne";
					break;
				case "southeast": case "south-east": case "eastsouth": case "east-south": case "es": 
					t[i] = "se";
					break;
				case "southwest": case "south-west": case "westsouth": case "west-south": case "ws": 
					t[i] = "sw";
					break;
					
				//other cmnds 
				case "inventory": case "inv": case "in":
					t[i] = "i";
					break;
				case "attack": case "atk": case "hit": case "smash": case "destroy": case "obliterate": case "eviscerate":
					t[i] = "a";
					break;
				case "grab": case "take": case "pickup":
					t[i] = "t";
					break;
				case "back": case "exit":
					t[i] = "b";
					break;
				case "help": case "commands": case "cmd": case "command": case "info": case "cmds":
					t[i] = "h";
					break;
				case "put":
					t[i] = "use"; 
					break;
				case "examine": case "look": case "read":
					t[i] = "x";
					break;
				case "open":
					t[i] = "o";
					break;
				case "quit":
					t[i] = "q";
					break;
					
				//other words
				case "over":
					t[i] = "across";
					break;
			}
		}
		
		//Return a string array. We can't do the commands here because some commands can only be done in certain places, some places have specific commands, etc.
		return t;
	}
}