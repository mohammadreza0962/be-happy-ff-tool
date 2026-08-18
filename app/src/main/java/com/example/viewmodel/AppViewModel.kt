package com.example.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DeviceDatabase
import com.example.data.HudTemplates
import com.example.data.RecommendationEngine
import com.example.data.local.AppDatabase
import com.example.data.local.AppPreferences
import com.example.data.local.FavoriteEntity
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = AppPreferences(application)
    private val favoriteDao = AppDatabase.getDatabase(application).favoriteDao()

    // --- Active Device Profile ---
    private val _deviceProfile = MutableStateFlow(preferences.getDeviceProfile())
    val deviceProfile: StateFlow<DeviceProfile> = _deviceProfile.asStateFlow()

    // --- Active Sensitivity (User fine-tuned or calculated) ---
    private val _sensitivity = MutableStateFlow(
        preferences.getCustomSensitivity() ?: RecommendationEngine.generateSensitivity(_deviceProfile.value)
    )
    val sensitivity: StateFlow<SensitivityPreset> = _sensitivity.asStateFlow()

    // --- Calculated Configurations ---
    val fireButtonConfig: StateFlow<FireButtonConfig> = _deviceProfile.map {
        val base = RecommendationEngine.generateFireButton(it)
        val customSize = preferences.getFireButtonSize()
        if (customSize != null) base.copy(sizePercent = customSize) else base
    }.stateIn(viewModelScope, SharingStarted.Eagerly, RecommendationEngine.generateFireButton(_deviceProfile.value))

    val dpiConfig: StateFlow<DpiConfig> = _deviceProfile.map {
        val base = RecommendationEngine.generateDpiGuidance(it)
        val customDpi = preferences.getCustomDpi()
        if (customDpi != null) base.copy(recommendedDpi = customDpi) else base
    }.stateIn(viewModelScope, SharingStarted.Eagerly, RecommendationEngine.generateDpiGuidance(_deviceProfile.value))

    val graphicsConfig: StateFlow<GraphicsConfig> = _deviceProfile.map {
        RecommendationEngine.generateGraphics(it)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, RecommendationEngine.generateGraphics(_deviceProfile.value))

    val gyroConfig: StateFlow<GyroConfig> = _deviceProfile.map {
        RecommendationEngine.generateGyroscope(it)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, RecommendationEngine.generateGyroscope(_deviceProfile.value))

    // --- Custom HUD State ---
    private val _selectedFingerCount = MutableStateFlow(2)
    val selectedFingerCount: StateFlow<Int> = _selectedFingerCount.asStateFlow()

    private val _activeHudLayout = MutableStateFlow(HudTemplates.getLayoutByFingers(2))
    val activeHudLayout: StateFlow<HudLayout> = _activeHudLayout.asStateFlow()

    // --- Optimization State ---
    private val _optimizationState = MutableStateFlow(preferences.getOptimizationChecklist())
    val optimizationState: StateFlow<OptimizationChecklistState> = _optimizationState.asStateFlow()

    // --- Headshot Training State ---
    private val _trainingState = MutableStateFlow(preferences.getTrainingState())
    val trainingState: StateFlow<TrainingDay> = _trainingState.asStateFlow()

    // Training Timer
    private val _timerSecondsRemaining = MutableStateFlow(180) // 3 minutes default
    val timerSecondsRemaining: StateFlow<Int> = _timerSecondsRemaining.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _activeRound = MutableStateFlow(1) // 1, 2, 3
    val activeRound: StateFlow<Int> = _activeRound.asStateFlow()

    private var timerJob: Job? = null

    // Target Game (Reaction & Drag Practice)
    private val _targetHitScore = MutableStateFlow(0)
    val targetHitScore: StateFlow<Int> = _targetHitScore.asStateFlow()

    private val _targetPosition = MutableStateFlow(Pair(0.5f, 0.4f)) // xPercent, yPercent
    val targetPosition: StateFlow<Pair<Float, Float>> = _targetPosition.asStateFlow()

    // --- Favorites from Room DB ---
    val favorites: StateFlow<List<FavoriteItem>> = favoriteDao.getAllFavorites()
        .map { list -> list.map { it.toFavoriteItem() } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // --- Active Manual Apply Dialog / Sheet ---
    private val _activeManualApply = MutableStateFlow<ManualApplyTarget?>(null)
    val activeManualApply: StateFlow<ManualApplyTarget?> = _activeManualApply.asStateFlow()

    // --- Toast / Feedback Events ---
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    // --- Device Search / Filters ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedBrandFilter = MutableStateFlow("All")
    val selectedBrandFilter: StateFlow<String> = _selectedBrandFilter.asStateFlow()

    private val _selectedRamFilter = MutableStateFlow("All")
    val selectedRamFilter: StateFlow<String> = _selectedRamFilter.asStateFlow()

    val filteredDevices: StateFlow<List<DeviceProfile>> = combine(
        _searchQuery,
        _selectedBrandFilter,
        _selectedRamFilter
    ) { query, brand, ram ->
        DeviceDatabase.allDevices.filter { device ->
            val matchQuery = query.isEmpty() ||
                    device.brand.contains(query, ignoreCase = true) ||
                    device.model.contains(query, ignoreCase = true)
            val matchBrand = brand == "All" || device.brand.equals(brand, ignoreCase = true)
            val matchRam = ram == "All" || device.ram.equals(ram, ignoreCase = true)
            matchQuery && matchBrand && matchRam
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, DeviceDatabase.allDevices)

    init {
        checkDailyStreakReset()
    }

    // ==========================================
    // DEVICE PROFILE ACTIONS
    // ==========================================

    fun selectDevice(device: DeviceProfile) {
        _deviceProfile.value = device
        preferences.saveDeviceProfile(device)
        preferences.clearCustomSensitivity()
        _sensitivity.value = RecommendationEngine.generateSensitivity(device)
        showToast("Profile applied: ${device.brand} ${device.model} (${device.ram})")
    }

    fun updateRam(newRam: String) {
        val updated = _deviceProfile.value.copy(ram = newRam)
        selectDevice(updated)
    }

    fun updatePerformanceProfile(newProfile: PerformanceProfile) {
        val updated = _deviceProfile.value.copy(performanceProfile = newProfile)
        selectDevice(updated)
    }

    fun setDeviceSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setBrandFilter(brand: String) {
        _selectedBrandFilter.value = brand
    }

    fun setRamFilter(ram: String) {
        _selectedRamFilter.value = ram
    }

    // ==========================================
    // SENSITIVITY ACTIONS
    // ==========================================

    fun updateGeneralSensitivity(value: Int) {
        _sensitivity.value = _sensitivity.value.copy(general = value)
        preferences.saveSensitivity(_sensitivity.value)
    }

    fun updateRedDotSensitivity(value: Int) {
        _sensitivity.value = _sensitivity.value.copy(redDot = value)
        preferences.saveSensitivity(_sensitivity.value)
    }

    fun update2xScopeSensitivity(value: Int) {
        _sensitivity.value = _sensitivity.value.copy(scope2x = value)
        preferences.saveSensitivity(_sensitivity.value)
    }

    fun update4xScopeSensitivity(value: Int) {
        _sensitivity.value = _sensitivity.value.copy(scope4x = value)
        preferences.saveSensitivity(_sensitivity.value)
    }

    fun updateSniperSensitivity(value: Int) {
        _sensitivity.value = _sensitivity.value.copy(sniperScope = value)
        preferences.saveSensitivity(_sensitivity.value)
    }

    fun updateFreeLookSensitivity(value: Int) {
        _sensitivity.value = _sensitivity.value.copy(freeLook = value)
        preferences.saveSensitivity(_sensitivity.value)
    }

    fun resetSensitivityToRecommendation() {
        preferences.clearCustomSensitivity()
        val rec = RecommendationEngine.generateSensitivity(_deviceProfile.value)
        _sensitivity.value = rec
        showToast("Sensitivity reset to recommended starting point.")
    }

    fun applyIndividualSensitivity(name: String, value: Int) {
        showToast("$name $value selected")
        _activeManualApply.value = ManualApplyTarget.SensitivitySetting(name, value)
    }

    fun copyAllSensitivity() {
        val text = RecommendationEngine.formatCopyAllText(_deviceProfile.value, _sensitivity.value)
        copyToClipboard("FF Sensitivity", text)
        showToast("All sensitivity settings copied to clipboard!")
    }

    fun shareSensitivityPreset() {
        val text = RecommendationEngine.formatShareText(
            _deviceProfile.value,
            _sensitivity.value,
            fireButtonConfig.value,
            dpiConfig.value
        )
        shareText("Be Happy FF Tools Preset", text)
    }

    fun saveSensitivityFavorite(customName: String? = null) {
        viewModelScope.launch {
            val name = if (!customName.isNullOrBlank()) customName else "${_deviceProfile.value.model} Sens (${_deviceProfile.value.ram})"
            val summary = "Gen: ${_sensitivity.value.general} | RedDot: ${_sensitivity.value.redDot} | 2X: ${_sensitivity.value.scope2x} | 4X: ${_sensitivity.value.scope4x}"
            val payload = "${_sensitivity.value.general},${_sensitivity.value.redDot},${_sensitivity.value.scope2x},${_sensitivity.value.scope4x},${_sensitivity.value.sniperScope},${_sensitivity.value.freeLook}"

            val favorite = FavoriteEntity(
                name = name,
                category = "SENSITIVITY",
                deviceName = _deviceProfile.value.fullName,
                ram = _deviceProfile.value.ram,
                summary = summary,
                payloadJson = payload
            )
            favoriteDao.insertFavorite(favorite)
            showToast("Saved '$name' to Favorites!")
        }
    }

    // ==========================================
    // FIRE BUTTON ACTIONS
    // ==========================================

    fun updateFireButtonSize(size: Int) {
        preferences.saveFireButtonSize(size)
        val current = fireButtonConfig.value
        // StateFlow will re-evaluate via preferences or directly
    }

    fun applyFireButton(size: Int) {
        preferences.saveFireButtonSize(size)
        showToast("Fire Button $size% selected.")
        _activeManualApply.value = ManualApplyTarget.FireButtonSetting(size)
    }

    // ==========================================
    // DPI ACTIONS
    // ==========================================

    fun updateCustomDpi(dpi: Int) {
        preferences.saveCustomDpi(dpi)
    }

    fun applyDpiGuidance(dpi: Int) {
        preferences.saveCustomDpi(dpi)
        showToast("DPI $dpi selected.")
        _activeManualApply.value = ManualApplyTarget.DpiSetting(dpi)
    }

    // ==========================================
    // GRAPHICS & FPS ACTIONS
    // ==========================================

    fun applyGraphicsSettings(graphics: String, fps: String) {
        showToast("Graphics ($graphics / $fps) selected.")
        _activeManualApply.value = ManualApplyTarget.GraphicsSetting(graphics, fps)
    }

    // ==========================================
    // GYROSCOPE ACTIONS
    // ==========================================

    fun applyGyroSettings(gen: Int, redDot: Int) {
        showToast("Gyroscope values selected.")
        _activeManualApply.value = ManualApplyTarget.GyroSetting(gen, redDot)
    }

    // ==========================================
    // HUD STUDIO ACTIONS
    // ==========================================

    fun selectHudFingerCount(fingers: Int) {
        _selectedFingerCount.value = fingers
        _activeHudLayout.value = HudTemplates.getLayoutByFingers(fingers)
    }

    fun updateHudControlPosition(controlId: String, newXPercent: Float, newYPercent: Float) {
        val current = _activeHudLayout.value
        val updatedControls = current.controls.map { ctrl ->
            if (ctrl.id == controlId) {
                ctrl.copy(
                    xPercent = newXPercent.coerceIn(0.05f, 0.95f),
                    yPercent = newYPercent.coerceIn(0.05f, 0.95f)
                )
            } else ctrl
        }
        _activeHudLayout.value = current.copy(controls = updatedControls)
    }

    fun updateHudControlSize(controlId: String, newSizeDp: Int) {
        val current = _activeHudLayout.value
        val updatedControls = current.controls.map { ctrl ->
            if (ctrl.id == controlId) {
                ctrl.copy(sizeDp = newSizeDp.coerceIn(40, 110))
            } else ctrl
        }
        _activeHudLayout.value = current.copy(controls = updatedControls)
    }

    fun applyHudLayout(layout: HudLayout) {
        showToast("${layout.fingerCount}-Finger HUD Layout selected.")
        _activeManualApply.value = ManualApplyTarget.HudSetting(layout.title, layout.fingerCount)
    }

    fun saveHudFavorite(customName: String? = null) {
        viewModelScope.launch {
            val layout = _activeHudLayout.value
            val name = if (!customName.isNullOrBlank()) customName else "${layout.fingerCount}-Finger Custom HUD"
            val summary = "${layout.fingerCount} Finger • ${layout.controls.size} Custom Buttons • ${layout.difficulty}"

            val favorite = FavoriteEntity(
                name = name,
                category = "HUD",
                deviceName = _deviceProfile.value.fullName,
                ram = _deviceProfile.value.ram,
                summary = summary,
                payloadJson = "finger_count:${layout.fingerCount}"
            )
            favoriteDao.insertFavorite(favorite)
            showToast("Saved HUD layout '$name' to Favorites!")
        }
    }

    fun shareHudLayout() {
        val layout = _activeHudLayout.value
        val text = buildString {
            appendLine("🎮 Be Happy FF Tools — ${layout.fingerCount}-Finger HUD Layout")
            appendLine("━━━━━━━━━━━━━━━━━━━━━")
            appendLine("Title: ${layout.title}")
            appendLine("Difficulty: ${layout.difficulty}")
            appendLine("Description: ${layout.description}")
            appendLine("Controls:")
            layout.controls.forEach { ctrl ->
                appendLine("• ${ctrl.name}: X=${(ctrl.xPercent * 100).toInt()}%, Y=${(ctrl.yPercent * 100).toInt()}%, Size=${ctrl.sizeDp}dp")
            }
            appendLine("━━━━━━━━━━━━━━━━━━━━━")
            appendLine("🛡️ Replicate manually inside Free Fire ➔ Controls ➔ Custom HUD")
        }
        shareText("Be Happy FF Tools HUD", text)
    }

    // ==========================================
    // OPTIMIZATION ACTIONS
    // ==========================================

    fun toggleOptimizationItem(itemKey: String) {
        val current = _optimizationState.value
        val updated = when (itemKey) {
            "closeBackgroundApps" -> current.copy(closeBackgroundApps = !current.closeBackgroundApps)
            "freeStorageSpace" -> current.copy(freeStorageSpace = !current.freeStorageSpace)
            "restartBeforeSession" -> current.copy(restartBeforeSession = !current.restartBeforeSession)
            "gameTurboEnabled" -> current.copy(gameTurboEnabled = !current.gameTurboEnabled)
            "batterySaverOff" -> current.copy(batterySaverOff = !current.batterySaverOff)
            "dndNotificationOn" -> current.copy(dndNotificationOn = !current.dndNotificationOn)
            "stableWifiOrData" -> current.copy(stableWifiOrData = !current.stableWifiOrData)
            "cleanScreenTouch" -> current.copy(cleanScreenTouch = !current.cleanScreenTouch)
            else -> current
        }
        _optimizationState.value = updated
        preferences.saveOptimizationChecklist(updated)

        if (updated.completedCount == updated.totalItems) {
            showToast("🎉 Optimization checklist completed! Your device is primed for Free Fire.")
        }
    }

    fun resetOptimizationChecklist() {
        val fresh = OptimizationChecklistState()
        _optimizationState.value = fresh
        preferences.saveOptimizationChecklist(fresh)
        showToast("Optimization checklist reset.")
    }

    // ==========================================
    // HEADSHOT TRAINING ACTIONS
    // ==========================================

    fun startTimer(roundNumber: Int) {
        _activeRound.value = roundNumber
        _isTimerRunning.value = true
        _timerSecondsRemaining.value = 180 // 3 minutes per round

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerSecondsRemaining.value > 0 && _isTimerRunning.value) {
                delay(1000L)
                _timerSecondsRemaining.value -= 1
            }
            if (_timerSecondsRemaining.value == 0) {
                _isTimerRunning.value = false
                completeRound(roundNumber)
                showToast("Round $roundNumber Complete! Great work.")
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun resumeTimer() {
        _isTimerRunning.value = true
        timerJob = viewModelScope.launch {
            while (_timerSecondsRemaining.value > 0 && _isTimerRunning.value) {
                delay(1000L)
                _timerSecondsRemaining.value -= 1
            }
            if (_timerSecondsRemaining.value == 0) {
                _isTimerRunning.value = false
                completeRound(_activeRound.value)
                showToast("Round ${_activeRound.value} Complete!")
            }
        }
    }

    fun completeRound(roundNumber: Int) {
        val current = _trainingState.value
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val updated = when (roundNumber) {
            1 -> current.copy(round1Completed = true, lastTrainedDate = todayStr)
            2 -> current.copy(round2Completed = true, lastTrainedDate = todayStr)
            3 -> current.copy(round3Completed = true, lastTrainedDate = todayStr)
            else -> current
        }

        val total = listOf(updated.round1Completed, updated.round2Completed, updated.round3Completed).count { it }
        val finalState = updated.copy(totalRoundsCompleted = total)
        _trainingState.value = finalState
        preferences.saveTrainingState(finalState)
        showToast("Round $roundNumber marked as completed ($total / 3 done today)")
    }

    fun hitTrainingTarget() {
        _targetHitScore.value += 1
        val randomX = (15..85).random() / 100f
        val randomY = (20..75).random() / 100f
        _targetPosition.value = Pair(randomX, randomY)

        val current = _trainingState.value
        val updated = current.copy(dragPracticeHits = current.dragPracticeHits + 1)
        _trainingState.value = updated
        preferences.saveTrainingState(updated)
    }

    fun resetTrainingProgress() {
        val reset = _trainingState.value.copy(
            round1Completed = false,
            round2Completed = false,
            round3Completed = false,
            totalRoundsCompleted = 0
        )
        _trainingState.value = reset
        preferences.saveTrainingState(reset)
        showToast("Today's training routine reset.")
    }

    private fun checkDailyStreakReset() {
        val current = preferences.getTrainingState()
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if (current.lastTrainedDate.isNotEmpty() && current.lastTrainedDate != todayStr) {
            // New day
            val resetDay = current.copy(
                round1Completed = false,
                round2Completed = false,
                round3Completed = false,
                totalRoundsCompleted = 0,
                streakDays = if (current.totalRoundsCompleted >= 2) current.streakDays + 1 else 1,
                lastTrainedDate = todayStr
            )
            _trainingState.value = resetDay
            preferences.saveTrainingState(resetDay)
        }
    }

    // ==========================================
    // FAVORITES ACTIONS
    // ==========================================

    fun loadFavorite(item: FavoriteItem) {
        when (item.category) {
            "SENSITIVITY" -> {
                try {
                    val parts = item.payloadJson.split(",").map { it.trim().toInt() }
                    if (parts.size >= 6) {
                        val preset = SensitivityPreset(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5])
                        _sensitivity.value = preset
                        preferences.saveSensitivity(preset)
                        showToast("Loaded preset: ${item.name}")
                    }
                } catch (_: Exception) {
                    showToast("Loaded favorite ${item.name}")
                }
            }
            "HUD" -> {
                val fingers = if (item.summary.contains("3 Finger")) 3 else if (item.summary.contains("4 Finger")) 4 else if (item.summary.contains("5 Finger")) 5 else 2
                selectHudFingerCount(fingers)
                showToast("Loaded HUD layout: ${item.name}")
            }
            "DEVICE" -> {
                val matched = DeviceDatabase.allDevices.firstOrNull { it.model.equals(item.name, ignoreCase = true) }
                if (matched != null) {
                    selectDevice(matched)
                } else {
                    showToast("Loaded device: ${item.name}")
                }
            }
        }
    }

    fun renameFavorite(id: Long, newName: String) {
        if (newName.isBlank()) return
        viewModelScope.launch {
            favoriteDao.renameFavorite(id, newName)
            showToast("Favorite renamed to '$newName'")
        }
    }

    fun deleteFavorite(item: FavoriteItem) {
        viewModelScope.launch {
            favoriteDao.deleteById(item.id)
            showToast("Deleted '${item.name}' from Favorites")
        }
    }

    fun saveDeviceFavorite(device: DeviceProfile) {
        viewModelScope.launch {
            val favorite = FavoriteEntity(
                name = "${device.brand} ${device.model}",
                category = "DEVICE",
                deviceName = device.fullName,
                ram = device.ram,
                summary = "${device.ram} RAM • ${device.performanceProfile.displayName} Profile • ${device.defaultDpi} DPI",
                payloadJson = "${device.brand},${device.model},${device.ram}"
            )
            favoriteDao.insertFavorite(favorite)
            showToast("Saved ${device.fullName} to Favorite Devices!")
        }
    }

    // ==========================================
    // MANUAL APPLY MODAL DISMISS
    // ==========================================

    fun dismissManualApply() {
        _activeManualApply.value = null
    }

    // ==========================================
    // UTILITIES
    // ==========================================

    private fun copyToClipboard(label: String, text: String) {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }

    private fun shareText(title: String, text: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(sendIntent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        getApplication<Application>().startActivity(chooser)
    }

    private fun showToast(msg: String) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show()
    }
}
