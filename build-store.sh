echo "1. Running clean check"
./gradlew clean check
echo "Done ==== Running clean check"

echo "2. Running spotlessApply"
./gradlew :spotlessApply
echo "Done ==== Running spotlessApply"

echo "3. Running clean test jacocoTestReport"
./gradlew clean test jacocoTestReport
echo "Done ==== Running clean test jacocoTestReport"

docker rm -f store-app
docker volume rm store-app

echo "4. building docker image"
docker build -t store-app .
echo "Done === building docker image"



echo "5. Deploying app check: http://localhost:8085"

docker run -d \
  --name store-app \
  --network store-network \
  -p 8085:8085 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/store \
  -e SPRING_DATASOURCE_USERNAME=admin \
  -e SPRING_DATASOURCE_PASSWORD=admin \
  -v $(pwd)/logs:/app/logs \
  store-app
