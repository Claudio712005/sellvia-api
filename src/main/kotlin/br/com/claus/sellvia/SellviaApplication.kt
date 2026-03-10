package br.com.claus.sellvia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SellviaApplication

fun main(args: Array<String>) {
    runApplication<SellviaApplication>(*args)
}