import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BlackjackGame {
    private List<Card> deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private int playerWins = 0;
    private int dealerWins = 0;
    private int playerLosses = 0;
    private int dealerLosses = 0;
    private static final int TARGET_SCORE = 21;

    public BlackjackGame() {
        deck = new ArrayList<>();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        initializeDeck();
    }
    //初始化撲克牌
    private void initializeDeck() {
        String[] suits = {"spade", "heart", "diamond", "club"};
        for (String suit : suits) {
            for (int i = 1; i <= 13; i++) {
                String number;
                switch (i) {
                    case 1:
                        number = "A";
                        break;
                    case 11:
                        number = "J";
                        break;
                    case 12:
                        number = "Q";
                        break;
                    case 13:
                        number = "K";
                        break;
                    default:
                        number = String.valueOf(i);
                        break;
                }
                deck.add(new Card(number, suit));
            }
        }
    }
    //洗牌
    private void shuffleDeck() {
        Random rand = new Random();
        for (int i = 0; i < deck.size(); i++) {
            int randomIndex = rand.nextInt(deck.size());
            Card temp = deck.get(i);
            deck.set(i, deck.get(randomIndex));
            deck.set(randomIndex, temp);
        }
    }
    //牌局初始畫
    private void dealInitialCards() {
        for (int i = 0; i < 2; i++) {
            playerHand.add(drawCard());
            dealerHand.add(drawCard());
        }
    }
    //發牌
    private Card drawCard() {
        if (deck.isEmpty()) {
            System.out.println("Deck is empty. Reshuffling...");
            shuffleDeck();
        }
        return deck.remove(0);
    }
    //計算點數
    private int calculateHandValue(List<Card> hand) {
        int value = 0;
        boolean hasAce = false;
        for (Card card : hand) {
            if (card.getNumber().equals("A")) {
                hasAce = true;
            }
            value += card.getValue();
        }
        if (hasAce && value + 10 <= TARGET_SCORE) {
            value += 10; // Ace counts as 11
        }
        return value;
    }
    //發手牌
    private void printHand(List<Card> hand, boolean hideFirstCard) {
        for (int i = 0; i < hand.size(); i++) {
            if (i == 0 && hideFirstCard) {
                System.out.println("Hidden Card");
            } else {
                System.out.println(hand.get(i).getFourSuits() + " " + hand.get(i).getNumber());
            }
        }
    }
    //判斷是否爆點
    private boolean isBusted(int handValue) {
        return handValue > TARGET_SCORE;
    }

    public void playGame() {
        shuffleDeck();
        dealInitialCards();

        Scanner scanner = new Scanner(System.in);

        // Show initial hands
        System.out.println("玩家手牌:");
        printHand(playerHand, false);
        System.out.println("電腦手牌:");
        printHand(dealerHand, true);

        // Player's turn
        while (true) {
            System.out.println("玩家是否還要牌? (Y/N)");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y")) {
                playerHand.add(drawCard());
                System.out.println("玩家手牌:");
                printHand(playerHand, false);
                int playerHandValue = calculateHandValue(playerHand);
                if (isBusted(playerHandValue)) {
                    System.out.println("玩家爆了，嫩逼!");
                    playerLosses++;
                    dealerWins++;
                    return;
                }
            } else if (choice.equals("n")) {
                break;
            } else {
                System.out.println("請確保你輸入了 'Y' 或 'N'，蠢貨!!");
            }
        }

        // Dealer's turn
        System.out.println("電腦回合:");
        System.out.println("電腦手牌:");
        printHand(dealerHand, false);
        while (calculateHandValue(dealerHand) < 17) {
            dealerHand.add(drawCard());
            System.out.println("電腦要牌.");
            System.out.println("電腦手牌:");
            printHand(dealerHand, false);
            if (isBusted(calculateHandValue(dealerHand))) {
                System.out.println("皮老闆，你老婆爆了!");
                dealerLosses++;
                playerWins++;
                return;
            }
        }

        // Compare hands
        int playerHandValue = calculateHandValue(playerHand);
        int dealerHandValue = calculateHandValue(dealerHand);
        System.out.println("玩家手牌總和: " + playerHandValue);
        System.out.println("電腦手牌總和: " + dealerHandValue);
        if (playerHandValue > dealerHandValue) {
            System.out.println("玩家贏了!");
            playerWins++;
            dealerLosses++;
        } else if (playerHandValue < dealerHandValue||dealerHand.size()==5) {
            System.out.println("電腦贏了!");
            dealerWins++;
            playerLosses++;
        } else if(playerHandValue == dealerHandValue){
                if(dealerHandValue==21){
                    System.out.println("電腦贏了!");
                    dealerWins++;
                    playerLosses++;
                } else {
                    System.out.println("宣布平手!");
                }
        }   
    }
    
    public static void main(String[] args) {
        int allPlayerWins = 0;
        int allDealerWins = 0;
        int allPlayerLosses = 0;
        int allDealerLosses = 0;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            BlackjackGame game = new BlackjackGame();
            game.playGame();

            // 更新總勝敗次數
            allPlayerWins += game.playerWins;
            allDealerWins += game.dealerWins;
            allPlayerLosses += game.playerLosses;
            allDealerLosses += game.dealerLosses;

            System.out.println("玩家勝利次數: " + allPlayerWins + ", 輸掉次數: " + allPlayerLosses);
            System.out.println("電腦勝利次數: " + allDealerWins + ", 輸掉次數: " + allDealerLosses);
            System.out.println("是否繼續遊戲? (Y/N)");
            String playAgain = scanner.nextLine().trim().toLowerCase();

            if (playAgain.equals("n")) {
                break;
            }
        }
        System.out.println("遊戲結束，謝謝遊玩!");
        scanner.close();
    }
}

class Card {
    private String number;
    private String fourSuits;

    public Card(String number, String fourSuits) {
        this.number = number;
        this.fourSuits = fourSuits;
    }

    public String getNumber() {
        return number;
    }

    public String getFourSuits() {
        return fourSuits;
    }

    public int getValue() {
        if (number.equals("A")) {
            return 1;
        } else if (number.equals("J") || number.equals("Q") || number.equals("K")) {
            return 10;
        } else {
            return Integer.parseInt(number);
        }
    }
}
