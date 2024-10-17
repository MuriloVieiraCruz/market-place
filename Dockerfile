FROM maven:3.9.5-amazoncorretto-21 AS build

RUN mkdir /app

COPY . /app

WORKDIR /app

RUN mvn clean package

FROM amazoncorretto:21

RUN mkdir /app

RUN addgroup -g 1001 -S tecogroup
RUN adduser -S teco -u 1001

COPY --from=build /app/target/market-place-0.0.1-SNAPSHOT.jar /app/market-place.jar

WORKDIR /app

RUN chown -R teco:tecogroup /app

CMD java $JAVA_OPTS -jar market-place.jar