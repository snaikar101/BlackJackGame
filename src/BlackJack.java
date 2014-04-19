import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
//import java.util.HashMap;


public class BlackJack {
	
	public static void main(String[] args) {
		ArrayList<Card> deckOfCards = new ArrayList<Card>(); //Array of Cards
		BlackJack.intializeCards(deckOfCards);               //Using 2 decks to play     
		BlackJack.intializeCards(deckOfCards);
		int userTotalChips = 100;							//Intial number of chips with user
		Scanner sc = new Scanner(System.in);
		Random randomGenerator = new Random();            
		ArrayList<Card> dealerCards = new ArrayList<Card>(); //Array of cards for dealer 
		ArrayList<Card> playerCards = new ArrayList<Card>(); //Array of cards for Player 
		while(true){
			playerCards.clear();
			dealerCards.clear();
			System.out.println("Current Chips: "+userTotalChips);
			if(userTotalChips == 0){
				break;
			}
			int bet = BlackJack.getBet(sc);
			if(bet == 0){
				System.out.println("Invalid entry!");
				continue;
			}
			else if(bet<1){
				break;
			}
			else if(bet > userTotalChips){
				System.out.println("Enter bet less than "+userTotalChips);
				continue;
			}
			//Gives dealer and user gets 2 cards each
			dealerCards.add(BlackJack.getCard(deckOfCards, randomGenerator));
			dealerCards.add(BlackJack.getCard(deckOfCards, randomGenerator));
			playerCards.add(BlackJack.getCard(deckOfCards, randomGenerator));
			playerCards.add(BlackJack.getCard(deckOfCards, randomGenerator));
			System.out.print("dealers Cards:");
			BlackJack.displayDelarCards(dealerCards);
			
			int bestScore=BlackJack.getBestScore(playerCards),leastScore;
			int dBestScore=BlackJack.getBestScore(dealerCards);
			boolean busted=false;
			if(bestScore==21){
				System.out.print("Your Cards:");
				BlackJack.displayAllCards(playerCards);
				System.out.println("BlackJack!!!");
				userTotalChips += bet; 
				continue;
			}
			while(true){
				System.out.print("Your Cards:");
				BlackJack.displayAllCards(playerCards);
				bestScore = BlackJack.getBestScore(playerCards);
				leastScore = BlackJack.getLeastScore(playerCards);
				System.out.println("Best score: "+bestScore+" Least score: "+leastScore);
				if(leastScore>21){
					busted=true;
					System.out.println("Busted!!");
					userTotalChips -= bet; 
					break;
				}
				boolean hit = BlackJack.getHit(sc);
				if(!hit)
					break;
				playerCards.add(BlackJack.getCard(deckOfCards, randomGenerator));
				
			}
			if(busted)
				continue;
			while(dBestScore<17){
				dealerCards.add(BlackJack.getCard(deckOfCards, randomGenerator));
				dBestScore=BlackJack.getBestScore(dealerCards);
			}
			System.out.print("dealers Cards:");
			BlackJack.displayAllCards(dealerCards);
			System.out.println("Dealers Score:"+dBestScore);
			if(dBestScore>21)
			{
				System.out.println("Dealer Busted!!");
				userTotalChips += bet;
			}
			else if(dBestScore>bestScore){
				System.out.println("Dealer Wins!!");
				userTotalChips -= bet;
			}
			else if(dBestScore<bestScore){
				System.out.println("You Win!!");
				userTotalChips += bet;
			}
			else{
				System.out.println("Draw");
			}		
		}
		System.out.println("Thanks for palying Your final chips: " +userTotalChips);
		
		
	}
	//Asks user whether to hit or stand
	private static boolean getHit(Scanner sc) {
		while (true){
		System.out.println("Enter hit or Enter Stand\n");
		String entered = sc.next();
		if(entered.equalsIgnoreCase("hit")){
			return true;
		}
		else if(entered.equalsIgnoreCase("stand")){
			return false;
		}
		System.out.println("Invalid Entry!");
		}
	}
	//Initializes Array with one deck of 52 cards
	static void intializeCards (ArrayList<Card> deckOfCards){
		String[] vals = {"A","K","Q","J","2","3","4","5","6","7","8","9","10"};
		String [] suits = {"Hearts","Spade","Daimond","Clubs"};
		for(String suit : suits){
			for(String val : vals){
			 Card c = new Card(suit,val);
			 deckOfCards.add(c);	
			}
		}
	}
	//Gets the best possible score under 21.
	//Once the least score crosses 21 best becomes equal to least
	static int getBestScore(ArrayList<Card> cards){
		int value=0;
		int noa=0;
		for(Card card : cards){
			if(card.getCardValue()==1){
				value +=11;
				noa += 1;
			}
			else
				value += card.getCardValue();
		}
		while(value>21 && noa>0){
			value = value - 10;
			noa = noa -1;
		}
		return value;
	}
	//gets the least possible score for a given array of cards 
	//A is considered as 1
	static int getLeastScore(ArrayList<Card> cards){
		int value=0;
		for(Card card : cards){
			value += card.getCardValue();
		}
		return value;
	}
	//Gets a random Card from the deck and removes it . Once the Deck is empty the cards are reshuffled 
	static Card getCard(ArrayList<Card> deckOfCards,Random randomGenerator){
		if(deckOfCards.size()==0){
			System.out.println("Reshuffeling...");
			BlackJack.intializeCards(deckOfCards);               //Using 2 decks to play     
			BlackJack.intializeCards(deckOfCards);
		}
		int cardId = randomGenerator.nextInt(deckOfCards.size());
		Card card = deckOfCards.get(cardId);
		deckOfCards.set(cardId, deckOfCards.get(deckOfCards.size()-1));
		deckOfCards.remove(deckOfCards.size()-1);
		return card;
	}
	//Dispalys all cards suits and value
	static void displayAllCards (ArrayList<Card> cards){
		for(Card card:cards){
			card.displayCard();			
		}
		System.out.println("");
	}
	//Used to display Delar initial Cards so that the first card is displayed X X
	static void displayDelarCards (ArrayList<Card> cards){
		System.out.print("X X ");
		cards.get(1).displayCard();			
		
		System.out.println("");
	}
	//Returns the bet entered by palyer
	static int getBet(Scanner sc){
		
		System.out.println("Enter the bet money to continue or enter -1 to quit");
		int bet;
		try{
			bet = sc.nextInt();
			
		}
		catch(InputMismatchException e){
			sc.next();
			return 0;
		}
		return bet;
	}
	
}

class Card {
	private String suite;
	private String c_val; //String value like "A" or "10"
	private int val; // Actual Integer Value "A"-->11 "K"-->10 etc
	//Initializes card
	Card(String suite,String c_val){
		this.suite=suite;
		this.c_val= c_val;
		if(Card.isInteger(c_val)){
			this.val= Integer.parseInt(c_val);
		}
		else if(c_val.equals("A")){
			this.val=1;
		}
		else{
			this.val = 10;
		}
		
	}
	//Returns true if passed string is a Integer
	public static boolean isInteger(String s) {
	   try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	   
	    return true;
	}
	//Prints Suite and value of card
	public void displayCard(){
		System.out.print(this.suite +" "+this.c_val +" ");
	}
	public int getCardValue(){
		return this.val;
	}
	
}
