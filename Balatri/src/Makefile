JAR=../lib/zen-6.0.jar
SRC=.
BIN=../bin

run: build
	java -cp "$(JAR):$(BIN)" Main

build:
	mkdir -p $(BIN)
	javac -d $(BIN) -cp "$(JAR):$(SRC)" $(shell find . -name '*.java')

clean:
	rm -rf $(BIN)