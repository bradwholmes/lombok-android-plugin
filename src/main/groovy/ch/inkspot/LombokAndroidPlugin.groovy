package ch.inkspot;

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.neenbedankt.gradle.androidapt.AndroidAptPlugin
import org.gradle.api.tasks.JavaExec

class LombokAndroidPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def hasAppPlugin = project.plugins.find { p -> p instanceof AppPlugin }
        def hasLibraryPlugin = project.plugins.find { p -> p instanceof LibraryPlugin }
        def hasAptPlugin = project.plugins.find { p -> p instanceof AndroidAptPlugin }

        if (!hasAppPlugin && !hasLibraryPlugin) throw new IllegalStateException("Not an android project")
        if (!hasAptPlugin) throw new IllegalStateException("Lombok not added with apt?")

        project.afterEvaluate {
            def lombokPublicApiJarPath = project.file(new File(project.projectDir, "/libs/"))

            def lombokDependency = project.configurations.apt
                    .find { dependency -> dependency.name.contains("lombok") }

            def publicApiCreationTask = project.tasks.create('generateLombokDependency', JavaExec)
            publicApiCreationTask.main = "-jar"
            publicApiCreationTask.args = [lombokDependency, "publicApi", lombokPublicApiJarPath]

            publicApiCreationTask.doFirst {
                if(!lombokPublicApiJarPath.exists()) {
                    lombokPublicApiJarPath.mkdirs()
                }
            }
        }
    }
}