set p=%1

call t

if "%p%"=="c" (
	mvn clean
)

if "%p%"=="i" (
	mvn install -DskiptTests=true
)

if "%p%"=="d" (
	mvn javadoc:javadoc
)

if "%p%"=="" (
	mvn install -DskiptTests=true
)

