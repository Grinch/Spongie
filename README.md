Grand Exchange
==============

## Prerequisites
* [Java] 8
* [Gradle] 4.0+

## Cloning
The following steps will ensure your project is cloned properly.  
1. `git clone git@github.com:AlmuraDev/Grand-Exchange.git`  
2. `cd Grand-Exchange`  
3. `cp scripts/pre-commit .git/hooks`

## Setup
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for Windows systems in place of any 'gradle' command.

__For [IntelliJ]__  
  1. Run `gradle setupDecompWorkspace --refresh-dependencies`  
  2. Make sure you have the Gradle plugin enabled (File > Settings > Plugins).  
  3. Click File > Import Module and select the **build.gradle** file for Grand Exchange.
  4. On the import screen, uncheck `Create separate module per source set`

## Building
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for Windows systems in place of any 'gradle' command.

In order to build Grand Exchange you simply need to run the `gradle` command. You can find the compiled JAR files in `./build/libs` but in most cases you'll only need 'grand_exchange-x.x-xxxx.jar'.

## Running (Manual Configuration)
__Note 1:__ The following is aimed to help you setup run configurations for Eclipse and IntelliJ. If you do not want to be able to run Grand Exchange directly from your IDE then you can skip this.  

__For [IntelliJ]__  
  1. Go to **Run > Edit Configurations**.  
  2. Click the green + button and select **Application**.  
  3. Set the name as `Grand Exchange (Client)` and apply the information for Client below.  
  4. Repeat step 2 and set the name as `Grand Exchange (Server)` and apply the information for Server below.  
  4a. When launching the server for the first time, it will shutdown by itself. You will need to modify the server.properties to set onlinemode=false and modify the eula.txt to set eula=true (this means you agree to the Mojang EULA, if you do not wish to do this then you cannot run the server).

__Client__

|     Property      | Value                               |
|:-----------------:|:------------------------------------|
|    Main class     | GradleStart                         |
|    VM options     | -Xincgc -Xms1024M -Xmx2048M         |
| Program arguments |                                     |
| Working directory | ./run (Included in project)         |
| Module classpath  | Grand Exchange (IntelliJ Only)      |

__Server__

|     Property      | Value                          |
|:-----------------:|:-------------------------------|
|    Main class     | GradleStartServer              |
|    VM options     | -Xincgc -Xms1024M -Xmx2048M    |
| Program arguments |                                |
| Working directory | ./run (Included in project)    |
| Module classpath  | Grand Exchange (IntelliJ Only) |


[Eclipse]: http://www.eclipse.org/
[Gradle]: http://www.gradle.org/
[IntelliJ]: http://www.jetbrains.com/idea/
[Java]: http://java.oracle.com/
