package de.timokrates.microuserverse.permissions

import io.kotlintest.specs.AnnotationSpec
import io.ktor.server.testing.withTestApplication

class ApplicationTest : AnnotationSpec() {

    @Test
    fun `INT - add permission`() {
        withTestApplication ({
            main()
        }) {

        }
    }
}