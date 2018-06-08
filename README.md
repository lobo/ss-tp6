![...](res/image/readme-header.png)

# Simulation N° 6: Crowd Simulation

## Build

To build the project, it is necessary to have _Maven +3.5.0_, and
_Java SE 8 Release_ installed. Then, run:

```
$ mvn clean package
```

This will generate a _\*.jar_ in the `target` folder. If you find any issues
with the building, remove the _\*.jar_ files from the _Maven_ local
repository with:

```
$ rm -fr ~/.m2/repository/ar/edu/itba/ss/*
```

Or do it manually, if you prefer.

## Execute

In the root folder (after build):

```
$ java -jar target/tp6-1.0-SNAPSHOT-jar-with-dependencies.jar <simulate | animate | flow>
```

Or better, run the precompiled _\*.jar_ in the root (we made it for you):

```
$ java -jar crowd-simulator.jar <simulate | animate | flow>
```

Each mode does:

* `simulate`: runs the simulation and outputs the _\*.static_, _\*.state_ and
_\*.drain_ files.
* `animate`: builds the animation file for _Ovito_ (_\*.xyz_ extension).
* `flow`: takes the _\*.drain_ file and generate a _\*.flow_ file, with a
temporal serie of the windowed-flow.

## Configuration

Receives a JSON file with the following format:

```json
{
    "output"            : "res/data/output",
    "delta"             : "0.0001",
    "time"              : "10.0",
    "fps"               : "50",
    "playbackSpeed"     : "1.0",
    "samplesPerSecond"  : "200",

    "integrator"        : "BeemanIntegrator",
    "reportEnergy"      : "false",
    "reportTime"        : "true",
    "radius"            : ["0.25", "0.29"],
    "mass"              : "80.0",

    "elasticNormal"     : "1.2E+5",
    "elasticTangent"    : "2.4E+5",
    "viscousDamping"    : "6196.773354",
    "siloDamping"       : "6196.773354",

    "a"                 : "2000.0",
    "b"                 : "0.08",
    "tau"               : "0.5",
    "desiredSpeed"      : "5.0",
    "breakRange"        : "2.5",
    "target"            : ["10.0", "0.0"],
    "targetWidth"       : "0.42",

    "generator"         : "73604268647601935",
    "n"                 : "20",
    "height"            : "20.0",
    "width"             : "20.0",
    "drain"             : "1.2",

    "window"            : "5.0",
    "flowRate"          : "0.01"
}

```

## Output Files Format

### Static Properties (_\*.static_)

Contains the intrinsic properties of each particle. This properties are
static, _i.e._, they are constant throughout the simulation.

```
<radius> <mass>
...
```

### Simulation Result (_\*.state_)

The complete state of the simulation. Includes the position and the velocity
vector of each particle:

```
<x> <y> <vx> <vy>
...
```

### Pressure Evolution (_\*.pressure_)

The pressure of the particles (_i.e._, the magnitude of the collisions and the
social forces over normal direction, divided by the perimeter of the body).
This has the same time-step like the simulation file:

```
<pressure>
...
```

### Flow Rate File (_\*.drain_)

This file contains the complete drain (_i.e._, the ID of a particle
drained at a certain time).

```
<t> <id>
...
```

### Windowed Flow Rate File (_\*.flow_)

This is the slided-windowed flow per unit of temporal step. The units are in
_[particles/second]_.

```
<t> <flow>
...
```

### Animation File (_\*.xyz_)

This file can be used in _Ovito_ to render the simulation:

```
<N>
<t0>
<x> <y> <radius> <speed> <pressure>
...
```

## Videos

* [Simulation N° 1: Desired Speed 0.5](https://www.youtube.com/watch?v=LQ-BC8Juc58)
* [Simulation N° 2: Desired Speed 1.0](https://youtu.be/MZHWmk2ng3A)
* [Simulation N° 3: Desired Speed 2.0](https://youtu.be/T396VaRHlZ4)
* [Simulation N° 4: Desired Speed 3.0](https://youtu.be/6XAavGodexw)
* [Simulation N° 5: Desired Speed 4.0](https://youtu.be/7EARQdVfIgg)
* [Simulation N° 5: Desired Speed 4.0 with A=0](https://www.youtube.com/watch?v=TC9T83Y2kj0)

## Developers

This project has been built, designed and maintained by the following authors:

* [Daniel Lobo](https://github.com/lobo)
* [Agustín Golmar](https://github.com/agustin-golmar)

## Bibliography

__"Simulating Dynamical Features of Escape Panic"__. Dirk Helbing, Illés
Farkas and Tamás Vicsek. _Nature, Vol. 407. 28th September, 2000. Macmillan
Magazine_.
