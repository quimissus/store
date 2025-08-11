echo "1. Running clean check"
./gradlew clean check
echo "Done ==== Running clean check"

echo "2. Running spotlessApply"
./gradlew :spotlessApply
echo "Done ==== Running spotlessApply"

echo "3. Running clean test jacocoTestReport"
./gradlew clean test jacocoTestReport
echo "Done ==== Running clean test jacocoTestReport"


