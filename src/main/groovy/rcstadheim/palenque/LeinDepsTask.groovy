package rcstadheim.palenque 

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction


class LeinDepsTask extends DefaultTask {


    String projFileName

    String gradleRepoPath = "/home/rcs/.gradle/caches/modules-2/files-2.1/"

    @Input
    boolean useLibsSymlink = true

    def getProjFile() {
        if (projFileName == null)
            return getProject().file("project.clj")
        else
            return new File(projFileName)
    }
    @TaskAction
    def curLeinDeps() {
        def ld = getLeinDeps()
        def splits = getSplits()

        File newPfile = getProjFile() //new File(getProjFileName())

        println newPfile

        newPfile.newWriter().withWriter { w ->
            splits[0].eachLine { l ->
                w << l << "\n"
            }
            w << "    ;deps\n"
            ld.each { d ->
                w << "\t\t" << d << "\n"
            }
            w << "    ;deps\n"
            //splits[2].eachLine { l ->
            splits.last().eachLine { l ->
                w << l << "\n"
            }
        }
    }

    static def makeLibPath(curLib) {
        "    \"libs/${curLib}\""
    }

    def getSplits() {
        //def pfile = new File("/home/rcs/opt/java/vegaq/project.clj").text
        //def pfile = new File(projFileName).text
        def pfile = getProjFile().text
        pfile.split(";deps")
    }

    def getLeinDeps() {
        def result = []
        def allc = getProject().getConfigurations().getAsMap()
        def compile = allc.get('compile')

        if (useLibsSymlink == true) {
            compile.each {
                def ar = it.canonicalPath.split(gradleRepoPath)
                if (ar.length == 1) {
                    result << "    \"${ar.getAt(0)}\""
                } else {
                    result << makeLibPath(ar.getAt(1))
                }
            }
        }
        else {
            compile.each {
                def unixPath = it.canonicalPath.replace("\\","/")
                result << "    \"$unixPath\""
            }
        }

        return result
    }
}
