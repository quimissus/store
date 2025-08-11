echo "1. Running clean check"
./gradlew clean check
echo "Done ==== Running clean check"

echo "2. Running spotlessApply"
./gradlew :spotlessApply
echo "Done ==== Running spotlessApply"

echo "3. Running clean test jacocoTestReport"
./gradlew clean test jacocoTestReport
echo "Done ==== Running clean test jacocoTestReport"

#echo "4. Running bootJar"
#./gradlew bootJar
#echo "Done ==== Running bootJar"


echo "4. building docker image"
docker build -t store-app .
echo "Done === building docker image"

echo "5. Deploying app check: http://localhost:8085"
docker run -p 8085:8085 store-app