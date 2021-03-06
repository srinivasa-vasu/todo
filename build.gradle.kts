import org.springframework.boot.buildpack.platform.build.PullPolicy.*

plugins {
	id("org.springframework.boot") version "2.4.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("java")
}

group = "io.humourmind"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

tasks.bootJar {
	layered {
		isEnabled = true
	}
}

tasks.bootBuildImage {
	environment = mapOf("BP_JVM_VERSION" to "11.0.7",
			"BPL_JVM_HEAD_ROOM" to "5")
//	imageName = "humourmind/${project.name}:${project.version}"
//	publish = true
//	environment = listOf("BP_BOOT_NATIVE_IMAGE" = "1", "BP_BOOT_NATIVE_IMAGE_BUILD_ARGUMENTS" = "-Dspring.native.remove-yaml-support=true -Dspring.native.remove-jmx-support=true -Dspring.native.remove-spel-support=true -H =TraceClassInitialization=true --initialize-at-build-time=org.springframework.boot.logging")
	pullPolicy = IF_NOT_PRESENT
	isVerboseLogging = true
	imageName = "harbor.sys.humourmind.com/kna/todo-cnb:1.0"
//	environment = listOf("BP_BOOT_NATIVE_IMAGE" = "1", "BP_JVM_VERSION" = "11")
//	builder = "paketobuildpacks/builder:tiny"
	builder = "humourmind/paketo-java-builder-tiny@sha256:40be20ed070cce98f6cfa3b9b588919502cc5f4f7ee330d19ec28cddf7d985bb"
}

dependencies {
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springdoc:springdoc-openapi-webflux-ui:1.5.2")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	runtimeOnly("io.r2dbc:r2dbc-postgresql")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
