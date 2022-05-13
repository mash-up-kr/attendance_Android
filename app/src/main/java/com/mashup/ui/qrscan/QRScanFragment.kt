package com.mashup.ui.qrscan

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.common.Barcode
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentQrScanBinding
import com.mashup.extensions.showToast
import com.mashup.ui.qrscan.camera.CameraManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRScanFragment : BaseFragment<FragmentQrScanBinding>() {

    private lateinit var qrCodeAnalyzer: QRCodeAnalyzer
    private lateinit var cameraManager: CameraManager<List<Barcode>>

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                cameraManager.startCamera()
            } else {
                showToast(requireContext(), getString(R.string.denied_camera_permission))
            }
        }

    companion object {
        fun newInstance() = QRScanFragment()
        private const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
    }

    override fun initViews() {
        initCamera()
    }

    private fun initCamera() {
        createCardAnalyzer()
        createCameraManager()


        when {
            isCameraPermissionGranted() -> {
                cameraManager.startCamera()
            }
            else -> {
                requestPermissionLauncher.launch(CAMERA_PERMISSION)
            }
        }
    }

    private fun createCardAnalyzer() {
        qrCodeAnalyzer = QRCodeAnalyzer(
            onCardRecognitionSuccess = { cardInfo ->
                cameraManager.stopCamera()
            },
            onCardRecognitionFailure = { throwable ->
                throwable?.message?.let { message ->
                    showToast(requireContext(), message)
                }
            }
        )
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            context = requireContext(),
            finderView = viewBinding.viewFinder,
            lifecycleOwner = viewLifecycleOwner,
            baseImageAnalyzer = qrCodeAnalyzer
        )
    }

    override fun onResume() {
        super.onResume()
        cameraManager.startCamera()
    }

    override fun onPause() {
        super.onPause()
        cameraManager.stopCamera()
    }

    private fun isCameraPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override val layoutId: Int = R.layout.fragment_qr_scan
}