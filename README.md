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

####To test on the same machine

To run the server :
```
./gradlew runServer
```
To run the client :
```
./gradlew run
```
####To test with different machines
To run the server :
```
./gradlew runServer --args='IPv4 of the machine'
```
To run the client on a different machine :

```
./gradlew run --args='IPv4 of the machine that runs the server'
```