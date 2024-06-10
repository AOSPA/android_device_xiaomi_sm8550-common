/*
 * Copyright (C) 2024 Paranoid Android
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xiaomi.settings.thermal

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

import com.xiaomi.settings.utils.FileUtils

class ThermalProfileTileService : TileService() {

    companion object {
        private const val THEMRAL_PROFILE_PATH = "/sys/class/thermal/thermal_message/sconfig"
        private const val THEMRAL_PROFILE_DEFAULT = 0
        private const val THEMRAL_PROFILE_MGAME = 19
    }

    private fun updateUI(profile: Int) {
        val tile = qsTile
        tile.label = "Thermal profile"
        tile.subtitle = if (profile == THEMRAL_PROFILE_DEFAULT) "Default" else if (profile == THEMRAL_PROFILE_MGAME) "Game" else "Unknown"
        tile.state = Tile.STATE_ACTIVE
        tile.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateUI(FileUtils.readLineInt(THEMRAL_PROFILE_PATH))
    }

    override fun onStopListening() {
        super.onStopListening()
    }

    override fun onClick() {
        super.onClick()
        val currentThermalProfile = FileUtils.readLineInt(THEMRAL_PROFILE_PATH)
        val newThermalProfile = if (THEMRAL_PROFILE_DEFAULT == currentThermalProfile) THEMRAL_PROFILE_MGAME else THEMRAL_PROFILE_DEFAULT
        FileUtils.writeLine(THEMRAL_PROFILE_PATH, newThermalProfile)
        updateUI(newThermalProfile)
    }
}
