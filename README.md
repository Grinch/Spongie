Spongie
=======

## Prerequisites
* [Java] 8
* [Gradle] 4.1+

## Cloning
The following steps will ensure your project is cloned properly.  
1. `git clone git@github.com:Grinch/Spongie.git`  
2. `cd Spongie`  
3. `cp scripts/pre-commit .git/hooks`

## Building
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for Windows systems in place of any
 'gradle' command.

In order to build Grand Exchange you simply need to run the `gradle` command. You can find the compiled JAR files in `./build/libs` but in most 
cases you'll only need 'spongie-x.x.jar'.

[Gradle]: http://www.gradle.org/
[Java]: http://java.oracle.com/
