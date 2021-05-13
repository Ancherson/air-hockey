# AIR HOCKEY

## Presentation

It's a video-game inspired by the real air-hockey !

## Usage

### Build

```
git clone https://gaufre.informatique.univ-paris-diderot.fr/rougeoll/air-hockey.git
cd air-hockey/Projet
./gradlew build
```
### Run

#### To test on the same machine

To run the server :
```
./gradlew runServer
```
To run the client :
```
./gradlew run
```
#### To test with different machines
To run the server :
```
./gradlew runServer --args='IPv4 of the machine'
```
To run the client on a different machine :

```
./gradlew run --args='IPv4 of the machine that runs the server'
```

### Play Online

#### Private Game :

You need to have a friend to play online in a private game.
You or your friend click on the button **create room**
This person can click on the **ID** to copy, and send it to the other

![Private Game 1](Projet/ressources/ressources-readme/image1.png)
![Private Game 2](Projet/ressources/ressources-readme/image2.png)

The other click on the button **join room**, receive the **ID** and copy it !

![Private Game 3](Projet/ressources/ressources-readme/image3.png)
![Private Game 4](Projet/ressources/ressources-readme/image4.png)

you can play with your friend !

#### Public Game :

To play against anyone, you just have to click on the button **join public room**

![Public Game](Projet/ressources/ressources-readme/image6.png)

### Play offline :

To play alone, against the AI, you have to click on the button **Training Mode**

![Offline](Projet/ressources/ressources-readme/image5.png)

### In Game :

The goal is to send the puck into the opposing goal with your blue pusher
The game ends when one of the players has 7 points

![](Projet/ressources/ressources-readme/video-air-hockey.mp4)

## Documentation

see documentation of [Serveur/UDP Protocole](doc_server.md)

