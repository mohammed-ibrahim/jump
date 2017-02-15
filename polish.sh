
find . -name "*.java" -exec sed -i 's/\t/    /g' {} +
find . -name "*.java" -exec sed -i 's/[ \t]*$//' {} +

