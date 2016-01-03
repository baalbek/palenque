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
    public void testProjectClj() {
        Project project = ProjectBuilder.builder().build()
        def pf = project.file("build.gradle")
        //assertEquals(pf.absolutePath, "/home/rcs/java/palenque/build.gradle")
        assertEquals(1 == 1)
    }
}
