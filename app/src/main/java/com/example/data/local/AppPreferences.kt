package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.DeviceDatabase
import com.example.model.DeviceProfile
import com.example.model.OptimizationChecklistState
import com.example.model.PerformanceProfile
import com.example.model.SensitivityPreset
import com.example.model.TrainingDay

class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("behappy_ff_tools_prefs", Context.MODE_PRIVATE)

    // --- Device Profile ---
    fun saveDeviceProfile(profile: DeviceProfile) {
        prefs.edit()
            .putString("dev_brand", profile.brand)
            .putString("dev_model", profile.model)
            .putString("dev_ram", profile.ram)
            .putString("dev_profile", profile.performanceProfile.name)
            .putInt("dev_sampling", profile.touchSamplingHz)
            .putInt("dev_dpi", profile.defaultDpi)
            .putBoolean("dev_flagship", profile.isFlagship)
            .apply()
    }

    fun getDeviceProfile(): DeviceProfile {
        val brand = prefs.getString("dev_brand", DeviceDatabase.defaultDevice.brand) ?: DeviceDatabase.defaultDevice.brand
        val model = prefs.getString("dev_model", DeviceDatabase.defaultDevice.model) ?: DeviceDatabase.defaultDevice.model
        val ram = prefs.getString("dev_ram", DeviceDatabase.defaultDevice.ram) ?: DeviceDatabase.defaultDevice.ram
        val profileStr = prefs.getString("dev_profile", PerformanceProfile.BALANCED.name) ?: PerformanceProfile.BALANCED.name
        val sampling = prefs.getInt("dev_sampling", 180)
        val dpi = prefs.getInt("dev_dpi", 384)
        val flagship = prefs.getBoolean("dev_flagship", false)

        val perfProfile = try {
            PerformanceProfile.valueOf(profileStr)
        } catch (_: Exception) {
            PerformanceProfile.BALANCED
        }

        return DeviceProfile(
            brand = brand,
            model = model,
            ram = ram,
            performanceProfile = perfProfile,
            touchSamplingHz = sampling,
            defaultDpi = dpi,
            isFlagship = flagship
        )
    }

    // --- Sensitivity fine-tune override ---
    fun saveSensitivity(preset: SensitivityPreset) {
        prefs.edit()
            .putInt("sens_gen", preset.general)
            .putInt("sens_red", preset.redDot)
            .putInt("sens_2x", preset.scope2x)
            .putInt("sens_4x", preset.scope4x)
            .putInt("sens_snp", preset.sniperScope)
            .putInt("sens_free", preset.freeLook)
            .putBoolean("sens_customized", true)
            .apply()
    }

    fun getCustomSensitivity(): SensitivityPreset? {
        if (!prefs.getBoolean("sens_customized", false)) return null
        return SensitivityPreset(
            general = prefs.getInt("sens_gen", 177),
            redDot = prefs.getInt("sens_red", 165),
            scope2x = prefs.getInt("sens_2x", 150),
            scope4x = prefs.getInt("sens_4x", 140),
            sniperScope = prefs.getInt("sens_snp", 95),
            freeLook = prefs.getInt("sens_free", 120)
        )
    }

    fun clearCustomSensitivity() {
        prefs.edit().remove("sens_customized").apply()
    }

    // --- Training state ---
    fun saveTrainingState(training: TrainingDay) {
        prefs.edit()
            .putBoolean("train_r1", training.round1Completed)
            .putBoolean("train_r2", training.round2Completed)
            .putBoolean("train_r3", training.round3Completed)
            .putInt("train_total", training.totalRoundsCompleted)
            .putInt("train_hits", training.dragPracticeHits)
            .putInt("train_streak", training.streakDays)
            .putString("train_date", training.lastTrainedDate)
            .apply()
    }

    fun getTrainingState(): TrainingDay {
        return TrainingDay(
            round1Completed = prefs.getBoolean("train_r1", false),
            round2Completed = prefs.getBoolean("train_r2", false),
            round3Completed = prefs.getBoolean("train_r3", false),
            totalRoundsCompleted = prefs.getInt("train_total", 0),
            dragPracticeHits = prefs.getInt("train_hits", 0),
            streakDays = prefs.getInt("train_streak", 1),
            lastTrainedDate = prefs.getString("train_date", "") ?: ""
        )
    }

    // --- Optimization checklist ---
    fun saveOptimizationChecklist(state: OptimizationChecklistState) {
        prefs.edit()
            .putBoolean("opt_bg", state.closeBackgroundApps)
            .putBoolean("opt_storage", state.freeStorageSpace)
            .putBoolean("opt_restart", state.restartBeforeSession)
            .putBoolean("opt_turbo", state.gameTurboEnabled)
            .putBoolean("opt_battery", state.batterySaverOff)
            .putBoolean("opt_dnd", state.dndNotificationOn)
            .putBoolean("opt_wifi", state.stableWifiOrData)
            .putBoolean("opt_screen", state.cleanScreenTouch)
            .apply()
    }

    fun getOptimizationChecklist(): OptimizationChecklistState {
        return OptimizationChecklistState(
            closeBackgroundApps = prefs.getBoolean("opt_bg", false),
            freeStorageSpace = prefs.getBoolean("opt_storage", false),
            restartBeforeSession = prefs.getBoolean("opt_restart", false),
            gameTurboEnabled = prefs.getBoolean("opt_turbo", false),
            batterySaverOff = prefs.getBoolean("opt_battery", false),
            dndNotificationOn = prefs.getBoolean("opt_dnd", false),
            stableWifiOrData = prefs.getBoolean("opt_wifi", false),
            cleanScreenTouch = prefs.getBoolean("opt_screen", false)
        )
    }

    // --- Fire Button Custom Size ---
    fun saveFireButtonSize(size: Int) {
        prefs.edit().putInt("custom_fire_btn_size", size).apply()
    }

    fun getFireButtonSize(): Int? {
        return if (prefs.contains("custom_fire_btn_size")) {
            prefs.getInt("custom_fire_btn_size", 52)
        } else null
    }

    // --- Custom DPI ---
    fun saveCustomDpi(dpi: Int) {
        prefs.edit().putInt("custom_dpi_val", dpi).apply()
    }

    fun getCustomDpi(): Int? {
        return if (prefs.contains("custom_dpi_val")) {
            prefs.getInt("custom_dpi_val", 420)
        } else null
    }
}
