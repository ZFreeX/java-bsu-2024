plugins {
    id("java")
}

group = "by.bsu.dependency"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.reflections:reflections:0.10.2")
    implementation ("ch.qos.logback:logback-classic:1.2.6") // Check for the latest version
    implementation ("org.slf4j:slf4j-api:1.7.32") // Check for the latest version
    compileOnly("org.projectlombok:lombok:1.18.34")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.test {
    useJUnitPlatform()
}
