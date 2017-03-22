
find . -name "*.java" -exec sed -i 's/\t/    /g' {} +
find . -name "*.java" -exec sed -i 's/[ \t]*$//' {} +

find . -name "*.ju" -exec sed -i 's/\t/    /g' {} +
find . -name "*.ju" -exec sed -i 's/[ \t]*$//' {} +
