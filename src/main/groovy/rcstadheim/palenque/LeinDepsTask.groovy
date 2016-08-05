package rcstadheim.palenque 

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Output
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Jar


class LeinDepsTask extends DefaultTask {


    @Input
    String leinFileName

    @Input
    String outputFileName

    String gradleRepoPath = "/home/rcs/.gradle/caches/modules-2/files-2.1/"

    def getLeinFile() {
        if (leinFileName == null)
            return getProject().file("project.clj")
        else
            return new File(leinFileName)
    }
    def getOutputFile() {
        if (outputFileName == null)
            return getProject().file("project.clj")
        else
            return new File(outputFileName)
    }

    @TaskAction
    def generateLeiningenFile() {
        def lds = leinTasks()
        lds.each {
            println it
        }
        def lrs = leinResources()
        lrs.each {
            println it
        }
    }

    def leinResources() {
        def result = []

        def subp = getProject().subprojects.findAll()
        subp.each {
            def at = it.getTasks()
            Jar j = at.findByName('jar') as Jar
            result.add(j.archivePath)
        }
        result
    }

    def leinTasks() {
        def result = []
        def bf = new File('build.gradle').text
        def compileMatcher = ~/.*compile.*|.*runtime.*/
        def depMatcher = ~/.*dependencies+\s.*/
        def depCloseMatcher = ~/.*}.*/
        def depNameMatcher = ~/.*"(.*)".*/
        def dollarMatcher = ~/(.*)\$(.*)/
        def inDeps = false

        bf.eachLine {
            def m = it =~ depMatcher
            if (m.matches()) {
                inDeps = true
            }
            if (inDeps) {
                def m2 = it =~ depCloseMatcher
                if (m2.matches()) {
                    inDeps = false
                }
                if (inDeps){
                    def m3 = it =~ compileMatcher
                    if (m3.matches()) {
                        def m4 = it =~ depNameMatcher
                        if (m4.matches()) {
                            def depName = m4.group(1)
                            def m5 = depName =~ dollarMatcher
                            if (m5.matches()) {
                                def ld = gradleDepWithParamToLeinDep(m5)
                                result.add(ld)
                            }
                            else {
                                def ld = gradleDepToLeinDep(depName)
                                result.add(ld)
                            }
                        }
                    }
                }
            }
        }
        result
    }

    def gradleDepWithParamToLeinDep(m) {
        def msplit = m.group(1).split(":")
        def depNamex = sprintf("%s:%s",msplit[0],msplit[1])
        def result = sprintf("[%s \"%s\"]", depNamex, getProject().getProperty(m.group(2)))
        //def result = sprintf("[%s \"%s\"]", depNamex, "1.0")
        result
    }

    def gradleDepToLeinDep(dep) {
        def s = dep.split(":")
        def result = sprintf("[%s:%s \"%s\"]", s[0], s[1], s[2])
        result
    }
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

