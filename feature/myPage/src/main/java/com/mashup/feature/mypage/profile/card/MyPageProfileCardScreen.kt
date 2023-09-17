package com.mashup.feature.mypage.profile.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.mashup.core.model.Platform
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.Gray200
import com.mashup.core.ui.colors.Gray800
import com.mashup.core.ui.typography.SubTitle1
import com.mashup.feature.mypage.profile.model.GenerationData

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyPageProfileCardScreen(cards: List<GenerationData>, pagerState: PagerState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        MyPageSubTitle("활동 카드")

        HorizontalPager(
            count = cards.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 15.dp)
        ) { card ->
            ProfileCard(
                modifier = Modifier.padding(horizontal = 5.dp),
                generationNumber = cards[card].generationNumber,
                name = cards[card].name,
                platform = Platform.getPlatform(cards[card].platform),
                isRunning = cards[card].isRunning,
                team = cards[card].projectTeamName,
                staff = cards[card].role,
            )
        }

        ProfileCardIndicator(
            modifier = Modifier.padding(top = 16.dp),
            size = cards.size,
            currentPage = pagerState.currentPage,
        )

        Divider(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 24.dp),
            color = Gray100,
            thickness = 1.dp
        )

        MyPageSubTitle("활동 히스토리")
    }
}

@Composable
fun MyPageSubTitle(text: String) {
    Text(
        text = text,
        style = SubTitle1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, bottom = 12.dp),
    )
}

@Composable
fun ProfileCardIndicator(modifier: Modifier = Modifier, size: Int, currentPage: Int) {
    Row(modifier = modifier) {
        repeat(size) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (it == currentPage) Gray800 else Gray200)
            )
        }
    }
}
