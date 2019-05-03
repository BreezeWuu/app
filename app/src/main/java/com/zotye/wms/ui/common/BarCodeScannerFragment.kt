package com.zotye.wms.ui.common

import android.Manifest
import android.content.Context.VIBRATOR_SERVICE
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.zotye.wms.R
import com.zotye.wms.data.api.model.BarCodeType
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_code_scanner.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textResource
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception


/**
 * Created by hechuangju on 2018/01/17
 */

const val REQUEST_CODE_QRCODE_PERMISSIONS = 1

class BarCodeScannerFragment : BaseFragment(), EasyPermissions.PermissionCallbacks, QRCodeView.Delegate {
    private var scannerDelegate: ScannerDelegate? = null

    fun setScannerDelegate(scannerDelegate: ScannerDelegate) {
        this.scannerDelegate = scannerDelegate
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_code_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.titleResource = R.string.code_scanner
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        zbarview.setDelegate(this)
        requestCodeQRCodePermissions()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        zbarview.startCamera()
        zbarview.startSpotAndShowRect()
        toggleLightButton.onClick {
            if (it!!.isSelected) {
                zbarview.closeFlashlight()
            } else {
                zbarview.openFlashlight()
            }
            it.isSelected = !it.isSelected
            (it as Button).textResource = if (it.isSelected) R.string.action_turn_off_light else R.string.action_turn_on_light
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        requestCodeQRCodePermissions()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private fun requestCodeQRCodePermissions() {
        val perms = listOf(Manifest.permission.CAMERA).toTypedArray()
        if (!EasyPermissions.hasPermissions(context!!, perms[0])) {
            EasyPermissions.requestPermissions(this, "扫描二维码必须打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms[0])
        } else {
            zbarview.startCamera()
            zbarview.startSpotAndShowRect()
            toggleLightButton.onClick {
                if (it!!.isSelected) {
                    zbarview.closeFlashlight()
                } else {
                    zbarview.openFlashlight()
                }
                it.isSelected = !it.isSelected
                (it as Button).textResource = if (it.isSelected) R.string.action_turn_off_light else R.string.action_turn_on_light
            }
        }
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {

    }

    override fun onScanQRCodeSuccess(result: String) {
        vibrate()
//        zbarview.startSpot()
        scannerDelegate?.let {
            it.succeed(result)
        }
        activity?.onBackPressed()
    }

    override fun onScanQRCodeOpenCameraError() {
        showMessage(R.string.error_open_camera)
    }

    private fun vibrate() {
        val vibrator = activity?.getSystemService(VIBRATOR_SERVICE)
        (vibrator as? Vibrator)?.vibrate(200)
    }

    override fun onStop() {
        super.onStop()
        zbarview.stopCamera()

    }

    override fun onDestroyView() {
        zbarview.onDestroy()
        super.onDestroyView()
    }
}