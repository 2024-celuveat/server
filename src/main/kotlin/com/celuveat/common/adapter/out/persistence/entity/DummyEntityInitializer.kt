package com.celuveat.common.adapter.out.persistence.entity

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaRepository
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.member.domain.SocialLoginType
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import java.time.LocalDate
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Suppress("ktlint:standard:max-line-length")
@Component
@Profile("local", "prod")
class DummyEntityInitializer(
    private val memberJpaRepository: MemberJpaRepository,
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val youtubeContentJpaRepository: YoutubeContentJpaRepository,
    private val celebrityYoutubeContentJpaRepository: CelebrityYoutubeContentJpaRepository,
    private val videoJpaRepository: VideoJpaRepository,
    private val restaurantInVideoJpaRepository: RestaurantInVideoJpaRepository,
    private val celebrityRestaurantJpaRepository: CelebrityRestaurantJpaRepository,
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val restaurantImageJpaRepository: RestaurantImageJpaRepository,
) {
    @EventListener(ApplicationReadyEvent::class)
    fun initializeData() {
        saveMember()

        val savedRestaurants = saveRestaurants()

        saveRestaurantImages(savedRestaurants)

        val savedCelebrities = saveCelebrities()

        val savedYoutubeContents = saveYoutubeContents()

        val savedVideos = saveVideos(savedYoutubeContents)

        saveCelebrityYoutubeContents(savedCelebrities, savedYoutubeContents)
        saveRestaurantInVideos(savedVideos, savedRestaurants, savedCelebrities)
    }

    private fun saveMember(): MemberJpaEntity {
        val member = MemberJpaEntity(
            nickname = "celuveat",
            profileImageUrl = "https://www.celuveat.com/images-data/jpeg/celuveat-logo.png",
            email = "email@celuveat.com",
            socialId = "1234567890",
            serverType = SocialLoginType.KAKAO,
        )
        return memberJpaRepository.save(member)
    }

    private fun saveRestaurants(): List<RestaurantJpaEntity> {
        val restaurants = listOf(
            RestaurantJpaEntity(
                name = "맛집_1",
                latitude = 37.5075029,
                longitude = 127.0268078,
                phoneNumber = "0507-1415-1113",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "요리주점",
                roadAddress = "서울 강남구 강남대로118길 47 2층",
                naverMapUrl = "https://map.naver.com/v5/entry/place/38252334?lng=127.02682069999999&lat=37.50750299999999&placePath=%2Fhome&entry=plt",
            ),
            RestaurantJpaEntity(
                name = "맛집_2",
                latitude = 37.5206993,
                longitude = 127.019975,
                phoneNumber = "0507-1401-0517",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "일식당",
                roadAddress = "서울 강남구 압구정로2길 15 1층 카이센동우니도",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1720070048?entry=plt&c=15,0,0,0,dh",
            ),
            RestaurantJpaEntity(
                name = "맛집_3",
                latitude = 37.5272713,
                longitude = 127.0354068,
                phoneNumber = "0507-1498-1171",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "한식",
                roadAddress = "서울 강남구 압구정로42길 25-3 1층",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1204924395?entry=plt&c=15,0,0,0,dh",
            ),
            RestaurantJpaEntity(
                name = "맛집_4",
                latitude = 37.5572713,
                longitude = 126.9466788,
                phoneNumber = "0507-1366-4573",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "초밥,롤",
                roadAddress = "서울 서대문구 이화여대2길 14 1층 하늘초밥",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1960457705?entry=plt&c=15,0,0,0,dh",
            ),
            RestaurantJpaEntity(
                name = "맛집_5",
                latitude = 37.5149238,
                longitude = 126.9389205,
                phoneNumber = "010-3669-5707",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "생선회",
                roadAddress = "서울 동작구 노들로 674 노량진수산시장 1층 활어 160, 161호",
                naverMapUrl = "https://map.naver.com/v5/entry/place/21290261?entry=plt&c=15,0,0,0,dh",
            ),
            RestaurantJpaEntity(
                name = "맛집_6",
                latitude = 37.60357,
                longitude = 126.9248002,
                phoneNumber = "02-356-8898",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "생선회",
                roadAddress = "서울 은평구 서오릉로 29-4 1층",
                naverMapUrl = "https://map.naver.com/v5/entry/place/35444584?entry=plt&c=19,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_7",
                latitude = 37.4835692,
                longitude = 126.9006767,
                phoneNumber = "02-866-3779",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "생선회",
                roadAddress = "서울 구로구 디지털로32길 97-13",
                naverMapUrl = "https://map.naver.com/v5/entry/place/32816646?entry=plt&c=19,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_8",
                latitude = 37.5890607,
                longitude = 127.0884496,
                phoneNumber = "0507-1322-4459",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "생선회",
                roadAddress = "서울 중랑구 겸재로 187-10 1층 슌락",
                naverMapUrl = "https://map.naver.com/v5/entry/place/33758704?entry=plt&c=19,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_9",
                latitude = 37.4962666,
                longitude = 127.0339938,
                phoneNumber = "0507-1416-3677",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "생선회",
                roadAddress = "서울 강남구 논현로79길 62 백악빌딩 1층 고래불",
                naverMapUrl = "https://map.naver.com/v5/entry/place/38233690?entry=plt&c=15,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_10",
                latitude = 37.5660175,
                longitude = 126.8129257,
                phoneNumber = "02-2699-2690",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "한식",
                roadAddress = "서울 강서구 방화동로10길 7",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1171129562?lng=126.81293779999997&lat=37.5659967&placePath=%2Fhome&entry=plt&c=15,0,0,0,dh",
            ),
            RestaurantJpaEntity(
                name = "맛집_11",
                latitude = 37.5422708,
                longitude = 127.1332702,
                phoneNumber = "02-482-2562",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "한식",
                roadAddress = "서울 강동구 진황도로27길 91",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1640035966?entry=plt&c=15,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_12",
                latitude = 37.5625372,
                longitude = 126.8086881,
                phoneNumber = "0507-1375-7602",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "일식당",
                roadAddress = "서울 강서구 방화동로 11 1층",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1889864154?entry=plt&c=15,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_13",
                latitude = 37.5627465,
                longitude = 126.8099223,
                phoneNumber = "0507-1369-8458",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "이자카야",
                roadAddress = "서울 강서구 방화동로 18-7 1층 아키",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1064847686?entry=plt&c=15,0,0,0,dh",
            ),
            RestaurantJpaEntity(
                name = "맛집_14",
                latitude = 37.5505895,
                longitude = 126.9557918,
                phoneNumber = "02-3211-4468",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "게요리",
                roadAddress = "서울 마포구 마포대로 186-6",
                naverMapUrl = "https://map.naver.com/v5/entry/place/19862126?entry=plt&c=12,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_15",
                latitude = 37.4934824,
                longitude = 127.1115038,
                phoneNumber = "02-6978-1765",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "생선회",
                roadAddress = "서울 송파구 양재대로 932 1층 수산부 C-25호",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1979269941?entry=plt&c=15,0,0,0,dh",
            ),
            RestaurantJpaEntity(
                name = "맛집_16",
                latitude = 37.5067751,
                longitude = 127.0580154,
                phoneNumber = "0507-1472-8081",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "해산물뷔페",
                roadAddress = "서울 강남구 테헤란로 508 해성2빌딩 지하1층",
                naverMapUrl = "https://map.naver.com/v5/entry/place/11684863?entry=plt&c=15,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_17",
                latitude = 37.550704,
                longitude = 127.172235,
                phoneNumber = "0507-1407-9482",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "초밥,롤",
                roadAddress = "서울 강동구 상일로 39-8",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1400532181?entry=plt&c=15,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_18",
                latitude = 37.5593797,
                longitude = 126.8347339,
                phoneNumber = "0507-1438-9876",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "일식,초밥뷔페",
                roadAddress = "서울 강서구 공항대로 247 퀸즈파크나인 b103, b104호",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1756519625?entry=plt&c=15,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_19",
                latitude = 36.03429,
                longitude = 129.3792307,
                phoneNumber = "054-242-1771",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "기사식당",
                roadAddress = "경북 포항시 남구 송도로 75",
                naverMapUrl = "https://map.naver.com/v5/entry/place/15678172?entry=plt&c=15,0,0,0,dh&isCorrectAnswer=true",
            ),
            RestaurantJpaEntity(
                name = "맛집_20",
                latitude = 37.5475282,
                longitude = 126.9546762,
                phoneNumber = "0507-1497-2210",
                businessHours = "매일 11:00 ~ 17:00",
                introduction = "맛집입니다.",
                category = "일식당",
                roadAddress = "서울 마포구 마포대로12길 34 소담빌딩 2층 201호 스시복",
                naverMapUrl = "https://map.naver.com/v5/entry/place/1155175292?entry=plt&c=15,0,0,0,dh&isCorrectAnswer=true",
            ),
        )
        return restaurantJpaRepository.saveAll(restaurants)
    }

    private fun saveRestaurantImages(restaurants: List<RestaurantJpaEntity>) {
        val imageSampleUrl = listOf(
            "https://www.celuveat.com/images-data/webp/bXlfX3N0ZWxsYS0x.webp",
            "https://www.celuveat.com/images-data/webp/Z3VfX19fX19fX19fX19fYS0x.webp",
            "https://www.celuveat.com/images-data/webp/bW9vZ2V1bmJvbi0y.webp",
            "https://www.celuveat.com/images-data/webp/bW9vZ2V1bmJvbi00.webp",
            "https://www.celuveat.com/images-data/webp/bW9vZ2V1bmJvbi01.webp",
            "https://www.celuveat.com/images-data/webp/Zm9vZGllX3Npc3dhXzFfQ2M5VVpneGhvemg.webp",
            "https://www.celuveat.com/images-data/webp/MTc5Y21naXJsLTQ.webp",
            "https://www.celuveat.com/images-data/webp/Zm9vZGllX3Npc3dhXzJfQ2M5VVpneGhvemg.webp",
            "https://www.celuveat.com/images-data/webp/MTc5Y21naXJsLTM.webp",
        )

        for (restaurant in restaurants) {
            val imageCount = (0..3).random()
            (0..imageCount).map {
                val restaurantImage = if (it == 1) {
                    RestaurantImageJpaEntity(
                        restaurant = restaurant,
                        name = "대표이미지",
                        author = "celuveat",
                        url = imageSampleUrl.random(),
                        isThumbnail = true,
                    )
                } else {
                    RestaurantImageJpaEntity(
                        restaurant = restaurant,
                        name = "이미지_$it",
                        author = "celuveat",
                        url = imageSampleUrl.random(),
                        isThumbnail = false,
                    )
                }
                restaurantImageJpaRepository.save(restaurantImage)
            }
        }
    }

    private fun saveCelebrities(): List<CelebrityJpaEntity> {
        val celebrities = listOf(
            CelebrityJpaEntity(
                name = "성시경",
                profileImageUrl = "https://yt3.googleusercontent.com/vQrdlCaT4Tx1axJtSUa1oxp2zlnRxH-oMreTwWqB-2tdNFStIOrWWw-0jwPvVCUEjm_MywltBFY=s176-c-k-c0x00ffffff-no-rj",
                introduction = "성시경",
            ),
            CelebrityJpaEntity(
                name = "이원일",
                profileImageUrl = "https://search.pstatic.net/common?type=b&size=216&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2Fportrait%2F201808%2F20180801002809461.jpg",
                introduction = "셰프 이원일",
            ),
            CelebrityJpaEntity(
                name = "홍석천",
                profileImageUrl = "https://search.pstatic.net/common?type=b&size=216&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2Fportrait%2F201404%2F20140405221057493-4339837.jpg",
                introduction = "홍석천",
            ),
            CelebrityJpaEntity(
                name = "먹적",
                profileImageUrl = "https://yt3.googleusercontent.com/ytc/AOPolaQ0vUJt9JWhig6GY1lWLPt_qIRiH-cKgO5Nnl5uicQ=s176-c-k-c0x00ffffff-no-rj",
                introduction = "스시 먹적",
            ),
            CelebrityJpaEntity(
                name = "빅페이스",
                profileImageUrl = "https://yt3.googleusercontent.com/ytc/AOPolaTCwJ_nAk7CRvRT5S6fL7pfYse6O7IZeANzxGamDQ=s176-c-k-c0x00ffffff-no-rj",
                introduction = "빅페이스",
            ),
        )
        return celebrityJpaRepository.saveAll(celebrities)
    }

    private fun saveYoutubeContents(): List<YoutubeContentJpaEntity> {
        val youtubeContents = listOf(
            YoutubeContentJpaEntity(
                contentsName = "먹을텐데",
                channelId = "@sungsikyung",
                channelUrl = "https://www.youtube.com/@sungsikyung",
                channelName = "성시경 SUNG SI KYUNG",
                contentsIntroduction = "",
                restaurantCount = 0,
                subscriberCount = 1_960_000,
            ),
            YoutubeContentJpaEntity(
                contentsName = "미식은 경험이다",
                channelId = "@gaypig1111",
                channelUrl = "https://www.youtube.com/@gaypig1111",
                channelName = "홍석천이원일",
                contentsIntroduction = """
                    나 홍석천, 원일이 너랑 
                    우리 둘이 하면 뭔가 되지 않겠냐?
                    안 될라나? 되겠지? 아닌가? 깅가? 안깅가? ㅡㅡ?
                    에~~~~ 라 나가보자 그냥~ 일단  놀아보자~ 꺄흐흥.

                    TOP게2와 미들🐷(탑 정도는 아님)의 유튜버 도전기.
                """.trimIndent(),
                restaurantCount = 0,
                subscriberCount = 268_000,
            ),
            YoutubeContentJpaEntity(
                contentsName = "과장말고 사장하자",
                channelId = "@lets_ceo",
                channelUrl = "https://www.youtube.com/@lets_ceo",
                channelName = "과장말고 사장하자",
                contentsIntroduction = "창업 박람회 주최사 월드전람의 자영업자를 위한 유튜브 프로젝트",
                restaurantCount = 0,
                subscriberCount = 149_000,
            ),
            YoutubeContentJpaEntity(
                contentsName = "먹적",
                channelId = "@monstergourmet",
                channelUrl = "https://www.youtube.com/@lets_ceo",
                channelName = "먹적 - (스시에 대출 박는 놈)",
                contentsIntroduction = "스시에 미친 놈",
                restaurantCount = 0,
                subscriberCount = 209_000,
            ),
            YoutubeContentJpaEntity(
                contentsName = "빅페이스",
                channelId = "@bigfacetv",
                channelUrl = "https://www.youtube.com/@bigfacetv",
                channelName = "빅페이스 BIGFACE",
                contentsIntroduction = """
                    재상이 김치 이거 엄청 묵고 간데이~
                    부터 시작된 본격 대두의 맛집 리뷰 채널!
                """.trimIndent(),
                restaurantCount = 0,
                subscriberCount = 836_000,
            ),
        )
        return youtubeContentJpaRepository.saveAll(youtubeContents)
    }

    private fun saveVideos(youtubeContents: List<YoutubeContentJpaEntity>): List<VideoJpaEntity> {
        val videos = listOf(
            VideoJpaEntity(
                youtubeContent = youtubeContents[0],
                videoUrl = "https://www.youtube.com/watch?v=AcShBsKZW8w",
                uploadDate = LocalDate.of(2024, 7, 31),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[0],
                videoUrl = "https://www.youtube.com/watch?v=_Rskex6miE4",
                uploadDate = LocalDate.of(2024, 7, 24),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[0],
                videoUrl = "https://www.youtube.com/watch?v=Un7Nbfh42hg",
                uploadDate = LocalDate.of(2024, 7, 15),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[1],
                videoUrl = "https://www.youtube.com/watch?v=T5pihF2Eu_I",
                uploadDate = LocalDate.of(2024, 7, 26),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[1],
                videoUrl = "https://www.youtube.com/watch?v=ZU1zp72Mj_U",
                uploadDate = LocalDate.of(2024, 7, 5),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[1],
                videoUrl = "https://www.youtube.com/watch?v=sM6CElFbsZc&t=1007s",
                uploadDate = LocalDate.of(2024, 6, 28),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[1],
                videoUrl = "https://www.youtube.com/watch?v=o05JGiEPrpo",
                uploadDate = LocalDate.of(2024, 5, 21),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[2],
                videoUrl = "https://www.youtube.com/watch?v=1mVT2uLuWV8&t=7s",
                uploadDate = LocalDate.of(2024, 7, 2),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[2],
                videoUrl = "https://www.youtube.com/watch?v=_Oo3Ji4MVZU",
                uploadDate = LocalDate.of(2024, 6, 30),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[2],
                videoUrl = "https://www.youtube.com/watch?v=R5-Bvl9utic",
                uploadDate = LocalDate.of(2024, 6, 28),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[3],
                videoUrl = "https://www.youtube.com/watch?v=Ls2I6t-vj6M",
                uploadDate = LocalDate.of(2024, 8, 11),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[3],
                videoUrl = "https://www.youtube.com/watch?v=_5O1MuzceXc",
                uploadDate = LocalDate.of(2024, 8, 9),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[4],
                videoUrl = "https://www.youtube.com/watch?v=qFhUe5Jycf8",
                uploadDate = LocalDate.of(2024, 8, 11),
            ),
            VideoJpaEntity(
                youtubeContent = youtubeContents[4],
                videoUrl = "https://www.youtube.com/watch?v=HiG0_0yW32Y",
                uploadDate = LocalDate.of(2024, 3, 24),
            ),
        )
        return videoJpaRepository.saveAll(videos)
    }

    private fun saveCelebrityYoutubeContents(
        savedCelebrities: List<CelebrityJpaEntity>,
        savedYoutubeContents: List<YoutubeContentJpaEntity>,
    ) {
        val celebrityYoutubeContents = listOf(
            CelebrityYoutubeContentJpaEntity(
                celebrity = savedCelebrities[0],
                youtubeContent = savedYoutubeContents[0],
            ),
            CelebrityYoutubeContentJpaEntity(
                celebrity = savedCelebrities[1],
                youtubeContent = savedYoutubeContents[1],
            ),
            CelebrityYoutubeContentJpaEntity(
                celebrity = savedCelebrities[2],
                youtubeContent = savedYoutubeContents[1],
            ),
            CelebrityYoutubeContentJpaEntity(
                celebrity = savedCelebrities[1],
                youtubeContent = savedYoutubeContents[2],
            ),
            CelebrityYoutubeContentJpaEntity(
                celebrity = savedCelebrities[2],
                youtubeContent = savedYoutubeContents[2],
            ),
            CelebrityYoutubeContentJpaEntity(
                celebrity = savedCelebrities[3],
                youtubeContent = savedYoutubeContents[3],
            ),
            CelebrityYoutubeContentJpaEntity(
                celebrity = savedCelebrities[4],
                youtubeContent = savedYoutubeContents[4],
            ),
        )
        celebrityYoutubeContentJpaRepository.saveAll(celebrityYoutubeContents)
    }

    private fun saveRestaurantInVideos(
        savedVideos: List<VideoJpaEntity>,
        savedRestaurants: List<RestaurantJpaEntity>,
        savedCelebrities: List<CelebrityJpaEntity>,
    ) {
        val restaurantsInVideoOfContentA = listOf(
            RestaurantInVideoJpaEntity(
                video = savedVideos[0],
                restaurant = savedRestaurants[0],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[0],
                restaurant = savedRestaurants[10],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[1],
                restaurant = savedRestaurants[1],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[2],
                restaurant = savedRestaurants[2],
            ),
        )
        val celebrityRestaurantA = listOf(
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[0],
                restaurant = savedRestaurants[0],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[0],
                restaurant = savedRestaurants[10],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[0],
                restaurant = savedRestaurants[1],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[0],
                restaurant = savedRestaurants[2],
            ),
        )

        val restaurantsInVideoOfContentB = listOf(
            RestaurantInVideoJpaEntity(
                video = savedVideos[3],
                restaurant = savedRestaurants[1],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[4],
                restaurant = savedRestaurants[11],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[5],
                restaurant = savedRestaurants[3],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[6],
                restaurant = savedRestaurants[4],
            ),
        )
        val restaurantsInVideoOfContentC = listOf(
            RestaurantInVideoJpaEntity(
                video = savedVideos[7],
                restaurant = savedRestaurants[5],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[8],
                restaurant = savedRestaurants[6],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[8],
                restaurant = savedRestaurants[7],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[9],
                restaurant = savedRestaurants[8],
            ),
        )
        val celebrityRestaurantBC = listOf(
            // [셀럽B] -> [컨텐츠B, C] 에 출연
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[1],
                restaurant = savedRestaurants[1],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[1],
                restaurant = savedRestaurants[11],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[1],
                restaurant = savedRestaurants[3],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[1],
                restaurant = savedRestaurants[4],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[1],
                restaurant = savedRestaurants[5],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[1],
                restaurant = savedRestaurants[6],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[1],
                restaurant = savedRestaurants[7],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[1],
                restaurant = savedRestaurants[8],
            ),
            // [셀럽C] -> [컨텐츠B, C] 에 출연
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[2],
                restaurant = savedRestaurants[1],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[2],
                restaurant = savedRestaurants[11],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[2],
                restaurant = savedRestaurants[3],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[2],
                restaurant = savedRestaurants[4],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[2],
                restaurant = savedRestaurants[5],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[2],
                restaurant = savedRestaurants[6],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[2],
                restaurant = savedRestaurants[7],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[2],
                restaurant = savedRestaurants[8],
            ),
        )

        val restaurantsInVideoOfContentD = listOf(
            RestaurantInVideoJpaEntity(
                video = savedVideos[10],
                restaurant = savedRestaurants[9],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[10],
                restaurant = savedRestaurants[1],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[11],
                restaurant = savedRestaurants[10],
            ),
        )
        val celebrityRestaurantD = listOf(
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[3],
                restaurant = savedRestaurants[9],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[3],
                restaurant = savedRestaurants[1],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[3],
                restaurant = savedRestaurants[10],
            ),
        )

        val restaurantsInVideoOfContentE = listOf(
            RestaurantInVideoJpaEntity(
                video = savedVideos[12],
                restaurant = savedRestaurants[12],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[12],
                restaurant = savedRestaurants[13],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[13],
                restaurant = savedRestaurants[14],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[13],
                restaurant = savedRestaurants[15],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[13],
                restaurant = savedRestaurants[16],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[13],
                restaurant = savedRestaurants[17],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[13],
                restaurant = savedRestaurants[18],
            ),
            RestaurantInVideoJpaEntity(
                video = savedVideos[13],
                restaurant = savedRestaurants[19],
            ),
        )

        val celebrityRestaurantE = listOf(
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[4],
                restaurant = savedRestaurants[12],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[4],
                restaurant = savedRestaurants[13],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[4],
                restaurant = savedRestaurants[14],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[4],
                restaurant = savedRestaurants[15],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[4],
                restaurant = savedRestaurants[16],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[4],
                restaurant = savedRestaurants[17],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[4],
                restaurant = savedRestaurants[18],
            ),
            CelebrityRestaurantJpaEntity(
                celebrity = savedCelebrities[4],
                restaurant = savedRestaurants[19],
            ),
        )

        restaurantInVideoJpaRepository.saveAll(
            listOf(
                *restaurantsInVideoOfContentA.toTypedArray(),
                *restaurantsInVideoOfContentB.toTypedArray(),
                *restaurantsInVideoOfContentC.toTypedArray(),
                *restaurantsInVideoOfContentD.toTypedArray(),
                *restaurantsInVideoOfContentE.toTypedArray(),
            ),
        )
        celebrityRestaurantJpaRepository.saveAll(
            listOf(
                *celebrityRestaurantA.toTypedArray(),
                *celebrityRestaurantBC.toTypedArray(),
                *celebrityRestaurantD.toTypedArray(),
                *celebrityRestaurantE.toTypedArray(),
            ),
        )
    }
}
