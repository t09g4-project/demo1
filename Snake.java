import java.awt.*;
import java.util.LinkedList;
import java.util.Scanner;


public class Snake {
   static final int WIDTH = 40, HEIGHT = 10;
   static char[][] map = new char[HEIGHT][WIDTH];
   //make a char array for the game map
   public static void main(String[] args) {
      Snake snake = new Snake();
      snake.iBackground();
      Snakebody snakebody = new Snakebody();
      snakebody.initSnake();
      snake.putSnakeInMap(snakebody);
      snake.show();

      Scanner scanner = new Scanner(System.in);
      int move;
      while (true) {
         System.out.println("Use AWSD to move the snake, Press Q to quit the game");
         String choice = scanner.next();
         switch (choice.toLowerCase()) {
            case "a":
               move = 2;
               break;
            case "s":
               move = 1;
               break;
            case "w":
               move = 3;
               break;
            case "d":
               move = 0;
               break;
            case "q":
               int points=snakebody.snakePoints.size();
               snake.putGameOverInMap(points);
            default:
               System.out.println("Input Error, Please try again");
               continue;
         }
         //set a loop for snake to move by using AWSD. 
         if (snakebody.move(move) == -1) {
            snake.putGameOverInMap(snakebody.snakePoints.size());
            snake.show();
            break;
            //when snake collide with body or the border of the game, GameOver will be put in the map.
         }
         snake.putSnakeInMap(snakebody);
         snake.show();
         //put snake into the game
      }
   }
   // build a 2D array to represent the background of the map
   private void iBackground() {
      for (int i = 0; i < HEIGHT; i++) {
         for (int j = 0; j < WIDTH; j++) {
            this.map[i][j] = (j == 0 || (j == WIDTH - 1) || i == 0 || (i == HEIGHT - 1)) ? '*' : ' ';
            // if the boolean return true, this.map[i][j] will equal to "*", which represent the wall in the game
            // if the boolean return false, this.map[i][j] will equal to " ", wich represent the space area in the game
         }
      }
   }
   // print out the map according to the 2D array
   public void show() {
      int height = map.length;
      int width = map[0].length;
      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j++) {
            System.out.print(map[i][j]);
         }
         System.out.println();
      }
   }
   // use the element of snakeLine to replace the element of map array
   void putSnakeInMap(Snakebody snakebody) {
      Point p;
      this.iBackground();
      map[Snakebody.food.y][Snakebody.food.x] = Snakebody.worm;
      for (int i = 0; i < snakebody.snakePoints.size(); i++) {
         p = snakebody.snakePoints.get(i);
         if (p.y > 0 && p.y < HEIGHT - 1 && p.x > 0 && p.x < WIDTH - 1) {
            map[p.y][p.x] = (i == 0) ? snakebody.head : snakebody.body;
         } else {
            putGameOverInMap(snakebody.snakePoints.size());
         }
      }
   }
   // print Gameover in map if the snake die
   //the original length of the snake is 3, so the mark = snake length - 3
   void putGameOverInMap(int points) {
      char[] gameOver = ("GameOver Score:"+(points-3)).toCharArray();
      for (int i = 0; i < gameOver.length; i++) {
         map[HEIGHT / 2 - 1][i + (WIDTH - gameOver.length) / 2] = gameOver[i];
      }
      show();
      System.exit(1);
   }
}
class Snakebody {
   static final int RIGHT = 0, DOWN = 1, LEFT = 2, UP = 3;

   //set the structure of the snake
   static final char head = 'O', body = 'o', worm = '~';

   //randomly set the location of the food
   static Point food = new Point((int) (Math.random() * (Snake.WIDTH - 2)) + 1, (int) (Math.random() * (Snake.HEIGHT - 2)) + 1);
   private void newFood() {
      food = new Point((int) (Math.random() * (Snake.WIDTH - 2)) + 1, (int) (Math.random() * (Snake.HEIGHT - 2)) + 1);
   }

   //set the initial snake
   //Point represent single units of the snake
   //LinkedList snakePoints represent the whole snake formed by Point


   LinkedList<Point> snakePoints = new LinkedList<>();
   void initSnake() {
      Point head = new Point(Snake.WIDTH / 2, Snake.HEIGHT / 2);
      snakePoints.addFirst(head);
      snakePoints.addLast(new Point(head.x - 1, head.y));
      snakePoints.addLast(new Point(head.x - 2, head.y));
   }

   //movement
   int move(int orient) {
      Point p = snakePoints.getFirst();
      // p represent the snake head
      Point np = null;


      //according to the direction of the movement, use the snakehead Point to replace the element of map
      switch (orient) {
         case Snakebody.RIGHT:
            np = new Point(p.x + 1, p.y);
            break;
         case Snakebody.LEFT:
            np = new Point(p.x - 1, p.y);
            break;
         case Snakebody.DOWN:
            np = new Point(p.x, p.y + 1);
            break;
         case Snakebody.UP:
            np = new Point(p.x, p.y - 1);
            break;
      }

      // if the snakehead overlap the snake body(snake eat itself), return -1
      if (snakePoints.contains(np)) {
         return -1;
      }

      // if the snakehead overlap the food(snake eat the food), return 2
      snakePoints.addFirst(np);
      if (np.equals(food)) {
         newFood();
         return 2;
      }

      // if nothing happen, return 1
      snakePoints.removeLast();
      return 1;
   }
}
