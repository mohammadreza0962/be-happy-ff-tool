package com.example.model

enum class PerformanceProfile(val displayName: String, val description: String) {
    LOW("Low", "Prioritizes battery & thermal stability on budget hardware"),
    BALANCED("Balanced", "Optimal blend of responsive drag speed and frame consistency"),
    HIGH("High", "Sharper flick response with elevated touch responsiveness"),
    ULTRA("Ultra", "Maximum drag speed for esports reaction and 120Hz+ displays")
}

data class DeviceProfile(
    val brand: String,
    val model: String,
    val ram: String, // "2GB", "3GB", "4GB", "6GB", "8GB", "12GB"
    val performanceProfile: PerformanceProfile = PerformanceProfile.BALANCED,
    val touchSamplingHz: Int = 180,
    val defaultDpi: Int = 392,
    val isFlagship: Boolean = false
) {
    val fullName: String get() = "$brand $model"
}

data class SensitivityPreset(
    val general: Int,
    val redDot: Int,
    val scope2x: Int,
    val scope4x: Int,
    val sniperScope: Int,
    val freeLook: Int
)

data class FireButtonConfig(
    val sizePercent: Int,
    val positionZone: String,
    val dragTechnique: String,
    val tip: String
)

data class DpiConfig(
    val recommendedDpi: Int,
    val minSafeDpi: Int,
    val maxSafeDpi: Int,
    val startingPoint: Int,
    val statusLevel: String,
    val guidance: String
)

data class GraphicsConfig(
    val graphicsLevel: String, // Smooth, Standard, Ultra, MAX
    val fpsLevel: String, // Normal (30 FPS), High (60 FPS), Ultra (90/120 FPS)
    val shadows: String, // Off, On
    val highRes: String, // Normal, High
    val filter: String, // Classic, Bright, Vivid, Ocean
    val thermalAdvice: String
)

data class GyroConfig(
    val generalGyro: Int,
    val redDotGyro: Int,
    val scope2xGyro: Int,
    val scope4xGyro: Int,
    val sniperGyro: Int,
    val freeLookGyro: Int,
    val skillLevel: String, // Beginner, Intermediate, Advanced
    val guidance: String
)

data class HudControl(
    val id: String,
    val name: String,
    val xPercent: Float, // 0.0f to 1.0f (relative to preview screen)
    val yPercent: Float, // 0.0f to 1.0f
    val sizeDp: Int,
    val opacityPercent: Int = 90,
    val iconType: String
)

data class HudLayout(
    val id: String,
    val title: String,
    val fingerCount: Int, // 2, 3, 4, 5
    val description: String,
    val difficulty: String,
    val controls: List<HudControl>
)

data class FavoriteItem(
    val id: Long = 0,
    val name: String,
    val category: String, // "SENSITIVITY", "HUD", "DEVICE"
    val deviceName: String,
    val ram: String,
    val summary: String,
    val payloadJson: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class TrainingDay(
    val round1Completed: Boolean = false,
    val round2Completed: Boolean = false,
    val round3Completed: Boolean = false,
    val totalRoundsCompleted: Int = 0,
    val dragPracticeHits: Int = 0,
    val streakDays: Int = 1,
    val lastTrainedDate: String = ""
)

data class OptimizationChecklistState(
    val closeBackgroundApps: Boolean = false,
    val freeStorageSpace: Boolean = false,
    val restartBeforeSession: Boolean = false,
    val gameTurboEnabled: Boolean = false,
    val batterySaverOff: Boolean = false,
    val dndNotificationOn: Boolean = false,
    val stableWifiOrData: Boolean = false,
    val cleanScreenTouch: Boolean = false
) {
    val totalItems: Int = 8
    val completedCount: Int
        get() = listOf(
            closeBackgroundApps, freeStorageSpace, restartBeforeSession,
            gameTurboEnabled, batterySaverOff, dndNotificationOn,
            stableWifiOrData, cleanScreenTouch
        ).count { it }
    
    val progressPercent: Float
        get() = completedCount.toFloat() / totalItems.toFloat()
}

sealed class ManualApplyTarget(
    val title: String,
    val settingName: String,
    val valueStr: String,
    val gameMenuPath: String,
    val stepByStep: List<String>
) {
    class SensitivitySetting(name: String, value: Int) : ManualApplyTarget(
        title = "Sensitivity Setup",
        settingName = name,
        valueStr = "$value",
        gameMenuPath = "Settings ⚙️ ➔ Sensitivity ➔ $name",
        stepByStep = listOf(
            "Launch Free Fire or Free Fire MAX.",
            "Tap the Settings (Gear ⚙️) icon at the top right of the main lobby.",
            "Select the 'Sensitivity' tab from the left navigation menu.",
            "Locate '$name' and adjust the slider to $value.",
            "Tap 'Save' or close settings to apply your new starting point."
        )
    )

    class FireButtonSetting(size: Int) : ManualApplyTarget(
        title = "Fire Button HUD Setup",
        settingName = "Fire Button Size",
        valueStr = "$size%",
        gameMenuPath = "Settings ⚙️ ➔ Controls ➔ Custom HUD",
        stepByStep = listOf(
            "Launch Free Fire.",
            "Open Settings ⚙️ ➔ 'Controls'.",
            "Tap 'Custom HUD' at the bottom right.",
            "Tap your primary Right Fire Button.",
            "Set the 'Button Size' slider to approximately $size%.",
            "Tap the yellow 'Save' icon at the top center of the HUD screen."
        )
    )

    class DpiSetting(recommendedDpi: Int) : ManualApplyTarget(
        title = "Android DPI Manual Guide",
        settingName = "Smallest Width (DPI)",
        valueStr = "$recommendedDpi dp",
        gameMenuPath = "Android Settings ➔ Developer Options ➔ Smallest Width",
        stepByStep = listOf(
            "Open your Android device 'Settings'.",
            "Go to 'About Phone' ➔ Tap 'Build Number' 7 times to enable Developer Options.",
            "Return to Settings ➔ System / Additional Settings ➔ 'Developer Options'.",
            "Scroll down to find 'Smallest width' (or Minimum width).",
            "Note your original DPI first, then enter $recommendedDpi.",
            "Tap OK. Open Free Fire to test smoothness."
        )
    )

    class GraphicsSetting(graphics: String, fps: String) : ManualApplyTarget(
        title = "Display & FPS Settings",
        settingName = "Graphics: $graphics | FPS: $fps",
        valueStr = "$graphics / $fps",
        gameMenuPath = "Settings ⚙️ ➔ Display",
        stepByStep = listOf(
            "Launch Free Fire.",
            "Open Settings ⚙️ ➔ 'Display'.",
            "Under 'Graphics', choose '$graphics'.",
            "Under 'High FPS', choose '$fps'.",
            "Restart Free Fire if prompted for higher frame rate stability."
        )
    )

    class GyroSetting(generalGyro: Int, redDotGyro: Int) : ManualApplyTarget(
        title = "Gyroscope Setup",
        settingName = "General: $generalGyro | Red Dot: $redDotGyro",
        valueStr = "General $generalGyro",
        gameMenuPath = "Settings ⚙️ ➔ Controls / Gyroscope",
        stepByStep = listOf(
            "Launch Free Fire.",
            "Open Settings ⚙️ ➔ 'Basic / Controls'.",
            "Ensure 'Gyroscope' is set to 'Always On' or 'Scope On'.",
            "Go to Sensitivity ➔ adjust Gyroscope General to $generalGyro and Red Dot to $redDotGyro.",
            "Calibrate in the Training Grounds before entering Ranked matches."
        )
    )

    class HudSetting(layoutName: String, fingerCount: Int) : ManualApplyTarget(
        title = "Custom HUD Recreation",
        settingName = "$fingerCount-Finger $layoutName",
        valueStr = "$fingerCount Finger",
        gameMenuPath = "Settings ⚙️ ➔ Controls ➔ Custom HUD",
        stepByStep = listOf(
            "Launch Free Fire.",
            "Open Settings ⚙️ ➔ 'Controls' ➔ 'Custom HUD'.",
            "Select Layout 1 or Layout 2.",
            "Position the Fire, Scope, Jump, and Gloo Wall buttons according to the on-screen visual map.",
            "Adjust button sizes and opacities as recommended.",
            "Tap 'Save' and practice in Lone Wolf or Training Ground."
        )
    )
}
