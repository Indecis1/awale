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
    // https://mvnrepository.com/artifact/org.eclipse.paho/org.eclipse.paho.client.mqttv3
    testImplementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.1")

}

tasks.test {
    useJUnitPlatform()
}