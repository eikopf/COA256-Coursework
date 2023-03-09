# COA256 Coursework

1. [Dependencies](#Dependencies)
2. [Project Overview](#project-overview)
3. [Building from Source](#building-from-source)
4. [Resources](#resources)

## Dependencies
This project relies on Gradle for dependency management, 
and uses it to install the following plugins:

| Plugin                                            | Justification                                   |
|---------------------------------------------------|-------------------------------------------------|
| [JavaFX](https://openjfx.io/)                     | A modern successor to Java's internal Swing API |
| [Shadow](https://github.com/johnrengelman/shadow) | Allows the creation of "fat" `.jar` files       |

## Project Overview
Broadly, this project consists of two components.

The first is a fairly generic set of classes for handling
dummy book and user data, and mainly exists to demonstrate
common OOP principles (inheritance, abstraction, interfaces),
as well as some more advanced concepts (nested objects, 
records, builders).

The second is the actual UI component, built using JavaFX. 
`TODO: finish this part`

## Building from Source
`TODO: explain this part`

## Resources
- [jenkov.com](https://jenkov.com/tutorials/javafx/index.html)
- [Shadow Documentation](https://imperceptiblethoughts.com/shadow/)
- [Stack Overflow: Configuring Shadow](https://stackoverflow.com/a/70864141)
- [Awesome JavaFX](https://github.com/mhrimaz/AwesomeJavaFX)