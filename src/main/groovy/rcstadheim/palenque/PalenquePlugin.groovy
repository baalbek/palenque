package rcstadheim.palenque 

import org.gradle.api.Project
import org.gradle.api.Plugin

class PalenquePlugin implements Plugin<Project> {
    void apply(Project target) {
        target.task('leinDeps', type: LeinDepsTask)
        target.task('leinResources', type: LeinResourcesTask)
    }
}
