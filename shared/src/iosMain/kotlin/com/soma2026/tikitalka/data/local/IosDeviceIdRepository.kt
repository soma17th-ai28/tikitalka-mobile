package com.soma2026.tikitalka.data.local

import com.soma2026.tikitalka.domain.repository.DeviceIdRepository
import platform.UIKit.UIDevice

class IosDeviceIdRepository : DeviceIdRepository {

    override fun getDeviceId(): String =
        UIDevice.currentDevice.identifierForVendor?.UUIDString ?: "unknown_device"
}