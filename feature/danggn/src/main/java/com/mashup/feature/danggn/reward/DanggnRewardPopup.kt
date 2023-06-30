package com.mashup.feature.danggn.reward

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.core.common.widget.CommonDialog
import com.mashup.core.ui.colors.Gray200
import com.mashup.core.ui.colors.Gray400
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body4
import com.mashup.core.ui.typography.SubTitle2
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.common.R as CR

class DanggnRewardPopup : BottomSheetDialogFragment() {

    private val behavior: BottomSheetBehavior<View>?
        get() {
            val bottomSheet =
                dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            return bottomSheet?.let {
                BottomSheetBehavior.from(bottomSheet)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MashUpTheme {
                    DanggnRewardPopupScreen(
                        onClickDismissButton = { this@DanggnRewardPopup.dismiss() },
                        onClickSubmitButton = onClickSubmitButton
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.run {
                setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.transparent
                    )
                )
            }

        addGlobalLayoutListener(view)
    }

    private fun addGlobalLayoutListener(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (this@DanggnRewardPopup.view?.height == 0) return
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                behavior?.peekHeight = this@DanggnRewardPopup.view?.height ?: 0
            }
        })
    }


    companion object {
        const val MAX_LENGTH = 20

        var onClickSubmitButton: (String) -> Unit = { }

        fun getNewInstance(
            onClickSubmitButton: (String) -> Unit,
        ): DanggnRewardPopup {
            this.onClickSubmitButton = onClickSubmitButton
            return DanggnRewardPopup()
        }
    }
}


@Composable
fun DanggnRewardPopupScreen(
    modifier: Modifier = Modifier,
    onClickDismissButton: () -> Unit,
    onClickSubmitButton: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .background(
                color = White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(20.dp)
    ) {
        Row {
            Text(text = "랭킹 1위의 리워드", style = SubTitle2, modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = CR.drawable.ic_xmark),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onClickDismissButton() },
            )
        }

        Text(
            text = "단 한 번만 작성할 수 있으며 완료 후 내용을 수정할 수 없습니다. 욕설 및 상대방을 비방하는 내용은 삼가주세요.",
            style = Body4,
            color = Gray600,
            modifier = Modifier.padding(top = 8.dp, bottom = 26.dp),
        )

        Row(
            modifier = Modifier.padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = text,
                onValueChange = { if (it.length <= DanggnRewardPopup.MAX_LENGTH) text = it },
                modifier = Modifier.weight(1f),
            )
            Image(
                painter = painterResource(id = CR.drawable.ic_xmark),
                contentDescription = null,
                modifier = Modifier
                    .padding(4.dp)
                    .size(16.dp)
                    .clickable { text = "" },
            )
        }

        Divider(thickness = 2.dp, color = Gray200)

        Text(
            text = "${text.length}/${DanggnRewardPopup.MAX_LENGTH}",
            style = Body4,
            color = Gray400,
            modifier = Modifier
                .align(Alignment.End)
                .padding(4.dp),
        )

        MashUpButton(
            text = "공지 등록하기",
            onClick = { /*TODO*/ },
            isEnabled = text.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .clickable { onClickSubmitButton(text) },
        )
    }
}

@Composable
@Preview
fun PreviewDanggnRewardPopupContent() {
    MashUpTheme {
        DanggnRewardPopupScreen(onClickDismissButton = {}, onClickSubmitButton = {})
    }
}
