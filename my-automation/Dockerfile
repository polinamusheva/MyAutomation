# Argument for specifying image name
ARG IMAGE_NAME=orbis-automated-tests
# Stage 1: Base
FROM webint-nexus.cognyte.local:8888/webint-zulu-gradle:17-8.5 AS base

# Stage 3: Set the working directory
FROM base AS directory
WORKDIR /home/gradle
ARG IMAGE_NAME
RUN mkdir ${IMAGE_NAME}
COPY src ${IMAGE_NAME}/src
COPY build.gradle ${IMAGE_NAME}/build.gradle
COPY settings.gradle ${IMAGE_NAME}/settings.gradle
COPY gradle.properties  ${IMAGE_NAME}/gradle.properties

# Stage 4: Builder
FROM directory AS builder
ARG IMAGE_NAME
ARG GIT_COMMIT_ID_FULL=1
ARG GIT_COMMIT_ID_SHORT=1
ARG GIT_BRANCH=1
ARG COMPONENT_VERSION=1
# Build your application
RUN cd ${IMAGE_NAME} && \
    gradle -Dtest.ignoreFailures=true -Dorg.gradle.daemon=false -PgitCommitId=${GIT_COMMIT_ID_FULL} -PgitBranchName=${GIT_BRANCH} -PgitCommitIdShort=${GIT_COMMIT_ID_SHORT} -PcomponentVersion=${COMPONENT_VERSION} --no-daemon --refresh-dependencies assemble -x test && \
    mkdir -p /home/gradle/${IMAGE_NAME}/build/test-results/test
# Echo the path where test results are stored
RUN echo "Test results stored in: /home/gradle/${IMAGE_NAME}/build/test-results/test"
RUN pwd

# Stage 5: Tests-getter
FROM builder AS tests-getter
ARG IMAGE_NAME
WORKDIR /home/gradle/${IMAGE_NAME}
# Copy test results
COPY --from=builder /home/gradle/${IMAGE_NAME}/build/test-results/test test
# Install Docker
RUN curl -sSL https://get.docker.com/ | sh
# Echo the test results path
RUN echo "Test results stored in: /home/gradle/${IMAGE_NAME}/build/test-results/test"
RUN pwd

# Stage 6: Jar-getter
FROM builder AS jar-getter
ARG IMAGE_NAME
# Copy jar files
COPY --from=builder /home/gradle/${IMAGE_NAME}/build/libs/*.jar /${IMAGE_NAME}/libs/

# Stage 7: Runner
FROM webint-nexus.cognyte.local:8888/azul/zulu-openjdk:17 AS runner
ARG IMAGE_NAME=orbis-automated-tests
# Install curl and other necessary packages (to ensure curl is set in the final image)
RUN apt-get update && apt-get install -y unzip && apt-get install -y curl
# Set working directory
WORKDIR /${IMAGE_NAME}
# Copy jar file
COPY --from=jar-getter /${IMAGE_NAME}/libs/*.jar ./
# Java Debug Options
ENV JAVA_OPTS_DEBUG -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=7004,server=y,suspend=n
# Create logs directory
RUN mkdir ./logs
# Command to run the application
CMD ["java", "-jar", "./orbis-automated-tests.jar"]
