package com.example.data

import com.example.model.HudControl
import com.example.model.HudLayout

object HudTemplates {

    val layouts: List<HudLayout> = listOf(
        // 2-Finger Basic / Pro
        HudLayout(
            id = "hud_2_finger",
            title = "2-Finger Classic Drag",
            fingerCount = 2,
            description = "Standard two-thumb layout with large right fire button and quick gloo wall access near left thumb.",
            difficulty = "Beginner to Intermediate",
            controls = listOf(
                HudControl("joystick", "Movement Joystick", 0.18f, 0.72f, 96, 85, "joystick"),
                HudControl("fire_right", "Right Fire", 0.82f, 0.65f, 78, 100, "fire"),
                HudControl("scope", "Scope Aim", 0.72f, 0.46f, 62, 90, "scope"),
                HudControl("jump", "Jump", 0.90f, 0.44f, 62, 90, "jump"),
                HudControl("crouch", "Crouch", 0.88f, 0.85f, 60, 90, "crouch"),
                HudControl("prone", "Prone", 0.75f, 0.88f, 50, 75, "prone"),
                HudControl("gloo_wall", "Gloo Wall", 0.22f, 0.44f, 70, 95, "shield"),
                HudControl("medkit", "Medkit", 0.10f, 0.48f, 54, 85, "medkit"),
                HudControl("weapon1", "Primary Weapon", 0.52f, 0.88f, 66, 90, "gun"),
                HudControl("weapon2", "Secondary Weapon", 0.62f, 0.88f, 66, 90, "gun"),
                HudControl("weapon3", "Melee / Pistol", 0.42f, 0.88f, 54, 80, "knife"),
                HudControl("reload", "Quick Reload", 0.62f, 0.72f, 54, 85, "reload"),
                HudControl("active_skill", "Active Skill", 0.74f, 0.30f, 58, 90, "skill"),
                HudControl("revive", "Revive / Help", 0.50f, 0.22f, 54, 80, "heart")
            )
        ),

        // 3-Finger Claw
        HudLayout(
            id = "hud_3_finger",
            title = "3-Finger Claw Pro",
            fingerCount = 3,
            description = "Left index finger controls dedicated top-left Fire button for continuous jumping-shots while right thumb tracks and aims.",
            difficulty = "Intermediate",
            controls = listOf(
                HudControl("joystick", "Movement Joystick", 0.18f, 0.72f, 90, 85, "joystick"),
                HudControl("fire_left", "Claw Left Fire", 0.16f, 0.18f, 84, 100, "fire"),
                HudControl("fire_right", "Right Drag Fire", 0.84f, 0.65f, 68, 90, "fire"),
                HudControl("scope", "Scope Aim", 0.75f, 0.45f, 64, 90, "scope"),
                HudControl("jump", "Jump", 0.90f, 0.45f, 64, 95, "jump"),
                HudControl("crouch", "Crouch", 0.88f, 0.84f, 62, 90, "crouch"),
                HudControl("prone", "Prone", 0.74f, 0.88f, 50, 75, "prone"),
                HudControl("gloo_wall", "Gloo Wall", 0.24f, 0.45f, 72, 95, "shield"),
                HudControl("medkit", "Medkit", 0.10f, 0.48f, 54, 85, "medkit"),
                HudControl("weapon1", "Primary Weapon", 0.50f, 0.88f, 68, 90, "gun"),
                HudControl("weapon2", "Secondary Weapon", 0.62f, 0.88f, 68, 90, "gun"),
                HudControl("reload", "Quick Reload", 0.64f, 0.72f, 54, 85, "reload"),
                HudControl("active_skill", "Active Skill", 0.82f, 0.28f, 60, 90, "skill"),
                HudControl("revive", "Revive", 0.50f, 0.20f, 54, 80, "heart")
            )
        ),

        // 4-Finger Tournament Layout
        HudLayout(
            id = "hud_4_finger",
            title = "4-Finger Tournament Speed",
            fingerCount = 4,
            description = "Dual-top triggers: Left index for Shooting & Gloo Wall, Right index for Jump & Scope. Max agility for fast Situp-Gloo Wall combos.",
            difficulty = "Advanced / Esports",
            controls = listOf(
                HudControl("joystick", "Movement Joystick", 0.16f, 0.72f, 85, 80, "joystick"),
                HudControl("fire_left", "Left Index Fire", 0.14f, 0.16f, 88, 100, "fire"),
                HudControl("gloo_wall", "Top Gloo Wall", 0.28f, 0.18f, 76, 95, "shield"),
                HudControl("jump", "Top Right Jump", 0.86f, 0.18f, 74, 95, "jump"),
                HudControl("scope", "Top Right Scope", 0.72f, 0.18f, 72, 90, "scope"),
                HudControl("fire_right", "Right Drag Fire", 0.82f, 0.62f, 64, 90, "fire"),
                HudControl("crouch", "Fast Crouch", 0.88f, 0.82f, 66, 95, "crouch"),
                HudControl("prone", "Prone", 0.72f, 0.88f, 50, 75, "prone"),
                HudControl("weapon1", "Primary Weapon", 0.50f, 0.88f, 70, 90, "gun"),
                HudControl("weapon2", "Secondary Weapon", 0.62f, 0.88f, 70, 90, "gun"),
                HudControl("reload", "Quick Reload", 0.65f, 0.68f, 56, 85, "reload"),
                HudControl("active_skill", "Active Skill", 0.50f, 0.45f, 60, 85, "skill"),
                HudControl("medkit", "Medkit", 0.10f, 0.45f, 54, 80, "medkit")
            )
        ),

        // 5-Finger Beast Mode
        HudLayout(
            id = "hud_5_finger",
            title = "5-Finger Precision Beast",
            fingerCount = 5,
            description = "Ultimate multi-touch configuration designed for large screens & tablets, separating weapon swaps, crouch-spam, and rapid fire.",
            difficulty = "Master",
            controls = listOf(
                HudControl("joystick", "Movement Joystick", 0.16f, 0.72f, 85, 80, "joystick"),
                HudControl("fire_left", "Left Index Fire", 0.14f, 0.14f, 88, 100, "fire"),
                HudControl("gloo_wall", "Left Middle Gloo", 0.28f, 0.15f, 78, 95, "shield"),
                HudControl("jump", "Right Index Jump", 0.86f, 0.14f, 76, 95, "jump"),
                HudControl("scope", "Right Middle Scope", 0.72f, 0.15f, 72, 90, "scope"),
                HudControl("crouch", "Right Index Crouch", 0.86f, 0.30f, 68, 95, "crouch"),
                HudControl("fire_right", "Right Thumb Drag", 0.80f, 0.65f, 62, 85, "fire"),
                HudControl("weapon1", "Slot 1 (Left Thumb)", 0.38f, 0.88f, 68, 90, "gun"),
                HudControl("weapon2", "Slot 2 (Right Thumb)", 0.58f, 0.88f, 68, 90, "gun"),
                HudControl("reload", "Quick Reload", 0.65f, 0.65f, 56, 85, "reload"),
                HudControl("active_skill", "Active Skill", 0.48f, 0.30f, 60, 90, "skill"),
                HudControl("medkit", "Medkit", 0.08f, 0.45f, 54, 80, "medkit"),
                HudControl("prone", "Prone", 0.70f, 0.88f, 50, 75, "prone")
            )
        )
    )

    fun getLayoutByFingers(fingers: Int): HudLayout {
        return layouts.firstOrNull { it.fingerCount == fingers } ?: layouts[0]
    }
}
