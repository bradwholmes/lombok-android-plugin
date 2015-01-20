package ch.inkspotch

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

class LombokAndroidPluginTest {
    @Test
    public void shouldApplyPlugin() {
        Project project = getAndroidProject()

        project.apply plugin: 'ch.inkspot.lombok-android'

        // expect no exceptions
    }

    @Test
    public void shouldAddLombokPublicApiJarToProvided() {
        Project project = getAndroidProject()
        project.apply plugin: 'ch.inkspot.lombok-android'

        project.evaluate()

        assertTrue project.configurations.provided.dependencies.any()
    }

    private Project getAndroidProject() {
        Project project = ProjectBuilder.builder()
                .withProjectDir(new File("app"))
                .build()

        project.apply plugin: 'com.android.application'
        project.apply plugin: 'android'
        project.apply plugin: 'com.neenbedankt.android-apt'

        project.repositories { mavenCentral() }
        project.dependencies { apt 'org.projectlombok:lombok:1.14.8' }

        project.android {
            compileSdkVersion 21
            buildToolsVersion '21.1.1'
        }

        project
    }
}
