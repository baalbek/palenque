package rcstadheim.palenque 

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


class LeinDepsTask extends DefaultTask {

    String projFileName

    String gradleRepoPath = "/home/rcs/.gradle/caches/modules-2/files-2.1/"

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
        /*
        ld.each { c ->
            print c
        }
        splits.each { s ->
            println s
        }
        */
        //def newPfile = new File("/home/rcs/opt/java/vegaq/project.clj")

        File newPfile = getProjFile() //new File(getProjFileName())

        println newPfile

        newPfile.newWriter().withWriter { w ->
            splits[0].eachLine { l ->
                w << l << "\n"
            }
            w << "\t\t;deps\n"
            ld.each { d ->
                w << "\t\t" << d << "\n"
            }
            w << "\t\t;deps\n"
            //splits[2].eachLine { l ->
            splits.last().eachLine { l ->
                w << l << "\n"
            }
        }
    }

    static def makeLibPath(curLib) {
        "\t\"libs/${curLib}\""
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

        compile.each {
            def ar = it.canonicalPath.split(gradleRepoPath)
            if (ar.length == 1) {
                result << "\t\"${ar.getAt(0)}\""
            } else {
                result << makeLibPath(ar.getAt(1))
            }
        }

        return result
    }
}