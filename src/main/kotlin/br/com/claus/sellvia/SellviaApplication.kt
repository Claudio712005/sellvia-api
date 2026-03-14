package br.com.claus.sellvia

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SellviaApplication

fun main(args: Array<String>) {
    EnvLoader.load()

    runApplication<SellviaApplication>(*args)
}

object EnvLoader {
    fun load() {
        val dotenv =
            dotenv {
                ignoreIfMissing = true
                ignoreIfMalformed = true
            }

        dotenv.entries().forEach {
            if (System.getenv(it.key) == null) {
                System.setProperty(it.key, it.value)
            }
        }
    }
}