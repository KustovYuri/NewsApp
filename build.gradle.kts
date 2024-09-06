import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetpack.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.detekt) apply false
}

allprojects.forEach {project->
    project.afterEvaluate{
        with(project.plugins){
            if(hasPlugin(libs.plugins.jetpack.compose.compiler.get().pluginId)){
                tasks.withType<KotlinCompilationTask<*>>().configureEach {
                    compilerOptions{
                        allWarningsAsErrors = false

                        freeCompilerArgs.addAll(
                            "-P",
                            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                                    layout.buildDirectory.asFile.get().absolutePath + "/compose_metrics",
                        )

                        freeCompilerArgs.addAll(
                            "-P",
                            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                                    layout.buildDirectory.asFile.get().absolutePath + "/compose_metrics",
                        )
                    }
                }
            }
        }
    }
}

allprojects.forEach { project ->
    project.afterEvaluate {
        with(project.plugins) {
            if (
                hasPlugin(libs.plugins.jetbrains.kotlin.android.get().pluginId) ||
                hasPlugin(libs.plugins.jetbrains.kotlin.jvm.get().pluginId)
            ) {
                apply(libs.plugins.detekt.get().pluginId)

                project.extensions.configure<DetektExtension> {
                    config.setFrom(file("config/detekt/detekt.yml"))
                }
                project.dependencies.add("detektPlugins", libs.detekt.formatting.get().toString())
            }
        }
    }
}
