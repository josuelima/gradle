/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.project.taskfactory

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import spock.lang.Issue

class TaskPropertyNamingIntegrationTest extends AbstractIntegrationSpec {

    @Issue("https://issues.gradle.org/browse/GRADLE-3538")
    def "names of annotated properties are used in property specs"() {
        file("input.txt").createNewFile()
        file("input1.txt").createNewFile()
        file("input2.txt").createNewFile()
        file("inputs").createDir()
        file("inputs/inputA.txt").createNewFile()
        file("inputs/inputB.txt").createNewFile()
        file("inputs1").createDir()
        file("inputs2").createDir()

        buildFile << """
            class MyTask extends DefaultTask {
                @Input String inputString
                @InputFile File inputFile
                @InputDirectory File inputDirectory
                @InputFiles FileCollection inputFiles

                @OutputFile File outputFile
                @OutputFiles FileCollection outputFiles
                @OutputDirectory File outputDirectory
                @OutputDirectories FileCollection outputDirectories
            }

            task myTask(type: MyTask) {
                inputString = "data"
                inputFile = file("input.txt")
                inputDirectory = file("inputs")
                inputFiles = files("input1.txt", "input2.txt")

                outputFile = file("output.txt")
                outputFiles = files("output1.txt", "output2.txt")
                outputDirectory = file("outputs")
                outputDirectories = files("outputs1", "outputs2")

                doLast {
                    inputs.fileProperties.each { property ->
                        println "Input: \${property.propertyName} \${property.propertyFiles.files*.name}"
                    }
                    outputs.fileProperties.each { property ->
                        println "Output: \${property.propertyName} \${property.propertyFiles.files*.name}"
                    }
                }
            }
        """
        when:
        run "myTask"
        then:
        output.contains "Input: inputDirectory [inputA.txt, inputB.txt]"
        output.contains "Input: inputFile [input.txt]"
        output.contains "Input: inputFiles [input1.txt, input2.txt]"
        output.contains "Output: outputDirectories\$1 [outputs1]"
        output.contains "Output: outputDirectories\$2 [outputs2]"
        output.contains "Output: outputDirectory [outputs]"
        output.contains "Output: outputFile [output.txt]"
        output.contains "Output: outputFiles\$1 [output1.txt]"
        output.contains "Output: outputFiles\$2 [output2.txt]"
    }
}