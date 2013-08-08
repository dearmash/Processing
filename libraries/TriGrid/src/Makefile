# Platform-specific rules
ifeq ($(shell uname -s),Darwin)
  # Mac OS X
  JDK       = /Applications/Processing.app/Contents/Resources/Java
  CLASSPATH = $(JDK)/core.jar:$(JDK)/modes/java/libraries/Serial/library/serial.jar:../../WS2801/library/WS2801.jar
endif
ifeq ($(shell uname -s),Linux)
  JDK       = ~/processing-1.5.1
  CLASSPATH = $(JDK)/lib/core.jar:$(JDK)/modes/java/libraries/serial/library/serial.jar:../../WS2801/library/WS2801.jar
endif
ifeq ($(findstring CYGWIN,$(shell uname -s)),CYGWIN)
  JDK       = /cygdrive/c/Program\ Files/Java/jdk1.6.0_26
  CLASSPATH = $(JDK)/lib/core.jar:$(JDK)/modes/java/libraries/serial/library/serial.jar:../../WS2801/library/WS2801.jar
endif

all: TriGrid.jar

TriGrid.jar: TriGrid.java
	javac -d . -classpath $(CLASSPATH) TriGrid.java
	jar -fc TriGrid.jar me
	cp *.jar ../library

clean:
	rm -rf me *.jar

