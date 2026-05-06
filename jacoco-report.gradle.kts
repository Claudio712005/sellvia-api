import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

val layerConfigs =
    listOf(
        LayerConfig("domain", "🎯 ", 0.00),
        LayerConfig("application", "⚙️ ", 0.0),
        LayerConfig("infrastructure", "🔌 ", 0.00)
    )
val globalMinCoverage = 0.05

val jacocoExcludes =
    listOf(
        "**/entity/**",
        "**/dto/**",
        "**/config/**",
        "**/*Application*",
        "**/infrastructure/persistence/model/**"
    )

data class LayerConfig(val name: String, val icon: String, val min: Double) : java.io.Serializable

tasks.named<JacocoReport>("jacocoTestReport") {
    outputs.upToDateWhen { false }
    dependsOn(tasks.named("test"))

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        files(
            classDirectories.map {
                fileTree(it) { exclude(jacocoExcludes) }
            }
        )
    )

    val reportFileProperty = layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml")
    val htmlReportUri = layout.buildDirectory.dir("reports/jacoco/test/html").get().asFile.toURI().toString()

    doLast {
        val reportFile = reportFileProperty.get().asFile
        if (!reportFile.exists()) return@doLast

        val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        val doc = factory.newDocumentBuilder().parse(reportFile)

        val lineWidth = 100
        println("\n" + "=".repeat(lineWidth))
        println("📊 RELATÓRIO DE COBERTURA CONSOLIDADO - SELLVIA".padStart(lineWidth / 2 + 20))
        println("=".repeat(lineWidth))

        val packages = doc.getElementsByTagName("package")
        val packageCoverage = mutableMapOf<String, Pair<Double, Double>>()
        val layerTotals = mutableMapOf<String, Pair<Double, Double>>()

        for (i in 0 until packages.length) {
            val pkg = packages.item(i) as org.w3c.dom.Element
            val pkgName = pkg.getAttribute("name").replace("/", ".")
            val children = pkg.childNodes

            var covered = 0.0
            var missed = 0.0

            for (j in 0 until children.length) {
                val node = children.item(j)
                if (node is org.w3c.dom.Element && node.tagName == "counter" && node.getAttribute("type") == "INSTRUCTION") {
                    missed = node.getAttribute("missed").toDouble()
                    covered = node.getAttribute("covered").toDouble()
                }
            }

            if (covered + missed > 0) {
                val total = covered + missed
                packageCoverage[pkgName] = Pair(covered, total)

                layerConfigs.forEach { config ->
                    if (pkgName.contains(".${config.name}")) {
                        val current = layerTotals.getOrDefault(config.name, Pair(0.0, 0.0))
                        layerTotals[config.name] = Pair(current.first + covered, current.second + total)
                    }
                }
            }
        }

        layerConfigs.forEach { config ->
            val pkgs = packageCoverage.filter { it.key.contains(".${config.name}") }
            if (pkgs.isNotEmpty()) {
                pkgs.forEach { (name, stats) ->
                    val perc = (stats.first / stats.second) * 100
                    val percStr = "${"%.2f".format(perc)}%"
                    val prefix = "${config.icon} [${config.name.uppercase().padEnd(14)}] $name : "
                    println(prefix + " ".repeat((lineWidth - prefix.length - percStr.length).coerceAtLeast(0)) + " " + percStr)
                }
                val stats = layerTotals[config.name] ?: Pair(0.0, 1.0)
                val totalPerc = (stats.first / stats.second) * 100
                val totalStr = "TOTAL ${config.name.uppercase()}: ${"%.2f".format(totalPerc)}%"
                println(" ".repeat(lineWidth - totalStr.length) + totalStr)
                println("-".repeat(lineWidth))
            }
        }

        val rootCounters = doc.documentElement.childNodes
        var globalPerc = 0.0
        for (i in 0 until rootCounters.length) {
            val node = rootCounters.item(i)
            if (node is org.w3c.dom.Element && node.tagName == "counter" && node.getAttribute("type") == "INSTRUCTION") {
                val c = node.getAttribute("covered").toDouble()
                val m = node.getAttribute("missed").toDouble()
                globalPerc = (c / (c + m)) * 100
            }
        }

        println("ESTADO DO QUALITY GATE:")
        layerConfigs.forEach { config ->
            val stats = layerTotals[config.name] ?: Pair(0.0, 1.0)
            val currentPerc = (stats.first / stats.second) * 100
            val pass = currentPerc >= (config.min * 100)
            val statusIcon = if (pass) "✅" else "❌"
            val label = "  $statusIcon ${config.name.replaceFirstChar { it.uppercase() }} Coverage (Min ${(config.min * 100).toInt()}%):"
            val valStr = "${"%.2f".format(currentPerc)}%"
            println(label + " ".repeat((lineWidth - label.length - valStr.length).coerceAtLeast(0)) + valStr)
        }

        val gPass = globalPerc >= (globalMinCoverage * 100)
        val gLabel = "  ${if (gPass) "✅" else "❌"} Global Coverage (Min ${(globalMinCoverage * 100).toInt()}%):"
        val gVal = "${"%.2f".format(globalPerc)}%"
        println(gLabel + " ".repeat((lineWidth - gLabel.length - gVal.length).coerceAtLeast(0)) + gVal)

        println("=".repeat(lineWidth))
    }
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    classDirectories.setFrom(
        files(
            classDirectories.map {
                fileTree(it) { exclude(jacocoExcludes) }
            }
        )
    )

    violationRules {
        rule {
            element = "BUNDLE"
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = globalMinCoverage.toBigDecimal()
            }
        }
        layerConfigs.forEach { config ->
            rule {
                element = "PACKAGE"
                includes = listOf("br.com.claus.sellvia.${config.name}.*")
                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = config.min.toBigDecimal()
                }
            }
        }
    }
}