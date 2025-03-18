import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;


// JPanel

public class PacMan extends JPanel implements ActionListener, KeyListener {
    
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction = 'U'; //U D L R
        int velocityX = 0;
        int velocityY = 0;

        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
    
        }

        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            int prevX = this.x;
            int prevY = this.y;
            this.x += this.velocityX;
            this.y += this.velocityY;
            boolean collided = false;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    collided = true;
                    break;
                }
            }
            if (collided) {
                this.x = prevX;
                this.y = prevY;
                this.direction = prevDirection;
                updateVelocity();
            }
        }

        void updateVelocity(){
            if(this.direction == 'U'){
                this.velocityX = 0;
                this.velocityY = -tileSize/4;
            }
            else if (this.direction == 'D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }
            else if (this.direction == 'L'){
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }
            else if (this.direction == 'R'){
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }
        }

        //Reset
        void reset(){
            this.x = this.startX;
            this.y = this.startY;
            
        }
    }
   private int rowCount  = 21;
   private int columnCount = 19;
   private int tileSize = 32;
   private int boardWidth = columnCount * tileSize;
   private int boardHeight = rowCount * tileSize;


    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImages;
    private Image pinkGhostImage;
    private Image redGhostImage;


    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    private Image cherryImage;
    private Image cherry2Image;
    private Image scaredGhostImage;

    private Image pauseImage;
    private Image gameOverImage;
    private Image livesImage;
    private boolean gamePaused = false;

    private HashSet<Block> powerUps;
    private boolean pacmanPoweredUp = false;
    private int powerUpDuration = 5000; // Changed from 10000 to 5000 (5 seconds instead of 10)
    private long powerUpStartTime;

    //Tile Map (I can modify the tile map by myself also)
    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    //Power-ups: C = cherry, c = cherry2
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X     c           X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXXCX XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    boolean pacmanMoved = false;

    //Timer for Game loop
    Timer gameLoop;

    //to move the Ghost and randomly select the direction
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;
 

//Creating Constructor
    PacMan(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);


        //loading the images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImages = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        //creating PacMan images
        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        cherryImage = new ImageIcon(getClass().getResource("./cherry.png")).getImage();
        cherry2Image = new ImageIcon(getClass().getResource("./cherry2.png")).getImage();
        scaredGhostImage = new ImageIcon(getClass().getResource("./scaredGhost.png")).getImage();

        pauseImage = new ImageIcon(getClass().getResource("./pause.png")).getImage();
        gameOverImage = new ImageIcon(getClass().getResource("./gameover.png")).getImage();
        livesImage = new ImageIcon(getClass().getResource("./lives.png")).getImage();

        loadMap();
        for(Block ghost : ghosts){
            char newDirection = (directions[random.nextInt(4)]);  // 0,1,2,3
            ghost.updateDirection(newDirection);
        }
        //how long it takes ot  start timer, milliseconds gone between frames  // 20fps (1000/50)
        gameLoop = new Timer(50, this);  //50 is the delay and this refers to the PacMan Object 
        gameLoop.start();


    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        powerUps = new HashSet<Block>();

        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){
                String tile = tileMap[r];
                char tileMapChar = tile.charAt(c);
                
                int x = c * tileSize;
                int y = r * tileSize;
                
                if(tileMapChar == 'X'){ //block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'b'){ //blue Ghost
                    Block ghost = new Block (blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o'){ //orange Ghost
                    Block ghost = new Block (orangeGhostImages, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p'){ //pink Ghost
                    Block ghost = new Block (pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r'){ //red Ghost
                    Block ghost = new Block (redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P'){ //pacman
                    pacman = new Block (pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' '){ //food
                    Block food = new Block (null, x + 14, y + 14, 4, 4);   //here we are creating an offset of the food image
                    foods.add(food);
                }
                else if (tileMapChar == 'C') { // cherry power-up
                    Block powerUp = new Block(cherryImage, x, y, tileSize, tileSize);
                    powerUps.add(powerUp);
                } else if (tileMapChar == 'c') { // cherry2 power-up
                    Block powerUp = new Block(cherry2Image, x, y, tileSize, tileSize);
                    powerUps.add(powerUp);
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null );

        for (Block ghost : ghosts){
            if (ghost.image == scaredGhostImage) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // Set opacity to 50%
                g2d.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
                g2d.dispose();
            } else {
                g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
            }
        }

        for (Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (Block food : foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        for (Block powerUp : powerUps) {
            g.drawImage(powerUp.image, powerUp.x, powerUp.y, powerUp.width, powerUp.height, null);
        }

        // Draw pause image when game is paused
        if (gamePaused) {
            int pauseImageWidth = 400; // Increased from 128 to 200
            int pauseImageHeight = 400; // Increased from 128 to 200
            int pauseX = (boardWidth - pauseImageWidth) / 2;
            int pauseY = (boardHeight - pauseImageHeight) / 2;
            g.drawImage(pauseImage, pauseX, pauseY, pauseImageWidth, pauseImageHeight, null);
        }

        //Score and Lives
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if(gameOver){
            // Draw game over image
            int gameOverImageWidth = 400;
            int gameOverImageHeight = 400;
            int gameOverX = (boardWidth - gameOverImageWidth) / 2;
            int gameOverY = (boardHeight - gameOverImageHeight) / 2;
            g.drawImage(gameOverImage, gameOverX, gameOverY, gameOverImageWidth, gameOverImageHeight, null);
            
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else {
            // Draw lives as heart images
            int livesImageWidth = 60;  // Decreased from 60 to 35
            int livesImageHeight = 60; // Decreased from 60 to 35
            int livesGap = -10;        // Negative gap to make them overlap slightly
            int startX = boardWidth - (livesImageWidth * lives) - (livesGap * (lives - 1)) - 10;
            int livesY = -10;          // Adjusted Y position
            
            for (int i = 0; i < lives; i++) {
                int x = startX + (i * (livesImageWidth + livesGap));
                g.drawImage(livesImage, x, livesY, livesImageWidth, livesImageHeight, null);
            }
            
            // Show just the score without the "x lives" text
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Score: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
    }


    public void move(){
        if (!pacmanMoved) return; // Ghosts don't move until PacMan moves

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;


        //CHECK WALL COLLISION  means that if there is a HIT on the wall we will move back
        for (Block wall : walls){
            if(collision(pacman, wall)){
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        //CHECK GHOST COLLISION
        HashSet<Block> ghostsToRemove = new HashSet<>();
        for (Block ghost : ghosts) {
            if (collision(ghost, pacman)) {
                if (ghost.image == scaredGhostImage) {
                    // If ghost is scared, remove it and add points
                    ghostsToRemove.add(ghost);
                    score += 10;
                } else {
                    // Only lose life if ghost is not scared
                    lives -= 1;
                    if (lives == 0) {
                        gameOver = true;
                        return;
                    }
                    resetPositions();
                    break;
                }
            }
            
            // Ghost movement logic continues as before
            if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall : walls) {
                if (collision(wall, ghost)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
            // Wrap around logic for ghosts
            if (ghost.x < 0) {
                ghost.x = boardWidth - ghost.width;
            } else if (ghost.x + ghost.width > boardWidth) {
                ghost.x = 0;
            }
        }
        // Remove eaten ghosts after the loop to avoid ConcurrentModificationException
        ghosts.removeAll(ghostsToRemove);

        //CHECK PACMAN COLLISION WITH WALLS
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        // Wrap around logic for pacman
        if (pacman.x < 0) {
            pacman.x = boardWidth - pacman.width;
        } else if (pacman.x + pacman.width > boardWidth) {
            pacman.x = 0;
        }

        //CHECK FOOD COLLISION
        Block foodEaten = null;
        for (Block food : foods){
            if(collision(pacman, food)){
                foodEaten = food;
                score++;
            }
        }
        foods.remove(foodEaten);

        // CHECK POWER-UP COLLISION
        Block powerUpCollected = null;
        for (Block powerUp : powerUps) {
            if (collision(pacman, powerUp)) {
                powerUpCollected = powerUp;
                pacmanPoweredUp = true;
                powerUpStartTime = System.currentTimeMillis();
                for (Block ghost : ghosts) {
                    ghost.image = scaredGhostImage;
                }
            }
        }
        powerUps.remove(powerUpCollected);

        // Power-up duration check
        if (pacmanPoweredUp && System.currentTimeMillis() - powerUpStartTime > powerUpDuration) {
            pacmanPoweredUp = false;
            for (Block ghost : ghosts) {
                if (ghost.image == scaredGhostImage) {
                    if (tileMap[ghost.startY / tileSize].charAt(ghost.startX / tileSize) == 'b') {
                        ghost.image = blueGhostImage;
                    } else if (tileMap[ghost.startY / tileSize].charAt(ghost.startX / tileSize) == 'o') {
                        ghost.image = orangeGhostImages;
                    } else if (tileMap[ghost.startY / tileSize].charAt(ghost.startX / tileSize) == 'p') {
                        ghost.image = pinkGhostImage;
                    } else if (tileMap[ghost.startY / tileSize].charAt(ghost.startX / tileSize) == 'r') {
                        ghost.image = redGhostImage;
                    }
                }
            }
        }

        //reload the same map if the food is all eaten 
        if (foods.isEmpty()){
            loadMap();
            resetPositions();
        }
    }

    public boolean collision(Block a, Block b){
        return  a.x < b.x + b.width && 
                a.x + a.width > b.x && 
                a.y < b.y + b.height && 
                a.y + a.height > b.y;
    } 

    public void resetPositions(){
        pacman.reset();
        pacman.velocityX = 0;    //When the pacman resets the velocity of pacman will be stop
        pacman.velocityY = 0;
        for (Block ghost : ghosts){
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];  // Fixed: was directions.random.nextInt(4)
            ghost.updateDirection(newDirection);
        }
    }
        


    @Override
    public void actionPerformed(ActionEvent e) {  //Game Loop 
        // Only update game state if the game is not paused
        if (!gamePaused) {
            move();                         
            repaint();
        } else {
            repaint(); // Still repaint even when paused to show pause image
        }
        
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if(gameOver){              //if the game stops press any key to play again
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                loadMap();
                resetPositions();
                lives = 3;
                score = 0;
                gameOver = false;
                gameLoop.start();
            }
            return;
        }

        // Handle pause functionality
        if (e.getKeyCode() == KeyEvent.VK_P) {
            gamePaused = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            gamePaused = false;
        }

        // Don't process movement keys when paused
        if (gamePaused) {
            return;
        }

        // System.out.println("KeyEvent: " + e.getKeyCode());
        if(e.getKeyCode() == KeyEvent.VK_UP){
            pacman.updateDirection('U');
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            pacman.updateDirection('D');
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            pacman.updateDirection('L');
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            pacman.updateDirection('R');
        }    
        pacmanMoved = true; // PacMan has moved, start ghost movement

        //To change the mouth of pac man when it moves in different directions
        if (pacman.direction == 'U'){
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction == 'D'){
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction == 'L'){
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction == 'R'){
            pacman.image = pacmanRightImage;
        }
    }
}
