import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame {
    final String titleWind = "Змейка";
    final String gameTitleOver = "Игра окончена";
    final int pointRadios = 30;
    final int gameHeight = 20;
    final int gameWidth = 30;
    final int gameX = 6;
    final int gameY = 28;
    final int startLoc = 200;
    final int startSizeSnake = 7;
    final int startLocSnakeX = 10;
    final int startLocSnakeY = 10;
    final int delayShow = 150;
    final int snakeLeft = 37;
    final int snakeUp = 38;
    final int snakeRight = 39;
    final int snakeDown = 40;
    final int startDirectionSnake = snakeRight;
    final Color snakyColor = Color.blue;
    final Color foodColor = new Color(247, 201, 158);
    Color backgroundCanvasColor = new Color(247, 201, 158);
    Snake snake;
    Food food;
    JFrame frame;
    Canvas canvasPanel;
    Random random = new Random();
    boolean gameOver = false;

    BufferedImage bufferedImage = ImageIO.read(new File("src/png/food.png"));
    Image img = bufferedImage.getScaledInstance(30, 34, Image.SCALE_DEFAULT);


    public SnakeGame() throws IOException {
    }


    //**//


    public static void main(String[] args) throws IOException {
        new SnakeGame().Main();
    }

    void Main() {
        frame = new JFrame(titleWind);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(gameWidth * pointRadios + gameX, gameHeight * pointRadios + gameY);
        frame.setLocation(startLoc, startLoc);
        frame.setResizable(false);
        canvasPanel = new Canvas();
        canvasPanel.setBackground(backgroundCanvasColor);

        frame.getContentPane().add(BorderLayout.CENTER, canvasPanel);
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                snake.setDirection(e.getKeyCode());
            }
        });

        frame.setVisible(true);

        snake = new Snake(startLocSnakeX, startLocSnakeY, startSizeSnake, startDirectionSnake);
        food = new Food();
        while (!gameOver) {
            snake.move();
            if(food.isEaten()){
                food.next();
            }
            canvasPanel.repaint();
            try {
                Thread.sleep(delayShow);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    class Food extends Point {

        public Food() {
            super(-1, -1);
            this.color = foodColor;
        }

        void eat() {
            this.setXY(-1, -1);
        }

        boolean isEaten() {
            return this.getX() == -1;
        }

        void next(){
            int x,y;
            do{
                x =  random.nextInt(gameWidth - 10);
                y =  random.nextInt(gameHeight - 10);
            } while(snake.isInsideSnake(x,y));
            this.setXY(x,y);
        }
    }



    public class Canvas extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            snake.paint(g);
            food.paint(g);
            if(gameOver){
                g.setColor(Color.red);
                g.setFont(new Font("Times new Roman", Font.BOLD, 50));
                FontMetrics q1 = g.getFontMetrics();
                g.drawString(gameTitleOver, (gameWidth * gameHeight + gameX - q1.stringWidth(gameTitleOver))/2, (gameHeight * pointRadios + gameY)/2);
            }
        }
    }

    class Point {
        int x, y;
        Color color = snakyColor;
        public Point(int x, int y) {
            this.setXY(x, y);
        }
        int getX() {

            return x;
        }

        int getY() {

            return y;
        }

        void paint(Graphics g) {
            g.setColor(color);
            g.fillOval(x * pointRadios, y * pointRadios, pointRadios, pointRadios);
            if (color == foodColor) {
                g.drawImage(img, x * pointRadios, y * pointRadios, null);
            }
        }

        void setXY(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }


    class Snake{
        ArrayList<Point> snake = new ArrayList<Point>();
        int direction;
        int snakeScore = snake.size();
        public Snake(int x, int y, int length, int direction) {
            for (int i = 0; i < length; i++) {   //цикл создает объекты
                Point point = new Point(x - i, y);
                snake.add(point);
            }
            this.direction = direction;
        }
        boolean isInsideSnake(int x, int y){
            for(Point point :  snake){
                if((point.getX() == x) && (point.getY() == y)) {
                    return true;
                }
            }
            return false;
        }

        void paint(Graphics p) {
            p.setFont(new Font("Times new Roman", Font.BOLD, 50));
            p.drawString(String.valueOf(snakeScore), 10, 45);
            for (Point point : snake) {
                point.paint(p);
            }
        }

        boolean isFood(Point food) {
            return ((snake.get(0).getX() == food.getX()) && (snake.get(0).getY() == food.getY()));
        }
        void move() {
            int x = snake.get(0).getX();
            int y = snake.get(0).getY();

            if (direction == snakeLeft) {
                x--;
            }
            if (direction == snakeRight) {
                x++;
            }
            if (direction == snakeUp) {
                y--;
            }
            if (direction == snakeDown) {
                y++;
            }
            if (x > gameWidth - 1) {
                x = 0;
            }
            if (x < 0) {
                x = gameWidth - 1;
            }
            if (y > gameHeight - 1) {
                y = 0;
            }
            if (y < 0) {
                y = gameHeight - 1;
            }

            gameOver = isInsideSnake(x,y);

            snake.add(0, new Point(x, y));
            if (isFood(food)) {
                food.eat();
                frame.setTitle(titleWind);
                snakeScore();
            } else {
                snake.remove(snake.size() - 1);
            }
        }

            void snakeScore() {
            snakeScore = snake.size() - 7;
        }

        void setDirection(int direction) {
            if ((direction >= snakeLeft) && (direction <= snakeDown)) {
                if(Math.abs(this.direction - direction) != 2){
                    this.direction = direction;
                }
            }
        }
    }

}