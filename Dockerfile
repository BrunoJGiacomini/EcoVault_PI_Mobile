FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive \
    JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 \
    ANDROID_HOME=/opt/android-sdk \
    ANDROID_SDK_ROOT=/opt/android-sdk \
    GRADLE_USER_HOME=/root/.gradle

ENV PATH=${PATH}:${JAVA_HOME}/bin:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

RUN apt-get update && apt-get install -y --no-install-recommends \
        openjdk-17-jdk \
        curl \
        ca-certificates \
        unzip \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    curl --fail --location --retry 3 \
        https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip \
        -o /tmp/android-commandlinetools.zip && \
    unzip -q /tmp/android-commandlinetools.zip -d ${ANDROID_HOME}/cmdline-tools && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm /tmp/android-commandlinetools.zip

RUN yes | sdkmanager --licenses >/dev/null && \
    sdkmanager \
        "platform-tools" \
        "platforms;android-36" \
        "build-tools;36.0.0"

WORKDIR /workspace
COPY . /workspace

RUN chmod +x gradlew

CMD ["./gradlew", "testDebugUnitTest", "assembleDebug", "--stacktrace"]
