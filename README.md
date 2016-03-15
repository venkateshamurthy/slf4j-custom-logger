# Logging a custom object string representation sensitive to logger level.
---------------------------------------------------------------------------------------

Reference : Please refer to [blog](http://vemurthy.blogspot.in/2016/03/slf4j-custom-logger-using-customized.html "Slf4j custom logger") to get the details.
 
##Project:
----------
This project aims to acheive the following objectives:

*   The objects to be logged (notably the value objects) can now be enabled for building string representations based on [level-logger/src/main/java/com/github/venkateshamurthy/util/logging/](level of detail); where brief can mean few key attributes and medium level of detail can add key and other import

    +    objects can be made to implement the interface [ToLevelledString](/blob/master/level-logger/src/main/java/com/github/venkateshamurthy/util/logging/ToLevelledString.java) and build the <code>toString(LevelOfDetail)</code> manually
    +    objects can be written in eclipse xtend code to have [@ToLevelledStringAnnotation](/blob/master/level-tostring/src/main/java/com/github/venkateshamurthy/util/tostring/xtend/ToDetailedStringProcessor.xtend) that generates the toString(LevelOfDetail) method

*   The project has a custom slf4j logger that deals with ToLevelledString instances on a special case to call the respective toString(LevelOfDetail) rather than default toString method

    +   The Logger is wired through SLF4J's StaticBindingLogger and hence the logger can be accessed transparently by the LoggerFactory.getLogger(..) methodss
    
##Modules:
----------
1.  level-logger            : This module has the ToLevelledString interface and the custom logger. This must be used as a dependency
2.  level-tostring          : This module has the eclipse xtend class to generate the toString(LevelOfDetail) code for any class. This must be used as a dependency
3.  level-tostring-examples : This module has an example using both level-logger and level-tostring and generate toString(LevelOfDetail) on example classes and log it in debug or info mode.

###How to build:
----------------
Say if the parent project slf4j-custom-logger is at c:\workspaces\slf4j-custom-logger

1.  Reach up to the base folder of parent project
2.  type mvn -U clean install

