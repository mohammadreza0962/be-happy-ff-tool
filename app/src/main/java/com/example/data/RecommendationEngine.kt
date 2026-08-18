package com.example.data

import com.example.model.*
import kotlin.math.abs

object RecommendationEngine {

    fun generateSensitivity(profile: DeviceProfile): SensitivityPreset {
        val ramValue = profile.ram.replace("GB", "").toIntOrNull() ?: 6
        val profileWeight = when (profile.performanceProfile) {
            PerformanceProfile.LOW -> -4
            PerformanceProfile.BALANCED -> 0
            PerformanceProfile.HIGH -> 5
            PerformanceProfile.ULTRA -> 10
        }

        // Base values per RAM tier
        // Lower RAM devices require higher general sensitivity to compensate for touch latency
        val baseGeneral = when {
            ramValue <= 2 -> 195
            ramValue == 3 -> 189
            ramValue == 4 -> 182
            ramValue == 6 -> 177
            ramValue == 8 -> 168
            else -> 160 // 12GB+
        }

        // Apply hash modifier for model uniqueness so different models in the same tier have fine-tuned distinction
        val modelHash = abs(profile.model.hashCode() % 5) - 2

        val general = (baseGeneral + profileWeight + modelHash).coerceIn(120, 200)
        val redDot = (general - 12 - (ramValue / 2)).coerceIn(100, 195)
        val scope2x = (redDot - 15).coerceIn(80, 185)
        val scope4x = (scope2x - 10).coerceIn(70, 175)
        val sniperScope = when (profile.performanceProfile) {
            PerformanceProfile.LOW -> 85
            PerformanceProfile.BALANCED -> 95
            PerformanceProfile.HIGH -> 105
            PerformanceProfile.ULTRA -> 115
        } + (modelHash / 2)
        val freeLook = (general - 50).coerceIn(70, 150)

        return SensitivityPreset(
            general = general,
            redDot = redDot,
            scope2x = scope2x,
            scope4x = scope4x,
            sniperScope = sniperScope.coerceIn(50, 150),
            freeLook = freeLook
        )
    }

    fun generateFireButton(profile: DeviceProfile): FireButtonConfig {
        val ramValue = profile.ram.replace("GB", "").toIntOrNull() ?: 6
        
        val size = when {
            ramValue <= 2 -> 58
            ramValue == 3 -> 55
            ramValue == 4 -> 53
            ramValue == 6 -> 50
            ramValue == 8 -> 46
            else -> 43 // 12GB pro precision
        } + when (profile.performanceProfile) {
            PerformanceProfile.LOW -> 2
            PerformanceProfile.BALANCED -> 0
            PerformanceProfile.HIGH -> -2
            PerformanceProfile.ULTRA -> -3
        }

        val dragTechnique = when {
            ramValue <= 3 -> "J-Shape Drag (Fast upward sweep with curved release to prevent overshoot)"
            ramValue <= 6 -> "Straight Upward Drag (Smooth vertical pull aimed directly toward chest/head level)"
            else -> "Rotation Drag (Micro-flick rotation technique suited for high refresh rate screens)"
        }

        val tip = "On your ${profile.brand} ${profile.model} (${profile.ram}), a ${size}% fire button gives sufficient touch contact area without cluttering the screen or interfering with right-side camera panning."

        return FireButtonConfig(
            sizePercent = size.coerceIn(38, 65),
            positionZone = "Lower-Right thumb quadrant (roughly 20% from bottom edge, 15% from right edge)",
            dragTechnique = dragTechnique,
            tip = tip
        )
    }

    fun generateDpiGuidance(profile: DeviceProfile): DpiConfig {
        val baseDpi = profile.defaultDpi
        val ramValue = profile.ram.replace("GB", "").toIntOrNull() ?: 6
        
        val recommendedBoost = when {
            ramValue <= 3 -> 20
            ramValue <= 6 -> 40
            ramValue <= 8 -> 60
            else -> 80
        }

        val recDpi = baseDpi + recommendedBoost
        val maxSafe = baseDpi + when {
            ramValue <= 3 -> 40
            ramValue <= 6 -> 80
            else -> 120
        }

        val statusLevel = when {
            ramValue <= 3 -> "Gentle Increase Recommended (Max +$recommendedBoost dp)"
            ramValue <= 6 -> "Balanced Performance Range"
            else -> "High-Precision Pro Range"
        }

        val guidance = "Default manufacturer DPI is approximately $baseDpi. For ${profile.fullName}, setting Smallest Width between $baseDpi and $recDpi provides noticeably sharper camera flicks without UI clipping or risking reboot bootloops."

        return DpiConfig(
            recommendedDpi = recDpi,
            minSafeDpi = baseDpi,
            maxSafeDpi = maxSafe,
            startingPoint = recDpi,
            statusLevel = statusLevel,
            guidance = guidance
        )
    }

    fun generateGraphics(profile: DeviceProfile): GraphicsConfig {
        val ramValue = profile.ram.replace("GB", "").toIntOrNull() ?: 6

        return when {
            ramValue <= 3 -> GraphicsConfig(
                graphicsLevel = "Smooth",
                fpsLevel = if (ramValue == 2) "Normal (30 FPS)" else "High (60 FPS)",
                shadows = "Off",
                highRes = "Normal",
                filter = "Classic",
                thermalAdvice = "Smooth graphics minimizes render drops and eliminates touch stutter during close-range 1v1 drag fights."
            )
            ramValue <= 6 -> GraphicsConfig(
                graphicsLevel = if (profile.performanceProfile == PerformanceProfile.LOW) "Smooth" else "Standard",
                fpsLevel = "High (60 FPS)",
                shadows = "Off",
                highRes = "Normal",
                filter = "Vivid",
                thermalAdvice = "Standard graphics with High FPS provides clear enemy silhouette visibility with steady 60 FPS in squad rushes."
            )
            ramValue <= 8 -> GraphicsConfig(
                graphicsLevel = if (profile.performanceProfile == PerformanceProfile.ULTRA) "Ultra" else "Standard",
                fpsLevel = "High (60 / 90 FPS)",
                shadows = "Off",
                highRes = "High",
                filter = "Vivid",
                thermalAdvice = "8GB RAM provides robust thermal headroom. Keep shadows Off for purest competitive frame pacing."
            )
            else -> GraphicsConfig(
                graphicsLevel = "Ultra",
                fpsLevel = "High (90 / 120 FPS)",
                shadows = if (profile.isFlagship) "On" else "Off",
                highRes = "High",
                filter = "Bright",
                thermalAdvice = "Your ${profile.model} has flagship processing capability. High FPS ensures maximum frame updates for instant drag response."
            )
        }
    }

    fun generateGyroscope(profile: DeviceProfile): GyroConfig {
        val ramValue = profile.ram.replace("GB", "").toIntOrNull() ?: 6

        val tier = when {
            ramValue <= 3 -> "Beginner"
            ramValue <= 6 -> "Intermediate"
            else -> "Advanced"
        }

        val (gen, red, s2, s4, snp) = when (profile.performanceProfile) {
            PerformanceProfile.LOW -> listOf(140, 130, 115, 100, 60)
            PerformanceProfile.BALANCED -> listOf(160, 150, 135, 120, 75)
            PerformanceProfile.HIGH -> listOf(175, 165, 150, 135, 85)
            PerformanceProfile.ULTRA -> listOf(190, 180, 165, 150, 95)
        }

        val guidance = "Recommended gyro baseline for ${profile.fullName}. Use gentle wrist tilts for micro-adjusting headshots after initial screen drag."

        return GyroConfig(
            generalGyro = gen,
            redDotGyro = red,
            scope2xGyro = s2,
            scope4xGyro = s4,
            sniperGyro = snp,
            freeLookGyro = 100,
            skillLevel = tier,
            guidance = guidance
        )
    }

    fun formatShareText(
        profile: DeviceProfile,
        sens: SensitivityPreset,
        fireBtn: FireButtonConfig,
        dpi: DpiConfig
    ): String {
        return buildString {
            appendLine("🔥 Be Happy FF Tools — Gaming Profile")
            appendLine("━━━━━━━━━━━━━━━━━━━━━")
            appendLine("📱 Device: ${profile.brand} ${profile.model}")
            appendLine("⚡ RAM: ${profile.ram} | Profile: ${profile.performanceProfile.displayName}")
            appendLine("")
            appendLine("🎯 SENSITIVITY PRESET:")
            appendLine("• General: ${sens.general}")
            appendLine("• Red Dot: ${sens.redDot}")
            appendLine("• 2X Scope: ${sens.scope2x}")
            appendLine("• 4X Scope: ${sens.scope4x}")
            appendLine("• Sniper Scope: ${sens.sniperScope}")
            appendLine("• Free Look: ${sens.freeLook}")
            appendLine("")
            appendLine("🔘 FIRE BUTTON: ${fireBtn.sizePercent}%")
            appendLine("📐 RECOMMENDED DPI: ${dpi.recommendedDpi} dp")
            appendLine("━━━━━━━━━━━━━━━━━━━━━")
            appendLine("💡 Recommended starting point — fine-tune according to your gameplay.")
            appendLine("🛡️ 100% Policy Safe • Manual In-Game Guidance Only")
        }
    }

    fun formatCopyAllText(
        profile: DeviceProfile,
        sens: SensitivityPreset
    ): String {
        return buildString {
            appendLine("BeHappy FF Tools")
            appendLine("Device: ${profile.brand} ${profile.model}")
            appendLine("RAM: ${profile.ram}")
            appendLine("")
            appendLine("General: ${sens.general}")
            appendLine("Red Dot: ${sens.redDot}")
            appendLine("2X Scope: ${sens.scope2x}")
            appendLine("4X Scope: ${sens.scope4x}")
            appendLine("Sniper: ${sens.sniperScope}")
            appendLine("Free Look: ${sens.freeLook}")
        }
    }
}
