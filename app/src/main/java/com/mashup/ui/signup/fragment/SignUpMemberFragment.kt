package com.mashup.ui.signup.fragment

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.common.Validation
import com.mashup.databinding.FragmentSignUpMemberBinding
import com.mashup.ui.extensions.setEmptyUIOfTextField
import com.mashup.ui.extensions.setFailedUiOfTextField
import com.mashup.ui.extensions.setSuccessUiOfTextField
import com.mashup.ui.signup.MemberState
import com.mashup.ui.signup.SignUpViewModel
import com.mashup.utils.keyboard.TranslateDeferringInsetsAnimationCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpMemberFragment : BaseFragment<FragmentSignUpMemberBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_member

    override fun onResume() {
        super.onResume()
        viewModel.setToolbarDividerVisible(false)
    }

    override fun initViews() {
        initTextField()
        initButton()
    }

    override fun initObserves() = with(viewModel) {
        flowViewLifecycleScope {
            memberState.collectLatest { memberState ->
                setUiOfMemberState(memberState)
            }
        }
    }

    private fun initTextField() {
        viewBinding.textFieldName.addOnTextChangedListener {
            viewModel.setUserName(it)
        }
        viewBinding.textFieldName.setFocus()

        viewBinding.textFieldPlatform.setSelectionThrottleFirstClickListener(viewLifecycleOwner) {
            viewBinding.textFieldName.clearTextFieldFocus()
            findNavController().navigate(R.id.action_signUpMemberFragment_to_platFormSelectionDialog)
        }
    }

    private fun setUiOfMemberState(memberState: MemberState) = with(viewBinding) {
        setUiOfNameState(memberState)
        textFieldPlatform.setText(memberState.platform)
        btnSignUp.setButtonEnabled(memberState.isValidationState)
    }

    private fun setUiOfNameState(memberState: MemberState) = with(viewBinding) {
        viewBinding.textFieldName.setText(memberState.name)
        when (memberState.isValidationName) {
            Validation.EMPTY -> {
                textFieldName.setDescriptionText("한글 이름(실명)을 입력해주세요.")
                textFieldName.setEmptyUIOfTextField()
            }
            Validation.SUCCESS -> {
                textFieldName.setDescriptionText("한글 이름(실명)을 입력해주세요.")
                textFieldName.setSuccessUiOfTextField()
            }
            Validation.FAILED -> {
                textFieldName.setDescriptionText("한글 이름(실명)을 입력해주세요.")
                textFieldName.setFailedUiOfTextField()
            }
        }
    }

    private fun initButton() {
        ViewCompat.setWindowInsetsAnimationCallback(
            viewBinding.layoutButton,
            TranslateDeferringInsetsAnimationCallback(
                view = viewBinding.layoutButton,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )
        viewBinding.btnSignUp.setOnButtonClickListener {
            findNavController().navigate(R.id.action_signUpMemberFragment_to_termsAgreementDialog)
        }
    }
}