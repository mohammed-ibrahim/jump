
# 1. Buildes tokens and java files based on grammer file.
java -jar ./lib/antlr-4.5.1-complete.jar ./src/main/antlr/Jump.g4 -visitor -package org.jump.parser.grammer -no-listener &&

# 2. Remove the older grammer files.
rm ./src/main/java/org/jump/parser/grammer/* &&

# 3 Move token files to proper location
mv ./src/main/antlr/*.tokens ./src/main/java/org/jump/parser/grammer/ &&

# 4 Move java files to proper location
mv ./src/main/antlr/*.java ./src/main/java/org/jump/parser/grammer/
