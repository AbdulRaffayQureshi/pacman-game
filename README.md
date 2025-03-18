# 🎮 Java Pac-Man Game

<div align="center">
  
![Pac-Man Logo](https://img.shields.io/badge/PAC--MAN-GAME-yellow?style=for-the-badge&logo=java&logoColor=white)

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-23%2B-orange)](https://www.oracle.com/java/)

</div>

## 📖 About The Project

A modern Java implementation of the classic arcade game Pac-Man. This project offers a nostalgic gaming experience with modern code architecture and features.

![Gameplay Screenshot](pacman%20image%20game.png)

## ✨ Features

- **Classic Gameplay** - Navigate through mazes, collect dots, and avoid ghosts
- **Multiple Levels** - Increasing difficulty as you progress through the game
- **Power-Ups** - Special items that allow Pac-Man to eat ghosts temporarily
- **Score Tracking** - Real-time score display and high score saving
- **Sound Effects** - Classic game sounds for an immersive experience
- **Custom Controls** - Keyboard controls for smooth navigation
- **Game State Management** - Pause, resume, and restart functionality

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Any Java IDE (VS Code with Java extension pack recommended)

### Installation

1. Clone the repository

```bash
git clone https://github.com/yourusername/java-pac-man-game.git
```

2. Open the project in your IDE

3. Build and run the project

```bash
cd java-pac-man-game
javac -d bin src/*.java
java -cp bin Main
```

## 🎮 How to Play

- Use arrow keys to move Pac-Man
- Collect all dots to complete a level
- Avoid ghosts unless you've eaten a power pellet
- Press `P` to pause the game
- Press `ESC` to exit

## 🧱 Project Structure

```
java-pac-man-game/
├── src/                  # Source code
│   ├── game/            # Game logic
│   ├── entities/        # Game entities (Pac-Man, ghosts, etc.)
│   ├── ui/              # User interface components
│   └── utils/           # Utility classes
├── res/                  # Resources (images, sounds)
├── lib/                  # Dependencies
└── bin/                  # Compiled classes
```

## 🛠️ Technical Implementation

- **OOP Design** - Utilizes object-oriented principles for clean code organization
- **Design Patterns** - Implements MVC architecture and other design patterns
- **Custom Graphics** - Java AWT/Swing for rendering game elements
- **Thread Management** - Proper game loop implementation
- **Collision Detection** - Efficient algorithms for game physics

## ⚙️ Customization

Modify game parameters in the `GameConfig.java` file:

- Ghost speed
- Pac-Man speed
- Level difficulty
- Map layouts

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Original Pac-Man game by Namco
- [Java Game Development Resources]
- Icons and graphics from [Example Source](https://canva.com)

---

<div align="center">
  Created with ❤️ by [Abdul Raffay Qureshi]
</div>
