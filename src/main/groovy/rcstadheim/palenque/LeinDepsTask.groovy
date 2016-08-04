package rcstadheim.palenque 

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Jar


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

//    task x << {
//        //def allc = getProject().getConfigurations().getAsMap()
//        //def compile = allc.get('compile')
//
//
//        def g = getProject().gradle
//        println g
//
//        //def g = getProject().gradle.taskGraph
//        //def tep = getTEP(g)
//        //def entryTasks = getEntryTasks(tep)
//
//        def subp = subprojects.findAll()
//        subp.each {
//            println it.projectDir
//            def at = it.getTasks()
//            Jar j = at.findByName('jar') as Jar
//            println j.archivePath
//        }
//        def bf = new File('build.gradle').text
//        bf.eachLine {
//
//        }
//    }

//    task y << {
//        def bf = new File('build.gradle').text
//        def compileMatcher = ~/.*compile.*|.*runtime.*/
//        def depMatcher = ~/.*dependencies+\s.*/
//        def depCloseMatcher = ~/.*}.*/
//        def depNameMatcher = ~/.*"(.*)".*/
//        def dollarMatcher = ~/(.*)\$(.*)/
//        def inDeps = false
//
//        bf.eachLine {
//            def m = it =~ depMatcher
//            if (m.matches()) {
//                inDeps = true
//            }
//            if (inDeps) {
//                def m2 = it =~ depCloseMatcher
//                if (m2.matches()) {
//                    inDeps = false
//                }
//                if (inDeps){
//                    def m3 = it =~ compileMatcher
//                    if (m3.matches()) {
//                        def m4 = it =~ depNameMatcher
//                        if (m4.matches()) {
//                            def depName = m4.group(1)
//                            def m5 = depName =~ dollarMatcher
//                            if (m5.matches()) {
//                                def m5split = m5.group(1).split(":")
//                                def depNamex = sprintf("%s:%s",m5split[0],m5split[1])
//                                printf("[%s \"%s\"]\n", depNamex, getProperty(m5.group(2)))
//                            }
//                            else {
//                                println depName
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }



    def getSplits() {
        //def pfile = new File("/home/rcs/opt/java/vegaq/project.clj").text
        //def pfile = new File(projFileName).text
        def pfile = getProjFile().text
        pfile.split(";deps")
    }

    def getLeinDeps() {
        def result = []
        //def allc = getProject().getConfigurations().getAsMap()
        //def compile = allc.get('compile')
        def compile = getConfigurations().getByName('compile')

        if (useLibsSymlink) {
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
