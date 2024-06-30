package com.celuveat.sample.adapter.`in`.rest.request

import com.celuveat.sample.application.port.`in`.command.SaveSampleCommand

data class SaveSampleRequest(
    val name: String,
) {

    fun toCommand(): SaveSampleCommand {
        return SaveSampleCommand(name)
    }
}
