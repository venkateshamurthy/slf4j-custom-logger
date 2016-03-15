# Logging a custom object string representation sensitive to logger level.
---------------------------------------------------------------------------------------

Reference : Please refer to [blog](http://vemurthy.blogspot.in/2016/03/slf4j-custom-logger-using-customized.html "Slf4j custom logger") to get the details.
 
##Project:
----------
This project aims to acheive the following objectives:

*   The objects to be logged (notably the value objects) can now be enabled for building string representations based on [level of detail](/level-logger/src/main/java/com/github/venkateshamurthy/util/logging/LevelOfDetail.java) where brief can mean few key attributes and medium level of detail can add key and other import

    +    objects can be made to implement the interface [LevelledToString](/level-logger/src/main/java/com/github/venkateshamurthy/util/logging/LevelledToString.java) and build the <code>toString(LevelOfDetail)</code> manually
    +    objects can be written in eclipse xtend code to have [@ToLevelledStringAnnotation](/level-tostring/src/main/java/com/github/venkateshamurthy/util/tostring/xtend/ToDetailedStringProcessor.xtend) that generates the toString(LevelOfDetail) method

*   The project has a custom slf4j logger that deals with [LevelledToString](/level-logger/src/main/java/com/github/venkateshamurthy/util/logging/LevelledToString.java) instances on a special case to call the respective <code>toString(LevelOfDetail)</code> rather than default toString method

    +   The Logger is wired through SLF4J's StaticBindingLogger and hence the logger can be accessed transparently by the LoggerFactory.getLogger(..) methodss
    
##Modules:
----------
1.  <b>level-logger</b>            : This module has the ToLevelledString interface and the custom logger. This must be used as a dependency
2.  <b>level-tostring</b>          : This module has the eclipse xtend class to generate the toString(LevelOfDetail) code for any class. This must be used as a dependency
3.  <b>level-tostring-examples</b> : This module has an example using both level-logger and level-tostring and generate toString(LevelOfDetail) on example classes and log it in debug or info mode.
4.  <b>level-tostring-sample</b>    : This is not a module per se. However its an independent project example that uses all the necessary dependencies along with level-logger and level-tostring (for now its stored locally).

###How to build:
----------------
Say if the parent project slf4j-custom-logger is at c:\workspaces\slf4j-custom-logger

1.  Reach up to the base folder of parent project
2.  type mvn -U clean install

