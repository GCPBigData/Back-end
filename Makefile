build-api:
	./gradlew liquibaseDiffChangelog
	./gradlew clean build -x test

docker-image:
	./gradlew -Pprod bootJar jibDockerBuild

docker-push: docker-image
	docker tag cliv_server:latest pereirapleandro/cliv_server:latest
	docker push pereirapleandro/cliv_server:latest
