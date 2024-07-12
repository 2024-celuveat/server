package com.celuveat.common.port.out

import com.celuveat.member.application.port.out.FetchOAuthMemberPort
import com.celuveat.member.application.port.out.FindMemberPort
import com.celuveat.member.application.port.out.SaveMemberPort
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK

class MockDependencies {

    @MockK
    lateinit var saveMemberPort: SaveMemberPort

    @MockK
    lateinit var findMemberPort: FindMemberPort

    @MockK
    lateinit var fetchOAuthMemberPort: FetchOAuthMemberPort

    init {
        MockKAnnotations.init(this)
    }
}
