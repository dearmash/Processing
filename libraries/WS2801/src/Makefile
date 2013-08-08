# Platform-specific rules
ifeq ($(shell uname -s),Darwin)
  # Mac OS X
  JDK       = /Applications/Processing.app/Contents/Resources/Java
  CLASSPATH = $(JDK)/core.jar:$(JDK)/modes/java/libraries/Serial/library/serial.jar
endif
ifeq ($(shell uname -s),Linux)
  JDK       = ~/processing-1.5.1
  CLASSPATH = $(JDK)/lib/core.jar:$(JDK)/modes/java/libraries/serial/library/serial.jar
endif
ifeq ($(findstring CYGWIN,$(shell uname -s)),CYGWIN)
  JDK       = /cygdrive/c/Program\ Files/Java/jdk1.6.0_26
  CLASSPATH = $(JDK)/lib/core.jar:$(JDK)/modes/java/libraries/serial/library/serial.jar
endif

all: WS2801.jar

WS2801.jar: WS2801.java
	javac -d . -classpath $(CLASSPATH) WS2801.java
	jar -fc WS2801.jar ws2801
	cp *.jar ../library

clean:
	rm -rf ws2801 *.jar

