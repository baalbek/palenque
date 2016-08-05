package rcstadheim.palenque

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

/**
 * Created by rcs on 03.01.16.
 *
 */

class LeinDepsTaskTest {
    @Test
    public void testLeinDeps() {
        Project project = ProjectBuilder.builder().build()
        def pf = project.file("build.gradle")
        def task = project.task('leinDeps', type: LeinDepsTask)
        assertTrue(task instanceof LeinDepsTask)
        //def t = task as LeinDepsTask
        //t.projFileName = '/home/rcs/opt/java/palenque/src/test/resources/project.clj'
        //t.execute()
    }
}
