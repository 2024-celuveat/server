package com.celuveat.sample.adapter.`in`.rest

import com.celuveat.sample.adapter.`in`.rest.request.SaveSampleRequest
import com.celuveat.sample.application.port.`in`.SaveSampleUseCase
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI

@Controller
class SampleController(
    val saveSampleUseCase: SaveSampleUseCase,
) : SampleApi {

    @GetMapping("/sample")
    override fun sample(
        @RequestBody request: SaveSampleRequest,
    ): ResponseEntity<Long> {
        val command = request.toCommand()
        val sample = saveSampleUseCase.saveSample(command)
        return ResponseEntity.created(URI.create("/sample/${sample.id}")).build()
    }
}
