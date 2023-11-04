plugins {
    id("java")
}

group = "fr.univ_cote_azur.ai_game_programming"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.apache.logging.log4j:log4j-core:2.21.1")
}

tasks.test {
    useJUnitPlatform()
}